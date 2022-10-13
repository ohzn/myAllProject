package com.study.jsp.dto;

import java.sql.Timestamp;

public class BDto
{
	int bId;
	String bMem;
	String bTitle;
	String bContent;
	Timestamp bDate;
	int bHit;
	int bGroup;
	int bStep;
	int bIndent;
	int bGood;
	int c_number;
	String bBoard;
	
	public BDto() {
		
	}
	
	public BDto(int bId, String bMem, String bTitle, String bContent,
			Timestamp bDate, int bHit, int bGroup, int bStep, int bIndent, int bGood, int c_number, String bBoard)
	{
		this.bId = bId;
		this.bMem = bMem;
		this.bTitle = bTitle;
		this.bContent = bContent;
		this.bDate = bDate;
		this.bHit = bHit;
		this.bGroup = bGroup;
		this.bStep = bStep;
		this.bIndent = bIndent;
		this.bGood = bGood;
		this.c_number = c_number;
		this.bBoard = bBoard;
	}

	public String getbBoard()
	{
		return bBoard;
	}

	public void setbBoard(String bBoard)
	{
		this.bBoard = bBoard;
	}

	public int getC_number()
	{
		return c_number;
	}

	public void setC_number(int c_number)
	{
		this.c_number = c_number;
	}

	public int getbId()
	{
		return bId;
	}

	public void setbId(int bId)
	{
		this.bId = bId;
	}

	public String getbMem()
	{
		return bMem;
	}

	public void setbMem(String bMem)
	{
		this.bMem = bMem;
	}

	public String getbTitle()
	{
		return bTitle;
	}

	public void setbTitle(String bTitle)
	{
		this.bTitle = bTitle;
	}

	public String getbContent()
	{
		return bContent;
	}

	public void setbContent(String bContent)
	{
		this.bContent = bContent;
	}

	public Timestamp getbDate()
	{
		return bDate;
	}

	public void setbDate(Timestamp bDate)
	{
		this.bDate = bDate;
	}

	public int getbHit()
	{
		return bHit;
	}

	public void setbHit(int bHit)
	{
		this.bHit = bHit;
	}

	public int getbGroup()
	{
		return bGroup;
	}

	public void setbGroup(int bGroup)
	{
		this.bGroup = bGroup;
	}

	public int getbStep()
	{
		return bStep;
	}

	public void setbStep(int bStep)
	{
		this.bStep = bStep;
	}

	public int getbIndent()
	{
		return bIndent;
	}

	public void setbIndent(int bIndent)
	{
		this.bIndent = bIndent;
	}

	public int getbGood()
	{
		return bGood;
	}

	public void setbGood(int bGood)
	{
		this.bGood = bGood;
	}
	
	
}
