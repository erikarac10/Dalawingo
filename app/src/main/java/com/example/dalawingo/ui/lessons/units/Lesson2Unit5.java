package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes5.dex */
public final class Lesson2Unit5 {
    private Lesson2Unit5() {
    }

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();
        items.add(CardItem.titleFromUnitKey("lesson2_unit5", UnitData.getUnitTitle("lesson2_unit5")));
        items.add(CardItem.title("Present Tense"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle(null, "To turn a root word into a present tense verb, repeat the first syllable and insert -um-.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle("Root begins with a consonant →", "Placed between first syllable's consonant and vowel.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItemStyle("[first consonant]  +  -um-  +  [first vowel]", null, false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "15sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "17sp"), PhraseItem.phraseItemStyle("Root begins with a vowel →", "Placed at the beginning of the first syllable.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItemStyle("um-   +   [first vowel]", null, false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "15sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, "17sp"))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemPyramid("Bili", null, null, "B-um-i", "-bili", null, "Bumibili", null, "(Buying)", null, true), PhraseItem.phraseItemPyramid("Inom", null, null, "Um-i", "-nom", null, "Umiinom", null, "(Drinking)", null, true))));
        items.add(CardItem.title("Future Tense"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle(null, "To turn a root word into a future tense verb, repeat the first syllable.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemPyramid("Bili", null, null, "Bi-", "ibili", null, "Bibili", null, "(will buy)", null, true), PhraseItem.phraseItemPyramid("Inom", null, null, "I-", "inom", null, "Iinom", null, "(will drink)", null, true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle(null, "You can also do this with 'Mag-' to make a definite future action by repeating the first syllable.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItemPyramid("Luto", null, null, "Mag-lu-", "luto", null, "Magluluto", null, "(Going to cook)", null, true), PhraseItem.phraseItemPyramid("Lakad", null, null, "Mag-la-", "lakad", null, "Maglalakad", null, "(Going to walk)", null, true))));
        items.add(CardItem.title("Action Words"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Bangon", "Wake up", true), PhraseItem.phraseItem("Gising", "Wake up", true), PhraseItem.phraseItem("Langoy", "Swim", true), PhraseItem.phraseItem("Lakad", "Walk", true), PhraseItem.phraseItem("Takbo", "Run", true))));
        items.add(CardItem.title("Consonant Verb Practice"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Bili", "(buy)", new String[][]{new String[]{"Past", "b", "um", "ili", "(bought)"}, new String[]{"Present", "", "bumi", "bili", "(buying)"}, new String[]{"Future", "", "bi", "bili", "(will buy)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Bangon", "(get up)", new String[][]{new String[]{"Past", "b", "um", "angon", "(got up)"}, new String[]{"Present", "", "bum", "abangon", "(getting up)"}, new String[]{"Future", "", "ba", "bangon", "(will get up)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Gising", "(wake up)", new String[][]{new String[]{"Past", "g", "um", "ising", "(woke up)"}, new String[]{"Present", "", "gum", "igising", "(waking up)"}, new String[]{"Future", "", "gi", "gising", "(will wake up)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Langoy", "(swim)", new String[][]{new String[]{"Past", "l", "um", "angoy", "(swam)"}, new String[]{"Present", "", "lum", "alangoy", "(swimming)"}, new String[]{"Future", "", "la", "langoy", "(will swim)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Lakad", "(walk)", new String[][]{new String[]{"Past", "l", "um", "akad", "(walked)"}, new String[]{"Present", "", "lum", "alakad", "(walking)"}, new String[]{"Future", "", "la", "lakad", "(will walk)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Takbo", "(run)", new String[][]{new String[]{"Past", "t", "um", "akbo", "(ran)"}, new String[]{"Present", "", "tum", "atakbo", "(running)"}, new String[]{"Future", "", "ta", "takbo", "(will run)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Sisid", "Dive", true), PhraseItem.phraseItem("Lipad", "Fly", true), PhraseItem.phraseItem("Tayo", "Stand", true), PhraseItem.phraseItem("Biyahe", "Travel", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table4(new String[]{"Root", "Past", "Present", "Future"}, new String[][]{new String[]{"Sisid", "Dive", "sumisid", "(dived)", "sumisisid", "(diving)", "sisisid", "(will dive)"}, new String[]{"Lipad", "Fly", "lumipad", "(flew)", "lumilipad", "(flying)", "lilipad", "(will fly)"}, new String[]{"Tayo", "Stand", "tumayo", "(stood up)", "tumatayo", "(standing)", "tatayo", "(will stand)"}, new String[]{"Biyahe", "Travel", "bumiyahe", "(traveled)", "bumabyahe", "(traveling)", "babiyahe", "(will travel)"}, new String[]{"Sayaw", "Dance", "sumayaw", "(danced)", "sumasayaw", "(dancing)", "sasayaw", "(will dance)"}, new String[]{"Kain", "Eat", "kumain", "(ate)", "kumakain", "(eating)", "kakain", "(will eat)"}, new String[]{"Punta", "Go", "pumunta", "(went)", "pumupunta", "(going)", "pupunta", "(will go)"}}))));
        items.add(CardItem.title("Verb Vowel Practice"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Inom", "(drink)", new String[][]{new String[]{"Past", "", "um", "inom", "(drank)"}, new String[]{"Present", "", "umi", "inom", "(drinking)"}, new String[]{"Future", "", "i", "inom", "(will drink)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Upo", "(sit)", new String[][]{new String[]{"Past", "", "um", "upo", "(sat)"}, new String[]{"Present", "", "umu", "upo", "(sitting)"}, new String[]{"Future", "", "u", "upo", "(will sit)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table3("Ulan", "(rain)", new String[][]{new String[]{"Past", "", "um", "ulan", "(rained)"}, new String[]{"Present", "", "umu", "ulan", "(raining)"}, new String[]{"Future", "", "u", "ulan", "(will rain)"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table4(new String[]{"Root", "Past", "Present", "Future"}, new String[][]{new String[]{"Inom", "Drink", "uminom", "(drank)", "umiinom", "(drinking)", "iinom", "(will drink)"}, new String[]{"Akyat", "Climb", "umakyat", "(climbed)", "umaakyat", "(climbing)", "aakyat", "(will climb)"}}))));
        items.add(CardItem.title("Past, Present, Future Sentences"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Papunta", "On the way/Headed to", true), PhraseItem.phraseItem("Bundok", "Mountain", true), PhraseItem.phraseItem("Maaga", "Early", true), PhraseItem.phraseItem("Ate", "Older sister", true), PhraseItem.phraseItem("Kuya", "Older brother", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Umakyat si Ate Cherry sa bundok.", "Ate Cherry climbed the mountain.", true), PhraseItem.phraseItem("Gigising ako nang maaga.", "I will wake up early.", true), PhraseItem.phraseItem("Umakyat si Kuya JR sa puno.", "Kuya JR climbed the tree.", true), PhraseItem.phraseItem("Papunta si Ate at Kuya sa bundok.", "Ate and Kuya are on the way to the mountain.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Bumangon ako.", "I got up.", true), PhraseItem.phraseItem("Iinom siya ng tubig.", "She/he will drink water.", true), PhraseItem.phraseItem("Tumatakbo siya sa bahay.", "She/he is running in the house.", true), PhraseItem.phraseItem("Si Marites ay lumalangoy sa pool.", "Marites is swimming in the pool.", true), PhraseItem.phraseItem("Maglalakad kami papuntang paaralan bukas.", "We will walk to school tomorrow.", true), PhraseItem.phraseItem("Uminom ng Coca-cola sina Lolo at Lola.", "Lolo and Lola drank Coca-cola.", true), PhraseItem.phraseItem("Tumakbo ako papuntang supermarket.", "I ran to the supermarket.", true))));
        items.add(CardItem.title("See if you can turn these into verbs yourself!"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Sumali", "Join", true), PhraseItem.phraseItem("Bumoto", "Vote", true), PhraseItem.phraseItem("Sumunod", "Obey/follow", true), PhraseItem.phraseItem("Umupa", "Rent", true))));
        return items;
    }
}
