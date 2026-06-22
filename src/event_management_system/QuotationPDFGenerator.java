package event_management_system;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
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

public class QuotationPDFGenerator {

    /** Escape HTML special characters. */
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void generateAndOpenPDF(String eventId, String clientName, String packageName,
                                          String basePrice, String extraCost, String advancePaid, 
                                          String dueBalance) {
        try {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            List<String[]> allRes = new ArrayList<>();
            String clientAddress = "N/A";
            String clientContact = "N/A";
            String clientEmail = "N/A";

            if (eventId != null && !eventId.trim().isEmpty()) {
                // Fetch client details
                String sqlClient = "SELECT c.address, c.contact_number, c.email FROM events e JOIN clients c ON e.client_id = c.client_id WHERE e.event_id = ?";
                try (Connection conn = DBConnect.connect();
                     PreparedStatement st = conn.prepareStatement(sqlClient)) {
                    st.setString(1, eventId);
                    try (ResultSet rs = st.executeQuery()) {
                        if (rs.next()) {
                            clientAddress = rs.getString("address");
                            if (clientAddress == null || clientAddress.trim().isEmpty()) clientAddress = "N/A";
                            clientContact = rs.getString("contact_number");
                            if (clientContact == null || clientContact.trim().isEmpty()) clientContact = "N/A";
                            clientEmail = rs.getString("email");
                            if (clientEmail == null || clientEmail.trim().isEmpty()) clientEmail = "N/A";
                        }
                    }
                }

                // Fetch resources
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
                                esc(rs2.getString("resource_name")),
                                esc(rs2.getString("resource_type")),
                                String.valueOf(rs2.getInt("quantity")),
                                "Rs. " + df.format(rs2.getDouble("cost_per_item")),
                                "Rs. " + df.format(rs2.getDouble("total_cost"))
                            };
                            allRes.add(row);
                        }
                    }
                }
            }

            String html = buildHtml(eventId, clientName, packageName, basePrice, extraCost, 
                                    advancePaid, dueBalance, allRes, clientAddress, clientContact, clientEmail);

            // Write to temp file
            File tmpFile = File.createTempFile("Quotation_" + eventId + "_", ".html");
            tmpFile.deleteOnExit();
            try (FileWriter fw = new FileWriter(tmpFile, StandardCharsets.UTF_8)) {
                fw.write(html);
            }

            // Open in default browser – user presses Ctrl+P → Save as PDF
            Desktop.getDesktop().open(tmpFile);

            JOptionPane.showMessageDialog(null,
                    "The quotation has opened in your browser.\n" +
                    "Press  Ctrl + P  then choose  'Save as PDF'  to save.",
                    "Save as PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Could not generate PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private static String buildHtml(String eventId, String clientName, String packageName,
            String basePrice, String extraCost, String advancePaid, String dueBalance,
            List<String[]> allRes, String clientAddress, String clientContact, String clientEmail) {

        String dateStr   = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        String quoteNo   = "QT-" + eventId.replace("E-", "");
        String validTill = new SimpleDateFormat("dd MMMM yyyy").format(
                new Date(System.currentTimeMillis() + 7L * 24 * 3600 * 1000));

        StringBuilder rows = new StringBuilder();
        if (allRes.isEmpty()) {
            rows.append("<tr><td colspan='4' style='text-align:center; color:#888;'>No resources assigned</td></tr>");
        } else {
            for (String[] r : allRes) {
                rows.append("<tr>")
                    .append("<td class='center'>").append(r[2]).append("</td>")
                    .append("<td>").append(r[0]).append(" <span style='font-size:10px;color:#999;'>(").append(r[1]).append(")</span></td>")
                    .append("<td class='center'>").append(r[3]).append("</td>")
                    .append("<td class='right'>").append(r[4]).append("</td>")
                    .append("</tr>");
            }
        }

        return "<!DOCTYPE html>\n"
            + "<html lang='en'>\n"
            + "<head>\n"
            + "<meta charset='UTF-8'/>\n"
            + "<meta name='viewport' content='width=device-width,initial-scale=1'/>\n"
            + "<title>Quotation " + quoteNo + " &#8211; Celestial Events</title>\n"
            + "<style>\n"
            + "  @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700;800&display=swap');\n"
            + "  *{margin:0;padding:0;box-sizing:border-box;}\n"
            + "  body{font-family:'Montserrat',Arial,sans-serif;background:#fadad8;padding:20px;}\n"
            + "  .page{width:794px;min-height:1123px;margin:0 auto;background:#fff;position:relative;box-shadow:0 10px 30px rgba(0,0,0,0.1);}\n"
            + "  .header-fluid{height:120px;background:linear-gradient(90deg, #64d1f4, #f38fe5, #fca8b6);}\n"
            + "  .footer-fluid{height:100px;background:linear-gradient(90deg, #f38fe5, #64d1f4, #fca8b6);position:absolute;bottom:0;width:100%;}\n"
            + "  .content{padding:40px 50px 140px 50px;}\n"
            + "  .logo h1{font-size:32px;font-weight:800;color:#3a3a3a;margin-bottom:30px;line-height:1.1;letter-spacing:1px;}\n"
            + "  .info-tables{display:flex;justify-content:space-between;margin-bottom:30px;gap:20px;}\n"
            + "  .info-tables table{width:48%;border-collapse:collapse;font-size:12px;}\n"
            + "  .info-tables th{background:#ff4da6;color:#fff;padding:8px;text-align:left;font-size:13px;font-weight:700;text-transform:uppercase;}\n"
            + "  .info-tables td{padding:6px 8px;border-bottom:1px solid #eee;}\n"
            + "  .info-tables td.lbl{color:#00bcd4;font-weight:600;width:40%;}\n"
            + "  .info-tables td.val{color:#333;}\n"
            + "  .items-table{width:100%;border-collapse:collapse;font-size:12px;margin-bottom:30px;}\n"
            + "  .items-table th{background:#ff4da6;color:#fff;padding:10px;text-align:left;font-size:13px;text-transform:uppercase;}\n"
            + "  .items-table th.center{text-align:center;}\n"
            + "  .items-table td{padding:10px;border-bottom:1px solid #ddd;color:#555;}\n"
            + "  .items-table td.center{text-align:center;}\n"
            + "  .items-table td.right{text-align:center;color:#333;font-weight:600;}\n"
            + "  .bottom-section{display:flex;justify-content:space-between;}\n"
            + "  .bottom-left{width:60%;font-size:12px;color:#333;}\n"
            + "  .terms{margin-bottom:20px;line-height:1.6;}\n"
            + "  .terms b{font-size:13px;}\n"
            + "  .bank{line-height:1.6;}\n"
            + "  .bank b{font-size:12px;text-transform:uppercase;}\n"
            + "  .bank table{margin-top:5px;border-collapse:collapse;font-size:12px;}\n"
            + "  .bank td{padding:3px 5px 3px 0;}\n"
            + "  .bank td.lbl{color:#00bcd4;font-weight:600;}\n"
            + "  .bottom-right{width:35%;}\n"
            + "  .totals-table{width:100%;border-collapse:collapse;font-size:13px;}\n"
            + "  .totals-table td{padding:8px;}\n"
            + "  .totals-table td.lbl{font-weight:700;text-align:right;color:#333;text-transform:uppercase;}\n"
            + "  .totals-table td.val{background:#4fc3f7;color:#fff;text-align:center;font-weight:600;border:1px solid #fff;}\n"
            + "  @media print{\n"
            + "    body{background:#fff;padding:0;}\n"
            + "    .page{box-shadow:none;margin:0;}\n"
            + "    .header-fluid, .footer-fluid, th, .totals-table td.val {-webkit-print-color-adjust:exact !important;print-color-adjust:exact !important;}\n"
            + "  }\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div class='page'>\n"
            + "  <div class='header-fluid'></div>\n"
            + "  <div class='content'>\n"
            + "    <div class='logo'>\n"
            + "      <h1>CELESTIAL<br>EVENTS</h1>\n"
            + "    </div>\n"
            + "    <div class='info-tables'>\n"
            + "      <table>\n"
            + "        <thead><tr><th colspan='2'>QUOTE TO:</th></tr></thead>\n"
            + "        <tbody>\n"
            + "          <tr><td class='lbl'>Customer Name:</td><td class='val'>" + esc(clientName) + "</td></tr>\n"
            + "          <tr><td class='lbl'>Address:</td><td class='val'>" + esc(clientAddress) + "</td></tr>\n"
            + "          <tr><td class='lbl'>Contact No.:</td><td class='val'>" + esc(clientContact) + "</td></tr>\n"
            + "          <tr><td class='lbl'>Email:</td><td class='val'>" + esc(clientEmail) + "</td></tr>\n"
            + "        </tbody>\n"
            + "      </table>\n"
            + "      <table>\n"
            + "        <thead><tr><th colspan='2'>QUOTE DETAILS:</th></tr></thead>\n"
            + "        <tbody>\n"
            + "          <tr><td class='lbl'>Quote No.:</td><td class='val'>" + quoteNo + "</td></tr>\n"
            + "          <tr><td class='lbl'>Booking ID:</td><td class='val'>" + esc(eventId) + "</td></tr>\n"
            + "          <tr><td class='lbl'>Date:</td><td class='val'>" + dateStr + "</td></tr>\n"
            + "          <tr><td class='lbl'>Valid Date:</td><td class='val'>" + validTill + "</td></tr>\n"
            + "        </tbody>\n"
            + "      </table>\n"
            + "    </div>\n"
            + "    <table class='items-table'>\n"
            + "      <thead><tr>\n"
            + "        <th style='width:10%;'>QTY</th>\n"
            + "        <th style='width:50%;'>DETAILS</th>\n"
            + "        <th class='center' style='width:20%;'>UNIT PRICE</th>\n"
            + "        <th class='center' style='width:20%;'>TOTAL</th>\n"
            + "      </tr></thead>\n"
            + "      <tbody>\n"
            +          rows.toString()
            + "      </tbody>\n"
            + "    </table>\n"
            + "    <div class='bottom-section'>\n"
            + "      <div class='bottom-left'>\n"
            + "        <div class='terms'>\n"
            + "          <b>Terms &amp; Conditions:</b><br>\n"
            + "          Payment must be made within 20 days.<br>\n"
            + "          A minimum 30% advance is required to confirm the booking.<br>\n"
            + "          Cancellation within 48 hours is non-refundable.\n"
            + "        </div>\n"
            + "        <div class='bank'>\n"
            + "          <b>PLEASE DEPOSIT IN THIS ACCOUNT:</b><br>\n"
            + "          <table>\n"
            + "            <tr><td class='lbl'>Name:</td><td class='val'>Celestial Events</td></tr>\n"
            + "            <tr><td class='lbl'>Account Number:</td><td class='val'>100 200 300 400</td></tr>\n"
            + "            <tr><td class='lbl'>Bank Name:</td><td class='val'>Commercial Bank</td></tr>\n"
            + "            <tr><td class='lbl'>Location:</td><td class='val'>Galle Branch</td></tr>\n"
            + "          </table>\n"
            + "        </div>\n"
            + "      </div>\n"
            + "      <div class='bottom-right'>\n"
            + "        <table class='totals-table'>\n"
            + "          <tr><td class='lbl'>SUB TOTAL</td><td class='val'>" + esc(basePrice) + "</td></tr>\n"
            + "          <tr><td class='lbl'>MISCELLANEOUS</td><td class='val'>" + esc(extraCost) + "</td></tr>\n"
            + "          <tr><td class='lbl'>ADVANCE PAID</td><td class='val'>" + esc(advancePaid) + "</td></tr>\n"
            + "          <tr><td class='lbl'>TOTAL DUE</td><td class='val'>" + esc(dueBalance) + "</td></tr>\n"
            + "        </table>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "  <div class='footer-fluid'></div>\n"
            + "</div>\n"
            + "</body></html>\n";
    }
}
