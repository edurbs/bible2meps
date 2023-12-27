package br.nom.soares.eduardo.bible2meps.domain.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BookNameTest {

    @Test
    void testGetMepsFormat() {
        Assertions.assertEquals("01_GEN", BookName.BOOK_01_GEN.getMepsFormat());
        Assertions.assertEquals("40_MAT", BookName.BOOK_40_MAT.getMepsFormat());
    }

    @Test
    void testGetNumberOfChapters() {
        int chapterPerBook[] = {50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10,
                42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16,
                24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22};
        for (BookName bookName : BookName.values()) {
            Assertions.assertEquals(chapterPerBook[bookName.ordinal()],
                    bookName.getNumberOfChapters());
        }
    }

    @Test
    void testGetNumberOfScriptures() {
        Assertions.assertEquals(176, BookName.BOOK_19_PSA.getNumberOfScriptures(119));
    }

    @Test
    void testGetOrdinalValueByName() {
        Assertions.assertEquals(40, BookName.BOOK_01_GEN.getOrdinalValue("BOOK_40_MAT"));
    }

    @Test
    void testGetOrdinalValue() {
        Assertions.assertEquals(40, BookName.BOOK_40_MAT.getOrdinalValue());
    }

    @Test
    void testGetScriptures() {
        Assertions.assertEquals(150, BookName.BOOK_19_PSA.getScriptures().length);
    }

    @Test
    void testValueOf() {
        Assertions.assertEquals(BookName.BOOK_19_PSA, BookName.valueOf("BOOK_19_PSA"));
    }

    @Test
    void testValues() {
        Assertions.assertEquals(66, BookName.values().length);
    }
}
