/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
// import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
// import { createNativeStackNavigator } from '@react-navigation/native-stack';
// import {NavigationContainer, } from '@react-navigation/native';
// import SpotSavedScreen from './src/screens/scrap/SpotSavedScreen';
// import NewWalkingSetting from './src/screens/walking/NewWalkingSetting';
// import HomeScreen from './src/screens/home/HomeScreen';
// import PopularSpot from './src/screens/popularSpot/PopularSpot';
// import MyPage from './src/screens/myPage/MyPage';
// import LoginScreen from './src/screens/loginStart/LoginScreen';
// import WalkingSavedScreen from './src/screens/scrap/WalkingSavedScreen';
// import SavedWalkingSetting from './src/screens/walking/SavedWalkingSetting';
// import TimeThemeSetting from './src/screens/walking/TimeThemeSetting';
import AppInner from './AppInner';
import { Provider } from 'react-redux';
import store from './src/redux/store';

const App : React.FC = (): JSX.Element => {

  
  // eslint-disable-next-line react/no-unstable-nested-components


  return (
    <Provider store={store}>
      <AppInner />
    </Provider>
  );

};


export default App;
