import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminMenu
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
	PreparedStatement pstmt2 = null;
	PreparedStatement pstmt2_2 = null;
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	PreparedStatement pstmt6 = null;
	PreparedStatement pstmt7 = null;
	
	Scanner sc = new Scanner(System.in);
	
	
	
//	public static void main(String[] args)
//	{
//		ShowBookDB bookDB = new ShowBookDB();
//		bookDB.doRun();
//	}
	


	
	public void doRun() {
		
		connectDatabase();
		int choice;
		
		while(true) {
			showMenu();
			choice = sc.nextInt();
			sc.nextLine();   
			
			switch(choice) {
			case 1:
				addBook();
				break;
			case 2:
				selBook();
				break;
			case 3:
				listBook();
				break;
			case 4:
				delBook();
				break;
			case 5:
				LibsDB db = new LibsDB();
				db.doLibsDB();
				break;
			case 6:
				showUsers();
				break;
			case 7:
				pwReset();
				break;
			case 8:
				System.out.println("메인 화면으로 돌아갑니다.");
				return;
			default:                  
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
		
		
		
		
	}
	
	
	public void showMenu() {
		System.out.println("            ┏━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("                 ▶관리자 메뉴 선택◀");
		System.out.println("             1. 도서 등록");
		System.out.println("             2. 도서 검색");
		System.out.println("             3. 전체 도서 리스트");
		System.out.println("             4. 도서 삭제");
		System.out.println("             5. 도서 대여/반납");
		System.out.println("                --------------------");
		System.out.println("             6. 회원정보 조회");
		System.out.println("             7. 회원 비밀번호 리셋");
		System.out.println("             8. 돌아가기");
		System.out.println("            ┗━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("            ▶선택 : ");
	}
	
	// 도서 등록
	public void addBook() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 등록]");
		System.out.print("▶ 도서 이름 : ");
		String bName = sc.nextLine();
		
		System.out.print("▶ 도서 번호 : ");
		String bCode = sc.nextLine();
		
		System.out.print("▶ 보유 도서 갯수 : ");
		String bNumber = sc.nextLine();
		
		System.out.print("▶ 지은이 | 출판사 : ");
		String bData = sc.nextLine();
		
		try {
			pstmt1.setString(1, bName);
			pstmt1.setString(2, bCode);
			pstmt1.setString(3, bNumber);
			pstmt1.setString(4, bData);
			int updateCount = pstmt1.executeUpdate();
			System.out.println("─────────────────────────────────────────────────────");
			System.out.println("▶ 데이터베이스에 추가되었습니다.");
			System.out.println("─────────────────────────────────────────────────────");
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("▶ 데이터베이스 입력 오류입니다.");
		}
		
	}
	
	// 도서 검색
	public void selBook() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 검색]");
		System.out.print("▶ 찾을 도서 이름 : ");
		String name = sc.nextLine();
		
		try {
			pstmt2.setString(1, "%" + name + "%");
			ResultSet sel_rs = pstmt2.executeQuery();
			int n = 1;
			if(sel_rs.next()) {
				pstmt2.setString(1, "%" + name + "%");
				ResultSet sel_rs1 = pstmt2.executeQuery();
				while(sel_rs1.next()) {
					System.out.println("┌──────────────────────────────────────────────┐");
					System.out.println(" [도서 검색 결과 " + n + "]");
					System.out.println(" ▶도서 이름: " + sel_rs1.getString(1));
					System.out.println(" ▶도서 번호: " + sel_rs1.getString(2));
					System.out.println(" ▶보유 도서 수: " + sel_rs1.getString(3));
					System.out.println(" ▶지은이, 출판사: " + sel_rs1.getString(4));
					System.out.println("└──────────────────────────────────────────────┘");
					n = n+1;
				}
				sel_rs1.close();
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 미등록 도서입니다.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			sel_rs.close();
			
		} catch (Exception e) {
			System.out.println("에러가 발생했습니다.");
		}
		
	} 
	
	// 도서 삭제
	public void delBook() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 삭제]");
		System.out.print("▶ 삭제할 도서 코드: ");
		String code = sc.nextLine();
		
		try {
			pstmt2_2.setString(1, code);
			ResultSet book = pstmt2_2.executeQuery();
			if(book.next()) {
				pstmt3.setString(1, code);
				int updateCount = pstmt3.executeUpdate();
				System.out.println("▶ 데이터베이스에서 삭제되었습니다.");
			} else {
				System.out.println("▶ 도서를 찾을 수 없습니다.");
			}
			System.out.println("─────────────────────────────────────────────────────");
			
		} catch (Exception e) {
			System.out.println("삭제 에러입니다.");
		}
		
	}
	
	// 도서 리스트
	public void listBook(){
		System.out.println("────────────────────────────────────────────────────────────────────────────────────");
		System.out.println("[전체 도서 리스트]");
		System.out.println("▶ 전체 도서 목록을 출력합니다.");
		System.out.println("────────────────────────────────────────────────────────────────────────────────────");
		System.out.println(" 도서 이름  |  도서 번호  |  잔여 권수  |  도서 정보           ");
		System.out.println("────────────────────────────────────────────────────────────────────────────────────");
		
		try {
			ResultSet list_rs = pstmt4.executeQuery();
			while(list_rs.next()) {
				System.out.print(list_rs.getString(1) + " | ");
				System.out.print(list_rs.getString(2)+ " | ");
				System.out.print(list_rs.getString(3)+ " | ");
				System.out.println(list_rs.getString(4));
				System.out.println("────────────────────────────────────────────────────────────────────────────────────");
			}
			
			list_rs.close();
		} catch (Exception e) {
			System.out.println("에러가 발생했습니다.");
		}

	}
	
	
	// 회원정보 조회
	public void showUsers() {
		System.out.println("─────────────────────────────────────────────────────────────────");
		System.out.println(" 유저 ID | 유저 PW | 유저 이름 | 대여금지기한 | 대출가능횟수  ");
		System.out.println("─────────────────────────────────────────────────────────────────");
		System.out.println("▶ 총 회원 정보를 조회합니다.");
		System.out.println("비밀번호와 이름은 앞에서부터 한자리까지만 표시됩니다.");
		System.out.println("─────────────────────────────────────────────────────────────────");		
		try {
			ResultSet info = pstmt5.executeQuery();
				while(info.next()) {
					System.out.print(info.getString(1) + " | ");
					System.out.print(info.getString(2).substring(0, 1) + "**" + " | ");
					System.out.print(info.getString(3).substring(0, 1) + "**" + " | ");
					if(info.getString(4) == null) {
						System.out.print("대여 가능 | ");
					} else {
						System.out.print(info.getString(4).substring(0, 10) + " | ");
					}
					System.out.println(info.getString(5) + "회");
					System.out.println("─────────────────────────────────────────────────────────────────");		
				}
			
			info.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("▶ 오류로 인하여 유저 정보를 찾을 수 없습니다.");
		}
		
	}
	
	
	// 비밀번호 리셋
	public void pwReset() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[회원 비밀번호 변경]");
		System.out.print("▶ 유저 ID: ");
		String id = sc.nextLine();
		
		try {
			pstmt6.setString(1, id);
			ResultSet ppw = pstmt6.executeQuery();
			if(ppw.next()) {
				System.out.println(id + " 님의 현재 비밀번호는");
				System.out.println("[" +ppw.getString(1) + "]입니다.");
				System.out.println("─────────────────────────────────────────────────────");
				pstmt7.setString(1, id);
				int pwupdate = pstmt7.executeUpdate();
				
				System.out.println("비밀번호를 [ 0000 ]으로 초기화 합니다.");
				System.out.println("회원 로그인 ▶ 회원정보 수정 ▶ 비밀번호 수정");
				System.out.println("위 경로에서 비밀번호를 직접 수정해주세요.");
				System.out.println("─────────────────────────────────────────────────────");
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ 등록된 아이디가 아닙니다.");
				System.out.println("─────────────────────────────────────────────────────");
			}
			ppw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("▶ 오류가 발생했습니다.");
		}
		
	}
	
	
	public void connectDatabase() {
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			String sql1 = "insert into BookDB values(?, ?, ?, ?)";
			pstmt1 = con.prepareStatement(sql1);
			
			String sql2 = "select * from BookDB where bName like ?";
			pstmt2 = con.prepareStatement(sql2);
			
			String sql2_2 = "select * from BookDB where bCode = ?";
			pstmt2_2 = con.prepareStatement(sql2_2);
			
			String sql3 = "delete from BookDB where bCode = ?";
			pstmt3 = con.prepareStatement(sql3);
			
			String sql4 = "select * from BookDB";
			pstmt4 = con.prepareStatement(sql4);
			
			String sql5 = "select * from UserDB";
			pstmt5 = con.prepareStatement(sql5);
			
			String sql6 = "select userpw from UserDB where userid = ?";
			pstmt6 = con.prepareStatement(sql6);
			
			String sql7 = "update userDB set userpw = '0000' where userid = ?";
			pstmt7 = con.prepareStatement(sql7);
			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
