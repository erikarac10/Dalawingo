package com.example.dalawingo.ui.lessons.units;

import com.example.dalawingo.R;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.PhraseItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Lesson1Unit3 {
    private Lesson1Unit3() {}

    public static List<CardItem> build() {
        List<CardItem> items = new ArrayList<>();

        final String key = "lesson1_unit3";

        // Title (new TITLE view type + auto pastel cycle)
        items.add(CardItem.titleFromUnitKey(key, UnitData.getUnitTitle(key)));

        // ----------------------------
        // Possessive Pronouns
        // ----------------------------
        items.add(CardItem.title("Possessive Pronouns"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ko", "My", true),
                PhraseItem.phraseItem("Mo", "Your", true),
                PhraseItem.phraseItem("Niya", "Her/His", true),
                PhraseItem.phraseItem("Namin", "Our (exclusive)", true),
                PhraseItem.phraseItem("Natin", "Our (inclusive)", true),
                PhraseItem.phraseItem("Ninyo", "Your (plural)", true),
                PhraseItem.phraseItem("Nila", "Their (plural)", true)
        )));

        items.add(CardItem.image(R.drawable.unit3image1));

        // ----------------------------
        // Relationships
        // ----------------------------
        items.add(CardItem.title("Relationships"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Kapatid", "Sibling", true),
                PhraseItem.phraseItem("Kapitbahay", "Neighbor", true),
                PhraseItem.phraseItem("Kaibigan", "Friend", true),
                PhraseItem.phraseItem("Katrabaho", "Co-worker", true),
                PhraseItem.phraseItem("Kaklase", "Classmate", true),

                // Extra explanation line preserved (merged into English)
                PhraseItem.phraseItem(
                        "Kababayan",
                        "Fellow country person\n(A warm way Filipinos address fellow Filipinos, especially abroad)",
                        true
                ),

                PhraseItem.phraseItem("Kasambahay", "Maid", true)
        )));

        items.add(CardItem.image(R.drawable.unit3image2));

        // ----------------------------
        // Vocab (white phrase card)
        // ----------------------------
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Sino", "Who", true),
                PhraseItem.phraseItem("Ito", "This", true),
                PhraseItem.phraseItem("Gawin", "Do/Make", true),
                PhraseItem.phraseItem("Isama", "Include/Bring", true)
        )));

        // Sentences
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Sino ang _____ mo?", "Who is your _____?", true),
                PhraseItem.phraseItem("Si __(name)__ ito.", "This is _____.", true),
                PhraseItem.phraseItem("Gawin mo ito.", "Do/make this.", true),
                PhraseItem.phraseItem("Isama ang kapatid mo.", "Bring your sister/brother (sibling) with you.", true),
                PhraseItem.phraseItem("Kapatid/Katrabaho/Kaibigan ko si _____.", "My sibling/co-worker/friend is _____.", true)
        )));

        // ----------------------------
        // Objects
        // ----------------------------
        items.add(CardItem.title("Objects"));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Pitaka", "Wallet", true),
                PhraseItem.phraseItem("Sapatos", "Shoes", true),
                PhraseItem.phraseItem("Pagkain", "Food", true),
                PhraseItem.phraseItem("Basura", "Trash", true),
                PhraseItem.phraseItem("Pagdiriwang", "Party", true),
                PhraseItem.phraseItem("Keyk", "Cake", true),
                PhraseItem.phraseItem("Telepono", "Phone", true),
                PhraseItem.phraseItem("Kuwarto", "Room", true),
                PhraseItem.phraseItem("Pera", "Money", true),
                PhraseItem.phraseItem("Bahay", "House", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Ito ang pitaka ko.", "This is my wallet.", true),
                PhraseItem.phraseItem("Ito ang pagkain nila.", "This is their food.", true),
                PhraseItem.phraseItem("Ito ang bahay namin.", "This is our house. (exclusive)", true),
                PhraseItem.phraseItem("Ito ang kuwarto niya.", "This is her/his room.", true),
                PhraseItem.phraseItem("Bag niya ito.", "This is her/his bag.", true)
        )));

        // ----------------------------
        // Adjectives
        // ----------------------------
        items.add(CardItem.title("Adjectives"));

        // Ma- explanation card (content preserved)
        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem(
                        "Ma-",
                        "It is",
                        "Insert in front of a noun to turn it into an adjective.",
                        true
                )
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Laki", "Size", true),
                PhraseItem.phraseItem("Liit", "Small", true),
                PhraseItem.phraseItem("Lamig", "Cold", true),
                PhraseItem.phraseItem("Init", "Hot", true)
        )));

        items.add(CardItem.phraseGroup(Arrays.asList(
                PhraseItem.phraseItem("Malaki", "It's big\n('Ma-' changes meaning into big)", true),
                PhraseItem.phraseItem("Maliit", "It's small", true),
                PhraseItem.phraseItem("Malamig", "It's cold", true),
                PhraseItem.phraseItem("Mainit", "It's hot", true)
        )));

        // ----------------------------
        // Introduce yourself!
        // ----------------------------
        items.add(CardItem.title("Introduce yourself!"));

        items.add(CardItem.image(R.drawable.unit3image3));
        items.add(CardItem.image(R.drawable.unit3image4));

        return items;
    }
}
