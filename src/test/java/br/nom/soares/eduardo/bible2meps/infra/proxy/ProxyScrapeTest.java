package br.nom.soares.eduardo.bible2meps.infra.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.domain.Proxy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ProxyScrapeTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProxyScrape proxyScrape;

    private String apiUrl =
            "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=1000&country=all&ssl=all&anonymity=elite";

    @Test
    void shouldReturnOneRandomProxy() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {

        String mockResponse = "127.0.0.1:8080\n192.168.0.1:8888\n10.0.0.1:8088";
        when(restTemplate.getForEntity(apiUrl, String.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        proxyScrape = new ProxyScrape(restTemplate);
        Proxy result = this.proxyScrape.getRandomProxy();

        List<Proxy> proxies = getProxies();

        assertEquals(3, proxies.size());
        assertTrue(proxies.contains(result));
    }

    @Test
    void shouldRemoveProxy() throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        String mockResponse = "127.0.0.1:8080";
        when(restTemplate.getForEntity(apiUrl, String.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        proxyScrape = new ProxyScrape(restTemplate);

        List<Proxy> proxies = getProxies();

        this.proxyScrape.getRandomProxy();
        int listSize = proxies.size();
        proxyScrape.removeProxy(new Proxy("127.0.0.1", 8080));
        int listSizeAfterRemove = proxies.size();
        assertEquals(listSizeAfterRemove, listSize - 1);
    }

    private List<Proxy> getProxies() {
        try {
            Class<?> clazz = proxyScrape.getClass();
            Field privatedProxies = clazz.getDeclaredField("proxies");
            privatedProxies.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<Proxy> proxies = (List<Proxy>) privatedProxies.get(proxyScrape);
            return proxies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
