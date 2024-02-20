package co.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {

	public static Connection conn;

	public static Connection getConn() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.70:1521:xe", "yedam", "yedam"); // 주소값, db계정, db비밀번호
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
