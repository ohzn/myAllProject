package com.study.jsp.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.BDao;

public class BReplyCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		

		String bId = request.getParameter("bId");
		String bMem = request.getParameter("bMem");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		String bGroup = request.getParameter("bGroup");
		String bStep = request.getParameter("bStep");
		String bIndent = request.getParameter("bIndent");


		
		BDao dao = BDao.getInstance();
		dao.reply(bId, bMem, bTitle, bContent, bGroup, bStep, bIndent);
		System.out.println("go_reply2");
	}
}
