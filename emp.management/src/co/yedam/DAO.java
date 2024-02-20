package co.yedam;

import java.sql.Connection;
import java.sql.DriverManager;

// Connection: db연결
public class DAO {

	public static Connection conn;

	public static Connection getConn() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.70:1521:1521:xe", "yedam", "yedam"); // 주소값,아이디,패스워드
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
