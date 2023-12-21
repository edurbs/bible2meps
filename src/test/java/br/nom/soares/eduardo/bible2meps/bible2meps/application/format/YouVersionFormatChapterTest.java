package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class YouVersionFormatChapterTest {
    
    

    @Test
    void ShouldFormatGenesisChapter1(){
        YouVersionFormatChapter youVersionFormatChapter;
        try {
            String url = "https://www.bible.com/bible/2645/GEN.1.A21";
            Document page = Jsoup.connect(url).get();
            youVersionFormatChapter = new YouVersionFormatChapter(page);            
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        youVersionFormatChapter.execute();
        
        Element cleanedPage = youVersionFormatChapter.getPage();
        // only the chapter
        assertEquals(0, cleanedPage.select(".div.ChapterContent_book__VkdB2").size());
        // no footnotes
        assertEquals(0, cleanedPage.select("span.ChapterContent_x__tsTlk").size());        
        // remove all unwanted text
        assertEquals(0, cleanedPage.select("div.ChapterContent_version-copyright__FlNOi").size());
        // check footnotes
        assertEquals(0, cleanedPage.select("span.ChapterContent_note__YlDW0").size());
        assertEquals(2, youVersionFormatChapter.getFootnotesList().size());
        assertEquals("#1:26 Cf. a Versão siríaca.<br>", youVersionFormatChapter.getFootnotesList().get(1));
    }
}
