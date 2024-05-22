package br.nom.soares.eduardo.bible2meps.application.format;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import br.nom.soares.eduardo.bible2meps.domain.Book;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FormatBible {
    @NonNull
    private BibleParams params;

    @Getter
    private int progress;
    private int totalBibleChapters;
    private Logger logger = Logger.getLogger(getClass().getName());

    public record BibleParams(String bibleId, String abbreviation, SiteParser siteParser,
            ZipFile zipFile) {
    }

    public byte[] execute() {
        totalBibleChapters = getTotalBibleChapters();

        SiteParser siteParser = params.siteParser();
        String abbreviation = params.abbreviation();
        List<Book> books = new ArrayList<>();
        for (BookName bookName : BookName.values()) {
            if(bookName.getOrdinalValue() < 40) continue; // TODO fix NT translation
            List<String> urls = siteParser.getUrls(bookName, params.bibleId(), abbreviation);
            String bookHtml = siteParser.formatBook(urls, bookName, this::updateProgress);
            books.add(new Book(bookName, bookHtml));
        }
        return params.zipFile().create(books, abbreviation + ".zip");
    }

    private int getTotalBibleChapters() {
        int totalChapters = 0;
        for (BookName bookName : BookName.values()) {
            totalChapters += bookName.getNumberOfChapters();
        }
        return totalChapters;
    }

    protected void updateProgress() {
        progress++;
        float percentDone = ((float) progress / (float) totalBibleChapters) * 100;
        String message = "Progress: " + percentDone + "%";
        logger.info(message);
    }

}
