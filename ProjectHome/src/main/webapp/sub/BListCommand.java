package com.study.jsp.board.command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.study.jsp.dao.BDao;
import com.study.jsp.dto.BDto;

public class BListCommand implements BCommand
{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)

	{
		String bBoard = request.getParameter("b_name");

		System.out.println("b_name : " + bBoard);

		
		
		int nPage = 1;
		try {
			String sPage = request.getParameter("page");
			nPage = Integer.parseInt(sPage);
		} catch (Exception e) {
			
		}
		
		BDao dao = BDao.getInstance();
		BPageInfo pinfo = dao.articlePage(nPage, bBoard);
		request.setAttribute("page", pinfo);
		
		nPage = pinfo.getCurPage();
		
		HttpSession session = null;
		session = request.getSession();
		session.setAttribute("cpage", nPage);
		
		ArrayList<BDto> dtos = dao.list(nPage, bBoard);
		request.setAttribute("list", dtos);
		
		request.setAttribute("b_name", bBoard);
		System.out.println(dtos);
		
		// 제이슨형태로 담기
		String json = null;
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer;
        
        try
        {
        	writer = response.getWriter();
        	
        	for(int i = 0 ; i<dtos.size() ; i++ ) {
                if( i == (dtos.size()-1)) {
                	System.out.println("i : " + i);
                    json = "{\"bId\":\""+dtos.get(i).getbId()+"\",";
                    json += "\"bMem\":\"" +dtos.get(i).getbMem()+"\",";
                    json += "\"bTitle\":\"" +dtos.get(i).getbTitle()+"\",";
                    json += "\"bContent\":\"" +dtos.get(i).getbContent()+"\",";
                    json += "\"bDate\":\""+ dtos.get(i).getbDate()+"\",";
                    json += "\"bHit\":\"" + dtos.get(i).getbHit()+"\",";
                    json += "\"bGroup\":\"" + dtos.get(i).getbGroup()+"\",";
                    json += "\"bStep\":\"" + dtos.get(i).getbGroup()+"\",";
                    json += "\"bIndent\":\"" + dtos.get(i).getbIndent()+"\",";
                    json += "\"bGood\":\"" + dtos.get(i).getbGood()+"\",";
                    json += "\"c_number\":\"" + dtos.get(i).getC_number()+"\",";
                    json += "\"bBoard\":\"" + dtos.get(i).getbBoard()+"\"}]";

                    System.out.println(json);
                    writer.print(json);
                }
				else {
                    if(i == 0)
                    {
                    	System.out.println("i : " + i);
						json ="[";
						json += "{\"bId\":\""+dtos.get(i).getbId()+"\",";
	                    json += "\"bMem\":\"" +dtos.get(i).getbMem()+"\",";
	                    json += "\"bTitle\":\"" +dtos.get(i).getbTitle()+"\",";
	                    json += "\"bContent\":\"" +dtos.get(i).getbContent()+"\",";
	                    json += "\"bDate\":\""+ dtos.get(i).getbDate()+"\",";
	                    json += "\"bHit\":\"" + dtos.get(i).getbHit()+"\",";
	                    json += "\"bGroup\":\"" + dtos.get(i).getbGroup()+"\",";
	                    json += "\"bStep\":\"" + dtos.get(i).getbGroup()+"\",";
	                    json += "\"bIndent\":\"" + dtos.get(i).getbIndent()+"\",";
	                    json += "\"bGood\":\"" + dtos.get(i).getbGood()+"\",";
	                    json += "\"c_number\":\"" + dtos.get(i).getC_number()+"\",";
	                    json += "\"bBoard\":\"" + dtos.get(i).getbBoard()+"\"}";

                    }else {
                    	System.out.println("i : " + i);
                    	json = "{\"bId\":\""+dtos.get(i).getbId()+"\",";
	                    json += "\"bMem\":\"" +dtos.get(i).getbMem()+"\",";
	                    json += "\"bTitle\":\"" +dtos.get(i).getbTitle()+"\",";
	                    json += "\"bContent\":\"" +dtos.get(i).getbContent()+"\",";
	                    json += "\"bDate\":\""+ dtos.get(i).getbDate()+"\",";
	                    json += "\"bHit\":\"" + dtos.get(i).getbHit()+"\",";
	                    json += "\"bGroup\":\"" + dtos.get(i).getbGroup()+"\",";
	                    json += "\"bStep\":\"" + dtos.get(i).getbGroup()+"\",";
	                    json += "\"bIndent\":\"" + dtos.get(i).getbIndent()+"\",";
	                    json += "\"bGood\":\"" + dtos.get(i).getbGood()+"\",";
	                    json += "\"c_number\":\"" + dtos.get(i).getC_number()+"\",";
	                    json += "\"bBoard\":\"" + dtos.get(i).getbBoard()+"\"}";
                    }
                    System.out.println(json);
                    writer.print(json);
                }
            }
            writer.close();
        	
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}
