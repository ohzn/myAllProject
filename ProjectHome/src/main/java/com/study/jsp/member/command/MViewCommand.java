package com.study.jsp.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MViewCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String mId = request.getParameter("mId");
		
		MDao dao = MDao.getInstance();
		MDto dto = dao.memberView(mId);
		
		request.setAttribute("member_view", dto);
		
	}
}
