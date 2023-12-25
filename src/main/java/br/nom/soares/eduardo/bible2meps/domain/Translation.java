package br.nom.soares.eduardo.bible2meps.domain;

import java.util.List;
import java.util.Optional;

public record Translation(String id, String abbreviation, String localTitle) {
    public static Optional<Translation> getById(List<Translation> translations, String id) {
        return translations.stream().filter(translation -> translation.id().equals(id)).findFirst();
    }

}
