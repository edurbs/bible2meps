package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class YouVersionSiteTest {

    Optional<YouVersionSite.LanguageRecord> getByLanguageTag(List<YouVersionSite.LanguageRecord> languageRecords,
            String languageTag) {
        return languageRecords.stream()
                .filter(language -> language.languageTag().equals(languageTag))
                .findFirst();

    }

    @Test
    void testGetLanguages() {
        YouVersionSite youVersionSite = new YouVersionSite();
        List<YouVersionSite.LanguageRecord> languages = youVersionSite.getLanguages();
        assertEquals("Portuguese (Brazil)", getByLanguageTag(languages, "por").get().name());
        assertEquals("English", getByLanguageTag(languages, "eng").get().name());
    }

    @Test
    void testListBibles() {

    }
}
