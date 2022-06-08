package com.tonyspizza.view;
//Window rendering of all the Additions Listing Screen

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

public class AdditionsListWindow extends JDialog {
	
	private JFrame frame;
	private JTable table;
	
	/**
	 * Create the Employee Window
	 */
	public AdditionsListWindow(Connection conn) {
		if (conn == null) dispose();
	    
		setBounds(130, 130, 618, 697);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Additions");
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<additions> lstAdditionItems = new ArrayList<additions>();
    
			try {
			    // Query a list of all the additions
			    String sql = "select additionsid, addedname, addedprice "
			    		+ "from tonyspizza.additions "
			    		+ "order by addedname ";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
					//pull specific fields and place in additions model
			    	int additionsid = rs.getInt("additionsid");
			    	String addedname = rs.getString("addedname");
			    	BigDecimal price = rs.getBigDecimal("addedprice");
			    	
			    	additions adds = new additions(additionsid, addedname, price);
			    	lstAdditionItems.add(adds);
				}
				rs.close();
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}			
			additions adds = null;
			
			
			// specifics for the additions table header
			String columns[] = new String[] { "ID", "Addition", "Price"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, String.class
	        };
	        
	        //place additions fields in table on screen
	        Object[][] data = new Object[1000][3];
			data = Arrays.copyOf(data, lstAdditionItems.size());
			for (int i=0; i < lstAdditionItems.size(); i++) {
		    	 adds = lstAdditionItems.get(i);
		    	 if (adds != null) {
					 data[i][0] = adds.getAdditionsID();
					 data[i][1] = adds.getAddedName();
					 data[i][2] = adds.getAddedPrice();
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
			table.setBounds(173, 50, 800, 500);
			
			//set size of columns
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(416);
		    table.getColumnModel().getColumn(2).setPreferredWidth(100);
		    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		    table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);		     
		     
		    //add scroll bar
			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(24, 50, 549, 529);
	        panel.add(scrollPane);
		
	        //create Add button which opens Add Addition window
			JButton btnAddAddition = new JButton("Add Addition");
			btnAddAddition.setBounds(24, 11, 123, 30);
			btnAddAddition.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdditionsWindow addwin = new AdditionsWindow(0, conn, table, -1);
					addwin.setVisible(true);
				}
			});
			panel.setLayout(null);
			panel.add(btnAddAddition);

	        //create Edit button which opens Edit Addition window
			JButton btnEditAddition = new JButton("Edit Addition");
			btnEditAddition.setBounds(154, 11, 123, 30);
			btnEditAddition.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						String idstring = table.getModel().getValueAt(row, 0).toString();
						if (idstring != null) {
							int idvalue = Integer.parseInt(idstring);
							AdditionsWindow editadditionswin = new AdditionsWindow(idvalue, conn, table, row);
							editadditionswin.setVisible(true);
						}
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select an addition in the table.");
				}
			});
			panel.add(btnEditAddition);
			
			
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
