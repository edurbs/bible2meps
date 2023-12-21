package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import java.util.HashMap;
import java.util.Map;

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

    private Map<String, String> footnotesMap = new HashMap<>();

    public void execute(){
        Element chapter = extractChapter(page);
        removeUnwantedNotes(chapter);   
        extractFootnotes(chapter);

        setPageWithFinalFormatedText(chapter);
    }

    protected void extractFootnotes(Element chapter) {
        Elements footnoteElements = chapter.select("span.ChapterContent_note__YlDW0");        
        for (Element footnote : footnoteElements) {
            Element footnoteValue = footnote.selectFirst("span.ChapterContent_body__O3qjr");
            Element footnoteKeyElement = footnoteValue.selectFirst("span.ChapterContent_fr__0KsID");
            String footnoteKey = footnoteKeyElement.text();
            footnoteKeyElement.remove();
            footnotesMap.put(footnoteKey, footnoteValue.text());        
        }
    }

    private void setPageWithFinalFormatedText(Element chapterOnly) {
        this.page = chapterOnly;
    }

    protected void removeUnwantedNotes(Element element) {
        Elements crossReferences = element.select("span.ChapterContent_x__tsTlk");
        for(Element crossReference : crossReferences){
            crossReference.remove();
        }       
    }

    protected Element extractChapter(Element page){
        return page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }


}
