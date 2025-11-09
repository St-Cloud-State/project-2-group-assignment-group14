import java.util.List;

public class ClerkMenuState implements State {
    private final Context ctx;

    public ClerkMenuState(Context ctx) { this.ctx = ctx; }

    @Override public int getId() { return Context.CLERK; }

    @Override
    public void run() {
        var in = ctx.getScanner();
        var wh = ctx.getWarehouse();
        System.out.println("\n-- Clerk Menu --");
        System.out.println("1) Add client");
        System.out.println("2) Show products (qty & price)");
        System.out.println("3) Show clients");
        System.out.println("4) Show clients with outstanding balance");
        System.out.println("5) Record payment");
        System.out.println("6) Become a client");
        System.out.println("7) Logout");
        System.out.print("Choice: ");
        String c = in.nextLine().trim();

        switch (c) {
            case "1" -> {
                System.out.print("Client ID: ");
                String id = in.nextLine().trim();
                System.out.print("Name: ");
                String name = in.nextLine().trim();
                System.out.print("Address: ");
                String addr = in.nextLine().trim();
                wh.addClient(new Client(id, name, addr));
                System.out.println("Added client " + id);
            }
            case "2" -> {
                wh.printAllProducts();
            }
            case "3" -> {
                wh.printAllClients();
            }
            case "4" -> {
                System.out.println("Clients with outstanding balance (> 0):");
                List<Client> cs = wh.getClientList().getClients();
                boolean any = false;
                for (Client cl : cs) {
                    if (cl.getBalance() > 0.0) {
                        System.out.println("  " + cl);
                        any = true;
                    }
                }
                if (!any) System.out.println("  (none)");
            }
            case "5" -> {
                System.out.print("Client ID: ");
                String cid = in.nextLine().trim();
                System.out.print("Payment amount: ");
                double amt = Double.parseDouble(in.nextLine().trim());
                wh.recordPayment(cid, amt);
            }
            case "6" -> {
                System.out.print("Client ID: ");
                String cid = in.nextLine().trim();
                if (wh.getClientList().search(cid) != null) {
                    ctx.setCurrentClientId(cid);
                    ctx.setOrigin(Context.Origin.CLERK);
                    ctx.trigger(Context.EVT_BECOME_CLIENT);
                } else {
                    System.out.println("Client not found.");
                }
            }
            case "7" -> {
                ctx.trigger(Context.EVT_LOGOUT); // Clerk → Opening
            }
            default -> System.out.println("Invalid.");
        }
    }
}
