package com.study.jsp.member.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MModifyCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession();
		MDto dto = new MDto();
		
		String mId = (String)session.getAttribute("id");
		String mPw = request.getParameter("mPw");
		String mName = request.getParameter("mName");
		String mEmail = request.getParameter("mEmail");
		String mAddress = request.getParameter("mAddress");
		
		dto.setmId(mId);
		dto.setmPw(mPw);
		dto.setmName(mName);
		dto.setmEmail(mEmail);
		dto.setmAddress(mAddress);
		
		MDao dao = MDao.getInstance();
		dao.updateMember(dto);
	}
}
