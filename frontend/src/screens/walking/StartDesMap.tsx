import React from 'react';
import { View, StyleSheet } from 'react-native';
import MapView from 'react-native-maps';

const StartDesMap: React.FC = ({ route }: any) => {
  // type은 'start' 또는 'destination'이 될 수 있습니다.
  const { type } = route.params;

  return (
    <View style={styles.container}>
      <MapView
        style={styles.map}
        // 다른 MapView 설정
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  map: {
    flex: 1,
  }
});

export default StartDesMap;
