package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YouVersionFormatChapterTest {

    private Map<String, YouVersionFormatChapterTestHelper> pages = new HashMap<>();

    @AfterAll
    void tearDown() throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta chatset=\"utf-8\"></meta></head><body>");

        // html.append("<h1>Mateus 11</h1>"+pages.get("MAT.11.NAA").getChapter().outerHtml());
        // html.append("<h1>Isaías 1</h1>"+pages.get("ISA.1.NAA").getChapter().outerHtml());
        // html.append("<h1>Genesis 2</h1>"+pages.get("GEN.2.NAA").getChapter().outerHtml());
        // html.append("<h1>Provérbios 1</h1>" + pages.get("PRO.1.NAA").getChapter().outerHtml());
        // html.append("<h1>Salmos 2</h1>" + pages.get("PSA.2.NAA").getChapter().outerHtml());
        // interate the map "pages" and append each chapter to the HTML string
        for (Map.Entry<String, YouVersionFormatChapterTestHelper> entry : pages.entrySet()) {
            html.append("<h1>" + entry.getKey() + "</h1>");
            html.append(entry.getValue().getChapter().outerHtml());
        }
        html.append("</body></html>");

        Path tempFilePath = Path.of("/tmp/testtempfile.html");
        // Write HTML content to the temporary file
        FileWriter writer = new FileWriter(tempFilePath.toFile());
        writer.write(html.toString());
        writer.close();
    }

    @BeforeAll
    void setup() {
        pages.put("GEN.1.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/GEN.1.A21")
                .chapterNumber("1")
                .totalScriptureNumbers(31)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#1:26 </span><span class=\"ft\">Cf. a </span><span class=\"ChapterContent_fqa__Xa2yn\">Versão siríaca.</span>")
                .footnoteExpectedPosition(1)
                .footnoteExpectedSize(2)
                .bookName(BookName.BOOK_01_GEN)
                .build().get());
        pages.put("GEN.2.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/GEN.2.A21")
                .chapterNumber("2")
                .totalScriptureNumbers(25)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#2:23 </span><span class=\"ft\">No hebr., há um jogo de palavras: <span class=\"ChapterContent_tl__at1as\">varoa</span> (</span><span class=\"ChapterContent_fk__ZzZlQ\">mulher</span><span class=\"ft\">) e <span class=\"ChapterContent_tl__at1as\">varão</span> (</span><span class=\"ChapterContent_fk__ZzZlQ\">homem</span><span class=\"ft\">).</span>")
                .footnoteExpectedPosition(0)
                .footnoteExpectedSize(1)
                .bookName(BookName.BOOK_01_GEN)
                .build().get());
        pages.put("PSA.1.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/PSA.1.A21")
                .chapterNumber("1")
                .totalScriptureNumbers(6)
                .psalmWithSuperscription(false)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#1:6 </span><span class=\"ft\">Lit., </span><span class=\"ChapterContent_fqa__Xa2yn\">conhece.</span>")
                .footnoteExpectedPosition(0)
                .footnoteExpectedSize(1)
                .psalmWithBookDivision(true)
                .bookName(BookName.BOOK_19_PSA).build().get());
        pages.put("PSA.2.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/PSA.2.A21")
                .chapterNumber("2")
                .totalScriptureNumbers(12)
                .psalmWithSuperscription(false)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#2:12 </span><span class=\"ft\">I.e., </span><span class=\"ChapterContent_fqa__Xa2yn\">dai honra ao. </span><span class=\"ft\">Algumas versões trazem </span><span class=\"ChapterContent_fqa__Xa2yn\">Beijai os pés do.</span>")
                .footnoteExpectedPosition(2)
                .footnoteExpectedSize(3)
                .bookName(BookName.BOOK_19_PSA)
                .build().get());
        pages.put("PSA.4.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/PSA.4.A21")
                .chapterNumber("4")
                .psalmWithSuperscription(true)
                .totalScriptureNumbers(8)
                .psalmWithSuperscription(true)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#4:5 </span><span class=\"ft\">I.e., </span><span class=\"ChapterContent_fqa__Xa2yn\">sacrifícios exigidos.</span>")
                .footnoteExpectedPosition(0)
                .footnoteExpectedSize(1)
                .bookName(BookName.BOOK_19_PSA)
                .build().get());
        pages.put("PSA.42.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/PSA.42.A21")
                .chapterNumber("42")
                .psalmWithSuperscription(true)
                .totalScriptureNumbers(11)
                .footnoteExpectedSize(0)
                .psalmWithBookDivision(true)
                .bookName(BookName.BOOK_19_PSA)
                .build().get());
        pages.put("OBA.1.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/OBA.1.A21")
                .chapterNumber("1")
                .totalScriptureNumbers(21)
                .footnoteExpectedSize(1)
                .footnoteExpectedPosition(0)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#1:15 </span><span class=\"ft\">Lit., </span><span class=\"ChapterContent_fqa__Xa2yn\">sobre a tua cabeça.</span>")
                .bookName(BookName.BOOK_31_OBA).build().get());
        pages.put("JOL.1.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/JOL.1.A21")
                .chapterNumber("1")
                .totalScriptureNumbers(20)
                .footnoteExpectedSize(3)
                .footnoteExpectedPosition(0)
                .footnoteExpectedText(
                        "<span class=\"ChapterContent_fr__0KsID\">#1:2 </span><span class=\"ft\">Ou </span><span class=\"ChapterContent_fqa__Xa2yn\">líderes.</span>")
                .bookName(BookName.BOOK_29_JOE).build().get());
        pages.put("PSA.98.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/PSA.98.A21")
                .chapterNumber("98")
                .totalScriptureNumbers(9)
                .footnoteExpectedSize(0)
                .psalmWithSuperscription(true)
                .bookName(BookName.BOOK_19_PSA).build().get());
        pages.put("3JN.1.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/3JN.1.A21")
                .chapterNumber("1")
                .totalScriptureNumbers(14)
                .footnoteExpectedSize(0)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_64_3JO).build().get());
        pages.put("2CO.13.A21", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/2645/2CO.13.A21")
                .chapterNumber("13")
                .totalScriptureNumbers(14)
                .footnoteExpectedSize(0)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_47_2CO).build().get());
        pages.put("GEN.1.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/GEN.1.NAA")
                .chapterNumber("1")
                .totalScriptureNumbers(31)
                .footnoteExpectedSize(0)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_01_GEN).build().get());
        pages.put("GEN.2.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/GEN.2.NAA")
                .chapterNumber("2")
                .totalScriptureNumbers(25)
                .footnoteExpectedSize(2)
                .footnoteExpectedPosition(0)
                .footnoteExpectedText("<span class=\"ChapterContent_fr__0KsID\">#2:7 </span><span class=\"ft\">Em hebraico a palavra “terra” (<span class=\"ChapterContent_tl__at1as\">adama</span>) soa parecido com “homem” (<span class=\"ChapterContent_tl__at1as\">adam</span>)</span>")
                .psalmWithSuperscription(false)
                .poeticTextSize(5)
                .bookName(BookName.BOOK_01_GEN).build().get());
        pages.put("MAT.11.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/MAT.11.NAA")
                .chapterNumber("11")
                .totalScriptureNumbers(30)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_40_MAT).build().get());
        pages.put("PRO.1.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/PRO.1.NAA")
                .chapterNumber("1")
                .totalScriptureNumbers(33)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_20_PRO).build().get());
        pages.put("ISA.1.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/ISA.1.NAA")
                .chapterNumber("1")
                .totalScriptureNumbers(31)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_23_ISA).build().get());
        pages.put("PSA.2.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/PSA.2.NAA")
                .chapterNumber("2")
                .totalScriptureNumbers(12)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_19_PSA).build().get());
        pages.put("MAT.5.NAA", YouVersionFormatChapterTestHelper.builder()
                .url("https://www.bible.com/bible/1840/MAT.5.NAA")
                .chapterNumber("5")
                .totalScriptureNumbers(48)
                .psalmWithSuperscription(false)
                .bookName(BookName.BOOK_40_MAT).build().get());
    }

    Stream<Arguments> provideTestData() {
        return pages.entrySet().stream().map(entry -> Arguments.of(entry.getValue()));
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddSoftReturnAtEachLineOfPoeticText(YouVersionFormatChapterTestHelper page) {
        Elements wrongPoeticlines = page.getChapter().select("div.ChapterContent_q__EZOnh");
        assertEquals(0, wrongPoeticlines.size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    @DisplayName("When poetic text starts in the middle of a verse, add a soft return (Shift+Enter) to the end of the line preceding the poetic text.")
    void shouldAddSoftReturnToEndOfLinePrecedingPoeticTextWhenPoeticTextStartsInMiddleOfVerse(
            YouVersionFormatChapterTestHelper page) {
        Element chapter = page.getChapter();
        Elements poeticsTexts =
                chapter.select("span.ChapterContent_q__EZOnh, span.ChapterContent_q2__Z9WWu");
        if(poeticsTexts.isEmpty()) {
            return;
        }
        Element firstPoeticText = poeticsTexts.first();
        Element spanUsfm = firstPoeticText.selectFirst("span[data-usfm]");
        String usfmValueOfFirstPoeticLine = spanUsfm.attr("data-usfm");
        Elements previousElementSiblings = firstPoeticText.previousElementSiblings();
        for (Element previousElementSibling : previousElementSiblings) {
            Elements previousUsfms = previousElementSibling.select("span[data-usfm]");
            if (previousUsfms.isEmpty()) {
                continue;
            }
            String previousUsfmValue = previousUsfms.last().attr("data-usfm");
            if (!previousElementSibling.text().isBlank()
                    && previousUsfmValue.equals(usfmValueOfFirstPoeticLine)) {
                Element parent = firstPoeticText.parent();
                assertEquals("p", parent.tagName());
                break;
            }
        }
    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldHaveTheRightNumberOfScriptures(YouVersionFormatChapterTestHelper page) {
        assertEquals(page.getTotalScriptureNumbers(), page.getChapter().select("span.scriptureNumberBold").size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveChapterOneIfItIsTheOnlyOne(YouVersionFormatChapterTestHelper page) {
        int totalChapters = page.getBookName().getNumberOfChapters();
        if (totalChapters == 1) {
            String chapterNumberExpected=page.getChapter().selectFirst("div.ChapterContent_label__R2PLt").text();
            assertFalse(chapterNumberExpected.contains("{1}"));
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddScriptureOneForBooksWithJustOneChapter(YouVersionFormatChapterTestHelper page) {
        int totalChapters = page.getBookName().getNumberOfChapters();
        if (totalChapters == 1) {
            assertEquals(
                    "<span class=\"scriptureNumberBold\" style=\"font-weight: bold\">1 </span>",
                    page.getChapter().select("span.scriptureNumberBold").get(0).outerHtml());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldFormatScriptureNumberAsBoldGeneric(YouVersionFormatChapterTestHelper page) {
        int totalScriptureNumbers = page.getTotalScriptureNumbers();
        Element chapter = page.getChapter();
        Elements scriptureNumbers = chapter.select("span.scriptureNumberBold");
        assertEquals(
                "<span class=\"scriptureNumberBold\" style=\"font-weight: bold\">"
                        + (totalScriptureNumbers) + " </span>",
                scriptureNumbers.get(totalScriptureNumbers - 1).outerHtml());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldgetAllScriptures(YouVersionFormatChapterTestHelper page) {
        int totalScriptureNumbers = page.getTotalScriptureNumbers();
        Element chapter = page.getChapter();
        Elements scriptureNumbers = chapter.select("span.scriptureNumberBold");
        assertEquals(totalScriptureNumbers, scriptureNumbers.size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldFormatFootNotes(YouVersionFormatChapterTestHelper page) {
        if(page.getFootnoteExpectedText().isEmpty()) {
            return;
        }
        assertEquals(0, page.getChapter().select("span.ChapterContent_note__YlDW0").size());
        List<Element> footnotesElementList = page.getFootnotesElementList();
        assertEquals(page.getFootnoteExpectedSize(), footnotesElementList.size());
        if (page.getFootnoteExpectedSize() > 0) {
            assertEquals(page.getFootnoteExpectedText(),
                    footnotesElementList.get(page.getFootnoteExpectedPosition()).html());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveCommaFromFootnotesReferences(YouVersionFormatChapterTestHelper page) {
        List<Element> footnotes = page.getFootnotesElementList();
        for (Element footnote : footnotes) {
            Element footnoteScriptureNumber = footnote.selectFirst("span.ChapterContent_fr__0KsID");
            assertFalse(footnoteScriptureNumber.text().contains(","));
        }

    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveScriptureNumberOne(YouVersionFormatChapterTestHelper page) {
        Elements scriptureNumbers = page.getChapter().select("span.scriptureNumberBold");
        if(page.getBookName().getNumberOfChapters() > 1) {
            assertEquals("", scriptureNumbers.get(0).wholeText());
        } else {
            assertEquals("1 ", scriptureNumbers.get(0).wholeText());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldMoveChapterNumberNextToScriptureNumberOne(YouVersionFormatChapterTestHelper page) {
        if (page.getBookName().getNumberOfChapters() == 1) {
            assertTrue(true);
            return;
        }
        Element chapter = page.getChapter();
        Element scriptureNumberOne = chapter.select("span.scriptureNumberBold").get(0);
        String chapterNumber = chapter.selectFirst("span.chapterNumber").wholeText();
        Element previousSibling = scriptureNumberOne.previousElementSibling();
        assertEquals(chapterNumber, previousSibling.wholeText());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddAtSignBeforeHeadings(YouVersionFormatChapterTestHelper page) {
        Elements headings = page.getChapter().select("div.ChapterContent_s1__bNNaW, div.ChapterContent_s2__l6Ny0, div.ChapterContent_mr__Vxus8, div.ChapterContent_qa__RzTnv, div.ChapterContent_sr__1YDDW");
        for (Element heading : headings) {
            assertEquals("@", heading.text().substring(0, 1));
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldGetOnlyTheChapter(YouVersionFormatChapterTestHelper page) {
        assertEquals(0, page.getChapter().select(".div.ChapterContent_book__VkdB2").size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldCleanAllUnwantedFootnotes(YouVersionFormatChapterTestHelper page) {
        assertEquals(0, page.getChapter().select("span.ChapterContent_x__tsTlk").size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveAllWantedText(YouVersionFormatChapterTestHelper page) {
        assertEquals(0,
                page.getChapter().select("div.ChapterContent_version-copyright__FlNOi").size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddCurlyBracketsToChapterNumber(YouVersionFormatChapterTestHelper page) {
        if (page.getBookName().getNumberOfChapters() == 1) {
            assertTrue(true);
            return;
        }
        String chapterNumberExpected = String.format("{%s} ", page.getChapterNumber());
        assertEquals(chapterNumberExpected,
                page.getChapter().selectFirst("span.chapterNumber").wholeText());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddPlusSignAtNextLineThatFollowsHeading(YouVersionFormatChapterTestHelper page) {
        Element chapter = page.getChapter();
        Elements headings = chapter.select(
                "div.ChapterContent_s1__bNNaW, div.ChapterContent_d__OHSpy, ChapterContent_ms1__s_U5R, ChapterContent_mr__Vxus8");
        for (Element heading : headings) {
            Elements nextSiblingElements = heading.nextElementSiblings();
            for (Element sibling : nextSiblingElements) {
                String siblingText = sibling.text();
                if ((sibling.classNames().contains("ChapterContent_m__3AINJ")
                        || sibling.classNames().contains("ChapterContent_p__dVKHb"))
                        && !siblingText.matches("[@$&].*")
                        && sibling.previousElementSibling().text().matches("[@$&].*")) {
                    assertEquals("+", siblingText.substring(0, 1));
                    assertNotEquals("+ ", siblingText.substring(0, 2));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddDolarSignToSuperscription(YouVersionFormatChapterTestHelper page) {
        Element superscription = page.getChapter().selectFirst("div.ChapterContent_d__OHSpy");
        if (page.isPsalmWithSuperscription()) {
            String superscriptionText = superscription.text();
            assertEquals("$", superscriptionText.substring(0, 1));
            if(superscriptionText.length() > 1) {
                assertNotEquals("$$", superscriptionText.substring(0, 2));
            }
        } else {
            assertNull(superscription);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddAmpersandToBookDivision(YouVersionFormatChapterTestHelper page) {
        // actually it's an At Sign
        Element bookDivision = page.getChapter().selectFirst("div.ChapterContent_ms1__s_U5R");
        if (page.isPsalmWithBookDivision()) {
            assertEquals("@", bookDivision.text().substring(0, 1));
            assertNotEquals("@@", bookDivision.text().substring(0, 2));
        } else {
            assertNull(bookDivision);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveUnwantedHeaders(YouVersionFormatChapterTestHelper page) {
        var chapter = page.getChapter();
        Elements unWantedHeaders = chapter.select("div.ChapterContent_r___3KRx, div.ChapterContent_b__BLNfi");
        assertEquals(0, unWantedHeaders.size());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveUnwantedSpaces(YouVersionFormatChapterTestHelper page) {
        var chapter = page.getChapter();
        Elements elements = chapter.getAllElements();
        for (Element span : elements) {
            assertFalse(span.wholeText().contains("\u200A"));
        }
    }

    @Test
    void testGetConstructorFields() {
        Element mockPage = new Element("b");
        YouVersionFormatChapter sut = new YouVersionFormatChapter(mockPage, BookName.BOOK_01_GEN);
        Element expectedPage = new Element("b");

        Element actualPage = sut.getPage();

        assertEquals(expectedPage.html(), actualPage.html());
        assertEquals(BookName.BOOK_01_GEN, sut.getBookName());
    }
}
