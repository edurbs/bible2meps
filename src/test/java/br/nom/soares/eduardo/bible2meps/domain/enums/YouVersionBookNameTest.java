package br.nom.soares.eduardo.bible2meps.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class YouVersionBookNameTest {

    @Test
    void testGetName() {
        assertEquals("GEN", YouVersionBookName.BOOK_GEN.getName());
        assertEquals("JAS", YouVersionBookName.BOOK_JAS.getName());
    }

    @Test
    void testFromString() {
        assertEquals(YouVersionBookName.BOOK_GEN, YouVersionBookName.fromString("BOOK_01_GEN"));
        assertEquals(YouVersionBookName.BOOK_JAS,
                YouVersionBookName.fromString(BookName.BOOK_59_JAM.name()));
    }
}
