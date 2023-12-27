package br.nom.soares.eduardo.bible2meps.domain.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class YouVersionBookNameTest {

    @Test
    public void testGetName() {
        assertEquals("GEN", YouVersionBookName._GEN.getName());
        assertEquals("JAS", YouVersionBookName._JAS.getName());
    }

    @Test
    public void testFromString() {
        assertEquals(YouVersionBookName._GEN, YouVersionBookName.fromString("_01_GEN"));
        assertEquals(YouVersionBookName._JAS,
                YouVersionBookName.fromString(BookName.BOOK_59_JAM.name()));
    }
}
