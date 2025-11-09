public class ClientMenuState implements State {
    private final Context ctx;

    public ClientMenuState(Context ctx) { this.ctx = ctx; }

    @Override public int getId() { return Context.CLIENT; }

    @Override
    public void run() {
        var in = ctx.getScanner();
        var wh = ctx.getWarehouse();
        String cid = ctx.getCurrentClientId();

        if (cid == null) {
            System.out.println("No client in context. Returning to Opening.");
            ctx.trigger(Context.EVT_LOGOUT);
            return;
        }

        System.out.println("\n-- Client Menu (Client: " + cid + ") --");
        System.out.println("1) Show client details");
        System.out.println("2) Show list of products (with price)");
        System.out.println("3) Show client transactions");
        System.out.println("4) Add item to wishlist");
        System.out.println("5) Display wishlist");
        System.out.println("6) Place an order");
        System.out.println("7) Logout");
        System.out.print("Choice: ");
        String c = in.nextLine().trim();

        switch (c) {
            case "1" -> {
                Client cl = wh.getClientList().search(cid);
                if (cl != null) System.out.println(cl);
                else System.out.println("Client not found.");
            }
            case "2" -> {
                // price already printed by Product.toString()
                wh.printAllProducts();
            }
            case "3" -> {
                wh.printInvoicesForClient(cid);
            }
            case "4" -> {
                System.out.print("Product ID: ");
                String pid = in.nextLine().trim();
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(in.nextLine().trim());
                Client cl = wh.getClientList().search(cid);
                Product p = wh.getProductCatalog().search(pid);
                if (cl == null) { System.out.println("Client not found."); break; }
                if (p  == null) { System.out.println("Product not found."); break; }
                cl.addOrUpdateWishlistItem(pid, qty);
                System.out.println("Added/updated wishlist.");
            }
            case "5" -> {
                wh.printClientWishlist(cid);
            }
            case "6" -> {
                wh.processClientOrder(cid);
            }
            case "7" -> {
                // clear client and go back to origin (Clerk or Opening)
                ctx.trigger(Context.EVT_LOGOUT);
            }
            default -> System.out.println("Invalid.");
        }
    }
}
