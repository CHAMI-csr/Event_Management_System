/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package event_management_system;

/**
 *
 * @author chamika
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class DBConnect {
    public Connection con;
    
   public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/event_db", "root", "");
            
            return conn;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Error: \n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
   public static void main(String[] args) {
        Connection c = connect();
        if (c != null) {
            JOptionPane.showMessageDialog(null, "Database Connected Successfully!100%");
        }
    }
    public boolean isConnected() {
        try {
            return con != null && !con.isClosed();
        } catch (SQLException ex) {
            return false;
        }
    }
    
}
