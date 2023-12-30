package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.io.IOException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class YouVersionFormatChapterTestHelper {
    private String url;
    private Element chapter;

    @Builder.Default
    private String chapterNumber = "";

    private YouVersionFormatChapter youVersionFormatChapter;
    private int totalScriptureNumbers;
    private List<Element> footnotesElementList;

    @Builder.Default
    private String footnoteExpectedText = "";

    private int footnoteExpectedPosition;
    private int footnoteExpectedSize;
    private BookName bookName;
    private int poeticTextSize;

    @Builder.Default
    private boolean psalmWithSuperscription = false;

    @Builder.Default
    private boolean psalmWithBookDivision = false;

    public YouVersionFormatChapterTestHelper get() {
        try {
            Document document = Jsoup.connect(url).get();
            YouVersionFormatChapter youVersionFormatChapter =
                    new YouVersionFormatChapter(document, bookName);
            youVersionFormatChapter.execute();
            this.youVersionFormatChapter = youVersionFormatChapter;
            this.chapter = youVersionFormatChapter.getChapter();
            this.footnotesElementList = youVersionFormatChapter.getChapterFootnotes();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
