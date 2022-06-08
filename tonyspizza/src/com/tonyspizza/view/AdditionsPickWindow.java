package com.tonyspizza.view;

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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Window rendering for the Manage Additions button on Order Screen
public class AdditionsPickWindow extends JDialog {
	
	private JFrame frame;
	private JTable table;
	boolean isDirtyOrderItems = false;
	
	/**
	 * Create the Employee Window
	 */
	public AdditionsPickWindow(Connection conn, String ids, int orderid, String menuitemname, JTable tblParent, JLabel lblTotalCost, JLabel lblTax, JLabel lblFinalCost) {
		if (conn == null) dispose();
	    
		setBounds(130, 130, 633, 693);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tony's Pizza");
		mnNewMenu.setFont(new Font("Arial", Font.BOLD, 16));
		menuBar.add(mnNewMenu);
		
		Label label = new Label("Add Additions to Order ID: " + orderid +" Menu Item: " + menuitemname);
		label.setFont(new Font("Arial Black", Font.BOLD, 16));
		getContentPane().add(label, BorderLayout.NORTH);
		
		Panel panel = new Panel();
		panel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		List<additions> lstAddedItems = new ArrayList<additions>();
		List<additions> lstAdditionItems = new ArrayList<additions>();
	    List<Integer> additemidlst = new ArrayList<Integer>();
	    List<String> additemnamelst = new ArrayList<String>();
	    List<BigDecimal> additempricelst = new ArrayList<BigDecimal>();
    
			try {
			    // Select all additions currently selected (passed in ids fields) 
			    String sqla = "select additionsid, addedname, addedprice "
			    		+ "from tonyspizza.additions "
			    		+ "where additionsid in (" + ids + ") " 
			    		+ " order by addedname ";
			    PreparedStatement pa = conn.prepareStatement(sqla);
			    ResultSet rsa = pa.executeQuery();
	
				while (rsa.next()) {
			    	int additionsid = rsa.getInt("additionsid");
			    	String addedname = rsa.getString("addedname");
			    	BigDecimal price = rsa.getBigDecimal("addedprice");
			    	
			    	additions adds = new additions(additionsid, addedname, price);
			    	lstAddedItems.add(adds);
				}
				rsa.close();
				pa.close();
				
				//get  a listing of all additions
			    String sql = "select additionsid, addedname, addedprice "
			    		+ "from tonyspizza.additions "
			    		+ "order by addedname ";
			    PreparedStatement p = conn.prepareStatement(sql);
			    ResultSet rs = p.executeQuery();
	
				while (rs.next()) {
			    	int additionsid = rs.getInt("additionsid");
			    	additemidlst.add(additionsid);
			    	String addedname = rs.getString("addedname");
			    	additemnamelst.add(addedname);
			    	BigDecimal price = rs.getBigDecimal("addedprice");
			    	additempricelst.add(price);
			    	
			    	additions adds = new additions(additionsid, addedname, price);
			    	lstAdditionItems.add(adds);
				}
				rs.close();
				p.close();			
			} catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}

			additions adds = null;
			
			//a combo box showing all possible additions
			JComboBox cbAdditionItems = new JComboBox();
			cbAdditionItems.setBounds(37, 31, 156, 22);
			String additemnameitems[] = additemnamelst.toArray(new String[0]);
			panel.setLayout(null);
			cbAdditionItems.setModel(new DefaultComboBoxModel(additemnameitems));
			panel.add(cbAdditionItems);
			
			
			// add header to the table listing of selected additions
			String columns[] = new String[] { "ID", "Addition", "Price"};
			
	        final Class[] columnClass = new Class[] {
	                Integer.class, String.class, BigDecimal.class
	        };
	        
	        //add the data to it
	        Object[][] data = new Object[1000][3];
			data = Arrays.copyOf(data, lstAddedItems.size());
			for (int i=0; i < lstAddedItems.size(); i++) {
		    	 adds = lstAddedItems.get(i);
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
			table.setBounds(173, 50, 1000, 700);
			
		    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    //size columns
		    table.getColumnModel().getColumn(0).setPreferredWidth(30);
		    table.getColumnModel().getColumn(1).setPreferredWidth(300);
		    table.getColumnModel().getColumn(2).setPreferredWidth(100);
		     
		    //add scroll pane
			JScrollPane scrollPane = new JScrollPane(table);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	        scrollPane.setBounds(37, 64, 452, 427);
	        panel.add(scrollPane);
		
	        //button with even to add the addition to the currently selected additions
			JButton btnAddItem = new JButton("Add Addition");
			btnAddItem.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnAddItem.setBounds(203, 30, 156, 23);
			btnAddItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//update parent window table data
					int intindex = cbAdditionItems.getSelectedIndex();
					if (intindex >= 0) {
						Object[] newrowdata = {additemidlst.get(intindex), additemnamelst.get(intindex), additempricelst.get(intindex)};
						((DefaultTableModel)table.getModel()).addRow(newrowdata);
						isDirtyOrderItems = true;
					}
				}
			});	
			panel.add(btnAddItem);
			
			//button to remove an addition in the table of currently selected additions
			JButton btnRemoveItem = new JButton("Remove Addition");
			btnRemoveItem.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnRemoveItem.setBounds(369, 29, 167, 24);
			btnRemoveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//update parent window table data
					int row = table.getSelectedRow();
					if (row >= 0) {
						((DefaultTableModel)table.getModel()).removeRow(row);
						isDirtyOrderItems = true;
					}
					else 
						JOptionPane.showMessageDialog(frame, "You must select an addition in the table.");
				}
			});
			panel.add(btnRemoveItem);

			//button to save all the selected additions and then recalculate totals
			JButton btnSave = new JButton("Save");
			btnSave.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnSave.setBounds(37, 502, 115, 23);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String ids = "";
					String names = "";
					BigDecimal tot = new BigDecimal(0);
					for (int i=0; i < table.getRowCount(); i++) {
						if (ids == "") 
							ids = String.valueOf(table.getValueAt(i, 0));
						else
							ids += "," + String.valueOf(table.getValueAt(i, 0));
						if (names == "") 
							names = (String)table.getValueAt(i, 1);
						else
							names += ", " + table.getValueAt(i, 1);	
						BigDecimal bd = (BigDecimal)table.getValueAt(i, 2);
						tot = tot.add(bd);
					}
					int row = tblParent.getSelectedRow();
					BigDecimal baseprice = (BigDecimal)tblParent.getValueAt(row, 2);
					BigDecimal finalprice = baseprice.add(tot);
					tblParent.setValueAt(ids, row, 6);
					tblParent.setValueAt(names, row, 3);
					tblParent.setValueAt(tot, row, 4);
					tblParent.setValueAt(finalprice, row, 5);
					recalculateordertotals(lblTotalCost, lblTax, lblFinalCost, tblParent, 5);
					dispose();
				}
			});
			panel.add(btnSave);
			
			//button to cancel the manage additions screen
			JButton btnCancel = new JButton("Cancel");
			btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
			btnCancel.setBounds(191, 502, 115, 23);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			panel.add(btnCancel);	
			
	}
	
	//recalculate the below order totals
	private void recalculateordertotals(JLabel lblTotalCost, JLabel lblTax, JLabel lblFinalCost, JTable tbl, int pricepos) {
		BigDecimal bdTotal = BigDecimal.ZERO;
		for (int i=0; i < tbl.getRowCount(); i++) {
			BigDecimal bdItem = (BigDecimal)tbl.getValueAt(i, pricepos);
			System.out.println(pricepos);
			bdTotal = bdTotal.add(bdItem);
		}
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        BigDecimal tax = new BigDecimal("0.06");
        
        lblTotalCost.setText(currency.format(bdTotal));
        BigDecimal salesTax = bdTotal.multiply(tax);
        salesTax = salesTax.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal finaltotal = bdTotal.add(salesTax);
        lblTax.setText(currency.format(salesTax)); 
		lblFinalCost.setText(currency.format(finaltotal));
	}
}
