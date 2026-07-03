/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package event_management_system;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author chamika
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
public class customize_Resource extends javax.swing.JInternalFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(customize_Resource.class.getName());

    // Reference to the parent booking form (for data passing)
    private manage_Bookings parentForm;
    private String selectedPackageId;
    private boolean isUpdating = false;
  

    /**
     * Parameterized constructor — called from manage_Bookings.
     * @param parentForm reference to the parent booking form
     * @param packageId  the currently selected package ID
     */
    public customize_Resource(manage_Bookings parentForm, String packageId) {
        initComponents();
        
        UITheme.removeInternalFrameChrome(this);
        customizeUI();
        setClosable(true);

        this.parentForm = parentForm;
        this.selectedPackageId = packageId;

        // ---- Replace the table model with 6 columns (Resource ID added) ----
        DefaultTableModel newModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Resource ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Added From"}
        ) {
            Class[] types = new Class[]{
                String.class, String.class, Integer.class, Double.class, Double.class, String.class
            };
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false};

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tblResources.setModel(newModel);

        // Optionally hide the Resource ID column from the user
        tblResources.getColumnModel().getColumn(0).setMinWidth(0);
        tblResources.getColumnModel().getColumn(0).setMaxWidth(0);
       tblResources.getColumnModel().getColumn(0).setWidth(0);

        // Load package dropdown from DB
        loadPackages();

        // Load all resources into cmbItems dropdown
        loadAllResources();

        // Set spinner minimum value to 1
        spnQty.setModel(new javax.swing.SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

        // Wire up Add Item button
        btnAddItem.addActionListener(this::btnAddItemActionPerformed);

        // Wire up Remove Item button
        btnRemoveItem.addActionListener(this::btnRemoveItemActionPerformed);



        // Auto-select the package if packageId was provided
        if (packageId != null && !packageId.isEmpty()) {
            for (int i = 0; i < cmbPackage.getItemCount(); i++) {
                if (cmbPackage.getItemAt(i).toString().startsWith(packageId)) {
                    cmbPackage.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Add search capability to cmbPackage
        javax.swing.JTextField txtPackageSearch = (javax.swing.JTextField) cmbPackage.getEditor().getEditorComponent();
        txtPackageSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                if (keyCode == java.awt.event.KeyEvent.VK_UP || keyCode == java.awt.event.KeyEvent.VK_DOWN || 
                    keyCode == java.awt.event.KeyEvent.VK_ENTER || keyCode == java.awt.event.KeyEvent.VK_LEFT || 
                    keyCode == java.awt.event.KeyEvent.VK_RIGHT) {
                    return;
                }
                String text = txtPackageSearch.getText().trim();
                suggestPackages(text);
            }
        });

        // Add search capability to cmbItems
        javax.swing.JTextField txtItemsSearch = (javax.swing.JTextField) cmbItems.getEditor().getEditorComponent();
        txtItemsSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                int keyCode = evt.getKeyCode();
                if (keyCode == java.awt.event.KeyEvent.VK_UP || keyCode == java.awt.event.KeyEvent.VK_DOWN || 
                    keyCode == java.awt.event.KeyEvent.VK_ENTER || keyCode == java.awt.event.KeyEvent.VK_LEFT || 
                    keyCode == java.awt.event.KeyEvent.VK_RIGHT) {
                    return;
                }
                String text = txtItemsSearch.getText().trim();
                suggestItems(text);
            }
        });

        // Initialize total to 0.00
        calculateTableTotal();
    }

    private void suggestPackages(String text) {
        isUpdating = true;
        try (
                java.sql.Connection conn = DBConnect.connect();
                java.sql.PreparedStatement stmt = conn.prepareStatement(
                        "SELECT package_id, package_name FROM packages WHERE package_name LIKE ? OR package_id LIKE ?");) {
            stmt.setString(1, "%" + text + "%");
            stmt.setString(2, "%" + text + "%");
            java.sql.ResultSet result = stmt.executeQuery();

            cmbPackage.removeAllItems();

            boolean hasData = false;
            while (result.next()) {
                hasData = true;
                cmbPackage.addItem(result.getString("package_id") + " - " + result.getString("package_name"));
            }

            javax.swing.JTextField editor = (javax.swing.JTextField) cmbPackage.getEditor().getEditorComponent();
            editor.setText(text);

            if (hasData && !text.isEmpty()) {
                cmbPackage.showPopup();
            } else {
                cmbPackage.hidePopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isUpdating = false;
        }
    }

    private void suggestItems(String text) {
        isUpdating = true;
        try (
                java.sql.Connection conn = DBConnect.connect();
                java.sql.PreparedStatement stmt = conn.prepareStatement(
                        "SELECT resource_id, resource_name FROM resources WHERE resource_name LIKE ? OR resource_id LIKE ?");) {
            stmt.setString(1, "%" + text + "%");
            stmt.setString(2, "%" + text + "%");
            java.sql.ResultSet result = stmt.executeQuery();

            cmbItems.removeAllItems();

            boolean hasData = false;
            while (result.next()) {
                hasData = true;
                cmbItems.addItem(result.getString("resource_id") + " - " + result.getString("resource_name"));
            }

            javax.swing.JTextField editor = (javax.swing.JTextField) cmbItems.getEditor().getEditorComponent();
            editor.setText(text);

            if (hasData && !text.isEmpty()) {
                cmbItems.showPopup();
            } else {
                cmbItems.hidePopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isUpdating = false;
        }
    }

   
    public customize_Resource() {
        this(null, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResources = new javax.swing.JTable();
        cmbPackage = new javax.swing.JComboBox<>();
        txtTotalAmount = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbItems = new javax.swing.JComboBox<>();
        spnQty = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        btnAddItem = new javax.swing.JButton();
        btnRemoveItem = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(UITheme.BG_DEEP);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblResources.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item Name", "Quantity", "Unit Price", "Total Price", "Added From"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblResources);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(531, 0, 510, 486));

        cmbPackage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbPackage.setEditable(true);
        cmbPackage.addActionListener(this::cmbPackageActionPerformed);
        jPanel1.add(cmbPackage, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 25, 292, 42));

        txtTotalAmount.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel1.add(txtTotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 76, 292, 41));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(230, 230, 255));
        jLabel1.setText("Select package");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 25, 195, 42));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(230, 230, 255));
        jLabel2.setText("Total Price");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 73, 195, 41));

        jPanel2.setBackground(UITheme.BG_CARD);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(230, 230, 255));
        jLabel3.setText("Select Item");

        cmbItems.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbItems.setEditable(true);
        cmbItems.addActionListener(this::cmbItemsActionPerformed);

        spnQty.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(230, 230, 255));
        jLabel4.setText("Quantity");

        btnAddItem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddItem.setBackground(new java.awt.Color(63, 84, 186));
        btnAddItem.setForeground(new java.awt.Color(255, 255, 255));
        btnAddItem.setText("Add");

        btnRemoveItem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRemoveItem.setBackground(new java.awt.Color(180, 50, 50));
        btnRemoveItem.setForeground(new java.awt.Color(255, 255, 255));
        btnRemoveItem.setText("Delete");

        btnConfirm.setBackground(new java.awt.Color(34, 139, 34));
        btnConfirm.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirm.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(this::btnConfirmActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnQty)
                            .addComponent(cmbItems, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemoveItem, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 144, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnQty, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemoveItem, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 135, 520, 350));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPackageActionPerformed
        if (isUpdating) {
            return;
        }

        Object selectedItem = cmbPackage.getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        String packageStr = selectedItem.toString().trim();
        if (packageStr.isEmpty() || packageStr.equals("Select Package")) {
            return;
        }

        try {
            
            String packageId = packageStr.split(" - ")[0].trim();
            this.selectedPackageId = packageId;

            DefaultTableModel model = (DefaultTableModel) tblResources.getModel();

            // Remove only package-sourced rows (preserve "Manual" rows)
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                Object addedFromVal = model.getValueAt(i, 5); // "Added From" column (index 5)
                if (addedFromVal != null && !"Manual".equals(addedFromVal.toString())) {
                    model.removeRow(i);
                }
            }

            // Fetch associated items via package_resources JOIN resources (including resource_id)
            try (Connection con = DBConnect.connect();
                 PreparedStatement pst = con.prepareStatement(
                     "SELECT r.resource_id, r.resource_name, pr.quantity, r.cost_per_item "
                     + "FROM package_resources pr "
                     + "JOIN resources r ON pr.resource_id = r.resource_id "
                     + "WHERE pr.package_id = ?")) {

                pst.setString(1, packageId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String resId = rs.getString("resource_id");
                    String itemName = rs.getString("resource_name");
                    int qty = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("cost_per_item");
                    double itemTotal = qty * unitPrice;

                    // Skip if this resource already exists as a Manual item
                    boolean existsAsManual = false;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (resId.equals(model.getValueAt(i, 0).toString())
                                && "Manual".equals(model.getValueAt(i, 5).toString())) {
                            existsAsManual = true;
                            break;
                        }
                    }

                    if (!existsAsManual) {
                        model.addRow(new Object[]{
                            resId,       // Resource ID (col 0)
                            itemName,    // Item Name (col 1)
                            qty,         // Quantity (col 2)
                            unitPrice,   // Unit Price (col 3)
                            itemTotal,   // Total Price (col 4)
                            packageId    // Added From (col 5)
                        });
                    }
                }
            }

            // Recalculate the grand total
            calculateTableTotal();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading package items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_cmbPackageActionPerformed

    private void cmbItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbItemsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbItemsActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        DefaultTableModel model = (DefaultTableModel) tblResources.getModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No items in the table. Please add items before confirming.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Extract all rows into a List<Object[]>
        List<Object[]> items = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] row = new Object[]{
                model.getValueAt(i, 0), // Resource ID
                model.getValueAt(i, 1), // Item Name
                model.getValueAt(i, 2), // Quantity
                model.getValueAt(i, 3), // Unit Price
                model.getValueAt(i, 4), // Total Price
                model.getValueAt(i, 5)  // Added From
            };
            items.add(row);
        }

        double grandTotal = 0.0;
        try {
            grandTotal = Double.parseDouble(txtTotalAmount.getText().trim());
        } catch (NumberFormatException e) {
            grandTotal = 0.0;
        }

        // Auto-save the items total as the package price in DB
        // so that Billing & Cost form reads the correct price
        if (selectedPackageId != null && !selectedPackageId.isEmpty()) {
            try (Connection conn = DBConnect.connect()) {
                String sql = "UPDATE package SET price = ? WHERE package_id = ?";
                PreparedStatement pstUpdate = conn.prepareStatement(sql);
                pstUpdate.setDouble(1, grandTotal);
                pstUpdate.setString(2, selectedPackageId);
                pstUpdate.executeUpdate();
                pstUpdate.close();
            } catch (Exception ex) {
                logger.log(java.util.logging.Level.WARNING, "Could not auto-save package price: " + ex.getMessage());
            }
        }

        if (parentForm != null) {
            parentForm.setCustomizedData(grandTotal, items);
        }

        this.dispose();
    }//GEN-LAST:event_btnConfirmActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new customize_Resource().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JComboBox<String> cmbItems;
    private javax.swing.JComboBox<String> cmbPackage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnQty;
    private javax.swing.JTable tblResources;
    private javax.swing.JTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables

    
    private void customizeUI() {
        getContentPane().setBackground(UITheme.BG_DEEP);
        
        UITheme.styleComboBox(cmbPackage);
        UITheme.styleComboBox(cmbItems);
        UITheme.styleSpinner(spnQty);
        UITheme.styleTextField(txtTotalAmount);
        
        UITheme.styleButton(btnAddItem, UITheme.BTN_BLUE);
        UITheme.styleButton(btnRemoveItem, UITheme.BTN_RED);
        UITheme.styleButton(btnConfirm, new java.awt.Color(34, 139, 34));
        
        UITheme.styleTable(tblResources);
        
        javax.swing.JLabel[] labels = {
            jLabel1, jLabel2, jLabel3, jLabel4
        };
        for (javax.swing.JLabel lbl : labels) {
            lbl.setForeground(UITheme.FG_WHITE);
        }
    }
    
    private void calculateTableTotal() {
       DefaultTableModel model = (DefaultTableModel) tblResources.getModel();
        double grandTotal = 0.0;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 4); // "Total Price" column (index 4 in 6-col model)
            if (value != null) {
                grandTotal += Double.parseDouble(value.toString());
            }
        }

        
        txtTotalAmount.setText(String.format("%.2f", grandTotal));
    }

    
    private void loadPackages() {
        try {
           Connection con = DBConnect.connect();
            String sql = "SELECT package_id, package_name FROM package ORDER BY package_id";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cmbPackage.removeAllItems();
            cmbPackage.addItem("Select Package"); // Placeholder item

            while (rs.next()) {
                cmbPackage.addItem(rs.getString("package_id") + " - " + rs.getString("package_name"));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading packages: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void loadAllResources() {
        try {
            Connection con = DBConnect.connect();
            String sql = "SELECT resource_id, resource_name FROM resources ORDER BY resource_id";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            cmbItems.removeAllItems();
            cmbItems.addItem("Select Item"); // Placeholder item

            while (rs.next()) {
                cmbItems.addItem(rs.getString("resource_id") + " - " + rs.getString("resource_name"));
            }

            rs.close();
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading resources: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {

        // Validate that an item is selected
        Object selectedItem = cmbItems.getSelectedItem();
        if (selectedItem == null || selectedItem.toString().equals("Select Item")) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item from the dropdown.",
                    "Validation",JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemStr = selectedItem.toString().trim();

      
        String resourceId = itemStr.split(" - ")[0].trim();
        String resourceName = itemStr.substring(itemStr.indexOf(" - ") + 3).trim();

        int quantity = (int) spnQty.getValue();

        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                 "SELECT cost_per_item FROM resources WHERE resource_id = ?")) {

            pst.setString(1, resourceId);
           ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double unitPrice = rs.getDouble("cost_per_item");
                double totalPrice = quantity * unitPrice;

                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblResources.getModel();

                // Check if the item already exists by resource_id (column 0)
                boolean itemExists = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String existingId = model.getValueAt(i, 0).toString();

                    if (existingId.equals(resourceId)) {
                        // Item exists — update quantity and total price
                        int existingQty = Integer.parseInt(model.getValueAt(i, 2).toString());
                        int newQty = existingQty + quantity;
                        double newTotal = newQty * unitPrice;

                        model.setValueAt(newQty, i, 2);       // Update Quantity (col 2)
                        model.setValueAt(newTotal, i, 4);     // Update Total Price (col 4)
                        model.setValueAt("Manual", i, 5);     // Mark as Manual (col 5)

                        itemExists = true;
                        break;
                    }
                }

              
                if (!itemExists) {
                    model.addRow(new Object[]{
                        resourceId,      // Resource ID (col 0)
                        resourceName,    // Item Name (col 1)
                        quantity,        // Quantity (col 2)
                        unitPrice,       // Unit Price (col 3)
                        totalPrice,      // Total Price (col 4)
                        "Manual"         // Added From (col 5)
                    });
                }

                // Recalculate the grand total
                calculateTableTotal();

            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Resource not found in the database.",
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Error adding item: " + e.getMessage(),
                    "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {

        int selectedRow = tblResources.getSelectedRow();

        if (selectedRow == -1) {
          
            JOptionPane.showMessageDialog(this,
                    "Please select a row to remove.",
                    "No Selection", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm before removing
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove the selected item?",
                "Confirm Removal", javax.swing.JOptionPane.YES_NO_OPTION);

        if (confirm ==JOptionPane.YES_OPTION) {
           DefaultTableModel model = (DefaultTableModel) tblResources.getModel();
            model.removeRow(selectedRow);

            // Recalculate the grand total
            calculateTableTotal();
        }
    }


}
