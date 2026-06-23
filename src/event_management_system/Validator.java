package event_management_system;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Reusable input-validation helpers for all GUI forms.
 * Every method shows a descriptive JOptionPane on failure and returns
 * false so the caller can abort the save/submit immediately.
 *
 * @author chamika
 */
public class Validator {

    private Validator() { /* static-only utility */ }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Checks that a JTextField is not null and not blank.
     *
     * @param field     the text field to validate
     * @param fieldName human-readable label shown in the error message
     * @return true if valid, false otherwise (error popup shown)
     */
    public static boolean isNotEmpty(JTextField field, String fieldName) {
        if (field == null || field.getText() == null || field.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "\"" + fieldName + "\" cannot be empty!\nPlease fill in this required field.",
                "Validation Error — " + fieldName,
                JOptionPane.WARNING_MESSAGE
            );
            if (field != null) field.requestFocus();
            return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Checks that a String represents a valid decimal number.
     *
     * @param text the raw text to parse
     * @return true if parseable as a double, false otherwise (error popup shown)
     */
    public static boolean isNumber(String text) {
        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "Value is empty — please enter a valid number.",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        try {
            Double.parseDouble(text.trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                null,
                "\"" + text + "\" is not a valid number!\nOnly digits and an optional decimal point are allowed.",
                "Validation Error — Invalid Number",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Checks that a phone number is a 10-digit numeric string.
     *
     * @param phone the phone number text to validate
     * @return true if valid, false otherwise (error popup shown)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "Phone number cannot be empty!",
                "Validation Error — Phone",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        if (!phone.trim().matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(
                null,
                "\"" + phone + "\" is not a valid phone number!\n"
                + "Please enter exactly 10 digits (e.g. 0712345678).",
                "Validation Error — Phone",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }
}
