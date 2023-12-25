package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YouVersionSiteTest {
    private static YouVersionSite youVersionSite;

    @BeforeAll
    public static void setup() {
        youVersionSite = new YouVersionSite();
    }

    @Test
    void testGetLanguages() {
        List<YouVersionSite.LanguageRecord> languages = youVersionSite.getLanguages();
        assertEquals("Portuguese (Brazil)", youVersionSite.getLanguageByTag(languages, "por").get().name());
        assertEquals("English", youVersionSite.getLanguageByTag(languages, "eng").get().name());
    }

    @Test
    void testGetBibles() {
        List<YouVersionSite.TranslationRecord> bibles = youVersionSite.getBibles("por");
        assertEquals("Biblia Almeida Século 21", youVersionSite.getTranslationById(bibles, "2645").get().localTitle());
        assertEquals("NVT", youVersionSite.getTranslationById(bibles, "1930").get().abbreviation());
    }
}
