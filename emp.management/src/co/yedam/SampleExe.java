package co.yedam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SampleExe {

	static void insertEmp(Connection conn) throws SQLException{
		String sql = "insert into employee (emp_no"
				+ "                       , emp_name"
				+ "                       , email"
				+ "                       , phone"
				+ "                       , salary"
				+ "                       , department) "
				+ "values (?, ?, ?, ?, ?, ?)";
		PreparedStatement psmt = conn.prepareStatement(sql);
		psmt.setInt(1, 778);
		psmt.setString(2, "박과장");
		psmt.setString(3, "park@email.com");
		psmt.setString(4, "123-4567");
		psmt.setString(5, "456"); //오라클에서 int처리 해준다
		psmt.setString(6, "인사");
		
		int r = psmt.executeUpdate(); // insert, update, delete
		System.out.println("처리된 건수: " + r);
		if (r > 0) {
			conn.commit(); // 커밋처리.
			System.out.println("정상적 입력.");
		} else {
			conn.rollback(); // 롤백처리
		}
	}

	public static void main(String[] args) {

		String dept = "인사"; // select * from employee where department = '인사'
//		String sql = "select * from employee where department = ? and emp_no >= ?"; // sql에서 인식가능하도록 작은따옴표 사용해야해서 불편하다.
		int page = 1;																			// 그래서 물음표 넣고 뒤에 psmt.setString(1,
		EmpDAO edao = new EmpDAO();																			// dept);
		List<Employee> list = edao.getList(dept, page);
		
		System.out.println("사원번호 이름   전화번호  부서");
		System.out.println("=========================");
		for(Employee emp : list) {
			System.out.println(emp.getEmpNo() + " " + emp.getEmpName() + " " + emp.getPhone() + " " + emp.getDepartment());
		}
		System.out.println("=========================");
		
		// Connection객체
		Connection conn = DAO.getConn();

		// 쿼리를 해석하고 처리된 결과를 반환.
		try {
			conn.setAutoCommit(false); // autocommit 해제
			insertEmp(conn);

			PreparedStatement psmt = conn.prepareStatement(dept);
			psmt.setString(1, dept); // 첫번째 물음표
			psmt.setInt(2, 101); // 두번째 물음표
			ResultSet rs = psmt.executeQuery(); // 조회
			System.out.println("사원번호       사원명");
			System.out.println("=================");
			while (rs.next()) {
				System.out.println("사원번호: " + rs.getInt("emp_no") + ", 사원명: " + rs.getString("emp_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("=================");
	}
}
