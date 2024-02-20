package co.project;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProjApp {
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
				System.out.println("ID를 입력하세요>> ");
				String id = scn.nextLine();
				System.out.println("Password를 입력하세요>> ");
				String pw = scn.nextLine();

				if (pdao.loginUser(id, pw)) {

					System.out.println("=====로그인 성공=====");
					int type = pdao.userType(id, pw);
					if (type == 0) { // 관리자 메뉴
						while (run) {
							System.out.println("1.물품 등록 2.물품 목록 3.물품 수정(재고,가격) 4.물품 삭제 5.물품 구매 9.로그아웃");
							System.out.println("선택>> ");
							menu = scn.nextInt();
							scn.nextLine();

							if (menu == 1) {
								System.out.println("물품 이름>> ");
								String pname = scn.nextLine();
								System.out.println("물품 수량>> ");
								int stock = scn.nextInt();
								scn.nextLine();
								System.out.println("물품 가격>> ");
								int price = scn.nextInt();
								scn.nextLine();
								System.out.println("판매자 번호>> ");
								int sno = scn.nextInt();
								scn.nextLine();

								Product pro = new Product(pname, stock, price, sno);

								if (pdao.insertProduct(pro)) {
									System.out.println("=====등록 완료=====");
								} else {
									System.out.println("!!!!!등록 실패!!!!!");
								}
							} else if (menu == 2) {
								System.out.println("물품검색>> ");
								String pname = scn.nextLine();
								int page = 1;

								while (true) {
									System.out.println("품목명 재고   가격   판매자이름");
									System.out.println("========================");
									List<Product> list = pdao.getList(pname, page);
									for (Product pro : list) {
										System.out.println(pro.getPName() + "  " + pro.getStock() + "  "
												+ pro.getPrice() + "  " + pro.getName());
									}
									int totalCnt = pdao.getTotalCnt(pname);
									int lastPage = (int) Math.ceil(totalCnt / 5.0);
									System.out.println("========================");
									for (int i = 1; i <= lastPage; i++) {
										System.out.printf("%3d", i);
									}
									System.out.println();
									System.out.println("페이지>> (0입력:뒤로가기)");
									page = scn.nextInt();
									scn.nextLine();
									if (page == 0) {
										break;
									}
								}
							} else if (menu == 3) {
								System.out.println("물품명 입력>>");
								String name = scn.nextLine();
								System.out.println("재고 수정량>>");
								String stock = scn.nextLine();
								System.out.println("가격 수정>>");
								String price = scn.nextLine();

								Product pro = new Product();
								pro.setPName(name);
								pro.setStock(Integer.parseInt(stock));
								pro.setPrice(Integer.parseInt(price));

								if (pdao.updatePro(pro)) {
									System.out.println("=====정상 수정=====");
								} else {
									System.out.println("!!!!!수정 실패!!!!!");
								}
							} else if (menu == 4) {
								System.out.println("삭제할 물품 (공란시 전체삭제)");
								String name = scn.nextLine();

								Product pro = new Product();
								pro.setPName(name);
								if (pdao.deletePro(pro)) {
									System.out.println("=====삭제 완료=====");
								} else {
									System.out.println("삭제 실패. 등록되어 있지 않은 물품입니다.");
								}
							} else if (menu == 5) {
								System.out.println("구매할 품목>> ");
								String name = scn.nextLine();
								System.out.println("구매할 수량>> ");
								String stock = scn.nextLine();

								Product pro = new Product();
								pro.setPName(name);
								pro.setStock(Integer.parseInt(stock));

								if (pdao.buyPro(pro)) {
									System.out.println("=====구매 완료=====");
								} else {
									System.out.println("구매 실패. 품목이름 및 수량을 다시 확인해주세요.");
								}
							} else if (menu == 9) {
								System.out.println("로그아웃 완료");
								break;
							}
						}
					} else if (type == 1) { // 판매자 메뉴
						while (run) {
							System.out.println("1.물품등록 2.물품목록 3.물품수정(재고,가격) 9.로그아웃");
							System.out.println("선택>> ");
							menu = scn.nextInt();
							scn.nextLine();

							if (menu == 1) {
								int stock, price, sno = 0;

								System.out.println("물품 이름>> ");
								String pname = scn.nextLine();

//								System.out.println(pdao.noCheck(id, pw));// 접속된 계정의 user_no 확인
//								System.out.println(pdao.uNum(pname));// 물품테이블에서 추출된 user_no arrylist
//								int a = Integer.parseInt(pdao.noCheck(id, pw));

//								List<Product> list = pdao.uNum(pname);
//								for (Product pro2 : list) {
//									System.out.println(pro2.getUserNo());
//									if (pro2.getUserNo() == a) {
//										System.out.println("이미 등록된 상품입니다.");
//										break;
//									}
//									System.out.println("asgr");
//								}
//								System.out.println("arg");
								
								System.out.println("물품 수량>> ");
								stock = scn.nextInt();
								scn.nextLine();
								System.out.println("물품 가격>> ");
								price = scn.nextInt();
								scn.nextLine();
								sno = Integer.parseInt(pdao.noCheck(id, pw)); // sno는 사용자번호 자동 등록

								Product pro = new Product(pname, stock, price, sno);
								if (pdao.insertProduct(pro)) {
									System.out.println("=====등록 완료=====");
								} else {
									System.out.println("!!!!!등록 실패!!!!!");
								}
							} else if (menu == 2) {
								System.out.println("물품검색>> ");
								String pname = scn.nextLine();
								int page = 1;

								while (true) {
									System.out.println("품목명 재고   가격   판매자이름");
									System.out.println("========================");
									List<Product> list = pdao.getList(pname, page);
									for (Product pro : list) {
										System.out.println(pro.getPName() + "  " + pro.getStock() + "  "
												+ pro.getPrice() + "  " + pro.getName());
									}
									int totalCnt = pdao.getTotalCnt(pname);
									int lastPage = (int) Math.ceil(totalCnt / 5.0);
									System.out.println("========================");
									for (int i = 1; i <= lastPage; i++) {
										System.out.printf("%3d", i);
									}
									System.out.println();
									System.out.println("페이지>> (0입력:뒤로가기)");
									page = scn.nextInt();
									scn.nextLine();
									if (page == 0) {
										break;
									}
								}
							} else if (menu == 3) {
								System.out.println("물품명 입력>>");
								String name = scn.nextLine();

								String sellCheck = pdao.userNum(name); // 물품명 입력했을때 받아와지는 판매자 번호
								String sellNo = pdao.noCheck(id, pw); // 현재 접속되어 있는 판매자 번호

								Product pro = new Product();
								pro.setPName(name);

								if (sellCheck.equals(sellNo)) {
									System.out.println("재고 수정량>>");
									String stock = scn.nextLine();
									System.out.println("가격 수정>>");
									String price = scn.nextLine();

									pro.setStock(Integer.parseInt(stock));
									pro.setPrice(Integer.parseInt(price));
								}

								if (pdao.updatePro(pro)) {
									System.out.println("=====정상 수정=====");
								} else {
									System.out.println("!!!!!수정 실패!!!!!");
								}

							} else if (menu == 9) {
								System.out.println("로그아웃 완료");
								break;
							} else if (menu == -1) {
								break;
							}
						}
					} else if (type == 2) { // 구매자 메뉴
						while (run) {
							System.out.println("1.물품목록 2.구매 9.로그아웃");
							System.out.println("선택>> ");
							menu = scn.nextInt();
							scn.nextLine();

							if (menu == 1) {
								System.out.println("물품검색>> ");
								String pname = scn.nextLine();
								int page = 1;

								while (true) {
									System.out.println("품목명 재고   가격   판매자이름");
									System.out.println("========================");
									List<Product> list = pdao.getList(pname, page);
									for (Product pro : list) {
										System.out.println(pro.getPName() + "  " + pro.getStock() + "  "
												+ pro.getPrice() + "  " + pro.getName());
									}
									int totalCnt = pdao.getTotalCnt(pname);
									int lastPage = (int) Math.ceil(totalCnt / 5.0);
									System.out.println("========================");
									for (int i = 1; i <= lastPage; i++) {
										System.out.printf("%3d", i);
									}
									System.out.println();
									System.out.println("페이지>> (0입력:뒤로가기)");
									page = scn.nextInt();
									scn.nextLine();
									if (page == 0) {
										break;
									}
								}
							} else if (menu == 2) {
								System.out.println("구매할 품목>> ");
								String name = scn.nextLine();
								System.out.println("구매할 수량>> ");
								String stock = scn.nextLine();

								Product pro = new Product();
								pro.setPName(name);
								pro.setStock(Integer.parseInt(stock));

								if (pdao.buyPro(pro)) {
									System.out.println("=====구매 완료=====");
								} else {
									System.out.println("구매 실패. 품목이름 및 수량을 다시 확인해주세요.");
								}
							} else if (menu == 9) {
								System.out.println("로그아웃 완료");
								break;
							}
						} // end of while (물품정보)
					}
				} else {
					System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
					continue;
				}
			} else if (smenu == 3) {
				System.out.println("프로그램을 종료합니다");
				break;
			}
		} // end of while (회원가입&로그인)
	} // end of main
}
