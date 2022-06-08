package com.tonyspizza.view;
//Window Render for the Catering Request Item Screen

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//Window rendering to add/edit a specific Catering Request item

public class CateringRequestWindow extends JDialog {

	private JPanel contentPane;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfNumGuests;
	private JTextField tfPhonenum;
	private JTextField tfEmail;
	private JTextField tfCaterDate;
	private JTextArea tfCaterDescription;
	public cateringrequest wincr;
	private JTable tblCRtems;

	/**
	 * Create the frame.
	 */
	
	public CateringRequestWindow(int id, Connection conn, JTable tbl, int tblrow) {

		wincr = new cateringrequest();
	    List<Integer> ordtypeidlst = new ArrayList<Integer>();
	    List<String> ordtypenamelst = new ArrayList<String>();
	    
	    if (conn != null) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 849, 707);
			try {
				if (id > 0) {
				    String sql = "select caterid, caterdate, firstname, lastname, caterordertype, ordertypename, email, numguests, phonenum, caterdescription "
				    		+ "from tonyspizza.cateringrequests cr "
				    		+ "inner join tonyspizza.ordertype ot on cr.caterordertype = ot.ordertypeid "
				    		+ "where cr.caterid = " + id + " order by cr.lastname, cr.firstname";
				    PreparedStatement p = conn.prepareStatement(sql);
				    ResultSet rs = p.executeQuery();
		
					if (rs.next()) {
						wincr.setCaterID(rs.getInt("caterid"));
						wincr.setFirstName(rs.getString("firstname"));
						wincr.setLastName(rs.getString("lastname"));
						wincr.setCaterDate(rs.getTimestamp("caterdate"));
						wincr.setCaterOrderTypeID(rs.getInt("caterordertype"));
						wincr.setCaterOrderType(rs.getString("ordertypename"));
						wincr.setEmail(rs.getString("email"));
						wincr.setNumGuests(rs.getInt("numguests"));
						wincr.setPhoneNum(rs.getString("phonenum"));
						wincr.setCaterDescription(rs.getString("caterdescription"));
				    	
				    }
					rs.close();
					p.close();
				}
				//get ordertype lookup vals
				String sql2 = "select ordertypeid, ordertypename from tonyspizza.ordertype order by ordertypename ";
				PreparedStatement p2 = conn.prepareStatement(sql2);
				ResultSet rs2 = p2.executeQuery(sql2);
				while (rs2.next()) {
					int ordtypeid = rs2.getInt("ordertypeid");
					String ordtypename = rs2.getString("ordertypename");
					ordtypeidlst.add(ordtypeid);
					ordtypenamelst.add(ordtypename);
				}
				rs2.close();
				p2.close();
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
			JLabel lblHeaderAddCR = new JLabel("Edit Catering Request: " + id);
			lblHeaderAddCR.setBounds(5, 5, 723, 50);
			lblHeaderAddCR.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddCR);
		}
		else {
			JLabel lblHeaderAddCR = new JLabel("Add Catering Request");
			lblHeaderAddCR.setBounds(5, 5, 723, 50);
			lblHeaderAddCR.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddCR);
		}
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFirstName.setBounds(15, 56, 127, 20);
		contentPane.add(lblFirstName);
		
		tfFirstName = new JTextField();
		tfFirstName.setBounds(152, 58, 182, 20);
		tfFirstName.setText(wincr.getFirstName());
		contentPane.add(tfFirstName);
		tfFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLastName.setBounds(15, 87, 127, 20);
		contentPane.add(lblLastName);
		
		tfLastName = new JTextField();
		tfLastName.setBounds(152, 89, 182, 20);
		tfLastName.setText(wincr.getLastName());
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
		int otindex = ordtypeidlst.indexOf(wincr.getCaterOrderTypeID());
		
		if (otindex < 0)
			cbOrderType.setSelectedIndex(2);
		else
			cbOrderType.setSelectedIndex(otindex);
		cbOrderType.setBounds(152, 121, 182, 22);
		contentPane.add(cbOrderType);

		JLabel lblNumGuests = new JLabel("Num Guests");
		lblNumGuests.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumGuests.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNumGuests.setBounds(15, 152, 127, 20);
		contentPane.add(lblNumGuests);
		
		tfNumGuests = new JTextField();
		tfNumGuests.setBounds(152, 154, 182, 20);
		tfNumGuests.setText(String.valueOf(wincr.getNumGuests()));
		contentPane.add(tfNumGuests);
		tfNumGuests.setColumns(50);	
		
		JLabel lblPhonenum = new JLabel("Phone Num");
		lblPhonenum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhonenum.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPhonenum.setBounds(15, 185, 127, 20);
		contentPane.add(lblPhonenum);
		
		tfPhonenum = new JTextField();
		tfPhonenum.setBounds(152, 187, 182, 20);
		tfPhonenum.setText(String.valueOf(wincr.getPhoneNum()));
		contentPane.add(tfPhonenum);
		tfPhonenum.setColumns(50);

		JLabel lblCaterDescription = new JLabel("Description");
		lblCaterDescription.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaterDescription.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCaterDescription.setBounds(17, 278, 125, 20);
		contentPane.add(lblCaterDescription);
		
		tfCaterDescription = new JTextArea();
		tfCaterDescription.setBounds(152, 278, 308, 133);
		contentPane.add(tfCaterDescription); 
		
		JLabel lblEmail = new JLabel("EMail");
		lblEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmail.setBounds(15, 247, 125, 20);
		contentPane.add(lblEmail);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(152, 249, 182, 20);
		tfEmail.setText(wincr.getEmail());
		contentPane.add(tfEmail);
		tfEmail.setColumns(50);
		
		JLabel lblCaterDate = new JLabel("Cater Date");
		lblCaterDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCaterDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCaterDate.setBounds(17, 216, 125, 20);
		contentPane.add(lblCaterDate);
		
		tfCaterDate = new JTextField();
		tfCaterDate.setBounds(152, 217, 182, 20);
		if (wincr.getCaterDate() == null)
		   tfCaterDate.setText("");
		else 
			tfCaterDate.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm").format(wincr.getCaterDate()));
		tfCaterDate.setColumns(50);
		contentPane.add(tfCaterDate);
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String sqlUpdate = "";
				boolean isAdd = false;
				if (id < 0) {
				    sqlUpdate = "insert into tonyspizza.cateringrequests(firstname, lastname, caterordertype, caterdate, email, numguests, phonenum, caterdescription) values(?, ?, ?, ?, ?, ?, ?, ?) ";
				    isAdd = true;
				}
				else
					sqlUpdate = "update tonyspizza.cateringrequests set firstname = ?, lastname = ?, caterordertype = ?, caterdate = ?, email = ?, " 
					   + "numguests = ?, phonenum = ?, caterdescription = ? where caterid = ?";
				try {
					PreparedStatement pUpdate = conn.prepareStatement(sqlUpdate);
					pUpdate.setString(1, tfFirstName.getText());
					pUpdate.setString(2, tfLastName.getText());
					
					int selOrderTypeItem = cbOrderType.getSelectedIndex();
					if (selOrderTypeItem >= 0) 
						pUpdate.setInt(3, ordtypeidlst.get(selOrderTypeItem));
					else 
						pUpdate.setInt(3,  2);
					
					try {
						java.util.Date cdt = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tfCaterDate.getText());
						java.sql.Timestamp sbdt = new java.sql.Timestamp(cdt.getTime());
						pUpdate.setTimestamp(4, sbdt);
					}
					catch(Exception ebdt) {
						pUpdate.setDate(4, null);
					}					
					pUpdate.setString(5, tfEmail.getText());
					pUpdate.setString(6, tfNumGuests.getText());
					pUpdate.setString(7, tfPhonenum.getText());
					pUpdate.setString(8, tfCaterDescription.getText());

					if (id > 0)
						pUpdate.setInt(9, wincr.getCaterID());
					pUpdate.executeUpdate();
					int caterid = id;
				    if (id < 0) {
						String justaddedSQL = "select max(caterid) from tonyspizza.cateringrequests";
						PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
						ResultSet rsJustAdded = pJustAdded.executeQuery();
						if (rsJustAdded.next()) {
						   caterid = rsJustAdded.getInt(1);
						}
				    }
				    
				    
					//update parent window table data
					if (tblrow < 0) {
						 Object[] newrowdata = {caterid, tfFirstName.getText(), tfLastName.getText(), 
								 ordtypenamelst.get(selOrderTypeItem), tfCaterDate.getText(),
								 tfEmail.getText(), Integer.parseInt(tfNumGuests.getText()), tfPhonenum.getText(), tfCaterDescription.getText()};
						((DefaultTableModel)tbl.getModel()).addRow(newrowdata);
					}
					else {
						tbl.setValueAt(caterid, tblrow, 0);
						tbl.setValueAt(tfFirstName.getText(), tblrow, 1);
						tbl.setValueAt(tfLastName.getText(), tblrow, 2);
						tbl.setValueAt(ordtypenamelst.get(selOrderTypeItem), tblrow, 3);
						tbl.setValueAt(tfCaterDate.getText(), tblrow, 4);
						tbl.setValueAt(tfEmail.getText(), tblrow, 5);
						tbl.setValueAt(Integer.parseInt(tfNumGuests.getText()), tblrow, 6);
						tbl.setValueAt(tfPhonenum.getText(), tblrow, 7);
						tbl.setValueAt(tfCaterDescription.getText(), tblrow, 8);
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
		btnSave.setBounds(61, 470, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(181, 470, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel = new JLabel("(Must be Number)");
		lblNewLabel.setBounds(343, 157, 148, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("(Must be Number)");
		lblNewLabel_1.setBounds(344, 190, 148, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblmmddyyyyHhmm = new JLabel("(MM/DD/YYYY HH:mm)");
		lblmmddyyyyHhmm.setBounds(343, 221, 148, 14);
		contentPane.add(lblmmddyyyyHhmm);
		

	}
}
