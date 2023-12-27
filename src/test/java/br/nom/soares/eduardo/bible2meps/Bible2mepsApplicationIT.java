package br.nom.soares.eduardo.bible2meps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import br.nom.soares.eduardo.bible2meps.application.format.FormatBible;
import br.nom.soares.eduardo.bible2meps.application.format.FormatBible.BibleParams;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import br.nom.soares.eduardo.bible2meps.application.format.SiteParser;
import br.nom.soares.eduardo.bible2meps.application.format.ZipFile;
import br.nom.soares.eduardo.bible2meps.infra.file.JavaZipFileImpl;
import br.nom.soares.eduardo.bible2meps.infra.parser.youversion.YouVersionFormatBook;
import br.nom.soares.eduardo.bible2meps.infra.parser.youversion.YouVersionSiteParser;
import br.nom.soares.eduardo.bible2meps.infra.proxy.ProxyScrape;
import jakarta.servlet.ServletContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = Bible2mepsApplication.class)
@WebAppConfiguration
public class Bible2mepsApplicationIT {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("javaZipFileImpl"));
    }

    @Test
    @Tag("integration")
    void shouldFormatA21Translation() throws IOException {

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ProxyListServer proxyScrape = new ProxyScrape(restTemplate);
        YouVersionFormatBook youVersionFormatBook = new YouVersionFormatBook(proxyScrape);
        SiteParser youVersionSiteParser = new YouVersionSiteParser(youVersionFormatBook);
        ZipFile zipFileHandler = new JavaZipFileImpl();
        String bibleId = "2645";
        String abbreviation = "A21";
        BibleParams bibleParams =
                new BibleParams(bibleId, abbreviation, youVersionSiteParser, zipFileHandler);
        FormatBible formatBible = new FormatBible(bibleParams);

        byte[] zipFile = formatBible.execute();

        File tempFile = File.createTempFile("temp" + abbreviation, ".zip");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        fileOutputStream.write(zipFile);
        fileOutputStream.close();

        assertNotNull(zipFile);
        assertTrue(tempFile.exists());
        assertEquals(1189, formatBible.getProgress());
    }

}
