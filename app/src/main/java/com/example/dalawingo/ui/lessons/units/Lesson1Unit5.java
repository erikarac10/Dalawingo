package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItemCharts;
import com.example.dalawingo.ui.lessons.cards.PhraseItemPyramid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit5 {
    private Lesson1Unit5() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson1_unit5";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Personal Pronouns
        // -------------------------
        items.add(CardItem.title("Personal Pronouns"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Akin", "Mine", true),
                PhraseItem.phraseItem("Iyo", "Yours", true),
                PhraseItem.phraseItem("Kanya", "Her/His", true),
                PhraseItem.phraseItem("Amin", "Ours (exclusive)", true),
                PhraseItem.phraseItem("Atin", "Ours (inclusive)", true),
                PhraseItem.phraseItem("Inyo", "Yours (plural)", true),
                PhraseItem.phraseItem("Kanila", "Theirs/Them", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Akin ito.", "This is mine.", true),
                PhraseItem.phraseItem("Iyo ang sapatos.", "The shoes are yours.", true),
                PhraseItem.phraseItem("Kanya ang pitaka.", "The wallet is her/his.", true),
                PhraseItem.phraseItem("Amin ang kwarto.", "The room is ours (exclusive).", true),
                PhraseItem.phraseItem("Atin ang pagkain.", "The food is ours (inclusive).", true),
                PhraseItem.phraseItem("Inyo ang keyk.", "The cake is yours (plural).", true),
                PhraseItem.phraseItem("Kanila ang bahay.", "The house is theirs.", true)
        )));

        // -------------------------
        // Ay
        // -------------------------
        items.add(CardItem.title("Ay"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ay", "Is", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ang pitaka ay kanya.", "The wallet is her/his.", true),
                PhraseItem.phraseItem("Ang kwarto ay amin.", "The room is ours (exclusive).", true),
                PhraseItem.phraseItem("Ang araw ay Lunes.", "The day is Monday.", true),
                PhraseItem.phraseItem("Ang bulaklak ay pula.", "The flower is red.", true)
        )));

        // -------------------------
        // Sa
        // -------------------------
        items.add(CardItem.title("Sa"));

        // Sa meanings (kept as normal rows)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemInfo(
                        "Sa",
                        "is a preposition that can mean",
                        true
                ),
                PhraseItem.phraseItem("to, at, in, for, on",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        false),
                PhraseItem.phraseItem(null,
                        null,
                        null,
                        null,
                        null,
                        "depending on the context.",
                        null,
                        false)
        )));

        // LOCATION
        items.add(CardItem.title("Sa marks the LOCATION"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Nag-aaral ako ",
                        "sa ",
                        "University of California",
                        null,
                        null,
                        null,
                        "I am studying at University of California.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null,null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Nagtatrabaho ako ",
                        "sa ",
                        "ospital.",
                        null,
                        null,
                        null,
                        "I work at the hospital.",
                        null,
                        false
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka nag-aaral?", "Where do you study?", true),
                PhraseItem.phraseItem("Nag-aaral ako sa _____.", "I study at _____.", true),
                PhraseItem.phraseItem("Nagtatrabaho ako sa _____.", "I work at _____.", true)
        )));

        // DIRECTION
        items.add(CardItem.title("Sa marks the DIRECTION"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Pupunta ako ",
                        "sa ",
                        "California bukas.",
                        null,
                        null,
                        null,
                        "I will go to California tomorrow.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Ibinigay niya ang pagkain ",
                        "sa ",
                        "kaibigan niya.",
                        null,
                        null,
                        null,
                        "She/he gave the food to her/his friend.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Umalis siya ",
                        "sa ",
                        "bahay.",
                        null,
                        null,
                        null,
                        "She/he left (from) the house.",
                        null,
                        false
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka pupunta mamaya?", "Where are you going later?", true),
                PhraseItem.phraseItem("Pupunta ako sa __(location)__.", "I will go to _____.", true)
        )));

        // BENEFICIARY
        items.add(CardItem.title("Sa marks the BENEFICIARY OF AN ACTION OR OBJECT."));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Bibili ako ng pagkain para ",
                        "sa ",
                        "nanay ko.",
                        null,
                        null,
                        null,
                        "I will buy food for my mom.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Magluluto ako para ",
                        "sa ",
                        "asawa ko.",
                        null,
                        null,
                        null,
                        "I will cook for my spouse (wife/husband).",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Maglilinis ako ng bahay para ",
                        "sa ",
                        "bisita ko.",
                        null,
                        null,
                        null,
                        "I will clean the house for my visitor.",
                        null,
                        false
                )
        )));

        items.add(CardItem.image(R.drawable.unit4image5b));

        // Unit 4 - CHART (5 rows x 3 columns)
        items.add(CardItem.phraseGroup(List.of(PhraseItem.chartItem(
                                        "Verb",
                                        "Actor / Pronoun",
                                        "Object",
                                        new String[][]{
                                                { "Bibili",    "ako",      "adobo" },
                                                { "Magluluto", "ka",       "kotse" },
                                                { "Kukuha",    "siya",     "keyk" },
                                                { "Gagawa",    "kami",     "regalo" },
                                                { "Magdala",   "si _____", "sinigang" }
                                        },
                                        new String[][]{
                                                { "(will buy)",  "(I)",              "(stew)" },
                                                { "(will cook)", "(you)",            "(car)" },
                                                { "(will get)",  "(she/he)",         "(cake)" },
                                                { "(will make)", "(we without you)", "(gift)" },
                                                { "(bring)",     "(name)",           "(tamarind soup)" }
                                        }))));


        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bibili akong adobo para sa Nanay ko.", "I will buy adobo for my mom.", true)
        )));

        items.add(CardItem.title("Sa marks a FUTURE DATE"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Magtatrabaho ako ",
                        "sa ",
                        "Lunes.",
                        null,
                        null,
                        null,
                        "I will work on Monday.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        PhraseItemPyramid.FontStyle.REGULAR,
                        "Magcecelebrate kami ng birthday ",
                        "sa ",
                        "Abril 8.",
                        null,
                        null,
                        null,
                        "We will celebrate a birthday on April 8.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        null,
                        "Pupunta ako ",
                        "sa ",
                        "Texas",
                        "sa ",
                        "Disyembre.",
                        null,
                        "I will go to Texas in December.",
                        null,
                        false
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Magbabakasyon ako sa __(day/month/season)__.",
                        "I will go on vacation in __(date)__.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka pupunta pagkatapos ng klase?", "Where will you go after class?", true),
                PhraseItem.phraseItem("Pupunta ako sa _____ pagkatapos ng klase.", "I will go to _____ after class.", true)
        )));

        // -------------------------
        // Sa + Personal Pronouns
        // -------------------------
        items.add(CardItem.title("Sa and Personal Pronouns"));

        //items.add(CardItem.image(R.drawable.unit4image7));
        //items.add(CardItem.image(R.drawable.unit4image9));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "When 'Sa' comes before a pronoun that starts with a vowel, drop the first vowel and replace it with 'Sa'.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("(Sa) akin = Sakin", "Mine", true),
                PhraseItem.phraseItem("(Sa) iyo = Sayo", "Yours", true),
                PhraseItem.phraseItem("(Sa) kanya = Sa kanya", "Her/His", true),
                PhraseItem.phraseItem("(Sa) amin = Samin", "Ours (exclusive)", true),
                PhraseItem.phraseItem("(Sa) atin = Satin", "Ours (inclusive)", true),
                PhraseItem.phraseItem("(Sa) inyo = Sanyo", "Yours (plural)", true),
                PhraseItem.phraseItem("(Sa) kanila = Sa kanila", "Theirs (plural)", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Sakin ang pera.", "The money is mine.", true),
                PhraseItem.phraseItem("Sayo ang keyk.", "The cake is yours.", true),
                PhraseItem.phraseItem("Sa kanya ang bahay.", "The house is her/his.", true),
                PhraseItem.phraseItem("Samin ang kwarto.", "The room is ours (exclusive).", true),
                PhraseItem.phraseItem("Satin ang pagkain.", "The food is ours (inclusive).", true),
                PhraseItem.phraseItem("Sanyo ang mga dokumento.", "The documents are yours (plural).", true),
                PhraseItem.phraseItem("Sa kanila ang bulaklak.", "The flowers are theirs.", true)
        )));

        // -------------------------
        // Kay and Kina
        // -------------------------
        items.add(CardItem.title("Kay and Kina"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Kay and Kina",
                        null,
                        null,
                        null,
                        "\nUsed instead of 'Sa' when talking about people's names.",
                        null,
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(
                        "Kay",
                        " - for one person's name.",
                        false
                ),
                PhraseItem.phraseItemInfo(
                        "Kina",
                        " - for more than one person's name.",
                        false
                ),
                PhraseItem.phraseItem("At", "And", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemPyramid(
                        null,
                        null,
                        "Bag ",
                        "kay ",
                        "Avery.",
                        null,
                        null,
                        null,
                        "Avery's bag.",
                        null,
                        false
                ),
                PhraseItem.phraseItemInfo(null, null, false),
                PhraseItem.phraseItemPyramid(
                        null,
                        null,
                        "Bag ",
                        "kina ",
                        "Avery ",
                        null,
                        "at ",
                        "Ezio.",
                        "Avery and Ezio's bag. ",
                        null,
                        false
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bibili ako ng regalo para kay _____.", "I will buy a gift for _____.", true),
                PhraseItem.phraseItem("Magluluto ako ng adobo para kay _____.", "I will cook adobo for _____.", true),
                PhraseItem.phraseItem("Magshoshopping ako para kina _____ at _____.", "I will go shopping for _____ and _____.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bibili ako ng pagkain para kay Lisa.", "I will buy food for Lisa.", false),
                PhraseItem.phraseItem("Magluluto ako para kina Lisa at Aaron.", "I will cook for Lisa and Aaron.", false)
        )));

        return items;
    }
}
