package kr.or.mrhi.sixclass;

import java.io.FileReader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtility {
	public static Connection getConnection() {
		Connection con = null;
		
		//Properties driver, url, userID, userPassword read
				try {
				//주석1
				Properties properties = new Properties();
				String filePath = DataBaseTest.class.getResource("db.properties").getPath();
				filePath = URLDecoder.decode(filePath,"utf-8");
				properties.load(new FileReader(filePath));
				
				//바인딩
				String drvier = properties.getProperty("DRIVER");
				String url = properties.getProperty("URL");
				String userID = properties.getProperty("userID");
				String userPassword = properties.getProperty("userPassword");
				
				//드라이버 로드하기
				Class.forName(drvier);
				
				
				//데이타베이스 연결하기
				con = DriverManager.getConnection(url, userID, userPassword);
				
				
				}catch (Exception e) {
					System.out.println("Mysql Database connection fail");
					e.printStackTrace();
				}
		
		return con;
	}
	
}
