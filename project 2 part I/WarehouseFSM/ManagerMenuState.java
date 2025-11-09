public class ManagerMenuState implements State {
    private final Context ctx;

    public ManagerMenuState(Context ctx) { this.ctx = ctx; }

    @Override public int getId() { return Context.MANAGER; }

    @Override
    public void run() {
        var in = ctx.getScanner();
        var wh = ctx.getWarehouse();

        System.out.println("\n-- Manager Menu --");
        System.out.println("1) Add product");
        System.out.println("2) Display waitlist for a product");
        System.out.println("3) Receive a shipment");
        System.out.println("4) Become a Clerk");
        System.out.println("5) Logout");
        System.out.print("Choice: ");
        String c = in.nextLine().trim();

        switch (c) {
            case "1" -> {
                System.out.print("Product ID: ");
                String pid = in.nextLine().trim();
                System.out.print("Name: ");
                String name = in.nextLine().trim();
                System.out.print("Unit price: ");
                double price = Double.parseDouble(in.nextLine().trim());
                System.out.print("Quantity in stock: ");
                int qty = Integer.parseInt(in.nextLine().trim());
                wh.addProduct(new Product(pid, name, price, qty));
                System.out.println("Added product " + pid);
            }
            case "2" -> {
                System.out.print("Product ID: ");
                String pid = in.nextLine().trim();
                wh.printProductWaitlist(pid);
            }
            case "3" -> {
                System.out.print("Product ID: ");
                String pid = in.nextLine().trim();
                System.out.print("Shipment quantity: ");
                int qty = Integer.parseInt(in.nextLine().trim());
                wh.receiveShipment(pid, qty);
            }
            case "4" -> {
                ctx.trigger(Context.EVT_BECOME_CLERK);
            }
            case "5" -> {
                ctx.trigger(Context.EVT_LOGOUT); // Manager → Opening
            }
            default -> System.out.println("Invalid.");
        }
    }
}
