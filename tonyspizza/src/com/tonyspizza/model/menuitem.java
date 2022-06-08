//Model for Menu Item 
package com.tonyspizza.model;

import java.math.BigDecimal;
import java.util.List;

public class menuitem
{
	private int _MenuItemID;
	public int getMenuItemID()
	{
		return this._MenuItemID;
	}
	public void setMenuItemID(int value)
	{
		this._MenuItemID = value;
	}

	private String _ItemName;
	public String getItemName()
	{
		return this._ItemName;
	}
	public void setItemName(String value)
	{
		this._ItemName = value;
	}

	private BigDecimal _Price;
	public BigDecimal getPrice()
	{
		return this._Price;
	}
	public void setPrice(BigDecimal value)
	{
		this._Price = value;
	}


	public menuitem(int MenuItemID,String ItemName,BigDecimal Price)
	{
		this._MenuItemID = MenuItemID;
		this._ItemName = ItemName;
		this._Price = Price;
	}
	
	public menuitem() {
		this._MenuItemID = 0;
		this._ItemName = "";
		this._Price = BigDecimal.ZERO;
	}
	
	private List<additions> _AdditionsList;
	public List<additions> getAdditionsList()
	{
		return this._AdditionsList;
	}
	public void setAdditionsList(List<additions> AdditionsList)
	{
		this._AdditionsList = AdditionsList;
	}
	
}