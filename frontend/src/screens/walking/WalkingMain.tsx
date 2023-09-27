
import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import NewWalkingSetting from './NewWalkingSetting';
import TimeThemeSetting from './TimeThemeSetting';
import WalkingMap from './WalkingMap';

const WalkingMain:React.FC = (): JSX.Element => {
  const Stack = createNativeStackNavigator();
  return (
    <Stack.Navigator initialRouteName='NewWalkingSetting'>
      <Stack.Screen name="NewWalkingSetting" component={NewWalkingSetting} />
      <Stack.Screen name="TimeThemeSetting" component={TimeThemeSetting}/>
      <Stack.Screen name="WalkingMap" component={WalkingMap}/>
    </Stack.Navigator>
  );
};

export default WalkingMain;