package com.tonyspizza.view;
//Window rendering of a specific Menu Item Window
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tonyspizza.model.employee;
import com.tonyspizza.model.menuitem;

public class MenuItemWindow extends JDialog {

	private JPanel contentPane;
	private JTextField tfItem;
	private JTextField tfPrice;

	/**
	 * Create the frame.
	 */
	public MenuItemWindow(int id, Connection conn, JTable tbl, int tblrow) {
		menuitem winmenuitem = new menuitem();
	    if (conn != null) {
	    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 649, 507);
			try {
				if (id > 0) {  //it is an edit, load the specific menu item data
				    String sql = "select menuitemid, itemname, price "
				    		+ "from tonyspizza.menuitem "
				    		+ "where menuitemid = " + id;
				    PreparedStatement p = conn.prepareStatement(sql);
				    ResultSet rs = p.executeQuery();
		
					if (rs.next()) {
						winmenuitem.setMenuItemID(rs.getInt("menuitemid"));
						winmenuitem.setItemName(rs.getString("itemname"));
						winmenuitem.setPrice(rs.getBigDecimal("price"));

				    }
					rs.close();
				}
				

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
			JLabel lblHeaderLabel = new JLabel("Edit Menu Item");
			lblHeaderLabel.setBounds(5, 5, 623, 20);
			lblHeaderLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderLabel);
		}
		else {
			JLabel lblHeaderLabel = new JLabel("Add Menu Item");
			lblHeaderLabel.setBounds(5, 5, 623, 20);
			lblHeaderLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderLabel);
		}
		
		JLabel lblItem = new JLabel("Menu Item");
		lblItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItem.setBounds(15, 36, 66, 20);
		contentPane.add(lblItem);
		
		tfItem = new JTextField();
		tfItem.setBounds(91, 36, 182, 20);
		tfItem.setText(winmenuitem.getItemName());
		contentPane.add(tfItem);
		tfItem.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrice.setBounds(15, 67, 66, 20);
		contentPane.add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.setBounds(91, 67, 182, 20);
		tfPrice.setText(String.valueOf(winmenuitem.getPrice()));
		contentPane.add(tfPrice);
		tfPrice.setColumns(10);
		
		//render Save button and attach Save event
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String sqlUpdate = "";
				if (id == 0)  // this is an addition
				    sqlUpdate = "insert into tonyspizza.menuitem(itemname, price) values(?, ?) ";
				else //this is an update
					sqlUpdate = "update tonyspizza.menuitem set itemname = ?, price = ? where menuitemid = ?";
				try {
					PreparedStatement pUpdate = conn.prepareStatement(sqlUpdate);
					pUpdate.setString(1, tfItem.getText());
					BigDecimal pricebd = BigDecimal.ZERO;
					String bdfinstr = "0.00";
					try {
						pricebd = BigDecimal.valueOf(Double.valueOf(tfPrice.getText()));
						bdfinstr = tfPrice.getText();
					}
					catch(Exception ebd) {}
					pUpdate.setBigDecimal(2, pricebd);
					
					if (id > 0)
						pUpdate.setInt(3, id);
					pUpdate.executeUpdate();
					int mitemid = id;
				    if (id == 0) {
						String justaddedSQL = "select max(menuitemid) from tonyspizza.menuitem";
						PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
						ResultSet rsJustAdded = pJustAdded.executeQuery();
						if (rsJustAdded.next()) {
							mitemid = rsJustAdded.getInt(1);
						}
				    }
				    
					//update parent window table data
					if (tblrow < 0) {
						 Object[] newrowdata = {mitemid, tfItem.getText(), bdfinstr};
						((DefaultTableModel)tbl.getModel()).addRow(newrowdata);
					}
					else {
						tbl.setValueAt(mitemid, tblrow, 0);
						tbl.setValueAt(tfItem.getText(), tblrow, 1);
						tbl.setValueAt(bdfinstr, tblrow, 2);
					}
				} catch (SQLException ex) {
				    // handle any errors
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}

				dispose();			}
		});
		btnSave.setBounds(35, 408, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(158, 408, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel = new JLabel("(Must be numeric price)");
		lblNewLabel.setBounds(283, 70, 156, 14);
		contentPane.add(lblNewLabel);
	}
}
