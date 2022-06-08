//Model for an order
package com.tonyspizza.model;

import java.util.List;

public class order
{
	private int _OrderID;
	public int getOrderID()
	{
		return this._OrderID;
	}
	public void setOrderID(int value)
	{
		this._OrderID = value;
	}

	private java.sql.Timestamp _OrderDate;
	public java.sql.Timestamp getOrderDate()
	{
		return this._OrderDate;
	}
	public void setOrderDate(java.sql.Timestamp value)
	{
		this._OrderDate = value;
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
	public String getFullName() 
	{
		return this._LastName + ", " + this._FirstName;
	}

	private int _OrderTypeID;
	public int getOrderTypeID()
	{
		return this._OrderTypeID;
	}
	public void setOrderTypeID(int value)
	{
		this._OrderTypeID = value;
	}	
	
	private String _OrderType;
	public String getOrderType()
	{
		return this._OrderType;
	}
	public void setOrderType(String value)
	{
		this._OrderType = value;
	}

	private String _OrderAddress;
	public String getOrderAddress()
	{
		return this._OrderAddress;
	}
	public void setOrderAddress(String value)
	{
		this._OrderAddress = value;
	}

	private String _TimeOfPickup;
	public String getTimeOfPickup()
	{
		return this._TimeOfPickup;
	}
	public void setTimeOfPickup(String value)
	{
		this._TimeOfPickup = value;
	}

	private int _DelivererStaffID;
	public int getDelivererStaffID()
	{
		return this._DelivererStaffID;
	}
	public void setDelivererStaffID(int value)
	{
		this._DelivererStaffID = value;
	}
	
	private String _DelivererStaffName;
	public String getDelivererStaffName()
	{
		return this._DelivererStaffName;
	}
	public void setDelivererStaffName(String value)
	{
		this._DelivererStaffName = value;
	}
	
	private java.sql.Timestamp _OrderCompletedDateTime;
	public java.sql.Timestamp getOrderCompletedDateTime()
	{
		return this._OrderCompletedDateTime;
	}
	public void setOrderCompletedDateTime(java.sql.Timestamp value)
	{
		this._OrderCompletedDateTime = value;
	}

	private List<menuitem> _MenuItemList;
	public List<menuitem> getMenuItemList()
	{
		return this._MenuItemList;
	}
	public void setMenuItemList(List<menuitem> MenuItemList)
	{
		this._MenuItemList = MenuItemList;
	}

	//constructor with specific fields
	public order(int OrderID,java.sql.Timestamp OrderDate,String FirstName,String LastName,
			int OrderTypeID, String OrderType,String OrderAddress,String TimeOfPickup, 
			int DelivererStaffID, String DelivererStaffName,java.sql.Timestamp OrderCompletedDateTime,
			List<menuitem> MenuItemList, List<additions> AdditionsList)
	{
		this._OrderID = OrderID;
		this._OrderDate = OrderDate;
		this._FirstName = FirstName;
		this._LastName = LastName;
		this._OrderTypeID = OrderTypeID;
		this._OrderType = OrderType;
		this._OrderAddress = OrderAddress;
		this._TimeOfPickup = TimeOfPickup;
		this._DelivererStaffID = DelivererStaffID;
		this._DelivererStaffName = DelivererStaffName;
		this._OrderCompletedDateTime = OrderCompletedDateTime;
		this._MenuItemList = MenuItemList;
	}
	
	//constructor with blank fields
	public order()
	{
		this._OrderID = 0;
		this._OrderDate = null;
		this._FirstName = "";
		this._LastName = "";
		this._OrderTypeID = 0;
		this._OrderType = "";
		this._OrderAddress = "";
		this._TimeOfPickup = "";
		this._DelivererStaffID = 0;
		this._DelivererStaffName = "";
		this._OrderCompletedDateTime = null;
		this._MenuItemList = null;
	}	
}