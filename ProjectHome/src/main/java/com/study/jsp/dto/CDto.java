package com.study.jsp.dto;

public class CDto
{
	int bId;
	int rId;
	String rMem;
	String rContent;
	
	public CDto() {
		
	}
	
	public CDto(int bId, int rId, String rMem, String rContent) {
		this.bId = bId;
		this.rId = rId;
		this.rMem = rMem;
		this.rContent = rContent;
	}

	public int getbId()
	{
		return bId;
	}

	public void setbId(int bId)
	{
		this.bId = bId;
	}

	public int getrId()
	{
		return rId;
	}

	public void setrId(int rId)
	{
		this.rId = rId;
	}

	public String getrMem()
	{
		return rMem;
	}

	public void setrMem(String rMem)
	{
		this.rMem = rMem;
	}

	public String getrContent()
	{
		return rContent;
	}

	public void setrContent(String rContent)
	{
		this.rContent = rContent;
	}
	
}
