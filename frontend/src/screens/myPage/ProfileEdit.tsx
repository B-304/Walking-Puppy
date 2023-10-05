import { View, Text, TextInput,TouchableOpacity,Modal } from 'react-native'
import React, {useEffect,useState} from 'react'
import axios from 'axios'
import { StyleSheet } from 'react-native';
import {Avatar} from 'react-native-paper';
import { useNavigation } from '@react-navigation/native';

const ProfileEdit:React.FC = (): JSX.Element => {
//  const [loading, setLoading] = useState(false);
    const [userData, setUserData] = useState(null);
    const [editedData, setEditedData] = useState({
        nickname: '',
        walkCount: '',
      });

    const [dogResponseData, setDogResponseData] = useState<any>({ name: '', dayCount: 0, dogLevel: 0, exp: 0, levelRange: 0 });
    const [dogEditedData, setDogEditedData] = useState({
          name: '',
      });
      const navigation = useNavigation();
      const [modalVisible, setModalVisible] = useState(false); // 모달 상태 추가
const BEARER_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbWluMzY3MkBuYXZlci5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwidHlwZSI6IkFDQ0VTUyIsInVzZXJJZCI6MiwiZXhwIjoxNjk2NDAzNzEwfQ.fROTgdimSGBJuKux_AZUFTj1rJRmfS7is6BfxWNtvq0';
    useEffect(()=> {
        const url = 'https://j9b304.p.ssafy.io/api/2';
        //const url = 'http://10.0.2.2:8080/2';
        axios.get(url, {headers: {
          Authorization: `Bearer ${BEARER_TOKEN}`,
        },})
        .then((response) => {
            //성공
            setUserData(response.data);
        })
        .catch((error)=>{
            //실패
            console.error('프로필 가져오기 실패',error);
        });

        axios
          .get('https://j9b304.p.ssafy.io/api/dog/2', {
            headers: {
              Authorization: `Bearer ${BEARER_TOKEN}`,
            },
          })
          .then((response) => {
            setDogResponseData(response.data);
//             setLoading(false);
          })
          .catch((error) => {
            console.error('강아지 데이터 가져오기 실패:', error);
//             setLoading(false);
        });
    }, []);

    const handleNicknameChange = (text: string) => {
        setEditedData({ ...editedData, nickname: text });
      };
    
    const handleWalkCountChange = (text: string) => {
      const walkCount = parseInt(text);
      setEditedData({ ...editedData, walkCount});
    };

    const handleDognameChange = (text: string) => {
        setDogEditedData({ ...editedData, name: text });
    };

      const handleSaveProfile = () => {

        axios
          .patch('https://j9b304.p.ssafy.io/api/2', editedData)
          .then((response) => {
            // 성공적으로 수정되었을 때의 처리
            console.log('프로필 수정 성공:', response.data);
            // 수정된 정보를 화면에 반영
            setUserData(response.data);
            navigation.navigate('마이페이지');
          })
          .catch((error) => {
            // 오류 발생 시의 처리
            console.error('프로필 수정 실패', error);
          });

          // 강아지 이름 수정
           const url = 'https://j9b304.p.ssafy.io/api/dog/2';
           //const url = 'http://10.0.2.2:8080/2';
            axios
              .post(url, dogEditedData,
              {
                headers: {
                Authorization: `Bearer ${BEARER_TOKEN}`,
              },
              })
              .then((response) => {
                // 성공적으로 수정되었을 때의 처리
                console.log('강아지 이름 수정 성공:', response.data);
                // 수정된 정보를 화면에 반영
                setDogResponseData(response.data);
                navigation.navigate('마이페이지');
              })
              .catch((error) => {
                // 오류 발생 시의 처리
                console.error('강아지 이름 수정 실패', error);
              });

      };


      const handleLogout = () => {
        // 로그아웃 로직을 구현
        console.log('로그아웃');
        // 로그아웃 후 필요한 처리를 수행할 수 있습니다.
      };
      
      const handleWithdrawModal = () =>{
        setModalVisible(true)
        return;
      }
    
      const handleWithdraw = () => {

        const url = 'https://j9b304.p.ssafy.io/api/2'; // 탈퇴 API 엔드포인트 URL로 변경해야 합니다.
        //const url = 'http://10.0.2.2:8080/2';
        axios
          .delete(url)
          .then((response) => {
            // 탈퇴 성공 시의 처리
            console.log('탈퇴 성공:', response.data);
            // 필요한 후속 처리를 수행할 수 있습니다.
          })
          .catch((error) => {
            // 탈퇴 실패 시의 처리
            console.error('탈퇴 실패', error);
            // 에러 처리를 수행하거나 사용자에게 메시지를 표시할 수 있습니다.
          });
      };
    


  return (

    <View style={{backgroundColor:'white',height:700}}>
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
                        <Text style={styles.modalText}>회원 탈퇴</Text>
                        <Text style={styles.modalComm}>정말 회원 탈퇴 하시겠습니까?
                        {'\n'}
                         회원 탈퇴시 회원 정보는 모두 소멸되며, 복구가 불가능 합니다.</Text>
                        <View style={styles.modalsmallButton}>
                          <TouchableOpacity style={styles.modalButton} onPress={() => {
                              setModalVisible(!modalVisible);
                          }}>
                              <Text style={styles.modalButtonText}>취소</Text>
                          </TouchableOpacity>

                          <TouchableOpacity style={styles.modalButton} onPress={handleWithdraw}>
                              <Text style={styles.modalButtonText}>확인</Text>
                          </TouchableOpacity>
                        </View>

                    </View>
                </View>
            </Modal>

      {userData ? (
          <View >
            <View style= {styles.userContainer}>
              <View style = {styles.profile}>
                <Avatar.Image
                  source={require("./assets/image-user.png")}
                  style = {styles.image}
                  size={100}
                />
              </View>
              <View style ={styles.lineContainer}>
                <Text style = {styles.text1}>닉네임</Text>
                <TextInput style={styles.input}
                  placeholder={userData.nickname}
                  value={editedData.nickname}
                  onChangeText={handleNicknameChange}
                />
                <Text style = {styles.text2}>님</Text>
              </View>
              <View style ={styles.lineContainer}>
                <Text style = {styles.text1}>목표 걸음수</Text>
                <TextInput style={styles.input}
                    placeholder={userData.walkCount.toString()}
                    value={editedData.walkCount.toString()}
                    onChangeText={handleWalkCountChange}
                  />
                <Text style = {styles.text2}>걸음</Text>
              </View>
              <View style ={styles.lineContainer}>
                              <Text style = {styles.text1}>강아지 이름</Text>
                              <TextInput style={styles.input}
                                  placeholder={dogResponseData.name}
                                  value={dogEditedData.name}
                                  onChangeText={handleDognameChange}
                                />
                              <Text style = {styles.text2}></Text>
                            </View>
          </View>


          <TouchableOpacity onPress={handleSaveProfile} style={styles.button}>
              <Text style={styles.buttonText}>저장하기</Text>
          </TouchableOpacity>
          <View style = {styles.textContainer}>
              <TouchableOpacity onPress={handleWithdrawModal}>
                  <Text style={styles.label}>탈퇴하기</Text>
              </TouchableOpacity>
              <Text style={styles.label}>|</Text>
              <TouchableOpacity onPress={handleLogout}>
                  <Text style={styles.label}>로그아웃</Text>
              </TouchableOpacity>
          </View>
        </View>
      ) : (
        null
      )}
  </View>

  );
};

const styles = StyleSheet.create({
  //모달
  centeredView:{
    width:500,
    height:800,
    backgroundColor: 'rgba(0,0,0,0.5)',
},
modalView: {
    width:328,
    height:234,
    marginTop:250,
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
    marginTop:20,
    marginLeft:18,
    marginRight:15,

},
modalButton:{
    width:130,
    height:40,
    backgroundColor:'#FF8E4F',
    marginTop:35,
    marginLeft:24,
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
  modalsmallButton:{
    flexDirection: 'row',

  },

    image:{
      backgroundColor:'#D9D9D9',
      borderRadius:50,
      marginLeft:20,
      marginRight:20,
      

    },
    userContainer:{
      flexDirection:'column',
      height :500,
      marginTop:40,
      
    },
    profile:{
      alignItems: 'center',
      marginBottom:20,

    },
    lineContainer:{
      flexDirection:'row',
      marginTop :10,

    },
    input:{
      width:170,
      height:46,
      fontFamily:'Inter',
      fontStyle:'normal',
      fontWeight : '400',
      fontSize : 16,
      backgroundColor:'#F6F6F6',
      borderColor:'#E8E8E8',
      borderWidth:2,
      padding:10,
      borderRadius:8,
      
    },
    text1:{
      fontFamily:'Inter',
      width:92,
      height:20,
      fontStyle:'normal',
      fontWeight : '900',
      fontSize : 16,
      lineHeight :19,
      color: '#292D32',
      marginLeft:37,
      marginTop:15,
    },

    text2:{
      fontFamily:'Inter',
      width:92,
      height:20,
      fontStyle:'normal',
      fontWeight : '900',
      fontSize : 16,
      lineHeight :19,
      color: '#292D32',
      marginLeft :10,
      marginTop:15,
    },

    button:{
       width:295,
       height:53,
       alignItems: 'center', 
       backgroundColor:'#4B9460',
       marginLeft:48,
       marginTop :350,
       borderRadius :10,
       position:'absolute'
       
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


    },
    label:{
        fontFamily:'Inter',
        fontStyle : 'normal',
        fontWeight:'800',
        fontSize:13,
        color:'#616161',
        lineHeight:16,
        display:'flex',
        alignItems:'center',
        textAlign:'center',
        letterSpacing:0.2,
        marginRight:5,
        letterSpacing:1.5
    },
    textContainer:{
        marginTop:50,
        marginLeft:133,
        flexDirection: 'row', // 자식 요소를 가로 방향으로 배치
    }

})


export default ProfileEdit