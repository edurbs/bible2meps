package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class YouVersionFormatChapter {
    
    @NonNull
    private Element page;

    private List<String> footnotesList = new ArrayList<>();

    public void execute(){
        Element chapter = extractChapter(page);
        removeUnwantedNotes(chapter);   
        extractFootnotes(chapter);
        
        this.page = chapter;
    }

    private void extractFootnotes(Element chapter) {
        Elements footnoteElements = chapter.select("span.ChapterContent_note__YlDW0");        
        for (Element footnote : footnoteElements) {
            Element footnoteValue = footnote.selectFirst("span.ChapterContent_body__O3qjr");
            Element footnoteKeyElement = footnoteValue.selectFirst("span.ChapterContent_fr__0KsID");
            String footnoteKey = footnoteKeyElement.text();
            footnoteKeyElement.remove();
            addFootnote(footnoteValue.text(), footnoteKey);        
        }
        footnoteElements.replaceAll(element -> new Element(Tag.valueOf("span"), "").text("*"));        
    }

    private void addFootnote(String footnoteValue, String footnoteKey) {
        String formatedFootnoteKey = "#"+footnoteKey.replace(".", ":")+" ";
        String formatedFootnoteValue = footnoteValue + "<br>";
        footnotesList.add(formatedFootnoteKey + formatedFootnoteValue);
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
