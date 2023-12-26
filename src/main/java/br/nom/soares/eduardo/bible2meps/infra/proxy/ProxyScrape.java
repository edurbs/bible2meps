package br.nom.soares.eduardo.bible2meps.infra.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.application.format.Proxy;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProxyScrape implements ProxyListServer {
    private List<Proxy> proxies = new ArrayList<>();

    @NonNull
    private RestTemplate restTemplate;

    public Proxy getRandomProxy() {
        if (proxies.isEmpty()) {
            readApi();
        }
        int index = new Random().nextInt(proxies.size());
        return proxies.get(index);
    }

    public void removeProxy(Proxy proxy) {
        proxies.remove(proxy);
        System.out.println("removed proxy: " + proxy);
    }

    public void readApi() {
        String apiUrl =
                "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=1000&country=all&ssl=all&anonymity=elite";
        String text = getText(apiUrl);
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            Proxy proxy = new Proxy(parts[0], Integer.parseInt(parts[1]));
            proxies.add(proxy);
        }
        proxies.add(new Proxy("", 0));
    }

    private String getText(String apiUrl) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }


}
