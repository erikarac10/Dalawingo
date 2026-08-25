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

public final class Lesson2Unit2 {
    private Lesson2Unit2() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson2_unit2";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Demonstrative Pronouns
        // -------------------------
        items.add(CardItem.title("Demonstrative Pronouns"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ito", "This", true),
                PhraseItem.phraseItem("Iyan", "That (near)", true),
                PhraseItem.phraseItem("Iyon", "That (far)", true)
        )));

        items.add(CardItem.image(R.drawable.lesson2_unit2image1));

        // Ito example (split: prompt, image, answer)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ano ito?", "What is this?", true),
                PhraseItem.image(R.drawable.lesson2_unit2image2),
                PhraseItem.phraseItem("Ito ay puto.", "This is puto.\n(Filipino Steamed Rice Cakes)", true)
        )));

        // Iyan example
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ano iyan?", "What is that? (near)", true),
                PhraseItem.image(R.drawable.lesson2_unit2image3),
                PhraseItem.phraseItem(
                        "Iyan ay tabo.",
                        "That is a tabo.\n(Filipino Hygiene Tool)",
                        true
                )
        )));

        // Iyon example
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ano iyon?", "What is that? (far)", true),
                PhraseItem.image(R.drawable.lesson2_unit2image4),
                PhraseItem.phraseItem(
                        "Iyon ay walis.",
                        "That is a broom.\n(Filipino Straw Broom)",
                        true
                )
        )));

        // Extra image card (from XML)
        items.add(CardItem.image(R.drawable.lesson2_unit2image13));

        // -------------------------
        // Numbers
        // -------------------------
        items.add(CardItem.title("Numbers"));

        // 1–20 (kept in the same two-column-per-row order as the table)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table1(new String[][]{
                        {"1",  "isa",    "11", "labing-isa"},
                        {"2",  "dalawa", "12", "labing-dalawa"},
                        {"3",  "tatlo",  "13", "labing-tatlo"},
                        {"4",  "apat",   "14", "labing-apat"},
                        {"5",  "lima",   "15", "labing-lima"},
                        {"6",  "anim",   "16", "labing-anim"},
                        {"7",  "pito",   "17", "labing-pito"},
                        {"8",  "walo",   "18", "labing-walo"},
                        {"9",  "siyam",  "19", "labing-siyam"},
                        {"10", "sampu",  "20", "dalawampu"}
                })
        )));

        // 21–30
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table2(new String[][]{
                        {"21", "dalawampu't isa"},
                        {"22", "dalawampu't dalawa"},
                        {"23", "dalawampu't tatlo"},
                        {"24", "dalawampu't apat"},
                        {"25", "dalawampu't lima"},
                        {"26", "dalawampu't anim"},
                        {"27", "dalawampu't pito"},
                        {"28", "dalawampu't walo"},
                        {"29", "dalawampu't siyam"},
                        {"30", "tatlumpu"}
                })
        )));

        // 40–100
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table2(new String[][]{
                        {"40",  "apatnapu"},
                        {"50",  "limampu"},
                        {"60",  "animnapu"},
                        {"70",  "pitumpu"},
                        {"80",  "walumpu"},
                        {"90",  "siyamnapu"},
                        {"100", "isang daan"}
                })
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("36",  "tatlumpu't anim", true),
                PhraseItem.phraseItem("69",  "animnapu't siyam", true),
                PhraseItem.phraseItem("81",  "walumpu't isa", true),
                PhraseItem.phraseItem("82",  "walumpu't dalawa", true),
                PhraseItem.phraseItem("121", "isang daan dalawampu't isa", true)
        )));

        // -------------------------
        // Using Numbers in Sentences
        // -------------------------
        items.add(CardItem.title("Using Numbers in Sentences"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "In Tagalog, when putting a number before a noun...",
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemStyle(
                        "Number ends in a consonant →",
                        "attach the linker (-na) to the number",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemStyle(
                        "Number ends in a vowel →",
                        "attach the linker (-ng) to the number",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Apat aso",
                        "Apat",
                        "-na ",
                        "aso",
                        null, "Apatna aso", null,
                        "Four dogs",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        "Isa pusa",
                        "Isa",
                        "-ng ",
                        "pusa",
                        null, "Isang pusa", null,
                        "One cat",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false
                )
        )));

        // Vocab for the practice
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ilan?", "How many?", true),
                PhraseItem.phraseItem("Bituin", "Star", true),
                PhraseItem.phraseItem("Watawat", "Flag", true),
                PhraseItem.phraseItem("Pilipinas", "Philippines", true)
        )));

        // Practice sentence
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ilan ang bituin sa watawat ng Pilipinas?",
                        "How many stars are there on the Philippine flag?",
                        true
                ),
                PhraseItem.phraseItem(
                        "Mayroong tatlong bituin sa watawat ng Pilipinas.",
                        "There are three stars on the Philippine flag.",
                        true
                )
        )));

        // More vocab
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Kutsara", "Spoon", true),
                PhraseItem.phraseItem("Lumpia", "Spring roll", true),
                PhraseItem.phraseItem("Larawan", "Picture", true),
                PhraseItem.phraseItem("Plato", "Plate", true)
        )));

        // Image practice 1
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ilan ang kutsara sa larawan?",
                        "How many spoons are there in the picture?",
                        true
                ),
                PhraseItem.image(R.drawable.lesson2_unit2image7),
                PhraseItem.phraseItem(
                        "Mayroong labindalawang kutsara sa larawan.",
                        "There are twelve spoons in the picture.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ilan ang lumpia sa plato?",
                        "How many lumpia are there on the plate?",
                        true
                ),
                PhraseItem.image(R.drawable.lesson2_unit2image8),
                PhraseItem.phraseItem(
                        "May siyam na lumpia sa plato.",
                        "There are nine spring rolls on the plate.",
                        true
                )
        )));


        // -------------------------
        // Plural Form
        // -------------------------
        items.add(CardItem.title("Plural Form"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "",
                        "To make nouns and demonstrative pronouns plural, add the word:",
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("mga", "", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bilis", "Speed", true),
                PhraseItem.phraseItem("Mabilis", "Is / It is fast", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "Maliit ang tricycle →",
                        "(The tricycle is small)",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.ITALIC
                ),
                PhraseItem.phraseItemStyle(
                        "Maliit ang mga tricycle",
                        "(The tricycles are small)",
                        true,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.ITALIC
                ),
                PhraseItem.phraseItemStyle(
                        "Mabilis ang jeepney →",
                        "(The jeepney is fast)",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.ITALIC
                ),
                PhraseItem.phraseItemStyle(
                        "Mabilis ang mga jeepney",
                        "(The jeepneys are fast)",
                        true,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.ITALIC
                )
        )));

        // Plural demonstrative examples
        items.add(CardItem.image(R.drawable.lesson2_unit2image9));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ano ang mga ito?",
                        "What are these?",
                        true
                ),
                PhraseItem.image(R.drawable.lesson2_unit2image10),
                PhraseItem.phraseItem(
                        "Ito ay mga puto.",
                        "These are puto.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ano ang mga iyan?",
                        "What are those?",
                        true
                ),
                PhraseItem.image(R.drawable.lesson2_unit2image11),
                PhraseItem.phraseItem(
                        "Iyan ay mga tabo.",
                        "Those are tabos.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ano ang mga iyon?",
                        "What are those?",
                        true
                ),
                PhraseItem.image(R.drawable.lesson2_unit2image12),
                PhraseItem.phraseItem(
                        "Iyon ay mga walis.",
                        "Those are brooms.",
                        true
                )
        )));

        // -------------------------
        // Linking Two Nouns
        // -------------------------
        items.add(CardItem.title("Linking Two Nouns"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "",
                        "To link two nouns, use the linker:",
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("at", "(and)", true)
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Tricycle ",
                        "at ",
                        "Jeepney.",
                        "Tricycle and Jeepney."),
                PhraseItem.phraseItemPyramid(
                        "Mga tabo ",
                        "at ",
                        "mga walis tambo.",
                        "Tabos and brooms."
                ),
                PhraseItem.phraseItemPyramid(
                        "Tatlong tabo ",
                        "at ",
                        "anim na walis tambo.",
                        "Three tabos and six brooms."
                )
        )));

        return items;
    }
}
