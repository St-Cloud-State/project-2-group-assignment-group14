import java.util.Scanner;

public class Context {
    // ----- State IDs -----
    public static final int OPENING = 0;
    public static final int CLERK   = 1;
    public static final int MANAGER = 2;
    public static final int CLIENT  = 3;

    // ----- Event IDs -----
    public static final int EVT_LOGIN_CLERK    = 0;
    public static final int EVT_LOGIN_MANAGER  = 1;
    public static final int EVT_LOGIN_CLIENT   = 2;
    public static final int EVT_BECOME_CLERK   = 3;
    public static final int EVT_BECOME_CLIENT  = 4;
    public static final int EVT_LOGOUT         = 5;

    // ----- Special codes -----
    private static final int TO_ORIGIN = 99;
    private static final int SAME      = -1;

    // ----- Transition rows for each state -----
    // [event0, event1, event2, event3, event4, event5]
    // index positions correspond to event constants
    private static final int[] CLERK_ROW   = { SAME, SAME, SAME, SAME, CLIENT, OPENING };
    private static final int[] MANAGER_ROW = { SAME, SAME, SAME, CLERK, SAME,  OPENING };
    private static final int[] CLIENT_ROW  = { SAME, SAME, SAME, SAME, SAME,  TO_ORIGIN };

    // ----- Instance variables -----
    private final Warehouse warehouse = new Warehouse();
    private final Scanner in = new Scanner(System.in);
    private final State[] states = new State[4];
    private State current;
    private Origin origin = Origin.OPENING;
    private String currentClientId = null;

    // ----- Constructor -----
    public Context() {
        states[OPENING] = new OpeningState(this);
        states[CLERK]   = new ClerkMenuState(this);
        states[MANAGER] = new ManagerMenuState(this);
        states[CLIENT]  = new ClientMenuState(this);
        current = states[OPENING];
    }

    // ----- Getters -----
    public Warehouse getWarehouse() { return warehouse; }
    public Scanner getScanner() { return in; }
    public String getCurrentClientId() { return currentClientId; }
    public void setCurrentClientId(String id) { currentClientId = id; }
    public Origin getOrigin() { return origin; }
    public void setOrigin(Origin o) { origin = o; }

    // ----- FSM Transition Resolver -----
    private int resolveNext(int stateId, int event) {
        switch (stateId) {
            case OPENING:
                switch (event) {
                    case EVT_LOGIN_CLERK:   return CLERK;
                    case EVT_LOGIN_MANAGER: return MANAGER;   // ✅ Fix for option 2
                    case EVT_LOGIN_CLIENT:  return CLIENT;
                    default: return SAME;
                }

            case CLERK:
                return CLERK_ROW[event];

            case MANAGER:
                return MANAGER_ROW[event];

            case CLIENT:
                return CLIENT_ROW[event];

            default:
                return SAME;
        }
    }

    // ----- Event trigger -----
    public void trigger(int event) {
        int stateId = current.getId();
        int next = resolveNext(stateId, event);

        if (next == SAME) return;

        // Special rule: client logout returns to origin (clerk/manager/opening)
        if (stateId == CLIENT && event == EVT_LOGOUT && next == TO_ORIGIN) {
            switch (origin) {
                case CLERK   -> setState(CLERK);
                case MANAGER -> setState(MANAGER);
                default      -> setState(OPENING);
            }
            currentClientId = null;
            return;
        }

        setState(next);
    }

    // ----- State management -----
    public void setState(int id) {
        if (id >= 0 && id < states.length) {
            current = states[id];
        }
    }

    // ----- Main FSM loop -----
    public void run() {
        System.out.println("=== Warehouse FSM (Part I) ===");
        while (true) {
            current.run();
        }
    }

    // ----- Origin Enum -----
    public enum Origin { OPENING, CLERK, MANAGER }
}
