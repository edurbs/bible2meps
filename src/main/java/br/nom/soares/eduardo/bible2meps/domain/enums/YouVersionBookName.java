package br.nom.soares.eduardo.bible2meps.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YouVersionBookName {

    _GEN(BookName.BOOK_01_GEN), _EXO(BookName.BOOK_02_EXO), _LEV(BookName.BOOK_03_LEV), _NUM(
            BookName.BOOK_04_NUM), _DEU(BookName.BOOK_05_DEU), _JOS(BookName.BOOK_06_JOS), _JDG(
                    BookName.BOOK_07_JUD), _RUT(BookName.BOOK_08_RUT), _1SA(BookName.BOOK_09_1SA), _2SA(
                            BookName.BOOK_10_2SA), _1KI(BookName.BOOK_11_1KI), _2KI(BookName.BOOK_12_2KI), _1CH(
                                    BookName.BOOK_13_1CH), _2CH(BookName.BOOK_14_2CH), _EZR(
                                            BookName.BOOK_15_EZR), _NEH(BookName.BOOK_16_NEH), _EST(
                                                    BookName.BOOK_17_EST), _JOB(BookName.BOOK_18_JOB), _PSA(
                                                            BookName.BOOK_19_PSA), _PRO(
                                                                    BookName.BOOK_20_PRO), _ECC(
                                                                            BookName.BOOK_21_ECC), _SNG(
                                                                                    BookName.BOOK_22_SON), _ISA(
                                                                                            BookName.BOOK_23_ISA), _JER(
                                                                                                    BookName.BOOK_24_JER), _LAM(
                                                                                                            BookName.BOOK_25_LAM), _EZK(
                                                                                                                    BookName.BOOK_26_EZE), _DAN(
                                                                                                                            BookName.BOOK_27_DAN), _HOS(
                                                                                                                                    BookName.BOOK_28_HOS), _JOL(
                                                                                                                                            BookName.BOOK_29_JOE), _AMO(
                                                                                                                                                    BookName.BOOK_30_AMO), _OBA(
                                                                                                                                                            BookName.BOOK_31_OBA), _JON(
                                                                                                                                                                    BookName.BOOK_32_JON), _MIC(
                                                                                                                                                                            BookName.BOOK_33_MIC), _NAM(
                                                                                                                                                                                    BookName.BOOK_34_NAH), _HAB(
                                                                                                                                                                                            BookName.BOOK_35_HAB), _ZEP(
                                                                                                                                                                                                    BookName.BOOK_36_ZEP), _HAG(
                                                                                                                                                                                                            BookName.BOOK_37_HAG), _ZEC(
                                                                                                                                                                                                                    BookName.BOOK_38_ZEC), _MAL(
                                                                                                                                                                                                                            BookName.BOOK_39_MAL), _MAT(
                                                                                                                                                                                                                                    BookName.BOOK_40_MAT), _MRK(
                                                                                                                                                                                                                                            BookName.BOOK_41_MAR), _LUK(
                                                                                                                                                                                                                                                    BookName.BOOK_42_LUK), _JHN(
                                                                                                                                                                                                                                                            BookName.BOOK_43_JOH), _ACT(
                                                                                                                                                                                                                                                                    BookName.BOOK_44_ACT), _ROM(
                                                                                                                                                                                                                                                                            BookName.BOOK_45_ROM), _1CO(
                                                                                                                                                                                                                                                                                    BookName.BOOK_46_1CO), _2CO(
                                                                                                                                                                                                                                                                                            BookName.BOOK_47_2CO), _GAL(
                                                                                                                                                                                                                                                                                                    BookName.BOOK_48_GAL), _EPH(
                                                                                                                                                                                                                                                                                                            BookName.BOOK_49_EPH), _PHP(
                                                                                                                                                                                                                                                                                                                    BookName.BOOK_50_PHI), _COL(
                                                                                                                                                                                                                                                                                                                            BookName.BOOK_51_COL), _1TH(
                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_52_1TH), _2TH(
                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_53_2TH), _1TI(
                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_54_1TI), _2TI(
                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_55_2TI), _TIT(
                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_56_TIT), _PHM(
                                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_57_PHM), _HEB(
                                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_58_HEB), _JAS(
                                                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_59_JAM), _1PE(
                                                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_60_1PE), _2PE(
                                                                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_61_2PE), _1JN(
                                                                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_62_1JO), _2JN(
                                                                                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_63_2JO), _3JN(
                                                                                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_64_3JO), _JUD(
                                                                                                                                                                                                                                                                                                                                                                                                                                            BookName.BOOK_65_JUD), _REV(
                                                                                                                                                                                                                                                                                                                                                                                                                                                    BookName.BOOK_66_REV);

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
