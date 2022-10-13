package com.study.jsp.board.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.jsp.dao.BDao;
import com.study.jsp.dto.CDto;

public class BComment_viewCommand implements BCommand
{
	public void execute(HttpServletRequest request, HttpServletResponse response)
	{
		String bId = request.getParameter("bId");
		System.out.println("bId : " + bId);
		
		BDao dao = BDao.getInstance();

		ArrayList<CDto> dtos = dao.comment_list(bId);
		request.setAttribute("list", dtos);
		System.out.println(dtos);
	}
}
