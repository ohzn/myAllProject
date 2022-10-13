package com.study.jsp.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;

public class MDeleteCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		
		String mId = (String)session.getAttribute("id");
		String mPw = request.getParameter("mPw");
		
		MDao dao = MDao.getInstance();
		dao.goodBye(mId, mPw);
		session.invalidate();
	}
}
