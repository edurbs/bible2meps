package br.nom.soares.eduardo.bible2meps.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum YouVersionBookName {

    BOOK_GEN(BookName.BOOK_01_GEN),
    BOOK_EXO(BookName.BOOK_02_EXO),
    BOOK_LEV(BookName.BOOK_03_LEV),
    BOOK_NUM(BookName.BOOK_04_NUM),
    BOOK_DEU(BookName.BOOK_05_DEU),
    BOOK_JOS(BookName.BOOK_06_JOS),
    BOOK_JDG(BookName.BOOK_07_JUD),
    BOOK_RUT(BookName.BOOK_08_RUT),
    BOOK_1SA(BookName.BOOK_09_1SA),
    BOOK_2SA(BookName.BOOK_10_2SA),
    BOOK_1KI(BookName.BOOK_11_1KI),
    BOOK_2KI(BookName.BOOK_12_2KI),
    BOOK_1CH(BookName.BOOK_13_1CH),
    BOOK_2CH(BookName.BOOK_14_2CH),
    BOOK_EZR(BookName.BOOK_15_EZR),
    BOOK_NEH(BookName.BOOK_16_NEH),
    BOOK_EST(BookName.BOOK_17_EST),
    BOOK_JOB(BookName.BOOK_18_JOB),
    BOOK_PSA(BookName.BOOK_19_PSA),
    BOOK_PRO(BookName.BOOK_20_PRO),
    BOOK_ECC(BookName.BOOK_21_ECC),
    BOOK_SNG(BookName.BOOK_22_SON),
    BOOK_ISA(BookName.BOOK_23_ISA),
    BOOK_JER(BookName.BOOK_24_JER),
    BOOK_LAM(BookName.BOOK_25_LAM),
    BOOK_EZK(BookName.BOOK_26_EZE),
    BOOK_DAN(BookName.BOOK_27_DAN),
    BOOK_HOS(BookName.BOOK_28_HOS),
    BOOK_JOL(BookName.BOOK_29_JOE),
    BOOK_AMO(BookName.BOOK_30_AMO),
    BOOK_OBA(BookName.BOOK_31_OBA),
    BOOK_JON(BookName.BOOK_32_JON),
    BOOK_MIC(BookName.BOOK_33_MIC),
    BOOK_NAM(BookName.BOOK_34_NAH),
    BOOK_HAB(BookName.BOOK_35_HAB),
    BOOK_ZEP(BookName.BOOK_36_ZEP),
    BOOK_HAG(BookName.BOOK_37_HAG),
    BOOK_ZEC(BookName.BOOK_38_ZEC),
    BOOK_MAL(BookName.BOOK_39_MAL),
    BOOK_MAT(BookName.BOOK_40_MAT),
    BOOK_MRK(BookName.BOOK_41_MAR),
    BOOK_LUK(BookName.BOOK_42_LUK),
    BOOK_JHN(BookName.BOOK_43_JOH),
    BOOK_ACT(BookName.BOOK_44_ACT),
    BOOK_ROM(BookName.BOOK_45_ROM),
    BOOK_1CO(BookName.BOOK_46_1CO),
    BOOK_2CO(BookName.BOOK_47_2CO),
    BOOK_GAL(BookName.BOOK_48_GAL),
    BOOK_EPH(BookName.BOOK_49_EPH),
    BOOK_PHP(BookName.BOOK_50_PHI),
    BOOK_COL(BookName.BOOK_51_COL),
    BOOK_1TH(BookName.BOOK_52_1TH),
    BOOK_2TH(BookName.BOOK_53_2TH),
    BOOK_1TI(BookName.BOOK_54_1TI),
    BOOK_2TI(BookName.BOOK_55_2TI),
    BOOK_TIT(BookName.BOOK_56_TIT),
    BOOK_PHM(BookName.BOOK_57_PHM),
    BOOK_HEB(BookName.BOOK_58_HEB),
    BOOK_JAS(BookName.BOOK_59_JAM),
    BOOK_1PE(BookName.BOOK_60_1PE),
    BOOK_2PE(BookName.BOOK_61_2PE),
    BOOK_1JN(BookName.BOOK_62_1JO),
    BOOK_2JN(BookName.BOOK_63_2JO),
    BOOK_3JN(BookName.BOOK_64_3JO),
    BOOK_JUD(BookName.BOOK_65_JUD),
    BOOK_REV(BookName.BOOK_66_REV);

    private final BookName bookName;

    public String getName() {
        return this.toString().substring(5);
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
