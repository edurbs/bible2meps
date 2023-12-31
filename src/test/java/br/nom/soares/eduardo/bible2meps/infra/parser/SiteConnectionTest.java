package br.nom.soares.eduardo.bible2meps.infra.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import br.nom.soares.eduardo.bible2meps.infra.proxy.ProxyScrape;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class SiteConnectionTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private ProxyScrape proxyScrapeMock;

    @InjectMocks
    private SiteConnection siteConnection;


    @Test
    void shouldGetDocumentNotNullWhenProxyIsValid() {
        restTemplateMock = new RestTemplateBuilder().build();
        proxyScrapeMock = new ProxyScrape(restTemplateMock);
        siteConnection = new SiteConnection(proxyScrapeMock);
        assertNotNull(siteConnection.getDocument("https://www.google.com.br"));
    }


}
