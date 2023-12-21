package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class YouVersionFormatChapter {
    
    private Element page;   

    public void execute(){
        Element chapter = extractChapter(page);
        removeUnwantedNotes(chapter);   
        setPageWithFinalFormatedText(chapter);
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
