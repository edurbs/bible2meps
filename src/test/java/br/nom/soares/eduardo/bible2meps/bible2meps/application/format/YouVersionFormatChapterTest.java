package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@TestInstance(Lifecycle.PER_CLASS)
class YouVersionFormatChapterTest {

    private YouVersionFormatChapter youVersionFormatChapter;
    private Element chapter;

    @AfterAll
    void afterAll() {
        System.out.println(chapter);
    }

    void formatChapter(String url) {
        try {
            Document page = Jsoup.connect(url).get();
            youVersionFormatChapter = new YouVersionFormatChapter(page);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        youVersionFormatChapter.execute();
        chapter = youVersionFormatChapter.getPage();
    }

    void shouldFormatScriptureNumberAsBoldGeneric(int totalScriptureNumbers) {
        Elements scriptureNumbers = chapter.select("strong.scriptureNumberBold");
        assertEquals(totalScriptureNumbers, scriptureNumbers.size());
        assertEquals(
                "<strong class=\"scriptureNumberBold\">" + (totalScriptureNumbers)+ " </strong>",
                scriptureNumbers.get(totalScriptureNumbers-1).outerHtml());
    }

    void shouldFormatFootNotesGeneric(String footnoteTextExpected, int position) {
        assertEquals(0, chapter.select("span.ChapterContent_note__YlDW0").size());
        assertEquals(position, youVersionFormatChapter.getFootnotesElementList().size());
        assertEquals(
                footnoteTextExpected,
                youVersionFormatChapter.getFootnotesElementList().get(position-1).html());
    }

    void shouldRemoveScriptureNumberOneGeneric() {
        Elements scriptureNumbers = chapter.select("strong.scriptureNumberBold");
        scriptureNumbers.get(0).wholeText().equals("");
    }

    void shouldMoveChapterNumberNextToScriptureNumberOneGeneric() {
        Element scriptureNumberOne = chapter.select("strong.scriptureNumberBold").get(0);
        String chapterNumber = chapter.selectFirst("span.chapterNumber").wholeText();
        Element previousSibling = scriptureNumberOne.previousElementSibling();
        assertEquals(chapterNumber, previousSibling.wholeText());
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class genesis1test {

        @BeforeAll
        void setup() {
            String url = "https://www.bible.com/bible/2645/GEN.1.A21";
            formatChapter(url);
        }

        @Test
        void shouldAddAtSighBeforeHeadings(){
            Elements headings = chapter.select("span.ChapterContent_heading__xBDcs");
            for (Element heading : headings) {
                assertEquals("@", heading.text().substring(0, 1));
            }
        }

        @Test
        void shouldGetOnlyTheChapter() {
            assertEquals(0, chapter.select(".div.ChapterContent_book__VkdB2").size());
        }

        @Test
        void shouldCleanAllUnwantedFootnotes() {
            assertEquals(0, chapter.select("span.ChapterContent_x__tsTlk").size());
        }

        @Test
        void shouldRemoveAllWantedText() {
            assertEquals(0, chapter.select("div.ChapterContent_version-copyright__FlNOi").size());
        }

        @Test
        void shouldFormatFootnotes() {
            String footnoteTextExpected = "#1:26 Cf. a Versão siríaca.<br>";
            shouldFormatFootNotesGeneric(footnoteTextExpected, 2);
        }

        @Test
        void shouldFormatScriptureNumbersAsBold() {
            shouldFormatScriptureNumberAsBoldGeneric(31);            
        }

        @Test
        void shouldAddCurlyBracketsToChapterNumber() {
            assertEquals("{1} ", chapter.selectFirst("span.chapterNumber").wholeText());
        }

        @Test
        void shouldRemoveScriptureNumberOne(){
            shouldRemoveScriptureNumberOneGeneric();
        }

        @Test
        void shouldMoveChapterNumberNextToScriptureNumberOne(){
            shouldMoveChapterNumberNextToScriptureNumberOneGeneric();
        }

        
    }
    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class genesis2test {

        @BeforeAll
        void setup() {
            String url = "https://www.bible.com/bible/2645/GEN.2.A21";
            formatChapter(url);
        }

        @Test
        void shouldFormatScriptureNumbersAsBold() {
            shouldFormatScriptureNumberAsBoldGeneric(25);
        }

        @Test
        void shouldFormatFootnotes() {
            String footnoteTextExpected = "#2:23 No hebr., há um jogo de palavras: varoa (mulher) e varão (homem).<br>";
            shouldFormatFootNotesGeneric(footnoteTextExpected, 1);
        }

        @Test
        void shouldAddCurlyBracketsToChapterNumber() {
            assertEquals("{2} ", chapter.selectFirst("span.chapterNumber").wholeText());
        }

        @Test
        void shouldMoveChapterNumberNextToScriptureNumberOne(){
            shouldMoveChapterNumberNextToScriptureNumberOneGeneric();
        }

    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class psalmo4test {

        @BeforeAll
        void setup() {
            String url = "https://www.bible.com/bible/2645/PSA.4.A21";
            formatChapter(url);
        }

        @Test
        void shouldAddDolarSignToSuperscription(){
            Element superscription = chapter.selectFirst("div.ChapterContent_d__OHSpy");
            assertEquals("$", superscription.text().substring(0, 1));
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class psalmo2test {

        @BeforeAll
        void setup() {
            String url = "https://www.bible.com/bible/2645/PSA.2.A21";
            formatChapter(url);
        }

        @Test
        void shouldAddDolarSignToSuperscription(){
            Element superscription = chapter.selectFirst("div.ChapterContent_d__OHSpy");
            assertNull(superscription);
        }
    }
}
