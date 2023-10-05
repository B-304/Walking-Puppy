import { View, Text ,StyleSheet,TouchableOpacity,Image} from 'react-native'
import React,{useEffect, useState} from 'react'
import axios from 'axios'
import{accessToken} from 'react-native-dotenv';
import { useNavigation } from "@react-navigation/native";

const SavedWalkingSetting:React.FC = (): JSX.Element => {
  
  const navigation = useNavigation();
  const BEARER_TOKEN =accessToken;
  const[data,setData] = useState({
    startLocation:'',
    arrivaLocation:'',
    estimatedTime:'',
    estimatedDistance:'',
    themeName:'',
    walkId:'',
  })

  useEffect(() => {

    console.log(accessToken);
    axios
      .get('https://j9b304.p.ssafy.io/api/walk/71',{ headers: {
            Authorization: `Bearer ${BEARER_TOKEN}`,
          },
         })
      .then((response)=>{
        const responseData = response.data;
        console.log(response.data);
        setData({
          startLocation:"유성구 덕명동" ,
          arrivaLocation:"유성구 덕명동" ,
          estimatedTime:responseData.estimatedTime,
          estimatedDistance:responseData.estimatedDistance,
          themeName:responseData.themeName,
          walkId:responseData.walkId,

        });

      })
      .catch((error) => {
        console.log('데이터 가져 오는중 오류',error);
      });
  },[]);

  return (
    <View style = {styles.container}>
      <Image style={styles.img}
        source={require('../../assets/route.png')}
      />
      <View style = {styles.walk}>
        <View style = {{flexDirection: 'row',}}>
          <View style={styles.circle} />
          <Text style= {styles.walkinfo}>출발지</Text>
          <Text style= {styles.walkinfo}>{data.startLocation}</Text>
        </View>
        <View style = {{flexDirection: 'row',}}>
          <View style={styles.circle} />
          <Text style= {styles.walkinfo}>도착지</Text>
          <Text style= {styles.walkinfo}>{data.arrivaLocation}</Text>
        </View>
        <View style = {{flexDirection: 'row',}}>
          <View style={styles.circle} />
          <Text style= {styles.walkinfo}>예상 소요시간</Text>
          <Text style= {styles.walkinfo}>{data.estimatedTime} 분</Text>
        </View>
        <View style = {{flexDirection: 'row',}}>
          <View style={styles.circle} />
          <Text style= {styles.walkinfo}>이동거리</Text>
          <Text style= {styles.walkinfo}>{data.estimatedDistance} km</Text>
        </View>
        <View style = {{flexDirection: 'row',}}>
          <View style={styles.circle} />
          <Text style= {styles.walkinfo}>테마</Text>
          <Text style= {styles.walkinfo}>{data.themeName}</Text>
        </View>
 
      </View>


      <TouchableOpacity style={styles.button}>
              <Text style={styles.buttonText} onPress={() => navigation.navigate('산책 중', data.walkId)}>산책 시작하기</Text>
        </TouchableOpacity>
    </View>
  )
}


const styles = StyleSheet.create({


  circle: {
    width: 8, 
    height: 8, 
    borderRadius: 10, 
    backgroundColor:'#4B9460',
    marginTop:5

  },

  walk:{
    width:280,
    height:137,
    marginLeft:55,
    marginTop:30,
   

  },
  walkinfo:{
    width:90,
    height:20,
    marginBottom:10,
    fontFamily:'Inter',
    fontStyle:'normal',
    fontsize:16,
    lineheight:19,
    fontWeight:'700',
    marginLeft:10

  },

  container:{
    width:400,
    height:800,
    backgroundColor:'white'

  },
  img:{
    width:300,
    height:350,
    marginTop:25,
    marginLeft:43,
    

  },


  button:{
    width:295,
    height:53,
    alignItems: 'center', 
    backgroundColor:'#4B9460',
    marginLeft:53,
    marginTop :35,
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

export default SavedWalkingSetting