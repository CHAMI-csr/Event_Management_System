/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package event_management_system;

/**
 *
 * @author chamika
 */
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Base64;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;

import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Admin_Management extends javax.swing.JInternalFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Admin_Management.class.getName());

    /**
     * Creates new form manage_Bookings
     */
    PreparedStatement pst;
    ResultSet rs;
    Connection con = DBConnect.connect();
    String package_Id;

    public Admin_Management() {

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        try {
            // Dark theme එකට:
            com.formdev.flatlaf.FlatDarkLaf.setup();

            // එහෙමත් නැත්නම් Light theme එකට:
            // UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        initComponents();
        visible_btn();

        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);

        // Override tbSuppliers column headers to match all 9 DB columns
        tbSuppliers.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Supplier ID", "Name", "Contact Number", "NIC", "Address",
                         "Vehicle Model", "Vehicle No", "Vehicle Price", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        });

        // Double-click on tbSuppliers row → open edit form pre-filled
        tbSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tbSuppliers.getSelectedRow();
                    if (row < 0) return;
                    DefaultTableModel m = (DefaultTableModel) tbSuppliers.getModel();
                    String supId   = safeStr(m.getValueAt(row, 0));
                    String name    = safeStr(m.getValueAt(row, 1));
                    String number  = safeStr(m.getValueAt(row, 2));
                    String nic     = safeStr(m.getValueAt(row, 3));
                    String address = safeStr(m.getValueAt(row, 4));
                    String vModel  = safeStr(m.getValueAt(row, 5));
                    String vNumber = safeStr(m.getValueAt(row, 6));
                    String vPrice  = safeStr(m.getValueAt(row, 7));
                    String status  = safeStr(m.getValueAt(row, 8));

                    Suppliers_Management sm = new Suppliers_Management(Admin_Management.this);
                    sm.fillForEdit(supId, name, number, nic, address, vModel, vNumber, vPrice, status);
                    sm.setVisible(true);
                }
            }
        });

        // ---- jTable1 (Staff table) setup ----
        jTable1.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Staff ID", "Name", "Contact", "Email", "Role"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        });

        // Double-click on jTable1 → action dialog
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jTable1.getSelectedRow();
                    if (row < 0) return;
                    DefaultTableModel m = (DefaultTableModel) jTable1.getModel();
                    String staffId = safeStr(m.getValueAt(row, 0));
                    String staffName = safeStr(m.getValueAt(row, 1));
                    onStaffRowDoubleClick(staffId, staffName);
                }
            }
        });

        // Wire btnPackageUpdate (no GEN handler; wired programmatically)
        btnPackageUpdate.addActionListener(e -> btnPackageUpdateClicked());

        String staffid = genarateUserId();
        lblNextId.setText(staffid);
        lblNextItemID.setText(generateItemId());
        loadItemTable();
        refreshPackageTable();
        loadSupplierTable();
        loadStaffTable();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        jpAddStaff = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblNextId = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtRole = new javax.swing.JComboBox<>();
        txtPassword = new javax.swing.JLabel();
        txrPassword = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        CheckPassword = new javax.swing.JCheckBox();
        btnCreate = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtPasswordConfirm = new javax.swing.JPasswordField();
        jpMannageStaff = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtStaffSearch = new javax.swing.JTextField();
        jpEventAdd = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pakageTable = new javax.swing.JTable();
        txtPackageSearch = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnItemManage = new javax.swing.JButton();
        btnPackageUpdate = new javax.swing.JButton();
        jpResourcesAdd = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        lblNameId = new javax.swing.JLabel();
        lblNextItemID = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtItemPrice = new javax.swing.JTextField();
        txtItemType = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        btnItemAdd = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnItemCancel = new javax.swing.JButton();
        btnItemUpadate = new javax.swing.JButton();
        btnItemDelete = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtItemQty = new javax.swing.JSpinner();
        jpSuppliers = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbSuppliers = new javax.swing.JTable();
        txtSupSearch = new javax.swing.JTextField();
        header = new javax.swing.JPanel();
        btnAddStaff = new javax.swing.JButton();
        btnMannage = new javax.swing.JButton();
        btnEventAdd = new javax.swing.JButton();
        btnSuppliers = new javax.swing.JButton();
        btnInventory = new javax.swing.JButton();

        setClosable(true);
        setMaximumSize(new java.awt.Dimension(1060, 600));
        setMinimumSize(new java.awt.Dimension(1060, 600));
        setPreferredSize(new java.awt.Dimension(1060, 600));

        main.setBackground(new java.awt.Color(255, 153, 153));
        main.setLayout(new java.awt.CardLayout());

        jpAddStaff.setMaximumSize(new java.awt.Dimension(1060, 540));
        jpAddStaff.setPreferredSize(new java.awt.Dimension(1060, 540));
        jpAddStaff.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(26, 26, 36));

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtName.addActionListener(this::txtNameActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("NAME");

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ASSIGN STAFF ID : ");

        lblNextId.setBackground(new java.awt.Color(51, 51, 51));
        lblNextId.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNextId.setForeground(new java.awt.Color(255, 255, 255));

        txtContact.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtContact.addActionListener(this::txtContactActionPerformed);
        txtContact.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContactKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CONTACT NO.");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmail.addActionListener(this::txtEmailActionPerformed);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("EMAIL");

        txtAddress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAddress.addActionListener(this::txtAddressActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ADDRESS");

        txtNId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtNId.addActionListener(this::txtNIdActionPerformed);
        txtNId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNIdKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("National Id");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("ADD STAFF ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(394, 394, 394))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtContact)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtEmail)
                                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtNId))
                                .addGap(210, 210, 210))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblNextId, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(lblNextId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNId, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110))
        );

        jpAddStaff.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 540));

        jPanel4.setBackground(new java.awt.Color(26, 26, 36));
        jPanel4.setMaximumSize(new java.awt.Dimension(400, 530));
        jPanel4.setMinimumSize(new java.awt.Dimension(400, 530));
        jPanel4.setPreferredSize(new java.awt.Dimension(400, 530));
        jPanel4.setVerifyInputWhenFocusTarget(false);
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Account  security");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, 42));

        jLabel9.setBackground(new java.awt.Color(51, 51, 51));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ROLE");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, 115, 42));

        txtRole.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Event Planner", "Ticketing Staff", "Technical", "Analytics", "Administrator" }));
        txtRole.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRoleFocusLost(evt);
            }
        });
        txtRole.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                txtRolePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        txtRole.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtRoleInputMethodTextChanged(evt);
            }
        });
        txtRole.addActionListener(this::txtRoleActionPerformed);
        jPanel4.add(txtRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 220, 42));

        txtPassword.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtPassword.setText("PASSOWORD");
        jPanel4.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 130, 40));

        txrPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txrPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txrPasswordKeyPressed(evt);
            }
        });
        jPanel4.add(txrPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 240, 40));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("CONFIRM pw.");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, 127, 40));

        jLabel12.setForeground(new java.awt.Color(255, 0, 51));
        jLabel12.setText("After checking this box, ");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 400, -1, 30));

        jLabel13.setForeground(new java.awt.Color(255, 0, 51));
        jLabel13.setText("the user can change the password the first time they log in.");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 420, 330, 30));

        CheckPassword.setForeground(new java.awt.Color(255, 255, 255));
        CheckPassword.setText("First Time Can Be Change Password User ");
        CheckPassword.addActionListener(this::CheckPasswordActionPerformed);
        jPanel4.add(CheckPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 380, 290, -1));

        btnCreate.setBackground(new java.awt.Color(153, 255, 204));
        btnCreate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnCreate.setForeground(new java.awt.Color(51, 51, 51));
        btnCreate.setText("CREATE ACCOUNT");
        btnCreate.addActionListener(this::btnCreateActionPerformed);
        jPanel4.add(btnCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 470, 220, 40));

        jLabel14.setForeground(new java.awt.Color(255, 0, 51));
        jLabel14.setText("The Staff ID has been assigned as the default password.");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, 32));

        jLabel15.setForeground(new java.awt.Color(255, 0, 51));
        jLabel15.setText("The password field was left empty. ");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, 32));

        txtPasswordConfirm.addActionListener(this::txtPasswordConfirmActionPerformed);
        jPanel4.add(txtPasswordConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 240, 40));

        jpAddStaff.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 0, 580, 540));

        main.add(jpAddStaff, "card3");

        jpMannageStaff.setMaximumSize(new java.awt.Dimension(880, 530));
        jpMannageStaff.setMinimumSize(new java.awt.Dimension(880, 530));
        jpMannageStaff.setPreferredSize(new java.awt.Dimension(880, 530));

        jPanel1.setBackground(new java.awt.Color(26, 26, 36));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel18.setText("Manage Staff");

        jTable1.setBackground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "staff_id", "staff_name", "contact_number", "staff_email", "role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane4.setViewportView(jTable1);

        txtStaffSearch.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtStaffSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStaffSearchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(300, 300, 300)
                        .addComponent(txtStaffSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtStaffSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jpMannageStaffLayout = new javax.swing.GroupLayout(jpMannageStaff);
        jpMannageStaff.setLayout(jpMannageStaffLayout);
        jpMannageStaffLayout.setHorizontalGroup(
            jpMannageStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpMannageStaffLayout.setVerticalGroup(
            jpMannageStaffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        main.add(jpMannageStaff, "card2");

        jpEventAdd.setMaximumSize(new java.awt.Dimension(880, 540));
        jpEventAdd.setMinimumSize(new java.awt.Dimension(880, 540));
        jpEventAdd.setPreferredSize(new java.awt.Dimension(880, 540));
        jpEventAdd.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(26, 26, 36));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("ADD PACKAGE ");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Circled Right.png"))); // NOI18N
        jButton1.setText("ADD PACKAGE");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        pakageTable.setBackground(new java.awt.Color(0, 0, 0));
        pakageTable.setForeground(new java.awt.Color(255, 255, 255));
        pakageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Package ID", "Package Name", "Package Description", "Estimate Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pakageTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        pakageTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        pakageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pakageTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(pakageTable);

        txtPackageSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPackageSearchMouseClicked(evt);
            }
        });
        txtPackageSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPackageSearchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPackageSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 32, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(txtPackageSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jpEventAdd.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 560, 540));

        jPanel6.setBackground(new java.awt.Color(26, 26, 36));
        jPanel6.setPreferredSize(new java.awt.Dimension(510, 594));

        btnItemManage.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnItemManage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Circled Right.png"))); // NOI18N
        btnItemManage.setText("GO TO PACKAGE ITEM ADD");
        btnItemManage.addActionListener(this::btnItemManageActionPerformed);

        btnPackageUpdate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPackageUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Circled Right.png"))); // NOI18N
        btnPackageUpdate.setText("GO TO UPDATE/DELETE");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemManage, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPackageUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(btnItemManage, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPackageUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(314, 314, 314))
        );

        jpEventAdd.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 500, 540));

        main.add(jpEventAdd, "card2");

        jpResourcesAdd.setBackground(new java.awt.Color(26, 26, 36));
        jpResourcesAdd.setMaximumSize(new java.awt.Dimension(1060, 540));
        jpResourcesAdd.setMinimumSize(new java.awt.Dimension(1060, 540));
        jpResourcesAdd.setPreferredSize(new java.awt.Dimension(1060, 540));
        jpResourcesAdd.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Resources ADD");
        jpResourcesAdd.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 280, 60));

        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jpResourcesAdd.add(txtItemName, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 193, 37));

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("resources Name");
        jpResourcesAdd.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 190, 37));

        lblNameId.setBackground(new java.awt.Color(255, 255, 204));
        lblNameId.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblNameId.setForeground(new java.awt.Color(255, 255, 255));
        lblNameId.setText("Next resources ID");
        jpResourcesAdd.add(lblNameId, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 270, 37));

        lblNextItemID.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblNextItemID.setForeground(new java.awt.Color(255, 255, 255));
        jpResourcesAdd.add(lblNextItemID, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 130, 37));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("resources Type");
        jpResourcesAdd.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 180, 37));

        txtItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jpResourcesAdd.add(txtItemPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 193, 37));

        txtItemType.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtItemType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Music", "Media", "Decor", "Furniture", "Food", "Others" }));
        jpResourcesAdd.add(txtItemType, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 193, 37));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("resources Price");
        jpResourcesAdd.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 180, 37));

        itemTable.setBackground(new java.awt.Color(0, 0, 0));
        itemTable.setForeground(new java.awt.Color(255, 255, 255));
        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Type", "Price", "Quantity"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.setSelectionBackground(new java.awt.Color(255, 255, 255));
        itemTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        itemTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(itemTable);

        jpResourcesAdd.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 90, 620, 430));

        btnItemAdd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnItemAdd.setText("ADD Resources");
        btnItemAdd.addActionListener(this::btnItemAddActionPerformed);
        jpResourcesAdd.add(btnItemAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 400, 170, 39));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Search_1.png"))); // NOI18N
        jpResourcesAdd.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, -1, 50));

        txtSearch.addActionListener(this::txtSearchActionPerformed);
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });
        jpResourcesAdd.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 380, 33));

        btnItemCancel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnItemCancel.setText("CANCEL");
        btnItemCancel.addActionListener(this::btnItemCancelActionPerformed);
        jpResourcesAdd.add(btnItemCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 450, 110, 40));

        btnItemUpadate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnItemUpadate.setText("UPDATE");
        btnItemUpadate.addActionListener(this::btnItemUpadateActionPerformed);
        jpResourcesAdd.add(btnItemUpadate, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 450, 110, 40));

        btnItemDelete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnItemDelete.setText("DELETE");
        btnItemDelete.addActionListener(this::btnItemDeleteActionPerformed);
        jpResourcesAdd.add(btnItemDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 450, 110, 40));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Quantity");
        jpResourcesAdd.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 132, 37));

        txtItemQty.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jpResourcesAdd.add(txtItemQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 330, 193, 37));
        txtItemQty.getAccessibleContext().setAccessibleName("");

        main.add(jpResourcesAdd, "card2");

        jpSuppliers.setBackground(new java.awt.Color(26, 26, 36));
        jpSuppliers.setMaximumSize(new java.awt.Dimension(880, 530));
        jpSuppliers.setMinimumSize(new java.awt.Dimension(880, 530));

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Add Suppliers");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        tbSuppliers.setBackground(new java.awt.Color(0, 0, 0));
        tbSuppliers.setForeground(new java.awt.Color(255, 255, 255));
        tbSuppliers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "suppliers name", "Supplier Number", "nic", "Address", "modal", "Vehicle Number", "Vehicle Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbSuppliers.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tbSuppliers.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbSuppliers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSuppliersMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbSuppliers);

        txtSupSearch.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtSupSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtSupSearchMouseEntered(evt);
            }
        });
        txtSupSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSupSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jpSuppliersLayout = new javax.swing.GroupLayout(jpSuppliers);
        jpSuppliers.setLayout(jpSuppliersLayout);
        jpSuppliersLayout.setHorizontalGroup(
            jpSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSuppliersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1055, Short.MAX_VALUE)
                    .addGroup(jpSuppliersLayout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSupSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        jpSuppliersLayout.setVerticalGroup(
            jpSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSuppliersLayout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jpSuppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSupSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        main.add(jpSuppliers, "card2");

        getContentPane().add(main, java.awt.BorderLayout.CENTER);

        header.setBackground(new java.awt.Color(26, 26, 36));
        header.setPreferredSize(new java.awt.Dimension(1064, 60));
        header.setLayout(new java.awt.GridLayout(1, 6, 10, 5));

        btnAddStaff.setBackground(new java.awt.Color(46, 24, 221));
        btnAddStaff.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAddStaff.setForeground(new java.awt.Color(255, 255, 255));
        btnAddStaff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Add.png"))); // NOI18N
        btnAddStaff.setText("ADD STAFF");
        btnAddStaff.setBorderPainted(false);
        btnAddStaff.setMaximumSize(new java.awt.Dimension(172, 41));
        btnAddStaff.setMinimumSize(new java.awt.Dimension(172, 41));
        btnAddStaff.setPreferredSize(new java.awt.Dimension(172, 41));
        btnAddStaff.addActionListener(this::btnAddStaffActionPerformed);
        header.add(btnAddStaff);

        btnMannage.setBackground(new java.awt.Color(46, 24, 221));
        btnMannage.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnMannage.setForeground(new java.awt.Color(255, 255, 255));
        btnMannage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Request Service_1.png"))); // NOI18N
        btnMannage.setText("MANAGE STAFF");
        btnMannage.setBorderPainted(false);
        btnMannage.setMaximumSize(new java.awt.Dimension(172, 41));
        btnMannage.setMinimumSize(new java.awt.Dimension(172, 41));
        btnMannage.setPreferredSize(new java.awt.Dimension(172, 41));
        btnMannage.addActionListener(this::btnMannageActionPerformed);
        header.add(btnMannage);

        btnEventAdd.setBackground(new java.awt.Color(46, 24, 221));
        btnEventAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEventAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnEventAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Package Settings.png"))); // NOI18N
        btnEventAdd.setText("EVENT PACKAGE ");
        btnEventAdd.setBorderPainted(false);
        btnEventAdd.setMaximumSize(new java.awt.Dimension(172, 41));
        btnEventAdd.setMinimumSize(new java.awt.Dimension(172, 41));
        btnEventAdd.setPreferredSize(new java.awt.Dimension(172, 41));
        btnEventAdd.addActionListener(this::btnEventAddActionPerformed);
        header.add(btnEventAdd);

        btnSuppliers.setBackground(new java.awt.Color(46, 24, 221));
        btnSuppliers.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSuppliers.setForeground(new java.awt.Color(255, 255, 255));
        btnSuppliers.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Supplier_1.png"))); // NOI18N
        btnSuppliers.setText("SUPPLIERS");
        btnSuppliers.setBorderPainted(false);
        btnSuppliers.addActionListener(this::btnSuppliersActionPerformed);
        header.add(btnSuppliers);

        btnInventory.setBackground(new java.awt.Color(46, 24, 221));
        btnInventory.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnInventory.setForeground(new java.awt.Color(255, 255, 255));
        btnInventory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Commodity.png"))); // NOI18N
        btnInventory.setText("RESOURCES");
        btnInventory.setBorderPainted(false);
        btnInventory.addActionListener(this::btnInventoryActionPerformed);
        header.add(btnInventory);

        getContentPane().add(header, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStaffActionPerformed
        jpAddStaff.setVisible(true);
        jpMannageStaff.setVisible(false);
        jpEventAdd.setVisible(false);
        jpResourcesAdd.setVisible(false);
        jpSuppliers.setVisible(false);

    }//GEN-LAST:event_btnAddStaffActionPerformed

    private void btnMannageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMannageActionPerformed
        jpMannageStaff.setVisible(true);
        jpAddStaff.setVisible(false);
        jpEventAdd.setVisible(false);
        jpResourcesAdd.setVisible(false);
        jpSuppliers.setVisible(false);
        loadStaffTable();
    }//GEN-LAST:event_btnMannageActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddressActionPerformed

    private void txtNIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNIdActionPerformed

    private void CheckPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CheckPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CheckPasswordActionPerformed

    private void txtPasswordConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordConfirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordConfirmActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // --- Collect field values ---

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();
        String role = txtRole.getSelectedItem().toString();

        if (role.equals("Administrator")) {
            role = "admin";
        }

        String password = txrPassword.getText().trim();
        String confirmPassword = new String(txtPasswordConfirm.getPassword()).trim();

        String contactStr = txtContact.getText().trim();
        String nIdStr = txtNId.getText().trim();

        // --- Basic empty-field validation ---
        if (name.isEmpty() || email.isEmpty() || address.isEmpty()
                || contactStr.isEmpty() || nIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        if (!email.isEmpty() && !email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address!\n(e.g., example@gmail.com)",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        long contact;
        double nId;
        try {
            contact = Long.parseLong(contactStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Contact number must contain digits only.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            nId = Double.parseDouble(nIdStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "National ID must contain digits only.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Generate next Staff ID ---
        String staffId = genarateUserId();

        if (password.isEmpty()) {
            jLabel15.setVisible(true);
            jLabel14.setVisible(true);
            password = staffId;
        } else {
            jLabel15.setVisible(false);
            jLabel14.setVisible(false);

            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

            if (!password.matches(passwordPattern)) {
                JOptionPane.showMessageDialog(this,
                        "Password must be at least 8 characters long, and include an uppercase letter, a lowercase letter, a digit, and a special character (@#$%^&+=!).",
                        "Weak Password Error",
                        JOptionPane.ERROR_MESSAGE);
                txrPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password and Confirm Password do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            } else {

            }
        }

        // --- Duplicate check (email OR national ID already exists) ---
        if (checkUsername(email, nId)) {
            JOptionPane.showMessageDialog(this, "A staff member with this Email or National ID already exists.", "Duplicate Record", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            String finalPassword = encryptMyPassword(password);

            String sql = "INSERT INTO staff (staff_id, staff_name, contact_number, staff_email, "
                    + "staff_address, Id, role, password,first_time_log) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, staffId);
            pst.setString(2, name);
            pst.setLong(3, contact);
            pst.setString(4, email);
            pst.setString(5, address);
            pst.setDouble(6, nId);
            pst.setString(7, role);
            pst.setString(8, finalPassword);

            if (CheckPassword.isSelected()) {
                pst.setString(9, "1");
            } else {
                pst.setString(9, "0");
            }

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Staff account created successfully!\nStaff ID: " + staffId, "Success", JOptionPane.INFORMATION_MESSAGE);

                txtName.setText("");
                txtContact.setText("");
                txtEmail.setText("");
                txtAddress.setText("");
                txtNId.setText("");
                txtPassword.setText("");
                txtPasswordConfirm.setText("");
                jLabel15.setVisible(false);
                jLabel14.setVisible(false);

                // Refresh the displayed next ID
                String nextId = genarateUserId();
                lblNextId.setText(nextId);

            } else {
                JOptionPane.showMessageDialog(this, "Failed to create staff account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCreateActionPerformed

    private void txtContactKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactKeyPressed
        txtContact.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtContactKeyPressed

    private void txtNIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNIdKeyPressed
        txtNId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();

                //  (0-9), 'V', 'v',
                if (!((c >= '0') && (c <= '9')
                        || (c == 'V')
                        || (c == 'v')
                        || (c == java.awt.event.KeyEvent.VK_BACK_SPACE)
                        || (c == java.awt.event.KeyEvent.VK_DELETE))) {

                    e.consume();
                }
            }
        });
    }//GEN-LAST:event_txtNIdKeyPressed

    private void txrPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txrPasswordKeyPressed


    }//GEN-LAST:event_txrPasswordKeyPressed

    private void btnEventAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEventAddActionPerformed
        jpEventAdd.setVisible(true);
        jpAddStaff.setVisible(false);
        jpMannageStaff.setVisible(false);
        jpResourcesAdd.setVisible(false);
        jpSuppliers.setVisible(false);
    }//GEN-LAST:event_btnEventAddActionPerformed

    private void btnInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInventoryActionPerformed
        jpResourcesAdd.setVisible(true);
        jpEventAdd.setVisible(false);
        jpAddStaff.setVisible(false);
        jpMannageStaff.setVisible(false);
        jpSuppliers.setVisible(false);
    }//GEN-LAST:event_btnInventoryActionPerformed

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        try {
            loadItemTable();

            String searchString = txtSearch.getText();

            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();

            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
            itemTable.setRowSorter(tr);

            tr.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnItemAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAddActionPerformed
        try {
            String itemId = generateItemId();
            String itemName = txtItemName.getText().trim();
            String itemType = txtItemType.getSelectedItem() != null ? txtItemType.getSelectedItem().toString() : "";
            String itemPrice = txtItemPrice.getText().trim();
            int qty = (Integer) txtItemQty.getValue();

            if (qty <= 0) {
                JOptionPane.showMessageDialog(rootPane, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (itemId.isEmpty() || itemName.isEmpty() || itemType.isEmpty() || itemPrice.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO resources(resource_id, resource_name, resource_type, cost_per_item, stock_qty) VALUES(?,?,?,?,?)";
            pst = con.prepareStatement(sql);

            pst.setString(1, itemId);
            pst.setString(2, itemName);
            pst.setString(3, itemType);
            pst.setString(4, itemPrice);
            pst.setInt(5, qty);

            pst.executeUpdate();

            loadItemTable();
            lblNextItemID.setText(generateItemId());
            txtItemName.setText("");
            txtItemPrice.setText("");
            txtItemQty.setValue(0);

        } catch (java.sql.SQLException ex) {
            java.util.logging.Logger.getLogger(Item_Management.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnItemAddActionPerformed

    private void btnItemCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemCancelActionPerformed
        visible_btn();
        lblNameId.setText("Next resources ID");
        lblNextItemID.setText(generateItemId());
        btnItemAdd.setVisible(true);
        txtItemName.setText("");
        txtItemType.setSelectedItem("");
        txtItemPrice.setText("");
        txtItemQty.setValue(0);
        lblNameId.setForeground(new Color(242, 242, 242));
        lblNextItemID.setForeground(new Color(242, 242, 242));
    }//GEN-LAST:event_btnItemCancelActionPerformed

    private void btnItemUpadateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemUpadateActionPerformed
        try {
            String itemId, itemName, itemType, itemPrice;
            itemId = lblNextItemID.getText();
            itemName = txtItemName.getText();
            itemType = txtItemType.getSelectedItem().toString();
            itemPrice = txtItemPrice.getText();
            int qty = (Integer) txtItemQty.getValue();

            if (qty <= 0) {
                JOptionPane.showMessageDialog(rootPane, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (itemId.isEmpty() || itemName.isEmpty() || itemType.isEmpty() || itemPrice.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Please Empty Fill Not Reqrerd");
                return;
            }
            String sql = "UPDATE resources SET resource_name=?, resource_type=?, cost_per_item=?,stock_qty=? WHERE resource_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, itemName);
            pst.setString(2, itemType);
            pst.setString(3, itemPrice);
            pst.setInt(4, qty);
            pst.setString(5, itemId);

            pst.executeUpdate();
            loadItemTable();
            txtItemName.setText("");
            txtItemPrice.setText("");
            txtItemQty.setValue(0);
            btnItemCancel.doClick();
        } catch (SQLException ex) {
            System.getLogger(Admin_Management.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }//GEN-LAST:event_btnItemUpadateActionPerformed

    private void btnItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDeleteActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Do you want to delete", "Delete", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try {

                String itemId = lblNextItemID.getText();
                // Database Update
                String sql = "DELETE FROM resources WHERE resource_id=? ";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, itemId);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Client Delete Successfully!");

                loadItemTable();

                btnItemCancel.doClick();

            } catch (SQLException ex) {
                System.getLogger(client_Details.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }
    }//GEN-LAST:event_btnItemDeleteActionPerformed

    private void itemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableMouseClicked
        int rowIndex = itemTable.getSelectedRow();

        DefaultTableModel iModel = (DefaultTableModel) itemTable.getModel();

        String item_id = iModel.getValueAt(rowIndex, 0).toString();
        String item_name = iModel.getValueAt(rowIndex, 1).toString();
        String item_type = iModel.getValueAt(rowIndex, 2).toString();
        String item_price = iModel.getValueAt(rowIndex, 3).toString();
        int item_qty = (int) iModel.getValueAt(rowIndex, 4);

        lblNameId.setText("Update resources ID");
        lblNameId.setForeground(Color.YELLOW);

        lblNextItemID.setText(item_id);
        lblNextItemID.setForeground(Color.red);

        txtItemName.setText(item_name);
        txtItemType.setSelectedItem(item_type);
        txtItemPrice.setText(item_price);
        txtItemQty.setValue(item_qty);
        btnItemAdd.setVisible(false);
        btnItemUpadate.setVisible(true);
        btnItemDelete.setVisible(true);
        btnItemCancel.setVisible(true);

    }//GEN-LAST:event_itemTableMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        Package_add pa = new Package_add(this);
        pa.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPackageSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPackageSearchKeyPressed
        try {

            String searchString = txtPackageSearch.getText();

            DefaultTableModel model = (DefaultTableModel) pakageTable.getModel();

            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
            pakageTable.setRowSorter(tr);

            tr.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtPackageSearchKeyPressed

    private void btnItemManageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemManageActionPerformed
        Item_Management iM = new Item_Management(package_Id);
        iM.setVisible(true);
    }//GEN-LAST:event_btnItemManageActionPerformed

    private void pakageTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pakageTableMouseClicked
        int rowIndex = pakageTable.getSelectedRow();
        if (rowIndex < 0) return;

        DefaultTableModel iModel = (DefaultTableModel) pakageTable.getModel();

        String package_id = iModel.getValueAt(rowIndex, 0).toString();

        this.package_Id = package_id;

        btnItemManage.setVisible(true);
        btnPackageUpdate.setVisible(true);
    }//GEN-LAST:event_pakageTableMouseClicked

    /** Called when btnPackageUpdate is clicked — asks: Update / Delete / Cancel */
    private void btnPackageUpdateClicked() {
        int rowIndex = pakageTable.getSelectedRow();
        if (rowIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a package row first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel m = (DefaultTableModel) pakageTable.getModel();
        String pkgId   = safeStr(m.getValueAt(rowIndex, 0));
        String pkgName = safeStr(m.getValueAt(rowIndex, 1));
        String pkgDesc = safeStr(m.getValueAt(rowIndex, 2));
        String pkgPrice= safeStr(m.getValueAt(rowIndex, 3));

        String[] options = {"Update", "Delete", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Package: " + pkgName + "  (" + pkgId + ")\nChoose an action:",
                "Package Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {   // Update
            Package_add pa = new Package_add(this);
            pa.fillForUpdate(pkgId, pkgName, pkgDesc, pkgPrice);
            pa.setVisible(true);
        } else if (choice == 1) {  // Delete
            deletePackage(pkgId);
        }
        // choice == 2 or closed → Cancel — do nothing
    }

    /** Deletes a package after YES/NO confirmation, then refreshes the table. */
    private void deletePackage(String pkgId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete package: " + pkgId + "?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String sql = "DELETE FROM `package` WHERE package_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pkgId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Package " + pkgId + " deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                refreshPackageTable();
                btnItemManage.setVisible(false);
                btnPackageUpdate.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtPackageSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPackageSearchMouseClicked
        btnItemManage.setVisible(false);
        btnPackageUpdate.setVisible(false);
        pakageTable.clearSelection();
    }//GEN-LAST:event_txtPackageSearchMouseClicked

    private void txtRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoleActionPerformed

    }//GEN-LAST:event_txtRoleActionPerformed

    private void txtRoleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRoleFocusLost
        txrPassword.setFocusable(true);
    }//GEN-LAST:event_txtRoleFocusLost

    private void txtRoleInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtRoleInputMethodTextChanged
        txrPassword.setFocusable(true);
    }//GEN-LAST:event_txtRoleInputMethodTextChanged

    private void txtRolePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_txtRolePopupMenuWillBecomeInvisible
        txrPassword.requestFocus();
    }//GEN-LAST:event_txtRolePopupMenuWillBecomeInvisible

    private void btnSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuppliersActionPerformed
        jpSuppliers.setVisible(true);
        jpResourcesAdd.setVisible(false);
        jpEventAdd.setVisible(false);
        jpAddStaff.setVisible(false);
        jpMannageStaff.setVisible(false);
        loadSupplierTable();
    }//GEN-LAST:event_btnSuppliersActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Suppliers_Management SM = new Suppliers_Management(this);
        SM.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tbSuppliersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSuppliersMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbSuppliersMouseClicked

    private void txtSupSearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSupSearchMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSupSearchMouseEntered

    private void txtSupSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupSearchKeyReleased
        loadSupplierTable();
        try {
            loadItemTable();

            String searchString = txtSupSearch.getText();

            DefaultTableModel model = (DefaultTableModel) tbSuppliers.getModel();

            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
            tbSuppliers.setRowSorter(tr);

            tr.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
    }//GEN-LAST:event_txtSupSearchKeyReleased

    private void txtStaffSearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStaffSearchKeyPressed
       try {
            loadItemTable();

            String searchString = txtStaffSearch.getText();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(model);
            jTable1.setRowSorter(tr);

            tr.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
           
    }//GEN-LAST:event_txtStaffSearchKeyPressed

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
        java.awt.EventQueue.invokeLater(() -> new Admin_Management().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox CheckPassword;
    private javax.swing.JButton btnAddStaff;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnEventAdd;
    private javax.swing.JButton btnInventory;
    private javax.swing.JButton btnItemAdd;
    private javax.swing.JButton btnItemCancel;
    private javax.swing.JButton btnItemDelete;
    private javax.swing.JButton btnItemManage;
    private javax.swing.JButton btnItemUpadate;
    private javax.swing.JButton btnMannage;
    private javax.swing.JButton btnPackageUpdate;
    private javax.swing.JButton btnSuppliers;
    private javax.swing.JPanel header;
    private javax.swing.JTable itemTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel jpAddStaff;
    private javax.swing.JPanel jpEventAdd;
    private javax.swing.JPanel jpMannageStaff;
    private javax.swing.JPanel jpResourcesAdd;
    private javax.swing.JPanel jpSuppliers;
    private javax.swing.JLabel lblNameId;
    private javax.swing.JLabel lblNextId;
    private javax.swing.JLabel lblNextItemID;
    private javax.swing.JPanel main;
    public javax.swing.JTable pakageTable;
    private javax.swing.JTable tbSuppliers;
    private javax.swing.JTextField txrPassword;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrice;
    private javax.swing.JSpinner txtItemQty;
    private javax.swing.JComboBox<String> txtItemType;
    private javax.swing.JTextField txtNId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPackageSearch;
    private javax.swing.JLabel txtPassword;
    private javax.swing.JPasswordField txtPasswordConfirm;
    private javax.swing.JComboBox<String> txtRole;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStaffSearch;
    private javax.swing.JTextField txtSupSearch;
    // End of variables declaration//GEN-END:variables

    private String genarateUserId() {
        String newId = "S-0001";  // default ID
        try {
            // MAX()
            String sql = "SELECT MAX(staff_id) AS max_id FROM staff";
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

    private boolean checkUsername(String email, double id) {
        try {
            String msg = "select * from staff where staff_email=? OR Id=?";
            pst = con.prepareStatement(msg);
            pst.setString(1, email);
            pst.setDouble(2, id);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    private String encryptMyPassword(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Secure hashing
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    private String generateItemId() {
        String newId = "R-001";  // default ID
        try {
            // MAX()
            String sql = "SELECT MAX(resource_id) AS max_id FROM resources";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            // Data  null 
            if (rs.next() && rs.getString("max_id") != null) {
                String lastId = rs.getString("max_id"); //ex : "R-001"

                // "R-" (substring(2))
                int num = Integer.parseInt(lastId.substring(2));

                num++; // (1 -> 2)

                //"R-" 
                newId = String.format("R-%03d", num); //  "R-002"
            }
        } catch (SQLException ex) {
            //  'manage_Resources' 
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newId;
    }

    private void loadItemTable() {
        try {

            ResultSet rs = pst.executeQuery("SELECT * FROM resources");

            DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                String item_id = rs.getString("resource_id");
                String item_name = rs.getString("resource_name");
                String item_type = rs.getString("resource_type");
                String item_price = rs.getString("cost_per_item");
                int qty = rs.getInt("stock_qty");

                model.addRow(new Object[]{item_id, item_name, item_type, item_price, qty});
            }
        } catch (SQLException ex) {
            System.getLogger(client_Details.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    private void visible_btn() {
        btnItemUpadate.setVisible(false);
        btnItemDelete.setVisible(false);
        btnItemCancel.setVisible(false);
        btnItemManage.setVisible(false);
        btnPackageUpdate.setVisible(false);
    }

    public void refreshPackageTable() {
        try {
            java.sql.Connection con = DBConnect.connect();
            java.sql.Statement s = con.createStatement();
            java.sql.ResultSet rs = s.executeQuery("SELECT * FROM package");

            // pTable 
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) pakageTable.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("package_id"),
                    rs.getString("package_name"),
                    rs.getString("description"),
                    rs.getString("price")
                });
            }
        } catch (Exception ex) {
            System.out.println("Table Load Error: " + ex.getMessage());
        }
    }

    /**
     * Loads all supplier records from the 'suppliers' table into tbSuppliers.
     * Columns displayed: Supplier ID | Name | Contact | NIC | Address |
     *                    Vehicle Model | Vehicle No | Vehicle Price | Status
     */
    public void loadSupplierTable() {
        try {
            String sql = "SELECT sup_id, sup_name, contact_number, nic, "
                    + "sup_address, vehicle_modal, vehicle_no, vehicle_Price, Status "
                    + "FROM suppliers ORDER BY sup_id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rsSup = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tbSuppliers.getModel();
            model.setRowCount(0); // clear existing rows

            while (rsSup.next()) {
                model.addRow(new Object[]{
                    rsSup.getString("sup_id"),
                    rsSup.getString("sup_name"),
                    rsSup.getString("contact_number"),
                    rsSup.getString("nic"),
                    rsSup.getString("sup_address"),
                    rsSup.getString("vehicle_modal"),
                    rsSup.getString("vehicle_no"),
                    rsSup.getString("vehicle_Price"),
                    rsSup.getString("Status")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Failed to load supplier data: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Safely converts a table cell Object to a non-null String. */
    private String safeStr(Object val) {
        return val == null ? "" : val.toString();
    }

    // ================================================================
    //  STAFF TABLE  –  load, double-click, history, reset, delete
    // ================================================================

    /** Loads all staff records into jTable1. */
    public void loadStaffTable() {
        try {
            String sql = "SELECT staff_id, staff_name, contact_number, staff_email, role FROM staff ORDER BY staff_id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rsStaff = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rsStaff.next()) {
                model.addRow(new Object[]{
                    rsStaff.getString("staff_id"),
                    rsStaff.getString("staff_name"),
                    rsStaff.getString("contact_number"),
                    rsStaff.getString("staff_email"),
                    rsStaff.getString("role")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Failed to load staff data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows an action-choice dialog when a staff row is double-clicked.
     * Options: Log History | Reset Password | Delete Account | Cancel
     */
    private void onStaffRowDoubleClick(String staffId, String staffName) {
        String[] options = {"Log History", "Reset Password", "Delete Account", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Staff: " + staffName + "  (" + staffId + ")\nChoose an action:",
                "Staff Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0 -> showStaffLogHistory(staffId, staffName);
            case 1 -> resetStaffPassword(staffId);
            case 2 -> deleteStaffAccount(staffId);
            default -> { /* Cancel – do nothing */ }
        }
    }

    /**
     * Opens the staff_Log_History window for the given staff member.
     * Delegates all DB querying and display to the dedicated form.
     */
    private void showStaffLogHistory(String staffId, String staffName) {
        staff_Log_History historyWindow = new staff_Log_History(staffId, staffName);
        historyWindow.setVisible(true);
    }

    /**
     * Resets the staff member's password to their staff_id (encrypted),
     * and sets first_time_log = 0 so they must change it on next login.
     * Shows a confirmation dialog before proceeding.
     */
    private void resetStaffPassword(String staffId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Reset password for " + staffId + "?\n"
                + "The new password will be set to their Staff ID.\n"
                + "They will be required to change it on next login.",
                "Confirm Password Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String newEncrypted = encryptMyPassword(staffId);

            String sql = "UPDATE staff SET password = ?, first_time_log = 0 WHERE staff_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newEncrypted);
            ps.setString(2, staffId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Password reset successfully!\nNew password = Staff ID: " + staffId + "\nThe staff member must change it on next login.",
                        "Password Reset", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Password reset failed. Staff ID not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the staff account after a YES/NO confirmation.
     * Refreshes jTable1 after a successful delete.
     */
    private void deleteStaffAccount(String staffId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to DELETE account: " + staffId + "?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String sql = "DELETE FROM staff WHERE staff_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, staffId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this,
                        "Staff account " + staffId + " deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                loadStaffTable(); // refresh table
            } else {
                JOptionPane.showMessageDialog(this,
                        "Delete failed. Staff ID not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin_Management.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
