package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class YouVersionSiteTest {
    @Test
    void testGetLanguagesMap() {
        YouVersionSite youVersionSite = new YouVersionSite();
        var languagesMap = youVersionSite.getLanguagesMap();
        assertEquals("Portuguese (Brazil)", languagesMap.get("por"));
        assertEquals("English", languagesMap.get("eng"));
    }

    @Test
    void testListBibles() {

    }
}
