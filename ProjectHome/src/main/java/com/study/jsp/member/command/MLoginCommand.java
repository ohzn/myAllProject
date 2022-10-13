package com.study.jsp.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MLoginCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		
		String mId = request.getParameter("mId");
		String mPw = request.getParameter("mPw");
		
		MDao dao = MDao.getInstance();
		dao.userCheck(mId, mPw);
		MDto dto = dao.getMember(mId);
		String mName = dto.getmName();
		String mGrade = dto.getmGrade();
		System.out.println("aa : " + mGrade);

		HttpSession session = request.getSession();
		session.setAttribute("id", mId);
		session.setAttribute("name", mName);
		session.setAttribute("grade", mGrade);
		
	}
}
