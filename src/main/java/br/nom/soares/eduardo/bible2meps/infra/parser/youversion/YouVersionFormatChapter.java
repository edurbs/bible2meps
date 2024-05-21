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

    private static final String CHAPTER_NUMBER_NEW = "chapterNumber";
    private static final String SPAN_CHAPTER_NUMBER_NEW = "span.chapterNumber";
    private static final String CHAPTER_CONTENT_BODY = "ChapterContent_body__O3qjr";
    private static final String SCRIPTURE_NUMBER_BOLD = "scriptureNumberBold";
    private static final String STYLE = "style";
    private static final String FONT_WEIGHT_BOLD = "font-weight: bold";
    private static final String PARAGRAPH = "paragraph";
    private static final String SUPERSCRIPTION = "ChapterContent_d__OHSpy";
    private static final String DIVISOR_FOR_POETIC_TEXT = "ChapterContent_b__BLNfi";
    private static final String POETIC_TEXT_2 = "ChapterContent_q2__Z9WWu";
    private static final String POETIC_TEXT_1 = "ChapterContent_q__EZOnh";
    private static final String SPAN_SCRIPTURE_NUMBER_BOLD = "span.scriptureNumberBold";
    private static final String CHAPTER_NUMBER_ORIGINAL = "ChapterContent_label__R2PLt";
    private static final String HEADER = "ChapterContent_s1__bNNaW";
    private static final String HEADER2 = "ChapterContent_s2__l6Ny0";

    @NonNull
    private Element page;

    @NonNull
    private BookName bookName;

    private List<Element> chapterFootnotes = new ArrayList<>();
    private Element chapter;
    private boolean divPreviousWasPoetic = false;

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
        handleDivergentNumberOfScriptures();
        addSoftReturnAtEachLineOfPoeticText();
        addSoftReturnToEndOfLinePrecedingPoeticTextWhenStartsInMiddleOfVerse();
        addPlusSignToHeadings();
        removeUnwantedSpaces();
        addStyles();
        page = Jsoup.parseBodyFragment(chapter.html());
    }

    private void addSoftReturnToEndOfLinePrecedingPoeticTextWhenStartsInMiddleOfVerse() {
        Element newPage = new Element("div").addClass("ChapterContent_chapter__uvbXo");
        Element paragraph = new Element("div").addClass(PARAGRAPH);
        Elements divs = chapter.children();
        for (Element div : divs) {
            if (isNotHeader(div) && (isPoetic(div) || poeticTextStartedInMiddle(div))) {
                paragraph = formatPoeticDiv(newPage, paragraph, div);
            } else {
                newPage.appendChild(div.clone());
            }
            div.remove();
        }
        chapter.remove();
        this.page = newPage;
        extractChapter();
    }


    private Element formatPoeticDiv(Element newPage, Element paragraph, Element div) {
        int scriptureNumber = getFirstScriptureNumberFromDiv(div);
        boolean poeticTextStartedInTheBeginning = poeticTextStartedInTheBeginning(div);
        int childrenSize = paragraph.childrenSize();
        int chapterNumber = getChapterNumberOriginal();
        if (childrenSize == 0 && poeticTextStartedInTheBeginning && (chapterNumber > 1 || scriptureNumber > 0) ){
            addSignBeforeScriptureNumber("=", div);
        }
        chapterNumber = getChapterNumber(paragraph);
        if(chapterNumber == 1 && scriptureNumber == 2 && poeticTextStartedInTheBeginning) {
            addSignBeforeScriptureNumber("=", div);
        }
        if(div.tagName().equals("div")){
            Element span = createPoeticSpan(div);
            paragraph.appendChild(span.clone());
        } else {
            paragraph.appendChild(div.clone());
        }
        Element nextDiv = div.nextElementSibling();
        if (nextDiv == null || !isPoetic(nextDiv)) {
            newPage.appendChild(paragraph.clone());
            paragraph = new Element("div").addClass(PARAGRAPH);
        }
        return paragraph;
    }

    private Element createPoeticSpan(Element div) {
        Element span = new Element("span").addClass(POETIC_TEXT_1);
        div.append("<br>");
        span.html(div.html());
        return span;
    }


    private boolean isNotHeader(Element div) {
        return !div.hasClass(HEADER) && !div.hasClass(SUPERSCRIPTION);
    }

    private int getFirstScriptureNumberFromDiv(Element div) {
        Element scriptureElement = div.selectFirst(SPAN_SCRIPTURE_NUMBER_BOLD);
        if (scriptureElement == null) {
            return 0;
        }
        String text = scriptureElement.text();
        if(text.isBlank()) {
            return 1;
        }
        return Integer.parseInt(text);
    }

    private boolean poeticTextStartedInTheBeginning(Element div) {
        Element usfmElement = div.selectFirst("span[data-usfm]");
        return (usfmElement != null && usfmElement.text().isBlank() && isPoetic(div))
        || (isPoetic(div) && getFirstScriptureNumberFromDiv(div) == 1 && getChapterNumberOriginal() > 1);
    }

    private boolean poeticTextStartedInMiddle(Element div) {
        Element nextDiv = div.nextElementSibling();
        if (nextDiv != null && isPoetic(nextDiv)) {
            Element usfmElement = nextDiv.selectFirst("span[data-usfm]");
            return usfmElement != null && !usfmElement.text().isBlank();
        }
        return false;
    }

    private boolean isPoetic(Element div) {
        return (div.hasClass(POETIC_TEXT_1) || div.hasClass(POETIC_TEXT_2));
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
        int chapterNumber = getChapterNumberOriginal();
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
                Element newScripture = new Element("span");
                Element space = new Element("span").text(" ");
                Element blankScripture = new Element("span")
                        .text(scriptureNumber + i + " ")
                        .addClass(SCRIPTURE_NUMBER_BOLD)
                        .attr(STYLE, FONT_WEIGHT_BOLD);
                Element dash = new Element("span").text(" —— ");
                newScripture.appendChild(space);
                newScripture.appendChild(blankScripture);
                newScripture.appendChild(dash);
                last.parent().after(newScripture);
            }
        }
    }

    private void addStyles() {
        Elements italics1 = page.select("span.ChapterContent_fqa__Xa2yn");
        italics1.attr(STYLE, "font-style: italic");
        Elements italics2 = page.select("span.ChapterContent_tl__at1as");
        italics2.attr(STYLE, "font-style: italic");
        Elements bolds1 = page.select("span.ChapterContent_fk__ZzZlQ");
        bolds1.attr(STYLE, FONT_WEIGHT_BOLD);
        Elements bolds2 = page.select(SPAN_SCRIPTURE_NUMBER_BOLD);
        bolds2.attr(STYLE, FONT_WEIGHT_BOLD);
        Elements smallCaps = page.select("span.ChapterContent_nd__ECPAf");
        smallCaps.attr(STYLE, "font-variant: small-caps");
    }

    private void removeUnwantedSpaces() {
        Elements elements = chapter.getAllElements();
        String utf200a = "\u200A";
        for (Element span : elements) {
            span.html(span.html().replace(utf200a, ""));
            if (!span.hasText()) {
                span.remove();
            }
        }
    }

    private void addAtSignToHeadings() {
        Elements headings = chapter.select("div."+HEADER+", div."+HEADER2+", div.ChapterContent_mr__Vxus8, div.ChapterContent_qa__RzTnv");
        for (Element heading : headings) {
            heading.text("@" + heading.text());
        }
    }

    private void removeUnwantedHeaders() {
        Elements unWantedHeaders = chapter.select("div.ChapterContent_r___3KRx, div.ChapterContent_sr__1YDDW, div."+DIVISOR_FOR_POETIC_TEXT);
        unWantedHeaders.remove();
    }

    private void addAmpersandToBookDivision() {
        Element bookDivision = chapter.selectFirst("div.ChapterContent_ms1__s_U5R, div.ChapterContent_ms__Z16Ky");
        if (bookDivision == null) {
            return;
        }
        bookDivision.text("@" + bookDivision.text());
    }

    private void addDolarSignToSuperscription() {
        if (bookName != BookName.BOOK_19_PSA) {
            return;
        }
        int chapterNumber = getChapterNumberOriginal();
        boolean thisChapterHasSupercription = Superscription.thisChapterHas(chapterNumber);
        Element superscriptionElement = chapter.selectFirst("div."+SUPERSCRIPTION);
        if (thisChapterHasSupercription) {
            if (superscriptionElement == null || superscriptionElement.text().isEmpty()) {
                superscriptionElement = new Element("div").addClass(SUPERSCRIPTION);
                chapter.prependChild(superscriptionElement);
            }
            superscriptionElement.text("$" + superscriptionElement.text());
        } else {
            if (superscriptionElement != null) {
                superscriptionElement.remove();
            }
        }
    }

    private int getChapterNumber(Element chapter) {
        Element elementChapterNumber = chapter.selectFirst(SPAN_CHAPTER_NUMBER_NEW);
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

    private int getChapterNumberOriginal() {
        return getChapterNumber(chapter);
    }

    private void addPlusSignToHeadings() {
        Elements chapterChildren = chapter.children();
        for (Element child : chapterChildren) {
            Element previous = child.previousElementSibling();
            if (shouldAddPlusSign(child, previous)) {
                addSignBeforeScriptureNumber("+",child);
            }
        }
    }

    private boolean shouldAddPlusSign(Element child, Element previous) {
        return (!child.text().matches("[@$&=].*")
                && (previous != null && previous.text().matches("[@$&].*")))
                || (previous != null && previous.hasClass(PARAGRAPH) && !child.text().matches("[@$&=].*"));
    }

    private void addSignBeforeScriptureNumber(String sign, Element div) {
        Elements verseBlocks = div.select("span.ChapterContent_verse__57FIw");
        if (!verseBlocks.isEmpty()) {
            for (Element block : verseBlocks) {
                if (block.hasText()) {
                    block.firstChild().before(sign);
                    break;
                }
            }
        }
    }
    private void moveChapterNumberNextToScriptureNumberOne() {
        Elements scriptureNumbers = chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
        Element scriptureNumberOne = scriptureNumbers.get(0);
        Element chapterNumber = chapter.selectFirst("span."+CHAPTER_NUMBER_NEW);
        if (chapterNumber == null) {
            return;
        }
        String chapterNumberText = chapterNumber.wholeText();
        chapterNumber.remove();
        Element newChapterNumber =
                new Element("span").addClass(CHAPTER_NUMBER_NEW).text(chapterNumberText);
        scriptureNumberOne.before(newChapterNumber);
    }

    private void removeScriptureNumberOne() {
        if (bookName.getNumberOfChapters() > 1) {
            Elements scriptureNumbers = chapter.select(SPAN_SCRIPTURE_NUMBER_BOLD);
            scriptureNumbers.get(0).text("");
        }
    }

    private void addCurlyBracketsToChapterNumber() {
        Element chapterNumber = chapter.selectFirst("div."+CHAPTER_NUMBER_ORIGINAL);
        if(bookName.getNumberOfChapters() == 1) {
            chapterNumber.text("");
            return;
        }
        String formatedChapterNumber = "{" + chapterNumber.text() + "} ";
        Element newChapterNumberElement =
                new Element("span").addClass(CHAPTER_NUMBER_NEW).text(formatedChapterNumber);
        chapterNumber.replaceWith(newChapterNumberElement);
    }

    private void formatScriptureNumberAsBold() {
        Elements scriptureNumbers = chapter.select("span."+CHAPTER_NUMBER_ORIGINAL);
        for (int i = 0; i < scriptureNumbers.size(); i++) {
            Element scriptureNumber = scriptureNumbers.get(i);
            String scriptureNumberText = scriptureNumber.text();
            // TODO handle united verses
            if(scriptureNumberText.contains("-")){
                String firstNumber = scriptureNumberText.split("-")[0];
                String lastNumber = scriptureNumberText.split("-")[1];
                int firstNumberInt = Integer.parseInt(firstNumber);
                int lastNumberInt = Integer.parseInt(lastNumber);
                int diff = lastNumberInt - firstNumberInt;
                Elements unitedScripturesElements = new Elements();
                unitedScripturesElements.addFirst(scriptureNumber);
                addBlankScriptureAfter(unitedScripturesElements, diff);
            }
            String scriptureText;
            String startSpace = " ";
            if (scriptureNumber.parent() != null) {
                scriptureText = scriptureNumber.parent().text();
                if (scriptureText.startsWith(scriptureNumberText)) {
                    startSpace = "";
                }
            }
            Element scriptureNumberBoldWithSpace = new Element("span")
                    .addClass(SCRIPTURE_NUMBER_BOLD).text(startSpace + scriptureNumberText + " ");
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
                    new Element("span").addClass(CHAPTER_CONTENT_BODY).text(" [Vide ");
            footnoteBody.appendChild(newVideSpan);
            for (int i = 1; i < scriptureNumbers.length; i++) {
                Element newSpan = new Element("span").addClass(CHAPTER_CONTENT_BODY);
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
