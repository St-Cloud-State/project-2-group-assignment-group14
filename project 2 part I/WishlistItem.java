public class WishlistItem {
    private String productId;
    private int quantity;

    public WishlistItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int q) { if (q > 0) quantity = q; }

    public String toString() {
        return "WishlistItem[Product=" + productId + ", Qty=" + quantity + "]";
    }
}
