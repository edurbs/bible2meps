package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YouVersionFormatChapterTest {

    private Map<String, YouVersionFormatChapterTestHelper> pages = new HashMap<>();

    @AfterAll
    void tearDown() {
        String html = pages.get("PSA.1.A21").getChapter().outerHtml();
        System.out.println(html);
    }

    @BeforeAll
    void setup() {
        pages.put("GEN.1.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/GEN.1.A21")
                        .chapterNumber("1")
                        .totalScriptureNumbers(31)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#1:26 </span><span class=\"ft\">Cf. a </span><span class=\"ChapterContent_fqa__Xa2yn\">Versão siríaca.</span><br>")
                        .footnoteExpectedPosition(1)
                        .footnoteExpectedSize(2)
                        .bookName(BookName._01_GEN)
                        .build().get());
        pages.put("GEN.2.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/GEN.2.A21")
                        .chapterNumber("2")
                        .totalScriptureNumbers(25)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#2:23 </span><span class=\"ft\">No hebr., há um jogo de palavras: <span class=\"ChapterContent_tl__at1as\">varoa</span> (</span><span class=\"ChapterContent_fk__ZzZlQ\">mulher</span><span class=\"ft\">) e <span class=\"ChapterContent_tl__at1as\">varão</span> (</span><span class=\"ChapterContent_fk__ZzZlQ\">homem</span><span class=\"ft\">).</span><br>")
                        .footnoteExpectedPosition(0)
                        .footnoteExpectedSize(1)
                        .bookName(BookName._01_GEN)
                        .build().get());
        pages.put("PSA.1.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/PSA.1.A21")
                        .chapterNumber("1")
                        .totalScriptureNumbers(6)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#1:6 </span><span class=\"ft\">Lit., </span><span class=\"ChapterContent_fqa__Xa2yn\">conhece.</span><br>")
                        .footnoteExpectedPosition(0)
                        .footnoteExpectedSize(1)
                        .psalmWithBookDivision(true)
                        .bookName(BookName._19_PSA)
                        .build().get());
        pages.put("PSA.2.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/PSA.2.A21")
                        .chapterNumber("2")
                        .totalScriptureNumbers(12)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#2:12 </span><span class=\"ft\">I.e., </span><span class=\"ChapterContent_fqa__Xa2yn\">dai honra ao. </span><span class=\"ft\">Algumas versões trazem </span><span class=\"ChapterContent_fqa__Xa2yn\">Beijai os pés do.</span><br>")
                        .footnoteExpectedPosition(2)
                        .footnoteExpectedSize(3)
                        .bookName(BookName._19_PSA)
                        .build().get());
        pages.put("PSA.4.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/PSA.4.A21")
                        .chapterNumber("4")
                        .psalmWithSuperscription(true)
                        .totalScriptureNumbers(8)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#4:5 </span><span class=\"ft\">I.e., </span><span class=\"ChapterContent_fqa__Xa2yn\">sacrifícios exigidos.</span><br>")
                        .footnoteExpectedPosition(0)
                        .footnoteExpectedSize(1)
                        .bookName(BookName._19_PSA)
                        .build().get());
        pages.put("PSA.42.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/PSA.42.A21")
                        .chapterNumber("42")
                        .psalmWithSuperscription(true)
                        .totalScriptureNumbers(11)
                        .footnoteExpectedSize(0)
                        .psalmWithBookDivision(true)
                        .bookName(BookName._19_PSA)
                        .build().get());
        pages.put("OBA.1.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/OBA.1.A21")
                        .chapterNumber("1")
                        .totalScriptureNumbers(21)
                        .footnoteExpectedSize(1)
                        .footnoteExpectedPosition(0)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#1:15 </span><span class=\"ft\">Lit., </span><span class=\"ChapterContent_fqa__Xa2yn\">sobre a tua cabeça.</span><br>")
                        .bookName(BookName._31_OBA)
                        .build().get());
        pages.put("JOL.1.A21",
                YouVersionFormatChapterTestHelper.builder()
                        .url("https://www.bible.com/bible/2645/JOL.1.A21")
                        .chapterNumber("1")
                        .totalScriptureNumbers(20)
                        .footnoteExpectedSize(3)
                        .footnoteExpectedPosition(0)
                        .footnoteExpectedText(
                                "<span class=\"ChapterContent_fr__0KsID\">#1:2 </span><span class=\"ft\">Ou </span><span class=\"ChapterContent_fqa__Xa2yn\">líderes.</span><br>")
                        .bookName(BookName._29_JOE)
                        .build().get());
    }

    Stream<Arguments> provideTestData() {
        return pages.entrySet().stream()
                .map(entry -> Arguments.of(entry.getValue()));
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
                "<span class=\"scriptureNumberBold\" style=\"font-weight: bold\">" + (totalScriptureNumbers)
                        + " </span>",
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
        assertEquals(0, page.getChapter().select("span.ChapterContent_note__YlDW0").size());
        List<Element> footnotesElementList = page.getFootnotesElementList();
        assertEquals(page.getFootnoteExpectedSize(), footnotesElementList.size());
        if (page.getFootnoteExpectedSize() > 0) {
            assertEquals(
                    page.getFootnoteExpectedText(),
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
        scriptureNumbers.get(0).wholeText().equals("");
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldMoveChapterNumberNextToScriptureNumberOne(YouVersionFormatChapterTestHelper page) {
        Element chapter = page.getChapter();
        Element scriptureNumberOne = chapter.select("span.scriptureNumberBold").get(0);
        String chapterNumber = chapter.selectFirst("span.chapterNumber").wholeText();
        Element previousSibling = scriptureNumberOne.previousElementSibling();
        assertEquals(chapterNumber, previousSibling.wholeText());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddAtSighBeforeHeadings(YouVersionFormatChapterTestHelper page) {
        Elements headings = page.getChapter().select("div.ChapterContent_s1__bNNaW");
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
        String chapterNumberExpected = String.format("{%s} ", page.getChapterNumber());
        assertEquals(chapterNumberExpected, page.getChapter().selectFirst("span.chapterNumber").wholeText());
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddPlusSignAtNextLineThatFollowsHeading(YouVersionFormatChapterTestHelper page) {
        Element chapter = page.getChapter();
        Elements headings = chapter
                .select("div.ChapterContent_s1__bNNaW, div.ChapterContent_d__OHSpy, ChapterContent_ms1__s_U5R");
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
                } else {
                    assertNotEquals("+", siblingText.substring(0, 1));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddDolarSignToSuperscription(YouVersionFormatChapterTestHelper page) {
        Element superscription = page.getChapter().selectFirst("div.ChapterContent_d__OHSpy");
        if (page.isPsalmWithSuperscription()) {
            assertEquals("$", superscription.text().substring(0, 1));
            assertNotEquals("$@", superscription.text().substring(0, 2));
        } else {
            assertNull(superscription);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldAddAmpersandToBookDivision(YouVersionFormatChapterTestHelper page) {
        Element bookDivision = page.getChapter().selectFirst("div.ChapterContent_ms1__s_U5R");
        if (page.isPsalmWithBookDivision()) {
            assertEquals("&", bookDivision.text().substring(0, 1));
            assertNotEquals("&@", bookDivision.text().substring(0, 2));
        } else {
            assertNull(bookDivision);
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void shouldRemoveUnwantedHeaders(YouVersionFormatChapterTestHelper page) {
        var chapter = page.getChapter();
        Elements headersWithPsalmDivision = chapter.select("div.ChapterContent_mr__Vxus8");
        assertEquals(0, headersWithPsalmDivision.size());
        Elements headersWithCrosReferences = chapter.select("div.ChapterContent_r___3KRx");
        assertEquals(0, headersWithCrosReferences.size());
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
}