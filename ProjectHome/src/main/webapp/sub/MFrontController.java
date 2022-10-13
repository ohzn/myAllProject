package com.study.jsp.frontcontroller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.board.command.BCommand;
import com.study.jsp.board.command.BCommentCommand;
import com.study.jsp.board.command.BComment_DeleteCommand;
import com.study.jsp.board.command.BComment_viewCommand;
import com.study.jsp.board.command.BContentCommand;
import com.study.jsp.board.command.BDeleteCommand;
import com.study.jsp.board.command.BGoodCommand;
import com.study.jsp.board.command.BListCommand;
import com.study.jsp.board.command.BModifyCommand;
import com.study.jsp.board.command.BReplyCommand;
import com.study.jsp.board.command.BReplyViewCommand;
import com.study.jsp.board.command.BSearch_viewCommand;
import com.study.jsp.board.command.BWriteCommand;
import com.study.jsp.dao.MDao;
import com.study.jsp.member.command.MCommand;
import com.study.jsp.member.command.MDeleteCommand;
import com.study.jsp.member.command.MJoinCommand;
import com.study.jsp.member.command.MListCommand;
import com.study.jsp.member.command.MLoginCommand;
import com.study.jsp.member.command.MLogoutCommand;
import com.study.jsp.member.command.MModifyCommand;
import com.study.jsp.member.command.MOutCommand;
import com.study.jsp.member.command.MSearch_memberCommand;
import com.study.jsp.member.command.MViewCommand;

@WebServlet("*.do")
public class MFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MFrontController() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("doGet");
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("doPost");
		actionDo(request, response);
	}
	
	protected void actionDo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("actionDo");
		request.setCharacterEncoding("UTF-8");
		
		String viewPage = null;
		MCommand command = null;
		BCommand bcommand = null;
		
		String uri = request.getRequestURI();
		String conPath = request.getContextPath();
		String com = uri.substring(conPath.length());
		
		HttpSession session = null;
		session = request.getSession();
		int curPage = 1;
		if(com.equals("/main.do")) {
			viewPage = "main.jsp";
		} else if (com.equals("/join.do")) {
			MDao dao = MDao.getInstance();
			String mId = request.getParameter("mId");
			
			String json_data = "";
			if(dao.confirmId(mId) == MDao.MEMBER_EXISTENT) {
				json_data = "{\"code\":\"fail\", \"desc\":\"아이디가 이미 존재합니다.\"}";
				sendJson(response, json_data);
			} else {
				System.out.println("JoinOk");
				command = new MJoinCommand();
				command.execute(request, response);
				
				json_data="{\"code\":\"success\", \"desc\":\"회원가입을 축하합니다.\"}";
				sendJson(response, json_data);
			}

			return;
			
		} else if(com.equals("/login.do")) {
			MDao dao = MDao.getInstance();
			String mId = request.getParameter("mId");
			String mPw = request.getParameter("mPw");
			
			int checkNum = dao.userCheck(mId, mPw);
			String json_data = "";
			if(checkNum == -1) {
				json_data = "{\"code\":\"fail\", \"desc\":\"아이디가 존재하지 않습니다.\"}";
				sendJson(response, json_data);
			} else if (checkNum == 0) {
				json_data = "{\"code\":\"fail\", \"desc\":\"비밀번호가 일치하지 않습니다.\"}";
				sendJson(response, json_data);
			} else if (checkNum == 1) {
				command = new MLoginCommand();
				command.execute(request, response);
			
				json_data="{\"code\":\"success\", \"desc\":\"로그인 되었습니다.\"}";
				sendJson(response, json_data);
			}
			return;
			
		} else if(com.equals("/logout.do")) {
			command = new MLogoutCommand();
			command.execute(request, response);
			System.out.println("logout");
			String json_data = "{\"code\":\"success\", \"desc\":\"로그아웃 되었습니다.\"}";
			sendJson(response, json_data);
			return;
		} else if (com.equals("/modify.do")) {
			MDao dao = MDao.getInstance();
			String mId = (String)session.getAttribute("id");
			String mPw = request.getParameter("mPw");
			
			String json_data = "";
			int checkNum = dao.userCheck(mId, mPw);
			if(checkNum==1) {
				command = new MModifyCommand();
				command.execute(request, response);
				
				json_data = "{\"code\":\"success\", \"desc\":\"회원 정보가 수정되었습니다.\"}";
				sendJson(response, json_data);
			} else if(checkNum == 0) {
				json_data = "{\"code\":\"fail\", \"desc\":\"맞는 비밀번호가 아닙니다. 비밀번호를 확인해주세요.\"}";
				sendJson(response, json_data);
			} else {
				json_data = "{\"code\":\"fail\", \"desc\":\"오류가 발생하였습니다.\"}";
				sendJson(response, json_data);
			}
			return;
		} else if(com.equals("/mem_delete.do")) {
			command = new MDeleteCommand();
			command.execute(request, response);

			String json_data = "";
			json_data = "{\"code\":\"success\", \"desc\":\"회원 탈퇴 완료.\"}";
			sendJson(response, json_data);
			return;
		} else if (com.equals("/mlist.do")) {
			command = new MListCommand();
			command.execute(request, response);
			viewPage="admin_menu_memberlist.jsp";
		} else if(com.equals("/memberview.do")) {
			command = new MViewCommand();
			command.execute(request, response);
			viewPage = "admin_menu_memberview.jsp";

		}else if (com.equals("/mdelete.do")) {
			command = new MOutCommand();
			command.execute(request, response);
			System.out.println("memberOut ok");
			viewPage = "mlist.do?page="+curPage;
		} else if(com.equals("/search_member.do")) {
			command = new MSearch_memberCommand();
			command.execute(request, response);
			
			viewPage= "admin_menu_membersearch.jsp";
		} else if(com.equals("/write_view.do")) {
			viewPage = "write_view.jsp";
		} else if (com.equals("/write.do")) {
			bcommand = new BWriteCommand();
			bcommand.execute(request, response);
			String json_data = "{\"code\":\"success\", \"desc\":\"글을 작성했습니다.\"}";
			sendJson(response, json_data);
			
			return;
		} else if(com.equals("/list.do")) {
			bcommand = new BListCommand();
			bcommand.execute(request, response);
			
			//viewPage="list.jsp";
			return;
		} else if(com.equals("/content_view.do")) {
			bcommand = new BContentCommand();
			bcommand.execute(request, response);
			
			bcommand = new BComment_viewCommand();
			bcommand.execute(request, response);		// 댓글 보여주는 기능
			
			//return;
			viewPage="content_view.jsp";
		} else if(com.equals("/modify_view.do")) {
			bcommand = new BContentCommand();
			bcommand.execute(request, response);
			viewPage="modify_view.jsp";
		} else if (com.equals("/modify_ok.do")) {
			bcommand = new BModifyCommand();
			bcommand.execute(request, response);

			String json_data = "{\"code\":\"success\", \"desc\":\"수정을 완료했습니다.\"}";
			sendJson(response, json_data);
			
			return;			
		} else if (com.equals("/delete.do")) {
			bcommand = new BDeleteCommand();
			bcommand.execute(request, response);
			System.out.println("delete ok");
			viewPage = "list.do?page="+curPage;
		} else if (com.equals("/reply_view.do")) {
			bcommand = new BReplyViewCommand();
			bcommand.execute(request, response);
			viewPage= "reply_view.jsp";
		} else if(com.equals("/reply.do")) {
			bcommand = new BReplyCommand();
			bcommand.execute(request, response);
			String json_data = "{\"code\":\"success\", \"desc\":\"답글을 작성했습니다.\"}";
			sendJson(response, json_data);
			
			return;
		} else if(com.equals("/good.do")) {
			bcommand = new BGoodCommand();
			bcommand.execute(request, response);
			String json_data = "{\"code\":\"success\", \"desc\":\"추천했습니다.\"}";
			sendJson(response, json_data);
			
			return;	
		} else if(com.equals("/comment.do")) {
			bcommand = new BCommentCommand();
			bcommand.execute(request, response);
			String json_data = "{\"code\":\"success\", \"desc\":\"댓글을 남겼습니다.\"}";
			sendJson(response, json_data);
			
			return;	
		} else if(com.equals("/del_comment.do")) {
			bcommand = new BComment_DeleteCommand();
			bcommand.execute(request, response);
			String json_data = "{\"code\":\"success\", \"desc\":\"댓글을 삭제했습니다.\"}";
			sendJson(response, json_data);
			return;	
		} else if(com.equals("/search_view.do")) {
			bcommand = new BSearch_viewCommand();
			bcommand.execute(request, response);
			
			viewPage= "search_view.jsp";
		}
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}
	
	public void sendJson(HttpServletResponse response, String json_data)
			throws ServletException, IOException
	{
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println(json_data);
		writer.close();
	}

}
