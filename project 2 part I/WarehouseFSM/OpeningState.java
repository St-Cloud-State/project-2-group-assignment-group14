public class OpeningState implements State {
    private final Context ctx;

    public OpeningState(Context ctx) { this.ctx = ctx; }

    @Override public int getId() { return Context.OPENING; }

    @Override
    public void run() {
        var in = ctx.getScanner();
        System.out.println("\n-- Opening/Login --");
        System.out.println("1) Login as Clerk");
        System.out.println("2) Login as Manager");
        System.out.println("3) Login as Client (enter ClientID)");
        System.out.print("Choice: ");
        String choice = in.nextLine().trim();

        switch (choice) {
            case "1" -> {
                ctx.setOrigin(Context.Origin.OPENING);
                ctx.trigger(Context.EVT_LOGIN_CLERK);
            }
            case "2" -> {
                ctx.setOrigin(Context.Origin.OPENING);
                ctx.trigger(Context.EVT_LOGIN_MANAGER);
            }
            case "3" -> {
                System.out.print("Enter Client ID: ");
                String cid = in.nextLine().trim();
                if (ctx.getWarehouse().getClientList().search(cid) != null) {
                    ctx.setCurrentClientId(cid);
                    ctx.setOrigin(Context.Origin.OPENING);
                    ctx.trigger(Context.EVT_LOGIN_CLIENT);
                } else {
                    System.out.println("Client not found.");
                }
            }
            default -> System.out.println("Invalid.");
        }
    }
}
