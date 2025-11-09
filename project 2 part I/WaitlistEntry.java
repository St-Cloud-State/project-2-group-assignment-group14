public class WaitlistEntry {
    private String clientId;
    private int qty;

    public WaitlistEntry(String clientId, int qty) {
        this.clientId = clientId;
        this.qty = qty;
    }

    public String getClientId() { return clientId; }
    public int getQty() { return qty; }
    public void decreaseQty(int d) { qty = Math.max(0, qty - d); }

    public String toString() {
        return "WaitlistEntry[Client=" + clientId + ", Qty=" + qty + "]";
    }
}