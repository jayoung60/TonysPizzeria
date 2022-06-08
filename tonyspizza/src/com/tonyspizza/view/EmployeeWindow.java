package com.tonyspizza.view;
//Window rendering of a specific Employee 
import com.tonyspizza.model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class EmployeeWindow extends JDialog {

	private JPanel contentPane;
	private JTextField tfFirstName;
	private JTextField tfLastName;
	private JTextField tfMiddleInit;
	private JTextField tfAddress;
	private JTextField tfPhoneNum;
	private JTextField tfSSN;
	private JTextField tfWorkingHours;
	private JTextField tfBirthDate;
	
	public employee winemp;
	private JTextField tfSalary;

	/**
	 * Create the frame.
	 */
	public EmployeeWindow(int id, Connection conn, JTable tbl, int tblrow) {

		employee winemp = new employee();
	    if (conn != null) {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setBounds(100, 100, 649, 507);
			try {
				if (id > 0) { // It is an edit, load the specific employee data
				    String sql = "select staffid, firstname, lastname, middleinitial, address, phonenum, birthdate, "
				    		+ "salary, sex, ssn, workinghours, isactive, iscook, ishost, ismanager, iswaiter, isdeliveryperson "
				    		+ "from tonyspizza.employee "
				    		+ "where staffid = " + id;
				    PreparedStatement p = conn.prepareStatement(sql);
				    ResultSet rs = p.executeQuery();
		
					if (rs.next()) {
						winemp.setStaffID(rs.getInt("staffid"));
						winemp.setFirstName(rs.getString("firstname"));
						winemp.setLastName(rs.getString("lastname"));
						winemp.setMiddleInitial(rs.getString("middleinitial"));
						winemp.setAddress(rs.getString("address"));
						winemp.setBirthDate(rs.getDate("birthdate"));
						winemp.setSalary(rs.getBigDecimal("salary"));
						winemp.setSex(rs.getString("sex"));
						winemp.setSSN(rs.getInt("ssn"));
						winemp.setPhoneNum(rs.getString("phonenum"));
						winemp.setWorkingHours(rs.getString("workinghours"));
						winemp.setisActive(rs.getBoolean("isactive"));
						winemp.setisCook(rs.getBoolean("iscook"));
						winemp.setisHost(rs.getBoolean("ishost"));
						winemp.setisManager(rs.getBoolean("ismanager"));
						winemp.setisWaiter(rs.getBoolean("iswaiter"));
						winemp.setisDeliveryPerson(rs.getBoolean("isdeliveryperson"));
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
		
		if (id <= 0) {
			JLabel lblHeaderAddEmp = new JLabel("Add Employee");
			lblHeaderAddEmp.setBounds(5, 5, 623, 20);
			lblHeaderAddEmp.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddEmp);
		}
		else {
			JLabel lblHeaderAddEmp = new JLabel("Edit Employee: " + id);
			lblHeaderAddEmp.setBounds(5, 5, 623, 20);
			lblHeaderAddEmp.setFont(new Font("Tahoma", Font.BOLD, 16));
			contentPane.add(lblHeaderAddEmp);
		}
		
		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFirstName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFirstName.setBounds(15, 36, 91, 20);
		contentPane.add(lblFirstName);
		
		tfFirstName = new JTextField();
		tfFirstName.setBounds(116, 36, 182, 20);
		tfFirstName.setText(winemp.getFirstName());
		contentPane.add(tfFirstName);
		tfFirstName.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLastName.setBounds(15, 67, 91, 20);
		contentPane.add(lblLastName);
		
		tfLastName = new JTextField();
		tfLastName.setBounds(116, 67, 182, 20);
		tfLastName.setText(winemp.getLastName());
		contentPane.add(tfLastName);
		tfLastName.setColumns(10);
		
		JLabel lblMiddleInit = new JLabel("Middle Init");
		lblMiddleInit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMiddleInit.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMiddleInit.setBounds(15, 96, 91, 20);
		contentPane.add(lblMiddleInit);
		
		tfMiddleInit = new JTextField();
		tfMiddleInit.setBounds(116, 98, 31, 20);
		tfMiddleInit.setText(winemp.getMiddleInitial());
		contentPane.add(tfMiddleInit);
		tfMiddleInit.setColumns(10);
		
		JLabel lblSex = new JLabel("Sex");
		lblSex.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSex.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSex.setBounds(5, 130, 101, 20);
		contentPane.add(lblSex);
		
		JComboBox cbSex = new JComboBox();
		String[] empsex = { "M", "F" };
		cbSex.setModel(new DefaultComboBoxModel(empsex));
		String strSex = winemp.getSex();
		if (strSex.equals("F")) {
			cbSex.setSelectedIndex(1);
		}
		else
			cbSex.setSelectedIndex(0);
		cbSex.setBounds(116, 129, 49, 22);
		contentPane.add(cbSex);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAddress.setBounds(15, 165, 89, 20);
		contentPane.add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setBounds(116, 164, 293, 20);
		tfAddress.setText(winemp.getAddress());
		contentPane.add(tfAddress);
		tfAddress.setColumns(50);

		JLabel lblPhoneNum = new JLabel("Phone No");
		lblPhoneNum.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhoneNum.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPhoneNum.setBounds(17, 196, 89, 20);
		contentPane.add(lblPhoneNum);
		
		tfPhoneNum = new JTextField();
		tfPhoneNum.setBounds(116, 195, 182, 20);
		tfPhoneNum.setText(winemp.getPhoneNum());
		contentPane.add(tfPhoneNum);
		tfPhoneNum.setColumns(50);
		
		JLabel lblSSN = new JLabel("SSN");
		lblSSN.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSSN.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSSN.setBounds(18, 227, 89, 20);
		contentPane.add(lblSSN);
		
		tfSSN = new JTextField();
		tfSSN.setBounds(117, 225, 182, 20);
		if (winemp.getSSN() == 0)
			tfSSN.setText("");
		else
			tfSSN.setText(String.valueOf(winemp.getSSN()));
		contentPane.add(tfSSN);
		tfSSN.setColumns(50);
     
		JLabel lblWorkingHours = new JLabel("Working Hours");
		lblWorkingHours.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWorkingHours.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWorkingHours.setBounds(17, 258, 89, 20);
		contentPane.add(lblWorkingHours);
		
		tfWorkingHours = new JTextField();
		tfWorkingHours.setBounds(116, 256, 182, 20);
		tfWorkingHours.setText(winemp.getWorkingHours());
		contentPane.add(tfWorkingHours);
		tfWorkingHours.setColumns(50);
		
		JLabel lblBirthDate = new JLabel("Birth Date");
		lblBirthDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBirthDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBirthDate.setBounds(15, 289, 89, 20);
		contentPane.add(lblBirthDate);
		
		tfBirthDate = new JTextField();
		tfBirthDate.setBounds(116, 289, 101, 20);
		if (winemp.getBirthDate() == null)
			tfBirthDate.setText("");
		else
			tfBirthDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(winemp.getBirthDate()));
		contentPane.add(tfBirthDate);
		tfBirthDate.setColumns(50);
		
		JCheckBox chbxIsActive = new JCheckBox("Is Active");
		if (winemp.getisActive())
			chbxIsActive.setSelected(true);
		else
			chbxIsActive.setSelected(false);
		chbxIsActive.setBounds(15, 360, 88, 23);
		contentPane.add(chbxIsActive);
		
		JCheckBox chbxIsCook = new JCheckBox("Is Cook");
		if (winemp.getisCook())
			chbxIsCook.setSelected(true);
		else
			chbxIsCook.setSelected(false);
		chbxIsCook.setBounds(158, 361, 113, 23);
		contentPane.add(chbxIsCook);
		
		JCheckBox chbxIsWaiter = new JCheckBox("Is Waiter");
		if (winemp.getisWaiter())
			chbxIsWaiter.setSelected(true);
		else
			chbxIsWaiter.setSelected(false);
		chbxIsWaiter.setBounds(308, 361, 133, 23);
		contentPane.add(chbxIsWaiter);
		
		JCheckBox chbxIsHost = new JCheckBox("Is Host");
		if (winemp.getisHost())
			chbxIsHost.setSelected(true);
		else
			chbxIsHost.setSelected(false);
		chbxIsHost.setBounds(15, 383, 101, 23);
		contentPane.add(chbxIsHost);
		
		JCheckBox chbxIsDelivery = new JCheckBox("Is Delivery");
		if (winemp.getisDeliveryPerson())
			chbxIsDelivery.setSelected(true);
		else
			chbxIsDelivery.setSelected(false);
		chbxIsDelivery.setBounds(158, 384, 113, 23);
		contentPane.add(chbxIsDelivery);
		
		JCheckBox chbxIsManager = new JCheckBox("Is Manager");
		if (winemp.getisManager())
			chbxIsManager.setSelected(true);
		else
			chbxIsManager.setSelected(false);
		chbxIsManager.setBounds(308, 385, 139, 23);
		contentPane.add(chbxIsManager);
		
		JLabel lblNewLabel = new JLabel("(Must be Number)");
		lblNewLabel.setBounds(308, 197, 148, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("(Must be Number)");
		lblNewLabel_1.setBounds(309, 230, 148, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("(MM/DD/YYYY)");
		lblNewLabel_1_1.setBounds(227, 292, 148, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalary.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSalary.setBounds(15, 320, 89, 20);
		contentPane.add(lblSalary);
		
		tfSalary = new JTextField(String.valueOf(winemp.getSalary()));
		tfSalary.setColumns(50);
		tfSalary.setBounds(116, 320, 101, 20);
		contentPane.add(tfSalary);
		
		JLabel lblNewLabel_1_2 = new JLabel("(Must be Number)");
		lblNewLabel_1_2.setBounds(227, 323, 148, 14);
		contentPane.add(lblNewLabel_1_2);		
		//render Save button and its event to save employee
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String sqlUpdate = "";
				if (id == 0)
				    sqlUpdate = "insert into tonyspizza.employee(firstname, lastname, middleinitial, sex, address, "
				    + "phonenum, workinghours, ssn, birthdate, salary, isactive, iscook, iswaiter, ishost, isdeliveryperson, ismanager ) "
				    + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
				else
					sqlUpdate = "update tonyspizza.employee set firstname = ?, lastname = ?, middleinitial = ?, sex = ?, address = ?, phonenum = ?, "
					+ "workinghours = ?, ssn = ?, birthdate = ?, salary = ?, isactive = ?, iscook = ?, iswaiter = ?,"
					+ "ishost = ?, isdeliveryperson = ?, ismanager = ? where staffid = ?";
				try {
					PreparedStatement pUpdate = conn.prepareStatement(sqlUpdate);
					pUpdate.setString(1, tfFirstName.getText());
					pUpdate.setString(2, tfLastName.getText());
					pUpdate.setString(3, tfMiddleInit.getText());
					pUpdate.setString(4, (String)cbSex.getSelectedItem());
					pUpdate.setString(5, tfAddress.getText());
					pUpdate.setInt(6, Integer.valueOf(tfPhoneNum.getText()));
					pUpdate.setString(7, tfWorkingHours.getText());
					pUpdate.setInt(8, Integer.valueOf(tfSSN.getText()));
					try {
						java.util.Date bdt = new SimpleDateFormat("dd/MM/yyyy").parse(tfBirthDate.getText());
						java.sql.Date sbdt = new java.sql.Date(bdt.getTime());
						pUpdate.setDate(9, sbdt);
					}
					catch(Exception ebdt) {
						pUpdate.setDate(9, null);
					}
					String bdfinstr = "0.00";
					BigDecimal pricebd = BigDecimal.ZERO;
					try {
						pricebd = BigDecimal.valueOf(Double.valueOf(tfSalary.getText()));
						bdfinstr = tfSalary.getText();
					}
					catch(Exception ebd) {}
					pUpdate.setBigDecimal(10, pricebd);
					
					
					if (chbxIsActive.isSelected())
						pUpdate.setBoolean(11, true);
					else 
						pUpdate.setBoolean(11,  false);
					
					if (chbxIsCook.isSelected())
						pUpdate.setBoolean(12, true);
					else 
						pUpdate.setBoolean(12,  false);
					
					if (chbxIsWaiter.isSelected())
						pUpdate.setBoolean(13, true);
					else 
						pUpdate.setBoolean(13,  false);
					
					if (chbxIsHost.isSelected())
						pUpdate.setBoolean(14, true);
					else 
						pUpdate.setBoolean(14,  false);
					
					if (chbxIsDelivery.isSelected())
						pUpdate.setBoolean(15, true);
					else 
						pUpdate.setBoolean(15,  false);
					
					if (chbxIsManager.isSelected())
						pUpdate.setBoolean(16, true);
					else 
						pUpdate.setBoolean(16,  false);
					
					if (id > 0)
						pUpdate.setInt(17, winemp.getStaffID());
					pUpdate.executeUpdate();
					int empid = id;
				    if (id == 0) {
						String justaddedSQL = "select max(staffid) from tonyspizza.employee";
						PreparedStatement pJustAdded = conn.prepareStatement(justaddedSQL);
						ResultSet rsJustAdded = pJustAdded.executeQuery();
						if (rsJustAdded.next()) {
						   empid = rsJustAdded.getInt(1);
						}
				    }
				    
				    boolean isactive = true;
				    if (!chbxIsActive.isSelected())
				    	isactive = false;
				    
				    boolean isdelivery = false;
				    if (chbxIsDelivery.isSelected())
				    	isdelivery = true;
				    
				    boolean iscook = false;
				    if (chbxIsCook.isSelected())
				    	iscook = true;
				    
				    boolean iswaiter = false;
				    if (chbxIsWaiter.isSelected())
				    	iswaiter = true;
				    
				    boolean ishost = false;
				    if (chbxIsHost.isSelected())
				    	ishost = true;
				    
				    boolean ismanager = false;
				    if (chbxIsManager.isSelected())
				    	ismanager = true;
				    
					//update parent window table data
					if (tblrow < 0) {
						 Object[] newrowdata = {empid, tfFirstName.getText(), tfLastName.getText(), tfMiddleInit.getText(),
								 tfAddress.getText(), tfPhoneNum.getText(), (String)cbSex.getSelectedItem(), tfSSN.getText(),
								 tfWorkingHours.getText(), tfBirthDate.getText(), tfSalary.getText(), isactive, isdelivery, iscook,
								 iswaiter, ishost, ismanager
								 };
						((DefaultTableModel)tbl.getModel()).addRow(newrowdata);
					}
					else {
						tbl.setValueAt(empid, tblrow, 0);
						tbl.setValueAt(tfFirstName.getText(), tblrow, 1);
						tbl.setValueAt(tfLastName.getText(), tblrow, 2);
						tbl.setValueAt(tfMiddleInit.getText(), tblrow, 3);
						tbl.setValueAt(tfAddress.getText(), tblrow, 4);
						tbl.setValueAt(tfPhoneNum.getText(), tblrow, 5);
						tbl.setValueAt((String)cbSex.getSelectedItem(), tblrow, 6);
						tbl.setValueAt(tfSSN.getText(), tblrow, 7);
						tbl.setValueAt(tfWorkingHours.getText(), tblrow, 8);
						tbl.setValueAt(tfBirthDate.getText(), tblrow, 9);
						tbl.setValueAt(tfSalary.getText(), tblrow, 10);
						tbl.setValueAt(isactive, tblrow, 11);
						tbl.setValueAt(isdelivery, tblrow, 12);
						tbl.setValueAt(iscook, tblrow, 13);
						tbl.setValueAt(iswaiter, tblrow, 14);
						tbl.setValueAt(ishost, tblrow, 15);
						tbl.setValueAt(ismanager, tblrow, 16);
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
		btnSave.setBounds(17, 417, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(140, 417, 89, 23);
		contentPane.add(btnCancel);
		

		

	}
}
