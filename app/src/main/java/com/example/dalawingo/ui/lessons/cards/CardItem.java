package com.example.dalawingo.ui.lessons.cards;

import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class CardItem {
    public final Integer imageRes;
    public final List<PhraseItem> phraseItems;
    public final String titleText;

    private CardItem(List<PhraseItem> phraseItems, Integer imageRes, String titleText) {
        this.phraseItems = phraseItems;
        this.imageRes = imageRes;
        this.titleText = titleText;
    }

    public static CardItem phraseGroup(List<PhraseItem> phraseItems) {
        return new CardItem(phraseItems, null, null);
    }

    public static CardItem image(int imageRes) {
        return new CardItem(null, Integer.valueOf(imageRes), null);
    }

    public static CardItem title(String titleText) {
        return new CardItem(null, null, titleText);
    }

    public static CardItem titleFromUnitKey(String unitKey, String unitTitle) {
        String[] parts = unitKey.split("_");
        String lesson = parts.length > 0 ? parts[0].replaceAll("\\D+", "") : "";
        String unit = parts.length > 1 ? parts[1].replaceAll("\\D+", "") : "";
        String fullTitle = "Lesson " + lesson + " Unit " + unit + ":\n" + unitTitle;
        return title(fullTitle);
    }
}
