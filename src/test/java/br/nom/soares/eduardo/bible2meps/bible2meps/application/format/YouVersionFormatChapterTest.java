package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@TestInstance(Lifecycle.PER_CLASS)
public class YouVersionFormatChapterTest {
    
    private YouVersionFormatChapter youVersionFormatChapter;
    private Element cleanedPage;

    @AfterAll
    void afterAll(){
        System.out.println(cleanedPage);
    }

    @BeforeAll
    void beforeAll(){
        try {
            String url = "https://www.bible.com/bible/2645/GEN.1.A21";
            Document page = Jsoup.connect(url).get();
            youVersionFormatChapter = new YouVersionFormatChapter(page);            
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        youVersionFormatChapter.execute();        
        cleanedPage = youVersionFormatChapter.getPage();        
    }
    
    @Test
    void shouldGetOnlyTheChapter(){
        assertEquals(0, cleanedPage.select(".div.ChapterContent_book__VkdB2").size());
    }

    @Test
    void shouldCleanAllUnwantedFootnotes(){
        assertEquals(0, cleanedPage.select("span.ChapterContent_x__tsTlk").size());        
    }

    @Test
    void shouldRemoveAllWantedText(){
        assertEquals(0, cleanedPage.select("div.ChapterContent_version-copyright__FlNOi").size());
    }

    @Test
    void shouldFormatFootnotes(){
        assertEquals(0, cleanedPage.select("span.ChapterContent_note__YlDW0").size());
        assertEquals(2, youVersionFormatChapter.getFootnotesElementList().size());
        assertEquals(
            "#1:26 Cf. a Versão siríaca.<br>", 
            youVersionFormatChapter.getFootnotesElementList().get(1).html());
    }

    @Test
    void shouldFormatScriptureNumbersAsBold(){
        // Elements scriptureNumbers = cleanedPage.select("span.ChapterContent_label__R2PLt");
        Elements scriptureNumbers = cleanedPage.select("strong.scriptureNumberBold");
        
        assertEquals(31, scriptureNumbers.size());
        assertEquals(
            "<strong class=\"scriptureNumberBold\"> 31 </strong>", 
            scriptureNumbers.get(30).outerHtml()
        );
    }
}
