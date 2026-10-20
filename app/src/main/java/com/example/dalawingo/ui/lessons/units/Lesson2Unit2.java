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

/* JADX INFO: loaded from: classes5.dex */
public final class Lesson2Unit2 {
    private Lesson2Unit2() {
    }

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();
        items.add(CardItem.titleFromUnitKey("lesson2_unit2", UnitData.getUnitTitle("lesson2_unit2")));
        items.add(CardItem.title("Demonstrative Pronouns"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ito", "This", true), PhraseItem.phraseItem("Iyan", "That (near)", true), PhraseItem.phraseItem("Iyon", "That (far)", true))));
        items.add(CardItem.image(R.drawable.lesson2_unit2image1));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ito?", "What is this?", true), PhraseItem.image(R.drawable.lesson2_unit2image2), PhraseItem.phraseItem("Ito ay puto.", "This is puto.\n(Filipino Steamed Rice Cakes)", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano iyan?", "What is that? (near)", true), PhraseItem.image(R.drawable.lesson2_unit2image3), PhraseItem.phraseItem("Iyan ay tabo.", "That is a tabo.\n(Filipino Hygiene Tool)", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano iyon?", "What is that? (far)", true), PhraseItem.image(R.drawable.lesson2_unit2image4), PhraseItem.phraseItem("Iyon ay walis.", "That is a broom.\n(Filipino Straw Broom)", true))));
        items.add(CardItem.image(R.drawable.lesson2_unit2image13));
        items.add(CardItem.title("Numbers"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table1(new String[][]{new String[]{"1", "isa", "11", "labing-isa"}, new String[]{"2", "dalawa", "12", "labing-dalawa"}, new String[]{"3", "tatlo", "13", "labing-tatlo"}, new String[]{"4", "apat", "14", "labing-apat"}, new String[]{"5", "lima", "15", "labing-lima"}, new String[]{"6", "anim", "16", "labing-anim"}, new String[]{"7", "pito", "17", "labing-pito"}, new String[]{"8", "walo", "18", "labing-walo"}, new String[]{"9", "siyam", "19", "labing-siyam"}, new String[]{"10", "sampu", "20", "dalawampu"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table2(new String[][]{new String[]{"21", "dalawampu't isa"}, new String[]{"22", "dalawampu't dalawa"}, new String[]{"23", "dalawampu't tatlo"}, new String[]{"24", "dalawampu't apat"}, new String[]{"25", "dalawampu't lima"}, new String[]{"26", "dalawampu't anim"}, new String[]{"27", "dalawampu't pito"}, new String[]{"28", "dalawampu't walo"}, new String[]{"29", "dalawampu't siyam"}, new String[]{"30", "tatlumpu"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.table2(new String[][]{new String[]{"40", "apatnapu"}, new String[]{"50", "limampu"}, new String[]{"60", "animnapu"}, new String[]{"70", "pitumpu"}, new String[]{"80", "walumpu"}, new String[]{"90", "siyamnapu"}, new String[]{"100", "isang daan"}}))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("36", "tatlumpu't anim", true), PhraseItem.phraseItem("69", "animnapu't siyam", true), PhraseItem.phraseItem("81", "walumpu't isa", true), PhraseItem.phraseItem("82", "walumpu't dalawa", true), PhraseItem.phraseItem("121", "isang daan dalawampu't isa", true))));
        items.add(CardItem.title("Using Numbers in Sentences"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle(null, "In Tagalog, when putting a number before a noun...", false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItemStyle("Number ends in a consonant →", "attach the linker (-na) to the number", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItemStyle("Number ends in a vowel →", "attach the linker (-ng) to the number", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.BOLD, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemPyramid("Apat aso", "Apat", "-na ", "aso", (String) null, "Apatna aso", (String) null, (Object) "Four dogs", (Object) PhraseItemPyramid.FontStyle.ITALIC, true), PhraseItem.phraseItemInfo(null, null, false), PhraseItem.phraseItemPyramid("Isa pusa", "Isa", "-ng ", "pusa", (String) null, "Isang pusa", (String) null, (Object) "One cat", (Object) PhraseItemPyramid.FontStyle.ITALIC, false))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ilan?", "How many?", true), PhraseItem.phraseItem("Bituin", "Star", true), PhraseItem.phraseItem("Watawat", "Flag", true), PhraseItem.phraseItem("Pilipinas", "Philippines", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ilan ang bituin sa watawat ng Pilipinas?", "How many stars are there on the Philippine flag?", true), PhraseItem.phraseItem("Mayroong tatlong bituin sa watawat ng Pilipinas.", "There are three stars on the Philippine flag.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Kutsara", "Spoon", true), PhraseItem.phraseItem("Lumpia", "Spring roll", true), PhraseItem.phraseItem("Larawan", "Picture", true), PhraseItem.phraseItem("Plato", "Plate", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ilan ang kutsara sa larawan?", "How many spoons are there in the picture?", true), PhraseItem.image(R.drawable.lesson2_unit2image7), PhraseItem.phraseItem("Mayroong labindalawang kutsara sa larawan.", "There are twelve spoons in the picture.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ilan ang lumpia sa plato?", "How many lumpia are there on the plate?", true), PhraseItem.image(R.drawable.lesson2_unit2image8), PhraseItem.phraseItem("May siyam na lumpia sa plato.", "There are nine spring rolls on the plate.", true))));
        items.add(CardItem.title("Plural Form"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle("", "To make nouns and demonstrative pronouns plural, add the word:", false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItem("mga", "", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Bilis", "Speed", true), PhraseItem.phraseItem("Mabilis", "Is / It is fast", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle("Maliit ang tricycle →", "(The tricycle is small)", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.ITALIC), PhraseItem.phraseItemStyle("Maliit ang mga tricycle", "(The tricycles are small)", true, CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, CardAdapter.Align.CENTER, CardAdapter.FontStyle.ITALIC), PhraseItem.phraseItemStyle("Mabilis ang jeepney →", "(The jeepney is fast)", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.ITALIC), PhraseItem.phraseItemStyle("Mabilis ang mga jeepney", "(The jeepneys are fast)", true, CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, CardAdapter.Align.CENTER, CardAdapter.FontStyle.ITALIC))));
        items.add(CardItem.image(R.drawable.lesson2_unit2image9));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ang mga ito?", "What are these?", true), PhraseItem.image(R.drawable.lesson2_unit2image10), PhraseItem.phraseItem("Ito ay mga puto.", "These are puto.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ang mga iyan?", "What are those?", true), PhraseItem.image(R.drawable.lesson2_unit2image11), PhraseItem.phraseItem("Iyan ay mga tabo.", "Those are tabos.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ang mga iyon?", "What are those?", true), PhraseItem.image(R.drawable.lesson2_unit2image12), PhraseItem.phraseItem("Iyon ay mga walis.", "Those are brooms.", true))));
        items.add(CardItem.title("Linking Two Nouns"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle("", "To link two nouns, use the linker:", false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR), PhraseItem.phraseItem("at", "(and)", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemPyramid("Tricycle ", "at ", "Jeepney.", "Tricycle and Jeepney."), PhraseItem.phraseItemPyramid("Mga tabo ", "at ", "mga walis tambo.", "Tabos and brooms."), PhraseItem.phraseItemPyramid("Tatlong tabo ", "at ", "anim na walis tambo.", "Three tabos and six brooms."))));
        return items;
    }
}
