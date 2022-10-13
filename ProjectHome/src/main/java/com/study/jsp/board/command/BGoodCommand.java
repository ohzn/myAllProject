package com.study.jsp.board.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.BDao;

public class BGoodCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String bId = request.getParameter("bId");
		System.out.println(bId);
		BDao dao = BDao.getInstance();
		dao.upGood(bId);
	}
}
