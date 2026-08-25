package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItemPyramid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit6 {
    private Lesson1Unit6() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson1_unit6";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Questions
        // -------------------------
        items.add(CardItem.title("Questions"));

        // Ba explanation
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ba", "?", true),
                PhraseItem.phraseItemStyle(
                        null,
                        "Placed as the 2nd or last word in a sentence to turn it into a question.",
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                )
        )));

        // Image
        items.add(CardItem.image(R.drawable.unit5image1));

        items.add(CardItem.title("Ni"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ni", "Of\n(Used to show ownership)", true)
        )));

        // Magtatrabaho
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Magtatrabaho", "Will work", true)
        )));

        // Statement -> Question examples (BA)
        /*
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Keyk ito ni Jordan.",
                        "Keyk ",
                        "ba ",
                        "ito ni Jordan?"
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Estudyante kayo.",
                        "Estudyante ",
                        "ba ",
                        "kayo?"
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Magtatrabaho sila sa Lunes.",
                        "Magtatrabaho ",
                        "ba ",
                        "sila sa Lunes?"
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Katrabaho natin si Elena.",
                        "Katrabaho ",
                        "ba ",
                        "natin si Elena?"
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Wallet ito ni Adam.",
                        "Wallet ",
                        "ba ",
                        "ito ni Adam?"
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Bibili tayo ng pagkain.",
                        "Bibili ",
                        "ba ",
                        "tayo ng pagkain?"
                )
        )));
        */

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Keyk ito ni Jordan.",
                        "Keyk ",
                        "ba ",
                        "ito ni Jordan?",
                        null, null, null,
                        "Is this Jordan's cake?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),

                PhraseItem.phraseItemPyramid(
                        "Estudyante kayo.",
                        "Estudyante ",
                        "ba ",
                        "kayo?",
                        null, null, null,
                        "Are you (plural) a student?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),

                PhraseItem.phraseItemPyramid(
                        "Magtatrabaho sila sa Lunes.",
                        "Magtatrabaho ",
                        "ba ",
                        "sila sa Lunes?",
                        null, null, null,
                        "Will they work on Monday?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),

                PhraseItem.phraseItemPyramid(
                        "Katrabaho natin si Elena.",
                        "Katrabaho ",
                        "ba ",
                        "natin si Elena?",
                        null, null, null,
                        "Is Elena our (inclusive) co-worker?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),

                PhraseItem.phraseItemPyramid(
                        "Wallet ito ni Adam.",
                        "Wallet ",
                        "ba ",
                        "ito ni Adam?",
                        null, null, null,
                        "Is this Adam's wallet?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),

                PhraseItem.phraseItemPyramid(
                        "Bibili tayo ng pagkain.",
                        "Bibili ",
                        "ba ",
                        "tayo ng pagkain?",
                        null, null, null,
                        "Will we (inclusive) buy food?",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                )
        )));

        // -------------------------
        // Na
        // -------------------------
        items.add(CardItem.title("Na"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo(
                        "Na",
                        "- a linker for adjectives, nouns, and phrases.",
                        true
                ),
                PhraseItem.phraseItemStyle(
                        null,
                        "It links adjectives to what they describe.",
                        true,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        // Vocab (for NA examples)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Mahaba", "Long", true),
                PhraseItem.phraseItem("Damit", "Clothes", true),
                PhraseItem.phraseItem("Mahal", "Expensive", true)
        )));

        // Adjective + NA + Noun examples (pyramid removed; one-line examples)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Blue bag",
                        null,
                        "Blue ",
                        "na ",
                        "bag",
                        "Asul ",
                        "na ",
                        "bag",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Beautiful flowers",
                        null,
                        "Beautiful ",
                        "na ",
                        "flowers",
                        "Maganda ",
                        "na ",
                        "bulaklak",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Green shoes",
                        null,
                        "Green ",
                        "na ",
                        "shoes",
                        "Berde ",
                        "na ",
                        "sapatos",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Big house",
                        null,
                        "Big ",
                        "na ",
                        "house",
                        "Malaki ",
                        "na ",
                        "bahay",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Small bag",
                        null,
                        "Small ",
                        "na ",
                        "bag",
                        "Maliit ",
                        "na ",
                        "bag",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Long clothes",
                        null,
                        "Long ",
                        "na ",
                        "clothes",
                        "Mahaba ",
                        "na ",
                        "damit",
                        null,
                        null,
                        false
                ),PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Expensive food",
                        null,
                        "Expensive ",
                        "na ",
                        "food",
                        "Mahal ",
                        "na ",
                        "pagkain",
                        null,
                        null,
                        false
                )
        )));

        // -------------------------
        // Numbers
        // -------------------------
        items.add(CardItem.title("Numbers"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Mga Numero", "Numbers", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Isa", "One", true),
                PhraseItem.phraseItem("Dalawa", "Two", true),
                PhraseItem.phraseItem("Tatlo", "Three", true),
                PhraseItem.phraseItem("Apat", "Four", true),
                PhraseItem.phraseItem("Lima", "Five", true),
                PhraseItem.phraseItem("Anim", "Six", true),
                PhraseItem.phraseItem("Pito", "Seven", true),
                PhraseItem.phraseItem("Walo", "Eight", true),
                PhraseItem.phraseItem("Siyam", "Nine", true),
                PhraseItem.phraseItem("Sampu", "Ten", true)
        )));

        // -------------------------
        // -ng
        // -------------------------
        items.add(CardItem.title("-ng "));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo(
                        "-ng",
                        "- if the first word ends in a vowel, you can replace 'na' with '-ng' to combine into one word.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Masaya na asawa",
                        null,
                        "Masay",
                        "a na ",
                        "asawa",
                        "Masay",
                        "a-ng ",
                        "asawa       ",
                        "Masayang asawa",
                        PhraseItemPyramid.FontStyle.BOLD,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Maganda na bahay ",
                        null,
                        "Magand",
                        "a na ",
                        "bahay",
                        "Magand",
                        "a-ng ",
                        "bahay       ",
                        "Magandang bahay",
                        PhraseItemPyramid.FontStyle.BOLD,
                        false
                )
        )));

        // More vocab for -ng practice
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bata", "Child", true),
                PhraseItem.phraseItem("Bago", "New", true),
                PhraseItem.phraseItem("Luma", "Old (object)", true),
                PhraseItem.phraseItem("Mataba", "Is fat", true),
                PhraseItem.phraseItem("Aso", "Dog", true),
                PhraseItem.phraseItem("Pusa", "Cat", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "Bata na masaya →",
                        "Batang masaya",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"
                ),
                PhraseItem.phraseItemStyle(
                        "Bago na bag →",
                        "Bagong bag",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"
                ),
                PhraseItem.phraseItemStyle(
                        "Luma na pusa →",
                        "Lumang sapatos",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"
                ),
                PhraseItem.phraseItemStyle(
                        "Mataba na pusa →",
                        "Matabang pusa",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"
                ),
                PhraseItem.phraseItemStyle(
                        "Isa na aso →",
                        "Isang aso",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"
                )
        )));

        // -------------------------
        // Everyday Words
        // -------------------------
        items.add(CardItem.title("Everyday Words"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Eskwelahan / Paaralan", "School", true),
                PhraseItem.phraseItem("Papasok", "Will enter", true),
                PhraseItem.phraseItem("Relo", "Watch", true),
                PhraseItem.phraseItem("Laruan", "Toy", true),
                PhraseItem.phraseItem("Mangga", "Mango", true),
                PhraseItem.phraseItem("Masarap", "Is delicious", true),
                PhraseItem.phraseItem("Maraming", "Is many / A lot", true),
                PhraseItem.phraseItem("Kaunting", "Few / A little", true),
                PhraseItem.phraseItem("Katabi", "Beside / Next to", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Malaking paaralan ito.", "This is a big school.", true),
                PhraseItem.phraseItem("Papasok ang batang masaya.", "The happy child will enter.", true),
                PhraseItem.phraseItem("Bagong relo ito.", "This is a new watch.", true),
                PhraseItem.phraseItem("Maraming laruan ang bata.", "The child has many toys.", true),
                PhraseItem.phraseItem("Bagong mangga ito.", "This is a fresh mango.", true),
                PhraseItem.phraseItem("Masarap ang bagong pagkain.", "The new food is delicious.", true),
                PhraseItem.phraseItem("Maraming batang masaya.", "There are many happy children.", true),
                PhraseItem.phraseItem("Kaunting pagkain ang dala niya.", "She/he brought a small amount of food.", true),
                PhraseItem.phraseItem("Katabing bahay ang amin.", "Our (exclusive) house is besides.", true)
        )));

        return items;
    }
}
