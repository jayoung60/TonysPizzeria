// Model for the MenuItem Additions 
package com.tonyspizza.model;

import java.math.BigDecimal;

public class additions
{
	private int _AdditionsID;
	public int getAdditionsID()
	{
		return this._AdditionsID;
	}
	public void setAdditionsID(int value)
	{
		this._AdditionsID = value;
	}

	private String _AddedName;
	public String getAddedName()
	{
		return this._AddedName;
	}
	public void setAddedName(String value)
	{
		this._AddedName = value;
	}

	private BigDecimal _AddedPrice;
	public BigDecimal getAddedPrice()
	{
		return this._AddedPrice;
	}
	public void setAddedPrice(BigDecimal value)
	{
		this._AddedPrice = value;
	}


	public additions(int AdditionsID,String AddedName,BigDecimal AddedPrice)
	{
		this._AdditionsID = AdditionsID;
		this._AddedName = AddedName;
		this._AddedPrice = AddedPrice;
	}
	
	public additions()
	{
		this._AdditionsID = 0;
		this._AddedName = "";
		this._AddedPrice = BigDecimal.ZERO;
	}
	
}