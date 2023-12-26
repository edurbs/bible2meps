package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    private Element book;

    private String bookNameFromPage = "";

    private List<YouVersionFormatChapter> youVersionFormatChapters = new ArrayList<>();

    public String execute(List<String> urls, BookName bookName, Runnable progress) {
        Elements bookChapters = new Elements();
        for (String url : urls) {
            System.out.println(url);
            Element page = parsePage(url, bookName);
            if (page != null) {
                bookChapters.add(page);
            }
            progress.run();
        }
        if (bookChapters.size() == 0) {
            return "";
        }
        book = Jsoup.parseBodyFragment(bookChapters.outerHtml());
        addBookNameAtSecondLine(bookNameFromPage);
        addBookCodeAtFirstLine(bookName);
        placeFootnotesAtEndOfBook();
        bookNameFromPage = "";
        return book.html();
    }

    private void placeFootnotesAtEndOfBook() {
        Elements footnotesElementList = new Elements();
        for (YouVersionFormatChapter chapter : youVersionFormatChapters) {
            footnotesElementList.addAll(chapter.getFootnotesElementList());
        }
        Element footnoteDiv = new Element("div").addClass("footnoteDiv");
        for (Element footnote : footnotesElementList) {
            footnote.appendTo(footnoteDiv);
        }
        Element body = book.selectFirst("body");
        footnoteDiv.appendTo(body);
    }

    private Element parsePage(String url, BookName bookName) {
        siteConnection = new SiteConnection(proxyListServer);
        Document document = siteConnection.getDocument(url);
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
        String bookNameFromPage = bookNameElement.text();
        String[] nameSplited = bookNameFromPage.split(" ");
        bookNameFromPage = "";
        for (int i = 0; i < nameSplited.length - 1; i++) {
            bookNameFromPage += nameSplited[i] + " ";
        }
        return bookNameFromPage.trim();
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
