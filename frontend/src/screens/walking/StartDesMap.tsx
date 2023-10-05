import React, { useState, useCallback } from 'react';
import { View, TextInput, Button, FlatList, StyleSheet } from 'react-native';
import MapView, { Marker } from 'react-native-maps';
import axios from 'axios';
import { useNavigation,RouteProp, } from '@react-navigation/native';
import {RootStackParamList} from './WalkingMain'
import { StackNavigationProp } from '@react-navigation/stack';

type LocationData = {
  lat: number;
  lng: number;
  formatted_address: string;
} | null;



const StartDesMap: React.FC = ({route}) => {
  const { setFunction } = route.params;
  
  const navigation = useNavigation<StackNavigationProp<RootStackParamList, 'NewWalkingSetting'>>();

  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState<LocationData>(null);
  
  const onChangePlace = useCallback((text:any) => setSearchTerm(text),[])
  
  const handleSearch = async () => {
    try {
      const response = await axios.get('https://maps.googleapis.com/maps/api/place/autocomplete/json', {
        params: {
          input: searchTerm,
          key: '${googleMapApiKey}'
        }
      });
  
      if (response.data.predictions) {
        setSearchResults(response.data.predictions);
      }
    } catch (error) {
      console.error('Error searching places:', error);
    }
  };

  const selectPlace = async (placeId: string) => {
    try {
      const response = await axios.get('https://maps.googleapis.com/maps/api/place/details/json', {
        params: {
          place_id: placeId,
          key: '${googleMapApiKey}'
        }
      });
  
      if (response.data.result) {
        const locationData = {
          lat: response.data.result.geometry.location.lat,
          lng: response.data.result.geometry.location.lng,
          formatted_address: response.data.result.formatted_address
        };
        setSelectedLocation(locationData);
      }
    } catch (error) {
      console.error('Error fetching place details:', error);
    }
  };

  return (
    <View style={styles.container}>
      <TextInput
        value={searchTerm}
        onChangeText={setSearchTerm}
        placeholder="장소 검색"
      />
      <Button title="검색" onPress={handleSearch} />
      <MapView style={styles.map}>
        {selectedLocation && (
          <Marker
            coordinate={{
              latitude: selectedLocation.lat,
              longitude: selectedLocation.lng,
            }}
            title={selectedLocation.formatted_address}
          />
        )}
      </MapView>
      <FlatList
        data={searchResults}
        keyExtractor={(item) => item.place_id}
        renderItem={({ item }) => (
          <Button
            title={item.description}
            onPress={() => selectPlace(item.place_id)}
          />
        )}
      />
      <Button
        title="출발"
        onPress={() => {
          navigation.navigate('NewWalkingSetting', { startLocation: selectedLocation });
        }}
      />
      <Button
        title="도착"
        onPress={() => {
          navigation.navigate('NewWalkingSetting', { endLocation: selectedLocation });
        }}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
  },
  map: {
    flex: 1,
    marginBottom: 10,
  },
});

export default StartDesMap;
