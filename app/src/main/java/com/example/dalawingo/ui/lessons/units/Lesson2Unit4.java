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

public final class Lesson2Unit4 {
    private Lesson2Unit4() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson2_unit4";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Verbs
        // -------------------------
        items.add(CardItem.title("Verbs"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "In Tagalog, verbs show past, present, and future tense by changing the form of the root word.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo(
                        "Root Word: ",
                        "A word that you can turn into a verb.",
                        false
                ),
                PhraseItem.phraseItemStyle(
                        "",
                        "A word before adding a prefix, infix, or suffix.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        // -------------------------
        // Past Verbs
        // -------------------------
        items.add(CardItem.title("Past Verbs"));

        items.add(CardItem.phraseGroup(Arrays.asList(

                PhraseItem.phraseItemStyle(
                        "",
                        "To turn a root word into a verb, insert -um-.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemStyle(
                        "Root begins with a consonant →",
                        "Placed between first consonant and vowel.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("-um-", null, false),
                PhraseItem.phraseItemStyle(
                        "Root begins with a vowel →",
                        "Placed at the beginning of the word.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("um-", null, false)
        )));

        // Kain / Inom vocab
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Kain", "Eat", true),
                PhraseItem.phraseItem("Inom", "Drink", true)
        )));

        // -um- examples (simplified so it works with the new row model)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Kain",
                        null,
                        "K",
                        "-um-",
                        "ain",
                        null,
                        "Kumain",
                        null,
                        "(Ate)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true),
                PhraseItem.phraseItemPyramid(
                        "Inom",
                        null,
                        null,
                        "um-",
                        "inom",
                        null,
                        "Uminom",
                        null,
                        "(Drank)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true)
        )));

        // Images 1–2
        items.add(CardItem.image(R.drawable.lesson2_unit4image1));
        items.add(CardItem.image(R.drawable.lesson2_unit4image2));

        // -------------------------
        // Using -UM- in Sentences
        // -------------------------
        items.add(CardItem.title("Using -UM- in Sentences"));

        // Tawa / Higa / Labas vocab
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Tawa", "Laugh", true),
                PhraseItem.phraseItem("Higa", "Lie down", true),
                PhraseItem.phraseItem("Labas", "Go out", true)
        )));

        // More -um- build examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Tawa",
                        null,
                        "T",
                        "-um-",
                        "awa",
                        null,
                        "Tumawa",
                        null,
                        "(Laughed)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Bili",
                        null,
                        "B",
                        "-um-",
                        "ili",
                        null,
                        "Bumili",
                        null,
                        "(Bought)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Higa",
                        null,
                        "H",
                        "-um-",
                        "iga",
                        null,
                        "Humiga",
                        null,
                        "(Laid down)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Labas",
                        null,
                        "L",
                        "-um-",
                        "abas",
                        null,
                        "Lumabas",
                        null,
                        "(Went out)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                )
        )));

        // -------------------------
        // Using UM- in Sentences
        // -------------------------
        items.add(CardItem.title("Using UM- in Sentences"));

        // Akyat / Upo vocab
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Akyat", "Climb/Go up", true),
                PhraseItem.phraseItem("Upo", "Sit", true)
        )));

// UM- prefix examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Akyat",
                        null,
                        "A",
                        "-um-",
                        "akyat",
                        null,
                        "Umakyat",
                        null,
                        "(Climbed)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Upo",
                        null,
                        "U",
                        "-um-",
                        "upo",
                        null,
                        "Umupo",
                        null,
                        "(Sat)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        true
                )
        )));

        // Image 3
        items.add(CardItem.image(R.drawable.lesson2_unit4image3));

        // -------------------------
        // Activities
        // -------------------------
        items.add(CardItem.title("Activities"));

        // Image 4
        items.add(CardItem.image(R.drawable.lesson2_unit4image4));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Gusto + kong ",
                        null,
                        null,
                        "+ [-um- verb] + ",
                        null,
                        "sa + [location / receiver]",
                        null,
                        null,
                        null,
                        null,
                        false
                )
        )));

        // Two examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Gusto kong umakyat sa rock.", "I want to climb the rock.", true),
                PhraseItem.phraseItem("Gusto kong lumabas sa bahay.", "I want to go outside the house.", true)
        )));

        // More vocab
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Takbo", "Run", true),
                PhraseItem.phraseItem("Tulong", "Help", true),
                PhraseItem.phraseItem("Kanta", "Sing", true),
                PhraseItem.phraseItem("Tawag", "Call", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Restawran", "Restaurant", true),
                PhraseItem.phraseItem("Sayaw", "Dance", true),
                PhraseItem.phraseItem("Koro", "Choir", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ano ang gusto mong gawin?", "What do you want/like to do?", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Gusto niyang kumain sa restawran.", "She/he wants to eat in the restaurant.", true),
                PhraseItem.phraseItem("Gusto ni Marie na sumayaw.", "Marie likes to dance.", true),
                PhraseItem.phraseItem("Gusto nilang kumanta sa koro.", "They want to sing in the choir.", true),
                PhraseItem.phraseItem("Gusto kong tumakbo sa labas.", "I like to run outside.", true)
        )));

        // -------------------------
        // Mag- and Ma-
        // -------------------------
        items.add(CardItem.title("Mag- and Ma-"));

        // Mag definition
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo("Mag",
                        "- is a marker placed before a root word.", true),

                PhraseItem.phraseItemStyle(
                        null,
                        "Used to turn a root word into an incomplete/contemplated action in the future.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        // Mag examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        "Mag",
                        "luto",
                        "Going to cook"
                ),
                PhraseItem.phraseItemPyramid(
                        null,
                        "Mag",
                        "laro",
                        "Going to play"
                )
        )));

        // Ma definition
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo(
                        "Ma",
                        "- is a marker placed before a root word.",
                        true
                ),
                PhraseItem.phraseItemStyle(
                        null,
                        "Used to show accidental/unintentional actions, or to describe a condition or ability.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        // Ma examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        "Ma",
                        "bili",
                        "Can be bought."
                ),
                PhraseItem.phraseItemPyramid(
                        null,
                        "Ma",
                        "gutom",
                        "Could get hungry."
                )
        )));

        items.add(CardItem.title("More Vocabulary"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Darating", "Upcoming", true),
                PhraseItem.phraseItem("Eleksyon", "Election", true),
                PhraseItem.phraseItem("Sumulat", "Wrote", true),
                PhraseItem.phraseItem("Paligid", "Around / Surroundings", true),
                PhraseItem.phraseItem("Dagat", "Sea / Ocean", true)
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Darating ang eleksyon.",
                        "The election is upcoming.",
                        true
                ),
                PhraseItem.phraseItem(
                        "Sumulat si Maria.",
                        "Maria wrote.",
                        true
                ),
                PhraseItem.phraseItem(
                        "Sa paligid ng bahay.",
                        "Around the house.",
                        true
                ),
                PhraseItem.phraseItem(
                        "Malaki ang dagat.",
                        "The sea is big.",
                        true
                )
        )));

        return items;
    }
}
