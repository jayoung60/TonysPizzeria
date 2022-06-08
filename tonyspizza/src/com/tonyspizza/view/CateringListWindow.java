package com.tonyspizza.view;
//Window Render for the Catering Requests Listing
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CateringListWindow extends JDialog {
	
	private JFrame frame;
	private JTable table;
	
	/**
	 * Create the Employee Window
	 */
	public CateringListWindow(Connection conn) {
		if (conn == null) dispose();
	    
		setBounds(130, 130, 1050, 850);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Catering Requests");
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<cateringrequest> lstCateringRequests = new ArrayList<cateringrequest>();
    
			try {
			    // load all the catering requests
			    String sql = "select caterid, caterdate, caterdescription, caterordertype, ot.ordertypename, firstname, lastname, "
			    		+  "phonenum, email, numguests "
			    		+ "from tonyspizza.cateringrequests cr "
			    		+ "inner join tonyspizza.ordertype ot on cr.caterordertype = ot.ordertypeid "
			    		+ "order by lastname, firstname ";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
			    	int caterid = rs.getInt("caterid");
			    	String firstname = rs.getString("firstname");
			    	String lastname = rs.getString("lastname");
			    	String caterdescription = rs.getString("caterdescription");
			    	String phonenum = rs.getString("phonenum");
			    	String email = rs.getString("email");
			    	int numguests = rs.getInt("numguests");
			    	String ordertypename = rs.getString("ordertypename");
			    	int ordertypeid = rs.getInt("caterordertype");
			    	Timestamp caterdate = rs.getTimestamp("caterdate");
			    	
			    	cateringrequest cr = new cateringrequest(caterid, firstname, lastname, caterdescription, phonenum, email, numguests,
			    			caterdate, ordertypeid, ordertypename);
			    	lstCateringRequests.add(cr);
				}
				rs.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}			
			cateringrequest cr = null;
			
			// add header of the table
			String columns[] = new String[] { "ID", "First Name", "Last Name", "Order Type", "Cater Date", "Email", "Num Guests", "Phone No", 
					"Description"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class, String.class, String.class, String.class, Integer.class, String.class, String.class
	        };
	        
	        //load data into table
	        Object[][] data = new Object[1000][9];
			data = Arrays.copyOf(data, lstCateringRequests.size());
			for (int i=0; i < lstCateringRequests.size(); i++) {
		    	 cr = lstCateringRequests.get(i);
		    	 if (cr != null) {
					 data[i][0] = cr.getCaterID();
					 data[i][1] = cr.getFirstName();
					 data[i][2] = cr.getLastName();
					 data[i][3] = cr.getCaterOrderType();
					 data[i][4] = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(cr.getCaterDate()); ;
					 data[i][5] = cr.getEmail();
					 data[i][6] = cr.getNumGuests();
					 data[i][7] = cr.getPhoneNum();
					 data[i][8] = cr.getCaterDescription();
					 
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
			
			//size table columns
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(100);
		    table.getColumnModel().getColumn(2).setPreferredWidth(100);
		    table.getColumnModel().getColumn(3).setPreferredWidth(90);
		    table.getColumnModel().getColumn(4).setPreferredWidth(100);
		    table.getColumnModel().getColumn(5).setPreferredWidth(200);
		    table.getColumnModel().getColumn(6).setPreferredWidth(90);
		    table.getColumnModel().getColumn(7).setPreferredWidth(90);
		    table.getColumnModel().getColumn(8).setPreferredWidth(200);
	     
		    //add scroll bar
			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 50, 1000, 700);
	        panel.add(scrollPane);
		
	        //render Add button and event to Open a Catering Request Item Window (Add)
			JButton btnAddCR = new JButton("Add Catering Request");
			btnAddCR.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnAddCR.setBounds(24, 11, 223, 30);
			btnAddCR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CateringRequestWindow crwin = new CateringRequestWindow(-1, conn, table, -1);
					crwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAddCR);

	        //render Edit button and event to Open a Catering Request Item Window (Edit)
			JButton btnEditCR = new JButton("Edit Catering Request");
			btnEditCR.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnEditCR.setBounds(254, 11, 223, 30);
			btnEditCR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							//pass in current item in idvalue to catering request window
							CateringRequestWindow crwin = new CateringRequestWindow(idvalue, conn, table, row);
							crwin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select a catering request in the table.");
				}
			});
			panel.add(btnEditCR);
			
			//render Remove button and event to Remove the Catering Request
			JButton btnRemoveCR = new JButton("Remove Catering Request");
			btnRemoveCR.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnRemoveCR.setBounds(484, 11, 223, 30);
			btnRemoveCR.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//update parent window table data
					int row = table.getSelectedRow();
					if (row >= 0) {
						int input = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the catering request?", "Delete Catering Request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (input == JOptionPane.YES_OPTION) {
							String idstring = table.getModel().getValueAt(row, 0).toString();
							if (idstring != null) {
								int ordid = Integer.valueOf(idstring);
								String sqlDel = "delete from tonyspizza.cateringrequests where caterid = ?";
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
						JOptionPane.showMessageDialog(frame, "You must select a catering request in the table.");
				}
			});	
			panel.add(btnRemoveCR);			
			
			JButton btnClose = new JButton("Close");
			btnClose.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnClose.setBounds(717, 11, 123, 30);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			panel.add(btnClose);
	    }
}
