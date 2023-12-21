package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import org.jsoup.Jsoup;
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
        Element chapter = extractChapter(page);
        removeUnwantedNotes(chapter);   
        setPageWithFinalFormatedText(chapter);
    }

    private void setPageWithFinalFormatedText(Element chapterOnly) {
        this.page = Jsoup.parse("<html><body>"+chapterOnly.html()+"</body></html>");
    }

    protected void removeUnwantedNotes(Element element) {
        Elements crossReferences = element.select("span.ChapterContent_x__tsTlk");
        for(Element crossReference : crossReferences){
            crossReference.remove();
        }       
    }

    protected Element extractChapter(Document page){
        return page.selectFirst("div.ChapterContent_chapter__uvbXo");
    }


}
