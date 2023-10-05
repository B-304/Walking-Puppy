import React, { useState, useEffect, useRef } from "react";
import MapView, { Polyline, Marker } from "react-native-maps";
import {
  View,
  StyleSheet,
  Image,
  Pressable,
  Text,
  TextInput,
  KeyboardAvoidingView,
  Platform,
} from "react-native";
import Octicons from "react-native-vector-icons/Octicons";
import { useNavigation } from "@react-navigation/native";
import DismissKeyboardView from "../../components/DismissKeyboardView";
import axios from "axios";
import Geolocation from "@react-native-community/geolocation";
import { accessToken } from "react-native-dotenv";
import { useRoute } from "@react-navigation/native";

const SpotSavedScreen: React.FC = (): JSX.Element => {
  const navigation = useNavigation();
  const route = useRoute();
  const walkId = route.params;

  const coordinates = [
    { latitude: 36.3463, longitude: 127.2941 },
    { latitude: 36.34604859, longitude: 127.2948598 },
    { latitude: 36.34478219, longitude: 127.2971263 },
    { latitude: 36.34238821, longitude: 127.3017015 },
    { latitude: 36.34237999, longitude: 127.3017057 },
    { latitude: 36.34244298, longitude: 127.301794 },
    { latitude: 36.34423989, longitude: 127.3065733 },
    { latitude: 36.34247574, longitude: 127.3017176 },
    { latitude: 36.34312939, longitude: 127.2992818 },
    { latitude: 36.34566924, longitude: 127.2947771 },
    { latitude: 36.34567735, longitude: 127.2947693 },
    { latitude: 36.34604859, longitude: 127.2948598 },
    { latitude: 36.3463, longitude: 127.2941 },

    // ... 기타 좌표들
  ];
  const [modalVisible, setModalVisible] = useState(false);
  const [detailModal, setDetailModal] = useState(false);
  const [saveRoute, setSaveRoute] = useState(false);
  const [elapsedSeconds, setElapsedSeconds] = useState(0);
  const [timer, setTimer] = useState<NodeJS.Timeout | null>(null);
  const [completeModal, setCompleteModal] = useState(false);
  const [saveTime, setSaveTime] = useState(false);
  const routeImg = require("../../assets/stopmap.png");
  const [latitude, setLatitude] = useState(coordinates[0].latitude + "");
  const [longitude, setLongitude] = useState(coordinates[0].longitude + "");
  const [walkTotal, setWalkTotal] = useState("");
  const mapRef = useRef(null);
  // const walkId = 57; //앞 화면에서 받아오는 데이터
  const [walkName, setWalkName] = useState("");

  useEffect(() => {
    const interval = setInterval(() => {
      setElapsedSeconds((prev) => {
        if (prev === 50 * 60) {
          // 5분이 되었을 때
          setCompleteModal(true);
          clearInterval(interval);
        }
        return prev + 1;
      });
    }, 1000);
    if (saveTime === true) {
      clearInterval(interval);
    }
    return () => clearInterval(interval); // 컴포넌트 unmount시 타이머 중지
  }, [saveTime]);

  // 20초 간격으로 현재 위치를 받아온다.
  useEffect(() => {
    const geoLocation = () => {
      Geolocation.getCurrentPosition(
        (position) => {
          const latitude_now = JSON.stringify(position.coords.latitude);
          const longitude_now = JSON.stringify(position.coords.longitude);

          setLatitude(latitude_now);
          setLongitude(longitude_now);
          console.log("=================now===================");
          console.log(latitude_now, longitude_now);
        },
        (error) => {
          console.log(error.code, error.message);
        },
        { enableHighAccuracy: true, timeout: 15000, maximumAge: 10000 },
      );
    };

    geoLocation();
    const intervalId = setInterval(geoLocation, 20000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    if (latitude && longitude && mapRef.current) {
      const newRegion = {
        latitude: Number(latitude),
        longitude: Number(longitude),
        latitudeDelta: 0.001,
        longitudeDelta: 0.001,
      };

      mapRef.current.animateToRegion(newRegion, 1000);
    }
  }, [latitude, longitude]);

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}분 ${remainingSeconds}초`;
  };

  //산책 종료
  const onConfirmPress = () => {
    setModalVisible(false);
    setDetailModal(true);
    setSaveTime(true);

    //산책 종료 axios
    // const url = "https://j9b304.p.ssafy.io/api/walk/over"; // 탈퇴 API 엔드포인트 URL로 변경해야 합니다.
    const url = "https://j9b304.p.ssafy.io/api/walk/over";
    const postdata = {
      walkId: walkId,
      distance: (elapsedSeconds / 60) * 60,
      walkCount: (elapsedSeconds / 60) * 95,
      calorie: (elapsedSeconds / 60) * 14,
      itemCount: 0,
      route: [
        {
          sequence: 1,
          latitude: latitude,
          longitude: longitude,
        },
      ],
    };
    console.log(postdata);
    axios
      .post(url, postdata, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        console.log("산책 종료/저장 성공:", response.data);
        setWalkTotal(response.data);
      })
      .catch((error) => {
        console.error("산책 종료/저장 실패", error);
        setSaveRoute(false);
      });
  };

  const onCompletePress = () => {
    setSaveTime(true);
    setCompleteModal(false);
    setDetailModal(true);
  };

  const handlenameChange = (text: string) => {
    setWalkName(text);
  };

  const saveWalking = () => {
    //산책로 저장 axios
    // const url = "https://j9b304.p.ssafy.io/api/walk/over";
    const url = "https://j9b304.p.ssafy.io/api/walk/scrap";
    const putdata = {
      walkId: walkId,
      name: walkName,
    };
    console.log(putdata);
    axios
      .put(url, putdata, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        console.log("산책로 저장 성공:", response.data);
        setSaveRoute(false);
        navigation.reset({
          index: 0,
          routes: [{ name: "홈" }],
        });
      })
      .catch((error) => {
        console.error("산책로 저장 실패", error);
      });
  };

  //여기 산책로 저장임
  // const saveWalking = async () => {
  //   try {
  //     const response = await axios.post(
  //       "https://j9b304.p.ssafy.io/api/walk/over",
  //       {
  //         walkId: 0,
  //         distance: 0,
  //         walkCount: 0,
  //         calorie: 0,
  //         itemCount: 0,
  //         route: [
  //           {
  //             sequence: 0,
  //             latitude: 0,
  //             longitude: 0,
  //           },
  //         ],
  //       },
  //       {
  //         headers: {
  //           Authorization: `Bearer ${BEARER_TOKEN}`,
  //         },
  //       },
  //     );
  //     console.log(response);
  //   } catch (error) {
  //     console.log(error, "응 에러떴어");
  //   } finally {
  //     setSaveRoute(false);
  //     navigation.reset({
  //       index: 0,
  //       routes: [{ name: "홈" }],
  //     });
  //   }
  // };
  return (
    <View style={styles.container}>
      <MapView
        ref={mapRef}
        style={styles.map}
        initialRegion={{
          latitude: coordinates[3].latitude,
          longitude: coordinates[3].longitude,
          latitudeDelta: 0.02, // 원하는 확대 수준으로 조정
          longitudeDelta: 0.017,
        }}
      >
        <Polyline
          coordinates={coordinates}
          strokeColor="#000" // 색상 설정
          strokeWidth={5} // 선 두께 설정
        />
        <Marker
          coordinate={{
            latitude: Number(latitude),
            longitude: Number(longitude),
          }}
        >
          <Image
            source={require("../../assets/dogFace.png")}
            style={{ height: 90, width: 90 }}
          />
        </Marker>
      </MapView>
      <Pressable
        style={styles.closeButton}
        onPress={() => setModalVisible(true)}
      >
        <Octicons name="stop" size={35} color="black" />
      </Pressable>
      <View style={styles.timeBox}>
        <Text style={styles.timeText}>
          {" "}
          소요시간 : {formatTime(elapsedSeconds)}
        </Text>
      </View>
      {modalVisible && (
        <Pressable
          style={styles.modalBackground}
          onPress={() => setModalVisible(false)}
        >
          <View style={styles.modalContent}>
            <View style={styles.modalTextSection}>
              <Text style={styles.modalText}>산책을 종료하시겠습니까?</Text>
            </View>
            <View style={styles.modalButtonSection}>
              <Pressable
                style={styles.cancelButton}
                onPress={() => setModalVisible(false)}
              >
                <Text style={styles.cancelButtonText}>취소</Text>
              </Pressable>
              <Pressable style={styles.endButton} onPress={onConfirmPress}>
                <Text style={styles.endButtonText}>확인</Text>
              </Pressable>
            </View>
          </View>
        </Pressable>
      )}
      {completeModal && (
        <Pressable style={styles.modalBackground}>
          <View style={styles.modalContent}>
            <View style={styles.modalTextSection}>
              <Text style={styles.modalText}>목적지 부근에 도착하여</Text>
              <Text style={styles.modalText}>산책을 종료합니다.</Text>
            </View>
            <View
              style={{
                paddingBottom: 10,
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <Pressable style={styles.endButton} onPress={onCompletePress}>
                <Text style={styles.endButtonText}>확인</Text>
              </Pressable>
            </View>
          </View>
        </Pressable>
      )}
      {detailModal && (
        <Pressable
          style={styles.modalBackground}
          onPress={() =>
            navigation.reset({
              index: 0,
              routes: [{ name: "홈" }],
            })
          }
        >
          <View style={styles.detailModal}>
            <View style={styles.modalTitle}>
              <Text style={styles.modalTitleContext}>산책 상세 기록</Text>
            </View>
            <View style={styles.routeImgContainer}>
              <Image source={routeImg} style={{ width: 290, height: 200 }} />
            </View>
            <View style={[styles.detailModalContent, { marginTop: 30 }]}>
              <View style={styles.modalDataRow}>
                <Text style={styles.dataDot}>•</Text>
                <Text style={styles.modalDataLabel}>소요시간</Text>
                <Text style={styles.modalDataValue}>{walkTotal.time}분</Text>
              </View>
              <View style={styles.modalDataRow}>
                <Text style={styles.dataDot}>•</Text>
                <Text style={styles.modalDataLabel}>걸음 수</Text>
                <Text style={styles.modalDataValue}>
                  {walkTotal.walkCount} 보
                </Text>
              </View>
              <View style={styles.modalDataRow}>
                <Text style={styles.dataDot}>•</Text>
                <Text style={styles.modalDataLabel}>테마</Text>
                <Text style={styles.modalDataValue}>{walkTotal.themeName}</Text>
              </View>
              <View style={styles.modalDataRow}>
                <Text style={styles.dataDot}>•</Text>
                <Text style={styles.modalDataLabel}>칼로리</Text>
                <Text style={styles.modalDataValue}>
                  {walkTotal.calorie} kcal
                </Text>
              </View>
              <View style={styles.modalDataRow}>
                <Text style={styles.dataDot}>•</Text>
                <Text style={styles.modalDataLabel}>이동거리</Text>
                <Text style={styles.modalDataValue}>
                  {walkTotal.distance} km
                </Text>
              </View>
            </View>

            <View>
              <Text style={{ fontSize: 12, marginBottom: 10 }}>
                저장 시 나의 보관함에서 해당 경로로 산책을 할 수 있습니다.
              </Text>
            </View>
            <View style={styles.detailModalButtonSection}>
              <Pressable
                style={styles.cancelButton}
                onPress={() =>
                  navigation.reset({
                    index: 0,
                    routes: [{ name: "홈" }],
                  })
                }
              >
                <Text style={styles.cancelButtonText}>홈으로</Text>
              </Pressable>
              <Pressable
                style={styles.endButton}
                onPress={() => {
                  setDetailModal(false);
                  setSaveRoute(true);
                }}
              >
                <Text style={styles.endButtonText}>저장</Text>
              </Pressable>
            </View>
          </View>
        </Pressable>
      )}
      {saveRoute && (
        <KeyboardAvoidingView
          behavior={Platform.OS === "ios" ? "padding" : "height"}
          style={{ flex: 1 }}
        >
          <Pressable
            style={styles.modalBackground}
            onPress={() => setSaveRoute(false)}
          >
            <View
              style={styles.saveRouteModal}
              onStartShouldSetResponder={() => true}
            >
              <View style={styles.modalTextSection}>
                <Text style={styles.modalText}>산책로 저장</Text>
              </View>
              <View style={{ paddingLeft: 20 }}>
                <Text
                  style={{ fontSize: 16, color: "black", fontWeight: "bold" }}
                >
                  산책로 이름
                </Text>
              </View>
              <View style={{ marginHorizontal: 15 }}>
                <View
                  style={{
                    paddingLeft: 20,
                    borderWidth: 1,
                    borderColor: "#E8E8E8",
                    backgroundColor: "#F6F6F6",
                    paddingVertical: 1,
                    paddingHorizontal: 2,
                    borderRadius: 4,
                    marginBottom: 12,
                    marginHorizontal: 5,
                    marginTop: 10,
                  }}
                >
                  <TextInput
                    placeholder="산책로 이름을 입력하세요."
                    value={walkName}
                    onChangeText={handlenameChange}
                  />
                </View>
              </View>

              <View style={styles.modalButtonSection}>
                <Pressable style={styles.cancelButton}>
                  <Text
                    style={styles.cancelButtonText}
                    onPress={() =>
                      navigation.reset({
                        index: 0,
                        routes: [{ name: "홈" }],
                      })
                    }
                  >
                    홈으로
                  </Text>
                </Pressable>
                <Pressable style={styles.endButton} onPress={saveWalking}>
                  <Text style={styles.endButtonText}>저장</Text>
                </Pressable>
              </View>
            </View>
          </Pressable>
        </KeyboardAvoidingView>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  timeBox: {
    width: 300,
    height: 70,
    backgroundColor: "#ffffff",
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
    position: "absolute",
    top: 10, // 위치를 조절하세요
    left: "50%",
    transform: [{ translateX: -150 }],
  },
  timeText: {
    fontSize: 20,
    fontWeight: "bold",
    color: "black",
  },
  closeButton: {
    position: "absolute",
    bottom: 15,
    right: 17,
    width: 65,
    height: 65,
    backgroundColor: "white",
    borderRadius: 32.5,
    justifyContent: "center",
    alignItems: "center",
    elevation: 5, // Android shadow
    shadowOffset: { width: 0, height: 3 }, // iOS shadow
    shadowOpacity: 0.3, // iOS shadow
    shadowRadius: 4, // iOS shadow
  },
  routeImgContainer: {},
  modalBackground: {
    position: "absolute",
    backgroundColor: "rgba(0,0,0,0.5)",
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,
  },
  modalContent: {
    position: "absolute",
    backgroundColor: "#ffffff",
    top: 250,
    bottom: 240,
    left: 16,
    right: 16,
    elevation: 5, // Android shadow
    borderRadius: 15,
  },
  saveRouteModal: {
    position: "absolute",
    backgroundColor: "#ffffff",
    top: 215,
    bottom: 215,
    left: 16,
    right: 16,
    elevation: 5, // Android shadow
    borderRadius: 15,
  },
  modalTextSection: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  detailModalButtonSection: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingBottom: 20,
  },
  modalButtonSection: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingHorizontal: 38, // 버튼을 모달 경계에서 떨어뜨리기 위한 패딩
    paddingBottom: 30,
  },
  modalText: {
    fontSize: 20,
    color: "black",
    fontWeight: "bold",
  },
  cancelButton: {
    width: 140,
    height: 40,

    justifyContent: "center",
    alignItems: "center",
    borderColor: "#4B9460",
    backgroundColor: "#FFFFFF",
    color: "#4B9460",
    borderWidth: 1,
    borderRadius: 5,
  },
  cancelButtonText: {
    color: "#4B9460",
    fontWeight: "bold",
  },
  endButton: {
    width: 140,
    height: 40,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#4B9460",
    borderRadius: 5,
  },
  endButtonText: {
    color: "#FFFFFF",
    fontWeight: "bold",
  },
  detailModal: {
    position: "absolute",
    backgroundColor: "#ffffff",
    top: 60,
    bottom: 60,
    left: 16,
    right: 16,
    elevation: 5, // Android shadow
    borderRadius: 15,
    paddingLeft: 33,
    paddingRight: 28,
  },
  modalDataRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingVertical: 5,
  },
  dataDot: {
    width: 7,
    height: 7,
    borderRadius: 3.5,
    backgroundColor: "#4B9460",
    marginRight: 8,
  },
  modalDataLabel: {
    color: "black",
    flex: 2,
    fontSize: 16,
    fontWeight: "bold",
  },
  modalDataValue: {
    flex: 3,
    fontSize: 16,
    textAlign: "left",
    color: "black",
  },
  detailModalContent: {
    flex: 5,
  },
  modalTitle: {
    justifyContent: "center",
    alignItems: "center",
    paddingVertical: 26,
  },
  modalTitleContext: {
    fontSize: 22,
    color: "black",
    fontWeight: "bold",
  },
});

export default SpotSavedScreen;
