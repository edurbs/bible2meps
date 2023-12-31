package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.web.client.RestTemplateBuilder;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import br.nom.soares.eduardo.bible2meps.infra.proxy.ProxyScrape;

@TestInstance(Lifecycle.PER_CLASS)
class YouVersionFormatBookTest {

    private YouVersionFormatBook youVersionFormatBook;
    String html = "";

    @BeforeAll
    void setup() {
        List<String> urls = new ArrayList<>();
        urls.add("https://www.bible.com/bible/2645/JOL.1.A21");
        urls.add("https://www.bible.com/bible/2645/JOL.2.A21");
        urls.add("https://www.bible.com/bible/2645/JOL.3.A21");
        BookName bookName = BookName.BOOK_29_JOE;
        ProxyListServer proxyListServer = new ProxyScrape(new RestTemplateBuilder().build());
        youVersionFormatBook = new YouVersionFormatBook(proxyListServer);
        html = youVersionFormatBook.execute(urls, bookName, this::mockProgress);
    }

    void mockProgress() {

    }

    @Test
    void shouldReturnTheCorrentNumberOfFoonotes() {
        Element book = youVersionFormatBook.getBook();
        Elements footnoteMeps = book.select("div.footnoteMeps");
        assertEquals(5, footnoteMeps.size());
    }

    @Test
    void shouldAddMetaCharset() {
        assertTrue(html.contains("<meta charset=\"utf-8\">"));
    }

    @Test
    void shouldReturnAnHtmlBook() {
        assertFalse(html.isBlank());
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
        assertEquals("footnoteDiv", lastDiv.className());
        Element penultimateDiv = divs.get(divs.size() - 2);
        assertEquals("ChapterContent_chapter__uvbXo", penultimateDiv.className());
    }
}
