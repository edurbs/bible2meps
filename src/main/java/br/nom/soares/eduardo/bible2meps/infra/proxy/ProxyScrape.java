package br.nom.soares.eduardo.bible2meps.infra.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private void addProxyFromTxtFile() {
        // read a file from resources and add each line as proxy to the proxies list
        try {
            InputStream inputStream =
                    ProxyScrape.class.getClassLoader().getResourceAsStream("proxyList.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                proxies.add(new Proxy(parts[0], Integer.parseInt(parts[1])));
            }
            reader.close();
        } catch (IOException e) {
            // handle exception
        }

    }

    public void removeProxy(Proxy proxy) {
        proxies.remove(proxy);
        // TODO make tests
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
        addProxyFromTxtFile();
    }

    private String getText(String apiUrl) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }


}
