package com.study.jsp.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.BDao;

public class BCommentCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String bId = request.getParameter("bId");
		String rMem = request.getParameter("bMem");
		String rContent = request.getParameter("rContent");
		
		System.out.println("bId : " + bId);
		System.out.println("rMem : " + rMem);
		System.out.println("rContent : " + rContent);
		
		BDao dao = BDao.getInstance();
		dao.comment(bId, rMem, rContent);
	}
}
