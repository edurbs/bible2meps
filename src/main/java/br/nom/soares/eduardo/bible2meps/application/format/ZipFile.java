package br.nom.soares.eduardo.bible2meps.application.format;

import java.util.List;
import br.nom.soares.eduardo.bible2meps.domain.Book;

public interface ZipFile {
    public byte[] create(List<Book> books, String zipFilename);
}
