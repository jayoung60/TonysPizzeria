package com.tonyspizza.view;
//Window rendering of Current Orders (Main) Screen
import java.awt.EventQueue;
import javax.swing.*;
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



public class Application {
	
	private JFrame frame;
	private JTable table;
	private static Connection conn;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

	    EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}
	
	private Connection connectDataBase() {
		//connect to the database using tonyspizzaadmin user
		Connection conn = null;
		try {
		    String url       = "jdbc:mysql://localhost:3306/tonyspizza";
		    String user      = "tonyspizzaadmin";
		    String password  = "thecheesiest";
			
		    // create a connection to the database
		    conn = DriverManager.getConnection(url, user, password);
		}
        catch (Exception ex)
        {
		       System.err.println ("Cannot connect to database server");
		       conn = null;
        }
		return conn;
	}

	
	private void initialize() {  //render the order screen window
	    conn = connectDataBase();
	    
		frame = new JFrame();
		frame.setBounds(100, 100, 1150, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Order Screen - Current Orders");
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		frame.getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<order> lstOrders = new ArrayList<order>();
    
	    if (conn != null) {
			try {
			    // Load all the orders
			    String sql = "select orderid, orderdate, o.firstname, o.lastname, o.ordertype, ot.ordertypename, orderaddress, o.delivererstaffid, timeofpickup, "
			    		+ " e.lastname as dellastname, e.firstname as delfirstname, e.middleinitial as delmiddleinit "
			    		+ "from tonyspizza.order o "
			    		+ "inner join tonyspizza.ordertype ot on o.ordertype = ot.ordertypeid " 
			    		+ "left outer join tonyspizza.employee e on o.delivererstaffid = e.staffid "
			    		+ "where o.ordercompleteddatetime is null "
			    		+ "order by orderdate desc";
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
			    			timeofpickup, deliverstaffid, delname, null, lstMenuItems, lstAdditions);
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
					  "Deliverer", "Time of Pick Up"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
	        };
	        //load data into table
	        Object[][] data = new Object[1000][8];
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
		    table.getColumnModel().getColumn(0).setPreferredWidth(40);
		    table.getColumnModel().getColumn(1).setPreferredWidth(108);
		    table.getColumnModel().getColumn(2).setPreferredWidth(108);
		    table.getColumnModel().getColumn(3).setPreferredWidth(108);
		    table.getColumnModel().getColumn(4).setPreferredWidth(87);
		    table.getColumnModel().getColumn(5).setPreferredWidth(300);
		    table.getColumnModel().getColumn(6).setPreferredWidth(138);
		    table.getColumnModel().getColumn(7).setPreferredWidth(108);		     

		    //add scroll bar
			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 102, 1000, 700);
	        panel.add(scrollPane);
		
	        //render add button and its event (open an orderwindow passing in 0 to indicate its an addition)
			JButton btnAddOrder = new JButton("Add Order");
			btnAddOrder.setBounds(24, 68, 123, 30);
			btnAddOrder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					OrderWindow addorderwin = new OrderWindow(0, conn, table, -1);
					addorderwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAddOrder);

	        //render edit button and its event (open an orderwindow passing in its idvalue to indicate its an edit, and which order to render)
			JButton btnEditOrder = new JButton("Edit Order");
			btnEditOrder.setBounds(152, 68, 123, 30);
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
						JOptionPane.showMessageDialog(frame, "You must select an order in the table.");
				}
			});
			panel.add(btnEditOrder);
			
			//render Delete Order Button and its Event Processing
			JButton btnDeleteOrder = new JButton("Delete Order");
			btnDeleteOrder.setBounds(285, 68, 123, 30);
			btnDeleteOrder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						int input = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the order?", "Delete Order", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
						JOptionPane.showMessageDialog(frame, "You must select an order in the table.");
				}
			});
			panel.add(btnDeleteOrder);
			
			//Render Complete Order Button and Its Event Processing
			JButton btnCompleteOrder = new JButton("Complete Order");
			btnCompleteOrder.setBounds(418, 68, 133, 30);
			btnCompleteOrder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int ordid = Integer.valueOf(idstring);
							String sqlDel = "update tonyspizza.order set OrderCompletedDateTime = now() where orderid = ?";
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

							//remove it from current orders since it is now completed
							((DefaultTableModel)table.getModel()).removeRow(row);							
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select an order in the table.");
				}
			});
			panel.add(btnCompleteOrder);

			//render Employees button and event to open up the Employee Listing screen
			JButton btnEmployees = new JButton("Employees");
			btnEmployees.setBounds(24, 11, 123, 30);
			btnEmployees.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EmployeeListWindow emplistwin = new EmployeeListWindow(conn);
					emplistwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnEmployees);
			
			//render Menu Items  button and event to open up the Menu Items Listing screen
			JButton btnMenuItems = new JButton("Menu Items");
			btnMenuItems.setBounds(152, 11, 123, 30);
			btnMenuItems.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MenuItemListWindow menuitemlistwin = new MenuItemListWindow(conn);
					menuitemlistwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnMenuItems);

			//render Additions button and event to open up the Additions Listing screen
			JButton btnAdditions = new JButton("Additions");
			btnAdditions.setBounds(285, 11, 123, 30);
			btnAdditions.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdditionsListWindow additionslistwin = new AdditionsListWindow(conn);
					additionslistwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAdditions);
			
			//render Catering Requests button and event to open up the Catering Requests Listing screen
			JButton btnCateringRequests = new JButton("Catering Requests");
			btnCateringRequests.setBounds(418, 11, 153, 30);
			btnCateringRequests.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CateringListWindow crlistwin = new CateringListWindow(conn);
					crlistwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnCateringRequests);
			
			JButton btnPastOrders = new JButton("Past Orders");
			btnPastOrders.setBounds(581, 11, 123, 30);
			btnPastOrders.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PastOrdersListWindow crplistwin = new PastOrdersListWindow(conn);
					crplistwin.setVisible(true);
				}
			});
			panel.add(btnPastOrders);
			
			JButton btnClose = new JButton("Close");
			btnClose.setBounds(714, 12, 123, 30);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			panel.add(btnClose);
	    }
	}
}
