import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private List<Product> products = new ArrayList<>();

    public boolean insertProduct(Product p) {
        if (p == null) return false;
        products.add(p);
        return true;
    }

    public Product search(String productId) {
        for (Product p : products) if (p.getProductId().equals(productId)) return p;
        return null;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
}