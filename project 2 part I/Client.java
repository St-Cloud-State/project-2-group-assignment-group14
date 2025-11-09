import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Client {
    private String clientId;
    private String name;
    private String address;
    // balance: positive means client owes money (debit); negative means client has credit
    private double balance = 0.0;

    private List<WishlistItem> wishlist = new ArrayList<>();

    public Client(String clientId, String name, String address) {
        this.clientId = clientId;
        this.name = name;
        this.address = address;
    }

    public String getClientId() { return clientId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public double getBalance() { return balance; }

    public void debit(double amount) {
        if (amount <= 0) return;
        balance += amount; // increase amount owed
    }

    public void receivePayment(double amount) {
        if (amount <= 0) return;
        balance -= amount;
    }

    // Wishlist operations
    public void addOrUpdateWishlistItem(String productId, int qty) {
        if (qty <= 0) return;
        for (WishlistItem wi : wishlist) {
            if (wi.getProductId().equals(productId)) {
                wi.setQuantity(qty);
                return;
            }
        }
        wishlist.add(new WishlistItem(productId, qty));
    }

    public void removeWishlistItem(String productId) {
        wishlist.removeIf(wi -> wi.getProductId().equals(productId));
    }

    public List<WishlistItem> getWishlistItems() {
        return new ArrayList<>(wishlist);
    }

    // Used by order processing: reduces or removes item from wishlist
    public void reduceWishlistItem(String productId, int reduceQty) {
        Iterator<WishlistItem> it = wishlist.iterator();
        while (it.hasNext()) {
            WishlistItem wi = it.next();
            if (wi.getProductId().equals(productId)) {
                int remaining = wi.getQuantity() - reduceQty;
                if (remaining <= 0) it.remove();
                else wi.setQuantity(remaining);
                return;
            }
        }
    }

    public String toString() {
        return String.format("Client[ID=%s, Name=%s, Address=%s, Balance=%.2f]",
                clientId, name, address, balance);
    }
}