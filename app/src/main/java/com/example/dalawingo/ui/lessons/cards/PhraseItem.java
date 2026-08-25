package com.example.dalawingo.ui.lessons.cards;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class PhraseItem {

    public enum ItemType {
        NORMAL, INFO, PYRAMID,
        CHART,
        TABLE1, TABLE2, TABLE3, TABLE4, TABLE5,

        IMAGE
    }

    @NonNull public final ItemType itemType;

    // ================= NORMAL / INFO =================
    @Nullable public final String tagalog1;
    @Nullable public final String tagalog2;
    @Nullable public final String tagalog3;

    @Nullable public final String english1;
    @Nullable public final String english2;
    @Nullable public final String english3;

    @Nullable public final String explanation1;
    @Nullable public final String explanation2;
    @Nullable public final String explanation3;

    public final boolean showSpeaker;

    @Nullable public final CardAdapter.Align tagalog1Align;
    @Nullable public final CardAdapter.FontStyle tagalog1Style;
    @Nullable public final String tagalog1SizeSp;

    @Nullable public final CardAdapter.Align english1Align;
    @Nullable public final CardAdapter.FontStyle english1Style;
    @Nullable public final String english1SizeSp;

    @Nullable public final CardAdapter.Align english2Align;
    @Nullable public final CardAdapter.FontStyle english2Style;

    @Nullable public final CardAdapter.Align explanation1Align;
    @Nullable public final CardAdapter.FontStyle explanation1Style;

    // ================= CHART =================
    @Nullable public final String chartHeader1;
    @Nullable public final String chartHeader2;
    @Nullable public final String chartHeader3;
    @Nullable public final String[][] chartTagalog;
    @Nullable public final String[][] chartEnglish;

    // ================= TABLES =================
    @Nullable public final String[][] table1;
    @Nullable public final String[][] table2;
    @Nullable public final String[][] table3;
    @Nullable public final String[][] table4;
    @Nullable public final String[][] table5;
    @Nullable public final String[] table5Headers;

    @Nullable public final String table3RootWord;
    @Nullable public final String table3RootMeaning;
    @Nullable public final String[] table4Headers;

    // ================= PYRAMID =================
    @Nullable public final String py1A;
    @NonNull  public final PhraseItemPyramid.FontStyle py1AStyle;

    @Nullable public final String py2A;
    @Nullable public final String py2B;
    @Nullable public final String py2C;

    @Nullable public final String py3A;
    @Nullable public final String py3B;
    @Nullable public final String py3C;

    @Nullable public final String py4;
    @NonNull  public final PhraseItemPyramid.FontStyle py4Style;

    // ================= IMAGE =================
    @Nullable public final Integer imageResId;

    // =====================================================
    // PRIVATE BASE
    // =====================================================
    PhraseItem(
            ItemType type,

            String t1, String t2, String t3,
            String e1, String e2, String e3,
            String ex1, String ex2, String ex3,
            boolean speak,

            CardAdapter.Align tAlign, CardAdapter.FontStyle tStyle, String tSize,
            CardAdapter.Align eAlign, CardAdapter.FontStyle eStyle, String eSize,
            CardAdapter.Align e2Align, CardAdapter.FontStyle e2Style,
            CardAdapter.Align xAlign, CardAdapter.FontStyle xStyle,

            String h1, String h2, String h3,
            String[][] cT, String[][] cE,

            String[][] tb1, String[][] tb2, String[][] tb3, String[][] tb4,
            String rootW, String rootM, String[] h4,String[][] tb5, String[] h5,

            String p1, PhraseItemPyramid.FontStyle p1s,
            String p2a, String p2b, String p2c,
            String p3a, String p3b, String p3c,
            String p4, PhraseItemPyramid.FontStyle p4s,

            Integer img
    ) {
        itemType = type;

        tagalog1 = t1; tagalog2 = t2; tagalog3 = t3;
        english1 = e1; english2 = e2; english3 = e3;
        explanation1 = ex1; explanation2 = ex2; explanation3 = ex3;

        showSpeaker = speak;

        tagalog1Align = tAlign;
        tagalog1Style = tStyle;
        tagalog1SizeSp = tSize;

        english1Align = eAlign;
        english1Style = eStyle;
        english1SizeSp = eSize;

        english2Align = e2Align;
        english2Style = e2Style;

        explanation1Align = xAlign;
        explanation1Style = xStyle;

        chartHeader1 = h1;
        chartHeader2 = h2;
        chartHeader3 = h3;
        chartTagalog = cT;
        chartEnglish = cE;

        table1 = tb1;
        table2 = tb2;
        table3 = tb3;
        table4 = tb4;

        table3RootWord = rootW;
        table3RootMeaning = rootM;
        table4Headers = h4;
        table5 = tb5;
        table5Headers = h5;

        py1A = p1;
        py1AStyle = p1s != null ? p1s : PhraseItemPyramid.FontStyle.REGULAR;
        py2A = p2a; py2B = p2b; py2C = p2c;
        py3A = p3a; py3B = p3b; py3C = p3c;
        py4 = p4;
        py4Style = p4s != null ? p4s : PhraseItemPyramid.FontStyle.REGULAR;

        imageResId = img;
    }

    // =====================================================
    // FACTORIES
    // =====================================================

    public static PhraseItem phraseItem(String t, String e, boolean s) {
        return new PhraseItem(
                ItemType.NORMAL,
                t,null,null,
                e,null,null,
                null,null,null,
                s,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItem(
            @NonNull String tagalog,
            @NonNull String english,
            @Nullable String explanation,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.NORMAL,
                tagalog, null, null,
                english, null, null,
                explanation, null, null,
                showSpeaker,

                null, null, null,
                null, null, null,
                null, null,
                null, null,

                null, null, null,
                null, null,

                // tb1..tb4
                null, null, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null, null, null,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItemInfo(
            @NonNull String tagalog,
            @NonNull String english,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.INFO,
                tagalog, null, null,
                english, null, null,
                null, null, null,
                showSpeaker,

                null, null, null,
                null, null, null,
                null, null,
                null, null,

                null, null, null,
                null, null,

                // tb1..tb4
                null, null, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null, null, null,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItemStyle(
            @NonNull String tagalog,
            @Nullable String english,
            boolean showSpeaker,
            @Nullable CardAdapter.Align tagalogAlign,
            @Nullable CardAdapter.FontStyle tagalogStyle,
            @Nullable CardAdapter.Align englishAlign,
            @Nullable CardAdapter.FontStyle englishStyle
    ) {
        return new PhraseItem(
                ItemType.NORMAL,
                tagalog, null, null,
                english, null, null,
                null, null, null,
                showSpeaker,

                tagalogAlign, tagalogStyle, null,
                englishAlign, englishStyle, null,
                null, null,
                null, null,

                null, null, null,
                null, null,

                // tb1..tb4
                null, null, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null, null, null,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItem(
            @Nullable String t1,
            @Nullable String t2,
            @Nullable String t3,
            @Nullable String e1,
            @Nullable String e2,
            @Nullable String e3,
            @Nullable String ex1,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.NORMAL,
                t1, t2, t3,
                e1, e2, e3,
                ex1, null, null,
                showSpeaker,

                null, null, null,
                null, null, null,
                null, null,
                null, null,

                null, null, null,
                null, null,

                // tb1..tb4
                null, null, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null, null, null,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItemPyramid(
            @Nullable String unused,
            @NonNull PhraseItemPyramid.FontStyle line1Style,
            @Nullable String line1,
            @Nullable String line2A,
            @Nullable String line2B,
            @Nullable String line2C,
            @Nullable String line3A,
            @Nullable String line3B,
            @Nullable String line3C,
            @Nullable String line4,
            @Nullable PhraseItemPyramid.FontStyle line4Style,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                showSpeaker,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                line1, line1Style,
                line2A, line2B, line2C,
                line3A, line3B, line3C,
                line4, (line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR),

                null
        );
    }

// ADD THIS overload to PhraseItem so phraseItemPyramid(String,String,String) compiles

    public static PhraseItem phraseItemPyramid(
            @Nullable String line2A,
            @Nullable String line2B,
            @Nullable String line2C
    ) {
        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                null, PhraseItemPyramid.FontStyle.REGULAR,
                line2A, line2B, line2C,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem table4(String[] headers, String[][] rows) {
        return new PhraseItem(
                ItemType.TABLE4,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4:
                null,null,null,rows,

                // rootW, rootM, h4:
                null, null, headers,

                // tb5, h5:
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,
                null
        );
    }

    public static PhraseItem phraseItemPyramid(
            @Nullable String line1,
            @Nullable PhraseItemPyramid.FontStyle line1Style,
            @Nullable String line2A,
            @Nullable String line2B,
            @Nullable String line2C,
            @Nullable String line3A,
            @Nullable String line3B,
            @Nullable String line3C,
            @Nullable String line4,
            @Nullable PhraseItemPyramid.FontStyle line4Style,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                showSpeaker,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                line1, (line1Style != null ? line1Style : PhraseItemPyramid.FontStyle.REGULAR),
                line2A, line2B, line2C,
                line3A, line3B, line3C,
                line4, (line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR),

                null
        );
    }

    public static PhraseItem phraseItemStyle(
            @NonNull String tagalog,
            @Nullable String english,
            boolean showSpeaker,

            @Nullable CardAdapter.Align tagalogAlign,
            @Nullable CardAdapter.FontStyle tagalogStyle,
            @Nullable String tagalogSizeSp,

            @Nullable CardAdapter.Align englishAlign,
            @Nullable CardAdapter.FontStyle englishStyle,
            @Nullable String englishSizeSp
    ) {
        return new PhraseItem(
                ItemType.NORMAL,

                tagalog, null, null,
                english, null, null,
                null, null, null,
                showSpeaker,

                tagalogAlign, tagalogStyle, tagalogSizeSp,
                englishAlign, englishStyle, englishSizeSp,
                null, null,
                null, null,

                null, null, null,
                null, null,

                // tb1..tb4
                null, null, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null, null, null,
                null, null, null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItemPyramid(
            @Nullable String line2A,
            @Nullable String line2B,
            @Nullable String line2C,
            @Nullable String line4
    ) {
        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                null, PhraseItemPyramid.FontStyle.REGULAR,
                line2A, line2B, line2C,
                null, null, null,
                line4, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem phraseItemPyramid(
            @Nullable String line1,
            @Nullable String line2A,
            @Nullable String line2B,
            @Nullable String line2C,
            @Nullable String line3A,
            @Nullable String line3B,
            @Nullable String line3C,
            @Nullable PhraseItemPyramid.FontStyle line4Style,
            @Nullable String line4,
            boolean showSpeaker
    ) {
        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                showSpeaker,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                line1, PhraseItemPyramid.FontStyle.REGULAR,
                line2A, line2B, line2C,
                line3A, line3B, line3C,
                line4, (line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR),

                null
        );
    }


    public static PhraseItem phraseItemPyramid(
            String line1,
            String line2A,
            String line2B,
            String line2C,
            String line3A,
            String line3B,
            String line3C,
            Object styleOrText,
            Object textOrStyle,
            boolean showSpeaker
    ) {
        PhraseItemPyramid.FontStyle finalStyle = PhraseItemPyramid.FontStyle.REGULAR;
        String finalLine = null;

        if (styleOrText instanceof PhraseItemPyramid.FontStyle) {
            finalStyle = (PhraseItemPyramid.FontStyle) styleOrText;
            finalLine = (String) textOrStyle;
        } else if (styleOrText instanceof String) {
            finalLine = (String) styleOrText;
            if (textOrStyle instanceof PhraseItemPyramid.FontStyle) {
                finalStyle = (PhraseItemPyramid.FontStyle) textOrStyle;
            }
        }

        return new PhraseItem(
                ItemType.PYRAMID,

                null,null,null,
                null,null,null,
                null,null,null,
                showSpeaker,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                // chart headers + chart data
                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                // pyramid fields
                line1, PhraseItemPyramid.FontStyle.REGULAR,
                line2A, line2B, line2C,
                line3A, line3B, line3C,
                finalLine, finalStyle,

                null
        );
    }


    public static PhraseItem chartItem(String h1, String h2, String h3, String[][] t, String[][] e) {
        return new PhraseItem(
                ItemType.CHART,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                h1,h2,h3,
                t,e,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }


    public static PhraseItem table1(String[][] rows) {
        return new PhraseItem(
                ItemType.TABLE1,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                rows, null, null, null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

    public static PhraseItem table2(String[][] rows) {
        return new PhraseItem(
                ItemType.TABLE2,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                null, rows, null, null,
                // rootW, rootM, h4
                null, null, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }


    public static PhraseItem table3(String rootW, String rootM, String[][] rows) {
        return new PhraseItem(
                ItemType.TABLE3,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                null, null, rows, null,
                // rootW, rootM, h4
                rootW, rootM, null,
                // tb5, h5
                null, null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }


    public static PhraseItem image(@DrawableRes int resId) {
        return new PhraseItem(
                ItemType.IMAGE,
                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                null,null,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                resId
        );
    }
    public static PhraseItem table5(String[] headers, String[][] rows) {
        return new PhraseItem(
                ItemType.TABLE5,

                null,null,null,
                null,null,null,
                null,null,null,
                false,

                null,null,null,
                null,null,null,
                null,null,
                null,null,

                null,null,null,
                null,null,

                // tb1..tb4
                null,null,null,null,
                // rootW, rootM, h4
                null,null,null,
                // tb5, h5
                rows, headers,

                null, PhraseItemPyramid.FontStyle.REGULAR,
                null,null,null,
                null,null,null,
                null, PhraseItemPyramid.FontStyle.REGULAR,

                null
        );
    }

}
