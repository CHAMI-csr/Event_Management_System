package event_management_system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class assign_suppliers extends javax.swing.JInternalFrame {
    private static final Logger logger = Logger.getLogger(assign_suppliers.class.getName());
    
    // --- Colors & Fonts (matching assign_resources) ---
    private static final Color BG_HDR = new Color(34, 34, 52);
    private static final Color BG_ROW_ODD = new Color(28, 28, 42);
    private static final Color BG_ROW_EVEN = new Color(24, 24, 38);
    private static final Color BG_ROW_SEL = new Color(60, 80, 200);
    private static final Color FG_WHITE = new Color(230, 230, 255);
    private static final Color BORDER_COL = new Color(50, 50, 75);
    
    private static final Font F_HDR = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 12);

    private String targetEventId;
    private String selectedSupId = "";
    private DefaultTableModel supTableModel;
    
    public assign_suppliers() {
        this("");
    }

    public assign_suppliers(String eventId) {
        if (eventId != null && eventId.contains(" - ")) {
            this.targetEventId = eventId.split(" - ")[0].trim();
        } else {
            this.targetEventId = (eventId == null) ? "" : eventId.trim();
        }
        
        initComponents();
        customizeUI();
        searchSuppliers(""); // Load initial table
        
        // Strip NetBeans internal-frame chrome to fit into dashboard seamlessly
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        if (ui != null) ui.setNorthPane(null);
    }
    
    private void customizeUI() {
        if (targetEventId != null && !targetEventId.isEmpty()) {
            txtEventId.setText(targetEventId);
        }
        
        supTableModel = (DefaultTableModel) tblSupplierResults.getModel();
        styleTable(tblSupplierResults);
    }
    
    private void styleTable(JTable tbl) {
        tbl.setFont(F_TABLE);
        tbl.setRowHeight(30);
        tbl.setGridColor(BORDER_COL);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setSelectionBackground(BG_ROW_SEL);
        tbl.setSelectionForeground(Color.WHITE);
        tbl.setFillsViewportHeight(true);
        
        JTableHeader hdr = tbl.getTableHeader();
        hdr.setBackground(BG_HDR);
        hdr.setForeground(Color.WHITE);
        hdr.setFont(F_HDR);
        hdr.setPreferredSize(new Dimension(hdr.getWidth(), 36));
        hdr.setReorderingAllowed(false);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.LEFT); }
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(BG_HDR);
                setForeground(Color.WHITE);
                setFont(F_HDR);
                setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 1, BORDER_COL), new EmptyBorder(0, 10, 0, 10)));
                return this;
            }
        });
        
        DefaultTableCellRenderer rowRend = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? BG_ROW_SEL : (row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD));
                setForeground(sel ? Color.WHITE : FG_WHITE);
                setFont(F_TABLE);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        };
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(rowRend);
        }
    }

    private void searchSuppliers(String keyword) {
        supTableModel.setRowCount(0);
        String sql = "SELECT sup_id, sup_name, vehicle_modal FROM suppliers WHERE LOWER(sup_id) LIKE ? OR LOWER(sup_name) LIKE ?";
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(sql)) {
            String searchStr = "%" + keyword.toLowerCase() + "%";
            pst.setString(1, searchStr);
            pst.setString(2, searchStr);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                supTableModel.addRow(new Object[]{
                    rs.getString("sup_id"),
                    rs.getString("sup_name"),
                    rs.getString("vehicle_modal")
                });
            }
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Supplier search error", ex);
        }
    }
    
    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {
        if (targetEventId == null || targetEventId.trim().isEmpty() || targetEventId.equals("Select Event")) {
            JOptionPane.showMessageDialog(this, "No valid Event ID passed.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedSupId.isEmpty() || lblSelectedSupplierID.getText().equals("None")) {
            JOptionPane.showMessageDialog(this, "Please select a Supplier from the table.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double transportCost = 0.0;
        try {
            transportCost = Double.parseDouble(txtTransportCost.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Transport Cost.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                 "UPDATE events SET sup_id = ?, transport_cost = ? WHERE event_id = ?")) {
            pst.setString(1, selectedSupId);
            pst.setDouble(2, transportCost);
            pst.setString(3, targetEventId);
            int updated = pst.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Supplier & Logistics successfully saved for Event: " + targetEventId, "Success", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "No event found to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save logistics: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtSearchSupplierKeyReleased(java.awt.event.KeyEvent evt) {
        searchSuppliers(txtSearchSupplier.getText().trim());
    }

    private void tblSupplierResultsMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tblSupplierResults.getSelectedRow();
        if(row != -1) {
            selectedSupId = supTableModel.getValueAt(row, 0).toString();
            lblSelectedSupplierID.setText(selectedSupId);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabelEventTitle = new javax.swing.JLabel();
        txtEventId = new javax.swing.JTextField();
        jLabelSearch = new javax.swing.JLabel();
        txtSearchSupplier = new javax.swing.JTextField();
        jLabelSelectedIDTitle = new javax.swing.JLabel();
        lblSelectedSupplierID = new javax.swing.JLabel();
        jLabelCost = new javax.swing.JLabel();
        txtTransportCost = new javax.swing.JTextField();
        btnAssign = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelTableTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplierResults = new javax.swing.JTable();

        setClosable(true);
        setMaximumSize(new java.awt.Dimension(1060, 600));
        setMinimumSize(new java.awt.Dimension(1060, 600));
        setPreferredSize(new java.awt.Dimension(1060, 600));

        jPanel1.setBackground(new java.awt.Color(22, 14, 14));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(230, 230, 255));
        lblTitle.setText("Logistics & Supplier Live-Search");
        jPanel1.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 400, 40));

        jPanel2.setBackground(new java.awt.Color(24, 24, 38));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelEventTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelEventTitle.setForeground(new java.awt.Color(230, 230, 255));
        jLabelEventTitle.setText("Target Event:");
        jPanel2.add(jLabelEventTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 130, 36));

        txtEventId.setEditable(false);
        txtEventId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtEventId, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 20, 228, 36));

        jLabelSearch.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelSearch.setForeground(new java.awt.Color(230, 230, 255));
        jLabelSearch.setText("Search Supplier:");
        jPanel2.add(jLabelSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 76, 130, 36));

        txtSearchSupplier.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierKeyReleased(evt);
            }
        });
        jPanel2.add(txtSearchSupplier, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 76, 228, 36));

        jLabelSelectedIDTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelSelectedIDTitle.setForeground(new java.awt.Color(230, 230, 255));
        jLabelSelectedIDTitle.setText("Selected ID:");
        jPanel2.add(jLabelSelectedIDTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 132, 130, 36));

        lblSelectedSupplierID.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblSelectedSupplierID.setForeground(new java.awt.Color(220, 60, 60));
        lblSelectedSupplierID.setText("None");
        jPanel2.add(lblSelectedSupplierID, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 132, 228, 36));

        jLabelCost.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCost.setForeground(new java.awt.Color(230, 230, 255));
        jLabelCost.setText("Transport Cost (Rs):");
        jPanel2.add(jLabelCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 188, 140, 36));

        txtTransportCost.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTransportCost.setText("0.00");
        jPanel2.add(txtTransportCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 188, 228, 36));

        btnAssign.setBackground(new java.awt.Color(50, 100, 255));
        btnAssign.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnAssign.setForeground(new java.awt.Color(255, 255, 255));
        btnAssign.setText("Assign Supplier & Save");
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });
        jPanel2.add(btnAssign, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 250, 367, 45));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 64, 400, 480));

        jPanel3.setBackground(new java.awt.Color(24, 24, 38));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTableTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTableTitle.setForeground(new java.awt.Color(90, 120, 240));
        jLabelTableTitle.setText("Supplier Directory");
        jPanel3.add(jLabelTableTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 300, 30));

        tblSupplierResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Supplier Name", "Vehicle Model"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplierResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierResultsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplierResults);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 46, 594, 420));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(424, 64, 610, 480));

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAssign;
    private javax.swing.JLabel jLabelCost;
    private javax.swing.JLabel jLabelEventTitle;
    private javax.swing.JLabel jLabelSearch;
    private javax.swing.JLabel jLabelSelectedIDTitle;
    private javax.swing.JLabel jLabelTableTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblSelectedSupplierID;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblSupplierResults;
    private javax.swing.JTextField txtEventId;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtTransportCost;
    // End of variables declaration//GEN-END:variables
}
