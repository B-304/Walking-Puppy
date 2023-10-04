import React, { useState, useEffect, useCallback } from 'react';
import { View, TextInput, StyleSheet, TouchableOpacity, Pressable, Text } from 'react-native';
import Geolocation from '@react-native-community/geolocation';
import axios from 'axios';

import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from './WalkingMain';
type LocationData = {
  lat: number ;
  lng: number;
  formatted_address: string | null;
} | null;

type TimeThemeSettingScreenProps = NativeStackScreenProps<RootStackParamList, 'TimeThemeSetting'>
type StartDesMapScreenProps = NativeStackScreenProps<RootStackParamList, 'StartDesMap'>
type CombinedProps = TimeThemeSettingScreenProps & StartDesMapScreenProps;

const NewWalkingSetting: React.FC<CombinedProps> = ({navigation}) => {
  const [start, setStart] = useState<LocationData | null>(null);
  const [destination, setDestination] = useState<LocationData | null>(null);
  
  const findStart = useCallback(() => {
    navigation.navigate('StartDesMap', { setFunction: setStart });
  },[navigation]);

  const findDestination = useCallback(() => {
    navigation.navigate('StartDesMap');
  },[navigation]);

  useEffect(() => {
    Geolocation.getCurrentPosition(
      async (position) => {
        try {
          const response = await axios.get('https://maps.googleapis.com/maps/api/geocode/json', {
            params: { // 내 위치 정보를 가져옴 현재 오류중
              latlng: `${position.coords.latitude},${position.coords.longitude}`,
              key: '${googleMapApiKey}',
            },
          });

          if (response.data.results && response.data.results.length > 0) {
            setStart(response.data.results[0].formatted_address);
            console.log(response);
          }

        } catch (error) {
          console.error('There was an error fetching the location data: ', error);
        }
      },
      (error) => {
        console.log(error);
      },
      { enableHighAccuracy: true, timeout: 15000, maximumAge: 10000 }
    );
  }, []);

  return (
    
    <View style={styles.container}>
      <View style={styles.inputBox}>
        <Pressable onPress={findStart}>
          <Text>
            {start?.formatted_address ? start.formatted_address : '출발지를 입력하세요'}
          </Text>
        </Pressable>
      </View>
      <View style={styles.inputBox}>
        <Pressable onPress={findDestination}>
          <Text>
            {destination?.formatted_address ? destination.formatted_address : '도착지를 입력하세요'}
          </Text>
        </Pressable>
      </View>
      
      <View>

        <TouchableOpacity style={styles.nextButton} onPress={() => navigation.navigate('산책 테마 설정',{
          start:{
            latitude:36.3463,
            longitude:127.2941
          },
          end:{
            latitude:36.3463,
            longitude:127.2941
          }
        })}>
              <Text style={styles.buttonText}>확인</Text>
        </TouchableOpacity>
      </View>
    </View>
    
    
    
)};

const styles = StyleSheet.create({
  container: {
    height:800,
    backgroundColor: '#FFFFFF',
    padding: 16,
  },
  inputBox: {
    borderWidth: 1,
    borderColor: '#E8E8E8',
    backgroundColor: '#F6F6F6',
    padding: 12,
    borderRadius: 4,
    marginBottom: 12,
  },
  nextButton: {
    position: 'absolute',
    left: 33.5,            // 왼쪽에서 33.5만큼 떨어짐
    right: 33.5,           // 오른쪽에서 33.5만큼 떨어짐
    top: 450,            // 하단에서 20만큼 떨어짐
    height: 53,            // 버튼의 높이
    backgroundColor: '#4B9460',
    justifyContent: 'center',  // 텍스트를 버튼 중앙에 위치시키기 위해
    alignItems: 'center',      // 텍스트를 버튼 중앙에 위치시키기 위해
    textAlignVertical: 'center',
    borderRadius: 10,       // 버튼을 화면 중앙에 위치시키기 위해
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 20,
    fontWeight: 'bold',  // semi-bold로 설정. 'bold'로 설정하면 더 굵게 됩니다.
  },
});

export default NewWalkingSetting;

{/* <View style={styles.container}>
      Vi
      <TouchableOpacity onPress={() => navigation.navigate('StartDesMap', { type: 'start' })}>
      {/* <TextInput
        style={styles.input}
        value={departure ?? ''}
        placeholder="현재 위치"
      /> */}
      
  //     </TouchableOpacity>
  //     <TextInput
  //       style={styles.input}
  //       value={destination}
  //       placeholder="도착지를 입력하세요."
  //       onChangeText={setDestination}
  //     />
  //     <Pressable onPress={() => setCount((p) => p+1)}>
  //       <Text>
  //         {count}
  //       </Text>
  //     </Pressable>
  //   </View>
  // ); */}