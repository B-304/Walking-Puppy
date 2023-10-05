import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import RouteDetail from './RouteDetail';
import WalkingSavedScreen from './WalkingSavedScreen'


const ScrapMain:React.FC = () => {
  const Stack = createNativeStackNavigator();
  return (

      <Stack.Navigator initialRouteName='WalkingSavedScreen'>
        <Stack.Screen name="스크랩" component={WalkingSavedScreen}
        options={{
          headerShown: true,
          headerTitleAlign: 'center',
          headerTitleStyle: {
            fontSize: 24,
          },
        }}
        />
        <Stack.Screen name="산책로 상세" component={RouteDetail} 
        options={{
            headerShown: true,
            headerTitleAlign: 'center',
            headerTitleStyle: {
              fontSize: 24,
            },
          }}/>
      </Stack.Navigator>

  );
};

export default ScrapMain;