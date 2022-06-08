package com.tonyspizza.view;
//Window rendering to add/edit a specific Addition lookup item

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

import com.tonyspizza.model.additions;

public class AdditionsWindow extends JDialog {

	private JPanel contentPane;
	private JTextField tfItem;
	private JTextField tfPrice;

	/**
	 * Create the frame.
	 */
	public AdditionsWindow(int id, Connection conn, JTable tbl, int tblrow) {
			if (conn == null) dispose();
			
			additions winadditionitem = new additions();
	    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 649, 507);
			try {
				if (id > 0) {  //it is an edit, load the addition data
				    String sql = "select additionsid, addedname, addedprice "
				    		+ "from tonyspizza.additions "
				    		+ "where additionsid = " + id;
				    PreparedStatement p = conn.prepareStatement(sql);
				    ResultSet rs = p.executeQuery();
		
					if (rs.next()) {
						winadditionitem.setAdditionsID(rs.getInt("additionsid"));
						winadditionitem.setAddedName(rs.getString("addedname"));
						winadditionitem.setAddedPrice(rs.getBigDecimal("addedprice"));
				    }
					rs.close();
				}
				

			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHeaderLabel = new JLabel("Add Addition");
		lblHeaderLabel.setBounds(5, 5, 623, 20);
		lblHeaderLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(lblHeaderLabel);
		
		JLabel lblItem = new JLabel("Addition");
		lblItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItem.setBounds(15, 36, 66, 20);
		contentPane.add(lblItem);
		
		tfItem = new JTextField();
		tfItem.setBounds(91, 36, 182, 20);
		tfItem.setText(winadditionitem.getAddedName());
		contentPane.add(tfItem);
		tfItem.setColumns(10);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrice.setBounds(15, 67, 66, 20);
		contentPane.add(lblPrice);
		
		tfPrice = new JTextField();
		tfPrice.setBounds(91, 67, 182, 20);
		tfPrice.setText(String.valueOf(winadditionitem.getAddedPrice()));
		contentPane.add(tfPrice);
		tfPrice.setColumns(10);
		
		//render save button and its event
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String sqlUpdate = "";
				if (id == 0) //it is an add
				    sqlUpdate = "insert into tonyspizza.additions(addedname, addedprice) values(?, ?) ";
				else //it is an edit
					sqlUpdate = "update tonyspizza.additions set addedname = ?, addedprice = ? where additionsid = ?";
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
					int addid = id;
				    if (id == 0) {
						String justaddedSQL = "select max(additionsid) from tonyspizza.additions";
						PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
						ResultSet rsJustAdded = pJustAdded.executeQuery();
						if (rsJustAdded.next()) {
							addid = rsJustAdded.getInt(1);
						}
				    }
				    
					//update parent window table data
					if (tblrow < 0) {
						 Object[] newrowdata = {addid, tfItem.getText(), bdfinstr};
						((DefaultTableModel)tbl.getModel()).addRow(newrowdata);
					}
					else {
						tbl.setValueAt(addid, tblrow, 0);
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
		lblNewLabel.setBounds(283, 70, 141, 14);
		contentPane.add(lblNewLabel);
	}
}
