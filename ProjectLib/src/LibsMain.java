import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LibsMain
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
	PreparedStatement pstmt3 = null;
	PreparedStatement pstmt4 = null;
	PreparedStatement pstmt5 = null;
	
	Scanner sc = new Scanner(System.in);
	
	
	
	
	
	public static void main(String[] args)
	{
		LibsMain lb = new LibsMain();
		lb.doRun();
	}
	
	public void doRun() {
		
		connectDatabase();
		int choice;
		
		while(true) {
			showMenu();
			choice = sc.nextInt();
			sc.nextLine();   
			
			switch(choice) {
			case 0:
				libsAdmin();
				break;
			case 1:
				userLogin();
				break;
			case 2:
				userJoin();
				break;
			case 3:
				selBook();
				break;
			case 4:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:                  
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	
	public void showMenu() {
		System.out.println("        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("        ┃  *도서관에 오신것을 환영합니다*  ┃");
		System.out.println("        ┃*보리 도서관의 메인 페이지 입니다*┃");
		System.out.println("        ┗━━━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━━┛");
		System.out.println("                          ┃ ");
		System.out.println("            ┏━━━━━━━━━━━━━┻━━━━━━━━━━━┓");
		System.out.println("                     ▶메뉴 선택◀");
		System.out.println("             1. 회원 로그인");
		System.out.println("             2. 회원 가입");
		System.out.println("             3. 도서 검색(비회원 가능)");
		System.out.println("             4. 프로그램 종료");
		System.out.println();
		System.out.println("             0. 관리자 메뉴");
		System.out.println("            ┗━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("            ▶ 선택 : ");
	}
	
	public void userLogin() {
		UserMenu userDB = new UserMenu();
		userDB.selUser();
	}
	
	public void userJoin() {
		UserMenu usrJoin = new UserMenu();
		usrJoin.addUser();
	}
	
	

	public void libsAdmin() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[관리자 로그인]");
		
		System.out.print("▶ ID : ");
		String id = sc.nextLine();
		
		System.out.print("▶ PW : ");
		String pw = sc.nextLine();
		
		try {
			pstmt5.setString(1, id);
			pstmt5.setString(2, pw);
			ResultSet ad_login = pstmt5.executeQuery();
			if(ad_login.next()) {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println(ad_login.getString(1) + " 계정으로 접속합니다.");
				ad_login.close();
				AdminMenu ad = new AdminMenu();
				ad.doRun();
			} else {
				System.out.println("─────────────────────────────────────────────────────");
				System.out.println("▶ ID와 PW가 맞지 않습니다.");
			}
			ad_login.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void selBook() {
		System.out.println("─────────────────────────────────────────────────────");
		System.out.println("[도서 검색]");
		System.out.print("▶ 도서 이름 : ");
		String name = sc.nextLine();
		
		try {
			pstmt2.setString(1, "%" + name + "%");
			ResultSet sel_rs = pstmt2.executeQuery();
			int n = 1;
			if(sel_rs.next()) {
				pstmt2.setString(1, "%" + name + "%");
				ResultSet sel_rs2 = pstmt2.executeQuery();
				while(sel_rs2.next()) {
					System.out.println("┌──────────────────────────────────────────────┐");
					System.out.println(" [도서 검색 결과" + n + "]");
					System.out.println(" ▶도서 이름: " + sel_rs2.getString(1));
					System.out.println(" ▶도서 번호: " + sel_rs2.getString(2));
					System.out.println(" ▶보유 도서 수: " + sel_rs2.getString(3));
					System.out.println(" ▶지은이, 출판사: " + sel_rs2.getString(4));
					System.out.println("└──────────────────────────────────────────────┘");
					n = n+1;
				}
				sel_rs2.close();
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
	
	public void connectDatabase() {
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			String sql1 = "insert into BookDB values(?, ?, ?)";
			pstmt1 = con.prepareStatement(sql1);
			
			String sql2 = "select * from BookDB where bName like ?";
			pstmt2 = con.prepareStatement(sql2);
			
			String sql3 = "delete from BookDB where bName = ?";
			pstmt3 = con.prepareStatement(sql3);
			
			String sql4 = "select * from BookDB";
			pstmt4 = con.prepareStatement(sql4);
			
			String sql5 = "select ad_Name from libsAdmin where ad_ID = ? and ad_PW = ?";
			pstmt5 = con.prepareStatement(sql5);
			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}
