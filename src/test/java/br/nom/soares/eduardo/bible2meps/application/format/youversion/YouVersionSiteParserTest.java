package br.nom.soares.eduardo.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import br.nom.soares.eduardo.bible2meps.domain.Language;
import br.nom.soares.eduardo.bible2meps.domain.Translation;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import br.nom.soares.eduardo.bible2meps.infra.parser.youversion.YouVersionSiteParser;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YouVersionSiteParserTest {
    private static YouVersionSiteParser youVersionSiteParser;

    @BeforeAll
    public static void setup() {
        youVersionSiteParser = new YouVersionSiteParser();
    }

    @Test
    void testFormatBook() {
        String html = youVersionSiteParser
                .formatBook(List.of("https://www.bible.com/bible/277/LEV.1.TB"), BookName._03_LEV);
        assertFalse(html.isBlank());
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
        List<String> urls = youVersionSiteParser.getUrls(BookName._03_LEV, "277", "TB");
        assertEquals(27, urls.size());
        assertEquals("https://www.bible.com/bible/277/LEV.1.TB", urls.get(0));
    }
}
