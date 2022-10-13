package com.study.jsp.member.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MListCommand implements MCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		int nPage = 1;
		try {
			String sPage = request.getParameter("page");
			// "page" 이건 list.jsp에서 보내는 리퀘스트( ${page.curpage} 이런애들..)
			nPage = Integer.parseInt(sPage);
		} catch (Exception e) {
			
		}
		
		MDao dao = MDao.getInstance();
		MPageInfo mInfo = dao.articlePage(nPage);
		request.setAttribute("page", mInfo);
		
		nPage = mInfo.getCurPage();
		
		HttpSession session = null;
		session = request.getSession();
		session.setAttribute("cpage", nPage);
		
		ArrayList<MDto> dto = dao.user_list(nPage);
		request.setAttribute("list", dto);
		
	}
}
