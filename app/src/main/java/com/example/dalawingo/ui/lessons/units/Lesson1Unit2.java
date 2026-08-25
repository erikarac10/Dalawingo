package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit2 {
    private Lesson1Unit2() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson1_unit2";

        // Title (uses the new TITLE view type)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // ----------------------------
        // Pronouns
        // ----------------------------
        items.add(CardItem.title("Pronouns"));

        // Inclusive vs Exclusive (info block)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "Inclusive vs Exclusive",
                        "\nIn Tagalog, some words change depending on whether the listener is INCLUDED in the group or EXCLUDED from it.",
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemInfo(
                        "Inclusive -",
                        " Refers to people being talked about AND includes the listener.",
                        false
                ),
                PhraseItem.phraseItemInfo(
                        "Exclusive -",
                        " Refers to people being talked about, but leaves out the listener.",
                        false
                )
        )));

        // Pronoun list
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ako",  "I", true),
                PhraseItem.phraseItem("Ka",   "You", true),
                PhraseItem.phraseItem("Siya", "She/He", true),
                PhraseItem.phraseItem("Kami", "We (exclusive)", true),
                PhraseItem.phraseItem("Tayo", "We (inclusive)", true),
                PhraseItem.phraseItem("Kayo", "You (plural)", true),
                PhraseItem.phraseItem("Sila", "They (plural)", true)
        )));

        // ----------------------------
        // Jobs
        // ----------------------------
        items.add(CardItem.title("Jobs"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Guro / Titser", "Teacher", true),
                PhraseItem.phraseItem("Estudyante", "Student", true),
                PhraseItem.phraseItem("Doktor", "Doctor", true),
                PhraseItem.phraseItem("Nars", "Nurse", true),
                PhraseItem.phraseItem("Pulis", "Police", true),
                PhraseItem.phraseItem("Abugado", "Lawyer", true),
                PhraseItem.phraseItem("Negosyante", "Business (woman/man)", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Kawani", "Employee", true),
                PhraseItem.phraseItem("Magsasaka", "Farmer", true),
                PhraseItem.phraseItem("Manunulat", "Writer", true),
                PhraseItem.phraseItem("Pintor", "Painter", true),
                PhraseItem.phraseItem("Karpintero", "Carpenter", true),
                PhraseItem.phraseItem("Mananayaw", "Dancer", true)
        )));

        // Images (exact order preserved)
        items.add(CardItem.image(R.drawable.unit2image1));
        items.add(CardItem.image(R.drawable.unit2image2));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Inhinyero", "Engineer", true),
                PhraseItem.phraseItem("Tagapaggupit", "Haircutter", true),
                PhraseItem.phraseItem("Mamamayan", "Citizen", true),
                PhraseItem.phraseItem("Botante", "Voter", true)
        )));

        items.add(CardItem.image(R.drawable.unit2image4));

        // ----------------------------
        // The 'Si' marker
        // ----------------------------
        items.add(CardItem.title("The 'Si' marker"));
        items.add(CardItem.image(R.drawable.unit2image3));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Abugado si Edward.", "Edward is a lawyer.", true),
                PhraseItem.phraseItem("Negosyante si Lara.", "Lara is a businesswoman.", true)
        )));

        items.add(CardItem.image(R.drawable.unit2image5));

        return items;
    }
}
