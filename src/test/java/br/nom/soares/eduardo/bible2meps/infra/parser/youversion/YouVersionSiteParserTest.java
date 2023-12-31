package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import br.nom.soares.eduardo.bible2meps.domain.Language;
import br.nom.soares.eduardo.bible2meps.domain.Translation;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import br.nom.soares.eduardo.bible2meps.infra.proxy.ProxyScrape;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class YouVersionSiteParserTest {

    @Mock
    private YouVersionFormatBook youVersionFormatBook;

    @Mock
    private ProxyScrape proxyScrapeMock;

    @InjectMocks
    private YouVersionSiteParser youVersionSiteParser;

    @BeforeAll
    void setup() {
        proxyScrapeMock = new ProxyScrape(new RestTemplateBuilder().build());
        youVersionFormatBook = new YouVersionFormatBook(proxyScrapeMock);
        youVersionSiteParser = new YouVersionSiteParser(youVersionFormatBook);
    }

    @Test
    void testFormatBook() {
        List<String> urls = new ArrayList<>();
        urls.add("https://example.com/url1");
        urls.add("https://example.com/url2");

        String expectedOutput = "formatted book";
        when(youVersionFormatBook.execute(urls, BookName.BOOK_01_GEN, null)).thenReturn(expectedOutput);

        assertEquals(expectedOutput, youVersionSiteParser.formatBook(urls, BookName.BOOK_01_GEN, null));
    }

    @Test
    void testGetLanguages() {
        List<Language> languages = youVersionSiteParser.getLanguages();
        assertEquals("Portuguese (Brazil)", Language.getByTag(languages, "por").get().name());
        assertEquals("English", Language.getByTag(languages, "eng").get().name());
    }

    @Test
    void testGetBibles() {
        List<Translation> bibles = youVersionSiteParser.getBibles("por");
        assertEquals("Biblia Almeida Século 21",
                Translation.getById(bibles, "2645").get().localTitle());
        assertEquals("NVT", Translation.getById(bibles, "1930").get().abbreviation());
    }

    @Test
    void testGetUrls() {
        // https://www.bible.com/bible/277/LEV.1.TB
        List<String> urls = youVersionSiteParser.getUrls(BookName.BOOK_03_LEV, "277", "TB");
        assertEquals(27, urls.size());
        assertEquals("https://www.bible.com/bible/277/LEV.1.TB", urls.get(0));
    }
}
