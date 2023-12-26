package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import br.nom.soares.eduardo.bible2meps.domain.Superscription;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class YouVersionFormatChapter {

    @NonNull
    private Element page;

    @NonNull
    private BookName bookName;

    private List<Element> footnotesElementList = new ArrayList<>();
    private Element chapter;

    public void execute() {
        extractChapter();
        removeUnwantedNotes();
        removeUnwantedHeaders();
        extractFootnotes();
        formatScriptureNumberAsBold();
        addCurlyBracketsToChapterNumber();
        removeScriptureNumberOne();
        moveChapterNumberNextToScriptureNumberOne();
        addAtSignToHeadings();
        addDolarSignToSuperscription();
        addAmpersandToBookDivision();
        addPlusSignToHeadings();
        removeUnwantedSpaces();
        addStyles();
        page = Jsoup.parseBodyFragment(chapter.html());
    }

    private void addStyles() {
        Elements italics1 = page.select("span.ChapterContent_fqa__Xa2yn");
        italics1.attr("style", "font-style: italic");
        Elements italics2 = page.select("span.ChapterContent_tl__at1as");
        italics2.attr("style", "font-style: italic");
        Elements bolds1 = page.select("span.ChapterContent_fk__ZzZlQ");
        bolds1.attr("style", "font-weight: bold");
        Elements bolds2 = page.select("span.scriptureNumberBold");
        bolds2.attr("style", "font-weight: bold");
    }

    private void removeUnwantedSpaces() {
        Elements elements = chapter.getAllElements();
        String _200A = "\u200A";
        for (Element span : elements) {
            span.html(span.html().replace(_200A, ""));
            if (!span.hasText()) {
                span.remove();
            }
        }
    }

    private void addAtSignToHeadings() {
        Elements headings = chapter.select("div.ChapterContent_s1__bNNaW");
        for (Element heading : headings) {
            heading.text("@" + heading.text());
        }
    }

    private void removeUnwantedHeaders() {
        Elements headersWithPsalmDivision = chapter.select("div.ChapterContent_mr__Vxus8");
        headersWithPsalmDivision.remove();

        Elements headersWithCrosReferences = chapter.select("div.ChapterContent_r___3KRx");
        headersWithCrosReferences.remove();
    }

    private void addAmpersandToBookDivision() {
        Element bookDivision = chapter.selectFirst("div.ChapterContent_ms1__s_U5R");
        if (bookDivision == null) {
            return;
        }
        bookDivision.text("@" + bookDivision.text());
    }

    private void addDolarSignToSuperscription() {
        if (bookName != BookName._19_PSA) {
            return;
        }
        String stringChapterNumber = chapter.selectFirst("span.chapterNumber").wholeText();
        stringChapterNumber = stringChapterNumber.replace("{", "").replace("}", "").trim();
        int chapterNumber = Integer.parseInt(stringChapterNumber);
        boolean thisChapterHasSupercription = Superscription.thisChapterHas(chapterNumber);
        Element superscriptionElement = chapter.selectFirst("div.ChapterContent_d__OHSpy");
        if (thisChapterHasSupercription) {
            if (superscriptionElement == null || superscriptionElement.text().isEmpty()) {
                superscriptionElement = new Element("div").addClass("ChapterContent_d__OHSpy");
            }
            superscriptionElement.text("$" + superscriptionElement.text());
        } else {
            if (superscriptionElement != null) {
                superscriptionElement.remove();
            }
        }
    }

    private void addPlusSignToHeadings() {
        Elements headings = chapter.select(
                "div.ChapterContent_s1__bNNaW, div.ChapterContent_d__OHSpy, ChapterContent_ms1__s_U5R");
        for (Element heading : headings) {
            Elements nextSiblingElements = heading.nextElementSiblings();
            for (Element sibling : nextSiblingElements) {
                if ((sibling.classNames().contains("ChapterContent_m__3AINJ")
                        || sibling.classNames().contains("ChapterContent_p__dVKHb"))
                        && !sibling.text().matches("[@$&].*")
                        && sibling.previousElementSibling().text().matches("[@$&].*")) {
                    // Element startBlock = sibling.selectFirst("span.ChapterContent_verse__57FIw");
                    Elements siblingBlocks = sibling.select("span.ChapterContent_verse__57FIw");
                    for (Element siblingBlock : siblingBlocks) {
                        if (siblingBlock.hasText()) {
                            siblingBlock.firstChild().before("+");
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    private void moveChapterNumberNextToScriptureNumberOne() {
        Elements scriptureNumbers = chapter.select("span.scriptureNumberBold");
        Element scriptureNumberOne = scriptureNumbers.get(0);
        Element chapterNumber = chapter.selectFirst("span.chapterNumber");
        String chapterNumberText = chapterNumber.wholeText();
        chapterNumber.remove();
        Element newChapterNumber =
                new Element("span").addClass("chapterNumber").text(chapterNumberText);
        scriptureNumberOne.before(newChapterNumber);
    }

    private void removeScriptureNumberOne() {
        if (bookName.getNumberOfChapters() > 1) {
            Elements scriptureNumbers = chapter.select("span.scriptureNumberBold");
            scriptureNumbers.get(0).text("");
        }
    }

    private void addCurlyBracketsToChapterNumber() {
        Element chapterNumber = chapter.selectFirst("div.ChapterContent_label__R2PLt");
        String formatedChapterNumber = "{" + chapterNumber.text() + "} ";
        Element newChapterNumberElement =
                new Element("span").addClass("chapterNumber").text(formatedChapterNumber);
        chapterNumber.replaceWith(newChapterNumberElement);
    }

    private void formatScriptureNumberAsBold() {
        Elements scriptureNumbers = chapter.select("span.ChapterContent_label__R2PLt");
        for (int i = 0; i < scriptureNumbers.size(); i++) {
            Element scriptureNumber = scriptureNumbers.get(i);
            String scriptureNumberText = scriptureNumber.text();
            String scriptureText = scriptureNumber.parent().text();
            String startSpace = " ";
            if (scriptureText.startsWith(scriptureNumberText)) {
                startSpace = "";
            }
            Element scriptureNumberBoldWithSpace = new Element("span")
                    .addClass("scriptureNumberBold").text(startSpace + scriptureNumberText + " ");
            scriptureNumber.replaceWith(scriptureNumberBoldWithSpace);
        }
    }

    private void extractFootnotes() {
        Elements footnoteElements = chapter.select("span.ChapterContent_note__YlDW0");
        for (Element footnote : footnoteElements) {
            addFootnoteToList(footnote);
        }
        footnoteElements.replaceAll(element -> new Element("span").text("*"));
    }

    private void addFootnoteToList(Element footnote) {
        // String footnoteKey, String footnoteValue
        Element footnoteBody = footnote.selectFirst("span.ChapterContent_body__O3qjr");
        Element footnoteScriptureNumber = footnoteBody.selectFirst("span.ChapterContent_fr__0KsID");
        footnoteScriptureNumber.text("#" + footnoteScriptureNumber.text().replace(".", ":") + " ");
        handleMultipleScripturesFootnote(footnoteBody, footnoteScriptureNumber);
        Element newFootnote = new Element("span").addClass("footnoteMeps");
        newFootnote.appendChild(footnoteScriptureNumber);
        Elements children = footnoteBody.children();
        for (Element child : children) {
            newFootnote.appendChild(child);
        }
        newFootnote.appendElement("br");
        footnotesElementList.add(newFootnote);
    }

    private void handleMultipleScripturesFootnote(Element footnoteBody,
            Element footnoteScriptureNumber) {
        if (footnoteScriptureNumber.text().contains(",")) {
            String[] scriptureNumbers = footnoteScriptureNumber.text().split(",");
            footnoteScriptureNumber.text(scriptureNumbers[0] + " ");
            Element newVideSpan =
                    new Element("span").addClass("ChapterContent_body__O3qjr").text(" Vide ");
            footnoteBody.appendChild(newVideSpan);
            for (int i = 1; i < scriptureNumbers.length; i++) {
                Element newSpan = new Element("span").addClass("ChapterContent_body__O3qjr");
                boolean lastI = i == scriptureNumbers.length - 1;
                newSpan.text(scriptureNumbers[i] + (lastI ? "." : ", "));
                footnoteBody.appendChild(newSpan);
            }
        }
    }

    private void removeUnwantedNotes() {
        Elements crossReferences = chapter.select("span.ChapterContent_x__tsTlk");
        for (Element crossReference : crossReferences) {
            crossReference.remove();
        }
    }

    private void extractChapter() {
        chapter = page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }

}
