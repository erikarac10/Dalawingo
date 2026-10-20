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
public final class Lesson2Unit3 {
    private Lesson2Unit3() {
    }

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();
        items.add(CardItem.titleFromUnitKey("lesson2_unit3", UnitData.getUnitTitle("lesson2_unit3")));
        items.add(CardItem.title("Pseudo Verbs"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Gusto", "Want or Like", true), PhraseItem.phraseItem("Ayaw", "Do not like / Do not want", true))));
        items.add(CardItem.title("Sentences using 'Gusto' and 'Ayaw'"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("O", "Or", true))));
        items.add(CardItem.image(R.drawable.lesson2_unit3image1));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Tubig", "Water", true), PhraseItem.phraseItem("Tsokolate", "Chocolate", true), PhraseItem.phraseItem("Sili", "Red pepper", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Gusto ko ng adobo.", "I want/like adobo.", true), PhraseItem.phraseItem("Gusto ko ng tubig.", "I want/like water.", true), PhraseItem.phraseItem("Gusto ko ng tsokolate.", "I want/like chocolate.", true), PhraseItem.phraseItem("Ayaw ko ng sili.", "I don't want/like red peppers.", true), PhraseItem.phraseItem("Ayaw ko ng balut.", "I don't want/like balut.", true), PhraseItem.phraseItem("Ayaw ko ng salty food.", "I don't want/like salty food.", true))));
        items.add(CardItem.title("Pronouns with 'Gusto' and 'Ayaw'"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Libangan", "Hobby", true), PhraseItem.phraseItem("Kahit", "Even if / although / though / whatever", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ano ang gusto mong libangan?", "What hobby do you like/want?", true), PhraseItem.phraseItem("Kahit pagod siya, gusto niya ang libangan.", "Even though she/he is tired, she/he likes hobbies.", true))));
        items.add(CardItem.image(R.drawable.lesson2_unit3image2));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Gusto kong _____.", "I like/want ____.", true))));
        items.add(CardItem.title("Ni"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemInfo("Ni", "- is a marker placed before a name.", true), PhraseItem.phraseItemStyle(null, "It is used to show ownership or who did something.", false, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR, CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItemPyramid("Aso ", "ni ", "Maria.", "Maria's dog."), PhraseItem.phraseItemPyramid("Tsokolate ", "ni ", "Chrisanto.", "Chrisanto's Chocolate."))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Tinapay", "Bread", true), PhraseItem.phraseItem("Ngayon", "Now", true))));
        items.add(CardItem.image(R.drawable.lesson2_unit3image3));
        items.add(CardItem.title("Using 'Ni' in Sentences"));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Gulay", "Vegetables", true), PhraseItem.phraseItem("Digmaan", "War", true), PhraseItem.phraseItem("Sumakay", "Ride", true), PhraseItem.phraseItem("Alagang hayop", "Pet", true), PhraseItem.phraseItem("Anak", "Child (daughter/son)", true))));
        items.add(CardItem.phraseGroup(Arrays.asList(PhraseItem.phraseItem("Ayaw ni Junjun ng gulay.", "Junjun does not like vegetables.", true), PhraseItem.phraseItem("Ayaw namin ng digmaan.", "We do not like war.", true), PhraseItem.phraseItem("Sumakay ang aso ni Maria sa jeep.", "Maria's dog rode the jeep.", true), PhraseItem.phraseItem("May alagang hayop si Maria.", "Maria has a pet.", true), PhraseItem.phraseItem("Mahal ni Maria ang anak niya.", "Maria loves her/his child.", true))));
        return items;
    }
}
