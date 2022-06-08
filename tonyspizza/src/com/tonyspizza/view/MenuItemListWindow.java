package com.tonyspizza.view;
//Window Render for the Menu Item Listing

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

public class MenuItemListWindow extends JDialog {
	
	private JFrame frame;
	private JTable table;
	
	/**
	 * Create the Employee Window
	 */
	public MenuItemListWindow(Connection conn) {
		if (conn == null) dispose();
	    
		setBounds(130, 130, 678, 615);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Menu Items");
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<menuitem> lstMenuItems = new ArrayList<menuitem>();
    
			try {
			    System.out.println ("conn not null");
	
			    // Do something with the Connection
			    String sql = "select menuitemid, itemname, price "
			    		+ "from tonyspizza.menuitem "
			    		+ "order by itemname ";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
			    	int menuitemid = rs.getInt("menuitemid");
			    	String itemname = rs.getString("itemname");
			    	BigDecimal price = rs.getBigDecimal("price");
			    	
			    	menuitem mi = new menuitem(menuitemid, itemname, price);
			    	lstMenuItems.add(mi);
				}
				rs.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}			
			menuitem menuitem = null;
			
			// add header of the table
			String columns[] = new String[] { "ID", "Item Name", "Price"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class
	        };
	        Object[][] data = new Object[1000][3];
			data = Arrays.copyOf(data, lstMenuItems.size());
			for (int i=0; i < lstMenuItems.size(); i++) {
		    	 menuitem = lstMenuItems.get(i);
		    	 if (menuitem != null) {
					 data[i][0] = menuitem.getMenuItemID();
					 data[i][1] = menuitem.getItemName();
					 data[i][2] = menuitem.getPrice();
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
			
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(467);
		    table.getColumnModel().getColumn(2).setPreferredWidth(100);
		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		    table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);		     

			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 50, 603, 451);
	        panel.add(scrollPane);

	        //render Add button and its event to open menu item window
			JButton btnAddMenuItem = new JButton("Add Menu Item");
			btnAddMenuItem.setBounds(24, 11, 123, 30);
			btnAddMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MenuItemWindow addmiwin = new MenuItemWindow(0, conn, table, -1);
					addmiwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAddMenuItem);

			//render Edit button and its event to open menu item window passing in idvalue for specific item
			JButton btnEditEmp = new JButton("Edit Menu Item");
			btnEditEmp.setBounds(154, 11, 123, 30);
			btnEditEmp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							MenuItemWindow editmiwin = new MenuItemWindow(idvalue, conn, table, row);
							editmiwin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select a menu item in the table.");
				}
			});
			panel.add(btnEditEmp);
			
			JButton btnClose = new JButton("Close");
			btnClose.setBounds(287, 10, 123, 30);
			btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			panel.add(btnClose);
		}
}
