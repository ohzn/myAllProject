package com.study.jsp.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.BDao;
import com.study.jsp.dto.BDto;

public class BModifyCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		
		
		String bId = request.getParameter("bId");
		String bMem = (String)session.getAttribute("id");
		String bTitle = request.getParameter("bTitle");
		String bContent = request.getParameter("bContent");
		
		BDao dao = BDao.getInstance();
		dao.modify(bId, bMem, bTitle, bContent);
	}
}
