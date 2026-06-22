/*
 * Quotation Form – Event Management System
 * Displays a professional quotation for a client based on billing data.
 */
package event_management_system;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Quotation window – opened from Billing_and_Cost with pre-filled data.
 * @author chamika
 */
public class quotation extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(quotation.class.getName());

    // Data passed in from Billing_and_Cost
    private String eventId      = "";
    private String clientName   = "";
    private String packageName  = "";
    private String basePrice    = "";
    private String extraCost    = "";
    private String grandTotal   = "";
    private String advancePaid  = "";
    private String dueBalance   = "";

    /** Default constructor (design-time only). */
    public quotation() {
        initComponents();
        applyStyles();
    }

    /**
     * Constructor called from Billing_and_Cost with live billing data.
     */
    public quotation(String eventId, String clientName, String packageName,
                     String basePrice, String extraCost, String grandTotal,
                     String advancePaid, String dueBalance) {
        this.eventId     = eventId;
        this.clientName  = clientName;
        this.packageName = packageName;
        this.basePrice   = basePrice;
        this.extraCost   = extraCost;
        this.grandTotal  = grandTotal;
        this.advancePaid = advancePaid;
        this.dueBalance  = dueBalance;

        initComponents();
        applyStyles();
        populateQuotation();
    }

    // ─── Style pass ──────────────────────────────────────────────────────────
    private void applyStyles() {
        // Window
        getContentPane().setBackground(new Color(14, 14, 26));
        setTitle("Quotation – Celestial Events");

        // Header panel
        pnlHeader.setBackground(new Color(24, 24, 42));

        // Labels
        lblCompany.setForeground(new Color(130, 170, 255));
        lblCompany.setFont(new Font("Segoe UI", Font.BOLD, 22));

        lblTagline.setForeground(new Color(150, 150, 200));
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblTitle.setForeground(new Color(255, 200, 80));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblQuoteNo.setForeground(new Color(180, 180, 220));
        lblQuoteNo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblDate.setForeground(new Color(180, 180, 220));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Content area
        txtQuoteArea.setBackground(new Color(18, 18, 32));
        txtQuoteArea.setForeground(new Color(210, 230, 210));
        txtQuoteArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtQuoteArea.setEditable(false);

        jScrollPane1.setBackground(new Color(18, 18, 32));
        jScrollPane1.getViewport().setBackground(new Color(18, 18, 32));

        // Buttons
        btnPrintQuote.setBackground(new Color(50, 80, 150));
        btnPrintQuote.setForeground(Color.WHITE);
        btnPrintQuote.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPrintQuote.setFocusPainted(false);
        btnPrintQuote.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnShareWA.setBackground(new Color(37, 150, 70));
        btnShareWA.setForeground(Color.WHITE);
        btnShareWA.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShareWA.setFocusPainted(false);
        btnShareWA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnShareEmail.setBackground(new Color(160, 80, 30));
        btnShareEmail.setForeground(Color.WHITE);
        btnShareEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShareEmail.setFocusPainted(false);
        btnShareEmail.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnSavePDF.setBackground(new Color(180, 30, 80));
        btnSavePDF.setForeground(Color.WHITE);
        btnSavePDF.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSavePDF.setFocusPainted(false);
        btnSavePDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnClose.setBackground(new Color(90, 30, 30));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }

    // ─── Populate quotation text ─────────────────────────────────────────────
    private void populateQuotation() {
        String dateStr   = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        String quoteNo   = "QT-" + eventId.replace("E-", "");
        String validTill = new SimpleDateFormat("dd MMMM yyyy").format(
                new Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000));
        DecimalFormat df = new DecimalFormat("#,##0.00");

        lblQuoteNo.setText("Quotation No: " + quoteNo);
        lblDate.setText("Date: " + dateStr);

        String line  = "-".repeat(64);
        String dline = "=".repeat(64);

        // ── Fetch resources from DB ────────────────────────────────────────
        // Package resources (package_id NOT NULL)
        List<String[]> pkgResources   = new ArrayList<>();
        // Extra/additional resources (package_id IS NULL)
        List<String[]> extraResources = new ArrayList<>();

        if (!eventId.isEmpty()) {
            String sql =
                "SELECT r.resource_name, r.resource_type, er.quantity, " +
                "       r.cost_per_item, er.total_cost, er.package_id " +
                "FROM event_resources er " +
                "JOIN resources r ON er.resource_id = r.resource_id " +
                "WHERE er.event_id = ? " +
                "ORDER BY er.package_id IS NULL, r.resource_type, r.resource_name";
            try (Connection conn = DBConnect.connect();
                 PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, eventId);
                try (ResultSet rs2 = st.executeQuery()) {
                    while (rs2.next()) {
                        String[] row = {
                            rs2.getString("resource_name"),
                            rs2.getString("resource_type"),
                            String.valueOf(rs2.getInt("quantity")),
                            df.format(rs2.getDouble("cost_per_item")),
                            df.format(rs2.getDouble("total_cost"))
                        };
                        if (rs2.getString("package_id") != null) {
                            pkgResources.add(row);
                        } else {
                            extraResources.add(row);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.log(java.util.logging.Level.WARNING, "Could not load resources", ex);
            }
        }

        // ── Build quotation text ───────────────────────────────────────────
        StringBuilder q = new StringBuilder();
        q.append("\n");
        q.append(dline).append("\n");
        q.append("           CELESTIAL EVENTS\n");
        q.append("      Premium Event Management Services\n");
        q.append("   Contact: +94 71 000 0000 | events@cel.lk\n");
        q.append(dline).append("\n\n");

        // Quotation info
        q.append("  QUOTATION\n");
        q.append("  ").append(line).append("\n");
        q.append(String.format("  Quotation No  : %s%n", quoteNo));
        q.append(String.format("  Date          : %s%n", dateStr));
        q.append(String.format("  Valid Until   : %s%n", validTill));
        q.append(String.format("  Booking ID    : %s%n", eventId));
        q.append("  ").append(line).append("\n\n");

        // Client
        q.append("  PREPARED FOR\n");
        q.append("  ").append(line).append("\n");
        q.append(String.format("  Client Name   : %s%n", clientName));
        q.append("  ").append(line).append("\n\n");

        // Package resources table
        q.append("  PACKAGE RESOURCES  (included in base package)\n");
        q.append("  ").append(line).append("\n");
        q.append(String.format("  %-28s %-12s %5s  %12s  %12s%n",
                "Resource", "Type", "Qty", "Unit Price", "Total"));
        q.append("  ").append(line).append("\n");
        if (pkgResources.isEmpty()) {
            q.append("  (No package resources assigned)\n");
        } else {
            for (String[] r : pkgResources) {
                q.append(String.format("  %-28s %-12s %5s  %12s  %12s%n",
                        truncate(r[0], 28), truncate(r[1], 12), r[2], r[3], r[4]));
            }
        }
        q.append("  ").append(line).append("\n");
        q.append(String.format("  %-54s  %12s%n", "Base Package Price", basePrice));
        q.append("  ").append(line).append("\n\n");

        // Extra resources table
        q.append("  ADDITIONAL / EXTRA RESOURCES\n");
        q.append("  ").append(line).append("\n");
        q.append(String.format("  %-28s %-12s %5s  %12s  %12s%n",
                "Resource", "Type", "Qty", "Unit Price", "Total"));
        q.append("  ").append(line).append("\n");
        if (extraResources.isEmpty()) {
            q.append("  (No additional resources)\n");
        } else {
            for (String[] r : extraResources) {
                q.append(String.format("  %-28s %-12s %5s  %12s  %12s%n",
                        truncate(r[0], 28), truncate(r[1], 12), r[2], r[3], r[4]));
            }
        }
        q.append("  ").append(line).append("\n");
        q.append(String.format("  %-54s  %12s%n", "Extra Cost Total", extraCost));
        q.append("  ").append(line).append("\n\n");

        // Billing summary
        q.append("  BILLING SUMMARY\n");
        q.append("  ").append(line).append("\n");
        q.append(String.format("  %-54s  %12s%n", "Grand Total", grandTotal));
        q.append(String.format("  %-54s  %12s%n", "Advance Paid", advancePaid));
        q.append(String.format("  %-54s  %12s%n", "Balance Due", dueBalance));
        q.append("  ").append(line).append("\n\n");

        // Terms
        q.append("  TERMS & CONDITIONS\n");
        q.append("  ").append(line).append("\n");
        q.append("  1. This quotation is valid for 7 days from the date above.\n");
        q.append("  2. A minimum 30% advance is required to confirm the booking.\n");
        q.append("  3. Balance payment is due on or before the event date.\n");
        q.append("  4. Cancellation within 48 hours of the event is non-refundable.\n");
        q.append("  5. Prices are inclusive of all applicable taxes.\n\n");

        q.append(dline).append("\n");
        q.append("    Thank you for choosing Celestial Events!\n");
        q.append(dline).append("\n");

        txtQuoteArea.setText(q.toString());
        txtQuoteArea.setCaretPosition(0);
    }

    /** Truncate a string to maxLen chars, pad with spaces. */
    private static String truncate(String s, int maxLen) {
        if (s == null) s = "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + ".";
    }

    // ─── Button handlers ─────────────────────────────────────────────────────
    private void btnPrintQuoteActionPerformed(java.awt.event.ActionEvent evt) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Quotation – " + clientName);
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g2.setFont(new Font("Courier New", Font.PLAIN, 9));
            g2.setColor(Color.BLACK);
            String[] lines = txtQuoteArea.getText().split("\n");
            int y = 14;
            for (String line : lines) {
                g2.drawString(line, 0, y);
                y += 14;
                if (y > pageFormat.getImageableHeight()) break;
            }
            return Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) {
            try { job.print(); }
            catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Print error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnShareWAActionPerformed(java.awt.event.ActionEvent evt) {
        String phone = JOptionPane.showInputDialog(this,
                "Enter WhatsApp number (with country code, e.g. 94711234567):",
                "Share via WhatsApp", JOptionPane.QUESTION_MESSAGE);
        if (phone == null || phone.trim().isEmpty()) return;
        phone = phone.trim().replaceAll("[^0-9]", "");
        try {
            String text = txtQuoteArea.getText();
            String encoded = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            Desktop.getDesktop().browse(new URI(
                    "https://web.whatsapp.com/send?phone=" + phone + "&text=" + encoded));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open WhatsApp: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnShareEmailActionPerformed(java.awt.event.ActionEvent evt) {
        String email = JOptionPane.showInputDialog(this,
                "Enter client email address:",
                "Share via Email", JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty()) return;
        try {
            String subject  = URLEncoder.encode("Quotation – Celestial Events – " + clientName,
                    StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String body     = URLEncoder.encode(txtQuoteArea.getText(),
                    StandardCharsets.UTF_8.toString()).replace("+", "%20");
            Desktop.getDesktop().mail(new URI("mailto:" + email + "?subject=" + subject + "&body=" + body));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not open mail client: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // ─── Save as PDF (HTML in browser) ───────────────────────────────────────
    private void btnSavePDFActionPerformed(java.awt.event.ActionEvent evt) {
        QuotationPDFGenerator.generateAndOpenPDF(eventId, clientName, packageName, basePrice, extraCost, advancePaid, dueBalance);
    }

    /** Escape HTML special characters. */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ─── initComponents ──────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblCompany = new javax.swing.JLabel();
        lblTagline = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblQuoteNo = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtQuoteArea = new javax.swing.JTextArea();
        pnlButtons = new javax.swing.JPanel();
        btnPrintQuote = new javax.swing.JButton();
        btnShareWA = new javax.swing.JButton();
        btnShareEmail = new javax.swing.JButton();
        btnSavePDF = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quotation");
        setMinimumSize(new java.awt.Dimension(760, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlHeader.setBackground(new java.awt.Color(24, 24, 42));
        pnlHeader.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCompany.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblCompany.setForeground(new java.awt.Color(130, 170, 255));
        lblCompany.setText("CELESTIAL EVENTS");
        pnlHeader.add(lblCompany, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 320, 34));

        lblTagline.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblTagline.setForeground(new java.awt.Color(150, 150, 200));
        lblTagline.setText("Premium Event Management Services");
        pnlHeader.add(lblTagline, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 280, 18));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 200, 80));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle.setText("QUOTATION");
        pnlHeader.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 12, 280, 30));

        lblQuoteNo.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblQuoteNo.setForeground(new java.awt.Color(180, 180, 220));
        lblQuoteNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQuoteNo.setText("Quotation No: --");
        pnlHeader.add(lblQuoteNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 44, 280, 16));

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDate.setForeground(new java.awt.Color(180, 180, 220));
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDate.setText("Date: --");
        pnlHeader.add(lblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 62, 280, 16));

        getContentPane().add(pnlHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 88));

        txtQuoteArea.setColumns(20);
        txtQuoteArea.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        txtQuoteArea.setRows(5);
        txtQuoteArea.setEditable(false);
        jScrollPane1.setViewportView(txtQuoteArea);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 96, 740, 534));

        pnlButtons.setBackground(new java.awt.Color(20, 20, 38));
        pnlButtons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnPrintQuote.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPrintQuote.setText("Print");
        btnPrintQuote.addActionListener(this::btnPrintQuoteActionPerformed);
        pnlButtons.add(btnPrintQuote, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 130, 36));

        btnShareWA.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnShareWA.setText("WhatsApp");
        btnShareWA.addActionListener(this::btnShareWAActionPerformed);
        pnlButtons.add(btnShareWA, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 8, 150, 36));

        btnShareEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnShareEmail.setText("Email");
        btnShareEmail.addActionListener(this::btnShareEmailActionPerformed);
        pnlButtons.add(btnShareEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 8, 130, 36));

        btnSavePDF.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSavePDF.setText("Save as PDF");
        btnSavePDF.addActionListener(this::btnSavePDFActionPerformed);
        pnlButtons.add(btnSavePDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 8, 130, 36));

        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(this::btnCloseActionPerformed);
        pnlButtons.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 8, 130, 36));

        getContentPane().add(pnlButtons, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 760, 56));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        java.awt.EventQueue.invokeLater(() -> new quotation().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPrintQuote;
    private javax.swing.JButton btnSavePDF;
    private javax.swing.JButton btnShareEmail;
    private javax.swing.JButton btnShareWA;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCompany;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblQuoteNo;
    private javax.swing.JLabel lblTagline;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JTextArea txtQuoteArea;
    // End of variables declaration//GEN-END:variables
}
