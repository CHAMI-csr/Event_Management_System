package event_management_system;

/**
 * Singleton session store for the currently logged-in staff member.
 * All fields are intentionally public static so every form can read/write
 * them without passing references around.
 *
 * @author chamika
 */
public class UserSession {

    /** Role of the logged-in user (e.g. "admin", "Event Planner", …). */
    public static String loggedUserRole = "";

    /** Display name of the logged-in user. */
    public static String loggedUserName = "";

    /** Clears the session — call this on logout. */
    public static void clear() {
        loggedUserRole = "";
        loggedUserName = "";
    }

    private UserSession() { /* static-only utility */ }
}
