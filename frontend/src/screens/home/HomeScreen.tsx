import { View, Text, TouchableOpacity, Image, StyleSheet } from "react-native";
import React, { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { userActions } from "../../redux/reducer/userSlice";

import axios from "axios";
import { RootState } from "../../redux/reducer";
import { useNavigation } from "@react-navigation/native";
import { accessToken } from "react-native-dotenv";

const HomeScreen: React.FC = (): JSX.Element => {
  const [responseData, setResponseData] = useState<any>(null);
  const [dogResponseData, setDogResponseData] = useState<any>({
    name: "",
    dayCount: 0,
    dogLevel: 0,
    exp: 0,
    levelRange: 0,
  });
  const [loading, setLoading] = useState(true);
  const [weatherData, setWeatherData] = useState<any>(null);
  const navigation = useNavigation();

  useEffect(() => {
    axios
      .get("https://j9b304.p.ssafy.io/api/dog/2", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setDogResponseData(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("강아지 데이터 가져오기 실패:", error);
        setLoading(false);
      });

    axios
      .get("https://j9b304.p.ssafy.io/api/walk/today", {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })
      .then((response) => {
        setResponseData(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("산책 데이터 가져오기 실패:", error);
        setLoading(false);
      });

    const lat = 36.35572;
    const lon = 127.346064;
    const API_KEY = "";
    const apiUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}`;

    axios
      .get(apiUrl)
      .then((response) => {
        setWeatherData(response.data);
      })
      .catch((error) => {
        console.error("날씨 데이터를 가져오는데 실패했습니다:", error);
      });
  }, []);

  const nickname = useSelector(
    (state: RootState) => state.user.user.nickname as string,
  );

  const handleWalkButtonClick = () => {
    navigation.navigate("산책");
  };

  const dailyWalkData = responseData;
  const DogData = dogResponseData;

  return (
    <View style={styles.container}>
      {loading ? ( // 데이터 로딩 중인 경우 "로딩 중" 메시지 표시
        <Text>Loading...</Text>
      ) : (
        <View style={styles.dataContainer}>
          <Text style={styles.title}>{nickname} 님, 안녕하세요</Text>
          <Text style={styles.greetingText}>오늘의 산책 기록</Text>

          <View style={styles.horizontalTextContainer}>
            <View style={styles.textWithDivider}>
              <Text style={styles.walkdataText}>
                {" "}
                걸음수 {"\n\n"} {dailyWalkData ? dailyWalkData.walkCount : 0}{" "}
                걸음
              </Text>
            </View>
            <View style={styles.textWithDivider}>
              <Text style={styles.walkdataText}>
                {" "}
                이동거리 {"\n\n"} {dailyWalkData ? dailyWalkData.distance : 0}{" "}
                km
              </Text>
            </View>
            <View style={styles.textWithDivider2}>
              <Text style={styles.walkdataText}>
                {" "}
                칼로리 {"\n\n"} {dailyWalkData ? dailyWalkData.calorie : 0} kcal
              </Text>
            </View>
          </View>
          <View style={styles.dogText}>
            <Text style={styles.dogDataNameText}>
              {DogData && DogData.name}
            </Text>
            <Text style={styles.dogdataText}>와 만난 지</Text>
            <Text style={styles.dogDataDayText}> {DogData.dayCount}</Text>
            <Text style={styles.dogdataText}>일 째</Text>
          </View>
          <Image
            source={require("../../assets/dog_test.png")}
            style={styles.dogImage}
          />
          <View style={styles.dogLevelText}>
            <Text style={styles.levelText}>Lv.{DogData.dogLevel}</Text>
            <Text style={styles.expText}>
              {DogData.exp} / {DogData.levelRange} EXP
            </Text>
          </View>
          <TouchableOpacity
            style={styles.buttonStyle}
            onPress={handleWalkButtonClick}
          >
            <Text style={styles.buttonText}>산책 시작하기</Text>
          </TouchableOpacity>
          {/* <View style={styles.weather}>
            <Text style={styles.city}>{weatherData.name}</Text>
            <Text style={styles.weatherDescription}>
              {weatherData.weather[0].description}
            </Text>
            <Text style={styles.temperature}>
              {Math.round(weatherData.main.temp - 273.15)}°C
            </Text>
          </View> */}
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 0,
    backgroundColor: "#f8f8f8",
  },
  dataContainer: {
    marginBottom: 20,
    padding: 15,
    backgroundColor: "#ffffff",
  },
  title: {
    fontSize: 20,
    fontWeight: "bold",
    marginBottom: 10,
  },
  dogDataNameText: {
    fontSize: 17,
    fontWeight: "bold",
    textAlign: "center",
  },
  dogDataDayText: {
    fontSize: 17,
    fontWeight: "bold",
    color: "#4B9460",
    textAlign: "center",
  },
  dogdataText: {
    fontSize: 17,
  },
  textWithDivider: {
    flex: 1,
    borderRightWidth: 2,
    borderRightColor: "#D3D3D3",
    paddingHorizontal: 10,
    alignItems: "center", // 수평 방향 가운데 정렬
    justifyContent: "center", // 수직 방향 가운데 정렬
  },
  textWithDivider2: {
    flex: 1,
    borderRightWidth: 1,
    borderRightColor: "#E8E8E8",
    paddingHorizontal: 10,
    alignItems: "center",
    justifyContent: "center",
  },
  dogText: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 10,
    marginTop: 10,
  },
  walkdataText: {
    fontSize: 15,
    marginTop: 10,
    marginBottom: 10,
    marginRight: 10,
    marginLeft: 10,
    textAlignVertical: "center",
    fontWeight: "bold",
  },
  greetingText: {
    fontSize: 16,
    fontWeight: "bold",
    marginTop: 10,
  },
  dogLevelText: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
  },
  levelText: {
    fontSize: 16,
    fontWeight: "bold",
    marginTop: 10,
    marginBottom: 5,
    marginRight: 5,
    marginLeft: 5,
    textAlign: "center",
  },
  expText: {
    fontSize: 14,
    marginTop: 10,
    marginBottom: 5,
    marginRight: 5,
    marginLeft: 5,
    textAlign: "center",
  },

  horizontalTextContainer: {
    flexDirection: "row",
    justifyContent: "center",
    borderRadius: 15,
    backgroundColor: "#E8E8E8",
    padding: 10,
    marginBottom: 10,
    marginTop: 10,
  },

  buttonStyle: {
    borderRadius: 30,
    backgroundColor: "#96D38C",
    padding: 15,
    width: 230,
    marginBottom: 10,
    marginTop: 10,
    alignSelf: "center",
    elevation: 5,
  },
  buttonText: {
    color: "white",
    fontSize: 22,
    textAlign: "center", // 가로로 가운데 정렬
    textAlignVertical: "center", // 세로로 가운데 정렬 (Android에서만 동작)
    fontWeight: "bold", // 글씨체를 두껍게 설정
  },
  dogImage: {
    width: 150, // 이미지의 가로 크기
    height: 240, // 이미지의 세로 크기
    marginTop: 10, // 이미지와 버튼 사이의 간격 조정
    justifyContent: "center", // 수직 방향 가운데 정렬
    alignItems: "center",
    alignSelf: "center",
  },
  weather: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    width: 400,
  },
  city: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  weatherDescription: {
    fontSize: 18,
    marginBottom: 10,
  },
  temperature: {
    fontSize: 32,
    fontWeight: "bold",
  },
  humidity: {
    fontSize: 18,
    marginTop: 10,
  },
});

export default HomeScreen;
