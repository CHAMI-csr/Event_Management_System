package event_management_system;


import com.formdev.flatlaf.FlatDarkLaf;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.*;

public class Event_Schedule extends javax.swing.JInternalFrame {

    // ─────────────────────────────────────────────────────────────────────────
    //  Logger
    // ─────────────────────────────────────────────────────────────────────────
    private static final Logger logger =
            Logger.getLogger(Event_Schedule.class.getName());

    // ─────────────────────────────────────────────────────────────────────────
    //  Colour Palette  (mirrors the project-wide dark theme)
    // ─────────────────────────────────────────────────────────────────────────
    private static final Color BG_DEEP        = new Color( 14,  14,  22);
    private static final Color BG_CARD        = new Color( 24,  24,  38);
    private static final Color BG_GRID        = new Color( 22,  22,  34);
    private static final Color BG_DAY_EMPTY   = new Color( 28,  28,  44);
    private static final Color BG_ROW_ODD     = new Color( 28,  28,  44);
    private static final Color BG_ROW_EVEN    = new Color( 34,  34,  52);
    private static final Color BG_ROW_SEL     = new Color( 70,  96, 215);
    private static final Color BG_HDR_TBL     = new Color( 55,  74, 195);

    private static final Color CLR_PAID       = new Color( 40, 180,  80);   // green
    private static final Color CLR_PENDING    = new Color(210, 180,  40);   // amber
    private static final Color CLR_OVERDUE    = new Color(200,  70,  70);   // red
    private static final Color CLR_TODAY      = new Color( 80, 110, 235);   // blue-accent
    private static final Color CLR_HEADER_DAY = new Color( 55,  74, 195);   // day-name header
    private static final Color CLR_WEEKEND    = new Color(170,  55,  55);   // sun/sat header

    private static final Color FG_WHITE       = new Color(230, 230, 255);
    private static final Color FG_MUTED       = new Color(140, 140, 175);
    private static final Color BORDER_COL     = new Color( 55,  55,  85);
    private static final Color ACCENT         = new Color( 90, 120, 240);

    // ─────────────────────────────────────────────────────────────────────────
    //  Fonts
    // ─────────────────────────────────────────────────────────────────────────
    private static final Font F_TITLE   = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font F_DAY_HDR = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_DAY_NUM = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font F_DAY_SUB = new Font("Segoe UI", Font.PLAIN,  9);
    private static final Font F_LEGEND  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_SIDE    = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font F_DATE    = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_HDR_TBL = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_TABLE   = new Font("Segoe UI", Font.PLAIN, 12);

    // ─────────────────────────────────────────────────────────────────────────
    //  Runtime State
    // ─────────────────────────────────────────────────────────────────────────
    private int currentMonth;   // 0-based (Calendar.JANUARY = 0)
    private int currentYear;
    private DefaultTableModel sideTableModel;

    // ─────────────────────────────────────────────────────────────────────────
    //  Programmatic UI components (NOT in GEN block — safe from NetBeans)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel            pnlCalendarColumn;
    private JPanel            pnlHeader;
    private JLabel            lblCalendarTitle;
    private JMonthChooser     monthChooser;
    private JYearChooser      yearChooser;
    private JPanel            pnlLegend;
    private JPanel            pnlCalendarGrid;
    private JPanel            pnlSidePanel;
    private JLabel            lblSideTitle;
    private JLabel            lblSelectedDate;
    private JLabel            lblEventCount;
    private JScrollPane       jScrollPane1;
    private JTable            tblSideEvents;

    // =========================================================================
    //  CONSTRUCTOR
    // =========================================================================
    public Event_Schedule() {
        initComponents();   // GEN block — creates jPanel1 only

        // Strip internal-frame chrome
        UITheme.removeInternalFrameChrome(this);

        // Build ALL calendar UI programmatically (safe from NetBeans Design View)
        buildUI();

        // Seed to current month / year
        Calendar now = Calendar.getInstance();
        currentMonth = now.get(Calendar.MONTH);
        currentYear  = now.get(Calendar.YEAR);

        // Sync choosers silently (listeners not yet active until after buildUI)
        monthChooser.setMonth(currentMonth);
        yearChooser.setYear(currentYear);

        // Wire chooser listeners AFTER setting initial values to avoid double-fire
        monthChooser.addPropertyChangeListener("month", evt ->
                generateCalendarGrid(monthChooser.getMonth(), currentYear));
        yearChooser.addPropertyChangeListener("year", evt ->
                generateCalendarGrid(currentMonth, yearChooser.getYear()));

        // Draw the initial grid
        generateCalendarGrid(currentMonth, currentYear);
    }

    // =========================================================================
    //  NetBeans Generated Code — DO NOT MODIFY
    //  (Kept minimal: only jPanel1. All real UI is in buildUI() below.)
    // =========================================================================
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(1060, 600));
        setMinimumSize(new java.awt.Dimension(1060, 600));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1068, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    // =========================================================================
    //  buildUI() — Programmatic calendar layout (outside GEN markers)
    // =========================================================================
    /**
     * Builds the entire Calendar + Side Panel UI programmatically and
     * attaches it to jPanel1. Called once from the constructor.
     * NetBeans Design View will NEVER touch this code.
     */
    private void buildUI() {
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.setBackground(BG_DEEP);
        getContentPane().setBackground(BG_DEEP);

        // ══ LEFT: Calendar Column ════════════════════════════════════════════
        pnlCalendarColumn = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCalendarColumn.setBackground(BG_DEEP);
        jPanel1.add(pnlCalendarColumn,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 600));

        // ── Header bar ───────────────────────────────────────────────────────
        pnlHeader = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlHeader.setBackground(BG_CARD);
        pnlHeader.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 4, 0, 4)));
        pnlCalendarColumn.add(pnlHeader,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 660, 60));

        lblCalendarTitle = new JLabel("\uD83D\uDCC5  Event Calendar");
        lblCalendarTitle.setFont(F_TITLE);
        lblCalendarTitle.setForeground(FG_WHITE);
        pnlHeader.add(lblCalendarTitle,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 10, 260, 40));

        monthChooser = new JMonthChooser();
        monthChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        monthChooser.setBackground(BG_CARD);
        monthChooser.setForeground(FG_WHITE);
        pnlHeader.add(monthChooser,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 8, 220, 44));

        yearChooser = new JYearChooser();
        yearChooser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        yearChooser.setBackground(BG_CARD);
        yearChooser.setForeground(FG_WHITE);
        yearChooser.setStartYear(2020);
        yearChooser.setEndYear(2035);
        pnlHeader.add(yearChooser,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(515, 8, 136, 44));

        // ── Legend strip ─────────────────────────────────────────────────────
        pnlLegend = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlLegend.setBackground(BG_DEEP);
        pnlCalendarColumn.add(pnlLegend,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 74, 660, 28));

        addLegendLabel(pnlLegend, "● Paid",    CLR_PAID,    0);
        addLegendLabel(pnlLegend, "● Pending", CLR_PENDING, 80);
        addLegendLabel(pnlLegend, "● Overdue", CLR_OVERDUE, 175);
        addLegendLabel(pnlLegend, "● Today",   CLR_TODAY,   270);

        // ── Calendar Grid Panel ───────────────────────────────────────────────
        // Leave EMPTY — GridLayout + buttons are set in generateCalendarGrid()
        pnlCalendarGrid = new JPanel();
        pnlCalendarGrid.setBackground(BG_GRID);
        pnlCalendarGrid.setBorder(new LineBorder(BORDER_COL, 1));
        pnlCalendarColumn.add(pnlCalendarGrid,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 108, 660, 482));

        // ══ RIGHT: Side Panel ════════════════════════════════════════════════
        pnlSidePanel = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlSidePanel.setBackground(BG_CARD);
        pnlSidePanel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(0, 0, 0, 0)));
        jPanel1.add(pnlSidePanel,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(685, 0, 375, 600));

        lblSideTitle = new JLabel("\uD83D\uDCCB  Events on Selected Date");
        lblSideTitle.setFont(F_SIDE);
        lblSideTitle.setForeground(ACCENT);
        pnlSidePanel.add(lblSideTitle,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 350, 30));

        lblSelectedDate = new JLabel("Click a day to view events");
        lblSelectedDate.setFont(F_DATE);
        lblSelectedDate.setForeground(FG_WHITE);
        pnlSidePanel.add(lblSelectedDate,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 50, 350, 26));

        lblEventCount = new JLabel("");
        lblEventCount.setFont(F_LEGEND);
        lblEventCount.setForeground(FG_MUTED);
        pnlSidePanel.add(lblEventCount,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 78, 350, 20));

        // Side table
        sideTableModel = new DefaultTableModel(
                new String[]{"booking_id", "Event ID", "Client", "Package", "Supplier", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSideEvents = new JTable(sideTableModel);
        tblSideEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSideEvents.setRowHeight(30);
        tblSideEvents.setShowHorizontalLines(true);
        tblSideEvents.setShowVerticalLines(false);
        // Hide booking_id column (index 0)
        tblSideEvents.getColumnModel().getColumn(0).setMinWidth(0);
        tblSideEvents.getColumnModel().getColumn(0).setMaxWidth(0);
        tblSideEvents.getColumnModel().getColumn(0).setWidth(0);
        tblSideEvents.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                tblSideEventsMouseClicked(e);
            }
        });
        styleTable(tblSideEvents);

        jScrollPane1 = new JScrollPane(tblSideEvents);
        jScrollPane1.getViewport().setBackground(BG_ROW_ODD);
        jScrollPane1.setBorder(new LineBorder(BORDER_COL, 1));
        pnlSidePanel.add(jScrollPane1,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 104, 358, 484));
    }

    /** Convenience: add a legend bullet label at a fixed x offset. */
    private void addLegendLabel(JPanel parent, String text, Color color, int x) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(F_LEGEND);
        lbl.setForeground(color);
        parent.add(lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, 4, 90, 20));
    }

    // =========================================================================
    //  CORE METHOD 1 — generateCalendarGrid(int month, int year)
    // =========================================================================
    /**
     * Clears pnlCalendarGrid and rebuilds it for the given month/year:
     *  • 7 header labels (Sun → Sat)
     *  • Blank spacers for days before the 1st
     *  • One JButton per day, color-coded by DB payment status
     *  • ActionListener on each button → loadSidePanelEvents()
     *
     * @param month  0-based month (Calendar.JANUARY = 0)
     * @param year   4-digit year
     */
    private void generateCalendarGrid(int month, int year) {
        // Keep state in sync for the chooser listeners
        currentMonth = month;
        currentYear  = year;

        // ── Step 1: Clear and set GridLayout (7 columns) ─────────────────────
        pnlCalendarGrid.removeAll();
        pnlCalendarGrid.setLayout(new GridLayout(0, 7, 3, 3));

        // ── Step 2: Day-name headers ──────────────────────────────────────────
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            JLabel hdr = new JLabel(dayNames[i], SwingConstants.CENTER);
            hdr.setFont(F_DAY_HDR);
            hdr.setOpaque(true);
            hdr.setBackground(i == 0 || i == 6 ? CLR_WEEKEND : CLR_HEADER_DAY);
            hdr.setForeground(Color.WHITE);
            hdr.setBorder(new EmptyBorder(6, 0, 6, 0));
            pnlCalendarGrid.add(hdr);
        }

        // ── Step 3: Calculate first weekday and days in month ─────────────────
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;  // 0=Sun
        int daysInMonth    = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Today for highlighting
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today.getTime());

        // ── Step 4: Fetch DB payment status for every day this month ──────────
        Map<String, String> dayStatusMap = fetchMonthStatus(month, year);

        // ── Step 5: Blank spacers before day 1 ───────────────────────────────
        for (int b = 0; b < firstDayOfWeek; b++) {
            JPanel blank = new JPanel();
            blank.setBackground(BG_GRID);
            pnlCalendarGrid.add(blank);
        }

        // ── Step 6: Day buttons ───────────────────────────────────────────────
        for (int day = 1; day <= daysInMonth; day++) {
            cal.set(year, month, day);
            String dateStr = sdf.format(cal.getTime());

            boolean isToday  = dateStr.equals(todayStr);
            boolean isPast   = cal.getTimeInMillis() < today.getTimeInMillis() && !isToday;
            String  dbStatus = dayStatusMap.get(dateStr);  // null = no event

            // Determine color
            Color bgColor;
            String tooltip;
            String subText = "";

            if (isToday) {
                bgColor = CLR_TODAY;
                tooltip = "Today" + (dbStatus != null ? " — " + dbStatus : " — No Events");
                subText = dbStatus != null ? dbStatus : "";
            } else if (dbStatus == null) {
                bgColor = BG_DAY_EMPTY;
                tooltip = "No events";
            } else if ("Paid".equalsIgnoreCase(dbStatus)) {
                bgColor = CLR_PAID;
                tooltip = "All Paid";
                subText = "Paid";
            } else if ("Pending".equalsIgnoreCase(dbStatus)) {
                if (isPast) {
                    bgColor = CLR_OVERDUE;
                    tooltip = "Overdue — Past date, not paid";
                    subText = "Overdue";
                } else {
                    bgColor = CLR_PENDING;
                    tooltip = "Pending Payment";
                    subText = "Pending";
                }
            } else {
                bgColor = CLR_OVERDUE;
                tooltip = "Overdue";
                subText = "Overdue";
            }

            final String capturedDate = dateStr;
            JButton btn = buildDayButton(day, bgColor, tooltip, subText);
            btn.addActionListener(e -> loadSidePanelEvents(capturedDate));
            pnlCalendarGrid.add(btn);
        }

        // ── Step 7: Fill trailing empty cells to complete the last row ─────────
        int total     = firstDayOfWeek + daysInMonth;
        int remainder = total % 7;
        if (remainder != 0) {
            for (int t = remainder; t < 7; t++) {
                JPanel blank = new JPanel();
                blank.setBackground(BG_GRID);
                pnlCalendarGrid.add(blank);
            }
        }

        pnlCalendarGrid.revalidate();
        pnlCalendarGrid.repaint();
    }

    /**
     * Creates a styled day JButton with the day number on top and a small
     * status sub-label below, plus hover effects.
     */
    private JButton buildDayButton(int day, Color bg, String tooltip, String subText) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 0));
        btn.setBackground(bg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.setBorder(new LineBorder(BORDER_COL, 1));

        JLabel numLbl = new JLabel(String.valueOf(day), SwingConstants.CENTER);
        numLbl.setFont(F_DAY_NUM);
        numLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel(subText, SwingConstants.CENTER);
        subLbl.setFont(F_DAY_SUB);
        subLbl.setForeground(new Color(220, 220, 255, 190));

        btn.add(numLbl, BorderLayout.CENTER);
        btn.add(subLbl, BorderLayout.SOUTH);

        Color hoverColor = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bg);         }
            @Override public void mousePressed (MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseReleased(MouseEvent e) { btn.setBackground(hoverColor); }
        });

        return btn;
    }

    /**
     * Queries the DB for all events in the given month.
     * Returns Map: "YYYY-MM-DD" → worst payment status ("Paid"|"Pending"|"Overdue").
     * If a date has multiple events, Overdue > Pending > Paid.
     */
    private Map<String, String> fetchMonthStatus(int month, int year) {
        Map<String, String> result = new LinkedHashMap<>();

        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        String startDate = String.format("%04d-%02d-01", year, month + 1);
        String endDate   = String.format("%04d-%02d-%02d", year, month + 1, lastDay);

        String sql =
            "SELECT e.event_date, b.payment_status " +
            "FROM events e " +
            "LEFT JOIN billing b ON e.event_id = b.event_id " +
            "WHERE e.event_date BETWEEN ? AND ? " +
            "ORDER BY e.event_date";

        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, startDate);
            pst.setString(2, endDate);
            ResultSet rs = pst.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar today = Calendar.getInstance();
            String todayStr = sdf.format(today.getTime());

            while (rs.next()) {
                java.sql.Date dbDate = rs.getDate("event_date");
                if (dbDate == null) continue;
                String dateKey = sdf.format(dbDate);
                String ps      = rs.getString("payment_status");
                if (ps == null) ps = "Pending";

                boolean isPast = dbDate.toLocalDate()
                        .isBefore(java.time.LocalDate.now());

                String effective;
                if ("Paid".equalsIgnoreCase(ps)) {
                    effective = "Paid";
                } else if (isPast && !dateKey.equals(todayStr)) {
                    effective = "Overdue";
                } else {
                    effective = "Pending";
                }

                // Merge — worst status wins
                String existing = result.get(dateKey);
                if (existing == null || statusRank(effective) > statusRank(existing)) {
                    result.put(dateKey, effective);
                }
            }

        } catch (Exception ex) {
            logger.log(Level.WARNING, "fetchMonthStatus error", ex);
        }

        return result;
    }

    /** Numeric rank for status severity: Overdue (3) > Pending (2) > Paid (1). */
    private int statusRank(String s) {
        if (s == null) return 0;
        switch (s) {
            case "Paid":    return 1;
            case "Pending": return 2;
            case "Overdue": return 3;
            default:        return 0;
        }
    }

    // =========================================================================
    //  CORE METHOD 2 — loadSidePanelEvents(String dateStr)
    // =========================================================================
    /**
     * Updates the side panel with all events for the clicked date.
     * Columns: booking_id (hidden col 0), Event ID, Client Name, Package, Status.
     *
     * @param dateStr  Format "YYYY-MM-DD"
     */
    private void loadSidePanelEvents(String dateStr) {
        // Update the selected-date label with a human-readable format
        try {
            SimpleDateFormat inFmt  = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outFmt = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            lblSelectedDate.setText(outFmt.format(inFmt.parse(dateStr)));
        } catch (Exception ex) {
            lblSelectedDate.setText(dateStr);
        }

        // Clear table
        sideTableModel.setRowCount(0);

        // Query DB
        String sql =
            "SELECT e.event_id, c.client_name, p.package_name, s.sup_name, b.payment_status " +
            "FROM events e " +
            "JOIN clients c ON e.client_id  = c.client_id " +
            "JOIN package p ON e.package_id = p.package_id " +
            "LEFT JOIN suppliers s ON e.sup_id = s.sup_id " +
            "LEFT JOIN billing b ON e.event_id = b.event_id " +
            "WHERE e.event_date = ? " +
            "ORDER BY e.event_id";

        try (Connection con = DBConnect.connect();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, dateStr);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String eventId   = rs.getString("event_id");
                String client    = rs.getString("client_name");
                String pkg       = rs.getString("package_name");
                String sup       = rs.getString("sup_name");
                String payStatus = rs.getString("payment_status");
                
                if (sup == null || sup.trim().isEmpty()) sup = "None";
                if (payStatus == null) payStatus = "Pending";

                // col 0 = hidden booking_id (same as event_id in this schema)
                sideTableModel.addRow(new Object[]{
                    eventId,    // [0] hidden
                    eventId,    // [1] Event ID
                    client,     // [2] Client
                    pkg,        // [3] Package
                    sup,        // [4] Supplier
                    payStatus   // [5] Status
                });
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "loadSidePanelEvents error for: " + dateStr, ex);
            JOptionPane.showMessageDialog(this,
                    "Error loading events:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Update event count badge
        int count = sideTableModel.getRowCount();
        lblEventCount.setText(count == 0
                ? "No events scheduled for this day."
                : count + (count == 1 ? " event" : " events") + " scheduled  (double-click to manage)");
    }

    // =========================================================================
    //  CORE METHOD 3 — Table double-click drill-through to assign_resources
    // =========================================================================
    private void tblSideEventsMouseClicked(MouseEvent evt) {
        if (evt.getClickCount() < 2) return;

        int row = tblSideEvents.getSelectedRow();
        if (row == -1) return;

        // Col 0 holds the hidden event_id / booking_id
        String bookingId = sideTableModel.getValueAt(row, 0).toString();

        try {
            assign_resources arFrame = new assign_resources(bookingId);
            arFrame.setVisible(true);

            // Add to the parent JDesktopPane if available
            Container parent = this.getParent();
            if (parent instanceof JDesktopPane) {
                JDesktopPane dp = (JDesktopPane) parent;
                dp.add(arFrame);
                try { arFrame.setSelected(true); } catch (Exception ignored) {}
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not open assign_resources", ex);
            JOptionPane.showMessageDialog(this,
                    "Error opening resource panel:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    //  Dark-theme table styling
    // =========================================================================
    private void styleTable(JTable tbl) {
        tbl.setBackground(BG_ROW_ODD);
        tbl.setForeground(FG_WHITE);
        tbl.setFont(F_TABLE);
        tbl.setGridColor(BORDER_COL);
        tbl.setSelectionBackground(BG_ROW_SEL);
        tbl.setSelectionForeground(Color.WHITE);
        tbl.setIntercellSpacing(new Dimension(0, 1));
        tbl.setFillsViewportHeight(true);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header
        JTableHeader hdr = tbl.getTableHeader();
        hdr.setBackground(BG_HDR_TBL);
        hdr.setForeground(Color.WHITE);
        hdr.setFont(F_HDR_TBL);
        hdr.setPreferredSize(new Dimension(hdr.getWidth(), 36));
        hdr.setReorderingAllowed(false);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(SwingConstants.LEFT); }
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setBackground(BG_HDR_TBL);
                setForeground(Color.WHITE);
                setFont(F_HDR_TBL);
                setBorder(new CompoundBorder(
                        new MatteBorder(0, 0, 0, 1, BORDER_COL),
                        new EmptyBorder(0, 10, 0, 10)));
                return this;
            }
        });

        // Row renderer — alternating rows + coloured status text
        DefaultTableCellRenderer rowRend = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? BG_ROW_SEL : (row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD));
                setFont(F_TABLE);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!sel && v != null && col == 4) {
                    String status = v.toString();
                    if ("Paid".equalsIgnoreCase(status))         setForeground(CLR_PAID);
                    else if ("Pending".equalsIgnoreCase(status)) setForeground(CLR_PENDING);
                    else                                         setForeground(CLR_OVERDUE);
                } else {
                    setForeground(sel ? Color.WHITE : FG_WHITE);
                }
                return this;
            }
        };
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(rowRend);
        }
    }

    // =========================================================================
    //  MAIN — standalone test harness
    // =========================================================================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatDarkLaf()); }
        catch (Exception ex) { logger.log(Level.WARNING, "FlatDarkLaf unavailable", ex); }

        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Event Calendar — Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 650);
            frame.setLocationRelativeTo(null);

            JDesktopPane desktop = new JDesktopPane();
            desktop.setBackground(new Color(10, 10, 18));
            frame.setContentPane(desktop);

            Event_Schedule cal = new Event_Schedule();
            cal.setSize(1060, 600);
            cal.setLocation(20, 20);
            desktop.add(cal);
            try { cal.setSelected(true); } catch (Exception ignored) {}
            cal.setVisible(true);
            frame.setVisible(true);
        });
    }
}
