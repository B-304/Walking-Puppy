import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import MyPage from "./MyPage";
import ProfileEdit from "./ProfileEdit";
import { NavigationContainer } from "@react-navigation/native";

const MypageMain: React.FC = () => {
  const Stack = createNativeStackNavigator();
  return (
    <Stack.Navigator initialRouteName="Mypage">
      <Stack.Screen
        name="마이페이지"
        component={MyPage}
        options={{
          headerShown: true,
          headerTitleAlign: "center",
          headerTitleStyle: {
            fontSize: 24,
          },
        }}
      />
      <Stack.Screen name="회원정보 수정" component={ProfileEdit} />
    </Stack.Navigator>
  );
};

export default MypageMain;
