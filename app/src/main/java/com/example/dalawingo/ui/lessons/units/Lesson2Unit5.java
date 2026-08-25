// Lesson2Unit5.java
package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItemCharts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson2Unit5 {
    private Lesson2Unit5() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson2_unit5";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Present Tense
        // -------------------------
        items.add(CardItem.title("Present Tense"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "To turn a root word into a present tense verb, repeat the first syllable and insert -um-.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "Root begins with a consonant →",
                        "Placed between first syllable's consonant and vowel.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemStyle(
                        "[first consonant]  +  -um-  +  [first vowel]",
                        null,
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "15sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "17sp"
                ),
                PhraseItem.phraseItemStyle(
                        "Root begins with a vowel →",
                        "Placed at the beginning of the first syllable.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemStyle(
                        "um-   +   [first vowel]",
                        null,
                        false,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "15sp",
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "17sp"
                )
        )));
        // Present examples (simplified to work with the new row model)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Bili",
                        null,
                        null,
                        "B-um-i",
                        "-bili",
                        null,
                        "Bumibili",
                        null,
                        "(Buying)",
                        null,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Inom",
                        null,
                        null,
                        "Um-i",
                        "-nom",
                        null,
                        "Umiinom",
                        null,
                        "(Drinking)",
                        null,
                        true
                )
        )));

        // -------------------------
        // Future Tense
        // -------------------------
        items.add(CardItem.title("Future Tense"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "To turn a root word into a future tense verb, repeat the first syllable.",
                        false,
                            CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        // Future examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        "Bili",
                        null,
                        null,
                        "Bi-",
                        "ibili",
                        null,
                        "Bibili",
                        null,
                        "(will buy)",
                        null,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Inom",
                        null,
                        null,
                        "I-",
                        "inom",
                        null,
                        "Iinom",
                        null,
                        "(will drink)",
                        null,
                        true
                )
        )));

        // Mag- note + examples
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "You can also do this with 'Mag-' to make a definite future action by repeating the first syllable.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItemPyramid(
                        "Luto",
                        null,
                        null,
                        "Mag-lu-",
                        "luto",
                        null,
                        "Magluluto",
                        null,
                        "(Going to cook)",
                        null,
                        true
                ),
                PhraseItem.phraseItemPyramid(
                        "Lakad",
                        null,
                        null,
                        "Mag-la-",
                        "lakad",
                        null,
                        "Maglalakad",
                        null,
                        "(Going to walk)",
                        null,
                        true
                )
        )));

        // -------------------------
        // Action Words
        // -------------------------
        items.add(CardItem.title("Action Words"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bangon", "Wake up", true),
                PhraseItem.phraseItem("Gising", "Wake up", true),
                PhraseItem.phraseItem("Langoy", "Swim", true),
                PhraseItem.phraseItem("Lakad", "Walk", true),
                PhraseItem.phraseItem("Takbo", "Run", true)
        )));

        // -------------------------
        // Consonant Verb Practice
        // -------------------------
        items.add(CardItem.title("Consonant Verb Practice"));

        // Sample TABLE 3 usage (card_phrase_item_table3.xml)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Bili",
                        "(buy)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "b",   "um",  "ili",  "(bought)"},
                                {"Present", "", "bumi", "bili", "(buying)"},
                                {"Future",  "", "bi", "bili", "(will buy)"}
                        }))));

        // Bangon (get up)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Bangon",
                        "(get up)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "b",   "um",   "angon",  "(got up)"},
                                {"Present", "",    "bum",  "abangon","(getting up)"},
                                {"Future",  "",    "ba",   "bangon", "(will get up)"}
                        }))));

        // Gising (wake up)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Gising",
                        "(wake up)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "g",   "um",   "ising",  "(woke up)"},
                                {"Present", "",    "gum",  "igising","(waking up)"},
                                {"Future",  "",    "gi",   "gising", "(will wake up)"}
                        }))));

        // Langoy (swim)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Langoy",
                        "(swim)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "l",   "um",   "angoy",  "(swam)"},
                                {"Present", "",    "lum",  "alangoy","(swimming)"},
                                {"Future",  "",    "la",   "langoy", "(will swim)"}
                        }))));

        // Lakad (walk)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Lakad",
                        "(walk)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "l",   "um",   "akad",   "(walked)"},
                                {"Present", "",    "lum",  "alakad", "(walking)"},
                                {"Future",  "",    "la",   "lakad",  "(will walk)"}
                        }))));

        // Takbo (run)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Takbo",
                        "(run)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "t",   "um",   "akbo",   "(ran)"},
                                {"Present", "",    "tum",  "atakbo", "(running)"},
                                {"Future",  "",    "ta",   "takbo",  "(will run)"}
                        }))));


        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Sisid", "Dive", true),
                PhraseItem.phraseItem("Lipad", "Fly", true),
                PhraseItem.phraseItem("Tayo", "Stand", true),
                PhraseItem.phraseItem("Biyahe", "Travel", true)
        )));

        // =========================
        // TABLE 4 sample call
        // (matches card_phrase_item_table4.xml)
        // rows are: [rootWord, rootMeaning, pastWord, pastMeaning, presentWord, presentMeaning, futureWord, futureMeaning]
        // max 6 rows (your binder loops r=1..6)
        // =========================
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table4(
                        new String[]{"Root", "Past", "Present", "Future"},
                        new String[][]{
                                // sisid
                                {"Sisid",  "Dive",   "sumisid",   "(dived)",     "sumisisid",  "(diving)",    "sisisid",   "(will dive)"},
                                // lipad
                                {"Lipad",  "Fly",    "lumipad",   "(flew)",      "lumilipad",  "(flying)",   "lilipad",  "(will fly)"},
                                // tayo
                                {"Tayo",   "Stand",  "tumayo",    "(stood up)",  "tumatayo",   "(standing)", "tatayo",   "(will stand)"},
                                // biyahe
                                {"Biyahe", "Travel", "bumiyahe",  "(traveled)",  "bumabyahe",  "(traveling)","babiyahe", "(will travel)"},
                                // lipad – sayaw
                                {"Sayaw",  "Dance",  "sumayaw",   "(danced)",   "sumasayaw",  "(dancing)",  "sasayaw",  "(will dance)"},
                                // kain
                                {"Kain",   "Eat",    "kumain",    "(ate)",       "kumakain",   "(eating)",   "kakain",   "(will eat)"},
                                // punta
                                {"Punta",  "Go",     "pumunta",   "(went)",      "pumupunta",  "(going)",    "pupunta",  "(will go)"}
                        }))));

        // -------------------------
        // Verb Vowel Practice
        // -------------------------
        items.add(CardItem.title("Verb Vowel Practice"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Inom",
                        "(drink)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "",    "um",   "inom",   "(drank)"},
                                {"Present", "",    "umi",  "inom",   "(drinking)"},
                                {"Future",  "",    "i",    "inom",   "(will drink)"}
                        }))));
        // Upo (sit)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Upo",
                        "(sit)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "",    "um",   "upo",    "(sat)"},
                                {"Present", "",    "umu",  "upo",    "(sitting)"},
                                {"Future",  "",    "u",    "upo",    "(will sit)"}
                        }))));

        // Ulan (rain)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table3(
                        "Ulan",
                        "(rain)",
                        new String[][]{
                                // [label, a, b (bold), c, english]
                                {"Past",    "",    "um",   "ulan",   "(rained)"},
                                {"Present", "",    "umu",  "ulan",   "(raining)"},
                                {"Future",  "",    "u",    "ulan",   "(will rain)"}
                        }))));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.table4(
                        new String[]{"Root", "Past", "Present", "Future"},
                        new String[][]{
                                // inom
                                {"Inom",  "Drink",  "uminom",   "(drank)",     "umiinom",   "(drinking)",  "iinom",  "(will drink)"},
                                // akyat
                                {"Akyat", "Climb",  "umakyat",  "(climbed)",   "umaakyat",  "(climbing)",  "aakyat",  "(will climb)"}
                        }
                )
        )));

        items.add(CardItem.title("Past, Present, Future Sentences"));

        // Card 1: Bundok, Maaga, Ate, Kuya
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Papunta", "On the way/Headed to", true),
                PhraseItem.phraseItem("Bundok", "Mountain", true),
                PhraseItem.phraseItem("Maaga", "Early", true),
                PhraseItem.phraseItem("Ate", "Older sister", true),
                PhraseItem.phraseItem("Kuya", "Older brother", true)
        )));

        // Card 3: extracted sentence pairs (Tagalog + English)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Umakyat si Ate Cherry sa bundok.", "Ate Cherry climbed the mountain.", true),
                PhraseItem.phraseItem("Gigising ako nang maaga.", "I will wake up early.", true),
                PhraseItem.phraseItem("Umakyat si Kuya JR sa puno.", "Kuya JR climbed the tree.", true),
                PhraseItem.phraseItem("Papunta si Ate at Kuya sa bundok.", "Ate and Kuya are on the way to the mountain.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bumangon ako.", "I got up.", true),
                PhraseItem.phraseItem("Iinom siya ng tubig.", "She/he will drink water.", true),
                PhraseItem.phraseItem("Tumatakbo siya sa bahay.", "She/he is running in the house.", true),
                PhraseItem.phraseItem("Si Marites ay lumalangoy sa pool.", "Marites is swimming in the pool.", true),
                PhraseItem.phraseItem("Maglalakad kami papuntang paaralan bukas.", "We will walk to school tomorrow.", true),
                PhraseItem.phraseItem("Uminom ng Coca-cola sina Lolo at Lola.", "Lolo and Lola drank Coca-cola.", true),
                PhraseItem.phraseItem("Tumakbo ako papuntang supermarket.", "I ran to the supermarket.", true)
        )));

        items.add(CardItem.title("See if you can turn these into verbs yourself!"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Sumali", "Join", true),
                PhraseItem.phraseItem("Bumoto", "Vote", true),
                PhraseItem.phraseItem("Sumunod", "Obey/follow", true),
                PhraseItem.phraseItem("Umupa", "Rent", true)
        )));
        return items;
    }
}
