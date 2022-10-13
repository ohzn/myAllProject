package com.study.jsp.member.command;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MJoinCommand implements MCommand
{
		public void execute(HttpServletRequest request, HttpServletResponse response)
		{
			String mId = request.getParameter("mId");
			String mPw = request.getParameter("mPw");
			String mName = request.getParameter("mName");
			String mEmail = request.getParameter("mEmail");
			String mAddress = request.getParameter("mAddress");

			
			MDao dao = MDao.getInstance();
			MDto dto = new MDto();
			dto.setmId(mId);
			dto.setmPw(mPw);
			dto.setmName(mName);
			dto.setmEmail(mEmail);
			dto.setmAddress(mAddress);
			dto.setmDate(new Timestamp(System.currentTimeMillis()));
			
			dao.insertMember(dto);
			
			HttpSession session = request.getSession();
			session.setAttribute("id", dto.getmId());

		}
}
