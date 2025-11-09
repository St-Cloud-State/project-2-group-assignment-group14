import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQty;

    // FIFO waitlist entries for this product
    private List<WaitlistEntry> waitlist = new ArrayList<>();

    public Product(String productId, String name, double price, int stockQty) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stockQty = stockQty;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStockQty() { return stockQty; }

    public void addStock(int qty) {
        if (qty > 0) stockQty += qty;
    }

    /**
     * Try to fulfill qtyRequested for clientId from stock.
     * Returns number actually fulfilled (0..qtyRequested).
     * If partial or zero fulfilled, caller handles waitlist logic.
     */
    public int fulfillFromStock(int qtyRequested) {
        if (qtyRequested <= 0) return 0;
        int fulfilled = Math.min(qtyRequested, stockQty);
        stockQty -= fulfilled;
        return fulfilled;
    }

    /** Add an entry to waitlist (append FIFO) */
    public void addToWaitlist(String clientId, int qty) {
        if (qty <= 0) return;
        waitlist.add(new WaitlistEntry(clientId, qty));
    }

    /**
     * Process a shipment: fill waitlist in FIFO order as much as possible.
     * Returns list of Invoice objects for each waitlist fill (non-empty when filling occurs).
     * Any leftover shipment qty after processing waitlist is added to stock.
     */
    public List<Invoice> processShipment(int qtyReceived) {
        List<Invoice> invoices = new ArrayList<>();
        if (qtyReceived <= 0) return invoices;

        int available = qtyReceived;
        Iterator<WaitlistEntry> it = waitlist.iterator();
        while (it.hasNext() && available > 0) {
            WaitlistEntry e = it.next();
            int need = e.getQty();
            int filled = Math.min(need, available);
            if (filled > 0) {
                double amount = filled * this.price;
                invoices.add(new Invoice(e.getClientId(), this.productId, filled, amount));
                available -= filled;
                if (filled >= need) {
                    it.remove();
                } else {
                    e.decreaseQty(filled);
                }
            }
        }
        // leftover becomes stock
        if (available > 0) stockQty += available;
        return invoices;
    }

    public List<WaitlistEntry> getWaitlist() {
        return new ArrayList<>(waitlist);
    }

    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Price=%.2f, Stock=%d, Waitlist=%d]",
                productId, name, price, stockQty, waitlist.size());
    }
}