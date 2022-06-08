//model for Employee
package com.tonyspizza.model;

import java.math.BigDecimal;

public class employee
{
	private int _StaffID;
	public int getStaffID()
	{
		return this._StaffID;
	}
	public void setStaffID(int value)
	{
		this._StaffID = value;
	}

	private int _SSN;
	public int getSSN()
	{
		return this._SSN;
	}
	public void setSSN(int value)
	{
		this._SSN = value;
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

	private String _MiddleInitial;
	public String getMiddleInitial()
	{
		return this._MiddleInitial;
	}
	public void setMiddleInitial(String value)
	{
		this._MiddleInitial = value;
	}

	public String getFullName() 
	{
		String fullname = this._LastName + ", " + this._FirstName;
		if (this._MiddleInitial != null)
			fullname += " " + this._MiddleInitial;
		return fullname;
	}
	
	private String _Address;
	public String getAddress()
	{
		return this._Address;
	}
	public void setAddress(String value)
	{
		this._Address = value;
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

	private BigDecimal _Salary;
	public BigDecimal getSalary()
	{
		return this._Salary;
	}
	public void setSalary(BigDecimal value)
	{
		this._Salary = value;
	}

	private String _WorkingHours;
	public String getWorkingHours()
	{
		return this._WorkingHours;
	}
	public void setWorkingHours(String value)
	{
		this._WorkingHours = value;
	}

	private boolean _isDeliveryPerson;
	public boolean getisDeliveryPerson()
	{
		return this._isDeliveryPerson;
	}
	public void setisDeliveryPerson(boolean value)
	{
		this._isDeliveryPerson = value;
	}

	private boolean _isCook;
	public boolean getisCook()
	{
		return this._isCook;
	}
	public void setisCook(boolean value)
	{
		this._isCook = value;
	}

	private boolean _isWaiter;
	public boolean getisWaiter()
	{
		return this._isWaiter;
	}
	public void setisWaiter(boolean value)
	{
		this._isWaiter = value;
	}

	private boolean _isHost;
	public boolean getisHost()
	{
		return this._isHost;
	}
	public void setisHost(boolean value)
	{
		this._isHost = value;
	}

	private boolean _isManager;
	public boolean getisManager()
	{
		return this._isManager;
	}
	public void setisManager(boolean value)
	{
		this._isManager = value;
	}

	private java.sql.Date _BirthDate;
	public java.sql.Date getBirthDate()
	{
		return this._BirthDate;
	}
	public void setBirthDate(java.sql.Date value)
	{
		this._BirthDate = value;
	}

	private String _Sex;
	public String getSex()
	{
		return this._Sex;
	}
	public void setSex(String value)
	{
		this._Sex = value;
	}

	private boolean _isActive;
	public boolean getisActive()
	{
		return this._isActive;
	}
	public void setisActive(boolean value)
	{
		this._isActive = value;
	}


	public employee(int StaffID,int SSN,String FirstName,String LastName,String MiddleInitial,
			String Address,String PhoneNum,BigDecimal Salary,String WorkingHours,
			boolean isDeliveryPerson,boolean isCook,boolean isWaiter,boolean isHost,boolean isManager,
			java.sql.Date BirthDate,String Sex,boolean isActive)
	{
		this._StaffID = StaffID;
		this._SSN = SSN;
		this._FirstName = FirstName;
		this._LastName = LastName;
		this._MiddleInitial = MiddleInitial;
		this._Address = Address;
		this._PhoneNum = PhoneNum;
		this._Salary = Salary;
		this._WorkingHours = WorkingHours;
		this._isDeliveryPerson = isDeliveryPerson;
		this._isCook = isCook;
		this._isWaiter = isWaiter;
		this._isHost = isHost;
		this._isManager = isManager;
		this._BirthDate = BirthDate;
		this._Sex = Sex;
		this._isActive = isActive;
	}
	
	public employee()
	{
		this._StaffID = 0;
		this._SSN = 0;
		this._FirstName = "";
		this._LastName = "";
		this._MiddleInitial = "";
		this._Address = "";
		this._PhoneNum = "";
		this._Salary = BigDecimal.ZERO;
		this._WorkingHours = "";
		this._isDeliveryPerson = false;
		this._isCook = false;
		this._isWaiter = false;
		this._isHost = false;
		this._isManager = false;
		this._BirthDate = null;
		this._Sex = "M";
		this._isActive = true;
	}
}