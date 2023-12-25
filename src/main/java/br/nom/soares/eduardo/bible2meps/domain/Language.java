package br.nom.soares.eduardo.bible2meps.domain;

import java.util.List;
import java.util.Optional;

public record Language(String name, String languageTag) {
    public static Optional<Language> getByTag(List<Language> languages, String languageTag) {
        return languages.stream().filter(language -> language.languageTag().equals(languageTag))
                .findFirst();
    }
}
