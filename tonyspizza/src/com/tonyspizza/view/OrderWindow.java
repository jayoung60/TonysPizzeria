package com.tonyspizza.view;
//Window Render to open the Order Item Window

import com.tonyspizza.model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderWindow extends JDialog {

	private JPanel contentPane;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfOrderAddress;
	private JTextField tfPickupTime;
	
	public order winorder;
	private JTable tblMenuItems;
	private boolean isDirtyOrderItems = false;

	/**
	 * Create the frame.
	 */
	
	public OrderWindow(int id, Connection conn, JTable tbl, int tblrow) {

		winorder = new order();
	    List<Integer> ordtypeidlst = new ArrayList<Integer>();
	    List<String> ordtypenamelst = new ArrayList<String>();
	    List<Integer> delivereridlst = new ArrayList<Integer>();
	    List<String> deliverernamelst = new ArrayList<String>();
	    List<Integer> menuitemidlst = new ArrayList<Integer>();
	    List<String> menuitemnamelst = new ArrayList<String>();
	    List<BigDecimal> menuitempricelst = new ArrayList<BigDecimal>();
		List<orderitem> lstOrderItems = new ArrayList<orderitem>();
	    delivereridlst.add(0);
	    deliverernamelst.add("");
	    if (conn != null) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 1049, 907);
			try {
				if (id > 0) { //it is an edit load the specific order data
				    String sql = "select orderid, orderdate, o.firstname, o.lastname, o.ordertype, o.timeofpickup, ot.ordertypename, orderaddress, timeofpickup,"
				    		+ "o.delivererstaffid, e.firstname as delfirstname, e.lastname as dellastname, e.middleinitial as delmiddleinit "
				    		+ "from tonyspizza.order o "
				    		+ "inner join tonyspizza.ordertype ot on o.ordertype = ot.ordertypeid "
				    		+ "left outer join tonyspizza.employee e on o.delivererstaffid = e.staffid "
				    		+ "where o.orderid = " + id;
				    PreparedStatement p = conn.prepareStatement(sql);
				    ResultSet rs = p.executeQuery();
		
					if (rs.next()) {
						winorder.setOrderID(rs.getInt("orderid"));
						winorder.setFirstName(rs.getString("firstname"));
						winorder.setLastName(rs.getString("lastname"));
						winorder.setOrderDate(rs.getTimestamp("orderdate"));
						winorder.setOrderTypeID(rs.getInt("ordertype"));
						winorder.setOrderType(rs.getString("ordertypename"));
						winorder.setOrderAddress(rs.getString("orderaddress"));
						winorder.setDelivererStaffID(rs.getInt("delivererstaffid"));
						winorder.setTimeOfPickup(rs.getString("timeofpickup"));
				    	
				    	String fname = rs.getString("delfirstname");
				    	String lname = rs.getString("dellastname");
				    	String middleinit = rs.getString("delmiddleinit");
				    	String delname = "";
				    	if (fname != null)
				    		delname = fname;
				    	if (middleinit != null)
				    		delname += " " + middleinit;
				    	if (lname != null)
				    		delname += " " + lname;
				    	delname.trim();
				    	winorder.setDelivererStaffName(delname);
				    	winorder.setTimeOfPickup(rs.getString("timeofpickup"));
				    }
					rs.close();
					p.close();
					
					//load all the menu items for the order and its associated additions
					String misql = "select oi.orderitemid, oi.menuitemid, mi.itemname, mi.price, adds.additionsid, adds.addedname, adds.addedprice "
						    		+ "from tonyspizza.orderitems oi "
						    		+ "left outer join tonyspizza.menuitemadditions miadds on miadds.orderitemid = oi.orderitemid "
						    		+ "inner join tonyspizza.menuitem mi on mi.menuitemid = oi.menuitemid "
						    		+ "left outer join tonyspizza.additions adds on adds.additionsid = miadds.additionsid "
						    		+ "where oi.orderid = " + id;
					PreparedStatement pordmi = conn.prepareStatement(misql);
					ResultSet rsordmi = pordmi.executeQuery();
					BigDecimal totItemPrice = BigDecimal.ZERO;
					int lastorderitemid = -1;
					List<additions> lstAdditions = new ArrayList<additions>();
					orderitem oi = new orderitem();
					while (rsordmi.next()) {
						int orderitemid = rsordmi.getInt("orderitemid");
						System.out.println(orderitemid);
						String menuitemname = rsordmi.getString("itemname");
						int menuitemid = rsordmi.getInt("menuitemid");
						BigDecimal menuitemprice = rsordmi.getBigDecimal("price");
						if (lastorderitemid != orderitemid) {
							if (lastorderitemid >= 0) {
								oi.setAdditionsList(lstAdditions);
								lstOrderItems.add(oi);
								lstAdditions = new ArrayList<additions>();
							}
							oi = new orderitem(orderitemid, menuitemid, menuitemname, menuitemprice);
							lastorderitemid = orderitemid;	
						}
						String addedname = rsordmi.getString("addedname");
						int addedid = rsordmi.getInt("additionsid");
						BigDecimal addedprice = rsordmi.getBigDecimal("addedprice");
						additions addition = new additions(addedid, addedname, addedprice);
						lstAdditions.add(addition);
					}
					if (lastorderitemid >= 0) {
						oi.setAdditionsList(lstAdditions);
						lstOrderItems.add(oi);
					}
					rsordmi.close();
					pordmi.close();
				}

				//get ordertype lookup vals
				String sql = "select ordertypeid, ordertypename from tonyspizza.ordertype order by ordertypename ";
				PreparedStatement p2 = conn.prepareStatement(sql);
				ResultSet rs = p2.executeQuery(sql);
				while (rs.next()) {
					int ordtypeid = rs.getInt("ordertypeid");
					String ordtypename = rs.getString("ordertypename");
					ordtypeidlst.add(ordtypeid);
					ordtypenamelst.add(ordtypename);
				}
				rs.close();
				
				//get Deliverer Staff lookup (employee table)
				String sqlEmployee = "select staffid, firstname, lastname, middleinitial from tonyspizza.employee "
						+ " where isActive=1 and isDeliveryPerson = 1 order by lastname, firstname, middleinitial";
				PreparedStatement pEmployee = conn.prepareStatement(sqlEmployee);
				ResultSet rsEmployee = pEmployee.executeQuery(sqlEmployee);
				while (rsEmployee.next()) {
					Integer staffid = rsEmployee.getInt("staffid");
					String firstname = rsEmployee.getString("firstname");
					String lastname = rsEmployee.getString("lastname");
					String middleinit = rsEmployee.getString("middleinitial");

			    	String fullname = "";
			    	if (firstname != null)
			    		fullname = firstname;
			    	if (middleinit != null)
			    		fullname += " " + middleinit;
			    	if (lastname != null)
			    		fullname += " " + lastname;					
					delivereridlst.add(staffid);
					deliverernamelst.add(fullname);
				}
				rsEmployee.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		if (id > 0) {
			JLabel lblHeaderAddOrder = new JLabel("Edit Order ID: " + id);
			lblHeaderAddOrder.setBounds(5, 5, 623, 20);
			lblHeaderAddOrder.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddOrder);
		}
		else {
			JLabel lblHeaderAddOrder = new JLabel("Add Order");
			lblHeaderAddOrder.setBounds(5, 5, 623, 20);
			lblHeaderAddOrder.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddOrder);
		}
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFirstName.setBounds(15, 56, 127, 20);
		contentPane.add(lblFirstName);
		
		tfFirstName = new JTextField();
		tfFirstName.setBounds(152, 58, 182, 20);
		tfFirstName.setText(winorder.getFirstName());
		contentPane.add(tfFirstName);
		tfFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLastName.setBounds(15, 87, 127, 20);
		contentPane.add(lblLastName);
		
		tfLastName = new JTextField();
		tfLastName.setBounds(152, 89, 182, 20);
		tfLastName.setText(winorder.getLastName());
		contentPane.add(tfLastName);
		tfLastName.setColumns(10);
		
		JLabel lblOrderType = new JLabel("Order Type");
		lblOrderType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrderType.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOrderType.setBounds(15, 120, 127, 20);
		contentPane.add(lblOrderType);
		
		JComboBox cbOrderType = new JComboBox();
		String ordtypenameitems[] = ordtypenamelst.toArray(new String[0]);
		cbOrderType.setModel(new DefaultComboBoxModel(ordtypenameitems));
		int otindex = ordtypeidlst.indexOf(winorder.getOrderTypeID());
		
		if (otindex < 0)
			cbOrderType.setSelectedIndex(1);
		else
			cbOrderType.setSelectedIndex(otindex);
		cbOrderType.setBounds(152, 121, 182, 22);
		contentPane.add(cbOrderType);

		JLabel lblDeliverer = new JLabel("Deliverer");
		lblDeliverer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDeliverer.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDeliverer.setBounds(15, 152, 127, 20);
		contentPane.add(lblDeliverer);
		
		JComboBox cbDeliverer = new JComboBox();
		String deliverernameitems[] = deliverernamelst.toArray(new String[0]);
		cbDeliverer.setModel(new DefaultComboBoxModel(deliverernameitems));
		int delindex = delivereridlst.indexOf(winorder.getDelivererStaffID());
		
		if (delindex < 0)
			cbDeliverer.setSelectedIndex(0);
		else
		    cbDeliverer.setSelectedIndex(delindex);
		cbDeliverer.setBounds(152, 154, 182, 22);
		contentPane.add(cbDeliverer);	
		
		JLabel lblOrderAddress = new JLabel("Order Address");
		lblOrderAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOrderAddress.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblOrderAddress.setBounds(15, 185, 127, 20);
		contentPane.add(lblOrderAddress);
		
		tfOrderAddress = new JTextField();
		tfOrderAddress.setBounds(152, 187, 405, 20);
		tfOrderAddress.setText(winorder.getOrderAddress());
		contentPane.add(tfOrderAddress);
		tfOrderAddress.setColumns(50);

		JLabel lblPickupTime = new JLabel("Time of Pick Up");
		lblPickupTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPickupTime.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPickupTime.setBounds(17, 217, 125, 20);
		contentPane.add(lblPickupTime);
		
		tfPickupTime = new JTextField();
		tfPickupTime.setBounds(152, 217, 182, 20);
		tfPickupTime.setText(winorder.getTimeOfPickup());
		contentPane.add(tfPickupTime);
		tfPickupTime.setColumns(50);
     
		JLabel lblLabelTotalCost = new JLabel("Total Cost:");
		lblLabelTotalCost.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLabelTotalCost.setBounds(53, 673, 89, 14);
		contentPane.add(lblLabelTotalCost);
		
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        
        BigDecimal bdTotal = new BigDecimal(0.0);
        JLabel lblTotalCost = new JLabel(currency.format(bdTotal));
		lblTotalCost.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalCost.setBounds(152, 673, 68, 14);
		contentPane.add(lblTotalCost);
		
		JLabel lblLabelTax = new JLabel("Tax:");
		lblLabelTax.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLabelTax.setBounds(53, 691, 89, 14);
		contentPane.add(lblLabelTax);
		
        BigDecimal tax = new BigDecimal("0.06");
          
        BigDecimal salesTax = bdTotal.multiply(tax);
        salesTax = salesTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal finaltotal = bdTotal.add(salesTax);
          
        JLabel lblTax = new JLabel(currency.format(salesTax));
		lblTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTax.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax.setBounds(152, 690, 68, 14);
		contentPane.add(lblTax);
		
		JLabel lblLabelFinalCost = new JLabel("Final Cost:");
		lblLabelFinalCost.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLabelFinalCost.setBounds(53, 710, 89, 14);
		contentPane.add(lblLabelFinalCost);
		
		JLabel lblFinalCost = new JLabel(currency.format(finaltotal));
		lblFinalCost.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFinalCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFinalCost.setBounds(152, 709, 68, 14);
		contentPane.add(lblFinalCost);
		
		//get menuitems
		try {
			String sql = "select menuitemid, itemname, price from tonyspizza.menuitem order by itemname ";
			PreparedStatement pmi = conn.prepareStatement(sql);
			ResultSet rsmi = pmi.executeQuery(sql);
			while (rsmi.next()) {
				int menuitemid = rsmi.getInt("menuitemid");
				String itemname = rsmi.getString("itemname");
				BigDecimal price = rsmi.getBigDecimal("price");
				menuitemidlst.add(menuitemid);
				menuitemnamelst.add(itemname);
				menuitempricelst.add(price);
			}
			rsmi.close();
			pmi.close();
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		JComboBox cbMenuItems = new JComboBox();
		cbMenuItems.setBounds(53, 268, 184, 22);
		String menuitemnameitems[] = menuitemnamelst.toArray(new String[0]);
		cbMenuItems.setModel(new DefaultComboBoxModel(menuitemnameitems));
		contentPane.add(cbMenuItems);
		
		
		// add header of the table
		String columns[] = new String[] { "ID", "Menu Item", "Menu Item Price", "Additions", "Additions Price", "Total Price", "IDs"};
		
        final Class[] columnClass = new Class[] {
                Integer.class, String.class, BigDecimal.class, String.class, BigDecimal.class, BigDecimal.class, String.class
        };
        Object[][] data = new Object[1000][7];
		data = Arrays.copyOf(data, lstOrderItems.size());
		orderitem oi = null;
		bdTotal = BigDecimal.ZERO;
		for (int i=0; i < lstOrderItems.size(); i++) {
	    	 oi = lstOrderItems.get(i);
	    	 if (oi != null) {
	    		 data[i][0] = oi.getMenuItemID();
				 data[i][1] = oi.getItemName();
				 data[i][2] = oi.getItemPrice();
				 data[i][3] = oi.getAdditionsNames();
				 data[i][4] = oi.getAddedPrice();
				 data[i][5] = oi.getTotalPrice();
				 data[i][6] = oi.getAdditionsIds();
				 if (oi.getTotalPrice() != null)
					bdTotal = bdTotal.add(oi.getTotalPrice());
	    	 }
		}

	 
	    //create table model with data
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex)
            {
                return columnClass[columnIndex];
            }
        };
		JTable tblMIItems = new JTable(model);
		tblMIItems.setBounds(53, 300, 879, 362);
		
		tblMIItems.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblMIItems.getColumnModel().getColumn(0).setPreferredWidth(30);
		tblMIItems.getColumnModel().getColumn(1).setPreferredWidth(200);
		tblMIItems.getColumnModel().getColumn(2).setPreferredWidth(100);
		tblMIItems.getColumnModel().getColumn(3).setPreferredWidth(300);
		tblMIItems.getColumnModel().getColumn(4).setPreferredWidth(100);
		tblMIItems.getColumnModel().getColumn(5).setPreferredWidth(100);
		tblMIItems.getColumnModel().getColumn(6).setPreferredWidth(100);
		tblMIItems.getColumnModel().getColumn(6).setMinWidth(0);
		tblMIItems.getColumnModel().getColumn(6).setMaxWidth(0);
		tblMIItems.getColumnModel().getColumn(6).setWidth(0);
		JScrollPane scrollPane = new JScrollPane(tblMIItems);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(53, 300, 879, 362);

		JButton btnAddMenuItem = new JButton("Add Menu Item");
		btnAddMenuItem.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnAddMenuItem.setBounds(237, 267, 139, 24);
		btnAddMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//update parent window table data
				int intindex = cbMenuItems.getSelectedIndex();
				if (intindex >= 0) {
					Object[] newrowdata = {menuitemidlst.get(intindex), menuitemnamelst.get(intindex), menuitempricelst.get(intindex), "", "", menuitempricelst.get(intindex)};
					((DefaultTableModel)tblMIItems.getModel()).addRow(newrowdata);
					isDirtyOrderItems = true;
					recalculateordertotals(lblTotalCost, lblTax, lblFinalCost, tblMIItems, 5);
				}
			}
		});	
		contentPane.add(btnAddMenuItem);
		
		JButton btnRemoveMenuItem = new JButton("Remove Menu Item");
		btnRemoveMenuItem.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnRemoveMenuItem.setBounds(392, 267, 167, 24);
		btnRemoveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//update parent window table data
				int row = tblMIItems.getSelectedRow();
				if (row >= 0) {
					((DefaultTableModel)tblMIItems.getModel()).removeRow(row);
					isDirtyOrderItems = true;
					recalculateordertotals(lblTotalCost, lblTax, lblFinalCost, tblMIItems, 5);
				}
				else 
					JOptionPane.showMessageDialog(null, "You must select a menu item in the table.");
			}
		});	
		contentPane.add(btnRemoveMenuItem);
		
		JButton btnManageAdditions = new JButton("Manage Additions");
		btnManageAdditions.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnManageAdditions.setBounds(567, 267, 167, 24);
		btnManageAdditions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//update parent window table data
				int row = tblMIItems.getSelectedRow();
				if (row >= 0) {
					AdditionsPickWindow addpickwin = new AdditionsPickWindow(conn, (String)tblMIItems.getValueAt(row, 6), id, (String)tblMIItems.getValueAt(row, 1), tblMIItems,
							lblTotalCost, lblTax, lblFinalCost);
					addpickwin.setVisible(true);
					isDirtyOrderItems = true;
				}
				else 
					JOptionPane.showMessageDialog(null, "You must select a menu item in the table.");
			}
		});	
		contentPane.add(btnManageAdditions);
		

		
		contentPane.add(scrollPane);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String sqlUpdate = "";
				boolean isAdd = false;
				if (id == 0) {
				    sqlUpdate = "insert into tonyspizza.order(firstname, lastname, ordertype, orderaddress, delivererstaffid, orderdate, timeofpickup) values(?, ?, ?, ?, ?, now(), ?) ";
				    isAdd = true;
				}
				else
					sqlUpdate = "update tonyspizza.order set firstname = ?, lastname = ?, ordertype = ?, orderaddress = ?, delivererstaffid = ?, timeofpickup = ? where orderid = ?";
				System.out.println(sqlUpdate);
				try {
					PreparedStatement pUpdate = conn.prepareStatement(sqlUpdate);
					pUpdate.setString(1, tfFirstName.getText());
					pUpdate.setString(2, tfLastName.getText());
					
					int selOrderTypeItem = cbOrderType.getSelectedIndex();
					System.out.println(ordtypeidlst);
					System.out.println("Selected " + selOrderTypeItem + " ordlst: " + ordtypeidlst.get(selOrderTypeItem));
					if (selOrderTypeItem >= 0) 
						pUpdate.setInt(3, ordtypeidlst.get(selOrderTypeItem));
					else 
						pUpdate.setInt(3,  1);
					
					pUpdate.setString(4, tfOrderAddress.getText());
					
					int selDelivererItem = cbDeliverer.getSelectedIndex();
					System.out.println("Selected Delete Index: " + selDelivererItem);
					String deliverername = "";
					if (selDelivererItem > 0) {
						pUpdate.setInt(5, delivereridlst.get(selDelivererItem));
						deliverername = deliverernamelst.get(selDelivererItem);
					}
					else 
						pUpdate.setString(5,  null);
					
					pUpdate.setString(6, tfPickupTime.getText());
					
					if (id > 0)
						pUpdate.setInt(7, winorder.getOrderID());
					pUpdate.executeUpdate();
					int orderid = id;
					String orderdtstr = "";
				    if (id == 0) {
						String justaddedSQL = "select max(orderid), max(orderdate) from tonyspizza.order";
						PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
						ResultSet rsJustAdded = pJustAdded.executeQuery();
						if (rsJustAdded.next()) {
						   orderid = rsJustAdded.getInt(1);
						   orderdtstr = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(rsJustAdded.getTimestamp(2));
						}
				    }
				    else 
				    	orderdtstr = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(winorder.getOrderDate());
				    
				    //save all order items if there have been changes
				    if (isDirtyOrderItems)
				    	saveOrderItems(tblMIItems, conn, orderid, isAdd);
				    
					//update parent window table data
					if (tblrow < 0) {
						 Object[] newrowdata = {orderid, tfFirstName.getText(), tfLastName.getText(), orderdtstr,
								 ordtypenamelst.get(selOrderTypeItem), tfOrderAddress.getText(),
								 deliverername, tfPickupTime.getText()};
						((DefaultTableModel)tbl.getModel()).addRow(newrowdata);
					}
					else {
						tbl.setValueAt(orderid, tblrow, 0);
						tbl.setValueAt(tfFirstName.getText(), tblrow, 1);
						tbl.setValueAt(tfLastName.getText(), tblrow, 2);
						tbl.setValueAt(orderdtstr, tblrow, 3);
						tbl.setValueAt(ordtypenamelst.get(selOrderTypeItem), tblrow, 4);
						tbl.setValueAt(tfOrderAddress.getText(), tblrow, 5);
						tbl.setValueAt(deliverername, tblrow, 6);
						tbl.setValueAt(tfPickupTime.getText(), tblrow, 7);
					}
				} catch (SQLException ex) {
				    // handle any errors
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}

				dispose();
			}
		});
		btnSave.setBounds(53, 735, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(170, 735, 89, 23);
		contentPane.add(btnCancel);
		
        lblTotalCost.setText(currency.format(bdTotal));
        salesTax = bdTotal.multiply(tax);
        salesTax = salesTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        finaltotal = bdTotal.add(salesTax);
        lblTax.setText(currency.format(salesTax)); 
		lblFinalCost.setText(currency.format(finaltotal));
	}
	
	//recalculate the below order totals
	private void recalculateordertotals(JLabel lblTotalCost, JLabel lblTax, JLabel lblFinalCost, JTable tbl, int pricepos) {
		BigDecimal bdTotal = BigDecimal.ZERO;
		for (int i=0; i < tbl.getRowCount(); i++) {
			BigDecimal bdItem = (BigDecimal)tbl.getValueAt(i, pricepos);
			bdTotal = bdTotal.add(bdItem);
		}
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        BigDecimal tax = new BigDecimal("0.06");
        
        lblTotalCost.setText(currency.format(bdTotal));
        BigDecimal salesTax = bdTotal.multiply(tax);
        salesTax = salesTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal finaltotal = bdTotal.add(salesTax);
        lblTax.setText(currency.format(salesTax)); 
		lblFinalCost.setText(currency.format(finaltotal));
	}
	
	//function to save the orderitem table data (order menuitems and order menuitem additions)
	private void saveOrderItems(JTable tbl, Connection conn, int orderid, boolean isAdd) {
		try {
			if (!isAdd) { //delete all previous order items and start over with new data
				String sqlDelete = "delete from tonyspizza.orderitems where orderid  = ?";
				PreparedStatement pDelete = conn.prepareStatement(sqlDelete);
				pDelete.setInt(1, orderid);		
				pDelete.executeUpdate();
				pDelete.close();
			}
			String sqlInsert = "insert into orderitems(orderid, menuitemid) values(?, ?)";
			PreparedStatement pInsert = conn.prepareStatement(sqlInsert);
			
			String sqlInsertAdditions = "insert into menuitemadditions(orderitemid, additionsid, addeddatetime) values(?, ?, now())";
			PreparedStatement pInsertAdditions = conn.prepareStatement(sqlInsertAdditions);
			
			for (int i=0; i < tbl.getRowCount(); i++) {
				pInsert.setInt(1, orderid);
				pInsert.setInt(2, (Integer)tbl.getValueAt(i, 0));
				pInsert.executeUpdate();
				
				String adds = (String)tbl.getValueAt(i, 6);
								
				if ((adds != null) && (adds.trim() != "") || (adds != null) && (adds != "0")) {
					String[] addIdArray = adds.split(",");
					//get orderitemid of recently added orderitem
					String justaddedSQL = "select max(orderitemid) from tonyspizza.orderitems";
					PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
					ResultSet rsJustAdded = pJustAdded.executeQuery();
					if (rsJustAdded.next()) {
					   int orderitemid = rsJustAdded.getInt(1);
					   for (int j=0; j < addIdArray.length; j++) {
							String addid = addIdArray[j];
							if (addid != null) {
								pInsertAdditions.setInt(1, orderitemid);
								pInsertAdditions.setInt(2, new Integer(addid).intValue());
								pInsertAdditions.executeUpdate();
							}
						}
					}
					
				}
			}
			pInsertAdditions.close();
			pInsert.close();



		} catch(SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

}
