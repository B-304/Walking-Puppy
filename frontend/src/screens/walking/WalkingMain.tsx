
import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import NewWalkingSetting from './NewWalkingSetting';
import TimeThemeSetting from './TimeThemeSetting';
import WalkingMap from './WalkingMap';
import StartDesMap from './StartDesMap';
import WalkingSetting from './WalkingSetting';



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
<<<<<<< HEAD
      <Stack.Screen name="NewWalkingSetting" component={NewWalkingSetting} />
      <Stack.Screen name="산책 설정" component={WalkingSetting} />
      <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting}/>
=======
      <Stack.Screen name="NewWalkingSetting" component={NewWalkingSetting} options={{headerShown: false}}/>
      <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting} options={{headerShown: false}}/>
>>>>>>> fb2a5e42c1e25954fae12ee7e3d7e7d446b1cb82
      <Stack.Screen name="WalkingMap" component={WalkingMap}/>
      <Stack.Screen name="StartDesMap" component={StartDesMap} options={{headerTitle:'장소 검색', headerTitleAlign:'center'}}/>
    </Stack.Navigator>
  );
};

export default WalkingMain;