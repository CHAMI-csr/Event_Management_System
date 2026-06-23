/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package event_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author chamika
 */
public class Suppliers_Management extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Suppliers_Management.class.getName());

    /**
     * Creates new form Suppliers_Management
     */
    PreparedStatement pst;
    ResultSet rs;
    Connection con = DBConnect.connect();
    private Admin_Management adminRef; // reference to refresh the table after save
    private boolean editMode = false;  // true when editing an existing supplier
    private String selectedSupId = "";  // ID of the supplier being edited

    // Extra buttons added programmatically (not in form designer)
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnCancel;

    public Suppliers_Management(Admin_Management admin) {
        this.adminRef = admin;
        initComponents();
        lblNextId.setText(nextsupId());
        btnSave.addActionListener(this::btnSaveActionPerformed);
        
        //UITheme.removeInternalFrameChrome(this);
        customizeUI();
        setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        // ---- Create Delete & Cancel buttons ----
        btnDelete = new javax.swing.JButton("Delete");
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 15));
        btnDelete.setBackground(new java.awt.Color(200, 40, 40));
        btnDelete.setForeground(java.awt.Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        jPanel1.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 528, 111, 51));

        btnCancel = new javax.swing.JButton("Cancel");
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 15));
        btnCancel.setBackground(new java.awt.Color(80, 80, 80));
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> resetToAddMode());
        jPanel1.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 528, 111, 51));

        // Position btnSave and btnUpdate side by side (both occupy same slot by default; fix layout)
        // btnSave at col 1, btnUpdate at col 2
        jPanel1.remove(btnSave);
        jPanel1.remove(btnUpdate);
        jPanel1.add(btnSave,   new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 528, 111, 51));
        jPanel1.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 528, 111, 51));

        setToAddMode();
    }

    /** No-arg constructor for standalone use (e.g. from Designer preview). */
    public Suppliers_Management() {
        this(null);
    }

    // ---------------------------------------------------------------
    //  MODE HELPERS
    // ---------------------------------------------------------------

    /** Switch UI into "Add new" mode. */
    private void setToAddMode() {
        editMode = false;
        selectedSupId = "";
        setTitle("Add Supplier");
        btnSave.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnCancel.setVisible(false);
        clearFields();
        lblNextId.setText(nextsupId());
    }

    /** Switch UI into "Edit existing" mode with pre-filled data. */
    public void fillForEdit(String supId, String name, String number, String nic,
                            String address, String vModel, String vNumber,
                            String vPrice, String status) {
        editMode = true;
        selectedSupId = supId;
        setTitle("Edit Supplier — " + supId);

        lblNextId.setText(supId);
        lblSupName.setText(name);
        lblSupNumber.setText(number);
        lblSupNic.setText(nic);
        lblSupAddress.setText(address);
        lblSupVehicleModel.setText(vModel);
        lblSupVehicleNumber.setText(vNumber);
        lblSupVehiclePrice.setText(vPrice);
        lblSupStatus.setSelectedItem(status);

        btnSave.setVisible(false);
        btnUpdate.setVisible(true);
        btnDelete.setVisible(true);
        btnCancel.setVisible(true);
    }

    /** Reset to add mode without closing. */
    private void resetToAddMode() {
        setToAddMode();
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
        jLabel23 = new javax.swing.JLabel();
        lblSupName = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        lblSupNumber = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        lblSupNic = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        lblSupAddress = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        lblSupVehicleModel = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        lblSupVehicleNumber = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        lblSupVehiclePrice = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        lblNextId = new javax.swing.JLabel();
        lblSupStatus = new javax.swing.JComboBox<>();
        btnUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(UITheme.BG_DEEP);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel23.setText("Suppliers Name");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 64, 141, 37));
        jPanel1.add(lblSupName, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 67, 185, 37));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel24.setText("Suppliers Number");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 122, -1, 37));
        jPanel1.add(lblSupNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 125, 185, 37));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setText("NIC");
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 180, 141, 37));

        lblSupNic.addActionListener(this::lblSupNicActionPerformed);
        jPanel1.add(lblSupNic, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 183, 185, 37));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setText("Suppliers Address");
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 238, 163, 37));
        jPanel1.add(lblSupAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 241, 185, 37));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel27.setText("Vehicle MODAL");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 296, 163, 37));
        jPanel1.add(lblSupVehicleModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 299, 185, 37));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel28.setText("Vehicle Number");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 354, 141, 37));
        jPanel1.add(lblSupVehicleNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 357, 185, 37));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel29.setText("Vehicle Price");
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 412, 141, 37));
        jPanel1.add(lblSupVehiclePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 415, 185, 37));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel30.setText("Status");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 470, 141, 37));

        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSave.setText("Add");
        btnSave.addActionListener(this::btnSaveActionPerformed);
        jPanel1.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 528, 111, 51));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel31.setText("Suppliers ID");
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 21, 141, 37));

        lblNextId.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel1.add(lblNextId, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 21, 141, 37));

        lblSupStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "On Goin", "Avilable" }));
        jPanel1.add(lblSupStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 473, 185, 37));

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        jPanel1.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 528, 111, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblSupNicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSupNicActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSupNicActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            // --- Read field values ---
            String supId = lblNextId.getText().trim();
            String name = lblSupName.getText().trim();
            String number = lblSupNumber.getText().trim();
            String nic = lblSupNic.getText().trim();
            String address = lblSupAddress.getText().trim();
            String vModel = lblSupVehicleModel.getText().trim();
            String vNumber = lblSupVehicleNumber.getText().trim();
            String vPrice = lblSupVehiclePrice.getText().trim();
            String status = lblSupStatus.getSelectedItem().toString();

            // --- Validation: check for empty fields ---
            if (name.isEmpty() || number.isEmpty() || nic.isEmpty()
                    || address.isEmpty() || vModel.isEmpty() || vNumber.isEmpty()
                    || vPrice.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // --- Validate vehicle price is a valid number ---
            double vehiclePrice;
            try {
                vehiclePrice = Double.parseDouble(vPrice);
                if (vehiclePrice < 0) {
                    throw new NumberFormatException("Negative value");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Vehicle Price must be a valid positive number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- INSERT into suppliers table ---
            String sql = "INSERT INTO suppliers "
                    + "(sup_id, sup_name, contact_number,nic, sup_address, "
                    + "vehicle_modal, vehicle_no, vehicle_Price, Status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, supId);
            pst.setString(2, name);
            pst.setString(3, number);
            pst.setString(4, nic);
            pst.setString(5, address);
            pst.setString(6, vModel);
            pst.setString(7, vNumber);
            pst.setDouble(8, vehiclePrice);
            pst.setString(9, status);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Supplier saved successfully!  (ID: " + supId + ")",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                
                lblNextId.setText(nextsupId());
                clearFields();
                // Refresh the suppliers table in Admin_Management if available
                if (adminRef != null) {
                    adminRef.loadSupplierTable();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save supplier. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Suppliers_Management.class.getName())
                    .log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Resets all input fields to empty strings after a successful save.
     */
    private void clearFields() {
        lblSupName.setText("");
        lblSupNumber.setText("");
        lblSupNic.setText("");
        lblSupAddress.setText("");
        lblSupVehicleModel.setText("");
        lblSupVehicleNumber.setText("");
        lblSupVehiclePrice.setText("");
        
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            String name    = lblSupName.getText().trim();
            String number  = lblSupNumber.getText().trim();
            String nic     = lblSupNic.getText().trim();
            String address = lblSupAddress.getText().trim();
            String vModel  = lblSupVehicleModel.getText().trim();
            String vNumber = lblSupVehicleNumber.getText().trim();
            String vPrice  = lblSupVehiclePrice.getText().trim();
            String status  = lblSupStatus.getSelectedItem().toString();

            if (name.isEmpty() || number.isEmpty() || nic.isEmpty()
                    || address.isEmpty() || vModel.isEmpty() || vNumber.isEmpty()
                    || vPrice.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all required fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double vehiclePrice;
            try {
                vehiclePrice = Double.parseDouble(vPrice);
                if (vehiclePrice < 0) throw new NumberFormatException("Negative");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Vehicle Price must be a valid positive number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "UPDATE suppliers SET sup_name=?, contact_number=?, nic=?, "
                       + "sup_address=?, vehicle_modal=?, vehicle_no=?, "
                       + "vehicle_Price=?, Status=? WHERE sup_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, number);
            pst.setString(3, nic);
            pst.setString(4, address);
            pst.setString(5, vModel);
            pst.setString(6, vNumber);
            pst.setDouble(7, vehiclePrice);
            pst.setString(8, status);
            pst.setString(9, selectedSupId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Supplier updated successfully! (ID: " + selectedSupId + ")",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                if (adminRef != null) adminRef.loadSupplierTable();
                resetToAddMode();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Update failed. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Suppliers_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    /** Called when the Delete button is clicked in edit mode. */
    private void btnDeleteActionPerformed() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete supplier \"" + selectedSupId + "\"?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return; // user chose No – do nothing

        try {
            String sql = "DELETE FROM suppliers WHERE sup_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, selectedSupId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Supplier deleted successfully!",
                        "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                if (adminRef != null) adminRef.loadSupplierTable();
                resetToAddMode();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Delete failed. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Suppliers_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

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
        java.awt.EventQueue.invokeLater(() -> new Suppliers_Management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblNextId;
    private javax.swing.JTextField lblSupAddress;
    private javax.swing.JTextField lblSupName;
    private javax.swing.JTextField lblSupNic;
    private javax.swing.JTextField lblSupNumber;
    private javax.swing.JComboBox<String> lblSupStatus;
    private javax.swing.JTextField lblSupVehicleModel;
    private javax.swing.JTextField lblSupVehicleNumber;
    private javax.swing.JTextField lblSupVehiclePrice;
    // End of variables declaration//GEN-END:variables
    
    private void customizeUI() {
        getContentPane().setBackground(UITheme.BG_DEEP);
        
        UITheme.styleTextField(lblSupName);
        UITheme.styleTextField(lblSupNumber);
        UITheme.styleTextField(lblSupNic);
        UITheme.styleTextField(lblSupAddress);
        UITheme.styleTextField(lblSupVehicleModel);
        UITheme.styleTextField(lblSupVehicleNumber);
        UITheme.styleTextField(lblSupVehiclePrice);
        
        UITheme.styleComboBox(lblSupStatus);
        
        UITheme.styleButton(btnSave, UITheme.BTN_BLUE);
        UITheme.styleButton(btnUpdate, UITheme.BTN_BLUE);
        
        javax.swing.JLabel[] labels = {
            jLabel23, jLabel24, jLabel25, jLabel26, jLabel27, jLabel28, jLabel29, jLabel30, jLabel31, lblNextId
        };
        for (javax.swing.JLabel lbl : labels) {
            lbl.setForeground(UITheme.FG_WHITE);
        }
    }

    private String nextsupId() {

        String newId = "S-0001";  // default ID
        try {
            // MAX()
            String sql = "SELECT MAX(sup_id) AS max_id FROM suppliers";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            // Data  null 
            if (rs.next() && rs.getString("max_id") != null) {
                String lastId = rs.getString("max_id"); //ex : "S-0001"

                // "S-" (substring(2)
                int num = Integer.parseInt(lastId.substring(2));

                num++; // (1 -> 2)

                //"S-" 
                newId = String.format("S-%04d", num); //  "S-0002"
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newId;

    }

}
