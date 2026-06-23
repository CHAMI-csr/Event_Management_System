package event_management_system;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UITheme {
    
    // ── Colour palette ────────────────────────────────────────────────────────
    public static final Color BG_DEEP = new Color(14, 14, 22);
    public static final Color BG_CARD = new Color(24, 24, 38);
    public static final Color BG_INPUT = new Color(38, 38, 60);
    public static final Color BG_HDR = new Color(55, 74, 195);
    public static final Color BG_ROW_ODD = new Color(28, 28, 44);
    public static final Color BG_ROW_EVEN = new Color(34, 34, 52);
    public static final Color BG_ROW_SEL = new Color(70, 96, 215);
    public static final Color BTN_BLUE = new Color(63, 84, 186);
    public static final Color BTN_RED = new Color(195, 55, 55);
    public static final Color BTN_GREY = new Color(60, 60, 85);
    public static final Color FG_WHITE = new Color(230, 230, 255);
    public static final Color FG_MUTED = new Color(140, 140, 175);
    public static final Color BORDER_COL = new Color(55, 55, 85);
    public static final Color ACCENT = new Color(90, 120, 240);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font F_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font F_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_INPUT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_BTN = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font F_HDR = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font F_TABLE = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Strips the NetBeans internal-frame chrome so it fits into the dashboard seamlessly.
     */
    public static void removeInternalFrameChrome(JInternalFrame frame) {
        frame.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) frame.getUI();
        if (ui != null) {
            ui.setNorthPane(null);
        }
    }

    public static void styleTextField(JTextField tf) {
        tf.setBackground(BG_INPUT);
        tf.setForeground(FG_WHITE);
        tf.setCaretColor(FG_WHITE);
        tf.setFont(F_INPUT);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(4, 10, 4, 10)));
        tf.putClientProperty("JTextField.placeholderForeground", FG_MUTED);
    }

    public static void styleComboBox(JComboBox<?> cb) {
        cb.setBackground(BG_INPUT);
        cb.setForeground(FG_WHITE);
        cb.setFont(F_INPUT);
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
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

    public static void styleSpinner(JSpinner sp) {
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

    public static void styleButton(JButton btn, Color bg) {
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
        
        // Remove existing mouse listeners added by this method previously to prevent duplicates
        for (java.awt.event.MouseListener ml : btn.getMouseListeners()) {
            if (ml.getClass().getName().contains("UITheme")) {
                btn.removeMouseListener(ml);
            }
        }
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
    }

    public static void styleTable(JTable tbl) {
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
            {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            @Override
            public Component getTableCellRendererComponent(
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
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
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
}
