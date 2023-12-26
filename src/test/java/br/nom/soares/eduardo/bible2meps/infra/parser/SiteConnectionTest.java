package br.nom.soares.eduardo.bible2meps.infra.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.infra.proxy.ProxyScrape;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class SiteConnectionTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ProxyScrape proxyScrapeMock;

    @InjectMocks
    private SiteConnection siteConnection;

    private String apiUrl =
            "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=http&timeout=1000&country=all&ssl=all&anonymity=elite";


    @Test
    void shouldGetDocumentNotNullWhenProxyPortIsZero() {
        String mockResponse = "127.0.0.1:0";
        when(restTemplateMock.getForEntity(apiUrl, String.class))
                .thenReturn(ResponseEntity.ok(mockResponse));
        proxyScrapeMock = new ProxyScrape(restTemplateMock);
        siteConnection = new SiteConnection(proxyScrapeMock);
        assertNotNull(siteConnection.getDocument("https://www.google.com.br"));
    }

    @Test
    void shouldGetDocumentNotNullWhenProxyHostIsNull() {
        String mockResponse = ":3128";
        when(restTemplateMock.getForEntity(apiUrl, String.class))
                .thenReturn(ResponseEntity.ok(mockResponse));
        proxyScrapeMock = new ProxyScrape(restTemplateMock);
        siteConnection = new SiteConnection(proxyScrapeMock);
        assertNotNull(siteConnection.getDocument("https://www.google.com.br"));
    }

    @Test
    void shouldGetDocumentNotNullWhenProxyIsValid() {
        // String mockResponse =
        // "47.88.3.19:8080\n127.0.0.1:0\n195.114.209.50:80\n189.240.60.168:9090";
        // when(restTemplateMock.getForEntity(apiUrl, String.class))
        // .thenReturn(ResponseEntity.ok(mockResponse));
        // proxyScrapeMock = new ProxyScrape(restTemplateMock);
        proxyScrapeMock = new ProxyScrape(new RestTemplateBuilder().build());
        siteConnection = new SiteConnection(proxyScrapeMock);
        assertNotNull(siteConnection.getDocument("https://www.google.com.br"));
    }


}
