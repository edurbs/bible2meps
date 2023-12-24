package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.nom.soares.eduardo.bible2meps.bible2meps.application.format.FormatBookAbstract;
import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class YouVersionFormatBook extends FormatBookAbstract {

    public YouVersionFormatBook(@NonNull List<String> urls, @NonNull BookName bookName) {
        super(urls, bookName);
    }

    private Elements bookChapters = new Elements();
    private String bookNameFromPage;
    private List<YouVersionFormatChapter> youVersionFormatChapters = new ArrayList<>();

    public void execute() {
        for (String url : urls) {
            bookChapters.add(parsePage(url));
        }
        book = Jsoup.parseBodyFragment(bookChapters.outerHtml());
        addBookNameAtSecondLine();
        addBookCodeAtFirstLine();
        placeFootnotesAtEndOfBook();
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
        youVersionFormatChapters.add(youVersionFormatChapter);
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
        Element body = book.selectFirst("body");
        body.prependChild(firstLine);
    }

    private void addBookNameAtSecondLine() {
        Element bookNameDiv = new Element("div").addClass("bookName").text("%");
        Element bookNameBold = new Element("b").text(bookNameFromPage.toUpperCase());
        bookNameBold.appendTo(bookNameDiv);
        Element body = book.selectFirst("body");
        body.prependChild(bookNameDiv);
    }

}
