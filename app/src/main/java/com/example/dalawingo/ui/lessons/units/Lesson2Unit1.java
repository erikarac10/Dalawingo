package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes5.dex */
public final class Lesson2Unit1 {
    private Lesson2Unit1() {
    }

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();
        items.add(CardItem.titleFromUnitKey("lesson2_unit1", UnitData.getUnitTitle("lesson2_unit1")));
        items.add(CardItem.title("May/Mayroon/Wala"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("May", "Have / There is", true), PhraseItem.phraseItem("Mayroon", "Have / There is", true), PhraseItem.phraseItem("Wala", "None / Nothing", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("May _____ sa video.", "There is a _____ in the video.", true), PhraseItem.phraseItem("Mayroon _____ sa video.", "There is a _____ in the video.", true), PhraseItem.phraseItemStyle("", "*Both sentences mean the same but are structured differently.", false, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.CENTER, CardAdapter.FontStyle.REGULAR))));
        items.add(CardItem.image(R.drawable.lesson2_unit1image1));
        items.add(CardItem.title("May/Mayroon Sentence Structure"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("May __(object)__ ako.", "I have an __(object)__.", true), PhraseItem.phraseItem("Mayroon akong __(object)__.", "I have an __(object)__.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Marami akong __(object)__.", "I have many __(object)__.", true), PhraseItem.phraseItem("Wala akong __(object)__.", "I have no __(object)__.", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ang mayroon ka sa bahay mo?", "What do you have in your house?", true), PhraseItem.phraseItem("May _____ ako sa bahay ko.", "I have a _____ in my house.", true), PhraseItem.phraseItem("Mayroon akong _____ sa bahay ko.", "I have a _____ in my house.", true))));
        items.add(CardItem.title("Changing Sentences into Questions"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemInfo("May - ", "Use 'Ba' as the second word.", false), PhraseItem.phraseItemInfo("Mayroon - ", "Use 'Bang' as the second word.", false))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Libro", "Book", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemStyle("May libro si Liza.  →", "May libro ba si Liza?", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"), PhraseItem.phraseItemStyle("Mayroong libro si Liza.  →", "Mayroon bang libro si Liza?", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"), PhraseItem.phraseItemStyle("Maraming libro si Liza.  →", "Marami bang libro si Liza?", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"), PhraseItem.phraseItemStyle("Walang libro si Liza.  →", "Wala bang libro si Liza?", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, "17sp", CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD, "19sp"))));
        items.add(CardItem.image(R.drawable.lesson2_unit1image2));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Buhok", "Hair", true), PhraseItem.phraseItem("Kalbo", "Bald", true), PhraseItem.phraseItem("Iskul", "Shortened 'School'", true), PhraseItem.phraseItem("Bagyo", "Typhoon", true), PhraseItem.phraseItem("Puno", "Tree / Full", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("May buhok si tito Joel?", "Does tito Joel have hair?", true), PhraseItem.phraseItem("May iskul ba si CJ?", "Does CJ have school?", true), PhraseItem.phraseItem("Wala bang puno?", "Is there no tree?", true), PhraseItem.phraseItem("Mayroon bang bagyo sa Pinas?", "Is there a typhoon in the Philippines?", true))));
        return items;
    }
}
