//Model for a listing of the menu items for an order
package com.tonyspizza.model;

import java.math.BigDecimal;
import java.util.List;

public class orderitem
{

	private int _OrderItemID;
	public int getOrderItemID()
	{
		return this._OrderItemID;
	}
	public void setOrderItemID(int value)
	{
		this._OrderItemID = value;
	}
	
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

	private BigDecimal _ItemPrice;
	public BigDecimal getItemPrice()
	{
		return this._ItemPrice;
	}
	public void setItemPrice(BigDecimal value)
	{
		this._ItemPrice = value;
	}

	public orderitem(int OrderItemID, int MenuItemID, String ItemName, BigDecimal ItemPrice)
	{
		this._OrderItemID = OrderItemID;
		this._MenuItemID = MenuItemID;
		this._ItemName = ItemName;
		this._ItemPrice = ItemPrice;
	}
	
	public orderitem() {
		this._OrderItemID = 0;
		this._MenuItemID = 0;
		this._ItemName = "";
		this._ItemPrice = BigDecimal.ZERO;
	}
	
	//get a list of all the additions to the menuitem
	private List<additions> _AdditionsList;
	public List<additions> getAdditionsList()
	{
		return this._AdditionsList;
	}
	public void setAdditionsList(List<additions> AdditionsList)
	{
		this._AdditionsList = AdditionsList;
	}
	
	// get the total price of all the addition items for a menu item
	public BigDecimal getAddedPrice() {
		BigDecimal bd = BigDecimal.ZERO;
		if ((_AdditionsList != null) && (_AdditionsList.size() > 0)) {
			for (int i=0; i < _AdditionsList.size(); i++) {
				additions added = _AdditionsList.get(i);
				if (added != null) {
					BigDecimal addPrice = added.getAddedPrice();
					if (addPrice != null)
						bd = bd.add(added.getAddedPrice());
				}
			}
		}
		return bd;
	}
	
	//get a comma delimited list of all addition names to the menu item
	public String getAdditionsNames() {
		String names = "";
		if ((_AdditionsList != null) && (_AdditionsList.size() > 0)) {
			for (int i=0; i < _AdditionsList.size(); i++) {
				additions added = _AdditionsList.get(i);
				if (added != null) {
					String addedname = added.getAddedName();
					if ((addedname != null) && (addedname.trim() != "")) {
						if (names == "")
							names = addedname;
						else
							names += ", " + addedname;
					}
				}
			}
		}
		return names;
	}
	
	//get a comma delimited list of all addition ids to the menu item
	public String getAdditionsIds() {
		String ids = "";
		if ((_AdditionsList != null) && (_AdditionsList.size() > 0)) {
			for (int i=0; i < _AdditionsList.size(); i++) {
				additions added = _AdditionsList.get(i);
				if (added != null) {
					int addedid = added.getAdditionsID();
				
					if (ids == "")
						ids = "" + addedid;
					else
						ids += "," + addedid;
				}
			}
		}
		return ids;
	}
	
	// get the total price of the menu item and all its additions
	public BigDecimal getTotalPrice() {
		BigDecimal totprice = BigDecimal.ZERO;
		if (_ItemPrice != null) {
			totprice = _ItemPrice;
		}
		totprice = totprice.add(getAddedPrice());
		return totprice;
	}
	
}