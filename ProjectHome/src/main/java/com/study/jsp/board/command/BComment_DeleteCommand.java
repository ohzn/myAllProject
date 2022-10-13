package com.study.jsp.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.BDao;

public class BComment_DeleteCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String rId = request.getParameter("rId");
		String bId = request.getParameter("bId");
		
		BDao dao = BDao.getInstance();
		dao.comment_delete(rId, bId);
		System.out.println("rId : " + rId);
		System.out.println("bId : " + bId);
	}
}
