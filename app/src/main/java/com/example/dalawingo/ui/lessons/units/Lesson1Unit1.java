package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit1 {
    private Lesson1Unit1() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        // Title
        String key = "lesson1_unit1";
        items.add(CardItem.titleFromUnitKey(
                key,
                UnitData.getUnitTitle(key)
        ));

        // 1) Mabuhay! Welcome!
        items.add(CardItem.title("Mabuhay! Welcome!"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("May tanong po ako.", "I have a question.", true),
                PhraseItem.phraseItem("Paki-ulit po.", "Please repeat.", true)
        )));

        // 2) Greetings
        items.add(CardItem.title("Greetings"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Kamusta!", "Hi!", true),
                PhraseItem.phraseItem("Kamusta ka?", "How are you?", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Masaya ako.", "I'm happy.", true),
                PhraseItem.phraseItem("Mabuti ako.", "I'm doing well.", true),
                PhraseItem.phraseItem("Pagod ako.", "I'm tired.", true),
                PhraseItem.phraseItem("Inaantok ako.", "I'm sleepy.", true),
                PhraseItem.phraseItem("Okay lang ako.", "I'm just okay.", true),
                PhraseItem.phraseItem("Gutom ako.", "I'm hungry.", true)
        )));

        items.add(CardItem.image(R.drawable.unit1image1));

        // 3) Identity
        items.add(CardItem.title("Identity"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pangalan", "Name", true),
                PhraseItem.phraseItem("Ano", "What", true),

                PhraseItem.phraseItem("Ang", "The\n(marks the subject)", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ano ang pangalan mo?", "What is your name?", true),
                PhraseItem.phraseItem("Ako si _____.", "I am _____.", true)
        )));

        // 4) School and Work
        items.add(CardItem.title("School and Work"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Taga saan ka?", "Where are you from?", true),
                PhraseItem.phraseItem("Taga _____ ako.", "I'm from __(place)__.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka nag-aral?", "Where did you study?", true),
                PhraseItem.phraseItem("Nag-aral ako sa _____.", "I studied at __(school)__.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka nagtatrabaho?", "Where do you work?", true),
                PhraseItem.phraseItem("Ano ang trabaho mo?", "What is your job?", true),
                PhraseItem.phraseItem("_____ po ako.", "I am a __(job title)__.", true)
        )));

        // 5) Time-Based Greetings
        items.add(CardItem.title("Time-Based Greetings"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Maganda",
                        "Beautiful",
                        "In time-based greetings, 'Maganda' is used to mean 'good'.", true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Magandang umaga!", "Good morning!", true),
                PhraseItem.phraseItem("Magandang tanghali!", "Good midday!\n(Said from 12PM–1PM)",true),
                PhraseItem.phraseItem("Magandang hapon!", "Good afternoon!", true),
                PhraseItem.phraseItem("Magandang araw!", "Good day!", true),
                PhraseItem.phraseItem("Magandang gabi!", "Good evening!", true)
        )));

        items.add(CardItem.image(R.drawable.unit1image2));
        items.add(CardItem.image(R.drawable.unit1image3));

        // 6) Respect Words
        items.add(CardItem.title("Respect Words"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        "Po",
                        "'Po' is used to show respect when speaking to someone older/in authority.",
                        true,
                        CardAdapter.Align.CENTER, CardAdapter.FontStyle.BOLD,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Oo", "Yes", true),
                PhraseItem.phraseItem("Hindi", "No", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Oo + po = Opo", "Yes (polite)", true),
                PhraseItem.phraseItem("Hindi + po = Hindi po", "No (polite)", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Salamat", "Thank you!", true),
                PhraseItem.phraseItem("Maraming salamat!", "Many thanks!", true)
        )));

        // 7) Farewells
        items.add(CardItem.title("Farewells"));
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Paalam!", "Goodbye!", true),
                PhraseItem.phraseItem("Ingat!", "Take care!", true)
        )));

        return items;
    }
}
