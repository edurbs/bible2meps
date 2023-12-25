package br.nom.soares.eduardo.bible2meps.infra.parser;

import java.util.List;
import org.jsoup.nodes.Element;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class FormatBookAbstract {
    @NonNull
    protected List<String> urls;

    @NonNull
    protected BookName bookName;

    protected Element book;

    public String html() {
        return book.html();
    }
}
