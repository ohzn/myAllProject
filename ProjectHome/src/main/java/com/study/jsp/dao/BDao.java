package com.study.jsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.study.jsp.board.command.BPageInfo;
import com.study.jsp.dto.BDto;
import com.study.jsp.dto.CDto;

public class BDao
{
	public static final int WRITE_SUCCESS = 1;
	public static final int MODIFY_SUCCESS = 1;
	public static final int REPLY_SUCCESS = 1;
	public static final int NON_EXISTENT = 0;
	public static final int NOT_YOUR_RULE = -1;
	
	private static BDao instance = new BDao();
	DataSource dataSource;
	
	int listCount = 5;			// 한 페이지당 보여줄 게시물의 갯수
	int pageCount = 5;			// 하단에 보여줄 페이지 리스트의 갯수
	
	private BDao() {
		try {
			// lookup 함수의 파라미터는 context.xml에 설정된
			// name(jdbc/Oracle11g)과 동일해야 한다.
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BDao getInstance() {
		return instance;
	}
	
	public int write(String bMem, String bTitle, String bContent) {
		int rn = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			String query = "insert into mem_board " + 
						   " (bId, bMem, bTitle, bContent, bHit, bGroup, bStep, bIndent, bGood) " + 
						   " values " + 
						   " (mem_board_seq.nextval, ?, ?, ?, 0, mem_board_seq.currval, 0, 0, 0)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bMem);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			rn = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rn;
	}
	
	
	public ArrayList<BDto> list(int curPage){
		ArrayList<BDto> dtos = new ArrayList<BDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		int nStart = (curPage -1) * listCount + 1;
		int nEnd = (curPage -1) * listCount + listCount;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * " +
						   "  from ( " +
						   "    select rownum num, A.* " +
						   "      from ( " +
						   "		select * " +
						   "          from mem_board" +
						   "         order by bgroup desc, bstep asc ) A " +
						   "	 where rownum <= ? ) B " +
						   " where B.num >= ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, nEnd);
			pstmt.setInt(2, nStart);
			
			resultSet = pstmt.executeQuery();

			while(resultSet.next()) {
				

				int bId = resultSet.getInt("bId");
				String bMem = resultSet.getString("bMem");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");
				int bGood = resultSet.getInt("bGood");
				int c_number = resultSet.getInt("c_number");
				
				BDto dto = new BDto(bId, bMem, bTitle, bContent, bDate,
									bHit, bGroup, bStep, bIndent, bGood, c_number);
				
				dtos.add(dto);

			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return dtos;
	}
	
	public BPageInfo articlePage(int curPage) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		// 총 개시물의 갯수
		int totalCount = 0;
		try {
			con = dataSource.getConnection();
			
			String query = "select count(*) as total from mem_board";
			// 보드 이름 추가
			pstmt = con.prepareStatement(query);
			resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) {
				totalCount = resultSet.getInt("total");
				System.out.println("totalcount : " + totalCount);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		// 총 페이지 수
		int totalPage = totalCount / listCount;
		if (totalCount % listCount > 0)
			totalPage++;
		
		// 현재 페이지
		int myCurPage = curPage;
		if (myCurPage > totalPage)
			myCurPage = totalPage;
		if (myCurPage < 1)
			myCurPage = 1;
		
		// 시작 페이지
		int startPage = ((myCurPage -1) / pageCount) * pageCount +1;
		
		// 끝 페이지
		int endPage = startPage + pageCount -1;
		if (endPage > totalPage)
			endPage = totalPage;
		
		BPageInfo pinfo = new BPageInfo();
		pinfo.setTotalCount(totalCount);
		pinfo.setListCount(listCount);
		pinfo.setTotalPage(totalPage);
		pinfo.setCurPage(myCurPage);
		pinfo.setPageCount(pageCount);
		pinfo.setStartPage(startPage);
		pinfo.setEndPage(endPage);
		
		return pinfo;
	}
	
	// 서치는 게시판 통합
	public ArrayList<BDto> search_list(int curPage, String searchValue, String searchContent) {
		ArrayList<BDto> dtos = new ArrayList<BDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			System.out.println("1 : " + searchValue);
			System.out.println("2 : " + searchContent);
			
			int nStart = (curPage -1) * listCount + 1;
			int nEnd = (curPage -1) * listCount + listCount;
			
			
			if(searchValue.equals("bMem")) {
//				String query = "select * from mem_board where bMem like ?";
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, "%" + searchContent + "%");
				String query = "select * " +
						   "  from ( " +
						   "    select rownum num, A.* " +
						   "      from ( " +
						   "		select * " +
						   "          from mem_board where bMem like ?" +
						   "         order by bgroup desc, bstep asc ) A " +
						   "	 where rownum <= ? ) B " +
						   " where B.num >= ? ";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, "%" + searchContent + "%");
				pstmt.setInt(2, nEnd);
				pstmt.setInt(3, nStart);
				
				resultSet = pstmt.executeQuery();
				
				while(resultSet.next()) {
					int bId = resultSet.getInt("bId");
					String bMem = resultSet.getString("bMem");
					String bTitle = resultSet.getString("bTitle");
					String bContent = resultSet.getString("bContent");
					Timestamp bDate = resultSet.getTimestamp("bDate");
					int bHit = resultSet.getInt("bHit");
					int bGroup = resultSet.getInt("bGroup");
					int bStep = resultSet.getInt("bStep");
					int bIndent = resultSet.getInt("bIndent");
					int bGood = resultSet.getInt("bGood");
					int c_number = resultSet.getInt("c_number");
					
					BDto dto = new BDto(bId, bMem, bTitle, bContent, bDate,
							bHit, bGroup, bStep, bIndent, bGood, c_number);
					
					dtos.add(dto);
				}
				
			} else if(searchValue.equals("bTitle")) {
//				String query = "select * from mem_board where bTitle like ?";
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, "%" + searchContent + "%");
				String query = "select * " +
						   "  from ( " +
						   "    select rownum num, A.* " +
						   "      from ( " +
						   "		select * " +
						   "          from mem_board where bTitle like ?" +
						   "         order by bgroup desc, bstep asc ) A " +
						   "	 where rownum <= ? ) B " +
						   " where B.num >= ? ";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, "%" + searchContent + "%");
				pstmt.setInt(2, nEnd);
				pstmt.setInt(3, nStart);
				
				resultSet = pstmt.executeQuery();
				
				while(resultSet.next()) {
					int bId = resultSet.getInt("bId");
					String bMem = resultSet.getString("bMem");
					String bTitle = resultSet.getString("bTitle");
					String bContent = resultSet.getString("bContent");
					Timestamp bDate = resultSet.getTimestamp("bDate");
					int bHit = resultSet.getInt("bHit");
					int bGroup = resultSet.getInt("bGroup");
					int bStep = resultSet.getInt("bStep");
					int bIndent = resultSet.getInt("bIndent");
					int bGood = resultSet.getInt("bGood");
					int c_number = resultSet.getInt("c_number");
					
					
					BDto dto = new BDto(bId, bMem, bTitle, bContent, bDate,
							bHit, bGroup, bStep, bIndent, bGood, c_number);
					
					dtos.add(dto);
				}
			
			
			
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return dtos;
	}
	
	// 서치 페이지도 게시판 통합
	public BPageInfo articlePage_sPage(int curPage, String searchValue, String searchContent) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		// 총 개시물의 갯수
		int totalCount = 0;
		
		if(searchValue.equals("bMem")) {
			try {
				con = dataSource.getConnection();
				
				String query = "select count(*) as total from mem_board where bMem like ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, "%" + searchContent + "%");
				
				resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) {
					totalCount = resultSet.getInt("total");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (resultSet != null) resultSet.close();
					if (pstmt != null) pstmt.close();
					if (con != null) con.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		} else if(searchValue.equals("bTitle")) {
			try {
				con = dataSource.getConnection();
				
				String query = "select count(*) as total from mem_board where bTitle like ?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, "%" + searchContent + "%");
				
				resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) {
					totalCount = resultSet.getInt("total");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (resultSet != null) resultSet.close();
					if (pstmt != null) pstmt.close();
					if (con != null) con.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		
		
		
		// 총 페이지 수
		int totalPage = totalCount / listCount;
		if (totalCount % listCount > 0)
			totalPage++;
		
		// 현재 페이지
		int myCurPage = curPage;
		if (myCurPage > totalPage)
			myCurPage = totalPage;
		if (myCurPage < 1)
			myCurPage = 1;
		
		// 시작 페이지
		int startPage = ((myCurPage -1) / pageCount) * pageCount +1;
		
		// 끝 페이지
		int endPage = startPage + pageCount -1;
		if (endPage > totalPage)
			endPage = totalPage;
		
		BPageInfo pinfo = new BPageInfo();
		pinfo.setTotalCount(totalCount);
		pinfo.setListCount(listCount);
		pinfo.setTotalPage(totalPage);
		pinfo.setCurPage(myCurPage);
		pinfo.setPageCount(pageCount);
		pinfo.setStartPage(startPage);
		pinfo.setEndPage(endPage);
		
		return pinfo;
	}
	
	
	public BDto contentView(String strID) {
		upHit(strID);
		
		BDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * from mem_board where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(strID));
			resultSet = pstmt.executeQuery();
			
			if(resultSet.next()) {
				int bId = resultSet.getInt("bId");
				String bMem = resultSet.getString("bMem");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");
				int bGood = resultSet.getInt("bGood");
				int c_number = resultSet.getInt("c_number");

				
				dto = new BDto(bId, bMem, bTitle, bContent, bDate,
							   bHit, bGroup, bStep, bIndent, bGood, c_number);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	public void modify(String bId, String bMem, String bTitle, String bContent) {

		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "update mem_board set bMem = ?, bTitle = ?, bContent = ? where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bMem);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, Integer.parseInt(bId));
			int rn = pstmt.executeUpdate();
			
			// 조회수 -1
			downHit(bId);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	public void delete(String bId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dataSource.getConnection();
			String query = "delete from mem_board where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(bId));
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public BDto reply_view(String str) {
		BDto dto = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * from mem_board where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(str));
			resultSet = pstmt.executeQuery();
			
			if(resultSet.next()) {
				int bId = resultSet.getInt("bId");
				String bMem = resultSet.getString("bMem");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");
				int bGood = resultSet.getInt("bGood");
				int c_number = resultSet.getInt("c_number");

				
				dto = new BDto(bId, bMem, bTitle, bContent, bDate,
							   bHit, bGroup, bStep, bIndent, bGood, c_number);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	
	public void reply(String bId,String bMem,String bTitle,String bContent,
					  String bGroup,String bStep,String bIndent)
	{
		replyShape(bGroup, bStep);
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "insert into mem_board " +
						   " (bId, bMem, bTitle, bContent, bGroup, bStep, bIndent, bGood, c_number) " +
						   " values (mem_board_seq.nextval, ?, ?, ?, ?, ?, ?, 0, 0)";
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, bMem);
			pstmt.setString(2, bTitle);
			pstmt.setString(3, bContent);
			pstmt.setInt(4, Integer.parseInt(bGroup));
			pstmt.setInt(5, Integer.parseInt(bStep) + 1);
			pstmt.setInt(6, Integer.parseInt(bIndent) + 1);
			
			int rn = pstmt.executeUpdate();
			System.out.println("aa : " + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private void replyShape(String strGroup, String strStep) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			String query = "update mem_board " + 
						   "   set bStep = bStep + 1 " +
						   " where bGroup = ? and bStep > ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(strGroup));
			pstmt.setInt(2, Integer.parseInt(strStep));
			
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void show_Cnumber(String bId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		
		try {
			con = dataSource.getConnection();
			
			String query = "select c_number from mem_board where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(bId));
			resultSet = pstmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void upGood(String bId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		try {
			con = dataSource.getConnection();
			String query = "update mem_board set bGood = bGood+1 where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bId);
			
			int rn = pstmt.executeUpdate();
			
			// 조회수 -1
			downHit(bId);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (pstmt2 != null) pstmt2.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public ArrayList<CDto> comment_list(String strID){
		ArrayList<CDto> dtos = new ArrayList<CDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * from comment_board where bId = ? order by rId asc";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(strID));
			resultSet = pstmt.executeQuery();
			
			while(resultSet.next()) {
				int bId = resultSet.getInt("bId");
				int rId = resultSet.getInt("rId");
				String rMem = resultSet.getString("rMem");
				String rContent = resultSet.getString("rContent");
				
				CDto dto = new CDto(bId, rId, rMem, rContent);
				
				dtos.add(dto);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return dtos;
		
	}

	public void comment(String bId, String rMem, String rContent) {
		int rn = 0;
		int rn2 = 0;
		int rn3 = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		
		try {
			con = dataSource.getConnection();
			String query = "insert into comment_board " +
							" (bId, rId, rMem, rContent) " + 
							" values " +
							" (?, comment_board_seq.nextval, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bId);
			pstmt.setString(2, rMem);
			pstmt.setString(3, rContent);
			
			rn = pstmt.executeUpdate();
			
			// 조회수 -1
			downHit(bId);
			
			// 댓글갯수 +1
			String query3 = "update mem_board set c_number = c_number+1 where bId = ?";
			pstmt3 = con.prepareStatement(query3);
			pstmt3.setString(1, bId);
			rn3 = pstmt3.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(pstmt2 != null) pstmt2.close();
				if(pstmt3 != null) pstmt3.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void comment_delete(String rId, String bId) {
		int rn = 0;
		int rn2 = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "delete from comment_board where rId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(rId));
			rn = pstmt.executeUpdate();
			
			// 조회수 -1
			downHit(bId);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(pstmt2 != null) pstmt2.close();
				if(con != null) pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	// 적용못해봄..
	public void comment_modify(int rId, String rContent) {
		int rn = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "update comment_board set rContent = ? where rId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, rContent);
			pstmt.setInt(2, rId);
			rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	
 	private void upHit(String bId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			String query = "update mem_board set bHit = bHit+1 where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bId);
			
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
 	
 	private void downHit(String bId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			String query = "update mem_board set bHit = bHit-1 where bId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bId);
			
			int rn = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
}
