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

    private static final String DIVISOR_FOR_POETIC_TEXT = "ChapterContent_b__BLNfi";

    private static final String POETIC_TEXT_2 = "ChapterContent_q2__Z9WWu";

    private static final String POETIC_TEXT_1 = "ChapterContent_q__EZOnh";

    private static final String SPAN_SCRIPTURE_NUMBER_BOLD = "span.scriptureNumberBold";

    @NonNull
    private Element page;

    @NonNull
    private BookName bookName;

    private List<Element> chapterFootnotes = new ArrayList<>();
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
        addSoftReturnAtEachLineOfPoeticText();
        addSoftReturnToEndOfLinePrecedingPoeticTextWhenStartsInMiddleOfVerse();
        removeUnwantedSpaces();
        handleDivergentNumberOfScriptures();
        addStyles();
        page = Jsoup.parseBodyFragment(chapter.html());
    }

    private void addSoftReturnToEndOfLinePrecedingPoeticTextWhenStartsInMiddleOfVerse() {
        /*
         * lê todos DIVISORES ChapterContent_b__BLNfi
         *      se o próximo sibling for um texto poético
         *          criar um <P> como primeiro filhosdo DIVISOR
         *          colocar num <P> todos siblings até o próximo DIVISOR
         *          se o código usfm do do sibling não estiver vazio (texto poético começou no meio)
         *              colocar num <P> o sibling anterior ao DIVISOR
         *          senão (é porque o texto poético começa no começo do versículo)
         *              se for um versículo maior que 1
         *                  colocar um sinal de = antes do versículo
         *              se for o versículo 1
         *                  se for o capitulo maior que 1
         *                      colocar um sinal de = antes do capítulo
         *                  se for o capítulo 1
         *                      se o versículo 2 também for poético
         *                          colocar um sinal de = antes do versículo 2
         */
        // reads all divisors
        Elements divisors = chapter.select("div." + DIVISOR_FOR_POETIC_TEXT);
        if(divisors.isEmpty()) {
            // psalm 1, 2, etc... (all verses are poetics)
            return;
        }
        for (Element divisor : divisors) {
            Element nextSibling = divisor.nextElementSibling();
            if (nextSibling == null) {
                continue;
            }
            Element previousSiblingOfDivisor = divisor.previousElementSibling();
            // if the next one is a poetic text
            if (nextSibling.tagName().equals("span")
                    && (nextSibling.hasClass(POETIC_TEXT_1)
                            || nextSibling.hasClass(POETIC_TEXT_2)
                    )) {
                Element poeticText = nextSibling;
                Element paragraph = new Element("p");
                divisor.before(paragraph);
                Elements nextSiblingsOfDivisor = divisor.nextElementSiblings();
                // add to <P> the next paragraphs until the next divisor
                for (Element nextSiblingOfDivisor : nextSiblingsOfDivisor) {
                    if (nextSiblingOfDivisor.hasClass(DIVISOR_FOR_POETIC_TEXT)) {
                        break;
                    }
                    paragraph.appendChild(nextSiblingOfDivisor);
                }
                Elements usfmElements = poeticText.select("span[data-usfm]");
                if (usfmElements.isEmpty()) {
                    continue;
                }
                String usfmText = usfmElements.getFirst().text();
                // if the usfm code text is not empty
                if (!usfmText.isBlank()) { // poetic text started in the middle
                    // add the paragraph before the poetic text as first child of <P>
                    if (previousSiblingOfDivisor != null) {
                        Element newElement = new Element("span");
                        previousSiblingOfDivisor.append("<br>");
                        newElement.html(previousSiblingOfDivisor.html());
                        paragraph.prependChild(newElement);
                        previousSiblingOfDivisor.remove();
                    }
                } else { // poetical text started at the beginning
                    // if this scripture is bigger than 1
                    Element scriptureElement = poeticText.selectFirst(SPAN_SCRIPTURE_NUMBER_BOLD);
                    if(scriptureElement == null) {
                        continue;
                    }
                    int scriptureNumber = Integer.parseInt(scriptureElement.text());
                    if (scriptureNumber > 1) {
                        scriptureElement.prependText("=");
                    } else if (scriptureNumber == 1) {
                        int chapterNumber = getChapterNumber();
                        if (chapterNumber > 1) {
                            Element chapterElement =
                                    chapter.selectFirst("span.ChapterContent_label__R2PLt");
                            if (chapterElement == null) {
                                continue;
                            }
                            chapterElement.prependText("=");
                        } else if (chapterNumber == 1) {
                            Elements scripturesElements =
                                    chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
                            for (Element scripture : scripturesElements) {
                                // if the scripture is 2
                                if (scripture.text().equals("2")){
                                    Element scriptureParent = scripture.parent();
                                    if (scriptureParent == null) {
                                        continue;
                                    }
                                    Element scriptureGrandParent = scriptureParent.parent();
                                    // if the scripture 2 is poetic
                                    if(scriptureGrandParent != null
                                        && ( scriptureGrandParent.hasClass(POETIC_TEXT_1)
                                                    || scriptureGrandParent
                                                            .hasClass(POETIC_TEXT_2))) {
                                        scripture.prependText("=");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        /* Elements poeticsTexts =
                chapter.select("span.ChapterContent_q__EZOnh, span.ChapterContent_q2__Z9WWu");
        if(poeticsTexts.isEmpty()) {
            return;
        }
        Element firstPoeticText = poeticsTexts.first();
        Element spanUsfm = firstPoeticText.selectFirst("span[data-usfm]");
        String usfmValueOfFirstPoeticLine = spanUsfm.attr("data-usfm");
        Elements previousElementSiblings = firstPoeticText.previousElementSiblings();
        for (Element previousElementSibling : previousElementSiblings){
            Elements previousUsfms = previousElementSibling.select("span[data-usfm]");
            if(previousUsfms.isEmpty()) {
                continue;
            }
            String previousUsfmValue = previousUsfms.last().attr("data-usfm");
            if (!previousElementSibling.text().isBlank() && previousUsfmValue.equals(usfmValueOfFirstPoeticLine)) {
                Element pointToAddTheParagraph = previousElementSibling.previousElementSibling();
                Element paragraph = new Element("p");
                pointToAddTheParagraph.after(paragraph);
                Element span = new Element("span").addClass("ChapterContent_q__EZOnh");
                span.html(previousElementSibling.html());
                previousElementSibling.replaceWith(span);
                Elements newPoeticsTexts = chapter.select("span.ChapterContent_q__EZOnh");
                for (Element poeticText : newPoeticsTexts) {
                    if (poeticText.equals(newPoeticsTexts.first())) {
                        poeticText.append("<br>");
                    }
                    paragraph.appendChild(poeticText.clone());
                    poeticText.remove();
                }
            }
        } */
    }

    private void addSoftReturnAtEachLineOfPoeticText() {
        Elements divPoeticLines = chapter.select("div."+POETIC_TEXT_1+", div."+POETIC_TEXT_2);
        Element lastDiv = divPoeticLines.last();
        if(lastDiv == null) {
            return;
        }
        for (Element divPoeticLine : divPoeticLines) {
            Element span = new Element("span").addClass(POETIC_TEXT_1);
            if (!divPoeticLine.equals(lastDiv)) {
                divPoeticLine.append("<br>");
            }
            span.html(divPoeticLine.html());
            divPoeticLine.replaceWith(span);
        }
    }

    private void handleDivergentNumberOfScriptures() {
        int chapterNumber = getChapterNumber();
        int totalScriptureNumbers = bookName.getNumberOfScriptures(chapterNumber);
        Elements scriptureNumbersInPage = chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
        int totalScriptureNumbersInPage = scriptureNumbersInPage.size();
        int diff = totalScriptureNumbersInPage - totalScriptureNumbers;
        if (diff > 0) {
            convertLastScripturesToSuperscript(scriptureNumbersInPage, diff);
        } else if (diff < 0) {
            addBlankScriptureAfter(scriptureNumbersInPage, diff);
        }
    }

    private void convertLastScripturesToSuperscript(Elements listScriptures, int diff) {
        int size = listScriptures.size();
        int listEnd = size - 1;
        int listStop = listEnd - diff;
        for (int i = listEnd; i > listStop; i--) {
            Element scriptureToConvert = listScriptures.get(i);
            Element superscriptElement = new Element("sup").text(scriptureToConvert.text());
            scriptureToConvert.replaceWith(superscriptElement);
        }
    }

    private void addBlankScriptureAfter(Elements listScriptures, int diff) {
        int scriptureNumber = listScriptures.size();
        int stop = diff * -1;
        Element last = listScriptures.last();
        if(last != null) {
            for (int i = 1; i <= stop; i++) {
                Element space = new Element("span").text(" ");
                Element blankScripture = new Element("span")
                        .text(scriptureNumber + i + " ")
                        .addClass("scriptureNumberBold")
                        .attr("style", "font-weight: bold");
                Element dash = new Element("span").text(" —— ");
                last.parent().after(space);
                last.parent().after(blankScripture);
                last.parent().after(dash);
            }
        }
    }

    private void addStyles() {
        Elements italics1 = page.select("span.ChapterContent_fqa__Xa2yn");
        italics1.attr("style", "font-style: italic");
        Elements italics2 = page.select("span.ChapterContent_tl__at1as");
        italics2.attr("style", "font-style: italic");
        Elements bolds1 = page.select("span.ChapterContent_fk__ZzZlQ");
        bolds1.attr("style", "font-weight: bold");
        Elements bolds2 = page.select(SPAN_SCRIPTURE_NUMBER_BOLD);
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
        Elements headings = chapter.select("div.ChapterContent_s1__bNNaW, div.ChapterContent_mr__Vxus8");
        for (Element heading : headings) {
            heading.text("@" + heading.text());
        }
    }

    private void removeUnwantedHeaders() {
        Elements headersWithCrosReferences = chapter.select("div.ChapterContent_r___3KRx");
        headersWithCrosReferences.remove();
    }

    private void addAmpersandToBookDivision() {
        //Element bookDivision = chapter.selectFirst("div.ChapterContent_ms1__s_U5R");
        Element bookDivision = chapter.selectFirst("div.ChapterContent_ms*");
        if (bookDivision == null) {
            return;
        }
        bookDivision.text("@" + bookDivision.text());
    }

    private void addDolarSignToSuperscription() {
        if (bookName != BookName.BOOK_19_PSA) {
            return;
        }
        int chapterNumber = getChapterNumber();
        boolean thisChapterHasSupercription = Superscription.thisChapterHas(chapterNumber);
        Element superscriptionElement = chapter.selectFirst("div.ChapterContent_d__OHSpy");
        if (thisChapterHasSupercription) {
            if (superscriptionElement == null || superscriptionElement.text().isEmpty()) {
                superscriptionElement = new Element("div").addClass("ChapterContent_d__OHSpy");
                chapter.prependChild(superscriptionElement);
            }
            superscriptionElement.text("$" + superscriptionElement.text());
        } else {
            if (superscriptionElement != null) {
                superscriptionElement.remove();
            }
        }
    }

    private int getChapterNumber() {
        Element elementChapterNumber = chapter.selectFirst("span.chapterNumber");
        if (elementChapterNumber != null) {
            String stringChapterNumber = elementChapterNumber.wholeText();
            stringChapterNumber = stringChapterNumber.replace("{", "").replace("}", "").trim();
            return Integer.parseInt(stringChapterNumber);
        }
        if(bookName.getNumberOfChapters() == 1) {
            return 1;
        }
        return 0;
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
        Elements scriptureNumbers = chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
        Element scriptureNumberOne = scriptureNumbers.get(0);
        Element chapterNumber = chapter.selectFirst("span.chapterNumber");
        if (chapterNumber == null) {
            return;
        }
        String chapterNumberText = chapterNumber.wholeText();
        chapterNumber.remove();
        Element newChapterNumber =
                new Element("span").addClass("chapterNumber").text(chapterNumberText);
        scriptureNumberOne.before(newChapterNumber);
    }

    private void removeScriptureNumberOne() {
        if (bookName.getNumberOfChapters() > 1) {
            Elements scriptureNumbers = chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
            scriptureNumbers.get(0).text("");
        }
    }

    private void addCurlyBracketsToChapterNumber() {
        Element chapterNumber = chapter.selectFirst("div.ChapterContent_label__R2PLt");
        if (chapterNumber == null) {
            return;
        }
        if(bookName.getNumberOfChapters() == 1) {
            chapterNumber.text("");
            return;
        }
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
        Element newFootnote = new Element("div").addClass("footnoteMeps");
        newFootnote.appendChild(footnoteScriptureNumber);
        Elements children = footnoteBody.children();
        for (Element child : children) {
            newFootnote.appendChild(child);
        }
        chapterFootnotes.add(newFootnote);
    }

    private void handleMultipleScripturesFootnote(Element footnoteBody,
            Element footnoteScriptureNumber) {
        if (footnoteScriptureNumber.text().contains(",")) {
            String[] scriptureNumbers = footnoteScriptureNumber.text().split(",");
            footnoteScriptureNumber.text(scriptureNumbers[0] + " ");
            Element newVideSpan =
                    new Element("span").addClass("ChapterContent_body__O3qjr").text(" [Vide ");
            footnoteBody.appendChild(newVideSpan);
            for (int i = 1; i < scriptureNumbers.length; i++) {
                Element newSpan = new Element("span").addClass("ChapterContent_body__O3qjr");
                boolean lastI = i == scriptureNumbers.length - 1;
                newSpan.text(scriptureNumbers[i] + (lastI ? ".]" : ", "));
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
