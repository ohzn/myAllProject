package com.study.jsp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.study.jsp.dto.MDto;
import com.study.jsp.member.command.MPageInfo;

public class MDao
{
	public static final int MEMBER_NONEXISTENT = 0;
	public static final int MEMBER_EXISTENT = 1;
	public static final int MEMBER_JOIN_FAIL = 0;
	public static final int MEMBER_JOIN_SUCCESS = 1;
	public static final int MEMBER_LOGIN_PW_NO_GOOD = 0;
	public static final int MEMBER_LOGIN_SUCCESS = 1;
	public static final int MEMBER_LOGIN_IS_NOT = -1;
	
	private static MDao instance = new MDao();
	DataSource dataSource;
	
	int listCount = 5;			// 한 페이지당 보여줄 게시물의 갯수
	int pageCount = 5;			// 하단에 보여줄 페이지 리스트의 갯수
	
	
	private MDao() {
		try {
			// lookup 함수의 파라미터는 context.xml에 설정된
			// name(jdbc/Oracle11g)과 동일해야 한다.
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/Oracle11g");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MDao getInstance() {
		return instance;
	}
	
	public int insertMember(MDto dto) {
		int ri = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String query = "insert into member_data values (?, ?, ?, ?, ?, ?, null, 1)";
		
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getmId());
			pstmt.setString(2, dto.getmPw());
			pstmt.setString(3, dto.getmName());
			pstmt.setString(4, dto.getmEmail());
			pstmt.setString(5, dto.getmAddress());
			pstmt.setTimestamp(6, dto.getmDate());
			pstmt.executeUpdate();
			
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
		
		return ri;
	}
	
	public int confirmId(String mId) {
		int ri = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select mId from member_data where mId = ?";
		
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, mId);
			set = pstmt.executeQuery();
			if(set.next()) {
				ri = MDao.MEMBER_EXISTENT;
			} else {
				ri = MDao.MEMBER_NONEXISTENT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				set.close();
				pstmt.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return ri;
	}
	
	public int userCheck(String mId, String mPw) {
		int ri = 0;
		String dbPw;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select mPw from member_data where mId = ?";
		
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, mId);
			set = pstmt.executeQuery();
			
			if(set.next()) {
				dbPw = set.getString("mPw");
				if(dbPw.equals(mPw)) {
					System.out.println("login ok");
					ri = MDao.MEMBER_LOGIN_SUCCESS;	// 로그인 ok
				} else {
					System.out.println("login fail");
					ri = MDao.MEMBER_LOGIN_PW_NO_GOOD;	// 비밀번호 x
				}
			} else {
				System.out.println("login fail");
				ri = MDao.MEMBER_LOGIN_IS_NOT;			// 아이디 x
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				set.close();
				pstmt.close();
				con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return ri;
	}
	
	public MDto getMember(String mId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet set = null;
		String query = "select * from member_data where mId = ?";
		MDto dto = null;
		
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, mId);
			set = pstmt.executeQuery();
			
			if(set.next()) {
				dto = new MDto();
				dto.setmId(set.getString("mId"));
				dto.setmPw(set.getString("mPw"));
				dto.setmName(set.getString("mName"));
				dto.setmEmail(set.getString("mEmail"));
				dto.setmDate(set.getTimestamp("mDate"));
				dto.setmAddress(set.getString("mAddress"));
				dto.setmGrade(set.getString("mGrade"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				set.close();
				pstmt.close();
				con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	public int updateMember(MDto dto) {
		int ri = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		String query = "update member_data set mPw=?, mEmail=?, mAddress=? where mId=?";
		
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getmPw());
			pstmt.setString(2, dto.getmEmail());
			pstmt.setString(3, dto.getmAddress());
			pstmt.setString(4, dto.getmId());
			ri = pstmt.executeUpdate();
			System.out.println("modify ok");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
		return ri;
	}
	
	public void goodBye(String mId, String mPw) {
		int ri = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet set = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * from member_data where mId = ? and mPw = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, mId);
			pstmt.setString(2, mPw);
			set = pstmt.executeQuery();
			
			if(set.next()) {
				String query2 = "delete from member_data where mId = ?";
				pstmt2 = con.prepareStatement(query2);
				pstmt2.setString(1, mId);
				ri = pstmt2.executeUpdate();
				System.out.println("member delete ok");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				con.close();
				set.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public ArrayList<MDto> user_list(int curPage) {
		ArrayList<MDto> dtos = new ArrayList<MDto>();
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
					   "          from member_data " +
					   "         order by mDate desc ) A " +
					   "	 where rownum <= ? ) B " +
					   " where B.num >= ? ";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, nEnd);
			pstmt.setInt(2, nStart);
			resultSet = pstmt.executeQuery();
			
			while(resultSet.next()) {
				String mId = resultSet.getString("mId");
				String mPw = resultSet.getString("mPw");
				String mName = resultSet.getString("mName");
				String mEmail = resultSet.getString("mEmail");
				String mAddress = resultSet.getString("mAddress");
				Timestamp mDate = resultSet.getTimestamp("mDate");
				Timestamp mBlack = resultSet.getTimestamp("mBlack");
				String mGrade = resultSet.getString("mGrade");
				
				MDto dto = new MDto(mId, mPw, mName, mEmail,
					 mAddress, mDate, mBlack, mGrade);
				
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
	
	public MPageInfo articlePage(int curPage) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		// 총 회원의 수
		int totalCount = 0;
		try {
			con = dataSource.getConnection();
			
			String query = "select count(*) as total from member_data";
			
			pstmt = con.prepareStatement(query);
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
		
		MPageInfo minfo = new MPageInfo();
		minfo.setTotalCount(totalCount);
		minfo.setListCount(listCount);
		minfo.setTotalPage(totalPage);
		minfo.setCurPage(myCurPage);
		minfo.setPageCount(pageCount);
		minfo.setStartPage(startPage);
		minfo.setEndPage(endPage);
		
		
		
		return minfo;
	}
	
	public ArrayList<MDto> suser_list(int curPage, String searchValue, String searchContent) {
		ArrayList<MDto> dtos = new ArrayList<MDto>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			System.out.println("1 : " + searchValue);
			System.out.println("2 : " + searchContent);
			
			int nStart = (curPage -1) * listCount + 1;
			int nEnd = (curPage -1) * listCount + listCount;
			
			if(searchValue.equals("mId")) {
				String query = "select * " +
						   "  from ( " +
						   "    select rownum num, A.* " +
						   "      from ( " +
						   "		select * " +
						   "          from member_data " +
						   "         order by mDate desc ) A " +
						   "	 where rownum <= ? ) B " +
						   " where B.num >= ? and mId like ?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, nEnd);
				pstmt.setInt(2, nStart);
				pstmt.setString(3, "%" + searchContent + "%");
				resultSet = pstmt.executeQuery();
				
				while(resultSet.next()) {
					String mId = resultSet.getString("mId");
					String mPw = resultSet.getString("mPw");
					String mName = resultSet.getString("mName");
					String mEmail = resultSet.getString("mEmail");
					String mAddress = resultSet.getString("mAddress");
					Timestamp mDate = resultSet.getTimestamp("mDate");
					Timestamp mBlack = resultSet.getTimestamp("mBlack");
					String mGrade = resultSet.getString("mGrade");
					
					MDto dto = new MDto(mId, mPw, mName, mEmail,
						 mAddress, mDate, mBlack, mGrade);
					
					dtos.add(dto);
				}
				
			} else if(searchValue.equals("mName")) {
				String query = "select * " +
						   "  from ( " +
						   "    select rownum num, A.* " +
						   "      from ( " +
						   "		select * " +
						   "          from member_data " +
						   "         order by mDate desc ) A " +
						   "	 where rownum <= ? ) B " +
						   " where B.num >= ? and mName like ?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, nEnd);
				pstmt.setInt(2, nStart);
				pstmt.setString(3, "%" + searchContent + "%");
				resultSet = pstmt.executeQuery();
				
				while(resultSet.next()) {
					String mId = resultSet.getString("mId");
					String mPw = resultSet.getString("mPw");
					String mName = resultSet.getString("mName");
					String mEmail = resultSet.getString("mEmail");
					String mAddress = resultSet.getString("mAddress");
					Timestamp mDate = resultSet.getTimestamp("mDate");
					Timestamp mBlack = resultSet.getTimestamp("mBlack");
					String mGrade = resultSet.getString("mGrade");
					
					MDto dto = new MDto(mId, mPw, mName, mEmail,
						 mAddress, mDate, mBlack, mGrade);
					
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
	
	public MPageInfo articlePage_mPage(int curPage, String searchValue, String searchContent) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		// 총 멤버 수
		int totalCount = 0;
		if(searchValue.equals("mId")) {
			try {
				con = dataSource.getConnection();
				
				String query = "select count(*) as total from member_data where mId like ?";
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
		} else if(searchValue.equals("mName")) {
			try {
				con = dataSource.getConnection();
				
				String query = "select count(*) as total from member_data where mName like ?";
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
		
		MPageInfo minfo = new MPageInfo();
		minfo.setTotalCount(totalCount);
		minfo.setListCount(listCount);
		minfo.setTotalPage(totalPage);
		minfo.setCurPage(myCurPage);
		minfo.setPageCount(pageCount);
		minfo.setStartPage(startPage);
		minfo.setEndPage(endPage);
		
		
		
		return minfo;
	}
	
	
	
	public MDto memberView(String strID) {
		MDto dto = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select * from member_data where mId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, strID);
			resultSet = pstmt.executeQuery();
			
			if(resultSet.next()) {
				String mId = resultSet.getString("mId");
				String mPw = resultSet.getString("mPw");
				String mName = resultSet.getString("mName");
				String mEmail = resultSet.getString("mEmail");
				String mAddress = resultSet.getString("mAddress");
				Timestamp mDate = resultSet.getTimestamp("mDate");
				Timestamp mBlack = resultSet.getTimestamp("mBlack");
				String mGrade = resultSet.getString("mGrade");
				
				dto = new MDto(mId, mPw, mName, mEmail,
							   mAddress, mDate, mBlack, mGrade);
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
	
	public void getOut(String mId) {
		int ri = 0;
		Connection con = null;
		PreparedStatement pstmt = null;

		
		try {
			con = dataSource.getConnection();
			
			String query = "delete from member_data where mId = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, mId);
			ri = pstmt.executeUpdate();
			System.out.println("member delete ok");
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				con.close();
			} catch(Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
}
