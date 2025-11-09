import java.util.ArrayList;
import java.util.List;

public class ClientList {
    private List<Client> clients = new ArrayList<>();

    public boolean insertClient(Client c) {
        if (c == null) return false;
        clients.add(c);
        return true;
    }

    public Client search(String clientId) {
        for (Client c : clients) if (c.getClientId().equals(clientId)) return c;
        return null;
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }
}