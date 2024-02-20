package co.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import co.yedam.DAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
	Connection conn;
	PreparedStatement psmt;
	ResultSet rs;
	String sql;
	public static Statement stmt;
	
	public boolean loginAdmin() {
		conn = DAO.getConn();
		sql = "select * "
			+ "from users "
			+ "where user_type = 0 ";
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
//				System.out.println(rs.getInt(1) + "\t" + rs.getString(2) 
//				+ "\t" + rs.getInt(7));
				
				if (rs.getInt(7) == 0) {
					return true;
				} else {
					return false;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
