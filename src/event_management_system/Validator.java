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
