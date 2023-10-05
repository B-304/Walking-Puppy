import { View, Text ,StyleSheet,TouchableOpacity,Modal,Button} from 'react-native'
import React,{useState} from 'react'
import Slider from '@react-native-community/slider';
import axios from 'axios'
import { useRoute } from '@react-navigation/native';
import { useNavigation } from "@react-navigation/native";
import{accessToken} from 'react-native-dotenv';



const WalkingSetting:React.FC = (): JSX.Element => {


    const [isSafe, setIsSafe] = useState(false); // 안전한 길 버튼 상태
    const [isNature, setIsNature] = useState(false); // 자연 버튼 상태
    const [selectedTime, setSelectedTime] = useState(15); // 초기 선택 시간 (예: 15분)
    const [modalVisible, setModalVisible] = useState(false); // 모달 상태 추가
    const route = useRoute();
    const {start,end} = route.params;
    const [pathData, setPathData] = useState({startLatitude: start.latitude,
        startLongitude: start.longitude,
        endLatitude: end.latitude,
        endLongitude: end.longitude,
        spotList: [],
        themeId: null, // 테마에 따라 업데이트
        estimatedTime: selectedTime,});


    const navigation = useNavigation();
    const BEARER_TOKEN =accessToken;

    const minimumValue=15,maximumValue=60;
    const handleTimeChange = (value: number) => {
      setSelectedTime(value);
    };



    const newPath = () => {

           const updatePathData={
            startLatitude: start.latitude,
            startLongitude: start.longitude,
            endLatitude: end.latitude,
            endLongitude: end.longitude,
            spotList: [],
            themeId: isSafe ? 1 : isNature ? 2 : null, // 테마에 따라 업데이트
            estimatedTime: selectedTime,
        };

        
        if(updatePathData.themeId ==null){

            console.log(updatePathData+"null");
            setModalVisible(true)
            return;
        }

        navigation.navigate('추천 산책 경로',Response.data);


        // const updatePathData={
        //     startLatitude: start.latitude,
        //     startLongitude: start.longitude,
        //     endLatitude: end.latitude,
        //     endLongitude: end.longitude,
        //     spotList: [],
        //     themeId: isSafe ? 1 : isNature ? 2 : null, // 테마에 따라 업데이트
        //     estimatedTime: selectedTime,
        // };

        // console.log(updatePathData+"1")

        // if(updatePathData.themeId ==null){

        //     console.log(updatePathData+"null");
        //     setModalVisible(true)
        //     return;
        // }

        // else{
        //     if(isNature){
        //         updatePathData.themeId = 2;
        //     }
        //     else{
        //         updatePathData.themeId = 1;
        //     }
        // }

        // console.log(updatePathData+"p");
        // setPathData(updatePathData);

        // //새로운 경로
        // const url = 'https://j9b304.p.ssafy.io/api/walk/new-path'; // 실제 API 엔드포인트 URL로 변경해야 합니다.
        // console.log(pathData);
        // axios
        //   .post(url,
        //     pathData,
        //    { headers: {
        //         Authorization: `Bearer ${BEARER_TOKEN}`,
        //       },
        // })
        //   .then((Response)=>{
        //     console.log('새로운 경로 생성 성공')
        //     console.log(Response.data)
        //     navigation.navigate('추천 상책 경로',Response.data)
        //   })
        //   .catch((error)=> {
        //     console.error('새로운 경로 생성 실패',error)
        //   })
      };

  

  return (
    <View style={{backgroundColor:"white",height:700}}>
            {/* 모달 컴포넌트 */}
            <Modal
                animationType="none"
                transparent={true}
                visible={modalVisible}
                onRequestClose={() => {
                    setModalVisible(!modalVisible);
                }}
            >
                <View style={styles.centeredView}>
                    <View style={styles.modalView}>
                        <Text style={styles.modalText}>테마 선택</Text>
                        <Text style={styles.modalComm}>정확한 산책 경로 추천을 위해 테마를 선택해 주세요.</Text>
                        <TouchableOpacity style={styles.modalButton} onPress={() => {
                            setModalVisible(!modalVisible);
                        }}>
                            <Text style={styles.modalButtonText}>확인</Text>
                        </TouchableOpacity>

                    </View>
                </View>
            </Modal>


        
        <View style = {styles.thema}>
            <Text style = {styles.text}>테마 설정</Text>

        <View style = {styles.samllButtonContainer}>
            <TouchableOpacity  style={[styles.smallButton,isSafe ? styles.activeButton : null]} onPress={() => {
            setIsSafe(true);
            setIsNature(false);
            }}>
              <Text style={[styles.smallButtonText,isSafe ? styles.activeText : null]}>안전한 길</Text>
            </TouchableOpacity>

            <TouchableOpacity  style={[styles.smallButton,isNature ? styles.activeButton : null]} onPress={() => {
            setIsSafe(false);
            setIsNature(true);
            }}>
              <Text style={[styles.smallButtonText,isNature ? styles.activeText : null]}>자연</Text>
            </TouchableOpacity>
        </View>

        <Text style={styles.explain}>안전한 길은 보안등,경찰서 위치 데이터를 기반으로 추천됩니다.</Text>



        </View>

        <View style={styles.time}>
            <Text style = {styles.text}>시간 설정</Text>

            <View style={styles.box}>

            <Text style={{alignItems:'center',textAlign:'center',marginTop:10,marginBottom:10}}>
                <Text style={styles.selectedTime}>{selectedTime}</Text>
                <Text style={styles.textTime}> 분</Text>
            </Text>
            <Text style={{textAlign:'center', marginBottom:10}}>산책 시간을 설정해주세요.</Text>
                <Slider
                    style={styles.slider}
                    minimumValue={minimumValue}
                    maximumValue={maximumValue}
                    step={5}
                    value={selectedTime}
                    onValueChange={handleTimeChange} 
                    thumbTintColor='#4B9460'
                />

            <Text>
                <Text style={styles.mTime}> {minimumValue}분</Text>
                <View style={{ width:250 }}></View>
                <Text style={styles.mTime}> {maximumValue}분</Text>
            </Text>    



            </View>
        </View>




        <TouchableOpacity style={styles.button} onPress={newPath}>
              <Text style={styles.buttonText}>경로 확인하기</Text>
        </TouchableOpacity>
    </View>
  )
}


const styles = StyleSheet.create({


    //모달
    centeredView:{
        width:500,
        height:800,
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalView: {
        width:328,
        height:250,
        marginTop:230,
        backgroundColor:'#FFFFFF',
        borderRadius:15,
        marginLeft:35,

    },
    modalText:{
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'700',
        fontSize:20,
        lineHeight:24,
        marginTop:25,
        marginLeft:120,
    },
    modalComm:{
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'600',
        fontSize:16,
        lineHeight:24,
        marginTop:30,
        marginLeft:18,
        marginRight:15,

    },
    modalButton:{
        width:150,
        height:40,
        alignItems: 'center', 
        backgroundColor:'#FF8E4F',
        marginTop:40,
        marginLeft:90,
        borderRadius:10,
        borderColor:'#C4C4C4',
        borderWidth:1.5,
    },
    modalButtonText:{
        fontFamily :'Inter',
        fontStyle: 'normal',
        fontWeight:'500',
        fontSize:17,
        lineHeight:19,
        textAlign:'center',
        color:'#FFFFFF',
        marginTop:10,
        letterSpacing:1.5

    },

    text:{
        fontFamily :'Inter',
        fontStyle:'normal',
        fontWeight:'700',
        fontSize:25,
        lineHeight:29,
        color:'#616161',
        marginTop :50,
        marginBottom:10,
        marginLeft : 20,

    },

    thema:{

    },

    time:{

    },


    selectedTime:{
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'800',
        fontSize:28,
        lineHeight:48,
        letterSpacing:0.25,
        color:'#4B9460',
    },
    textTime:{
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'800',
        fontSize:28,
        lineHeight:48,
        letterSpacing:0.25,

    },

    slider:{
        

    },

    mTime:{
        width:26,
        height:19,
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'500',
        fontSize:16,
        lineHeight:19,
        color: '#989898'


    },

    activeButton:{
        backgroundColor:'#FF8E4F'
    },
    activeText:{
        color:'white'
    },
    smallButton:{
        width:150,
        height:40,
        backgroundColor:'#FFFFFF',
        marginTop:10,
        marginBottom:10,
        marginLeft:10,
        marginRight:10,
        borderRadius:100,
        borderColor:'#C4C4C4',
        borderWidth:1,
    },

    smallButtonText:{
        color:'#FF8E4F',
        width:80,
        height:30,
        fontFamily:'Roboto',
        fontStyle:'normal',
        fontWeight:'800',
        fontSize:20,
        textAlign:'center',
        marginLeft:33,
        alignItems:'center',
        marginTop:3.5

    },
    samllButtonContainer:{
        flexDirection: 'row', // 자식 요소를 가로 방향으로 배치
        marginLeft:30,
    },
    explain:{
        width:315,
        height:28,
        fontFamily:'Inter',
        fontStyle:'normal',
        fontWeight:'500',
        fontSize:12,
        lineHeight:28,
        alignItems:'center',
        textalign:'center',
        marginLeft:45,
        marginTop:20,
        color:'#B8B8B8'
    },
    box:{
        width:328,
        height:139,
        backgroundColor:'#F1F1F1',
        marginLeft:35,
        marginTop: 20,
        borderRadius:10,

    },

    button:{
        width:295,
        height:53,
        alignItems: 'center', 
        backgroundColor:'#4B9460',
        marginLeft:53,
        marginTop :70,
        borderRadius :10,
    },

    buttonText:{
        fontFamily :'Inter',
        fontStyle: 'normal',
        fontWeight:'700',
        fontSize:20,
        lineHeight:28,
        textAlign:'center',
        letterSpacing :0.1,
        color:'#FFFFFF',
        marginTop:12,
        marginBottom:12,
        letterSpacing:1.5

    }



})



export default WalkingSetting