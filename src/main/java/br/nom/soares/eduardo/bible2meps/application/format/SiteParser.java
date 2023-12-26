package br.nom.soares.eduardo.bible2meps.application.format;

import java.util.List;
import br.nom.soares.eduardo.bible2meps.domain.Language;
import br.nom.soares.eduardo.bible2meps.domain.Translation;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;

public interface SiteParser {
    public List<String> getUrls(BookName bookName, String id, String abbreviation);

    public List<Language> getLanguages();

    public List<Translation> getBibles(String languageTag);

    public String formatBook(List<String> urls, BookName bookName, Runnable progress);
}
