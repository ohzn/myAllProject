import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserLibs
{
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	Connection con = null;
	
	PreparedStatement pstmt12 = null;
	
	//대출용
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt3_1 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	PreparedStatement pstmt6_1 = null;
	PreparedStatement pstmt6_2 = null;
	PreparedStatement pstmt4_3 = null;
	PreparedStatement pstmt4_3_2 = null;
	PreparedStatement pstmt4_4 = null;
	PreparedStatement pstmt11 = null;
	PreparedStatement pstmt11_2 = null;


	
	//반납용
	PreparedStatement pstmt7 = null;
	PreparedStatement pstmt8 = null;
	PreparedStatement pstmt8_2 = null;
	PreparedStatement pstmt8_3 = null;
	PreparedStatement pstmt9 = null;
	PreparedStatement pstmt3_2 = null;
	PreparedStatement pstmt4_2 = null;
	
	
	//연장용
	PreparedStatement pstmt13 = null;
	PreparedStatement pstmt14 = null;
	PreparedStatement pstmt15 = null;
	PreparedStatement pstmt16 = null;




	Scanner sc = new Scanner(System.in);
	
	
//	public static void main(String[] args)
//	{
//		LibsDB db = new LibsDB();
//		db.doLibsDB();
//	}
	

	public void doLibsDB() {
		connectlibsDB();
		int choice;
		
		while(true) {
			
			RentMenu();
			choice = sc.nextInt();
			sc.nextLine();   
			
			switch(choice) {
			case 1:
				selfRent();
				break;
			case 2:
				selfReturn();
				break;
			case 3:
				rent_EX();
				break;
			case 4:
				System.out.println("회원 메뉴로 돌아갑니다.");
				return;	
			default:                  
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	
	public void RentMenu() {
		System.out.println("            ┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("                     ▶메뉴 선택◀");
		System.out.println("             1. 도서 대여");
		System.out.println("             2. 도서 반납");
		System.out.println("             3. 대여기한 연장");
		System.out.println("             4. 돌아가기");
		System.out.println("            ┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("            ▶선택 : ");
	}
	
	//대여2
	public void selfRent() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 대여]");
		System.out.println("도서 코드를 입력하세요.");
		System.out.print("▶ 도서 코드 : ");
		String code = sc.nextLine();
		
// 		String sql12 = "select ikey from idkey";
		
		try {
			ResultSet userid = pstmt12.executeQuery(); // 키에서 아이디 빼기
			if(userid.next()) {
				String id = userid.getString(1); // id = 유저 아이디
				
				pstmt4.setString(1, id);
				ResultSet uDB = pstmt4.executeQuery(); // 유저db에서 id가 있는지 비교
				if(uDB.next()) {
					// 연체된 책이 있는지 확인(있으면 반납먼저)
					pstmt8_3.setString(1, id);
					ResultSet cut = pstmt8_3.executeQuery();
					if(cut.next()) {
						System.out.println("─────────────────────────────────────────────────────");
						System.out.println("▶ 현재 대여목록에 연체된 도서가 있습니다.");
						System.out.println("▶ 먼저 연체도서를 반납해주세요.");
						System.out.println("─────────────────────────────────────────────────────");
					} else {
						// 대출 불가능 기간 체크
						pstmt4_3.setString(1, id);
						ResultSet bDB = pstmt4_3.executeQuery();
						
						if(bDB.next()) {
							System.out.println("─────────────────────────────────────────────────────");
							System.out.println(uDB.getString(1)+" 님은 " + bDB.getString(1).substring(0, 10) + " 까지");
							System.out.println("대출 불가능 기간입니다.");
							System.out.println("─────────────────────────────────────────────────────");
						} 
						else  //black 기간이 지났을경우
						{
							pstmt4_3_2.setString(1, id);
							ResultSet notB = pstmt4_3_2.executeQuery();
							// 기한 지난 블랙은 널으로 초기화
							if(notB.next()) {
								pstmt4_4.setString(1, id);
								int resetBlack = pstmt4_4.executeUpdate();
							}
							//대출
							rentLimit(id, code);     // 대여 수 카운트 메소드
			
							notB.close();
						}
						bDB.close();
					}
					cut.close();
				} else {
					System.out.println("▶ 미등록 사용자입니다. 회원가입을 진행해주세요.");
					System.out.println("─────────────────────────────────────────────────────");
				}
				uDB.close();
			}
			userid.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 반납2
	public void selfReturn() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 반납]");
		System.out.println("도서 코드를 입력하세요.");
		System.out.print("▶ 도서 코드 : ");
		String code = sc.nextLine();
		
		try {
			ResultSet userid = pstmt12.executeQuery(); // 키에서 아이디 빼기
			if(userid.next()) {
				String id = userid.getString(1); // id = 유저 아이디
				
				pstmt4.setString(1, id);
				ResultSet cUser = pstmt4.executeQuery();
				if(cUser.next()) {
					try {
						pstmt7.setString(1, code);
						pstmt7.setString(2, id);
						ResultSet bDB = pstmt7.executeQuery();
						
						if(bDB.next()) {
							System.out.println("─────────────────────────────────────────────────────");
							System.out.println("[대출 내역]");
							System.out.println("▶도서 이름: " + bDB.getString(2));
							System.out.println("▶도서 번호: " + bDB.getString(1));
							System.out.println("▶반납 기한: " + bDB.getString(5).substring(0, 10));
							System.out.println("─────────────────────────────────────────────────────");
							
							pstmt8.setString(1, code);
							pstmt8.setString(2, id);
							ResultSet over = pstmt8.executeQuery(); 
							
							if(over.next()) {
								//연체된 경우 (=반납기한이 sysdate보다 작음)
								ResultSet black = pstmt6_2.executeQuery();
								System.out.println("▶ 반납 기한이 지난 책입니다.");
								if(black.next()) {
									System.out.println(over.getString(1) + " 님은 연체로 인하여");
									System.out.println(black.getString(1) + " 까지 대출이 불가능합니다.");

									//userDB의 black에 대출불가 기한 추가
									pstmt4.setString(1, id);
									ResultSet blackD = pstmt4.executeQuery(); 
									if(blackD.next()) {
										pstmt4_2.setString(1, id);
										int updateCount = pstmt4_2.executeUpdate();	
									}
									blackD.close();
								}
								black.close();
								
								returnLimit(id, code);
								// 반납(반납처리용 메소드)

							} else {
								returnLimit(id, code);
								// 정상 반납(반납처리용 메소드)
							}
							over.close();	
						} else {
							System.out.println("─────────────────────────────────────────────────────");
							System.out.println("▶ 대출 내역을 찾을 수 없습니다.");
							System.out.println("─────────────────────────────────────────────────────");
						}
					} catch (Exception e) {
						System.out.println("에러가 발생했습니다.");
					}
					
				} else {
					System.out.println("─────────────────────────────────────────────────────");
					System.out.println("▶ 미등록 사용자입니다. 회원가입을 먼저 진행해주세요.");
					System.out.println("─────────────────────────────────────────────────────");
				}
				cUser.close();
			}
			userid.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	// 대여 횟수 체크 후 rentAndcount 실행
	public void rentLimit(String a, String b) {
		String userid = a;
		String bookCode = b;

		
		

		// 개인 대출횟수 제한
		try {
			pstmt11.setString(1, userid);
			ResultSet userCount = pstmt11.executeQuery();
			if(userCount.next()) {
				int uCount = userCount.getInt(1);
				if(uCount == 0) {
					System.out.println("▶ 대여 가능한 횟수: 0회");
					System.out.println("▶ 횟수를 전부 소진하여 대여가 불가능합니다.");
				} else if (uCount > 0) {
					rentAndcount(userid, bookCode); // 대여처리용 메소드 <이거에서 libsdb에 기록안됐으면 밑에 진행x
					try {
						pstmt7.setString(1, bookCode);
						pstmt7.setString(2, userid);
						ResultSet rentss = pstmt7.executeQuery();
						if(rentss.next()) {
							uCount = uCount-1;
							pstmt11_2.setInt(1, uCount);
							pstmt11_2.setString(2, userid);
							int updateUserCount = pstmt11_2.executeUpdate();
							
							System.out.println("▶ 대여에 성공했습니다.");
							System.out.println("▶ 남은 대여 횟수: " + uCount + "회");
							System.out.println("─────────────────────────────────────────────────────");
						} 
						rentss.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				userCount.close();
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("등록된 횟수가 없습니다. 아이디를 확인해주세요.");
		}
	}

	

	// 대여처리용 메소드
	public void rentAndcount (String uc, String cc) {
		String userid = uc;
		String bookCode = cc;
		
		try {
			pstmt3.setString(1, bookCode);
			ResultSet bookDB = pstmt3.executeQuery();
			
			if(bookDB.next()) {
				System.out.println("┌──────────────────────────────────────────────┐");
				System.out.println(" [도서 검색 결과]");
				System.out.println(" ▶도서 이름: " + bookDB.getString(1));
				System.out.println(" ▶도서 번호: " + bookDB.getString(2));
				System.out.println(" ▶도서 잔여 권수: " + bookDB.getInt(3));
				System.out.println(" ▶지은이, 출판사: " + bookDB.getString(4));
				System.out.println("└──────────────────────────────────────────────┘");
				if(bookDB.getInt(3)==0)
				{
					System.out.println("─────────────────────────────────────────────────────");
					System.out.println("▶ 잔여 권수가 없어 대출이 불가능합니다.");
					System.out.println("─────────────────────────────────────────────────────");
				} else {
					System.out.println("─────────────────────────────────────────────────────");
					pstmt5.setString(1, bookDB.getString(2));
					pstmt5.setString(2, bookDB.getString(1));
					pstmt5.setString(3, userid);
					int updateCount = pstmt5.executeUpdate();
					
					System.out.println("▶ 대여 도서 이름: " + bookDB.getString(1));
					
					ResultSet rentDate = pstmt6_1.executeQuery();
					if(rentDate.next()) {
						System.out.println("▶ 대여 날짜 : " + rentDate.getString(1));
					}
					
					ResultSet returnDate = pstmt6_2.executeQuery();
					if(returnDate.next()) {
						System.out.println("▶ 반납 날짜 : " + returnDate.getString(1));
					}
					System.out.println("─────────────────────────────────────────────────────");
					// 책수 빼기(bookDB)
					try {
						int bookCount = bookDB.getInt(3)-1;
						pstmt3_1.setInt(1, bookCount); 
						pstmt3_1.setString(2, bookCode);
						int updatebookCount = pstmt3_1.executeUpdate();
						
					} catch(Exception e) {
						e.printStackTrace();
					}
					rentDate.close();
					returnDate.close();
				}
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("미등록 도서입니다. 도서번호를 확인해주세요.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			
			bookDB.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("처리중 오류가 발생했습니다.");
		}
	}
	
	

	// 대여횟수 복구 후 returnAndcount 실행
 	public void returnLimit(String a, String b) {
 		String userid = a;
		String bookCode = b;
		try {
			pstmt11.setString(1, userid);
			ResultSet userCount = pstmt11.executeQuery();
			if(userCount.next()) {
				int uCount = userCount.getInt(1);
				if(uCount > 2 || uCount < -1) {
					System.out.println("횟수 오류입니다. 아이디를 확인해주세요.");
				} else {
					uCount = uCount+1;
					pstmt11_2.setInt(1, uCount);
					pstmt11_2.setString(2, userid);
					int updateUserCount = pstmt11_2.executeUpdate();
					
					returnAndcount(userid, bookCode);
					
					System.out.println("▶ 대여 가능한 횟수: " + uCount + "회");
					System.out.println("─────────────────────────────────────────────────────");
				}
				userCount.close();
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 등록된 내용이 없습니다. 아이디를 확인해주세요.");
				System.out.println("─────────────────────────────────────────────────────");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("처리중 오류가 발생했습니다.");
		}
 	}

	
	
	// 반납처리용 메소드
 	public void returnAndcount (String ac, String bc) {
		String userId = ac;
 		String bookCode = bc;
		try {
			pstmt9.setString(1, bookCode);
			pstmt9.setString(2, userId);
			int updateCount = pstmt9.executeUpdate();
			System.out.println("▶ 반납 처리가 완료되었습니다.");
			
			// 책수 더하기(bookDB)
			pstmt3.setString(1, bookCode);
			ResultSet re = pstmt3.executeQuery();
			
			if(re.next()){
				int count = re.getInt(3)+1;
				pstmt3_2.setInt(1, count);
				pstmt3_2.setString(2, bookCode);
				updateCount = pstmt3_2.executeUpdate();
			}
			re.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// 대여기한 연장
	public void rent_EX() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[대여기한 연장]");
		try {
			ResultSet id = pstmt12.executeQuery();
			if(id.next()) {
				System.out.println(id.getString(1) + " 님의 대여 목록");
				System.out.println("─────────────────────────────────────────────────────");
				pstmt13.setString(1, id.getString(1));
				ResultSet info = pstmt13.executeQuery();
				if(info.next()) {
					pstmt13.setString(1, id.getString(1));
					ResultSet info2 = pstmt13.executeQuery();
					while(info2.next()) {
						System.out.println("▶도서 이름: "+info2.getString(2));
						System.out.println("▶도서 코드: "+info2.getString(1));
						System.out.println("▶반납 기한: "+info2.getString(5).substring(0, 10));
						System.out.println("▶연장 가능 횟수: "+info2.getString(6));
					}
					System.out.println("─────────────────────────────────────────────────────");
					System.out.print("▶ 연장을 원하는 도서의 도서코드 : ");
					String code = sc.nextLine();
					
					pstmt8_2.setString(1, code);
					pstmt8_2.setString(2, id.getString(1));
					ResultSet chance = pstmt8_2.executeQuery();
					if(chance.next()) {
						System.out.println("▶ 이미 연체된 도서는 기간을 연장할 수 없습니다.");
						System.out.println("─────────────────────────────────────────────────────");
					} else {
						pstmt14.setString(1, code);
						pstmt14.setString(2, id.getString(1));
						ResultSet count = pstmt14.executeQuery();
						if(count.next()) {
							// 연장카운트 1인지 확인
							int ex = count.getInt(1);
							if(ex > 0 && ex < 2) {
								// 연장카운트 0으로 수정
								pstmt16.setString(1, code);
								pstmt16.setString(2, id.getString(1));
								int exCount = pstmt16.executeUpdate();
								
								// 반납 기한 연장
								pstmt15.setString(1, code);
								pstmt15.setString(2, id.getString(1));
								int rentEx = pstmt15.executeUpdate();
								
								pstmt13.setString(1, id.getString(1));
								ResultSet rs = pstmt13.executeQuery();
								if(rs.next()) {
									System.out.println("▶반납 기한이 "+rs.getString(5).substring(0, 10)+"까지 연장되었습니다.");
								}
								System.out.println("─────────────────────────────────────────────────────");
							} else {
								System.out.println("─────────────────────────────────────────────────────");
								System.out.println("▶ 해당 도서는 남은 연장횟수가 없습니다.");
								System.out.println("─────────────────────────────────────────────────────");
							}
							
						} else {
							System.out.println("해당 도서를 대출 목록에서 찾을 수 없습니다.");
						}
						chance.close();
					}
					
				}
				
			}else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶대여 현황이 없습니다.");
				System.out.println("─────────────────────────────────────────────────────");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void connectlibsDB() {
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			//ikey 이용한 대출
			String sql12 = "select ikey from idkey";
			pstmt12 = con.prepareStatement(sql12);
			
			//대출용
			String sql3 = "select * from BookDB where bCode = ?";
			pstmt3 = con.prepareStatement(sql3);
			
			String sql3_3 = "update BookDB set bNumber = ? where bCode = ?";
			pstmt3_1 = con.prepareStatement(sql3_3);
			
			String sql4 = "select * from UserDB where userID =?";
			pstmt4 = con.prepareStatement(sql4);
			
			
			
			String sql5 = "insert into libsDB (l_bcode, l_bname, l_userID) values (?, ?, ?)";
			pstmt5 = con.prepareStatement(sql5);
			
			String sql6_1 = "select to_char(to_date(sysdate), 'yy/mm/dd') from dual";  // 오늘날짜 구하기
			pstmt6_1 = con.prepareStatement(sql6_1);
			String sql6_2 = "select to_char(to_date(sysdate+7), 'yy/mm/dd') from dual";  // 오늘 날짜 +7
			pstmt6_2 = con.prepareStatement(sql6_2);
			
			String sql4_3 = "select black from userDB where to_char(to_date(sysdate), 'yyyy/mm/dd') < to_char(to_date(black), 'yyyy/mm/dd') and userID = ?";
			pstmt4_3 = con.prepareStatement(sql4_3);
			
			String sql4_3_2 = "select black from userDB where to_char(to_date(sysdate), 'yyyy/mm/dd') > to_char(to_date(black), 'yyyy/mm/dd') and userID = ?";
			pstmt4_3_2 = con.prepareStatement(sql4_3_2);
			
			String sql4_4 = "update userDB set black = '' where userID = ?";
			pstmt4_4 = con.prepareStatement(sql4_4);
			
			String sql11 = "select bCount from userDB where userID = ?";
			pstmt11 = con.prepareStatement(sql11);
			
			String sql11_2 = "update userDB set bCount =? where userID = ?";
			pstmt11_2 = con.prepareStatement(sql11_2);

			//
			
			//반납용
			String sql7 = "select * from libsDB where l_bcode = ? and l_userid = ?";
			pstmt7 = con.prepareStatement(sql7);

			String sql8_3 = "select l_userid, l_bcode from libsDB where to_char(to_date(sysdate), 'yyyy/mm/dd') > to_char(to_date(returnday), 'yyyy/mm/dd') and l_userID =?";
			pstmt8_3 = con.prepareStatement(sql8_3);
			
			String sql8 = "select l_userid, l_bcode from libsDB where to_char(to_date(sysdate), 'yyyy/mm/dd') > to_char(to_date(returnday), 'yyyy/mm/dd') and l_bcode =? and l_userID =?";
			pstmt8 = con.prepareStatement(sql8);
			
			String sql8_2 = "select l_userid, l_bcode from libsDB where to_char(to_date(sysdate), 'yyyy/mm/dd') > to_char(to_date(returnday), 'yyyy/mm/dd') and l_bcode =? and l_userID =?";
			pstmt8_2 = con.prepareStatement(sql8_2);
			
			String sql9 = "delete from libsDB where l_bcode = ? and l_userID = ?";
			pstmt9 = con.prepareStatement(sql9);
			
			String sql3_2 = "update BookDB set bNumber = ? where bCode =?";
			pstmt3_2 = con.prepareStatement(sql3_2);
			
			String sql4_2 = "update userDB set black = sysdate+7 where userID = ?";
			pstmt4_2 = con.prepareStatement(sql4_2);
			//
			
			
			// 연장용
			String sql13 = "select * from libsDB where l_userID = ?";
			pstmt13 = con.prepareStatement(sql13);
			
			String sql14 = "select exCount from libsDB where l_bcode = ? and l_userID = ?";
			pstmt14 = con.prepareStatement(sql14);
			
			String sql15 = "update libsDB set returnday = returnday+7 where l_bcode = ? and l_userID = ?";
			pstmt15 = con.prepareStatement(sql15);
			
			String sql16 = "update libsDB set exCount = 0 where l_bcode = ? and l_userID = ?";
			pstmt16 = con.prepareStatement(sql16);
			
			
			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
}


