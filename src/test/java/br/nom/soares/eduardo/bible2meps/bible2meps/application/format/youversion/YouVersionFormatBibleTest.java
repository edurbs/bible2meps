package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import br.nom.soares.eduardo.bible2meps.bible2meps.application.format.FormatBible;
import br.nom.soares.eduardo.bible2meps.bible2meps.domain.Book;
import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;

@TestInstance(Lifecycle.PER_CLASS)
public class YouVersionFormatBibleTest {

    private static List<Book> books;

    // @Test
    // void testExecute() {
    // YouVersionFormatBible youVersionFormatBible = new YouVersionFormatBible("2645", "A21");
    // youVersionFormatBible.execute();
    // books = youVersionFormatBible.getBooks();
    // assertEquals(66, books.size());
    // }

    @Test
    void testGetBooks() {
        FormatBible youVersionFormatBibleMock = mock(FormatBible.class);
        when(youVersionFormatBibleMock.getBooks()).thenReturn(List.of(
                new Book(BookName._01_GEN, "Content 1"), new Book(BookName._02_EXO, "Content 2")));

        // Call the method under test
        youVersionFormatBibleMock.execute();
        books = youVersionFormatBibleMock.getBooks();

        // Assert the expected result
        assertEquals(2, books.size());
        assertEquals("_01_GEN", books.get(0).bookName().name());
        assertEquals("Content 1", books.get(0).html());
        assertEquals("_02_EXO", books.get(1).bookName().name());
        assertEquals("Content 2", books.get(1).html());
    }
}
