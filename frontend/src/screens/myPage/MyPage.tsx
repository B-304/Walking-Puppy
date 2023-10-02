import { View, Text, Image, TouchableOpacity } from "react-native";
import React, { useState, useEffect } from "react";
import axios from "axios";
// import { useNavigation } from "@react-navigation/native";
import MyPageDetail from "./MyPageDetail";

const MyPage: React.FC = (): JSX.Element => {
  const [content, setContent] = useState("");
  const navigation = useNavigation();

  useEffect(() => {
    setContent("홍지민");
    //   // Axios 요청을 보내고 데이터를 받아옵니다.
    //   axios
    //     .get("http://localhost:8080/2")
    //     .then((response) => {
    //       console.log(response);
    //       const { data } = response;
    //       setContent(data);
    //     })
    //     .catch((error) => {
    //       console.error("데이터 요청 실패:", error);
    //     });
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.grayBox}>
        <Image
          source={require("./assets/image-user.png")}
          style={styles.image}
        />
        <Text style={styles.nameStyle}>{content}</Text>
        <Text style={styles.suffixStyle}> 님</Text>
        {/* <TouchableOpacity onPress={() => navigation.navigate("MyPageDetail")}> */}
        <Image
          source={require("./assets/modify-user.png")}
          style={styles.modImage}
        />
        {/* </TouchableOpacity> */}
      </View>
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
    position: "absolute",
    width: 326,
    height: 77,
    // left: 19,
    top: 55,
    backgroundColor: "rgba(217,217,217,0.29)",
    borderRadius: 20,
    alignItems: "center", // 이미지와 텍스트 중앙 정렬을 위해 추가
    justifyContent: "center", // 이미지와 텍스트 중앙 정렬을 위해 추가
    paddingHorizontal: 10, // 내부 여백 조정을 위해 추가
  },
  image: {
    position: "absolute",
    left: "15.75%",
    right: "80.72%",
    top: "15.03%",
    bottom: "83.39%",
    width: 50,
    height: 50,
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
    fontSize: 20,
    lineHeight: 24,
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
};

export default MyPage;
