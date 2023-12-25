package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.Getter;

@Component
public class YouVersionFormatBook {

    @Getter
    private Element book;

    private String bookNameFromPage = "";

    private List<YouVersionFormatChapter> youVersionFormatChapters = new ArrayList<>();

    public String execute(List<String> urls, BookName bookName) {
        Elements bookChapters = new Elements();
        for (String url : urls) {
            Element page = parsePage(url, bookName);
            if (page != null) {
                bookChapters.add(page);
            }
        }
        book = Jsoup.parseBodyFragment(bookChapters.outerHtml());
        addBookNameAtSecondLine(bookNameFromPage);
        addBookCodeAtFirstLine();
        placeFootnotesAtEndOfBook();
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
        Element page;
        try {
            page = Jsoup.connect(url).get();
            var youVersionFormatChapter = new YouVersionFormatChapter(page, bookName);
            youVersionFormatChapter.execute();
            if (bookNameFromPage.isEmpty()) {
                bookNameFromPage = getBookNameFromPage(page);
            }
            youVersionFormatChapters.add(youVersionFormatChapter);
            return youVersionFormatChapter.getChapter();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

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

    private void addBookCodeAtFirstLine() {
        Element firstLine = new Element("div").addClass("bookCode").text("%29");
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
