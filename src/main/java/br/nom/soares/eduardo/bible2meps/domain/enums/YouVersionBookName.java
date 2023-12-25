package br.nom.soares.eduardo.bible2meps.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YouVersionBookName {

    _GEN(BookName._01_GEN), _EXO(BookName._02_EXO), _LEV(BookName._03_LEV), _NUM(
            BookName._04_NUM), _DEU(BookName._05_DEU), _JOS(BookName._06_JOS), _JDG(
                    BookName._07_JUD), _RUT(BookName._08_RUT), _1SA(BookName._09_1SA), _2SA(
                            BookName._10_2SA), _1KI(BookName._11_1KI), _2KI(BookName._12_2KI), _1CH(
                                    BookName._13_1CH), _2CH(BookName._14_2CH), _EZR(
                                            BookName._15_EZR), _NEH(BookName._16_NEH), _EST(
                                                    BookName._17_EST), _JOB(BookName._18_JOB), _PSA(
                                                            BookName._19_PSA), _PRO(
                                                                    BookName._20_PRO), _ECC(
                                                                            BookName._21_ECC), _SNG(
                                                                                    BookName._22_SON), _ISA(
                                                                                            BookName._23_ISA), _JER(
                                                                                                    BookName._24_JER), _LAM(
                                                                                                            BookName._25_LAM), _EZK(
                                                                                                                    BookName._26_EZE), _DAN(
                                                                                                                            BookName._27_DAN), _HOS(
                                                                                                                                    BookName._28_HOS), _JOL(
                                                                                                                                            BookName._29_JOE), _AMO(
                                                                                                                                                    BookName._30_AMO), _OBA(
                                                                                                                                                            BookName._31_OBA), _JON(
                                                                                                                                                                    BookName._32_JON), _MIC(
                                                                                                                                                                            BookName._33_MIC), _NAM(
                                                                                                                                                                                    BookName._34_NAH), _HAB(
                                                                                                                                                                                            BookName._35_HAB), _ZEP(
                                                                                                                                                                                                    BookName._36_ZEP), _HAG(
                                                                                                                                                                                                            BookName._37_HAG), _ZEC(
                                                                                                                                                                                                                    BookName._38_ZEC), _MAL(
                                                                                                                                                                                                                            BookName._39_MAL), _MAT(
                                                                                                                                                                                                                                    BookName._40_MAT), _MRK(
                                                                                                                                                                                                                                            BookName._41_MAR), _LUK(
                                                                                                                                                                                                                                                    BookName._42_LUK), _JHN(
                                                                                                                                                                                                                                                            BookName._43_JOH), _ACT(
                                                                                                                                                                                                                                                                    BookName._44_ACT), _ROM(
                                                                                                                                                                                                                                                                            BookName._45_ROM), _1CO(
                                                                                                                                                                                                                                                                                    BookName._46_1CO), _2CO(
                                                                                                                                                                                                                                                                                            BookName._47_2CO), _GAL(
                                                                                                                                                                                                                                                                                                    BookName._48_GAL), _EPH(
                                                                                                                                                                                                                                                                                                            BookName._49_EPH), _PHP(
                                                                                                                                                                                                                                                                                                                    BookName._50_PHI), _COL(
                                                                                                                                                                                                                                                                                                                            BookName._51_COL), _1TH(
                                                                                                                                                                                                                                                                                                                                    BookName._52_1TH), _2TH(
                                                                                                                                                                                                                                                                                                                                            BookName._53_2TH), _1TI(
                                                                                                                                                                                                                                                                                                                                                    BookName._54_1TI), _2TI(
                                                                                                                                                                                                                                                                                                                                                            BookName._55_2TI), _TIT(
                                                                                                                                                                                                                                                                                                                                                                    BookName._56_TIT), _PHM(
                                                                                                                                                                                                                                                                                                                                                                            BookName._57_PHM), _HEB(
                                                                                                                                                                                                                                                                                                                                                                                    BookName._58_HEB), _JAS(
                                                                                                                                                                                                                                                                                                                                                                                            BookName._59_JAM), _1PE(
                                                                                                                                                                                                                                                                                                                                                                                                    BookName._60_1PE), _2PE(
                                                                                                                                                                                                                                                                                                                                                                                                            BookName._61_2PE), _1JN(
                                                                                                                                                                                                                                                                                                                                                                                                                    BookName._62_1JO), _2JN(
                                                                                                                                                                                                                                                                                                                                                                                                                            BookName._63_2JO), _3JN(
                                                                                                                                                                                                                                                                                                                                                                                                                                    BookName._64_3JO), _JUD(
                                                                                                                                                                                                                                                                                                                                                                                                                                            BookName._65_JUD), _REV(
                                                                                                                                                                                                                                                                                                                                                                                                                                                    BookName._66_REV);

    private final BookName bookName;

    public String getName() {
        return this.toString().substring(1);
    }

    public static YouVersionBookName fromString(String bookName) {
        for (YouVersionBookName youVersionBookName : YouVersionBookName.values()) {
            if (youVersionBookName.bookName.equals(BookName.valueOf(bookName))) {
                return youVersionBookName;
            }
        }
        throw new IllegalArgumentException("No enum constant found for string: " + bookName);
    }
}
