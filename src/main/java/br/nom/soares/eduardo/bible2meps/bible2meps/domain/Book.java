package br.nom.soares.eduardo.bible2meps.bible2meps.domain;

import br.nom.soares.eduardo.bible2meps.bible2meps.domain.enums.BookName;

public record Book(BookName bookName, String html) {
}
