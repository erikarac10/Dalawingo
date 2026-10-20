package com.example.dalawingo.ui.lessons.cards;

/* JADX INFO: loaded from: classes10.dex */
public final class PhraseItem {
    public final String[][] chartEnglish;
    public final String chartHeader1;
    public final String chartHeader2;
    public final String chartHeader3;
    public final String[][] chartTagalog;
    public final String english1;
    public final CardAdapter.Align english1Align;
    public final String english1SizeSp;
    public final CardAdapter.FontStyle english1Style;
    public final String english2;
    public final CardAdapter.Align english2Align;
    public final CardAdapter.FontStyle english2Style;
    public final String english3;
    public final String explanation1;
    public final CardAdapter.Align explanation1Align;
    public final CardAdapter.FontStyle explanation1Style;
    public final String explanation2;
    public final String explanation3;
    public final Integer imageResId;
    public final ItemType itemType;
    public final String py1A;
    public final PhraseItemPyramid.FontStyle py1AStyle;
    public final String py2A;
    public final String py2B;
    public final String py2C;
    public final String py3A;
    public final String py3B;
    public final String py3C;
    public final String py4;
    public final PhraseItemPyramid.FontStyle py4Style;
    public final boolean showSpeaker;
    public final String[][] table1;
    public final String[][] table2;
    public final String[][] table3;
    public final String table3RootMeaning;
    public final String table3RootWord;
    public final String[][] table4;
    public final String[] table4Headers;
    public final String[][] table5;
    public final String[] table5Headers;
    public final String tagalog1;
    public final CardAdapter.Align tagalog1Align;
    public final String tagalog1SizeSp;
    public final CardAdapter.FontStyle tagalog1Style;
    public final String tagalog2;
    public final String tagalog3;

    public enum ItemType {
        NORMAL,
        INFO,
        PYRAMID,
        CHART,
        TABLE1,
        TABLE2,
        TABLE3,
        TABLE4,
        TABLE5,
        IMAGE
    }

    PhraseItem(ItemType type, String t1, String t2, String t3, String e1, String e2, String e3, String ex1, String ex2, String ex3, boolean speak, CardAdapter.Align tAlign, CardAdapter.FontStyle tStyle, String tSize, CardAdapter.Align eAlign, CardAdapter.FontStyle eStyle, String eSize, CardAdapter.Align e2Align, CardAdapter.FontStyle e2Style, CardAdapter.Align xAlign, CardAdapter.FontStyle xStyle, String h1, String h2, String h3, String[][] cT, String[][] cE, String[][] tb1, String[][] tb2, String[][] tb3, String[][] tb4, String rootW, String rootM, String[] h4, String[][] tb5, String[] h5, String p1, PhraseItemPyramid.FontStyle p1s, String p2a, String p2b, String p2c, String p3a, String p3b, String p3c, String p4, PhraseItemPyramid.FontStyle p4s, Integer img) {
        this.itemType = type;
        this.tagalog1 = t1;
        this.tagalog2 = t2;
        this.tagalog3 = t3;
        this.english1 = e1;
        this.english2 = e2;
        this.english3 = e3;
        this.explanation1 = ex1;
        this.explanation2 = ex2;
        this.explanation3 = ex3;
        this.showSpeaker = speak;
        this.tagalog1Align = tAlign;
        this.tagalog1Style = tStyle;
        this.tagalog1SizeSp = tSize;
        this.english1Align = eAlign;
        this.english1Style = eStyle;
        this.english1SizeSp = eSize;
        this.english2Align = e2Align;
        this.english2Style = e2Style;
        this.explanation1Align = xAlign;
        this.explanation1Style = xStyle;
        this.chartHeader1 = h1;
        this.chartHeader2 = h2;
        this.chartHeader3 = h3;
        this.chartTagalog = cT;
        this.chartEnglish = cE;
        this.table1 = tb1;
        this.table2 = tb2;
        this.table3 = tb3;
        this.table4 = tb4;
        this.table3RootWord = rootW;
        this.table3RootMeaning = rootM;
        this.table4Headers = h4;
        this.table5 = tb5;
        this.table5Headers = h5;
        this.py1A = p1;
        this.py1AStyle = p1s != null ? p1s : PhraseItemPyramid.FontStyle.REGULAR;
        this.py2A = p2a;
        this.py2B = p2b;
        this.py2C = p2c;
        this.py3A = p3a;
        this.py3B = p3b;
        this.py3C = p3c;
        this.py4 = p4;
        this.py4Style = p4s != null ? p4s : PhraseItemPyramid.FontStyle.REGULAR;
        this.imageResId = img;
    }

    public static PhraseItem phraseItem(String t, String e, boolean s) {
        return new PhraseItem(ItemType.NORMAL, t, null, null, e, null, null, null, null, null, s, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItem(String tagalog, String english, String explanation, boolean showSpeaker) {
        return new PhraseItem(ItemType.NORMAL, tagalog, null, null, english, null, null, explanation, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemInfo(String tagalog, String english, boolean showSpeaker) {
        return new PhraseItem(ItemType.INFO, tagalog, null, null, english, null, null, null, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemStyle(String tagalog, String english, boolean showSpeaker, CardAdapter.Align tagalogAlign, CardAdapter.FontStyle tagalogStyle, CardAdapter.Align englishAlign, CardAdapter.FontStyle englishStyle) {
        return new PhraseItem(ItemType.NORMAL, tagalog, null, null, english, null, null, null, null, null, showSpeaker, tagalogAlign, tagalogStyle, null, englishAlign, englishStyle, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItem(String t1, String t2, String t3, String e1, String e2, String e3, String ex1, boolean showSpeaker) {
        return new PhraseItem(ItemType.NORMAL, t1, t2, t3, e1, e2, e3, ex1, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String unused, PhraseItemPyramid.FontStyle line1Style, String line1, String line2A, String line2B, String line2C, String line3A, String line3B, String line3C, String line4, PhraseItemPyramid.FontStyle line4Style, boolean showSpeaker) {
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, line1, line1Style, line2A, line2B, line2C, line3A, line3B, line3C, line4, line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String line2A, String line2B, String line2C) {
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, line2A, line2B, line2C, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem table4(String[] headers, String[][] rows) {
        return new PhraseItem(ItemType.TABLE4, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rows, null, null, headers, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String line1, PhraseItemPyramid.FontStyle line1Style, String line2A, String line2B, String line2C, String line3A, String line3B, String line3C, String line4, PhraseItemPyramid.FontStyle line4Style, boolean showSpeaker) {
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, line1, line1Style != null ? line1Style : PhraseItemPyramid.FontStyle.REGULAR, line2A, line2B, line2C, line3A, line3B, line3C, line4, line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemStyle(String tagalog, String english, boolean showSpeaker, CardAdapter.Align tagalogAlign, CardAdapter.FontStyle tagalogStyle, String tagalogSizeSp, CardAdapter.Align englishAlign, CardAdapter.FontStyle englishStyle, String englishSizeSp) {
        return new PhraseItem(ItemType.NORMAL, tagalog, null, null, english, null, null, null, null, null, showSpeaker, tagalogAlign, tagalogStyle, tagalogSizeSp, englishAlign, englishStyle, englishSizeSp, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String line2A, String line2B, String line2C, String line4) {
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, line2A, line2B, line2C, null, null, null, line4, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String line1, String line2A, String line2B, String line2C, String line3A, String line3B, String line3C, PhraseItemPyramid.FontStyle line4Style, String line4, boolean showSpeaker) {
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, line1, PhraseItemPyramid.FontStyle.REGULAR, line2A, line2B, line2C, line3A, line3B, line3C, line4, line4Style != null ? line4Style : PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem phraseItemPyramid(String line1, String line2A, String line2B, String line2C, String line3A, String line3B, String line3C, Object styleOrText, Object textOrStyle, boolean showSpeaker) {
        PhraseItemPyramid.FontStyle finalStyle;
        String finalLine;
        PhraseItemPyramid.FontStyle finalStyle2 = PhraseItemPyramid.FontStyle.REGULAR;
        if (styleOrText instanceof PhraseItemPyramid.FontStyle) {
            String finalLine2 = (String) textOrStyle;
            finalStyle = (PhraseItemPyramid.FontStyle) styleOrText;
            finalLine = finalLine2;
        } else if (!(styleOrText instanceof String)) {
            finalStyle = finalStyle2;
            finalLine = null;
        } else {
            String finalLine3 = (String) styleOrText;
            if (!(textOrStyle instanceof PhraseItemPyramid.FontStyle)) {
                finalStyle = finalStyle2;
                finalLine = finalLine3;
            } else {
                finalStyle = (PhraseItemPyramid.FontStyle) textOrStyle;
                finalLine = finalLine3;
            }
        }
        return new PhraseItem(ItemType.PYRAMID, null, null, null, null, null, null, null, null, null, showSpeaker, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, line1, PhraseItemPyramid.FontStyle.REGULAR, line2A, line2B, line2C, line3A, line3B, line3C, finalLine, finalStyle, null);
    }

    public static PhraseItem chartItem(String h1, String h2, String h3, String[][] t, String[][] e) {
        return new PhraseItem(ItemType.CHART, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, h1, h2, h3, t, e, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem table1(String[][] rows) {
        return new PhraseItem(ItemType.TABLE1, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rows, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem table2(String[][] rows) {
        return new PhraseItem(ItemType.TABLE2, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rows, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem table3(String rootW, String rootM, String[][] rows) {
        return new PhraseItem(ItemType.TABLE3, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rows, null, rootW, rootM, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }

    public static PhraseItem image(int resId) {
        return new PhraseItem(ItemType.IMAGE, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, Integer.valueOf(resId));
    }

    public static PhraseItem table5(String[] headers, String[][] rows) {
        return new PhraseItem(ItemType.TABLE5, null, null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, rows, headers, null, PhraseItemPyramid.FontStyle.REGULAR, null, null, null, null, null, null, null, PhraseItemPyramid.FontStyle.REGULAR, null);
    }
}
