import React, { useEffect, useState } from "react";
import { useNavigation } from "@react-navigation/native";
import {
  View,
  Text,
  FlatList,
  Image,
  StyleSheet,
  TouchableOpacity,
} from "react-native";
import axios from "axios";

type Walk = {
  walkId: number;
  name: string | null;
  distance: number;
  time: number;
  imageUrl: string;
};


const WalkListItem: React.FC<{ walk: Walk }> = ({ walk }) => {
  const [isHighlighted, setIsHighlighted] = useState<boolean>(false);
  const navigation = useNavigation();

  const toggleHighlight = () => {
    console.log(walk.walkId);
    navigation.navigate("산책로 상세",walk.walkId);
    setIsHighlighted(!isHighlighted);
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[
          styles.walkItem,
          {
            borderColor: isHighlighted ? "#4B9460" : "#ccc",
          },
        ]}
        onPress={toggleHighlight}
      >
        <View style={styles.imageContainer}>
          {walk.imageUrl ? (
            <Image source={{ uri: walk.imageUrl }} style={styles.image} />
          ) : (
            <View style={styles.imagePlaceholder} />
          )}
        </View>
        <View style={styles.walkInfo}>
          <Text style={styles.walkInfoName}>
            {walk.name ? walk.name : "제목없음"}
          </Text>
          <Text style={styles.walkInfoTime}>
            <Text>소요 시간 </Text>
            <Text style={{ marginLeft: 5 }}>
              {walk.time} 분 {"\n"}
            </Text>
            <Text>이동 거리 </Text>
            <Text style={{ marginLeft: 5 }}>{walk.distance} km</Text>
          </Text>
        </View>
      </TouchableOpacity>
    </View>
  );
};

const NewWalkingSetting: React.FC = (): JSX.Element => {
  const [walks, setWalks] = useState<Walk[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const BEARER_TOKEN: string =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpbWluMzY3MkBuYXZlci5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwidHlwZSI6IkFDQ0VTUyIsInVzZXJJZCI6MiwiZXhwIjoxNjk2NjA4NDI2fQ.V6oAsUPGAMxYomvzX25Hny1z1RaJFJMLYXvSizEsyY4";

    axios
      .get("https://j9b304.p.ssafy.io/api/walk/scrap-list", {
        headers: {
          Authorization: `Bearer ${BEARER_TOKEN}`,
        },
      })
      .then((response) => {
        setWalks(response.data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("API 요청 중 오류 발생:", error);
        setLoading(false);
      });
  }, []);

  return (
    <View style={styles.container}>
      {loading ? (
        <Text>Loading...</Text>
      ) : (
        <FlatList
          data={walks}
          keyExtractor={(item) => item.walkId.toString()}
          renderItem={({ item }) => <WalkListItem walk={item} />}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 0,
    backgroundColor: "#ffffff",
  },
  walkItem: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 2,
    borderColor: "#ccc",
    borderRadius: 10,
    padding: 10,
    margin: 10,
    backgroundColor: "#ffffff",
  },
  walkInfo: {
    flex: 1,
  },
  walkInfoName: {
    fontWeight: "bold",
    textAlign: "center",
    fontSize: 18,
  },
  walkInfoTime: {
    marginTop: 15,
    textAlign: "center",
    fontSize: 14,
  },
  imageContainer: {
    marginRight: 10,
    flexDirection: "row",
  },
  image: {
    width: 160,
    height: 100,
    borderRadius: 10,
  },
  imagePlaceholder: {
    width: 160,
    height: 100,
    backgroundColor: "#ccc",
    borderRadius: 10,
  },
});

export default NewWalkingSetting;
