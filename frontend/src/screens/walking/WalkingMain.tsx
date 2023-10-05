
import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import NewWalkingSetting from './NewWalkingSetting';
import TimeThemeSetting from './TimeThemeSetting';
import WalkingMap from './WalkingMap';
import StartDesMap from './StartDesMap';
import WalkingSetting from './WalkingSetting';
import SavedWalkingSetting from './SavedWalkingSetting';



export type RootStackParamList = {
  NewWalkingSetting: undefined;
  TimeThemeSetting: undefined;
  WalkingMap: undefined;
  StartDesMap: undefined;
  
};
const WalkingMain:React.FC = (): JSX.Element => {
  
  const Stack = createNativeStackNavigator<RootStackParamList>();
  return (
    <Stack.Navigator initialRouteName='NewWalkingSetting'>
      <Stack.Screen name="산책 설정" component={NewWalkingSetting}         options={{
          headerShown: true,
          headerTitleAlign: 'center',
          headerTitleStyle: {
            fontSize: 24,
          },
        }}/>
      <Stack.Screen name="산책 테마 설정" component={WalkingSetting} 
              options={{
                headerShown: true,
                headerTitleAlign: 'center',
                headerTitleStyle: {
                  fontSize: 24,
                },
              }}/>
      <Stack.Screen name="추천 산책 경로" component={SavedWalkingSetting}
                    options={{
                      headerShown: true,
                      headerTitleAlign: 'center',
                      headerTitleStyle: {
                        fontSize: 24,
                      },
                    }}/>
      <Stack.Screen name="WalkingMap" component={WalkingMap}/>
      <Stack.Screen name="StartDesMap" component={StartDesMap} options={{headerTitle:'장소 검색', headerTitleAlign:'center'}}/>
    </Stack.Navigator>
  );
};

export default WalkingMain;