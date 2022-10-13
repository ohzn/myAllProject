package com.study.jsp.member.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.MDao;
import com.study.jsp.dto.MDto;

public class MSearch_memberCommand implements MCommand
{
	public 	void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String searchValue = request.getParameter("s_menu");
		String searchContent = request.getParameter("s_keyword");

		System.out.println("s_menu : " + searchValue);
		System.out.println("s_keyword : " + searchContent);

		int nPage = 1;
		try {
			String sPage = request.getParameter("page");
			// "page" 이건 list.jsp에서 보내는 리퀘스트( ${page.curpage} 이런애들..)
			nPage = Integer.parseInt(sPage);
		} catch (Exception e) {
			
		}
		
		MDao dao = MDao.getInstance();
		MPageInfo minfo = dao.articlePage_mPage(nPage, searchValue, searchContent);
		request.setAttribute("page", minfo);
		
		nPage = minfo.getCurPage();
		
		HttpSession session = null;
		session = request.getSession();
		session.setAttribute("cpage", nPage);
		
		ArrayList<MDto> dto = dao.suser_list(nPage, searchValue, searchContent);
		request.setAttribute("list", dto);
	}
}
