package com.tonyspizza.view;
//Window rendering to display a list of employees

import javax.swing.*;

import java.awt.*;
import javax.swing.table.*;
import com.tonyspizza.model.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeListWindow extends JDialog {
	public EmployeeListWindow() {
	}
	
	private JFrame frame;
	private JTable table;
	
	/**
	 * Create the Employee Window
	 */
	public EmployeeListWindow(Connection conn) {
		if (conn == null) dispose();
	    
		setBounds(130, 130, 1300, 900);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Employees");
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<employee> lstEmployees = new ArrayList<employee>();
    
			try {
			    System.err.println ("conn not null");
	
			    // select all the employees for the listing
			    String sql = "select staffid, firstname, lastname, middleinitial, address, phonenum, sex, ssn, workinghours, salary, "
			    		+ " isactive, iscook, isdeliveryperson, ishost, ismanager, iswaiter, birthdate "
			    		+ "from tonyspizza.employee "
			    		+ "order by lastname, firstname, middleinitial ";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
			    	int staffid = rs.getInt("staffid");
			    	String firstname = rs.getString("firstname");
			    	String lastname = rs.getString("lastname");
			    	String middleinitial = rs.getString("middleinitial");
			    	String address = rs.getString("address");
			    	String phonenum = rs.getString("phonenum");
			    	String sex = rs.getString("sex");
			    	int ssn = rs.getInt("ssn");
			    	String workinghours = rs.getString("workinghours");
			    	Date birthdate = rs.getDate("birthdate");
			    	boolean isactive = rs.getBoolean("isactive");
			    	boolean isdeliveryperson = rs.getBoolean("isdeliveryperson");
			    	boolean iscook = rs.getBoolean("iscook");
			    	boolean iswaiter = rs.getBoolean("iswaiter");
			    	boolean ishost = rs.getBoolean("ishost");
			    	boolean ismanager = rs.getBoolean("ismanager");
			    	BigDecimal salary = rs.getBigDecimal("salary");
			    	
			    	//assign it to employee model
			    	employee emp = new employee(staffid, ssn, firstname, lastname, middleinitial, address,
			    			phonenum, salary, workinghours, isdeliveryperson, iscook, iswaiter, ishost, ismanager,
			    			birthdate, sex, isactive);
			    	lstEmployees.add(emp);
				}
				rs.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}			
			employee curremp = null;
			
			// add header of the table
			String columns[] = new String[] { "ID", "First Name", "Last Name", "MI", "Address",
					  "Phone No", "Sex", "SSN", "Working Hours", "Birth Date", "Salary", "Active", "Delivery", "Cook",
					  "Waiter", "Host", "Mgr"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class, String.class, String.class, 
	                Integer.class, String.class, Integer.class, String.class, String.class, BigDecimal.class, Boolean.class, Boolean.class, Boolean.class,
	                Boolean.class, Boolean.class, Boolean.class
	        };
	        Object[][] data = new Object[1000][17];
			data = Arrays.copyOf(data, lstEmployees.size());
			for (int i=0; i < lstEmployees.size(); i++) {
		    	 curremp = lstEmployees.get(i);
		    	 if (curremp != null) {
					 data[i][0] = curremp.getStaffID();
					 data[i][1] = curremp.getFirstName();
					 data[i][2] = curremp.getLastName();
					 data[i][3] = curremp.getMiddleInitial();
					 data[i][4] = curremp.getAddress();
					 data[i][5] = curremp.getPhoneNum();
					 data[i][6] = curremp.getSex();
					 data[i][7] = curremp.getSSN();
					 data[i][8] = curremp.getWorkingHours();
					 data[i][9] = new SimpleDateFormat("MM/dd/yyyy").format(curremp.getBirthDate()); 
					 data[i][10] = curremp.getSalary(); 
					 data[i][11] = curremp.getisActive();
					 data[i][12] = curremp.getisDeliveryPerson();
					 data[i][13] = curremp.getisCook();
					 data[i][14] = curremp.getisWaiter();
					 data[i][15] = curremp.getisHost();
					 data[i][16] = curremp.getisManager();
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
			table = new JTable(model);
			table.setBounds(173, 50, 1250, 900);
			
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(88);
		    table.getColumnModel().getColumn(2).setPreferredWidth(88);
		    table.getColumnModel().getColumn(3).setPreferredWidth(13);
		    table.getColumnModel().getColumn(4).setPreferredWidth(250);
		    table.getColumnModel().getColumn(5).setPreferredWidth(80);
		    table.getColumnModel().getColumn(6).setPreferredWidth(28);
		    table.getColumnModel().getColumn(7).setPreferredWidth(90);		     
		    table.getColumnModel().getColumn(8).setPreferredWidth(110);		     
		    table.getColumnModel().getColumn(9).setPreferredWidth(80);		     
		    table.getColumnModel().getColumn(10).setPreferredWidth(65);		     
		    table.getColumnModel().getColumn(11).setPreferredWidth(40);		     
		    table.getColumnModel().getColumn(12).setPreferredWidth(50);		     
		    table.getColumnModel().getColumn(13).setPreferredWidth(40);		     
		    table.getColumnModel().getColumn(14).setPreferredWidth(45);		     
		    table.getColumnModel().getColumn(15).setPreferredWidth(40);		     
		    table.getColumnModel().getColumn(16).setPreferredWidth(40);		     

			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 50, 1250, 700);
	        panel.add(scrollPane);
		
			JButton btnAddEmp = new JButton("Add Employee");
			btnAddEmp.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnAddEmp.setBounds(24, 11, 123, 30);
			btnAddEmp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmployeeWindow addempwin = new EmployeeWindow(0, conn, table, -1);
					addempwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAddEmp);

			JButton btnEditEmp = new JButton("Edit Employee");
			btnEditEmp.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnEditEmp.setBounds(154, 11, 123, 30);
			btnEditEmp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							EmployeeWindow editorderwin = new EmployeeWindow(idvalue, conn, table, row);
							editorderwin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select an employee in the table.");
				}
			});
			panel.add(btnEditEmp);
			
			JButton btnDeliveries = new JButton("Deliveries");
			btnDeliveries.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnDeliveries.setBounds(284, 11, 123, 30);
			btnDeliveries.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							DeliveryDetailsListWindow deliverylistwin = new DeliveryDetailsListWindow(conn, idvalue);
							deliverylistwin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select an employee in the table.");
				}
			});
			panel.add(btnDeliveries);
			
			JButton btnClose = new JButton("Close");
			btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnClose.setBounds(427, 11, 123, 30);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			panel.add(btnClose);
			
	    }
}
