package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class YouVersionFormatBook {

    @NonNull
    private List<String> urls;

    @NonNull
    private BookName bookName;

    private Element bookElement;
    private Elements bookChapters = new Elements();
    private String bookNameFromPage;

    public void execute() {
        for (String url : urls) {
            bookChapters.add(parsePage(url));
        }
        bookElement = Jsoup.parseBodyFragment(bookChapters.outerHtml());
        addBookNameAtSecondLine();
        addBookCodeAtFirstLine();
    }

    private Element parsePage(String url) {
        Element page;
        try {
            page = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        setBookNameFromPage(page);
        var youVersionFormatChapter = new YouVersionFormatChapter(page, bookName);
        youVersionFormatChapter.execute();
        return youVersionFormatChapter.getChapter();
    }

    private void setBookNameFromPage(Element page) {
        if (bookNameFromPage != null) {
            return;
        }
        bookNameFromPage = page.selectFirst("h1").text();
        String[] nameSplited = bookNameFromPage.split(" ");
        bookNameFromPage = "";
        for (int i = 0; i < nameSplited.length - 1; i++) {
            bookNameFromPage += nameSplited[i] + " ";
        }
        bookNameFromPage = bookNameFromPage.trim();
    }

    private void addBookCodeAtFirstLine() {
        Element firstLine = new Element("div").addClass("bookCode").text("%29");
        Element body = bookElement.selectFirst("body");
        body.prependChild(firstLine);
    }

    private void addBookNameAtSecondLine() {
        Element bookNameDiv = new Element("div").addClass("bookName").text("%");
        Element bookNameBold = new Element("b").text(bookNameFromPage.toUpperCase());
        bookNameBold.appendTo(bookNameDiv);
        Element body = bookElement.selectFirst("body");
        body.prependChild(bookNameDiv);
    }
}
