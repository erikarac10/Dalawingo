package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItemPyramid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson2Unit6 {
    private Lesson2Unit6() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson2_unit6";

        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        items.add(CardItem.title("Focus Types"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "Tagalog verbs change form to show time and what the sentence is focusing on.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "-Um- , Mag- , Ma-",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(
                        "Focus: ",
                        "Actor who did it.",
                        false
                )
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "In- , I-",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(
                        "Focus: ",
                        "What was done/happened to an object.",
                        false
                )
        )));

        items.add(CardItem.title("Prefixes"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "The prefix tells how the action happens. (intentional, ongoing, accidental/state, etc.)",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("-Um-", "Did/Does\n(Completed action)", true),
                PhraseItem.phraseItem("Mag-", "Going to\n(Incompleted, Contemplated action)", true),
                PhraseItem.phraseItem("Ma-", "State/Happens to\n(Ability, Condition, Accidental or Unintentional Action)", true),PhraseItem.phraseItemStyle(
                        null,
                        "* 'ma-' verbs turn into 'na-' for past & present tense",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("-In-", "Done to\n(Focuses on Object Acted Upon)", true),
                PhraseItem.phraseItem("I-", "Put/Give/Use\n(Applying, Transferring, or Using Object)", true)
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Gutom ako.", "I'm hungry.", true),
                PhraseItem.phraseItem("Magutom ako mamaya.", "I'll get hungry later.", true),
                PhraseItem.phraseItem("Nagutom ako.", "I got hungry.", true)
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(

                PhraseItem.phraseItemStyle(
                        null,
                        "Some root words naturally belong to one prefix:",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("Lamig", "Cold", true),
                PhraseItem.phraseItem("Malamig", "It's cold.", true),
                PhraseItem.phraseItemStyle(
                        null,
                        "Because cold is something that happens involuntarily, it uses 'ma-'.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));
        // Chart 1: Prefix + Focus + Key Use
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table5(
                        new String[]{"Prefix", "Focus", "Key Use"},
                        new String[][]{
                                {"-UM-",  "Actor",             "Completed"},
                                {"MAG-",  "Actor",             "Incomplete / Contemplated"},
                                {"MA-",   "Actor /\nState",     "Ability /\nCondition /\nAccidental /\nUnintentional"},
                                {"-IN-",  "Object",            "Object acted upon"},
                                {"I-",    "Object /\nTransfer", "Apply /\nTransfer /\nUsing"}
                        }
                )
        )));

        items.add(CardItem.title("Tense"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Past Tense",
                        "[First Consonant of Root Word]\n+ [Prefix]\n+ Root Word",
                        false),
                PhraseItem.phraseItemPyramid(
                        "Dala",
                        null,
                        "",
                        "Mag-",
                        "dala",
                        null,
                        "Magdala",
                        null,
                        "(Brought)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Present Tense",
                        "[First Consonant of Root Word]\n+ [Prefix]\n+ [First Syllable of Root Word]\n+ Root Word",
                        false),
                PhraseItem.phraseItemPyramid(
                        "Gutom",
                        null,
                        null,
                        "Ma-gu-",
                        "gutom",
                        null,
                        "Magugutom",
                        null,
                        "(Is Hungry)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false)
        )));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Future Tense",
                        "[First Syllable of Root Word]\n+ Root Word",
                        false),
                PhraseItem.phraseItemPyramid(
                        "Bigay",
                        null,
                        null,
                        "Bi-",
                        "bigay",
                        null,
                        "Bibigay",
                        null,
                        "(Will give)",
                        PhraseItemPyramid.FontStyle.ITALIC,
                        false)
        )));

        items.add(CardItem.title("Verb Examples"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Higa",
                        "(lie down)",
                        new String[][]{
                                {"Past",    "h",   "um",    "iga",   "(laid down)"},
                                {"Present", "",    "humi",  "higa",  "(lying down)"},
                                {"Future",  "",    "hi",    "higa",  "(will lie down)"}
                        }))
        ));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Dala",
                        "(bring)",
                        new String[][]{
                                {"Past",    "",   "nag",   "dala", "(brought)"},
                                {"Present", "",   "nagda", "dala", "(bringing)"},
                                {"Future",  "",   "magda", "dala", "(will bring)"}
                        }))
        ));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Buhay",
                        "(live)",
                        new String[][]{
                                {"Past",    "",   "na",    "buhay",  "(lived / came alive)"},
                                {"Present", "",   "nabu",  "buhay",  "(living)"},
                                {"Future",  "",   "mabu",  "buhay",  "(will live)"}
                        }))
        ));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Gawa",
                        "(do/make)",
                        new String[][]{
                                {"Past",    "",   "gin",  "awa",  "(did it / made it)"},
                                {"Present", "",   "gina", "gawa", "(doing it / making it)"},
                                {"Future",  "",   "gaga", "win",  "(will do it / will make it)"}
                        }))
        ));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Sama",
                        "(include/bring along)",
                        new String[][]{
                                {"Past",    "",   "isi",  "nama", "(included / brought along)"},
                                {"Present", "",   "isina","sama", "(including / bringing along)"},
                                {"Future",  "",   "isa",  "sama", "(will include / will bring along)"}
                        }))
        ));

        return items;
    }
}
