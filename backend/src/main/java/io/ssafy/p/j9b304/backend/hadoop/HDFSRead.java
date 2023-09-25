package io.ssafy.p.j9b304.backend.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataInputStream;

public class HDFSRead {
        public static void main(String[] args) {
            try {
                // Hadoop Configuration 객체 생성
                Configuration conf = new Configuration();
                conf.set("fs.defaultFS", "hdfs://j9b304a.p.ssafy.io:9000"); // Hadoop 마스터 노드 주소 설정

                // HDFS 파일 시스템 객체 생성
                FileSystem fs = FileSystem.get(conf);
                // 읽을 파일의 경로
                Path filePath = new Path("/data/police_station.csv");

                // 파일을 읽기 위한 입력 스트림 열기
                FSDataInputStream inputStream = fs.open(filePath);

                // 데이터 읽기
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    System.out.write(buffer, 0, bytesRead);
                }
                // 입력 스트림 닫기
                inputStream.close();
                // 파일 시스템 닫기
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
