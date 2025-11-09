import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private ProductCatalog productCatalog = new ProductCatalog();
    private ClientList clientList = new ClientList();
    private List<Invoice> invoices = new ArrayList<>();

    public ProductCatalog getProductCatalog() { return productCatalog; }
    public ClientList getClientList() { return clientList; }

    // Add product/client helpers
    public boolean addProduct(Product p) { return productCatalog.insertProduct(p); }
    public boolean addClient(Client c) { return clientList.insertClient(c); }

    // Place a client's order: attempt to buy everything in client's wishlist.
    // Fulfilled qty is debited to client; unfulfilled qty is added to product waitlist (full-version behavior).
    public void processClientOrder(String clientId) {
        Client client = clientList.search(clientId);
        if (client == null) {
            System.out.println("Client not found: " + clientId);
            return;
        }
        List<WishlistItem> wishlist = client.getWishlistItems();
        if (wishlist.isEmpty()) {
            System.out.println("Client " + clientId + " wishlist is empty.");
            return;
        }

        for (WishlistItem wi : wishlist) {
            Product prod = productCatalog.search(wi.getProductId());
            if (prod == null) {
                System.out.println("Product not found: " + wi.getProductId());
                continue;
            }
            int requested = wi.getQuantity();
            int fulfilled = prod.fulfillFromStock(requested);
            if (fulfilled > 0) {
                double amount = fulfilled * prod.getPrice();
                client.debit(amount); // debit client for fulfilled portion
                // create invoice record for record-keeping (optionally)
                Invoice inv = new Invoice(clientId, prod.getProductId(), fulfilled, amount);
                invoices.add(inv);
                System.out.printf("Fulfilled %d of %s for %s; charged %.2f%n", fulfilled, prod.getProductId(), clientId, amount);
            }
            int remainder = requested - fulfilled;
            if (remainder > 0) {
                // Add remainder to product waitlist (full version)
                prod.addToWaitlist(clientId, remainder);
                System.out.printf("Unfulfilled %d of %s for %s; added to waitlist%n", remainder, prod.getProductId(), clientId);
            }
            // After processing, remove wishlist item (either fully satisfied or waitlisted)
            client.reduceWishlistItem(wi.getProductId(), requested); // remove or adjust
        }

        System.out.println("Order processing complete for " + clientId);
    }

    // Process a shipment: product fills waitlist first; warehouse records invoices and debits clients
    public void receiveShipment(String productId, int qtyReceived) {
        Product prod = productCatalog.search(productId);
        if (prod == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        List<Invoice> generated = prod.processShipment(qtyReceived);
        if (generated.isEmpty()) {
            System.out.println("Shipment processed; no waitlist fills. Remaining added to stock.");
        } else {
            System.out.println("Shipment processed; invoices generated for waitlist fills:");
            for (Invoice inv : generated) {
                invoices.add(inv);
                // debit the associated client
                Client client = clientList.search(inv.getClientId());
                if (client != null) {
                    client.debit(inv.getAmount());
                    System.out.println("  - " + inv + " (client debited)");
                } else {
                    System.out.println("  - " + inv + " (client not found to debit)");
                }
            }
        }
    }

    public void recordPayment(String clientId, double amount) {
        Client client = clientList.search(clientId);
        if (client == null) {
            System.out.println("Client not found: " + clientId);
            return;
        }
        client.receivePayment(amount);
        System.out.printf("Recorded payment of %.2f for %s%n", amount, clientId);
    }

    // Queries
    public void printAllClients() {
        for (Client c : clientList.getClients()) System.out.println(c);
    }

    public void printAllProducts() {
        for (Product p : productCatalog.getAllProducts()) System.out.println(p);
    }

    public void printClientWishlist(String clientId) {
        Client client = clientList.search(clientId);
        if (client == null) {
            System.out.println("Client not found: " + clientId);
            return;
        }
        System.out.println("Wishlist for " + client.getName() + ":");
        List<WishlistItem> w = client.getWishlistItems();
        if (w.isEmpty()) System.out.println("  (none)");
        else for (WishlistItem wi : w) System.out.println("  " + wi);
    }

    public void printProductWaitlist(String productId) {
        Product p = productCatalog.search(productId);
        if (p == null) {
            System.out.println("Product not found: " + productId);
            return;
        }
        System.out.println("Waitlist for product " + productId + ":");
        List<WaitlistEntry> wl = p.getWaitlist();
        if (wl.isEmpty()) System.out.println("  (none)");
        else for (WaitlistEntry e : wl) System.out.println("  " + e);
    }

    public void printInvoicesForClient(String clientId) {
        System.out.println("Invoices for " + clientId + ":");
        boolean found = false;
        for (Invoice inv : invoices) {
            if (inv.getClientId().equals(clientId)) {
                System.out.println("  " + inv);
                found = true;
            }
        }
        if (!found) System.out.println("  (none)");
    }
}