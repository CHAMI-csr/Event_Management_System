/*
 * First Time Login — Force Password Reset Dialog
 */
package event_management_system;

import java.awt.Color;
import java.awt.Font;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Modal dialog shown on first login to force the user to set a new password.
 * @author chamika
 */
public class First_time_log extends javax.swing.JDialog {

    private static final Logger logger = Logger.getLogger(First_time_log.class.getName());

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG_DEEP    = new Color(14,  14,  22);
    private static final Color BG_PANEL   = new Color(24,  24,  38);
    private static final Color BG_INPUT   = new Color(38,  38,  60);
    private static final Color ACCENT     = new Color(90, 120, 240);
    private static final Color BTN_OK     = new Color(63,  84, 186);
    private static final Color BTN_CANCEL = new Color(100, 40,  40);
    private static final Color FG_WHITE   = new Color(230, 230, 255);
    private static final Color FG_MUTED   = new Color(140, 140, 175);
    private static final Color BORDER_COL = new Color(55,  55,  85);
    private static final Color WARN_RED   = new Color(220, 60,  60);

    // ── State ─────────────────────────────────────────────────────────────────
    private boolean passwordChangedSuccessfully = false;
    private final Staff staff;

    // ─────────────────────────────────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────────────────────────────────
    public First_time_log(java.awt.Frame parent, Staff staff) {
        super(parent, true);
        this.staff = staff;
        initComponents();
        applyDarkStyling();
        wireDocumentListeners();
        getRootPane().setDefaultButton(btnChange);
        setLocationRelativeTo(parent);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Generated Code (NetBeans Form)
    // ─────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        lblNewPw = new javax.swing.JLabel();
        txtNewPw = new javax.swing.JPasswordField();
        lblStrength = new javax.swing.JLabel();
        lblConfirm = new javax.swing.JLabel();
        txtConfirmPw = new javax.swing.JPasswordField();
        lblMatchStatus = new javax.swing.JLabel();
        chkShow = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnChange = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("First Login — Set Your Password");

        pnlMain.setBackground(new java.awt.Color(24, 24, 38));
        pnlMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlMain.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(230, 230, 255));
        lblTitle.setText("🔒  Set Your New Password");
        pnlMain.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 440, 36));

        lblSubtitle.setForeground(new java.awt.Color(140, 140, 175));
        lblSubtitle.setText("For your security, please set a new password before continuing.");
        pnlMain.add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 68, 440, 20));

        lblNewPw.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNewPw.setForeground(new java.awt.Color(230, 230, 255));
        lblNewPw.setText("New Password");
        pnlMain.add(lblNewPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 108, 440, 22));

        txtNewPw.setBackground(new java.awt.Color(38, 38, 60));
        txtNewPw.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNewPw.setForeground(new java.awt.Color(230, 230, 255));
        txtNewPw.setCaretColor(new java.awt.Color(230, 230, 255));
        pnlMain.add(txtNewPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 134, 440, 42));

        lblStrength.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblStrength.setForeground(new java.awt.Color(140, 140, 175));
        pnlMain.add(lblStrength, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 440, 18));

        lblConfirm.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblConfirm.setForeground(new java.awt.Color(230, 230, 255));
        lblConfirm.setText("Confirm Password");
        pnlMain.add(lblConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 208, 440, 22));

        txtConfirmPw.setBackground(new java.awt.Color(38, 38, 60));
        txtConfirmPw.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtConfirmPw.setForeground(new java.awt.Color(230, 230, 255));
        txtConfirmPw.setCaretColor(new java.awt.Color(230, 230, 255));
        pnlMain.add(txtConfirmPw, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 234, 440, 42));

        lblMatchStatus.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblMatchStatus.setForeground(new java.awt.Color(140, 140, 175));
        pnlMain.add(lblMatchStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 440, 18));

        chkShow.setBackground(new java.awt.Color(24, 24, 38));
        chkShow.setForeground(new java.awt.Color(140, 140, 175));
        chkShow.setText("Show passwords");
        chkShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShowActionPerformed(evt);
            }
        });
        pnlMain.add(chkShow, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 308, 200, 26));

        btnCancel.setBackground(new java.awt.Color(100, 40, 40));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setText("Cancel");
        btnCancel.setBorderPainted(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        pnlMain.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 206, 42));

        btnChange.setBackground(new java.awt.Color(63, 84, 186));
        btnChange.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnChange.setForeground(new java.awt.Color(255, 255, 255));
        btnChange.setText("Set Password ✓");
        btnChange.setBorderPainted(false);
        btnChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeActionPerformed(evt);
            }
        });
        pnlMain.add(btnChange, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 350, 218, 42));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowActionPerformed
        boolean show = chkShow.isSelected();
        txtNewPw.setEchoChar(show ? (char) 0 : '\u2022');
        txtConfirmPw.setEchoChar(show ? (char) 0 : '\u2022');
    }//GEN-LAST:event_chkShowActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        confirmCancel();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeActionPerformed
        handlePasswordChange();
    }//GEN-LAST:event_btnChangeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        confirmCancel();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnChange;
    private javax.swing.JCheckBox chkShow;
    private javax.swing.JLabel lblConfirm;
    private javax.swing.JLabel lblMatchStatus;
    private javax.swing.JLabel lblNewPw;
    private javax.swing.JLabel lblStrength;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPasswordField txtConfirmPw;
    private javax.swing.JPasswordField txtNewPw;
    // End of variables declaration//GEN-END:variables

    // ─────────────────────────────────────────────────────────────────────────
    //  Dark UI styling (runs after initComponents)
    // ─────────────────────────────────────────────────────────────────────────
    private void applyDarkStyling() {
        getContentPane().setBackground(BG_DEEP);
        pnlMain.setBackground(BG_PANEL);
        pnlMain.setBorder(new CompoundBorder(
            new LineBorder(ACCENT, 2, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Input border
        LineBorder inputBorder = new LineBorder(BORDER_COL, 1, true);
        txtNewPw.setBorder(new CompoundBorder(inputBorder, new EmptyBorder(4, 10, 4, 10)));
        txtConfirmPw.setBorder(new CompoundBorder(inputBorder, new EmptyBorder(4, 10, 4, 10)));

        // Button hover effects
        addHover(btnChange, BTN_OK);
        addHover(btnCancel, BTN_CANCEL);
        btnChange.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        chkShow.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    }

    private static void addHover(javax.swing.JButton btn, Color base) {
        Color bright = base.brighter();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bright); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(base);   }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Live document listeners (strength + match indicators)
    // ─────────────────────────────────────────────────────────────────────────
    private void wireDocumentListeners() {
        txtNewPw.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate (javax.swing.event.DocumentEvent e) { updateStrength(); updateMatch(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { updateStrength(); updateMatch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); updateMatch(); }
        });
        txtConfirmPw.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate (javax.swing.event.DocumentEvent e) { updateMatch(); }
            public void removeUpdate (javax.swing.event.DocumentEvent e) { updateMatch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateMatch(); }
        });
    }

    private void updateStrength() {
        String pw = new String(txtNewPw.getPassword());
        if (pw.isEmpty()) { lblStrength.setText(""); return; }
        int score = 0;
        if (pw.length() >= 8)               score++;
        if (pw.matches(".*[A-Z].*"))        score++;
        if (pw.matches(".*[0-9].*"))        score++;
        if (pw.matches(".*[^a-zA-Z0-9].*")) score++;
        switch (score) {
            case 0, 1 -> { lblStrength.setText("Strength: Weak \u25cf\u25cb\u25cb\u25cb");   lblStrength.setForeground(WARN_RED); }
            case 2    -> { lblStrength.setText("Strength: Fair \u25cf\u25cf\u25cb\u25cb");   lblStrength.setForeground(new Color(220,160,30)); }
            case 3    -> { lblStrength.setText("Strength: Good \u25cf\u25cf\u25cf\u25cb");   lblStrength.setForeground(new Color(80,180,80)); }
            default   -> { lblStrength.setText("Strength: Strong \u25cf\u25cf\u25cf\u25cf"); lblStrength.setForeground(new Color(60,200,100)); }
        }
    }

    private void updateMatch() {
        String p1 = new String(txtNewPw.getPassword());
        String p2 = new String(txtConfirmPw.getPassword());
        if (p2.isEmpty()) { lblMatchStatus.setText(""); return; }
        if (p1.equals(p2)) {
            lblMatchStatus.setText("\u2713 Passwords match");
            lblMatchStatus.setForeground(new Color(60, 200, 100));
        } else {
            lblMatchStatus.setText("\u2717 Passwords do not match");
            lblMatchStatus.setForeground(WARN_RED);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Business logic
    // ─────────────────────────────────────────────────────────────────────────
    private void confirmCancel() {
        int ans = JOptionPane.showConfirmDialog(this,
            "You must set a new password to continue.\nAre you sure you want to cancel and exit?",
            "Cancel Login", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ans == JOptionPane.YES_OPTION) {
            passwordChangedSuccessfully = false;
            dispose();
        }
    }

    private void handlePasswordChange() {
        char[] c1 = txtNewPw.getPassword();
        char[] c2 = txtConfirmPw.getPassword();
        String newPw     = new String(c1).trim();
        String confirmPw = new String(c2).trim();
        Arrays.fill(c1, ' ');
        Arrays.fill(c2, ' ');

        if (newPw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "New password cannot be empty!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNewPw.requestFocus();
            return;
        }
        if (newPw.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtNewPw.requestFocus();
            return;
        }
        if (!newPw.equals(confirmPw)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!\nPlease re-enter both fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            txtConfirmPw.setText("");
            txtConfirmPw.requestFocus();
            lblMatchStatus.setText("\u2717 Passwords do not match");
            lblMatchStatus.setForeground(WARN_RED);
            return;
        }

        try (Connection con = DBConnect.connect()) {
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String hashed = encryptPassword(newPw);
            String sql = "UPDATE staff SET password = ?, first_time_log = 1 " +
                         "WHERE (staff_id = ? OR staff_email = ? OR Id = ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, hashed);
                pst.setString(2, staff.getStaff_id());
                pst.setString(3, staff.getStaff_email());
                pst.setString(4, staff.getId());
                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Password changed successfully!\nYou will now be taken to the Dashboard.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    passwordChangedSuccessfully = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password. Please contact Admin.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Password update failed", ex);
            JOptionPane.showMessageDialog(this, "Database error:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** SHA-256 + Base64 — same algorithm used in Loging.java */
    private static String encryptPassword(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(plain.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, "SHA-256 not available", ex);
        }
        return "";
    }

    /** @return true only if the user successfully set a new password. */
    public boolean isPasswordChanged() {
        return passwordChangedSuccessfully;
    }
}
