public class Invoice {
    private static int counter = 1;
    private String invoiceId;
    private String clientId;
    private String productId;
    private int qty;
    private double amount;

    public Invoice(String clientId, String productId, int qty, double amount) {
        this.invoiceId = "I" + (counter++);
        this.clientId = clientId;
        this.productId = productId;
        this.qty = qty;
        this.amount = amount;
    }

    public String getInvoiceId() { return invoiceId; }
    public String getClientId() { return clientId; }
    public String getProductId() { return productId; }
    public int getQty() { return qty; }
    public double getAmount() { return amount; }

    public String toString() {
        return String.format("Invoice[ID=%s, Client=%s, Product=%s, Qty=%d, Amount=%.2f]",
                invoiceId, clientId, productId, qty, amount);
    }
}