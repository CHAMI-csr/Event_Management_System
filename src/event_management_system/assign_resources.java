/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package event_management_system;

/**
 * Assign Resources — internal frame.
 * Allows assigning resources to events, viewing, updating, and removing
 * assignments — all backed by the event_resources database table.
 *
 * @author chamika
 */
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.*;

public class assign_resources extends javax.swing.JInternalFrame {

    private static final Logger logger =
            Logger.getLogger(assign_resources.class.getName());

    // ── Colour palette ────────────────────────────────────────────────────────
    private static final Color BG_DEEP      = new Color( 14,  14,  22);
    private static final Color BG_CARD      = new Color( 24,  24,  38);
    private static final Color BG_INPUT     = new Color( 38,  38,  60);
    private static final Color BG_HDR       = new Color( 55,  74, 195);
    private static final Color BG_ROW_ODD   = new Color( 28,  28,  44);
    private static final Color BG_ROW_EVEN  = new Color( 34,  34,  52);
    private static final Color BG_ROW_SEL   = new Color( 70,  96, 215);
    private static final Color BTN_BLUE     = new Color( 63,  84, 186);
    private static final Color BTN_RED      = new Color(195,  55,  55);
    private static final Color BTN_GREY     = new Color( 60,  60,  85);
    private static final Color FG_WHITE     = new Color(230, 230, 255);
    private static final Color FG_MUTED     = new Color(140, 140, 175);
    private static final Color BORDER_COL   = new Color( 55,  55,  85);
    private static final Color ACCENT       = new Color( 90, 120, 240);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BTN   = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_HDR   = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 12);

    // ── State ─────────────────────────────────────────────────────────────────
    private DefaultTableModel tableModel;

    // ─────────────────────────────────────────────────────────────────────────
    public assign_resources() {
        initComponents();

        // Strip NetBeans internal-frame chrome
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        // Build the DefaultTableModel and wire it to the table
        setupTableModel();

        // Apply modern dark styling
        customizeUI();

        // Wire spinner + combo listeners (not possible in GEN block)
        cmbResource.addActionListener(this::cmbResourceActionPerformed);
        spnQty.addChangeListener(e -> recalcTotal());
        tblAssignments.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFormFromTable();
        });

        // Load data from DB
        loadEvents();
        loadResources();
        loadAllAssignments();
    }

    // =========================================================================
    //  TABLE MODEL SETUP
    // =========================================================================
    private void setupTableModel() {
        tableModel = new DefaultTableModel(
            new String[]{"Assign ID","Event ID","Event","Resource","Qty","Unit Cost","Total Cost"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                if (c == 4) return Integer.class;
                if (c == 5 || c == 6) return Double.class;
                return String.class;
            }
        };
        tblAssignments.setModel(tableModel);
        tblAssignments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide internal ID columns (col 0 = Assign ID, col 1 = Event ID)
        tblAssignments.getColumnModel().getColumn(0).setMinWidth(0);
        tblAssignments.getColumnModel().getColumn(0).setMaxWidth(0);
        tblAssignments.getColumnModel().getColumn(1).setMinWidth(0);
        tblAssignments.getColumnModel().getColumn(1).setMaxWidth(0);
    }

    // =========================================================================
    //  STYLING
    // =========================================================================
    private void customizeUI() {
        // Content pane + main panel
        getContentPane().setBackground(BG_DEEP);
        jPanel1.setBackground(BG_DEEP);

        // Title
        lblTitle.setFont(F_TITLE);
        lblTitle.setForeground(Color.WHITE);

        // Cards
        jPanel2.setBackground(BG_CARD);
        jPanel2.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(8, 8, 8, 8)));
        jPanel3.setBackground(BG_CARD);
        jPanel3.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(8, 8, 8, 8)));

        // Form labels
        for (JLabel lbl : new JLabel[]{jLabel1, jLabel2, jLabel3, jLabel4, jLabel5}) {
            lbl.setFont(F_LABEL);
            lbl.setForeground(FG_WHITE);
        }

        // Table section label
        jLabel6.setFont(F_LABEL);
        jLabel6.setForeground(ACCENT);

        // Row count badge
        lblRowCount.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblRowCount.setForeground(FG_MUTED);

        // Inputs
        styleTextField(txtUnitCost);
        styleTextField(txtTotalCost);
        styleTextField(txtSearch);
        txtSearch.putClientProperty("JTextField.placeholderText", "🔍 Search…");
        styleComboBox(cmbEvent);
        styleComboBox(cmbResource);
        styleSpinner(spnQty);

        // Buttons
        styleButton(btnAdd,    BTN_BLUE);
        styleButton(btnUpdate, BTN_GREY);
        styleButton(btnRemove, BTN_RED);
        styleButton(btnClear,  BTN_GREY);

        // Table
        styleTable(tblAssignments);
        jScrollPane1.getViewport().setBackground(BG_ROW_ODD);
        jScrollPane1.setBorder(new LineBorder(BORDER_COL, 1));
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(BG_INPUT);
        tf.setForeground(FG_WHITE);
        tf.setCaretColor(FG_WHITE);
        tf.setFont(F_INPUT);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        tf.putClientProperty("JTextField.placeholderForeground", FG_MUTED);
    }

    private void styleComboBox(JComboBox<?> cb) {
        cb.setBackground(BG_INPUT);
        cb.setForeground(FG_WHITE);
        cb.setFont(F_INPUT);
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? BTN_BLUE : new Color(30, 30, 48));
                setForeground(sel ? Color.WHITE : FG_WHITE);
                setFont(F_INPUT);
                setBorder(new EmptyBorder(4, 10, 4, 10));
                return this;
            }
        });
    }

    private void styleSpinner(JSpinner sp) {
        sp.setBackground(BG_INPUT);
        sp.setForeground(FG_WHITE);
        sp.setFont(F_INPUT);
        sp.setBorder(new LineBorder(BORDER_COL, 1, true));
        if (sp.getEditor() instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setBackground(BG_INPUT);
            de.getTextField().setForeground(FG_WHITE);
            de.getTextField().setFont(F_INPUT);
            de.getTextField().setCaretColor(FG_WHITE);
            de.getTextField().setBorder(new EmptyBorder(4, 8, 4, 8));
        }
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(F_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        btn.putClientProperty("Button.arc", 14);
        Color hover = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bg);    }
        });
    }

    private void styleTable(JTable tbl) {
        tbl.setRowHeight(30);
        tbl.setBackground(BG_ROW_ODD);
        tbl.setForeground(FG_WHITE);
        tbl.setFont(F_TABLE);
        tbl.setGridColor(BORDER_COL);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setSelectionBackground(BG_ROW_SEL);
        tbl.setSelectionForeground(Color.WHITE);
        tbl.setIntercellSpacing(new Dimension(0, 1));
        tbl.setFillsViewportHeight(true);

        JTableHeader hdr = tbl.getTableHeader();
        hdr.setBackground(BG_HDR);
        hdr.setForeground(Color.WHITE);
        hdr.setFont(F_HDR);
        hdr.setPreferredSize(new Dimension(hdr.getWidth(), 36));
        hdr.setReorderingAllowed(false);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.LEFT); }
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(BG_HDR);
                setForeground(Color.WHITE);
                setFont(F_HDR);
                setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 0, 1, BORDER_COL),
                        new EmptyBorder(0, 10, 0, 10)));
                return this;
            }
        });

        DefaultTableCellRenderer rowRend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? BG_ROW_SEL : (row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD));
                setForeground(sel ? Color.WHITE : FG_WHITE);
                setFont(F_TABLE);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(col >= 4 ? SwingConstants.RIGHT : SwingConstants.LEFT);
                return this;
            }
        };
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(rowRend);
        }
    }

    // =========================================================================
    //  DATABASE LOADERS
    // =========================================================================

    private void loadEvents() {
        cmbEvent.removeAllItems();
        cmbEvent.addItem("Select Event");
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT e.event_id, c.client_name, e.event_date " +
                     "FROM events e JOIN clients c ON e.client_id = c.client_id " +
                     "ORDER BY e.event_id DESC");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                cmbEvent.addItem(rs.getString("event_id")
                        + " - " + rs.getString("client_name")
                        + " (" + rs.getDate("event_date") + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loading events: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadResources() {
        cmbResource.removeAllItems();
        cmbResource.addItem("Select Resource");
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT resource_id, resource_name FROM resources ORDER BY resource_id");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                cmbResource.addItem(rs.getString("resource_id")
                        + " - " + rs.getString("resource_name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loading resources: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllAssignments() {
        tableModel.setRowCount(0);
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT er.assignment_id, er.event_id, " +
                     "       CONCAT(c.client_name, ' (', e.event_date, ')') AS event_label, " +
                     "       r.resource_name, er.quantity, r.cost_per_item, er.total_cost " +
                     "FROM event_resources er " +
                     "JOIN events    e ON er.event_id    = e.event_id " +
                     "JOIN clients   c ON e.client_id    = c.client_id " +
                     "JOIN resources r ON er.resource_id = r.resource_id " +
                     "ORDER BY er.assignment_id DESC");
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("assignment_id"),
                    rs.getString("event_id"),
                    rs.getString("event_label"),
                    rs.getString("resource_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("cost_per_item"),
                    rs.getDouble("total_cost")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loading assignments: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateRowCount();
    }

    // =========================================================================
    //  GEN EVENT HANDLERS  (called by initComponents via addActionListener)
    // =========================================================================

    private void cmbEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEventActionPerformed
        // No auto-action needed; event is chosen by user
    }//GEN-LAST:event_cmbEventActionPerformed

    private void cmbResourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbResourceActionPerformed
        autoFillUnitCost();
    }//GEN-LAST:event_cmbResourceActionPerformed

    private void spnQtyStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnQtyStateChanged
        recalcTotal();
    }//GEN-LAST:event_spnQtyStateChanged

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        handleAdd();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        handleUpdate();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        handleRemove();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        filterTable();
    }//GEN-LAST:event_txtSearchKeyReleased

    // =========================================================================
    //  BUSINESS LOGIC
    // =========================================================================

    private void handleAdd() {
        String eventId = getSelectedEventId();
        if (eventId == null) { showWarn("Please select an Event."); return; }

        String resourceId = getSelectedResourceId();
        if (resourceId == null) { showWarn("Please select a Resource."); return; }

        int qty = (int) spnQty.getValue();
        double unitCost, totalCost;
        try {
            unitCost  = Double.parseDouble(txtUnitCost.getText().trim());
            totalCost = Double.parseDouble(txtTotalCost.getText().trim());
        } catch (NumberFormatException ex) { showWarn("Invalid cost values."); return; }

        if (assignmentExists(eventId, resourceId)) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "This resource is already assigned to this event.\n" +
                    "Do you want to ADD to the existing quantity?",
                    "Duplicate", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;
            updateExistingQty(eventId, resourceId, qty);
            loadAllAssignments(); clearForm(); return;
        }

        String newId = generateAssignmentId();
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO event_resources " +
                     "(assignment_id, event_id, resource_id, quantity, total_cost) VALUES (?,?,?,?,?)")) {
            pst.setString(1, newId);
            pst.setString(2, eventId);
            pst.setString(3, resourceId);
            pst.setInt   (4, qty);
            pst.setDouble(5, totalCost);
            pst.executeUpdate();
            showInfo("Resource assigned!\nAssignment ID: " + newId);
            loadAllAssignments(); clearForm();
        } catch (Exception ex) { showError("Add failed: " + ex.getMessage()); }
    }

    private void handleUpdate() {
        int row = tblAssignments.getSelectedRow();
        if (row == -1) { showWarn("Please select a row to update."); return; }

        String assignId = tableModel.getValueAt(row, 0).toString();
        int    qty      = (int) spnQty.getValue();
        double unitCost, totalCost;
        try {
            unitCost  = Double.parseDouble(txtUnitCost.getText().trim());
            totalCost = qty * unitCost;
        } catch (NumberFormatException ex) { showWarn("Invalid cost values."); return; }

        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "UPDATE event_resources SET quantity=?, total_cost=? WHERE assignment_id=?")) {
            pst.setInt   (1, qty);
            pst.setDouble(2, totalCost);
            pst.setString(3, assignId);
            pst.executeUpdate();
            showInfo("Assignment updated!");
            loadAllAssignments(); clearForm();
        } catch (Exception ex) { showError("Update failed: " + ex.getMessage()); }
    }

    private void handleRemove() {
        int row = tblAssignments.getSelectedRow();
        if (row == -1) { showWarn("Please select a row to remove."); return; }

        String assignId = tableModel.getValueAt(row, 0).toString();
        String resource = tableModel.getValueAt(row, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove assignment for: " + resource + "?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "DELETE FROM event_resources WHERE assignment_id=?")) {
            pst.setString(1, assignId);
            pst.executeUpdate();
            showInfo("Assignment removed.");
            loadAllAssignments(); clearForm();
        } catch (Exception ex) { showError("Remove failed: " + ex.getMessage()); }
    }

    private void autoFillUnitCost() {
        String resourceId = getSelectedResourceId();
        if (resourceId == null) { txtUnitCost.setText("0.00"); txtTotalCost.setText("0.00"); return; }
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT cost_per_item FROM resources WHERE resource_id=?")) {
            pst.setString(1, resourceId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                txtUnitCost.setText(String.format("%.2f", rs.getDouble("cost_per_item")));
                recalcTotal();
            }
        } catch (Exception ex) { txtUnitCost.setText("0.00"); }
    }

    private void recalcTotal() {
        try {
            double unit = Double.parseDouble(txtUnitCost.getText().trim());
            int    qty  = (int) spnQty.getValue();
            txtTotalCost.setText(String.format("%.2f", unit * qty));
        } catch (NumberFormatException ex) { txtTotalCost.setText("0.00"); }
    }

    private void populateFormFromTable() {
        int row = tblAssignments.getSelectedRow();
        if (row == -1) return;
        String eventId   = tableModel.getValueAt(row, 1).toString();
        String resource  = tableModel.getValueAt(row, 3).toString();
        int    qty       = Integer.parseInt(tableModel.getValueAt(row, 4).toString());
        double unitCost  = Double.parseDouble(tableModel.getValueAt(row, 5).toString());
        double totalCost = Double.parseDouble(tableModel.getValueAt(row, 6).toString());

        for (int i = 0; i < cmbEvent.getItemCount(); i++) {
            if (cmbEvent.getItemAt(i).startsWith(eventId)) { cmbEvent.setSelectedIndex(i); break; }
        }
        for (int i = 0; i < cmbResource.getItemCount(); i++) {
            if (cmbResource.getItemAt(i).contains(resource)) { cmbResource.setSelectedIndex(i); break; }
        }
        spnQty.setValue(qty);
        txtUnitCost.setText(String.format("%.2f", unitCost));
        txtTotalCost.setText(String.format("%.2f", totalCost));
    }

    private void filterTable() {
        String q = "%" + txtSearch.getText().trim().toLowerCase() + "%";
        tableModel.setRowCount(0);
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT er.assignment_id, er.event_id, " +
                     "       CONCAT(c.client_name,' (',e.event_date,')') AS event_label, " +
                     "       r.resource_name, er.quantity, r.cost_per_item, er.total_cost " +
                     "FROM event_resources er " +
                     "JOIN events e ON er.event_id=e.event_id " +
                     "JOIN clients c ON e.client_id=c.client_id " +
                     "JOIN resources r ON er.resource_id=r.resource_id " +
                     "WHERE LOWER(er.event_id) LIKE ? OR LOWER(r.resource_name) LIKE ? " +
                     "   OR LOWER(c.client_name) LIKE ? ORDER BY er.assignment_id DESC")) {
            pst.setString(1, q); pst.setString(2, q); pst.setString(3, q);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("assignment_id"), rs.getString("event_id"),
                    rs.getString("event_label"),   rs.getString("resource_name"),
                    rs.getInt("quantity"),          rs.getDouble("cost_per_item"),
                    rs.getDouble("total_cost")
                });
            }
        } catch (Exception ex) { logger.log(Level.WARNING, "Filter error", ex); }
        updateRowCount();
    }

    private boolean assignmentExists(String eventId, String resourceId) {
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT 1 FROM event_resources WHERE event_id=? AND resource_id=?")) {
            pst.setString(1, eventId); pst.setString(2, resourceId);
            return pst.executeQuery().next();
        } catch (Exception ex) { return false; }
    }

    private void updateExistingQty(String eventId, String resourceId, int additionalQty) {
        try (Connection con = DBConnect.connect()) {
            double unitCost = 0; int curQty = 0; String assignId = "";
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT er.assignment_id, er.quantity, r.cost_per_item " +
                    "FROM event_resources er JOIN resources r ON er.resource_id=r.resource_id " +
                    "WHERE er.event_id=? AND er.resource_id=?")) {
                pst.setString(1, eventId); pst.setString(2, resourceId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    assignId = rs.getString("assignment_id");
                    curQty   = rs.getInt("quantity");
                    unitCost = rs.getDouble("cost_per_item");
                }
            }
            int newQty = curQty + additionalQty;
            try (PreparedStatement pst = con.prepareStatement(
                    "UPDATE event_resources SET quantity=?, total_cost=? WHERE assignment_id=?")) {
                pst.setInt(1, newQty); pst.setDouble(2, newQty * unitCost); pst.setString(3, assignId);
                pst.executeUpdate();
            }
            showInfo("Quantity updated to " + newQty + " (+" + additionalQty + ").");
        } catch (Exception ex) { showError("Update failed: " + ex.getMessage()); }
    }

    private String generateAssignmentId() {
        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT MAX(assignment_id) AS max_id FROM event_resources");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next() && rs.getString("max_id") != null) {
                return String.format("ASG-%04d",
                        Integer.parseInt(rs.getString("max_id").substring(4)) + 1);
            }
        } catch (Exception ex) { logger.log(Level.WARNING, "ID gen error", ex); }
        return "ASG-0001";
    }

    private String getSelectedEventId() {
        Object o = cmbEvent.getSelectedItem();
        if (o == null) return null;
        String s = o.toString().trim();
        if (s.isEmpty() || s.equals("Select Event")) return null;
        return s.split(" - ")[0].trim();
    }

    private String getSelectedResourceId() {
        Object o = cmbResource.getSelectedItem();
        if (o == null) return null;
        String s = o.toString().trim();
        if (s.isEmpty() || s.equals("Select Resource")) return null;
        return s.split(" - ")[0].trim();
    }

    private void clearForm() {
        cmbEvent.setSelectedIndex(0);
        cmbResource.setSelectedIndex(0);
        spnQty.setValue(1);
        txtUnitCost.setText("0.00");
        txtTotalCost.setText("0.00");
        tblAssignments.clearSelection();
    }

    private void updateRowCount() {
        int n = tableModel.getRowCount();
        lblRowCount.setText(n + (n == 1 ? " record" : " records"));
    }

    private void showInfo (String m) { JOptionPane.showMessageDialog(this, m, "Success",    JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn (String m) { JOptionPane.showMessageDialog(this, m, "Validation", JOptionPane.WARNING_MESSAGE);     }
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Error",      JOptionPane.ERROR_MESSAGE);       }

    // =========================================================================
    //  NetBeans generated code — DO NOT MODIFY
    //  (Mirrors the assign_resources.form XML exactly)
    // =========================================================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbEvent = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbResource = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        spnQty = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        txtUnitCost = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTotalCost = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        lblRowCount = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAssignments = new javax.swing.JTable();

        setClosable(true);
        setMaximumSize(new java.awt.Dimension(1060, 600));
        setMinimumSize(new java.awt.Dimension(1060, 600));
        setPreferredSize(new java.awt.Dimension(1060, 600));

        jPanel1.setBackground(new java.awt.Color(26, 26, 28));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(230, 230, 255));
        lblTitle.setText("Assign Resources");
        jPanel1.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 320, 40));

        jPanel2.setBackground(new java.awt.Color(24, 24, 38));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(230, 230, 255));
        jLabel1.setText("Event");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 130, 36));

        cmbEvent.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbEvent.setEditable(true);
        cmbEvent.addActionListener(this::cmbEventActionPerformed);
        jPanel2.add(cmbEvent, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 20, 228, 36));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(230, 230, 255));
        jLabel2.setText("Resource");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 76, 130, 36));

        cmbResource.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbResource.addActionListener(this::cmbResourceActionPerformed);
        jPanel2.add(cmbResource, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 76, 228, 36));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(230, 230, 255));
        jLabel3.setText("Quantity");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 132, 130, 36));

        spnQty.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        spnQty.addChangeListener(this::spnQtyStateChanged);
        jPanel2.add(spnQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 132, 228, 36));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(230, 230, 255));
        jLabel4.setText("Unit Cost (Rs.)");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 188, 130, 36));

        txtUnitCost.setEditable(false);
        txtUnitCost.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtUnitCost.setText("0.00");
        jPanel2.add(txtUnitCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 188, 228, 36));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(230, 230, 255));
        jLabel5.setText("Total Cost (Rs.)");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 244, 130, 36));

        txtTotalCost.setEditable(false);
        txtTotalCost.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTotalCost.setText("0.00");
        jPanel2.add(txtTotalCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 244, 228, 36));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Plus Math.png"))); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(this::btnAddActionPerformed);
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 320, 170, 40));

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Upgrade.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        jPanel2.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 320, 170, 40));

        btnRemove.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Remove.png"))); // NOI18N
        btnRemove.setText("Remove");
        btnRemove.addActionListener(this::btnRemoveActionPerformed);
        jPanel2.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 374, 170, 40));

        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnClear.setText(" Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);
        jPanel2.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 374, 170, 40));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 64, 400, 520));

        jPanel3.setBackground(new java.awt.Color(24, 24, 38));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(90, 120, 240));
        jLabel6.setText("📋  Assigned Resources");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 8, 300, 30));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        jPanel3.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 8, 220, 30));

        lblRowCount.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRowCount.setForeground(new java.awt.Color(140, 140, 170));
        lblRowCount.setText("0 records");
        jPanel3.add(lblRowCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(548, 8, 70, 30));

        tblAssignments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Assign ID", "Event ID", "Event", "Resource", "Qty", "Unit Cost", "Total Cost"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAssignments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblAssignments);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 46, 610, 462));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(424, 64, 626, 520));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try { javax.swing.UIManager.setLookAndFeel(new FlatDarkLaf()); }
        catch (Exception ex) { logger.log(Level.SEVERE, null, ex); }
        java.awt.EventQueue.invokeLater(() -> new assign_resources().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbEvent;
    private javax.swing.JComboBox<String> cmbResource;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblRowCount;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JSpinner spnQty;
    private javax.swing.JTable tblAssignments;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTotalCost;
    private javax.swing.JTextField txtUnitCost;
    // End of variables declaration//GEN-END:variables
}
