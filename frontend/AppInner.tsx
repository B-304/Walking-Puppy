import React from 'react';
import { StackNavigationProp } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import {NavigationContainer, } from '@react-navigation/native';
import SpotSavedScreen from './src/screens/scrap/SpotSavedScreen';
import NewWalkingSetting from './src/screens/walking/NewWalkingSetting';
import HomeScreen from './src/screens/home/HomeScreen';
import PopularSpot from './src/screens/popularSpot/PopularSpot';
import MyPageMain from './src/screens/myPage/MypageMain';
import LoginScreen from './src/screens/loginStart/LoginScreen';
// import WalkingSavedScreen from './src/screens/scrap/WalkingSavedScreen';
// import SavedWalkingSetting from './src/screens/walking/SavedWalkingSetting';
// import TimeThemeSetting from './src/screens/walking/TimeThemeSetting';
import Ionic from 'react-native-vector-icons/Ionicons';
import { useSelector } from 'react-redux';
import { RootState } from './src/redux/reducer';
import WalkingMain from './src/screens/walking/WalkingMain';
import WalkingSavedScreen from './src/screens/scrap/WalkingSavedScreen';
import HomeMain from './src/screens/home/HomeMain';



type RootStackParamList = {
  Home: undefined;
  
};

type NavigationProp = StackNavigationProp<RootStackParamList, 'Home'>; // 'Home'은 현재 스크린의 이름입니다.

const AppInner:React.FC = () => {
  const isLoggendIn = useSelector((state:RootState) => state.user.isLoggedIn);
  
  const Stack = createNativeStackNavigator();
  const Tab = createBottomTabNavigator();


  const BottomTabScreen:React.FC = () => {
    return (
    <Tab.Navigator initialRouteName="홈." screenOptions={({ route }) => (
      {
        tabBarHideOnKeyboard: true,
        headerShown: true,
        headerTitleAlign: 'center',
        headerTitleStyle: {
          fontSize: 24,
        },
        tabBarStyle: {
          height: 70,
        },
        // eslint-disable-next-line react/no-unstable-nested-components
        tabBarIcon: ({ focused, color, size }) => {
          let iconName:string;
          color = '#4B9460';
          if (route.name === '인기스팟') {
            iconName = focused ? 'trophy' : 'trophy-outline';
          } else if (route.name === '산책') {
            iconName = focused ? 'footsteps-sharp' : 'footsteps-outline';
          } else if (route.name === '홈.') {
            iconName = focused ? 'home-sharp' : 'home-outline';
          } else if (route.name === '스크랩') {
            iconName = focused ? 'bookmark-sharp' : 'bookmark-outline';
          } else if (route.name === '마이페이지') {
            iconName = focused ? 'person' : 'person-outline';
          }
          return <Ionic name={iconName} size={size} color={color}/>;
        },
        tabBarActiveTintColor: '#4B9460', // 포커스 될 때의 색상
        tabBarInactiveTintColor: 'gray',  // 포커스되지 않을 때의 색상
        tabBarLabelStyle: {
          fontSize: 12,
          fontFamily: 'Your-Font-Family-Here', // 폰트 가족을 변경하려면 이 줄을 수정하세요
          marginBottom: 12,
        },
      }
    )}>
      <Tab.Screen name="인기스팟" component={PopularSpot} /> 
      <Tab.Screen name="산책" component={WalkingMain} options={{ headerShown: false }} /> 
      {/*<Tab.Screen name="산책" component={NewWalkingSetting} />*/}
      {/*<Tab.Screen name="산책" component={WalkingSetting} />*/}
      <Tab.Screen name="홈." component={HomeMain} options={{ headerShown: false }} /> 
      <Tab.Screen name="스크랩" component={WalkingSavedScreen} /> 
      <Tab.Screen name="마이페이지" component={MyPageMain} options={{ headerShown: false }}/>
      {/*<Tab.Screen name="마이페이지" component={ProfileEdit} />*/}
    </Tab.Navigator>
    )
  };

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{headerShown:false}}>
       
      {isLoggendIn?  
      <Stack.Screen name="BottomNav" component={BottomTabScreen} /> 
      : (<Stack.Screen name="Login" component={LoginScreen} />
      )}
      
        {/* 하단바 */}
        
        {/* 로그인 및 시작 */}
        
        {/* 인기스팟 */}

        {/* 산책 */}
        {/* <Stack.Screen name="SavedWalking" component={SavedWalkingSetting} />
        <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting} /> */}
        {/* 홈 */}
        
        {/* 스크랩 */}
        {/* <Stack.Screen name="WalkingSaved" component={WalkingSavedScreen} /> */}

        {/* 마이페이지 */}
      </Stack.Navigator>
    </NavigationContainer>
  )
}

export default AppInner