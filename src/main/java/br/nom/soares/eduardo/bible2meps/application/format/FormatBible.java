package br.nom.soares.eduardo.bible2meps.application.format;

import java.util.ArrayList;
import java.util.List;
import br.nom.soares.eduardo.bible2meps.domain.Book;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FormatBible {
    @NonNull
    private BibleParams params;

    public byte[] execute() {
        // TODO depedence injection to test
        // TODO add thread
        // TODO jsoup add proxy https://www.proxyrotator.com/free-proxy-list/
        // TODO handle jsoup http 503
        // TODO handle jsoup timeout

        SiteParser siteParser = params.siteParser();
        String abbreviation = params.abbreviation();
        List<Book> books = new ArrayList<>();
        for (BookName bookName : BookName.values()) {
            List<String> urls = siteParser.getUrls(bookName, params.bibleId(), abbreviation);
            String bookHtml = siteParser.formatBook(urls, bookName);
            books.add(new Book(bookName, bookHtml));
        }
        return params.zipFile().create(books, abbreviation + ".zip");
    }

}
