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
        
        this.page = chapter;
    }

    private void formatScriptureNumberAsBold(Element chapter) {
        Elements scriptureNumbers = chapter.select("span.ChapterContent_label__R2PLt");
        for(Element scriptureNumber : scriptureNumbers){
            // scriptureNumber.appendElement("strong").text(scriptureNumber.text());
            scriptureNumber.replaceWith(new Element("strong").addClass("scriptureNumberBold").text(" "+scriptureNumber.text()+" "));
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
