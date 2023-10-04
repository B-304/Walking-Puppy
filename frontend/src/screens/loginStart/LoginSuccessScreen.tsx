import { View, Text, Button } from 'react-native'
import React from 'react'
import { useDispatch } from 'react-redux';
import { userActions } from '../../redux/reducer/userSlice';  // 경로가 바뀔 수 있으니, userActions가 위치한 경로로 올바르게 수정해주세요.

const LoginSuccessScreen: React.FC = (): JSX.Element => {
    const dispatch = useDispatch();
  
    const handleLoginButtonClick = () => {
      // Redux의 로그인 상태를 true로 설정
      dispatch(userActions.loginSuccess());
      
      
    };
  
    return (
      <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
        <Text>LoginScreen</Text>
        <Button title="로그인" onPress={handleLoginButtonClick} />
      </View>
    )
  }
  
  export default LoginSuccessScreen;