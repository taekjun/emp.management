package co.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import co.yedam.DAO;

public class test {
	private static final String DRIVER = "oracle.jdbc.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "yedam";
	private static final String PW = "yedam";

	public static void main(String[] args) {

		ProdDAO pdao = new ProdDAO();
		Scanner scn = new Scanner(System.in);
		boolean run = true;
		int menu = 0;	

		while (true) {
			System.out.println("1.회원가입 2.로그인 3.종료");
			System.out.println("선택>> ");
			int smenu = scn.nextInt();
			scn.nextLine();
			if (smenu == 1) {
				System.out.println("회원번호 입력>> ");
				int userno = scn.nextInt();
				scn.nextLine();
				System.out.println("ID입력>> ");
				String id = scn.nextLine();
				System.out.println("PW입력>> ");
				String pw = scn.nextLine();
				System.out.println("이름>> ");
				String name = scn.nextLine();
				System.out.println("주소>> ");
				String addr = scn.nextLine();
				System.out.println("연락처>> ");
				String phone = scn.nextLine();
				System.out.println("회원타입 입력>> (판매자:1,구매자:2)");
				int usertype = scn.nextInt();
				scn.nextLine();

				User us = new User(userno, id, pw, name, addr, phone, usertype);
				if (pdao.insertUser(us)) {
					System.out.println("=====등록 완료=====");
				} else {
					System.out.println("!!!!!등록 실패!!!!!");
				}
			} else if (smenu == 2) {
				System.out.println("ID>> ");
				String id = scn.nextLine();
				System.out.println("PW>> ");
				String pw = scn.nextLine();
				
				if (pdao.loginUser(id, pw)) {
					System.out.println("=====로그인 성공=====");
					int type = pdao.userType(id, pw);
					if (type == 0) {
						System.out.println("1.물품등록 2.물품목록 3.물품수정(재고,가격) 4.물품삭제 5.물품구매 9.로그아웃");
					} else if (type == 1) {
						System.out.println("판매자");
					} else if (type == 2) {
						System.out.println("구매자");
					}
				} else {
					System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
					continue;
				}
			} else if (smenu == 3) {
				System.out.println("프로그램을 종료합니다");
				break;
			}

		} // end of while (회원가입)
	}
}
