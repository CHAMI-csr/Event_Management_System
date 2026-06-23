package event_management_system;

/*
 * Billing and Cost Management Form
 * Handles invoice generation, payment status update, and sharing.
 */
import java.awt.Desktop;
import java.io.PrintStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author chamika
 */
public class Billing_and_Cost extends javax.swing.JInternalFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(Billing_and_Cost.class.getName());
    private static final DecimalFormat MONEY_FMT = new DecimalFormat("#,##0.00");

    Connection con = DBConnect.connect();
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Creates new form Billing_and_Cost
     */
    public Billing_and_Cost() {
        initComponents();

        // Remove internal frame decorations
        UITheme.removeInternalFrameChrome(this);
        customizeUI();
        
        // Manually link the Quotation buttons (in case NetBeans UI builder dropped them)
        btnQuotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationActionPerformed(evt);
            }
        });
        btnQuotationPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationPdfActionPerformed(evt);
            }
        });
        
        // Make result fields read-only (txtExtraCost stays editable)
        txtClientName.setEditable(false);
        txtPackageName.setEditable(false);
        txtBasePrice.setEditable(false);
        txtGrandTotal.setEditable(false);
        txtAdvance.setEditable(false);
        txtDueBalance.setEditable(false);

        // Live recalc when user edits Extra Cost or New Payment manually
        java.awt.event.KeyAdapter recalcAdapter = new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recalculateTotals();
            }
        };
        txtExtraCost.addKeyListener(recalcAdapter);
        txtNewPayment.addKeyListener(recalcAdapter);

        // Load status combo
        cmbStatus.removeAllItems();
        cmbStatus.addItem("Pending");
        cmbStatus.addItem("Partial");
        cmbStatus.addItem("Paid");
        cmbStatus.addItem("Cancelled");
    }



    // =========================================================
    // HELPER – re-calculate Grand Total & Due Balance
    // =========================================================
    private void recalculateTotals() {
        try {
            // Parse base price (stored as "Rs. 1,234.00")
            String baseTxt = txtBasePrice.getText().replace("Rs.", "").replace(",", "").trim();
            double basePrice = baseTxt.isEmpty() ? 0.0 : Double.parseDouble(baseTxt);

            // Parse extra cost – user may type plain number or "Rs. x"
            String extraTxt = txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim();
            double extraCost = extraTxt.isEmpty() ? 0.0 : Double.parseDouble(extraTxt);
            
            // Parse transport cost
            String transTxt = txtTransportCost.getText().replace("Rs.", "").replace(",", "").trim();
            double transportCost = transTxt.isEmpty() ? 0.0 : Double.parseDouble(transTxt);

            // Parse advance
            String advTxt = txtAdvance.getText().replace("Rs.", "").replace(",", "").trim();
            double advance = advTxt.isEmpty() ? 0.0 : Double.parseDouble(advTxt);
            
            // Parse new payment
            String newPayTxt = txtNewPayment.getText().replace("Rs.", "").replace(",", "").trim();
            double newPayment = newPayTxt.isEmpty() ? 0.0 : Double.parseDouble(newPayTxt);

            double grandTotal = basePrice + extraCost + transportCost;
            double dueBalance = grandTotal - advance - newPayment;
            
            // Auto-select Payment Status based on due balance
            if (dueBalance <= 0 && grandTotal > 0) {
                cmbStatus.setSelectedItem("Paid");
            } else if (advance + newPayment > 0 && dueBalance > 0) {
                cmbStatus.setSelectedItem("Partial");
            }

            txtGrandTotal.setText("Rs. " + MONEY_FMT.format(grandTotal));
            txtDueBalance.setText("Rs. " + MONEY_FMT.format(dueBalance));
        } catch (NumberFormatException ex) {
            // Ignore parse errors while user is still typing
        }
    }

    // =========================================================
    // HELPER – update Grand Total with Transport Cost
    // =========================================================
    public void updateGrandTotal(String eventID) {
        if (eventID == null || eventID.trim().isEmpty()) {
            return;
        }
        
        try (Connection conn = DBConnect.connect()) {
            // 1. Get transport_cost from events table
            String sql = "SELECT transport_cost FROM events WHERE event_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, eventID);
            ResultSet rs = pst.executeQuery();
            
            double transportCost = 0.0;
            if (rs.next()) {
                transportCost = rs.getDouble("transport_cost");
            }
            rs.close();
            pst.close();

            // 2. Add to existing totals
            // Retrieve current values
            String baseTxt = txtBasePrice.getText().replace("Rs.", "").replace(",", "").trim();
            double basePrice = baseTxt.isEmpty() ? 0.0 : Double.parseDouble(baseTxt);

            String extraTxt = txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim();
            double extraCost = extraTxt.isEmpty() ? 0.0 : Double.parseDouble(extraTxt);

            String advTxt = txtAdvance.getText().replace("Rs.", "").replace(",", "").trim();
            double advance = advTxt.isEmpty() ? 0.0 : Double.parseDouble(advTxt);

            // New calculation
            double grandTotal = basePrice + extraCost + transportCost;
            double dueBalance = grandTotal - advance;

            // Update UI
            txtGrandTotal.setText("Rs. " + MONEY_FMT.format(grandTotal));
            txtDueBalance.setText("Rs. " + MONEY_FMT.format(dueBalance));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating transport cost: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    // =========================================================
    // STORY 3: Logistics - calculateFinalBill
    // =========================================================
    public void calculateFinalBill(String eventID) {
        if (eventID == null || eventID.trim().isEmpty()) {
            return;
        }
        
        double packagePrice = 0.0;
        double transportCost = 0.0;
        double totalExtraCost = 0.0;
        
        // Use JOIN to get package price since price is in package table
        String sqlEvents = "SELECT p.price AS package_price, e.transport_cost FROM events e " +
                           "LEFT JOIN package p ON e.package_id = p.package_id WHERE e.event_id = ?";
        
        String sqlExtraCost = "SELECT SUM(total_cost) as sum_extra FROM event_resources WHERE event_id = ?";
        
        try (Connection conn = DBConnect.connect()) {
            
            // Fetch Event Costs
            try (PreparedStatement pstEvents = conn.prepareStatement(sqlEvents)) {
                pstEvents.setString(1, eventID);
                try (ResultSet rsEvents = pstEvents.executeQuery()) {
                    if (rsEvents.next()) {
                        packagePrice = rsEvents.getDouble("package_price");
                        transportCost = rsEvents.getDouble("transport_cost");
                    }
                }
            }
            
            // Fetch Extra Costs
            try (PreparedStatement pstExtra = conn.prepareStatement(sqlExtraCost)) {
                pstExtra.setString(1, eventID);
                try (ResultSet rsExtra = pstExtra.executeQuery()) {
                    if (rsExtra.next()) {
                        totalExtraCost = rsExtra.getDouble("sum_extra");
                    }
                }
            }
            
            // Calculate Grand Total
            double grandTotal = packagePrice + totalExtraCost + transportCost;
            
            // Update the relevant UI components on the billing form
            txtBasePrice.setText("Rs. " + MONEY_FMT.format(packagePrice));
            txtExtraCost.setText(MONEY_FMT.format(totalExtraCost));
            txtTransportCost.setText(MONEY_FMT.format(transportCost));
            txtGrandTotal.setText("Rs. " + MONEY_FMT.format(grandTotal));
            
            // Optional: calculate due balance based on advance
            String advTxt = txtAdvance.getText().replace("Rs.", "").replace(",", "").trim();
            double advance = advTxt.isEmpty() ? 0.0 : Double.parseDouble(advTxt);
            txtDueBalance.setText("Rs. " + MONEY_FMT.format(grandTotal - advance));
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error calculating final bill: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // =========================================================
    // STORY 1 – SEARCH (btnSearch)
    // Accepts: Event ID OR Customer Contact Number
    // =========================================================
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String input = txtBookingId.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an Event ID or Customer Contact Number.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnect.connect()) {

            // ── Try direct Event ID lookup first ──────────────────────
            String resolvedEventId = tryResolveByEventId(conn, input);

            if (resolvedEventId == null) {
                // ── Treat input as contact number ─────────────────────
                resolvedEventId = resolveByContactNumber(conn, input);
            }

            if (resolvedEventId == null)
                return; // user cancelled or nothing found

            loadBillingData(conn, resolvedEventId);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    /**
     * Check if input is a valid event_id. Returns event_id or null.
     */
    private String tryResolveByEventId(Connection conn, String input) throws Exception {
        String sql = "SELECT event_id FROM events WHERE event_id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, input);
        ResultSet rs2 = st.executeQuery();
        String id = rs2.next() ? rs2.getString(1) : null;
        rs2.close();
        st.close();
        return id;
    }

    /**
     * Find client(s) by contact number, let user pick an event, return event_id.
     */
    private String resolveByContactNumber(Connection conn, String contact) throws Exception {
        // Find matching clients
        String sqlClient = "SELECT client_id, client_name FROM clients WHERE contact_number = ?";
        PreparedStatement stClient = conn.prepareStatement(sqlClient);
        stClient.setString(1, contact);
        ResultSet rsClient = stClient.executeQuery();

        java.util.List<String[]> clients = new java.util.ArrayList<>();
        while (rsClient.next()) {
            clients.add(new String[] { rsClient.getString("client_id"), rsClient.getString("client_name") });
        }
        rsClient.close();
        stClient.close();

        if (clients.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No client found with contact: " + contact,
                    "Not Found", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String clientId = clients.get(0)[0];
        String clientName = clients.get(0)[1];

        // Load all events for this client
        String sqlEvents = "SELECT e.event_id, e.event_date, e.event_type, p.package_name " +
                "FROM events e " +
                "LEFT JOIN package p ON e.package_id = p.package_id " +
                "WHERE e.client_id = ? ORDER BY e.event_date DESC";
        PreparedStatement stEvents = conn.prepareStatement(sqlEvents);
        stEvents.setString(1, clientId);
        ResultSet rsEvents = stEvents.executeQuery();

        java.util.List<String[]> events = new java.util.ArrayList<>();
        while (rsEvents.next()) {
            events.add(new String[] {
                    rsEvents.getString("event_id"),
                    rsEvents.getString("event_date") + " | " +
                            (rsEvents.getString("event_type") != null ? rsEvents.getString("event_type") : "Event") +
                            " | "
                            + (rsEvents.getString("package_name") != null ? rsEvents.getString("package_name") : "")
            });
        }
        rsEvents.close();
        stEvents.close();

        if (events.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No events found for client: " + clientName,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (events.size() == 1) {
            // Only one event – auto-select
            String eid = events.get(0)[0];
            txtBookingId.setText(eid);
            return eid;
        }

        // Multiple events – show chooser
        String[] options = events.stream().map(ev -> ev[0] + "  –  " + ev[1]).toArray(String[]::new);
        String chosen = (String) JOptionPane.showInputDialog(
                this,
                "Client: " + clientName + "\nSelect an event:",
                "Choose Event",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (chosen == null)
            return null; // cancelled

        String eventId = chosen.split("  –  ")[0].trim();
        txtBookingId.setText(eventId);
        return eventId;
    }

    /**
     * Load and display all billing data for a given event_id.
     */
    private void loadBillingData(Connection conn, String eventId) throws Exception {
        String sqlMain = "SELECT c.client_name, p.package_name, p.price, b.advance_payment, e.transport_cost " +
                "FROM events e " +
                "JOIN clients  c ON e.client_id  = c.client_id " +
                "JOIN package  p ON e.package_id  = p.package_id " +
                "LEFT JOIN billing b ON b.event_id = e.event_id " +
                "WHERE e.event_id = ?";

        PreparedStatement stmtMain = conn.prepareStatement(sqlMain);
        stmtMain.setString(1, eventId);
        ResultSet rsMain = stmtMain.executeQuery();

        if (!rsMain.next()) {
            JOptionPane.showMessageDialog(this, "No billing data for Event ID: " + eventId,
                    "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String clientName = rsMain.getString("client_name");
        String packageName = rsMain.getString("package_name");
        double basePrice = rsMain.getDouble("price");
        double advancePaid = rsMain.getDouble("advance_payment");
        double transportCost = rsMain.getDouble("transport_cost");
        rsMain.close();
        stmtMain.close();

        // Extra cost – items added outside base package
        String sqlExtra = "SELECT SUM(total_cost) AS extra_total FROM event_resources " +
                "WHERE event_id = ? AND package_id IS NULL";
        PreparedStatement stmtExtra = conn.prepareStatement(sqlExtra);
        stmtExtra.setString(1, eventId);
        ResultSet rsExtra = stmtExtra.executeQuery();
        double extraCost = rsExtra.next() ? rsExtra.getDouble("extra_total") : 0.0;
        rsExtra.close();
        stmtExtra.close();

        double grandTotal = basePrice + extraCost + transportCost;
        double dueBalance = grandTotal - advancePaid;

        txtClientName.setText(clientName);
        txtPackageName.setText(packageName);
        txtBasePrice.setText("Rs. " + MONEY_FMT.format(basePrice));
        txtExtraCost.setText(MONEY_FMT.format(extraCost)); // plain number – user can edit
        txtTransportCost.setText(MONEY_FMT.format(transportCost)); // supplier cost
        txtGrandTotal.setText("Rs. " + MONEY_FMT.format(grandTotal));
        txtAdvance.setText("Rs. " + MONEY_FMT.format(advancePaid));
        txtNewPayment.setText("0.00"); // Reset new payment on load
        txtDueBalance.setText("Rs. " + MONEY_FMT.format(dueBalance));
    }

    // =========================================================
    // STORY 2 – GENERATE RECEIPT (btnGenerate)
    // =========================================================
    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {
        String bookingId = txtBookingId.getText().trim();

        // Validate that search has been performed
        if (txtClientName.getText().isEmpty() || bookingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please search a valid Booking ID first.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String clientName = txtClientName.getText();
        String packageName = txtPackageName.getText();
        String basePrice = txtBasePrice.getText();
        String extraCost = txtExtraCost.getText();
        String transportCost = txtTransportCost.getText();
        String grandTotal = txtGrandTotal.getText();
        String advance = txtAdvance.getText();
        String dueBalance = txtDueBalance.getText();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        String div = "=".repeat(48);
        String sep = "-".repeat(48);

        StringBuilder receipt = new StringBuilder();
        receipt.append("\n").append(div).append("\n");
        receipt.append("        ★  CELESTIAL EVENTS  ★\n");
        receipt.append("     Premium Event Management Services\n");
        receipt.append(div).append("\n");
        receipt.append(String.format("  Date         : %s%n", dateStr));
        receipt.append(String.format("  Invoice No   : INV-%s%n", bookingId));
        receipt.append(String.format("  Booking ID   : %s%n", bookingId));
        receipt.append(sep).append("\n");
        receipt.append("  CLIENT INFORMATION\n");
        receipt.append(sep).append("\n");
        receipt.append(String.format("  Client Name  : %s%n", clientName));
        receipt.append(sep).append("\n");
        receipt.append("  PACKAGE DETAILS\n");
        receipt.append(sep).append("\n");
        receipt.append(String.format("  Package      : %s%n", packageName));
        receipt.append(String.format("  Base Price   : %s%n", basePrice));
        receipt.append(String.format("  Extra Cost   : Rs. %s%n", extraCost));
        receipt.append(String.format("  Supplier Cost: Rs. %s%n", transportCost));
        receipt.append(sep).append("\n");
        receipt.append("  BILLING SUMMARY\n");
        receipt.append(sep).append("\n");
        receipt.append(String.format("  Grand Total  : %s%n", grandTotal));
        receipt.append(String.format("  Advance Paid : %s%n", advance));
        receipt.append(String.format("  Due Balance  : %s%n", dueBalance));
        receipt.append(div).append("\n");
        receipt.append("   Thank you for choosing Celestial Events!\n");
        receipt.append("      Contact: +94 71 000 0000  |  events@cel.lk\n");
        receipt.append(div).append("\n");

        txtBillArea.setText(receipt.toString());
    }

    // =========================================================
    // STORY 3 – UPDATE STATUS (btnUpdateStatus)
    // =========================================================
    private void btnUpdateStatusActionPerformed(java.awt.event.ActionEvent evt) {
        String bookingId = txtBookingId.getText().trim();
        if (bookingId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please search a valid Booking ID first.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newStatus = (String) cmbStatus.getSelectedItem();
        if (newStatus == null) {
            JOptionPane.showMessageDialog(this, "Please select a payment status.", "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Parse the new payment
        String newPayTxt = txtNewPayment.getText().replace("Rs.", "").replace(",", "").trim();
        double newPayment = newPayTxt.isEmpty() ? 0.0 : Double.parseDouble(newPayTxt);

        try (Connection conn = DBConnect.connect()) {
            if (newPayment > 0) {
                // If a new payment is made, add it to the existing advance_payment
                String sql = "UPDATE billing SET payment_status = ?, advance_payment = advance_payment + ? WHERE event_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newStatus);
                stmt.setDouble(2, newPayment);
                stmt.setString(3, bookingId);
                int rows = stmt.executeUpdate();
                stmt.close();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "✔ Payment of Rs. " + MONEY_FMT.format(newPayment) + " received!\nPayment status updated to: " + newStatus,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Reload to reflect new totals
                    loadBillingData(conn, bookingId);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No billing record found for Booking ID: " + bookingId,
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                // Just update status if no new payment
                String sql = "UPDATE billing SET payment_status = ? WHERE event_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, newStatus);
                stmt.setString(2, bookingId);
                int rows = stmt.executeUpdate();
                stmt.close();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "✔ Transaction Closed!\nPayment status updated to: " + newStatus,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No billing record found for Booking ID: " + bookingId +
                                    "\nPlease ensure billing record exists.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage(),
                    "DB Error", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    // =========================================================
    // STORY 4a – PRINT (btnPrint)
    // =========================================================
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            txtBillArea.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Print error: " + e.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // STORY 4b – WHATSAPP (btnWhatsApp)
    // =========================================================
    private void btnWhatsAppActionPerformed(java.awt.event.ActionEvent evt) {
        String phone = JOptionPane.showInputDialog(this,
                "Enter client's WhatsApp number\n(include country code, e.g. 94711234567):",
                "WhatsApp Share", JOptionPane.QUESTION_MESSAGE);

        if (phone == null || phone.trim().isEmpty())
            return;
        phone = phone.trim().replaceAll("[^0-9]", "");

        String invoiceText = txtBillArea.getText();
        if (invoiceText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please generate the receipt first.",
                    "Empty Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String encoded = URLEncoder.encode(invoiceText, StandardCharsets.UTF_8.toString());
            String url = "https://web.whatsapp.com/send?phone=" + phone + "&text=" + encoded;
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open WhatsApp: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // STORY 4c – EMAIL (btnEmail)
    // =========================================================
    private void btnEmailActionPerformed(java.awt.event.ActionEvent evt) {
        String email = JOptionPane.showInputDialog(this,
                "Enter client's email address:",
                "Email Invoice", JOptionPane.QUESTION_MESSAGE);

        if (email == null || email.trim().isEmpty())
            return;

        String subject = "Your Event Invoice - " + txtBookingId.getText().trim();
        String invoiceText = txtBillArea.getText();

        if (invoiceText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please generate the receipt first.",
                    "Empty Receipt", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String encodedSubject = URLEncoder.encode(subject, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String encodedBody = URLEncoder.encode(invoiceText, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            String mailtoUri = "mailto:" + email + "?subject=" + encodedSubject + "&body=" + encodedBody;
            Desktop.getDesktop().mail(new URI(mailtoUri));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open mail client: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // STORY 5 – GENERATE QUOTATION (btnQuotation)
    // =========================================================
    private void btnQuotationActionPerformed(java.awt.event.ActionEvent evt) {
        // Validate that search has been performed
        if (txtClientName.getText().isEmpty() || txtBookingId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please search a valid Booking / Event ID first before generating a quotation.",
                    "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Collect current field values
        String eventId = txtBookingId.getText().trim();
        String clientName = txtClientName.getText();
        String packageName = txtPackageName.getText();
        String basePrice = txtBasePrice.getText();
        
        // Combine extra cost and transport cost for the quotation view
        double parsedExtra = txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim().isEmpty() ? 0 : Double.parseDouble(txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim());
        double parsedTrans = txtTransportCost.getText().replace("Rs.", "").replace(",", "").trim().isEmpty() ? 0 : Double.parseDouble(txtTransportCost.getText().replace("Rs.", "").replace(",", "").trim());
        String combinedExtraCost = String.format("%.2f", parsedExtra + parsedTrans);
        
        String grandTotal = txtGrandTotal.getText();
        String advance = txtAdvance.getText();
        String dueBalance = txtDueBalance.getText();

        // Open quotation window with all data pre-filled
        quotation q = new quotation(
                eventId, clientName, packageName,
                basePrice, combinedExtraCost, grandTotal,
                advance, dueBalance);
        q.setLocationRelativeTo(null); // centre on screen
        q.setVisible(true);
    }

    private void btnQuotationPdfActionPerformed(java.awt.event.ActionEvent evt) {
        if (txtClientName.getText().isEmpty() || txtBookingId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please search a valid Booking / Event ID first before generating a quotation.",
                    "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String eventId = txtBookingId.getText().trim();
        String clientName = txtClientName.getText();
        String packageName = txtPackageName.getText();
        String basePrice = txtBasePrice.getText();
        
        double parsedExtra = txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim().isEmpty() ? 0 : Double.parseDouble(txtExtraCost.getText().replace("Rs.", "").replace(",", "").trim());
        double parsedTrans = txtTransportCost.getText().replace("Rs.", "").replace(",", "").trim().isEmpty() ? 0 : Double.parseDouble(txtTransportCost.getText().replace("Rs.", "").replace(",", "").trim());
        String combinedExtraCost = String.format("%.2f", parsedExtra + parsedTrans);
        
        String advance = txtAdvance.getText();
        String dueBalance = txtDueBalance.getText();

        QuotationPDFGenerator.generateAndOpenPDF(eventId, clientName, packageName, basePrice, combinedExtraCost, advance,
                dueBalance);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        txtBookingId = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtClientName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPackageName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtBasePrice = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtExtraCost = new javax.swing.JTextField();
        lblTransportCost = new javax.swing.JLabel();
        txtTransportCost = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtGrandTotal = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAdvance = new javax.swing.JTextField();
        lblNewPayment = new javax.swing.JLabel();
        txtNewPayment = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtDueBalance = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnUpdateStatus = new javax.swing.JButton();
        btnGenerate = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBillArea = new javax.swing.JTextArea();
        btnPrint = new javax.swing.JButton();
        btnWhatsApp = new javax.swing.JButton();
        btnEmail = new javax.swing.JButton();
        btnQuotation = new javax.swing.JButton();
        btnQuotationPdf = new javax.swing.JButton();

        setClosable(true);
        setMaximumSize(new java.awt.Dimension(1060, 660));
        setMinimumSize(new java.awt.Dimension(1060, 660));
        setPreferredSize(new java.awt.Dimension(1060, 660));
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(18, 18, 30));
        jPanel1.setMaximumSize(new java.awt.Dimension(1060, 660));
        jPanel1.setMinimumSize(new java.awt.Dimension(1060, 660));
        jPanel1.setPreferredSize(new java.awt.Dimension(1060, 660));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(24, 24, 42));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(130, 170, 255));
        jLabel1.setText("Billing & Cost");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 14, 300, 38));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 58, 388, 4));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(180, 200, 255));
        jLabel2.setText("Booking / Event ID:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 72, 150, 34));

        txtBookingId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtBookingId, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 72, 140, 34));

        btnSearch.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        jPanel2.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(318, 72, 86, 34));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(100, 180, 255));
        jLabel3.setText("BILLING DETAILS");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 118, 200, 20));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(200, 210, 255));
        jLabel4.setText("Client Name:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 148, 150, 34));

        txtClientName.setEditable(false);
        txtClientName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtClientName, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 148, 228, 34));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(200, 210, 255));
        jLabel5.setText("Package:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 192, 150, 34));

        txtPackageName.setEditable(false);
        txtPackageName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtPackageName, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 192, 228, 34));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(200, 210, 255));
        jLabel6.setText("Base Price (Rs.):");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 236, 150, 34));

        txtBasePrice.setEditable(false);
        txtBasePrice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtBasePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 236, 228, 34));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(200, 210, 255));
        jLabel7.setText("Extra Cost (Rs.):");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 280, 150, 34));

        txtExtraCost.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtExtraCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 280, 228, 34));

        lblTransportCost.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblTransportCost.setForeground(new java.awt.Color(200, 210, 255));
        lblTransportCost.setText("Supplier Cost (Rs.):");
        jPanel2.add(lblTransportCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 324, 150, 34));

        txtTransportCost.setEditable(false);
        txtTransportCost.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtTransportCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 324, 228, 34));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(200, 210, 255));
        jLabel8.setText("Grand Total (Rs.):");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 362, 150, 34));

        txtGrandTotal.setEditable(false);
        txtGrandTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel2.add(txtGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 362, 228, 34));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(200, 210, 255));
        jLabel9.setText("Advance Paid (Rs.):");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 400, 150, 34));

        txtAdvance.setEditable(false);
        txtAdvance.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanel2.add(txtAdvance, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 400, 228, 34));

        lblNewPayment.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblNewPayment.setForeground(new java.awt.Color(130, 255, 130));
        lblNewPayment.setText("New Payment (Rs.):");
        jPanel2.add(lblNewPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 438, 150, 34));

        txtNewPayment.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtNewPayment.setText("0.00");
        jPanel2.add(txtNewPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 438, 228, 34));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(200, 210, 255));
        jLabel10.setText("Due Balance (Rs.):");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 476, 150, 34));

        txtDueBalance.setEditable(false);
        txtDueBalance.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel2.add(txtDueBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 476, 228, 34));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(200, 210, 255));
        jLabel11.setText("Payment Status:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 514, 150, 34));

        cmbStatus.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Partial", "Paid", "Cancelled" }));
        jPanel2.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 514, 228, 34));

        btnUpdateStatus.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnUpdateStatus.setText("Update Status");
        btnUpdateStatus.addActionListener(this::btnUpdateStatusActionPerformed);
        jPanel2.add(btnUpdateStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 560, 174, 38));

        btnGenerate.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnGenerate.setText("Generate Receipt");
        btnGenerate.addActionListener(this::btnGenerateActionPerformed);
        jPanel2.add(btnGenerate, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 560, 202, 38));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 620));

        jPanel3.setBackground(new java.awt.Color(20, 20, 36));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(130, 170, 255));
        jLabel12.setText("Receipt Preview");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 14, 300, 28));

        txtBillArea.setColumns(20);
        txtBillArea.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        txtBillArea.setRows(5);
        jScrollPane1.setViewportView(txtBillArea);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 50, 400, 440));

        btnPrint.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnPrint.setText("Print");
        btnPrint.addActionListener(this::btnPrintActionPerformed);
        jPanel3.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 120, 36));

        btnWhatsApp.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnWhatsApp.setText("WhatsApp");
        btnWhatsApp.addActionListener(this::btnWhatsAppActionPerformed);
        jPanel3.add(btnWhatsApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 510, 140, 36));

        btnEmail.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnEmail.setText("Email");
        btnEmail.addActionListener(this::btnEmailActionPerformed);
        jPanel3.add(btnEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 510, 120, 36));

        btnQuotation.setText("Quotation");
        jPanel3.add(btnQuotation, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 130, 40));

        btnQuotationPdf.setText("QuotationPdf");
        jPanel3.add(btnQuotationPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 130, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 640, 620));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

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

        java.awt.EventQueue.invokeLater(() -> new Billing_and_Cost().setVisible(true));
    }

    private void customizeUI() {
        getContentPane().setBackground(UITheme.BG_DEEP);
        
        UITheme.styleTextField(txtBookingId);
        UITheme.styleTextField(txtClientName);
        UITheme.styleTextField(txtPackageName);
        UITheme.styleTextField(txtBasePrice);
        UITheme.styleTextField(txtExtraCost);
        UITheme.styleTextField(txtGrandTotal);
        UITheme.styleTextField(txtAdvance);
        UITheme.styleTextField(txtDueBalance);
        
        UITheme.styleComboBox(cmbStatus);
        
        UITheme.styleButton(btnSearch, UITheme.BTN_GREY);
        UITheme.styleButton(btnUpdateStatus, UITheme.BTN_BLUE);
        UITheme.styleButton(btnGenerate, new java.awt.Color(40, 150, 80));
        UITheme.styleButton(btnPrint, UITheme.BTN_GREY);
        UITheme.styleButton(btnWhatsApp, new java.awt.Color(37, 211, 102));
        UITheme.styleButton(btnEmail, new java.awt.Color(219, 68, 55));
        UITheme.styleButton(btnQuotation, UITheme.BTN_BLUE);
        UITheme.styleButton(btnQuotationPdf, new java.awt.Color(180, 30, 80));
        
        txtBillArea.setBackground(UITheme.BG_INPUT);
        txtBillArea.setForeground(UITheme.FG_WHITE);
        txtBillArea.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        jScrollPane1.setBorder(new javax.swing.border.LineBorder(UITheme.BORDER_COL));
        
        javax.swing.JLabel[] labels = {
            jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7,
            jLabel8, jLabel9, jLabel10, jLabel11, jLabel12
        };
        for (javax.swing.JLabel lbl : labels) {
            lbl.setForeground(UITheme.FG_WHITE);
        }
        jLabel1.setFont(UITheme.F_TITLE);
        jLabel12.setFont(UITheme.F_TITLE);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEmail;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnQuotation;
    private javax.swing.JButton btnQuotationPdf;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdateStatus;
    private javax.swing.JButton btnWhatsApp;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblNewPayment;
    private javax.swing.JLabel lblTransportCost;
    private javax.swing.JTextField txtAdvance;
    private javax.swing.JTextField txtBasePrice;
    private javax.swing.JTextArea txtBillArea;
    private javax.swing.JTextField txtBookingId;
    private javax.swing.JTextField txtClientName;
    private javax.swing.JTextField txtDueBalance;
    private javax.swing.JTextField txtExtraCost;
    private javax.swing.JTextField txtGrandTotal;
    private javax.swing.JTextField txtNewPayment;
    private javax.swing.JTextField txtPackageName;
    private javax.swing.JTextField txtTransportCost;
    // End of variables declaration//GEN-END:variables
}
