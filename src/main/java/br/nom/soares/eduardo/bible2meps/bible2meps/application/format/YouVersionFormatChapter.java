package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class YouVersionFormatChapter {
    
    private Document page;   

    public void execute(){
        Element chapterOnly = extractChapter(page);
        removeUnwantedText(chapterOnly);        
    }

    protected void removeUnwantedText(Element element) {
        Elements crossReferences = element.select("span.ChapterContent_x__tsTlk");
        for(Element crossReference : crossReferences){
            crossReference.remove();
        }       
    }

    protected Element extractChapter(Document page){
        return page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }


}
