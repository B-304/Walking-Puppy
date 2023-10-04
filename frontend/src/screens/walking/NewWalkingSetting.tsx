import React, { useState } from 'react';
import { View, TextInput, StyleSheet, TouchableOpacity, Text, Alert } from 'react-native';
import axios from 'axios';

const NewWalkingSetting: React.FC = () => {
  const [startAddress, setStartAddress] = useState<string>(''); // 출발지 주소
  const [endAddress, setEndAddress] = useState<string>(''); // 도착지 주소

  
  const searchAddress = async (address: string) => {
    try {
      const response = await axios.get(
        `https://dapi.kakao.com/v2/local/search/address.json?query=${address}`,
        {
          headers: {
            Authorization: 'KakaoAK 6e6828052f9580b3bc77e19c8b639327',
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
      } else {
        Alert.alert('검색 결과가 없습니다.');
      }
    } catch (error) {
      console.error('주소 검색 중 오류 발생:', error);
      Alert.alert('주소 검색 중 오류 발생했습니다.');
    }
  };

  // 엔터 키를 누를 때 주소 검색 수행
  const handleEnterKeyPress = (address: string) => {
    if (address.trim() !== '') {
      searchAddress(address);
    }
  };

  return (
    <View style={styles.container}>
      {/* <Text style={styles.topText}>출발지와 도착지를 설정해주세요.</Text> */}
      <TextInput
        style={styles.inputBox}
        placeholder="출발지 입력"
        value={startAddress}
        onChangeText={(text) => setStartAddress(text)}
        onEndEditing={() => handleEnterKeyPress(startAddress)}
      />
      <TextInput
        style={styles.inputBox}
        placeholder="도착지 입력"
        value={endAddress}
        onChangeText={(text) => setEndAddress(text)}
        onEndEditing={() => handleEnterKeyPress(endAddress)}
      />
      <TouchableOpacity
        style={styles.nextButton}
        onPress={() => {
          // 이동하는 페이지 지정
        }}
        disabled={!startAddress || !endAddress}
      >
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
