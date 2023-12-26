package br.nom.soares.eduardo.bible2meps.domain;

import java.util.Arrays;

public class Superscription {
    private static final Integer[] chaptersWithoutSuperscription =
            {1, 2, 10, 33, 43, 71, 91, 93, 94, 95, 96, 97, 99, 104, 105, 106, 107, 111, 112, 113,
                    114, 115, 116, 117, 118, 119, 135, 136, 137, 146, 147, 148, 149, 150};
    public static boolean thisChapterHas(int chapterNumber) {
        return !Arrays.asList(chaptersWithoutSuperscription).contains(chapterNumber);
    }
}
