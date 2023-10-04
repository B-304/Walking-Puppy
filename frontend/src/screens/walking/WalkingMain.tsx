
import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import NewWalkingSetting from './NewWalkingSetting';
import TimeThemeSetting from './TimeThemeSetting';
import WalkingMap from './WalkingMap';
import StartDesMap from './StartDesMap';



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
      <Stack.Screen name="NewWalkingSetting" component={NewWalkingSetting} options={{headerShown: false}}/>
      <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting} options={{headerShown: false}}/>
      <Stack.Screen name="WalkingMap" component={WalkingMap}/>
      <Stack.Screen name="StartDesMap" component={StartDesMap} options={{headerTitle:'장소 검색', headerTitleAlign:'center'}}/>
    </Stack.Navigator>
  );
};

export default WalkingMain;