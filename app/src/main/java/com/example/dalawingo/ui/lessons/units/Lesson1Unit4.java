package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit4 {
    private Lesson1Unit4() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson1_unit4";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // -------------------------
        // Anong
        // -------------------------
        items.add(CardItem.title("Anong"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItemStyle(
                        null,
                        "If a word ends in a vowel and the next one begins with a vowel, they can sometimes be combined.",
                        false,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR,
                        CardAdapter.Align.LEFT, CardAdapter.FontStyle.REGULAR
                ),
                PhraseItem.phraseItem("(Ano) ang = Anong", "What is", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Anong klase mo?", "What is your class?", true),
                PhraseItem.phraseItem("Anong pagkain ito?", "What food is this?", true),
                PhraseItem.phraseItem("Anong bahay ito?", "What house is this?", true)
        )));

        // -------------------------
        // Core Question Words & Time
        // -------------------------
        items.add(CardItem.title("Planning"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan", "Where", true),
                PhraseItem.phraseItem("Pupunta", "Will go", true),
                PhraseItem.phraseItem("Mamaya", "Later", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Saan ka pupunta mamaya?", "Where will you go later?", true),
                PhraseItem.phraseItem("Pupunta ako mamaya.", "I will go later.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bukas", "Tomorrow/Open", true),
                PhraseItem.phraseItem("Ibinigay", "Gave", true),
                PhraseItem.phraseItem("Umalis", "Left", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bukas tayo pupunta.", "Tomorrow we (inclusive) will go.", true),
                PhraseItem.phraseItem("Ibinigay mo ito.", "You gave this.", true),
                PhraseItem.phraseItem("Umalis siya.", "She/he left.", true)
        )));

        // -------------------------
        // People & Purpose
        // -------------------------
        items.add(CardItem.title("Purpose and People"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Para", "For", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Nanay", "Mom", true),
                PhraseItem.phraseItem("Tatay", "Dad", true),
                PhraseItem.phraseItem("Asawa", "Wife/Husband", true),
                PhraseItem.phraseItem("Bisita", "Visitor", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Para sa nanay ito.", "This is for Mom.", true),
                PhraseItem.phraseItem("Para sa tatay ito.", "This is for Dad.", true),
                PhraseItem.phraseItem("Para sa asawa niya ito.", "This is for her/his spouse.", true),
                PhraseItem.phraseItem("Para sa bisita ito.", "This is for the visitor.", true)
        )));

        // -------------------------
        // Future Actions
        // -------------------------
        items.add(CardItem.title("Actions"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bibili", "Will buy", true),
                PhraseItem.phraseItem("Magluluto", "Will cook", true),
                PhraseItem.phraseItem("Maglilinis", "Will clean", true),
                PhraseItem.phraseItem("Kukuha", "Will get", true),
                PhraseItem.phraseItem("Gagawa", "Will make", true),
                PhraseItem.phraseItem("Magdadala", "Will bring", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bibili ako mamaya.", "I will buy later.", true),
                PhraseItem.phraseItem("Magluluto siya mamaya.", "She/he will cook later.", true),
                PhraseItem.phraseItem("Maglilinis tayo bukas.", "We (inclusive) will clean tomorrow.", true),
                PhraseItem.phraseItem("Kukuha siya mamaya.", "She/he will get it later.", true),
                PhraseItem.phraseItem("Gagawa ako bukas.", "I will make (something) tomorrow.", true),
                PhraseItem.phraseItem("Magdadala tayo mamaya.", "We (inclusive) will bring (something) later.", true)
        )));

        items.add(CardItem.title("Activities"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pagkatapos", "Afterward", true),
                PhraseItem.phraseItem("Klase", "Class", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pupunta ako pagkatapos ng klase.", "I will go after class.", true),
                PhraseItem.phraseItem("Saan ka pupunta pagkatapos ng klase?", "Where will you go after class?", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Magshoshopping", "Will go shopping", true),
                PhraseItem.phraseItem("Magbabakasyon", "Will go on vacation.", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Magshoshopping ako mamaya.", "I will go shopping later.", true),
                PhraseItem.phraseItem("Magbabakasyon tayo.", "We (inclusive) will go on vacation.", true)
        )));

        // -------------------------
        // Dates
        // -------------------------
        items.add(CardItem.title("Dates"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Araw", "Day/Sun", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Lunes", "Monday", true),
                PhraseItem.phraseItem("Martes", "Tuesday", true),
                PhraseItem.phraseItem("Miyerkules", "Wednesday", true),
                PhraseItem.phraseItem("Huwebes", "Thursday", true),
                PhraseItem.phraseItem("Biyernes", "Friday", true),
                PhraseItem.phraseItem("Sabado", "Saturday", true),
                PhraseItem.phraseItem("Linggo", "Sunday", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Buwan", "Month/Moon", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Enero", "January", true),
                PhraseItem.phraseItem("Pebrero", "February", true),
                PhraseItem.phraseItem("Marso", "March", true),
                PhraseItem.phraseItem("Abril", "April", true),
                PhraseItem.phraseItem("Mayo", "May", true),
                PhraseItem.phraseItem("Hunyo", "June", true),
                PhraseItem.phraseItem("Hulyo", "July", true),
                PhraseItem.phraseItem("Agosto", "August", true),
                PhraseItem.phraseItem("Setyembre", "September", true),
                PhraseItem.phraseItem("Oktubre", "October", true),
                PhraseItem.phraseItem("Nobyembre", "November", true),
                PhraseItem.phraseItem("Disyembre", "December", true)
        )));

        // -------------------------
        // Things and Colors
        // -------------------------
        items.add(CardItem.title("Colors"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pula", "Red", true),
                PhraseItem.phraseItem("Kahel", "Orange", true),
                PhraseItem.phraseItem("Dilaw", "Yellow", true),
                PhraseItem.phraseItem("Berde", "Green", true),
                PhraseItem.phraseItem("Bughaw / Asul", "Blue", true),
                PhraseItem.phraseItem("Lila", "Purple", true),
                PhraseItem.phraseItem("Rosas", "Pink", true),
                PhraseItem.phraseItem("Kayumanggi", "Brown", true),
                PhraseItem.phraseItem("Abo", "Gray", true),
                PhraseItem.phraseItem("Puti", "White", true),
                PhraseItem.phraseItem("Itim", "Black", true)
        )));

        items.add(CardItem.title("Things and Colors"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Bulaklak", "Flower", true),
                PhraseItem.phraseItem("Hardin", "Garden", true),
                PhraseItem.phraseItem("Mesa", "Table", true),
                PhraseItem.phraseItem("Regalo", "Gift", true),
                PhraseItem.phraseItem("Mga Dokumento", "Documents", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pula ang bulaklak.", "The flower is red.", true),
                PhraseItem.phraseItem("Berde ang hardin.", "The garden is green.", true),
                PhraseItem.phraseItem("Mga dokumento ito.", "These are documents.", true)
        )));

        return items;
    }
}
