import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  Image,
  Button,
  TouchableOpacity,
  Modal,
} from "react-native";
import axios from "axios";
import { StyleSheet } from "react-native";
import Config from "react-native-config";
import Ionicons from "react-native-vector-icons/Ionicons";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/reducer";
import { setWalkId as setNewWalkId } from "../../redux/action/walkAction";
import { useRoute } from '@react-navigation/native';

const RouteDetail: React.FC = (): JSX.Element => {
  const [walk, setWalk] = useState([]);
  const [departureAddress, setDepartureAddress] = useState("유성구 덕명동");
  const [arrivalAddress, setArrivalAddress] = useState("유성구 덕명동");
  const [modalVisible, setModalVisible] = useState(false); // 모달 상태 추가
  const route = useRoute();
  const walkId = route.params;

  const BEARER_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbWluMzY3MkBuYXZlci5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwidHlwZSI6IkFDQ0VTUyIsInVzZXJJZCI6MiwiZXhwIjoxNjk2NjA4NDI2fQ.V6oAsUPGAMxYomvzX25Hny1z1RaJFJMLYXvSizEsyY4";

  const walkIdTest = useSelector((state: RootState) => state.walk.walkId);
  const dispatch = useDispatch();

  useEffect(() => {
    //const walkId = 13;
    console.log("walkIdTest=" + walkIdTest);
    console.log(walkId)
    axios
      .get(`https://j9b304.p.ssafy.io/api/walk/${walkId}`, {
        //   .get("http://10.0.2.2:8080/walk/13", {
        headers: { Authorization: `Bearer ${BEARER_TOKEN}` },
      })
      .then((response) => {
        console.log("산책 상세");
        console.log(response.data);
        setWalk(response.data);

        getGeoAddress(walk.startLatitude, walk.startLongitude)
          .then((address) => setDepartureAddress(address))
          .catch((error) => console.error(error));

        // 도착지 위경도 값으로 주소 가져오기
        getGeoAddress(walk.endLatitude, walk.endLongitude)
          .then((address) => setArrivalAddress(address))
          .catch((error) => console.error(error));
      })
      .catch((error) => {
        console.error("데이터 요청 실패:", error);
      });
  }, []);

  const getGeoAddress = async (latitude, longitude) => {
    const apiKey = "";
    const url = `https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${longitude}&y=${latitude}`;

    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `KakaoAK ${Config.kakaoApiKey}`,
        },
      });

      console.log(url);
      if (response.data.meta.total_count > 0) {
        // 주소 정보 가져오기
        // const address = response.data.documents[0].address_name;
        const region2DepthName =
          response.data.documents[0].address.region_2depth_name;
        const region3DepthName =
          response.data.documents[0].address.region_3depth_name;

        return region2DepthName + ", " + region3DepthName;
      } else {
        throw new Error("Unable to fetch the address.");
      }
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  const toggleModal = () => {
    console.log(walk.walkId);
    setModalVisible(!modalVisible);
    console.log("==========");
  };

  const handleDeleteRoute = () => {
    const walkId = walk.walkId;
    // const url = "https://j9b304.p.ssafy.io/api/walk/${walkId}"; // 탈퇴 API 엔드포인트 URL로 변경해야 합니다.
    const url = `http://10.0.2.2:8080/walk/${walkId}`;
    axios
      .delete(url, {
        headers: { Authorization: `Bearer ${BEARER_TOKEN}` },
      })
      .then((response) => {
        // 탈퇴 성공 시의 처리
        console.log("산책 삭제 성공:", response.data);
        setModalVisible(!modalVisible);
        setWalk("");
        //여기서 목록화면으로 가야함
      })
      .catch((error) => {
        // 탈퇴 실패 시의 처리
        console.error("산책 삭제 실패", error);
      });
  };

  const newWalkExistPath = () => {
    const walkId = walk.walkId;
    // const url = "https://j9b304.p.ssafy.io/api/walk/${walkId}"; // 탈퇴 API 엔드포인트 URL로 변경해야 합니다.
    const url = "http://10.0.2.2:8080/walk/exist-path";
    axios
      .post(
        url,
        { walkId: `${walkId}` },
        { headers: { Authorization: `Bearer ${BEARER_TOKEN}` } },
      )
      .then((response) => {
        console.log("기존경로로 산책:", response.data);
        const newWalkId = response.data.walkId;
        console.log("newWalkId", newWalkId);
        dispatch(setNewWalkId(newWalkId));

        //여기서 화면 이동 AR로
      })
      .catch((error) => {
        // 탈퇴 실패 시의 처리
        console.error("기존 경로로 산책 시작 실패", error);
      });
  };

  return (
    <View style={{ backgroundColor: "white", height: 800 }}>
      <Modal
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => {
          setModalVisible(!modalVisible);
        }}
      >
        <View style={styles.centeredView}>
          <View style={styles.modalView}>
            <Text style={styles.modalText}>산책로 삭제</Text>
            <Text style={styles.modalComm}>
              정말 산책로를 삭제하시겠습니까?
              {"\n"}
              삭제하시면 저장 목록에서 확인하실 수 없습니다.
            </Text>
            <View style={styles.modalsmallButton}>
              <TouchableOpacity
                style={styles.modalButton}
                onPress={() => {
                  setModalVisible(!modalVisible);
                }}
              >
                <Text style={styles.modalButtonText}>취소</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.modalButton}
                onPress={handleDeleteRoute}
              >
                <Text style={styles.modalButtonText}>확인</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      <View
        style={{
          marginLeft: 20,
          marginRight: 20,
          alignItems: "center",
          marginTop: 20,
        }}
      >
        <Image
          source={require("../../assets/walkroute.png")}
          style={{ width: 350, height: 207 }}
        />
      </View>

      <View
        style={{
          marginLeft: 20,
          marginRight: 20,
          marginTop: 20,
          marginBottom: 10,
          backgroundColor: "#F5F5F5",
          height: 320,
        }}
      >
        <View
          style={{
            marginRight: 20,
            marginLeft: 20,
            flexDirection: "row",
            alignItems: "center",
            justifyContent: "space-between",
            marginTop: 20,
            marginBottom: 10,
          }}
        >
          <Text style={[styles.middleTitle]}>{walk.name || "산책 제목"}</Text>

          <TouchableOpacity onPress={toggleModal}>
            <Ionicons name="trash" size={23} color="#616161" />
          </TouchableOpacity>
        </View>
        <View
          style={{
            flexDirection: "row",
            justifyContent: "space-between",
            width: "70%",
            alignSelf: "center",
            marginTop: 10,
          }}
        >
          <View>
            <Text style={styles.modalInnerTitleText}>소요 시간</Text>
            <Text style={styles.modalInnerText}>{walk.time}분</Text>
          </View>
          <View>
            <Text style={styles.modalInnerTitleText}>걸음 수</Text>
            <Text style={styles.modalInnerText}>{walk.walkCount}보</Text>
          </View>
          <View>
            <Text style={styles.modalInnerTitleText}>테마</Text>
            <Text style={styles.modalInnerText}>{walk.themeName}</Text>
          </View>
        </View>

        <View
          style={{
            flexDirection: "row",
            justifyContent: "space-between",
            width: "70%",
            alignSelf: "center",
            marginTop: 20,
          }}
        >
          <View>
            <Text style={styles.modalInnerTitleText}>총 길이</Text>
            <Text style={styles.modalInnerText}>{walk.distance}km</Text>
          </View>
          <View>
            <Text style={styles.modalInnerTitleText}>소모 칼로리</Text>
            <Text style={styles.modalInnerText}>{walk.calorie}kcal</Text>
          </View>
        </View>
        <View
          style={{
            flexDirection: "row",
            justifyContent: "space-between",
            width: "70%",
            alignSelf: "center",
            marginTop: 20,
          }}
        >
          <View>
            <Text style={styles.modalInnerTitleText}>출발지</Text>
            <Text style={styles.modalInnerText}>
              {arrivalAddress || "유성구 덕명동"}
            </Text>
          </View>
          <View>
            <Text style={styles.modalInnerTitleText}>도착지</Text>
            <Text style={styles.modalInnerText}>
              {departureAddress || "유성구 덕명동"}
            </Text>
          </View>
        </View>
      </View>
      <View style={{ alignItems: "center", marginTop: 10 }}>
        <TouchableOpacity onPress={newWalkExistPath} style={styles.button}>
          <Text style={styles.buttonText}>산책 시작하기</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = {
  middleTitle: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "700",
    fontSize: 20,
    lineHeight: 24,
    color: "#616161",
    marginBottom: 10,
    textAlign: "left",
    marginLeft: 10,
  },
  modalInnerTitleText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "700",
    fontSize: 16,
    lineHeight: 19,
    color: "#616161",
    marginVertical: 5,
  },
  modalInnerText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "400",
    fontSize: 15,
    lineHeight: 18,
    color: "#616161",
    marginVertical: 5,
  },
  threeColText: {
    marginLeft: 40,
    marginRight: 40,
  },
  button: {
    width: 295,
    height: 53,
    alignItems: "center",
    backgroundColor: "#4B9460",
    borderRadius: 10,
    justifyContent: "center",
  },
  buttonText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "700",
    fontSize: 20,
    lineHeight: 28,
    textAlign: "center",
    letterSpacing: 0.1,
    color: "#FFFFFF",
    marginTop: 12,
    marginBottom: 12,
    letterSpacing: 1.5,
  },
  centeredView: {
    width: 500,
    height: 800,
    backgroundColor: "rgba(0,0,0,0.5)",
  },
  modalView: {
    width: 328,
    height: 234,
    marginTop: 250,
    backgroundColor: "#FFFFFF",
    borderRadius: 15,
    marginLeft: 35,
  },
  modalText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "700",
    fontSize: 20,
    lineHeight: 24,
    marginTop: 25,
    marginLeft: 120,
  },
  modalComm: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "600",
    fontSize: 16,
    lineHeight: 24,
    marginTop: 20,
    marginLeft: 18,
    marginRight: 15,
  },
  modalButton: {
    width: 130,
    height: 40,
    backgroundColor: "#FF8E4F",
    marginTop: 35,
    marginLeft: 24,
    borderRadius: 10,
    borderColor: "#C4C4C4",
    borderWidth: 1.5,
  },
  modalButtonText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "500",
    fontSize: 17,
    lineHeight: 19,
    textAlign: "center",
    color: "#FFFFFF",
    marginTop: 10,
    letterSpacing: 1.5,
  },
  modalsmallButton: {
    flexDirection: "row",
  },
};

export default RouteDetail;
