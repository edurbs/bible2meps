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

    public void execute(){
        Element chapter = extractChapter(page);
        removeUnwantedNotes(chapter);   
        extractFootnotes(chapter);
        formatScriptureNumberAsBold(chapter);
        addCurlyBracketsToChapterNumber(chapter);
        this.page = chapter;
    }

    private void addCurlyBracketsToChapterNumber(Element chapter) {
        Element chapterNumber = chapter.selectFirst("div.ChapterContent_label__R2PLt");
        String formatedChapterNumber = "{"+chapterNumber.text()+"} ";
        Element newChapterNumberElement = new Element("div")
                .addClass("chapterNumber")
                .text(formatedChapterNumber);
        chapterNumber.replaceWith(newChapterNumberElement);
    }

    private void formatScriptureNumberAsBold(Element chapter) {
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

    private void extractFootnotes(Element chapter) {
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

    private void removeUnwantedNotes(Element element) {
        Elements crossReferences = element.select("span.ChapterContent_x__tsTlk");
        for(Element crossReference : crossReferences){
            crossReference.remove();
        }       
    }

    private Element extractChapter(Element page){
        return page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }


}
