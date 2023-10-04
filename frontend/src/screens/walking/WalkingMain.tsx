
import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import NewWalkingSetting from './NewWalkingSetting';
import TimeThemeSetting from './TimeThemeSetting';
import WalkingMap from './WalkingMap';
import StartDesMap from './StartDesMap';
import WalkingSetting from './WalkingSetting';

const WalkingMain:React.FC = (): JSX.Element => {
  const Stack = createNativeStackNavigator();
  return (
    <Stack.Navigator initialRouteName='NewWalkingSetting'>
      <Stack.Screen name="NewWalkingSetting" component={NewWalkingSetting} />
      <Stack.Screen name="산책 설정" component={WalkingSetting} />
      <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting}/>
      <Stack.Screen name="WalkingMap" component={WalkingMap}/>
      <Stack.Screen name="StartDesMap" component={StartDesMap} />
    </Stack.Navigator>
  );
};

export default WalkingMain;