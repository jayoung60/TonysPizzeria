//Model for Catering Request item
package com.tonyspizza.model;

public class cateringrequest
{
	private int _CaterID;
	public int getCaterID()
	{
		return this._CaterID;
	}
	public void setCaterID(int value)
	{
		this._CaterID = value;
	}

	private String _FirstName;
	public String getFirstName()
	{
		return this._FirstName;
	}
	public void setFirstName(String value)
	{
		this._FirstName = value;
	}

	private String _LastName;
	public String getLastName()
	{
		return this._LastName;
	}
	public void setLastName(String value)
	{
		this._LastName = value;
	}

	private String _CaterDescription;
	public String getCaterDescription()
	{
		return this._CaterDescription;
	}
	public void setCaterDescription(String value)
	{
		this._CaterDescription = value;
	}

	private String _PhoneNum;
	public String getPhoneNum()
	{
		return this._PhoneNum;
	}
	public void setPhoneNum(String value)
	{
		this._PhoneNum = value;
	}

	private String _Email;
	public String getEmail()
	{
		return this._Email;
	}
	public void setEmail(String value)
	{
		this._Email = value;
	}

	private int _NumGuests;
	public int getNumGuests()
	{
		return this._NumGuests;
	}
	public void setNumGuests(int value)
	{
		this._NumGuests = value;
	}

	private java.sql.Timestamp _CaterDate;
	public java.sql.Timestamp getCaterDate()
	{
		return this._CaterDate;
	}
	public void setCaterDate(java.sql.Timestamp value)
	{
		this._CaterDate = value;
	}

	private int _CaterOrderTypeID;
	public int getCaterOrderTypeID()
	{
		return this._CaterOrderTypeID;
	}
	public void setCaterOrderTypeID(int value)
	{
		this._CaterOrderTypeID = value;
	}

	private String _CaterOrderType;
	public String getCaterOrderType()
	{
		return this._CaterOrderType;
	}
	public void setCaterOrderType(String value)
	{
		this._CaterOrderType = value;
	}

	public cateringrequest(int CaterID,String FirstName,String LastName,String CaterDescription,
			String PhoneNum,String Email,int NumGuests,java.sql.Timestamp CaterDate,int CaterOrderTypeID, String CaterOrderType)
	{
		this._CaterID = CaterID;
		this._FirstName = FirstName;
		this._LastName = LastName;
		this._CaterDescription = CaterDescription;
		this._PhoneNum = PhoneNum;
		this._Email = Email;
		this._NumGuests = NumGuests;
		this._CaterDate = CaterDate;
		this._CaterOrderTypeID = CaterOrderTypeID;
		this._CaterOrderType = CaterOrderType;
	}
	
	public cateringrequest()
	{
		this._CaterID = 0;
		this._FirstName = "";
		this._LastName = "";
		this._CaterDescription = "";
		this._PhoneNum = "";
		this._Email = "";
		this._NumGuests = 0;
		this._CaterDate = null;
		this._CaterOrderTypeID = 0;
		this._CaterOrderType = "";
	}
}