import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserMenu
{
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
	
	Connection con = null;
	PreparedStatement pstmt1 = null;
	PreparedStatement pstmt1_1 = null;
	PreparedStatement pstmt1_2 = null;
	PreparedStatement pstmt1_3 = null;
	
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt4_2 = null;
	PreparedStatement pstmt5 = null;
	PreparedStatement pstmt6_1 = null;
	PreparedStatement pstmt6_2 = null;

	PreparedStatement pstmt7 = null;
	PreparedStatement pstmt7_2 = null;
	PreparedStatement pstmt7_3 = null;
	PreparedStatement pstmt7_4 = null;
	PreparedStatement pstmt7_5 = null;
	PreparedStatement pstmt7_6 = null;
	PreparedStatement pstmt8 = null;
	PreparedStatement pstmt8_2 = null;
	PreparedStatement pstmt8_3 = null;
	PreparedStatement pstmt9 = null;

	Scanner sc = new Scanner(System.in);
	
//	public static void main(String[] args)
//	{
//		ShowBookDB bookDB = new ShowBookDB();
//		bookDB.doRun();
//	}
	
	
	
	public void selUser() {
		connectUserDB();  // 이게 있어야 다른 클래스에서 이 메서드 호출했을때 로그인가능
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[회원 로그인]");
		
		System.out.print("▶ 회원 ID : ");
		String userID = sc.nextLine();
		
		System.out.print("▶ 비밀번호 : ");
		String userPW = sc.nextLine();
		
		try {
			pstmt1.setString(1, userID);
			pstmt1.setString(2, userPW);
			ResultSet sel_rs = pstmt1.executeQuery();
			if(sel_rs.next()) {
				pstmt1_1.setString(1, userID);
				int updateCount = pstmt1_1.executeUpdate();
				System.out.println("** " +sel_rs.getString(3) + " 회원님 환영합니다! **");
				System.out.println("─────────────────────────────────────────────────────");
				doRun();
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 잘못된 정보입니다.");
				System.out.println("▶ 계정정보를 잊어버리셨다면, 사서에게 문의해주세요.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			sel_rs.close();
			
			
			
		} catch (Exception e) {
			System.out.println("오류가 발생했습니다.");
		}
		
	}
	
	
	public void addUser() {
		connectUserDB();
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[회원 가입]");
		
		System.out.println("[주의] ID는 변경할 수 없습니다.");
		System.out.print("▶ 사용할 ID를 입력하세요 : ");
		String userID = sc.nextLine();
		
		System.out.print("▶ 비밀번호를 입력하세요 : ");
		String userPW = sc.nextLine();
		
		System.out.print("▶ 회원님의 이름을 입력하세요 : ");
		String uName = sc.nextLine();
		
		try {
			pstmt2.setString(1, userID);
			pstmt2.setString(2, userPW);
			pstmt2.setString(3, uName);
			pstmt2.setString(4, "");
			pstmt2.setString(5, "2");
			int updateCount = pstmt2.executeUpdate();
			System.out.println("▶ 회원 가입이 완료되었습니다.");
			System.out.println("─────────────────────────────────────────────────────");
			
		} catch(SQLException sqle) {
			System.out.println("─────────────────────────────────────────────────────");
			System.out.println("▶ 중복된 아이디입니다.");
		}
		
	} 
	
	
	public void doRun() {
		connectUserDB();
		int choice;
		
		while(true) {
			userMenu();
			choice = sc.nextInt();
			sc.nextLine();   
			
			switch(choice) {
			case 1:
				UserLibs self = new UserLibs();
				self.doLibsDB();
				break;
			case 2:
				userRent(); 
				break;
			case 3:
				do_modi();
				break;
			case 4:
				logout();
				System.out.println("메인 화면으로 돌아갑니다.");
				return;  
			default:                  
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	
	public void userMenu() {
		System.out.println("            ┏━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("                    ▶회원 메뉴◀");
		System.out.println("             1. 도서 대여/반납/연장");
		System.out.println("             2. 도서 대여 현황");
		System.out.println("             3. 회원정보 수정");
		System.out.println("             4. 뒤로가기(로그아웃)");
		System.out.println("            ┗━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("            ▶선택 : ");
	}
	
	// 대여 현황
	public void userRent() {
		try {
			ResultSet id = pstmt1_2.executeQuery();
			if(id.next()) {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶" + id.getString(1) + " 님의 대여 현황");
				
				pstmt4.setString(1, id.getString(1));
				ResultSet userinfo = pstmt4.executeQuery();
				if(userinfo.next()) {
					pstmt4.setString(1, id.getString(1));
					ResultSet userinfo2 = pstmt4.executeQuery();
					
					while(userinfo2.next()) {
						System.out.println("─────────────────────────────────────────────────────");
						System.out.println("▶도서 이름: " + userinfo2.getString(2));
						System.out.println("▶도서 코드: " + userinfo2.getString(1));
						System.out.println("▶대여한 날짜: " + userinfo2.getString(4).substring(0, 10));
						System.out.println("▶반납 예정일: " + userinfo2.getString(5).substring(0, 10));
						
					} 
					pstmt4_2.setString(1, id.getString(1));
					ResultSet userCount = pstmt4_2.executeQuery();
					if(userCount.next()) {
						System.out.println("─────────────────────────────────────────────────────");
						System.out.println("▶잔여 대여 횟수: " + userCount.getInt(1) + "회");
						System.out.println("─────────────────────────────────────────────────────");
					}
					System.out.println();
					userCount.close();
					
					userinfo.close();
					userinfo2.close();
				} else {
					System.out.println("─────────────────────────────────────────────────────");
					System.out.println("▶대여 현황이 없습니다.");
					System.out.println("─────────────────────────────────────────────────────");
				}
			}
			id.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("오류가 발생했습니다.");
		}
		
	}
	
	
	// idkey에 있는 유저아이디 초기화
	public void logout() {
		try {
			ResultSet out = pstmt1_2.executeQuery();
			if(out.next()) {
				int userLogout = pstmt1_3.executeUpdate();
				System.out.println("정상적으로 로그아웃 되었습니다.");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// 회원정보 수정
	public void do_modi() {
		int choice;
		
		while(true) {
			modi_Menu();
			choice = sc.nextInt();
			sc.nextLine();   
			
			switch(choice) {
			case 1:
				modi_Name();
				break;
			case 2:
				modi_Pw();
				break;
			case 3:
				goodbye();
				break;
			case 4:
				System.out.println();
				System.out.println("회원 메뉴로 돌아갑니다.");
				return;  
			default:                  
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	
	
	public void modi_Menu() {
		System.out.println("            ┏━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("                  ▶회원정보 수정◀");
		System.out.println("             1. 회원 이름 수정");
		System.out.println("             2. 비밀번호 수정");
		System.out.println("             3. 회원 탈퇴");
		System.out.println("             4. 뒤로가기");
		System.out.println("            ┗━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("            ▶선택 : ");
	}
	

	// 비밀번호 수정
	public void modi_Pw() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[비밀번호 수정]");
		System.out.print("▶ 사용중인 아이디 : ");
		String id = sc.nextLine();
		
		try {
			pstmt7.setString(1, id);
			ResultSet modi2 = pstmt7.executeQuery();
			if(modi2.next()) {
				System.out.print("▶ 현재 비밀번호 : ");
				String pw = sc.nextLine();
				
				pstmt7_4.setString(1, pw);
				ResultSet opw = pstmt7_4.executeQuery();
				if(opw.next()) {
					
					System.out.print("▶ 새 비밀번호 : ");
					String modipw = sc.nextLine();
					
					pstmt7_3.setString(1, modipw);
					pstmt7_3.setString(2, id);
					int updateCount = pstmt7_3.executeUpdate();
					
					System.out.println("▶ 성공적으로 비밀번호를 수정하였습니다.");
					System.out.println("─────────────────────────────────────────────────────");
				} else {
					System.out.println("─────────────────────────────────────────────────────");
					System.out.println("▶ 비밀번호가 일치하지 않습니다.");
					System.out.println("─────────────────────────────────────────────────────");
				}
				opw.close();
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 등록되지 않은 아이디 입니다.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			modi2.close();
		} catch (Exception e) {
			System.out.println("오류가 발생하였습니다.");
		}
	}
	
	// 이름 수정
	public void modi_Name() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[회원 이름 수정]");
		System.out.print("▶ 사용중인 아이디 : ");
		String id = sc.nextLine();
		
		try {
			pstmt7_5.setString(1, id);
			ResultSet name = pstmt7_5.executeQuery();
			if(name.next()) {
				System.out.print("▶ 수정하실 이름 : ");
				String modiN = sc.nextLine();
				
				pstmt7_6.setString(1, modiN);
				pstmt7_6.setString(2, id);
				int updateCount = pstmt7_6.executeUpdate();
				
				System.out.println("▶ 이름이 성공적으로 수정되었습니다.");
				System.out.println("─────────────────────────────────────────────────────");
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 정보를 찾을 수 없습니다.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			name.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("오류가 발생하였습니다.");
		}

	}
	
	
	// 회원 탈퇴
	public void goodbye() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[회원 탈퇴]");
		System.out.println("[주의] 도서 대여 내역이 있으면 탈퇴할 수 없습니다.");
		System.out.print("▶ 사용중인 아이디 : ");
		String id = sc.nextLine();
		
		try {
			pstmt4.setString(1, id);
			ResultSet rentinfo = pstmt4.executeQuery();
			if(rentinfo.next()) {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 대여중인 도서가 있어 회원 탈퇴가 불가능합니다.");
				System.out.println("─────────────────────────────────────────────────────");
			} else {
				System.out.println("▶ 탈퇴할 회원 계정 정보");
				pstmt7.setString(1, id);
				ResultSet uDB = pstmt7.executeQuery();
				if(uDB.next()) {
					System.out.println("유저 ID : " + uDB.getString(1));
					System.out.println("유저 이름 : " + uDB.getString(3));
					
					System.out.println("─────────────────────────────────────────────────────");
					System.out.println("비밀번호를 입력하면 탈퇴가 완료됩니다.");
					System.out.print("▶ 비밀번호 : ");
					String pw = sc.nextLine();
					
					pstmt1.setString(1, id);
					pstmt1.setString(2, pw);
					ResultSet bye = pstmt1.executeQuery();
					if(bye.next()) {
						pstmt9.setString(1, id);
						int byebye = pstmt9.executeUpdate();
						System.out.println("▶ 회원 탈퇴가 완료되었습니다.");
						System.out.println("─────────────────────────────────────────────────────");
					} else {
						System.out.println("▶ 비밀번호가 맞지 않습니다.");
						System.out.println("─────────────────────────────────────────────────────");
					}
					bye.close();
				}
				uDB.close();
			}
			rentinfo.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("오류가 발생했습니다.");
		}
		
	}
	
	
	public void connectUserDB() {
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			
			//
			String sql1 = "select * from UserDB where userID = ? and userPW = ?";
			pstmt1 = con.prepareStatement(sql1);
			
			String sql1_1 = "update idkey set ikey = ?";
			pstmt1_1 = con.prepareStatement(sql1_1);
			
			String sql1_2 = "select ikey from idkey";
			pstmt1_2 = con.prepareStatement(sql1_2);
			
			String sql1_3 = "update idkey set ikey = 'dummy'";
			pstmt1_3 = con.prepareStatement(sql1_3);
			
			String sql2 = "insert into UserDB values(?, ?, ?, ?, ?)";
			pstmt2 = con.prepareStatement(sql2);
			
			String sql3 = "select * from BookDB where bCode = ?";
			pstmt3 = con.prepareStatement(sql3);
			
			String sql4 = "select * from libsDB where l_userID = ?";
			pstmt4 = con.prepareStatement(sql4);
			
			String sql4_2 = "select bCount from userDB where userID = ?";
			pstmt4_2 = con.prepareStatement(sql4_2);
			
			String sql5 = "insert into libsDB (l_bcode, l_bname, l_uerID) values (?, ?, ?)";
			pstmt5 = con.prepareStatement(sql5);
			
			String sql6_1 = "select to_char(to_date(sysdate), 'yy/mm/dd') from dual";
			pstmt6_1 = con.prepareStatement(sql6_1);
			String sql6_2 = "select to_char(to_date(sysdate+7), 'yy/mm/dd') from dual";
			pstmt6_2 = con.prepareStatement(sql6_2);
			//
			
			
			// 정보수정용
			String sql7 = "select * from userDB where userID = ?";
			pstmt7 = con.prepareStatement(sql7);
			
			String sql7_2 = "update userDB set userID = ? where userID = ?";
			pstmt7_2 = con.prepareStatement(sql7_2);
			
			String sql7_3 = "update userDB set userPW = ? where userID = ?";
			pstmt7_3 = con.prepareStatement(sql7_3);
			
			String sql7_4 = "select userID from userDB where userPW = ?";
			pstmt7_4 = con.prepareStatement(sql7_4);
			
			String sql7_5 = "select uName from userDB where userID = ?";
			pstmt7_5 = con.prepareStatement(sql7_5);
			
			String sql7_6 = "update userDB set uName = ? where userID = ?";
			pstmt7_6 = con.prepareStatement(sql7_6);
			
			String sql8 = "select exCount from libsDB where l_bcode = ? and l_userID = ?";
			pstmt8 = con.prepareStatement(sql8);
			
			String sql8_2 = "update libsDB set returnday = returnday+7 where l_bcode = ? and l_userID = ?";
			pstmt8_2 = con.prepareStatement(sql8_2);
			
			String sql8_3 = "update libsDB set exCount = 0 where l_bcode = ? and l_userID = ?";
			pstmt8_3 = con.prepareStatement(sql8_3);
			
			String sql9 = "delete from userDB where userid = ?";
			pstmt9 = con.prepareStatement(sql9);
			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	

}


// 아이디 수정 (ui수정으로 제외)
//public void modi_Id() {
//	System.out.println("─────────────────────────────────────────────");
//	System.out.println("[아이디 수정]");
//	System.out.println("[경고] 아이디를 수정하신 뒤에 회원 메뉴를 이용하시려면,");
//	System.out.println("      반드시 메인으로 돌아가 다시 로그인해주세요.");
//	System.out.println("─────────────────────────────────────────────");
//
//	boolean modify = true;
//	
//	while(modify) {
//		System.out.print("현재 아이디 : ");
//		String id = sc.nextLine();
//		try {
//			pstmt7.setString(1, id);
//			ResultSet modi1 = pstmt7.executeQuery();
//			if(modi1.next()) {
//				System.out.print("새로운 아이디 : ");
//				String modi_id = sc.nextLine();
//				try {
//					pstmt7_2.setString(1, modi_id);
//					pstmt7_2.setString(2, id);
//					int updateCount = pstmt7_2.executeUpdate();
//					
//					System.out.println("성공적으로 아이디를 수정하였습니다.");
//					System.out.println("메인 화면으로 돌아가 다시 로그인해주세요.");
//					System.out.println("─────────────────────────────────────────────");
//					
//				} catch (Exception ee) {
//					modi1.close();
//					System.out.println("─────────────────────────────────────────────");
//					System.out.println("이미 존재하는 아이디로는 수정할 수 없습니다.");
//					System.out.println("─────────────────────────────────────────────");
//
//					return;
//				}	
//				modi1.close();
//			} else {
//				System.out.println("─────────────────────────────────────────────");
//				System.out.println("해당 아이디를 찾을 수 없습니다.");
//				System.out.println("─────────────────────────────────────────────");
//				modi1.close();
//			}
//
//		} catch (Exception e) {
//			System.out.println("오류가 발생했습니다.");
//		}
//		modify = false;
//	}
//
//}