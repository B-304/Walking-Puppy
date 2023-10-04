import React, {useState} from 'react';
import MapView, { Polyline } from 'react-native-maps';
import { View, StyleSheet, Pressable, Text, TextInput, KeyboardAvoidingView } from 'react-native';
import Octicons from 'react-native-vector-icons/Octicons';
import { useNavigation } from '@react-navigation/native';
import DismissKeyboardView from '../../components/DismissKeyboardView';

const SpotSavedScreen:React.FC = (): JSX.Element => {
  const navigation = useNavigation();
  const coordinates = [
    { latitude: 37.78825, longitude: -122.4324 },
    { latitude: 37.78845, longitude: -122.4322 },
    { latitude: 37.78865, longitude: -122.5000  },
    // ... 기타 좌표들
  ];
  const [modalVisible, setModalVisible] = useState(false);
  const [detailModal, setDetailModal] = useState(false);
  const [saveRoute, setSaveRoute] = useState(false);
  return (
    <View style={styles.container}>
      <MapView
        style={styles.map}
        initialRegion={{
          latitude: 37.78825,
          longitude: -122.4324,
          latitudeDelta: 0.0922,
          longitudeDelta: 0.0421,
        }}>
        <Polyline
          coordinates={coordinates}
          strokeColor="#000" // 색상 설정
          strokeWidth={3} // 선 두께 설정
        />
      </MapView>
      <Pressable
        style={styles.closeButton} 
        onPress={() => setModalVisible(true)}
      >
        <Octicons name="stop" size={35} color="black" />
      </Pressable>
    {modalVisible && (
      <Pressable style={styles.modalBackground} onPress={() => setModalVisible(false)}>
        <View style={styles.modalContent}>
          <View style={styles.modalTextSection}>
            <Text style={styles.modalText}>
            산책을 종료하시겠습니까?
            </Text>
          </View>
          <View style={styles.modalButtonSection}>
            <Pressable style={styles.cancelButton}>
              <Text style={styles.cancelButtonText}>
                취소
              </Text>
            </Pressable>
            <Pressable style={styles.endButton} onPress={() => {setModalVisible(false); setDetailModal(true)}}>
              <Text style={styles.endButtonText}>
                확인
              </Text>
            </Pressable>
          </View>
        </View>
      </Pressable>
    )}
   {detailModal && (
  <Pressable style={styles.modalBackground} onPress={() => setDetailModal(false)}>
    <View style={styles.detailModal}>
      <View style={styles.modalTitle}>
          <Text style={styles.modalTitleContext}>
            산책 상세 기록
          </Text>
      </View>
      <View style={styles.detailModalContent}>
        
        <View style={styles.modalDataRow}>
          <Text style={styles.dataDot}>•</Text>
          <Text style={styles.modalDataLabel}>소요시간</Text>
          <Text style={styles.modalDataValue}>1시간 36분</Text>
        </View>
        <View style={styles.modalDataRow}>
          <Text style={styles.dataDot}>•</Text>
          <Text style={styles.modalDataLabel}>걸음 수</Text>
          <Text style={styles.modalDataValue}>20000 보</Text>
        </View>
        <View style={styles.modalDataRow}>
          <Text style={styles.dataDot}>•</Text>
          <Text style={styles.modalDataLabel}>테마</Text>
          <Text style={styles.modalDataValue}>자연</Text>
        </View>
        <View style={styles.modalDataRow}>
          <Text style={styles.dataDot}>•</Text>
          <Text style={styles.modalDataLabel}>칼로리</Text>
          <Text style={styles.modalDataValue}>850 Kcal</Text>
        </View>
        <View style={styles.modalDataRow}>
          <Text style={styles.dataDot}>•</Text>
          <Text style={styles.modalDataLabel}>이동거리</Text>
          <Text style={styles.modalDataValue}>0.4km</Text>
        </View>
      </View>

      <View>
        <Text style={{fontSize:12, marginBottom:10}}>
          저장 시 나의 보관함에서 해당 경로로 산책을 할 수 있습니다.
        </Text>
      </View>
      <View style={styles.detailModalButtonSection}>
            <Pressable style={styles.cancelButton} onPress={() => 
              navigation.reset({
                index: 0,
                routes: [{ name: '홈' }],
              })
              }>
              <Text style={styles.cancelButtonText}>
                홈으로
              </Text>
            </Pressable>
            <Pressable style={styles.endButton} onPress={() => { setDetailModal(false); setSaveRoute(true)}}>
              <Text style={styles.endButtonText}>
                저장
              </Text>
            </Pressable>
          </View>
    </View>
  </Pressable>
)}
{saveRoute && (
  <KeyboardAvoidingView behavior={Platform.OS === "ios" ? "padding" : "height"} style={{flex: 1}}>
  <Pressable style={styles.modalBackground} onPress={() => setSaveRoute(false)}>
  <View style={styles.saveRouteModal} onStartShouldSetResponder={() => true}>
    <View style={styles.modalTextSection}>
      <Text style={styles.modalText}>
      산책로 저장
      </Text>
    </View>
    <View style={{paddingLeft:20}}>
      <Text style={{fontSize:16, color: 'black', fontWeight: 'bold'}}>
        산책로 이름
      </Text>
    </View>
    <View style={{ marginHorizontal: 15}}>
      <View style={{paddingLeft:20, borderWidth: 1,
      borderColor: '#E8E8E8',
      backgroundColor: '#F6F6F6',
      paddingVertical: 1,
      paddingHorizontal: 2,
      borderRadius: 4,
      marginBottom: 12,
      marginHorizontal: 5,
      marginTop: 10,
      }}>
        <TextInput placeholder='산책로 이름을 입력하세요.'/>

        
      </View>
    </View>
    <View style={styles.modalButtonSection}>
      <Pressable style={styles.cancelButton}>
        <Text style={styles.cancelButtonText} onPress={() => 
              navigation.reset({
                index: 0,
                routes: [{ name: '홈' }],
              })
              }>
          홈으로
        </Text>
      </Pressable>
      <Pressable style={styles.endButton} onPress={() => {setSaveRoute(false)}}>
        <Text style={styles.endButtonText}>
          저장
        </Text>
      </Pressable>
    </View>
  </View>
</Pressable>
</KeyboardAvoidingView>
)}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    ...StyleSheet.absoluteFillObject,
  },
  closeButton: {
    position: 'absolute',
    bottom: 15,
    right: 17,
    width: 65,
    height: 65,
    backgroundColor: 'white',
    borderRadius: 32.5,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 5,  // Android shadow
    shadowOffset: { width: 0, height: 3 }, // iOS shadow
    shadowOpacity: 0.3, // iOS shadow
    shadowRadius: 4, // iOS shadow
  },
  modalBackground: {
    position: 'absolute',
    backgroundColor: 'rgba(0,0,0,0.5)',
    top: 0,
    bottom: 0,
    left: 0,
    right: 0,

  },
  modalContent: {
    position: 'absolute',
    backgroundColor: '#ffffff',
    top: 250,
    bottom: 240,
    left: 16,
    right: 16,
    elevation: 5,  // Android shadow
    borderRadius: 15,
  },
  saveRouteModal:{
    position: 'absolute',
    backgroundColor: '#ffffff',
    top: 215,
    bottom: 215,
    left: 16,
    right: 16,
    elevation: 5,  // Android shadow
    borderRadius: 15,
  },
  modalTextSection: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  detailModalButtonSection: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingBottom: 20,
  },
  modalButtonSection: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 38, // 버튼을 모달 경계에서 떨어뜨리기 위한 패딩
    paddingBottom: 30,
  },
  modalText: {
    fontSize: 20,
    color: 'black',
    fontWeight: 'bold',
  },
  cancelButton: {
    width: 140,
    height: 40,
    
    justifyContent: 'center',
    alignItems: 'center',
    borderColor: '#4B9460',
    backgroundColor: '#FFFFFF',
    color: '#4B9460',
    borderWidth: 1,
    borderRadius: 5,
  },
  cancelButtonText: {
    color: '#4B9460',
    fontWeight: 'bold',
  },
  endButton: {
    width: 140,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#4B9460',
    borderRadius: 5,
  },
  endButtonText: {
    color: '#FFFFFF',
    fontWeight: 'bold',
  },
  detailModal:{
    position: 'absolute',
    backgroundColor: '#ffffff',
    top: 125,
    bottom: 125,
    left: 16,
    right: 16,
    elevation: 5,  // Android shadow
    borderRadius: 15,
    paddingLeft: 33,
    paddingRight: 28,
  },
  modalDataRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingVertical: 8,
  },
  dataDot: {
    width: 7,
    height: 7,
    borderRadius: 3.5,
    backgroundColor: '#4B9460',
    marginRight: 8,
  },
  modalDataLabel: {
    color: 'black',
    flex: 2,
    fontSize: 16,
    fontWeight: 'bold',
    
  },
  modalDataValue: {
    flex: 3,
    fontSize: 16,
    textAlign: 'left',
    color: 'black',
  },
  detailModalContent: {
    flex: 5,
  },
  modalTitle: {
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical:  26,
  },
  modalTitleContext: {
    fontSize: 22,
    color: 'black',
    fontWeight: 'bold',
  }
  
});


export default SpotSavedScreen;