package br.nom.soares.eduardo.bible2meps.bible2meps.application.format;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion.YouVersionFormatBook;
import br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion.YouVersionSite;
import br.nom.soares.eduardo.bible2meps.bible2meps.domain.Book;
import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FormatBible {
    @NonNull
    private String bibleId;

    @NonNull
    private String abbreviation;
    List<Book> books = new ArrayList<>();

    public void execute() {
        // TODO depedence injection to test
        // TODO add thread
        // TODO jsoup add proxy https://www.proxyrotator.com/free-proxy-list/
        // TODO handle jsoup http 503
        // TODO handle jsoup timeout
        YouVersionSite youVersionSite = new YouVersionSite();
        for (BookName bookName : BookName.values()) {
            List<String> urls = youVersionSite.getUrls(bookName, bibleId, abbreviation);
            var formatBook = new YouVersionFormatBook(urls, bookName);
            formatBook.execute();
            Element book = formatBook.getBook();
            books.add(new Book(bookName, book.html()));
        }
    }

}
