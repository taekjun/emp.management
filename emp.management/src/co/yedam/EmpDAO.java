package co.yedam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// Database 처리기능.
public class EmpDAO {
	// 필드
	Connection conn;
	PreparedStatement psmt;
	ResultSet rs;
	String sql;
	
	void disconn() {
			try {
				if (conn != null) {
					conn.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}		
	}
	
	// 입력
	public boolean insertEmp(Employee emp) {
		conn = DAO.getConn(); // Connection 객체 생성
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sql = "insert into employee (emp_no"
				+ "                       , emp_name"
				+ "                       , email"
				+ "                       , phone"
				+ "                       , salary"
				+ "                       , hire_date"
				+ "                       , department) "
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, emp.getEmpNo());
			psmt.setString(2, emp.getEmpName());
			psmt.setString(3, emp.getEmail());
			psmt.setString(4, emp.getPhone());
			psmt.setInt(5, emp.getSalary());
			psmt.setString(6, sdf.format(emp.getHireDate()));
			psmt.setString(7, emp.getDepartment());
			
			int r = psmt.executeUpdate(); // 데이터 반영
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		
		return false;
	}
	
	// 목록
	public List<Employee> getList(String dept, int page) {
		// "인사" vs. ""
		conn = DAO.getConn(); // Connection 객체 생성
		List<Employee> list = new ArrayList<>();
		sql = "select emp_no,\r\n"
				+ "       emp_name,\r\n"
				+ "       email,\r\n"
				+ "       phone,\r\n"
				+ "       hire_date,\r\n"
				+ "       salary,\r\n"
				+ "       department\r\n"
				+ "from(select rownum rn, a.*\r\n"
				+ "     from (select *\r\n"
				+ "           from employee\r\n"
				+ "           where department = nvl(?, department)\r\n"
				+ "           order by emp_no) a ) b\r\n"
				+ "where b.rn > (?-1)*5 and b.rn <= (?)*5" ;
		
//		if(!dept.equals("")) { // 부서값이 있으면..
//			sql += "where department = ?";
//		}
		//sql += "where department = nvl(?, department)"; //if문 안들어갔을시
//		sql	+=  "order by emp_no";
		try {
			psmt = conn.prepareStatement(sql);
//			if(!dept.equals("")) { // 부서값이 있으면..
				psmt.setString(1, dept);
				psmt.setInt(2, page);
				psmt.setInt(3, page); //물음표 갯수와 같아야 한다
//			}
			rs = psmt.executeQuery();
			while(rs.next()) {
				Employee emp = new Employee();
				emp.setEmpNo(rs.getInt("emp_no"));
				emp.setEmpName(rs.getString("emp_name"));
				emp.setEmail(rs.getString("email"));
				emp.setPhone(rs.getString("Phone"));
				emp.setSalary(rs.getInt("Salary"));
				emp.setHireDate(rs.getDate("hire_date"));
				emp.setDepartment(rs.getString("department"));
				list.add(emp); // DB -> Collection
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}
	
	//수정
	public boolean updateEmp(Employee emp) {
		conn = DAO.getConn();
		sql = "update employee "
				+ "set department = nvl(?, department)";
		if(!emp.getPhone().equals("")) {
			sql += ", phone = ?";
		}
		sql += " where emp_no = ?";
		int p = 1;		
		try {
			psmt = conn.prepareStatement(sql);
			psmt .setString(p++, emp.getDepartment());
			if(!emp.getPhone().equals("")) {
				psmt.setString(p++, emp.getPhone());
			}
			psmt.setInt(p++, emp.getEmpNo());
			int r = psmt.executeUpdate(); // 처리된 건수 반환.
			if(r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}
	
	//삭제
	public boolean deleteEmp(Employee emp) {
		conn = DAO.getConn();
		sql = "delete employee "				
				+ "where emp_no = nvl(?, emp_no)";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, emp.getEmpNo()); //물음표 갯수 조건 확인해서 넣어줘야 한다
			int r = psmt.executeUpdate(); 
			if(r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}
	
	// 부서별 인원.
	public int getTotalCnt(String dept) {
		conn = DAO.getConn();
		sql = "select count(*) as cnt "
				+ "from employee "
				+ "where department = nvl(?, department)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, dept);
			rs = psmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return -1;
	}
}
