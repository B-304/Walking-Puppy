import { View, Image, TouchableOpacity, StyleSheet, ImageBackground } from 'react-native'
import React from 'react'
import { useDispatch } from 'react-redux';
import { userActions } from '../../redux/reducer/userSlice';  // 경로가 바뀔 수 있으니, userActions가 위치한 경로로 올바르게 수정해주세요.
// import { NavigationContainer } from '@react-navigation/native';
import {accessToken} from 'react-native-dotenv';
const LoginScreen: React.FC = (): JSX.Element => {
  const dispatch = useDispatch();

  const handleLoginButtonClick = () => {

    // Redux의 로그인 상태를 true로 설정
    dispatch(userActions.loginSuccess());

    // dispatch(userActions.getTokensUserId({
    //   accessToken:,
    //   refreshToken:,
    //   userId:,
    //   nickname:,
    // }))
    
    // Linking.openURL('https://kauth.kakao.com/oauth/authorize?client_id=6e6828052f9580b3bc77e19c8b639327&redirect_uri=http://localhost:8080/oauth/kakao&response_type=code&scope=account_email');
  };

  const kakaoLogo = require('../../assets/kakao_login_button.png');
  const backgroundImage = require('../../assets/main_background.png');

  return (
    <ImageBackground source={backgroundImage} style={styles.Backcontainer}>
      <View style={styles.container}>
        <TouchableOpacity style={styles.imageContainer} onPress={handleLoginButtonClick}>
          <Image source={kakaoLogo} style={styles.kakaoLogo} />
        </TouchableOpacity>
      </View>
    </ImageBackground>
  );
};

const styles = StyleSheet.create({
  Backcontainer: {
    flex: 1,
    resizeMode: 'cover',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%', // 화면 너비에 맞게 설정
    height: '100%', // 화면 높이에 맞게 설정
  },
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  imageContainer: {
    padding: 10,
    borderRadius: 5,
    backgroundColor: '#FEE500',
    position: 'absolute',
    bottom: '15%',
  },
  kakaoLogo: {
    width: 300, // 이미지의 너비 조정
    height: 30, // 이미지의 높이 조정
  },
});

export default LoginScreen;