package br.nom.soares.eduardo.bible2meps.infra.proxy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.application.format.Proxy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ProxyScrapeTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProxyScrape proxyScrape;

    @Test
    public void shouldReturnOneRandomProxy() {
        String apiUrl =
                "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=1000&country=all&ssl=all&anonymity=elite";
        String mockResponse = "127.0.0.1:8080\n192.168.0.1:8888\n10.0.0.1:8088";
        when(restTemplate.getForEntity(apiUrl, String.class))
                .thenReturn(ResponseEntity.ok(mockResponse));

        proxyScrape = new ProxyScrape(restTemplate);

        Proxy result = this.proxyScrape.getRandomProxy();

        assertTrue(mockResponse.contains(result.host() + ":" + result.port()));
    }

}
