package br.nom.soares.eduardo.bible2meps.infra.proxy;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import br.nom.soares.eduardo.bible2meps.domain.Proxy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyScrape implements ProxyListServer {
    private List<Proxy> proxies = new ArrayList<>();
    private SecureRandom random = new SecureRandom();

    @NonNull
    private RestTemplate restTemplate;

    public Proxy getRandomProxy() {
        if (proxies.isEmpty()) {
            readApi();
        }
        int index = this.random.nextInt(proxies.size());
        return proxies.get(index);
    }

    public void removeProxy(Proxy proxy) {
        proxies.remove(proxy);
    }

    private void readApi() {
        String apiUrl =
                "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=1000&country=all&ssl=all&anonymity=elite";
        String text = getText(apiUrl);
        if (text!=null) {
            String[] lines = text.split("\\r?\\n");
            for (String line : lines) {
                String[] parts = line.split(":");
                Proxy proxy = new Proxy(parts[0], Integer.parseInt(parts[1]));
                proxies.add(proxy);
            }
        }
    }

    private String getText(String apiUrl) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }


}
