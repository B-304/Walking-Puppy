import { View, Text, Image, Button, TouchableOpacity } from "react-native";
import React, { useState, useEffect } from "react";
import axios from "axios";
import { Calendar } from "react-native-calendars";
import Modal from "react-native-modal";
import FontAwesome from "react-native-vector-icons/FontAwesome";
import Ionicons from "react-native-vector-icons/Ionicons";
import { useNavigation } from "@react-navigation/native";
import { accessToken, kakao_api } from "react-native-dotenv";
import { useFocusEffect } from "@react-navigation/native";

const MyPage: React.FC = (): JSX.Element => {
  const [name, setName] = useState(null);
  const [walkList, setWalkList] = useState([]);
  const [userData, setUserData] = useState(null);
  const [isModalVisible, setModalVisible] = useState(false);
  const [selectedDate, setSelectedDate] = useState("");
  const date: Date = new Date();
  const inityear = date.getFullYear();
  const initmonth = date.getMonth() + 1;
  const [currentYear, setCurrentYear] = useState(inityear); // 현재 년도
  const [currentMonth, setCurrentMonth] = useState(initmonth); // 현재 달
  const [selectedItem, setSelectedItem] = useState(null);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const [selectedModalWalk, setSelectedModalWalk] = useState("");
  const [departureAddress, setDepartureAddress] = useState("유성구 덕명동");
  const [arrivalAddress, setArrivalAddress] = useState("유성구 덕명동");
  const navigation = useNavigation();

  const Profile = () => {
    navigation.navigate("회원정보 수정");
  };

  const fetchWalkList = (year: number, month: number) => {
    const walkurl =
      "https://j9b304.p.ssafy.io/api/walk-list?yearAndMonth=" +
      year +
      "-" +
      month;
    console.log(walkurl);
    axios
      .get(walkurl, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        console.log("Response from server:", response);
        setWalkList(response.data);
        console.log(year, month);
        console.log(walkList);
      })
      .catch((error) => {
        console.log(year, month);
        console.log("!!!!!!!!!!!!!!!!!!!!!!!!!실패 ")
        console.error("데이터 가져오기 실패:", error);
        setWalkList([]);
      });
  };
  // useEffect(() => {
  //   axios
  //     .get("https://j9b304.p.ssafy.io/api/2")
  //     //.get("http://10.0.2.2:8080/2")
  //     .then((response) => {
  //       console.log(response.data);
  //       setName(response.data.nickname);
  //       fetchWalkList(new Date().getFullYear(), new Date().getMonth() + 1);
  //     })
  //     .catch((error) => {
  //       console.error("데이터 요청 실패:", error);
  //     });
  // }, []);

  useFocusEffect(
    React.useCallback(() => {
      fetchData();
    }, []),
  );

  const fetchData = () => {
    axios
      .get("https://j9b304.p.ssafy.io/api/2")
      .then((response) => {
        console.log(response.data);
        setName(response.data.nickname);
        fetchWalkList(new Date().getFullYear(), new Date().getMonth() + 1);
      })
      .catch((error) => {
        console.error("사용자 데이터 요청 실패:", error);
      });
  };

  const handleMonthChange = (monthData) => {
    const { year, month } = monthData;
    setCurrentMonth(month);
    setCurrentYear(year);
  };

  useEffect(() => {
    fetchWalkList(currentYear, currentMonth);
  }, [currentMonth, currentYear]);

  //모달 관련

  const toggleModal = (item, date) => {
    if (item && item.walkIdList && item.walkIdList.length > 0) {
      console.log(item);
      console.log(date); // 추가: date 출력
      setSelectedItem(item); // 선택된 아이템 설정
      setModalVisible(!isModalVisible);
      setSelectedDate(item.day);
      console.log("==========");
      console.log(currentMonth);
      console.log(item);
      console.log(item.day);
      console.log(date);
    }
  };
  const handleConfirmButtonClick = () => {
    // 필요한 처리 작성...
    setSelectedItem(null); // 선택된 아이템 설정
    setModalVisible(!isModalVisible);
    setSelectedDate("");
    setModalVisible(false);
    setSelectedModalWalk("");
  };

  useEffect(() => {
    if (isModalVisible && selectedItem) {
      const walkId = selectedItem.walkIdList[selectedIndex];
      axios
        .get(`https://j9b304.p.ssafy.io/api/walk/${walkId}`, {
          // .get("http://10.0.2.2:8080/walk/10", {
          headers: { Authorization: `Bearer ${accessToken}` },
        })
        .then((response) => {
          console.log(response.data);
          setSelectedModalWalk(response.data);
        })
        .catch((error) => {
          console.error("데이터 요청 실패:", error);
        });
    }
  }, [isModalVisible, selectedItem, selectedIndex]);

  // 오른쪽 화살표 클릭 이벤트 핸들러 추가
  const handleRightArrowClick = () => {
    if (selectedItem && selectedItem.walkIdList.length > selectedIndex + 1) {
      setSelectedIndex(selectedIndex + 1);
    }
  };

  // 왼쪽 화살표 클릭 이벤트 핸들러 추가
  const handleLeftArrowClick = () => {
    if (selectedIndex > 0) {
      setSelectedIndex(selectedIndex - 1);
    }
  };

  // 캘린더 클릭 이벤트 핸들러 수정
  const handleDayPress = (date) => {
    const todayWalkItem = walkList.find(
      (item) => item.day === dparseInt(date.day, 10) && state != "disabled",
    ); // 현재 날짜와 일치하는 항목 찾기

    if (todayWalkItem) {
      toggleModal(todayWalkItem, date);
      setSelectedDate(date.dateString); // 선택된 날짜 설정
      setSelectedIndex(0); // 인덱스 초기화
    }
  };

  const getGeoAddress = async (latitude, longitude) => {
    // const apiKey = "";
    const url = `https://dapi.kakao.com/v2/local/geo/coord2address.json?x=${longitude}&y=${latitude}`;

    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `KakaoAK ${kakao_api}`,
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

  useEffect(() => {
    if (selectedModalWalk) {
      console.log(
        selectedModalWalk.startLatitude,
        selectedModalWalk.startLongitude,
      );
      // 출발지 위경도 값으로 주소 가져오기
      getGeoAddress(
        selectedModalWalk.startLatitude,
        selectedModalWalk.startLongitude,
      )
        .then((address) => setDepartureAddress(address))
        .catch((error) => console.error(error));

      // 도착지 위경도 값으로 주소 가져오기
      getGeoAddress(
        selectedModalWalk.endLatitude,
        selectedModalWalk.endLongitude,
      )
        .then((address) => setArrivalAddress(address))
        .catch((error) => console.error(error));
    }
  }, [selectedModalWalk]);

  return (
    <View style={{ flex: 1, justifyContent: "center", alignItems: "center" }}>
      <TouchableOpacity onPress={Profile} style={styles.grayBox}>
        <Image
          source={require("./assets/image-user.png")}
          style={styles.image}
        />
        <Text style={styles.nameStyle}>{name}</Text>
        <Text style={styles.suffixStyle}> 님</Text>
        <View style={{ flex: 1, alignItems: "flex-end" }}>
          <Ionicons name="chevron-forward" size={35} color="#616161" />
        </View>
      </TouchableOpacity>

      <View style={styles.contentContainer}>
        <Text style={styles.middleTitle}>월별 산책 기록</Text>
        <View style={styles.calcontainer}>
          <Calendar
            onMonthChange={handleMonthChange}
            dayComponent={({ date, state }) => {
              // const todayWalkItem = walkList.find();
              const todayWalkItem = walkList.find(
                (item) => item.day == date.day && state != "disabled",
              ); // 현재 날짜와 일치하는 항목 찾기

              const today = date.month + "월 " + date.day + "일";
              // console.log(today);
              return (
                <TouchableOpacity
                  onPress={() => toggleModal(todayWalkItem, today)}
                >
                  <View>
                    <Text
                      style={{
                        textAlign: "center",
                        color:
                          state === "disabled"
                            ? "gray"
                            : date.day == new Date().getDate()
                            ? "#4B9460"
                            : "black",
                      }}
                    >
                      {date.day}
                    </Text>
                    {todayWalkItem ? (
                      todayWalkItem.achieved ? (
                        <FontAwesome
                          name="paw"
                          size={30}
                          color="#D9F489"
                          style={{ textAlign: "center" }}
                        />
                      ) : (
                        <FontAwesome
                          name="paw"
                          size={30}
                          color="#D9F489"
                          style={{ textAlign: "center", opacity: 0.3 }}
                        />
                      )
                    ) : (
                      <View style={{ width: 30, height: 30 }} /> // FontAwesome 아이콘과 동일한 크기의 빈 View 컴포넌트
                    )}

                    {todayWalkItem ? (
                      <Text style={styles.calText}>
                        {todayWalkItem.walkCount}보
                      </Text>
                    ) : (
                      <View style={{ height: "auto" }} /> // 높이를 'auto'로 설정하여 내용이 없을 때는 공백으로 표시
                    )}
                  </View>
                </TouchableOpacity>
              );
            }}
          />
        </View>
      </View>
      <Modal isVisible={isModalVisible}>
        <View
          style={{
            // width: 328,
            height: 538,
            backgroundColor: "#fff",
            alignItems: "center",
            // justifyContent: "center",
          }}
        >
          <Text
            style={[styles.middleTitle, { marginTop: 20, marginBottom: 10 }]}
          >
            {currentMonth}월 {selectedDate}일 산책기록
          </Text>
          <View
            style={{
              flexDirection: "row",
              justifyContent: "center",
              alignItems: "center",
              marginBottom: 10,
            }}
          >
            {/* <Ionicons name="chevron-back" size={30} color="#616161" /> */}

            <TouchableOpacity onPress={handleLeftArrowClick}>
              <Ionicons name="chevron-back" size={30} color="#616161" />
            </TouchableOpacity>
            <Image
              source={require("./assets/walkroute.png")}
              style={{ width: 280, height: 180 }}
            />

            {/* <Ionicons name="chevron-forward" size={30} color="#616161" /> */}

            <TouchableOpacity onPress={handleRightArrowClick}>
              <Ionicons name="chevron-forward" size={30} color="#616161" />
            </TouchableOpacity>
          </View>
          <View
            style={{
              flexDirection: "row",
              justifyContent: "flex-end",
              width: "100%",
              marginRight: 50,
            }}
          >
            <Ionicons
              name="share-outline"
              size={30}
              color="#616161"
              style={{ marginRight: 5 }}
            />
            <Ionicons name="bookmark-outline" size={30} color="#616161" />
          </View>
          <View
            style={{
              flexDirection: "row",
              justifyContent: "space-between",
              width: "80%",
              alignSelf: "center",
              marginTop: 10,
            }}
          >
            <View>
              <Text style={styles.modalInnerTitleText}>소요 시간</Text>
              <Text style={styles.modalInnerText}>
                {selectedModalWalk.time}분
              </Text>
            </View>
            <View>
              <Text style={styles.modalInnerTitleText}>걸음 수</Text>
              <Text style={styles.modalInnerText}>
                {selectedModalWalk.walkCount}보
              </Text>
            </View>
            <View>
              <Text style={styles.modalInnerTitleText}>테마</Text>
              <Text style={styles.modalInnerText}>
                {selectedModalWalk.themeName}
              </Text>
            </View>
          </View>
          <View
            style={{
              width: "90%",
              alignSelf: "center",
            }}
          >
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-between",
                alignSelf: "center",
                width: "80%",
                marginTop: 10,
              }}
            >
              <View style={{ width: "50%", marginLeft: 20 }}>
                <Text style={styles.modalInnerTitleText}>출발지</Text>
                <Text style={styles.modalInnerText}>{departureAddress}</Text>
              </View>
              <View style={{ width: "50%" }}>
                <Text style={styles.modalInnerTitleText}>도착지</Text>
                <Text style={styles.modalInnerText}>{arrivalAddress}</Text>
              </View>
            </View>
          </View>
          <View
            style={{
              width: "90%",
              alignSelf: "center",
              marginBottom: 10,
            }}
          >
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-between",
                alignSelf: "center",
                width: "80%",
                marginTop: 10,
              }}
            >
              <View style={{ width: "50%", marginLeft: 20 }}>
                <Text style={styles.modalInnerTitleText}>출발 시간</Text>
                <Text style={styles.modalInnerText}>
                  {selectedModalWalk.startTime}
                </Text>
              </View>
              <View style={{ width: "50%" }}>
                <Text style={styles.modalInnerTitleText}>도착 시간</Text>
                <Text style={styles.modalInnerText}>
                  {selectedModalWalk.endTime}
                </Text>
              </View>
            </View>
          </View>
          {/* <Button title="Hide modal" onPress={toggleModal} /> */}
          <TouchableOpacity
            style={styles.button}
            onPress={handleConfirmButtonClick}
          >
            <Text style={styles.buttonText}>확 인</Text>
          </TouchableOpacity>
        </View>
      </Modal>
    </View>
  );
};

const styles = {
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  grayBox: {
    flexDirection: "row",
    height: 77,
    width: "96%",
    marginLeft: 10,
    marginTop: 50,
    backgroundColor: "rgba(217,217,217,0.29)",
    borderRadius: 20,
    alignItems: "center", // 이미지와 텍스트 중앙 정렬을 위해 추가
    justifyContent: "center", // 여기를 추, // 이미지와 텍스트 중앙 정렬을 위해 추가
    paddingHorizontal: 10, // 내부 여백 조정을 위해 추가
  },
  image: {
    // position: "absolute",
    // left: "10%",
    // right: "80.72%",
    // top: "15.03%",
    // bottom: "83.39%",
    width: 50,
    height: 50,
    marginRight: 20,
  },
  nameStyle: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "700",
    fontSize: 20,
    lineHeight: 24,
    color: "#616161",
  },
  suffixStyle: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "300",
    fontSize: 20,
    lineHeight: 24,
  },
  calText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "300",
    fontSize: 10,
    textAlign: "center",
    // lineHeight: 24,
  },
  modImage: {
    // position: "absolute",
    // top: "15.03%",
    // bottom: "83.39%",
    // // width: 50,
    // // height: 50,
    position: "absolute",
    right: 20,
    // resizeMode: "contain",
  },
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
  calendar: {
    // borderBottomWidth: 1,
    // borderBottomColor: "#e0e0e0",
  },
  calcontainer: {
    width: "96%",
    alignSelf: "center",
  },
  contentContainer: {
    // flex: 1,
    width: "96%",
    alignSelf: "center",
    paddingTop: 30, // 위 여백 추가
    paddingBottom: 20, // 아래 여백 추가
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
  button: {
    width: 129,
    height: 39,
    backgroundColor: "#4B9460",
    borderRadius: 10,
    justifyContent: "center", // 텍스트 중앙 정렬 (상하)
    alignItems: "center", // 텍스트 중앙 정렬 (좌우)
  },
  buttonText: {
    fontFamily: "Inter",
    fontStyle: "normal",
    fontWeight: "600",
    fontSize: 13,
    lineHeight: 13,
    textAlign: "center",
    letterSpacing: 0.2,
    color: "#FFFFFF",
  },
};

export default MyPage;
