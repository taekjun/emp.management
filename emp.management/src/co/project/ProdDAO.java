package co.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import co.yedam.DAO;

public class ProdDAO {
	Connection conn;
	PreparedStatement psmt;
	Statement stmt;
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
	
	// 로그인
	public boolean loginUser(String id, String password) {
		try {
			conn = DAO.getConn();
			
			sql = "select * "
				+ "from users "
				+ "where id = ? "
				+ "and password = ?";
		
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, password);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				if (rs.getInt(7) == 0) {
					System.out.println("관리자 계정입니다.");
				} else if(rs.getInt(7) == 1){
					System.out.println("판매자 계정입니다.");
				} else if(rs.getInt(7) == 2) {
					System.out.println("구매자 계정입니다.");
				}
				return true;
			}
			return false;
		} catch (SQLException e) {
		e.printStackTrace();
		} finally {
			disconn();
		}
		return false;
	}
	
	// 로그인 유저 타입 체크
		public int userType(String id, String password) {
			try {
				conn = DAO.getConn();
				
				sql = "select * "
					+ "from users "
					+ "where id = ? "
					+ "and password = ?";
			
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, id);
				psmt.setString(2, password);
				rs = psmt.executeQuery();
				
				while (rs.next()) {
					return rs.getInt(7);
				}
			} catch (SQLException e) {
			e.printStackTrace();
			} finally {
				disconn();
			}
			return -1;
		}
	
	// 회원 가입
	public boolean insertUser(User us) {
		conn = DAO.getConn();
		sql = "insert into users (user_no,"
				+ "                id,"
				+ "                password,"
				+ "                name,"
				+ "                address,"
				+ "                phone,"
				+ "                user_type)"
				+ "values (?, ?, ?, ?, ?, ?, ?)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, us.getUserNo());	//기본키
			psmt.setString(2, us.getId());	//유니크
			psmt.setString(3, us.getPw());
			psmt.setString(4, us.getName());
			psmt.setString(5, us.getAddr());
			psmt.setString(6, us.getPhone());		
			psmt.setInt(7, us.getUserType());	
			
			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("!!회원번호 및 ID 중복 확인!!");
		} finally {
			disconn();
		}
		return false;
	}
	
	// 입력
	// 판매물품 등록
	public boolean insertProduct(Product pro) {
		conn = DAO.getConn();
		sql = "insert into product (p_name,"
				+ "                 stock,"
				+ "                 price,"
				+ "                 user_no)"
				+ "values (?, ?, ?, ?)";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pro.getPName());
			psmt.setInt(2, pro.getStock());
			psmt.setInt(3, pro.getPrice());
			psmt.setInt(4, pro.getUserNo()); //관리자 계정만 사용
			
			int r = psmt.executeUpdate();
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
	
	// 물품명 입력했을때 반환되는 유저 번호
	public String userNum(String pname) {
		try {
			conn = DAO.getConn();
						
			sql = "select user_no "
				+ "from product "
				+ "where p_name = nvl(?, p_name)";
					
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pname);
			rs = psmt.executeQuery();
						
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
		e.printStackTrace();
		} finally {
			disconn();
		}
		return null;
	}
	
	// 물품리스트에 user_no만 추출해서 중복 등록 방지 xxxx테스트
	public List<Product> uNum(String pname) {
		List<Product> list = new ArrayList<>();
		try {
			conn = DAO.getConn();
						
			sql = "select user_no "
				+ "from product "
				+ "where p_name = nvl(?, p_name)";
						
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pname);
			rs = psmt.executeQuery();
							
			while(rs.next()) {
				Product pro = new Product();	
				pro.setUserNo(rs.getInt("user_no"));
				list.add(pro); // DB -> Collection
			}
		} catch (SQLException e) {
		e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}
		
	// 목록보기
	public List<Product> getList(String pname, int page){
		conn = DAO.getConn();
		List<Product> list = new ArrayList<>();
		
		sql = "select p.p_name,\r\n"
				+ "       p.stock,\r\n"
				+ "       p.price,\r\n"
				+ "       u.name\r\n"
				+ "from(select rownum rn, \r\n"
				+ "            a.*\r\n"
				+ "     from (select *\r\n"
				+ "           from product\r\n"
				+ "           where p_name = nvl(?, p_name)) a ) p join users u \r\n"
				+ "                                                   on (p.user_no = u.user_no)\r\n"
				+ "where rn > (?-1)*5 \r\n"
				+ "and rn <= (?)*5 "
				+ "order by p.p_name";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pname);
			psmt.setInt(2, page);
			psmt.setInt(3, page);
			rs = psmt.executeQuery();
			while(rs.next()) {
				Product pro = new Product();	
				pro.setPName(rs.getString("p_name"));
				pro.setStock(rs.getInt("stock"));
				pro.setPrice(rs.getInt("price"));
//				pro.setUserNo(rs.getInt("user_no"));
				pro.setName(rs.getString("name"));
				list.add(pro); // DB -> Collection
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconn();
		}
		return list;
	}
	
	public int getTotalCnt(String pname) {
		conn = DAO.getConn();
		sql = "select count(*) as cnt "
				+ "from product "
				+ "where p_name = nvl(?, p_name)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pname);
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
	
	// 수정
	public boolean updatePro(Product pro) {
		conn = DAO.getConn();
		sql = "update product "
				+ "set stock = nvl(?, stock)";
		if(pro.getPrice() != 0) {
			sql += ", price = ?";
		}
		sql += " where p_name = ?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, pro.getStock());
			if(pro.getPrice() != 0) {
				psmt.setInt(2, pro.getPrice());
			}
			psmt.setString(3, pro.getPName());
			int r = psmt.executeUpdate();
			if(r > 0) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("다른 사용자가 등록한 물품입니다.");
		} finally {
			disconn();
		}
		return false;
	}
	
	// 로그인 유저 번호 체크 (자기가 등록한 물건만 수정 가능하도록 기능추가)
	public String noCheck(String id, String password) {
		try {
			conn = DAO.getConn();
					
			sql = "select * "
				+ "from users "
				+ "where id = ? "
				+ "and password = ?";
				
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, id);
			psmt.setString(2, password);
			rs = psmt.executeQuery();
					
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
		e.printStackTrace();
		} finally {
			disconn();
		}
		return null;
	}
	
	// 삭제
	public boolean deletePro(Product pro) {
		conn = DAO.getConn();
		sql = "delete product "
				+ "where p_name = nvl(?, p_name)";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, pro.getPName());
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
	
	// 물품 구매
	public boolean buyPro(Product pro) {
		conn = DAO.getConn();
		sql = "update product\r\n"
				+ "set stock = nvl((stock - ?), stock) \r\n"
				+ "where p_name = ?";	
	
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, pro.getStock());
			psmt.setString(2, pro.getPName());
			
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
}
