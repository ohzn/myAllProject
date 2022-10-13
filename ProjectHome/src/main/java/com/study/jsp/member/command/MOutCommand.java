package com.study.jsp.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.MDao;

public class MOutCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String mId = request.getParameter("mId");
		MDao dao = MDao.getInstance();
		dao.getOut(mId);
	}

}
