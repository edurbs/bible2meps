package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class YouVersionFormatChapter {
    
    @NonNull
    private Element page;

    private List<Element> footnotesElementList = new ArrayList<>();
    private Element chapter;

    public void execute(){
        extractChapter();
        removeUnwantedNotes();   
        extractFootnotes();
        formatScriptureNumberAsBold();
        addCurlyBracketsToChapterNumber();
        removeScriptureNumberOne();
        moveChapterNumberNextToScriptureNumberOne();
        addAtSignToHeadings();
        addDolarSignToSuperscription();
        addAmpersandToBookDivision();
        removeUnwantedHeaders();
        page = chapter;
    }

    private void removeUnwantedHeaders() {
        Elements headersWithPsalmDivision = chapter.select("div.ChapterContent_mr__Vxus8");
        headersWithPsalmDivision.remove();

        Elements headersWithCrosReferences = chapter.select("div.ChapterContent_r___3KRx");
        headersWithCrosReferences.remove();
    }

    private void addAmpersandToBookDivision() {
        Element bookDivision = chapter.selectFirst("div.ChapterContent_ms1__s_U5R");
        bookDivision.text("&"+bookDivision.text());
    }

    private void addDolarSignToSuperscription() {
        // TODO: do this logic only on book of Psalms
        String stringChapterNumber = chapter.selectFirst("span.chapterNumber").wholeText();
        stringChapterNumber = stringChapterNumber
                .replace("{","")
                .replace("}","")
                .trim();
        int chapterNumber = Integer.parseInt(stringChapterNumber);  
        boolean thisChapterHasSupercription = switch (chapterNumber) {
            case 1, 2, 10, 33, 43, 71, 91, 93, 94, 95, 96, 97, 99, 104, 105, 106, 107, 111, 112, 113, 114, 115, 116,
                    117, 118, 119, 135, 136, 137, 146, 147, 148, 149, 150 -> false;
            default -> true;
        };
        Element superscription = chapter.selectFirst("div.ChapterContent_d__OHSpy");
        if(thisChapterHasSupercription){
            if(superscription == null){
                superscription = new Element("div").addClass("ChapterContent_d__OHSpy");
            }
            superscription.text("$"+superscription.text());
        }else{
            if(superscription != null){
                superscription.remove();
            }
        }
    }

    private void addAtSignToHeadings() {
        Elements headings = chapter.select("span.ChapterContent_heading__xBDcs");
        for (Element heading : headings) {
            heading.text("@"+heading.text());
        }
    }

    private void moveChapterNumberNextToScriptureNumberOne() {
        Elements scriptureNumbers = chapter.select("strong.scriptureNumberBold");
        Element scriptureNumberOne = scriptureNumbers.get(0);
        Element chapterNumber = chapter.selectFirst("span.chapterNumber");
        String chapterNumberText = chapterNumber.wholeText();
        chapterNumber.remove();        
        Element newChapterNumber = new Element("span").addClass("chapterNumber").text(chapterNumberText);
        scriptureNumberOne.before(newChapterNumber);
    }

    private void removeScriptureNumberOne() {
        Elements scriptureNumbers = chapter.select("strong.scriptureNumberBold");
        scriptureNumbers.get(0).text("");
    }

    private void addCurlyBracketsToChapterNumber() {
        Element chapterNumber = chapter.selectFirst("div.ChapterContent_label__R2PLt");
        String formatedChapterNumber = "{"+chapterNumber.text()+"} ";
        Element newChapterNumberElement = new Element("span")
                .addClass("chapterNumber")
                .text(formatedChapterNumber);
        chapterNumber.replaceWith(newChapterNumberElement);
    }

    private void formatScriptureNumberAsBold() {
        Elements scriptureNumbers = chapter.select("span.ChapterContent_label__R2PLt");
        for (int i = 0; i < scriptureNumbers.size(); i++) {
            Element scriptureNumber = scriptureNumbers.get(i);
            String scriptureNumberText = scriptureNumber.text();
            String scriptureText = scriptureNumber.parent().text();
            String startSpace = " ";
            if(scriptureText.startsWith(scriptureNumberText)){
                startSpace = "";
            }
            Element scriptureNumberBoldWithSpace = new Element("strong")
                    .addClass("scriptureNumberBold")
                    .text(startSpace+scriptureNumberText+" ");
            scriptureNumber.replaceWith(scriptureNumberBoldWithSpace);
        }
    }

    private void extractFootnotes() {
        Elements footnoteElements = chapter.select("span.ChapterContent_note__YlDW0");        
        for (Element footnote : footnoteElements) {
            Element footnoteBody = footnote.selectFirst("span.ChapterContent_body__O3qjr");
            Element footnoteScriptureNumber = footnoteBody.selectFirst("span.ChapterContent_fr__0KsID");            
            String footnoteScriptureNumberText = footnoteScriptureNumber.text();
            footnoteScriptureNumber.remove();
            addFootnote(footnoteScriptureNumberText, footnoteBody.text());        
        }
        footnoteElements.replaceAll(element -> new Element("span").text("*"));
    }

    private void addFootnote(String footnoteKey, String footnoteValue) {
        String formatedFootnoteText = "#"+footnoteKey.replace(".", ":")+" "+footnoteValue;
        Element footnoteElement = new Element("span").text(formatedFootnoteText);
        footnoteElement.appendElement("br");
        footnotesElementList.add(footnoteElement);
    }

    private void removeUnwantedNotes() {
        Elements crossReferences = chapter.select("span.ChapterContent_x__tsTlk");
        for(Element crossReference : crossReferences){
            crossReference.remove();
        }       
    }

    private void extractChapter(){
        chapter = page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }


}
