package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YouVersionFormatChapterTest {
    
    
    private YouVersionFormatChapter youVersionFormatChapter;
    private Document page;
    private Element body;

    @BeforeAll
    void setup() {
        try {
            String url = "https://www.bible.com/bible/2645/GEN.1.A21";
            this.page = Jsoup.connect(url).get();
            this.body = page.body();            
            this.youVersionFormatChapter = new YouVersionFormatChapter(page);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @Test
    void shoudlRemoveAllUnwantedText(){
        youVersionFormatChapter.execute();
        Element finalCleanText = youVersionFormatChapter.getPage();
        assertEquals(0, finalCleanText.select("div.ChapterContent_version-copyright__FlNOi").size());
    }

    @Test
    void shouldGetOnlyTheChapter(){
        Element chapter = youVersionFormatChapter.extractChapter(page);
        assertEquals(0, chapter.select(".div.ChapterContent_book__VkdB2").size());
    }

    @Test
    void shoudlRemoveUnwantedNotes(){                
        youVersionFormatChapter.removeUnwantedNotes(body);        
        assertEquals(0, body.select("span.ChapterContent_x__tsTlk").size());        
    }
}
