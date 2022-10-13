package com.study.jsp.dto;

import java.sql.Timestamp;

public class MDto
{
	String mId;
	String mPw;
	String mName;
	String mEmail;
	String mAddress;
	Timestamp mDate;
	Timestamp mBlack;
	String mGrade;
	
	
	public MDto() {
		
	}
	
	public MDto(String mId, String mPw, String mName, String mEmail,
			String mAddress, Timestamp mDate, Timestamp mBlack, String mGrade) {
		this.mId = mId;
		this.mPw = mPw;
		this.mName = mName;
		this.mAddress = mAddress;
		this.mDate = mDate;
		this.mBlack = mBlack;
		this.mGrade = mGrade;
	}


	public String getmGrade()
	{
		return mGrade;
	}

	public void setmGrade(String mGrade)
	{
		this.mGrade = mGrade;
	}

	public String getmId()
	{
		return mId;
	}

	public void setmId(String mId)
	{
		this.mId = mId;
	}

	public String getmPw()
	{
		return mPw;
	}

	public void setmPw(String mPw)
	{
		this.mPw = mPw;
	}

	public String getmName()
	{
		return mName;
	}

	public void setmName(String mName)
	{
		this.mName = mName;
	}
	
	public String getmEmail()
	{
		return mEmail;
	}

	public void setmEmail(String mEmail)
	{
		this.mEmail = mEmail;
	}

	public String getmAddress()
	{
		return mAddress;
	}

	public void setmAddress(String mAddress)
	{
		this.mAddress = mAddress;
	}

	public Timestamp getmDate()
	{
		return mDate;
	}

	public void setmDate(Timestamp mDate)
	{
		this.mDate = mDate;
	}

	public Timestamp getmBlack()
	{
		return mBlack;
	}

	public void setmBlack(Timestamp mBlack)
	{
		this.mBlack = mBlack;
	}
	
}
