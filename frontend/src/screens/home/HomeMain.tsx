import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Walk from '../walking/NewWalkingSetting';
import HomeScreen  from './HomeScreen';

const HomeMain:React.FC = () => {
  const Stack = createNativeStackNavigator();
  return (

      <Stack.Navigator initialRouteName='HomeScreen'>
        <Stack.Screen name="홈" component={HomeScreen}  
        options={{
          headerShown: true,
          headerTitleAlign: 'center',
          headerTitleStyle: {
            fontSize: 24,
          },
        }}
        />
        <Stack.Screen name="산책 설정" component={Walk} />
      </Stack.Navigator>
  );
};

export default HomeMain;