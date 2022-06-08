package com.tonyspizza.view;
//Render Window for Delivery Details Listing of Orders for a Particular User
import javax.swing.*;
import javax.swing.border.EmptyBorder;

//Window rendering of Past Orders Screen
import java.awt.*;
import javax.swing.table.*;
import com.tonyspizza.model.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryDetailsListWindow extends JDialog {
	
	private JFrame frame;
	private JPanel contentPane;
	private JTable table;
	
	public DeliveryDetailsListWindow(Connection conn, int staffid) {  
		if (conn == null) dispose();
		setBounds(130, 130, 1150, 1000);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Deliverer Screen Orders for Employee ID: " + staffid);
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);

		
		List<order> lstOrders = new ArrayList<order>();
    
			try {
			    // Load all the orders
			    String sql = "select orderid, orderdate, o.firstname, o.lastname, o.ordertype, ot.ordertypename, orderaddress, o.delivererstaffid, timeofpickup, "
			    		+ " e.lastname as dellastname, e.firstname as delfirstname, e.middleinitial as delmiddleinit, o.ordercompleteddatetime "
			    		+ "from tonyspizza.order o "
			    		+ "inner join tonyspizza.ordertype ot on o.ordertype = ot.ordertypeid " 
			    		+ "left outer join tonyspizza.employee e on o.delivererstaffid = e.staffid "
			    		+ "where o.delivererstaffid = " + staffid
			    		+ " order by o.ordercompleteddatetime, orderdate desc";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
			    	int id = rs.getInt("OrderID");
			    	String firstname = rs.getString("firstname");
			    	String lastname = rs.getString("lastname");
			    	java.sql.Timestamp dt = rs.getTimestamp("orderdate");
			    	int ordertypeid = rs.getInt("ordertype");
			    	String ordertypename = rs.getString("ordertypename");
			    	String orderaddress = rs.getString("orderaddress");
			    	String timeofpickup = rs.getString("timeofpickup");
			    	java.sql.Timestamp compdt = rs.getTimestamp("ordercompleteddatetime");
			    	
			    	//get deliverer name
			    	int deliverstaffid = rs.getInt("delivererstaffid");
			    	String dellastname = rs.getString("dellastname");
			    	String delfirstname = rs.getString("delfirstname");
			    	String delmiddleinit = rs.getString("delmiddleinit");
			    	String delname = "";
			    	if (delfirstname != null)
			    		delname = delfirstname;
			    	if (delmiddleinit != null)
			    		delname += " " + delmiddleinit;
			    	if (dellastname != null)
			    		delname += " " + dellastname;
			    	delname = delname.trim();

			    	List<menuitem> lstMenuItems = new ArrayList<menuitem>();
			    	List<additions> lstAdditions = new ArrayList<additions>();
			    	
			    	order ord = new order(id, dt, firstname, lastname, ordertypeid, ordertypename, orderaddress,
			    			timeofpickup, deliverstaffid, delname, compdt, lstMenuItems, lstAdditions);
			    	lstOrders.add(ord);
				}
				rs.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}			
			order currord = null;
			
			// add header of the table
			String columns[] = new String[] { "ID", "First Name", "Last Name", "Order Date", "Order Type", "Order Address",
					  "Deliverer", "Time of Pick Up", "Completed Date"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
	        };
	        //load data into table
	        Object[][] data = new Object[1000][9];
			data = Arrays.copyOf(data, lstOrders.size());
			for (int i=0; i < lstOrders.size(); i++) {
		    	 currord = lstOrders.get(i);
		    	 if (currord != null) {
					 data[i][0] = currord.getOrderID();
					 data[i][1] = currord.getFirstName();
					 data[i][2] = currord.getLastName();
					 data[i][3] = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(currord.getOrderDate()); 
					 data[i][4] = currord.getOrderType(); 
					 data[i][5] = currord.getOrderAddress();
					 data[i][6] = currord.getDelivererStaffName();
					 data[i][7] = currord.getTimeOfPickup();
					 if (currord.getOrderCompletedDateTime() != null)
						 data[i][8] = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(currord.getOrderCompletedDateTime()); 
					 else
						 data[i][8] = "";
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
			table.setBounds(173, 50, 1000, 700);
			
			//set size of columns
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(98);
		    table.getColumnModel().getColumn(2).setPreferredWidth(98);
		    table.getColumnModel().getColumn(3).setPreferredWidth(108);
		    table.getColumnModel().getColumn(4).setPreferredWidth(87);
		    table.getColumnModel().getColumn(5).setPreferredWidth(250);
		    table.getColumnModel().getColumn(6).setPreferredWidth(108);
		    table.getColumnModel().getColumn(7).setPreferredWidth(98);		     
		    table.getColumnModel().getColumn(8).setPreferredWidth(128);		     

		    //add scroll bar
			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 52, 1000, 720);
	        panel.add(scrollPane);
			panel.setLayout(null);

	        //render edit button and its event (open an orderwindow passing in its idvalue to indicate its an edit, and which order to render)
			JButton btnEditOrder = new JButton("Edit Order");
			btnEditOrder.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnEditOrder.setBounds(24, 11, 123, 30);
			btnEditOrder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							OrderWindow editorderwin = new OrderWindow(idvalue, conn, table, row);
							editorderwin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(null, "You must select an order in the table.");
				}
			});
			panel.add(btnEditOrder);
			
			//render Delete Order Button and its Event Processing
			JButton btnDeleteOrder = new JButton("Delete Order");
			btnDeleteOrder.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnDeleteOrder.setBounds(157, 11, 123, 30);
			btnDeleteOrder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the order?", "Delete Order", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (input == JOptionPane.YES_OPTION) {
							String idstring = table.getModel().getValueAt(row, 0).toString();
							if (idstring != null) {
								int ordid = Integer.valueOf(idstring);
								String sqlDel = "delete from tonyspizza.order where orderid = ?";
								try {
									PreparedStatement pDel = conn.prepareStatement(sqlDel);
									pDel.setInt(1, ordid);
									pDel.executeUpdate();
									pDel.close();
								} catch (SQLException ex) {
								    // handle any errors
								    System.out.println("SQLException: " + ex.getMessage());
								    System.out.println("SQLState: " + ex.getSQLState());
								    System.out.println("VendorError: " + ex.getErrorCode());
								}
	
								
								((DefaultTableModel)table.getModel()).removeRow(row);							
							}
						}
					}
					else 
						JOptionPane.showMessageDialog(null, "You must select an order in the table.");
				}
			});
			panel.add(btnDeleteOrder);
			
			JButton btnClose = new JButton("Close");
			btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnClose.setBounds(290, 11, 123, 30);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			panel.add(btnClose);
	    }
}
