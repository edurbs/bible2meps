package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import br.nom.soares.eduardo.bible2meps.application.format.ProxyListServer;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import br.nom.soares.eduardo.bible2meps.infra.parser.SiteConnection;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class YouVersionFormatBook {

    @NonNull
    private ProxyListServer proxyListServer;

    private SiteConnection siteConnection;

    @Getter
    private Document book;

    private String bookNameFromPage = "";
    private Logger logger = Logger.getLogger(getClass().getName());

    private List<YouVersionFormatChapter> youVersionFormatChapters = new ArrayList<>();

    public String execute(List<String> urls, BookName bookName, Runnable progress) {
        Elements bookChapters = new Elements();
        for (String url : urls) {
            logger.info(url);
            Element page = parsePage(url, bookName);
            if (page != null) {
                bookChapters.add(page);
            }
            progress.run();
        }
        book = Jsoup.parseBodyFragment(bookChapters.outerHtml());
        addBookNameAtSecondLine(bookNameFromPage);
        addBookCodeAtFirstLine(bookName);
        placeFootnotesAtEndOfBook();
        bookNameFromPage = "";
        return generateFinalHtml(book);
    }

    private String generateFinalHtml(Document book) {
        Element metaCharset = new Element(Tag.valueOf("meta"), "")
                .attr("charset", "utf-8");
        book.head().prependChild(metaCharset);
        return book.html();
    }

    private void placeFootnotesAtEndOfBook() {
        Elements bookFootnotes = new Elements();
        for (YouVersionFormatChapter chapter : youVersionFormatChapters) {
            List<Element> chapterFootnotes = chapter.getChapterFootnotes();
            bookFootnotes.addAll(chapterFootnotes);
            chapterFootnotes.clear();
        }
        Element footnoteDiv = new Element("div").addClass("footnoteDiv");
        for (Element footnote : bookFootnotes) {
            footnote.appendTo(footnoteDiv);
        }
        Element body = book.selectFirst("body");
        footnoteDiv.appendTo(body);
    }

    private Element parsePage(String url, BookName bookName) {
        siteConnection = new SiteConnection(proxyListServer);
        Document document = siteConnection.getDocument(url);
        if (document == null) {
            return null;
        }
        var youVersionFormatChapter = new YouVersionFormatChapter(document, bookName);
        youVersionFormatChapter.execute();
        if (bookNameFromPage.isEmpty()) {
            bookNameFromPage = getBookNameFromPage(document);
        }
        youVersionFormatChapters.add(youVersionFormatChapter);
        return youVersionFormatChapter.getChapter();
    }

    private String getBookNameFromPage(Element page) {
        Element bookNameElement = page.selectFirst("h1");
        if (bookNameElement == null) {
            return "";
        }
        String bookName = bookNameElement.text();
        String[] nameSplited = bookName.split(" ");
        StringBuilder bookNameBuilder = new StringBuilder();
        for (int i = 0; i < nameSplited.length - 1; i++) {
            bookNameBuilder.append(nameSplited[i] + " ");
        }
        return bookNameBuilder.toString().trim();
    }

    private void addBookCodeAtFirstLine(BookName bookName) {
        String bookCode = bookName.getMepsFormat().substring(0, 2);
        Element firstLine = new Element("div").addClass("bookCode").text("%"+bookCode);
        Element body = book.selectFirst("body");
        body.prependChild(firstLine);
    }

    private void addBookNameAtSecondLine(String bookNameFromPage) {
        Element bookNameDiv = new Element("div").addClass("bookName").text("%");
        Element bookNameBold = new Element("b").text(bookNameFromPage.toUpperCase());
        bookNameBold.appendTo(bookNameDiv);
        Element body = book.selectFirst("body");
        body.prependChild(bookNameDiv);
    }

}
