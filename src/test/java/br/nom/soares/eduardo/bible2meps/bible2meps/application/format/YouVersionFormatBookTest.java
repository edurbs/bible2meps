package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;

@TestInstance(Lifecycle.PER_CLASS)
public class YouVersionFormatBookTest {

    private YouVersionFormatBook youVersionFormatBook;

    @BeforeAll
    void setup() {
        List<String> urls = new ArrayList<>();
        urls.add("https://www.bible.com/bible/2645/JOL.1.A21");
        urls.add("https://www.bible.com/bible/2645/JOL.2.A21");
        urls.add("https://www.bible.com/bible/2645/JOL.3.A21");
        BookName bookName = BookName._29_JOE;

        youVersionFormatBook = new YouVersionFormatBook(urls, bookName);
        youVersionFormatBook.execute();
    }

    @AfterAll
    void tearDown() {
        String html = youVersionFormatBook.getBook().outerHtml();
        System.out.println(html);
    }

    @Test
    void shouldAddTheBookCodeAtFirstLine() {
        Element htmlBook = youVersionFormatBook.getBook();
        String firstLine = htmlBook.selectFirst("div.bookCode").text();
        assertEquals("%29", firstLine);
    }

    @Test
    void shouldAddBibleBookNameAtSecondLine() {
        Element htmlBook = youVersionFormatBook.getBook();
        Element secondLine = htmlBook.selectFirst("div.bookName");
        Element bookNameDiv = new Element("div").addClass("bookName").text("%");
        Element bookNameBold = new Element("b").text("JOEL");
        bookNameBold.appendTo(bookNameDiv);
        assertEquals(bookNameDiv.outerHtml(), secondLine.outerHtml());
    }

    @Test
    void shouldAddFootnotesAtEndOfBook() {
        Element body = youVersionFormatBook.getBook().selectFirst("body");
        Elements divs = body.children();
        Element lastDiv = divs.getLast();
        assertTrue(lastDiv.className().equals("footnoteDiv"));
        Element penultimateDiv = divs.get(divs.size() - 2);
        assertTrue(penultimateDiv.className().equals("ChapterContent_chapter__uvbXo"));
    }
}
