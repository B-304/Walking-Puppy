import React, { useState } from 'react';
import { View, TextInput, StyleSheet, TouchableOpacity, Text, Alert } from 'react-native';
import axios from 'axios';
import { useNavigation } from "@react-navigation/native";

import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { RootStackParamList } from './WalkingMain';
import{kakao_api} from 'react-native-dotenv';

type LocationData = {
  lat: number ;
  lng: number;
  formatted_address: string | null;
} | null;

const NewWalkingSetting: React.FC = () => {

  const navigation = useNavigation();

  const [startAddress, setStartAddress] = useState<string>(''); // 출발지 주소
  const [endAddress, setEndAddress] = useState<string>(''); // 도착지 주소
  const [startCoordinates, setStartCoordinates] = useState<{ startLatitude: number; startLongitude: number } | null>(null); // 출발지 위도 및 경도
  const [endCoordinates, setEndCoordinates] = useState<{ endLatitude: number; endLongitude: number } | null>(null); // 도착지 위도 및 경도

  const searchAddress = async (address: string, isStartAddress: boolean) => {
    try {
      const response = await axios.get(
        `https://dapi.kakao.com/v2/local/search/address.json?query=${address}`,
        {
          headers: {
            Authorization: `KakaoAK ${kakao_api}`,
          },
        }
      );
      const firstAddress = response.data.documents[0];
      if (firstAddress) {
        const { x, y } = firstAddress;
        console.log(`주소: ${address}`);
        console.log(`위도: ${parseFloat(y)}`);
        console.log(`경도: ${parseFloat(x)}`);
        Alert.alert('위치가 지정되었습니다.');
        
        // 출발지 주소의 경우
        if (isStartAddress) {
          const startLatitude = parseFloat(y);
          const startLongitude = parseFloat(x);
          setStartCoordinates({ startLatitude, startLongitude});
          console.log(`출발지 위도: ${startLatitude}`);
          console.log(`출발지 경도: ${startLongitude}`);
        }
        // 도착지 주소의 경우
        else {
          // setEndCoordinates({ endLatitude: parseFloat(y), endLongitude: parseFloat(x) });
          const endLatitude = parseFloat(y);
          const endLongitude = parseFloat(x);
          setEndCoordinates({ endLatitude, endLongitude});
          console.log(`도착지 위도: ${endLatitude}`);
          console.log(`됴착지 경도: ${endLongitude}`);
        }
      } else {
        Alert.alert('검색 결과가 없습니다.');
      }
    } catch (error) {
      console.error('주소 검색 중 오류 발생:', error);
      Alert.alert('주소 검색 중 오류 발생했습니다.');
    }
  };

    // 엔터 키를 누를 때 주소 검색 수행
    const handleEnterKeyPress = (address: string, isStartAddress: boolean) => {
      if (address.trim() !== '') {
        searchAddress(address, isStartAddress);
      }
    };

    // 확인 버튼을 누를 때 호출되는 함수
  const handleConfirmation = () => {
    if (startCoordinates && endCoordinates) {
      console.log('출발지 위도:', startCoordinates.startLatitude);
      console.log('출발지 경도:', startCoordinates.startLongitude);
      console.log('도착지 위도:', endCoordinates.endLatitude);
      console.log('도착지 경도:', endCoordinates.endLongitude);
    } else {
      Alert.alert('출발지와 도착지 주소를 먼저 입력해주세요.');
    }
  };

return (
  <View style={styles.container}>
    <TextInput
    style={styles.inputBox}
    placeholder="출발지 입력"
    value={startAddress}
    onChangeText={(text) => setStartAddress(text)}
    onEndEditing={() => handleEnterKeyPress(startAddress, true)}
  />
  <TextInput
    style={styles.inputBox}
    placeholder="도착지 입력"
    value={endAddress}
    onChangeText={(text) => setEndAddress(text)}
    onEndEditing={() => handleEnterKeyPress(endAddress, false)}
  />
  <TouchableOpacity style={styles.nextButton} onPress={() => navigation.navigate('산책 테마 설정',{ 
      start:{
        latitude: startCoordinates.startLatitude,
        longitude: startCoordinates.startLongitude
      },
      end:{
        latitude:endCoordinates.endLatitude,
        longitude:endCoordinates.endLongitude
      }
    })}disabled={!startAddress || !endAddress}>
          <Text style={styles.buttonText}>확인</Text>
    </TouchableOpacity>


</View>
);
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
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
    marginTop: 12,
  },
  nextButton: {
    position: 'absolute',
    left: 33.5,
    right: 33.5,
    bottom: '10%',
    height: 53,
    backgroundColor: '#4B9460',
    justifyContent: 'center',
    alignItems: 'center',
    textAlignVertical: 'center',
    borderRadius: 10,
  },
  buttonText: {
    color: '#FFFFFF',
    fontSize: 20,
    fontWeight: 'bold',
  },
  topText: {
    fontSize: 15,
    fontWeight: 'bold',
    marginBottom: 12,
  },
});

export default NewWalkingSetting;