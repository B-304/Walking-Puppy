import React, { useState, useEffect } from 'react';
import { View, TextInput, StyleSheet, TouchableOpacity } from 'react-native';
import Geolocation from '@react-native-community/geolocation';
import axios from 'axios';
import {useNavigation} from '@react-navigation/native';

const NewWalkingSetting: React.FC = () => {
  const [departure, setDeparture] = useState<string | null>(null);
  const [destination, setDestination] = useState<string>('');
  const navigation = useNavigation();

  useEffect(() => {
    Geolocation.getCurrentPosition(
      async (position) => {
        try {
          const response = await axios.get('https://maps.googleapis.com/maps/api/geocode/json', {
            params: {
              latlng: `${position.coords.latitude},${position.coords.longitude}`,
              key: '${googleMapApiKey}',
            },
          });

          if (response.data.results && response.data.results.length > 0) {
            setDeparture(response.data.results[0].formatted_address);
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
      <TouchableOpacity onPress={() => navigation.navigate('StartDesMap', { type: 'start' })}>
      <TextInput
        style={styles.input}
        value={departure ?? ''}
        placeholder="현재 위치"
      />
      </TouchableOpacity>
      <TextInput
        style={styles.input}
        value={destination}
        placeholder="도착지를 입력하세요."
        onChangeText={setDestination}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    padding: 16,
  },
  input: {
    borderWidth: 1,
    borderColor: '#E8E8E8',
    backgroundColor: '#F6F6F6',
    padding: 12,
    borderRadius: 4,
    marginBottom: 12,
  },
});

export default NewWalkingSetting;