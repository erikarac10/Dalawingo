package com.example.dalawingo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitData {

    public static final Map<String, Unit> units = new HashMap<>();

    static {
        // -------------------
        // UNIT 1: Greetings & Introductions
        // -------------------
        List<Phrase> lesson1unit1Phrases = new ArrayList<>();
        lesson1unit1Phrases.add(new Phrase("Mabuhay!", "Welcome!"));
        lesson1unit1Phrases.add(new Phrase("May tanong po ako.", "I have a question."));
        lesson1unit1Phrases.add(new Phrase("Paki-ulit po.", "Please repeat."));
        lesson1unit1Phrases.add(new Phrase("Kamusta!", "Hi!"));
        lesson1unit1Phrases.add(new Phrase("Kamusta ka?", "How are you?"));
        lesson1unit1Phrases.add(new Phrase("Masaya ako.", "I'm happy."));
        lesson1unit1Phrases.add(new Phrase("Mabuti ako.", "I'm good."));
        lesson1unit1Phrases.add(new Phrase("Pagod ako.", "I'm exhausted."));
        lesson1unit1Phrases.add(new Phrase("Inaantok ako.", "I'm sleepy."));
        lesson1unit1Phrases.add(new Phrase("Okay lang ako.", "I'm just okay."));
        lesson1unit1Phrases.add(new Phrase("Gutom ako.", "I'm hungry."));
        lesson1unit1Phrases.add(new Phrase("Pangalan", "Name"));
        lesson1unit1Phrases.add(new Phrase("Ano", "What"));
        lesson1unit1Phrases.add(new Phrase("Ang", "'the' (marks the subject)"));
        lesson1unit1Phrases.add(new Phrase("Ano ang pangalan mo?", "What is your name?"));
        lesson1unit1Phrases.add(new Phrase("Ako si _____.", "I am _____."));
        lesson1unit1Phrases.add(new Phrase("Taga saan ka?", "Where are you from?"));
        lesson1unit1Phrases.add(new Phrase("Taga _____ ako.", "I'm from __(place)__."));
        lesson1unit1Phrases.add(new Phrase("Saan ka nag-aral?", "Where did you study?"));
        lesson1unit1Phrases.add(new Phrase("Nag-aral ako sa _____.", "I studied at __(school)__."));
        lesson1unit1Phrases.add(new Phrase("Saan ka nagtatrabaho?", "Where do you work?"));
        lesson1unit1Phrases.add(new Phrase("Ano ang trabaho mo?", "What is your job?"));
        lesson1unit1Phrases.add(new Phrase("__(job title)___ po ako.", "I am a __(job title)__."));
        lesson1unit1Phrases.add(new Phrase("Maganda", "Beautiful"));
        lesson1unit1Phrases.add(new Phrase("Magandang umaga!", "Good morning!"));
        lesson1unit1Phrases.add(new Phrase("Magandang tanghali!", "Good midday! (12–1 PM)"));
        lesson1unit1Phrases.add(new Phrase("Magandang hapon!", "Good afternoon!"));
        lesson1unit1Phrases.add(new Phrase("Magandang araw!", "Good day!"));
        lesson1unit1Phrases.add(new Phrase("Magandang gabi!", "Good evening!"));
        lesson1unit1Phrases.add(new Phrase("Po", "Used to show respect"));
        lesson1unit1Phrases.add(new Phrase("Oo", "Yes"));
        lesson1unit1Phrases.add(new Phrase("Hindi", "No"));
        lesson1unit1Phrases.add(new Phrase("Opo", "Yes (polite)"));
        lesson1unit1Phrases.add(new Phrase("Hindi po", "No (polite)"));
        lesson1unit1Phrases.add(new Phrase("Salamat!", "Thank you!"));
        lesson1unit1Phrases.add(new Phrase("Maraming Salamat!", "Many thanks!"));
        lesson1unit1Phrases.add(new Phrase("Paalam!", "Goodbye!"));
        lesson1unit1Phrases.add(new Phrase("Ingat!", "Take care!"));

        List<UnitQuestion> lesson1unit1Questions = new ArrayList<>();
        lesson1unit1Questions.add(new UnitQuestion(
                "You feel very sleepy. Kumusta ka?",
                "Inaantok ako."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Ano ang trabaho mo?",
                "_____ po ako."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "You are on your way to work and saw a friend. How will you greet them?",
                "Kamusta!"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "How do you ask the name of a person?",
                "Ano ang pangalan mo?"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "You made a new friend and you want to know where they're from. What do you ask?",
                "Taga saan ka?"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "You've had a long day at work. What do you say if someone asks “Kumusta ka?”",
                "Pagod ako."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "It's 7pm and you haven't eaten since noon. Kumusta ka?",
                "Gutom ako."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Ano ang pangalan mo?",
                "Ako si _____. / _____ ang pangalan ko."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "You probably hear this at every family gathering. What do they ask when they want to know what you do for a living?",
                "Ano ang trabaho mo?"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Saan ka nagtatrabaho?",
                "Nagtatrabaho ako sa _____."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Your work phone rings at 12pm. How do you greet the caller?",
                "Magandang tanghali!"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "You're about to join a video call with family members living in different countries. How would you greet them?",
                "Kamusta!"
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Your uncle said something to you, but you're not quite sure what he said. What do you say?",
                "Paki-ulit po."
        ));
        lesson1unit1Questions.add(new UnitQuestion(
                "Your grandma gives you money for your birthday. What do you say to thank her?",
                "Salamat po. / Maraming salamat po."
        ));

        units.put("lesson1_unit1", new Unit(
                "Greetings and Introductions",
                "Answer these questions in Tagalog, then say them aloud:",
                lesson1unit1Phrases,
                lesson1unit1Questions
        ));

        // -------------------
        // UNIT 2: Pronouns & Jobs
        // -------------------
        List<Phrase> lesson1unit2Phrases = new ArrayList<>();
        // pronouns
        lesson1unit2Phrases.add(new Phrase("Ako", "I"));
        lesson1unit2Phrases.add(new Phrase("Ka", "You"));
        lesson1unit2Phrases.add(new Phrase("Siya", "She/He"));
        lesson1unit2Phrases.add(new Phrase("Kami", "We (exclusive)"));
        lesson1unit2Phrases.add(new Phrase("Tayo", "We (inclusive)"));
        lesson1unit2Phrases.add(new Phrase("Kayo", "You (plural)"));
        lesson1unit2Phrases.add(new Phrase("Sila", "They (plural)"));
        // jobs
        lesson1unit2Phrases.add(new Phrase("Guro/Titser", "Teacher"));
        lesson1unit2Phrases.add(new Phrase("Estudyante", "Student"));
        lesson1unit2Phrases.add(new Phrase("Doktor", "Doctor"));
        lesson1unit2Phrases.add(new Phrase("Nars", "Nurse"));
        lesson1unit2Phrases.add(new Phrase("Pulis", "Police"));
        lesson1unit2Phrases.add(new Phrase("Abugado", "Lawyer"));
        lesson1unit2Phrases.add(new Phrase("Negosyante", "Businessperson"));
        lesson1unit2Phrases.add(new Phrase("Kawani", "Employee"));
        lesson1unit2Phrases.add(new Phrase("Magsasaka", "Farmer"));
        lesson1unit2Phrases.add(new Phrase("Manunulat", "Writer"));
        lesson1unit2Phrases.add(new Phrase("Pintor", "Painter"));
        lesson1unit2Phrases.add(new Phrase("Karpintero", "Carpenter"));
        lesson1unit2Phrases.add(new Phrase("Mananayaw", "Dancer"));

        // conjugations: Estudyante
        lesson1unit2Phrases.add(new Phrase("Estudyante ako.", "I am a student."));
        lesson1unit2Phrases.add(new Phrase("Estudyante ka.", "You are a student."));
        lesson1unit2Phrases.add(new Phrase("Estudyante siya.", "She/He is a student."));
        lesson1unit2Phrases.add(new Phrase("Estudyante kami.", "We are students (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Estudyante tayo.", "We are students (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Estudyante kayo.", "You are students."));
        lesson1unit2Phrases.add(new Phrase("Estudyante sila.", "They are students."));

        // other jobs
        lesson1unit2Phrases.add(new Phrase("Inhinyero", "Engineer"));
        lesson1unit2Phrases.add(new Phrase("Tagapaggupit", "Haircutter"));
        lesson1unit2Phrases.add(new Phrase("Mamamayan", "Citizen"));
        lesson1unit2Phrases.add(new Phrase("Botante", "Voter"));

        // conjugations: Guro
        lesson1unit2Phrases.add(new Phrase("Guro ako.", "I am a teacher."));
        lesson1unit2Phrases.add(new Phrase("Guro ka.", "You are a teacher."));
        lesson1unit2Phrases.add(new Phrase("Guro siya.", "She/He is a teacher."));
        lesson1unit2Phrases.add(new Phrase("Guro kami.", "We are teachers (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Guro tayo.", "We are teachers (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Guro kayo.", "You are teachers."));
        lesson1unit2Phrases.add(new Phrase("Guro sila.", "They are teachers."));
        // conjugations: Doktor
        lesson1unit2Phrases.add(new Phrase("Doktor ako.", "I am a doctor."));
        lesson1unit2Phrases.add(new Phrase("Doktor ka.", "You are a doctor."));
        lesson1unit2Phrases.add(new Phrase("Doktor siya.", "She/He is a doctor."));
        lesson1unit2Phrases.add(new Phrase("Doktor kami.", "We are doctors (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Doktor tayo.", "We are doctors (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Doktor kayo.", "You are doctors."));
        lesson1unit2Phrases.add(new Phrase("Doktor sila.", "They are doctors."));
        // conjugations: Nars
        lesson1unit2Phrases.add(new Phrase("Nars ako.", "I am a nurse."));
        lesson1unit2Phrases.add(new Phrase("Nars ka.", "You are a nurse."));
        lesson1unit2Phrases.add(new Phrase("Nars siya.", "She/He is a nurse."));
        lesson1unit2Phrases.add(new Phrase("Nars kami.", "We are nurses (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Nars tayo.", "We are nurses (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Nars kayo.", "You are nurses."));
        lesson1unit2Phrases.add(new Phrase("Nars sila.", "They are nurses."));
        // conjugations: Pulis
        lesson1unit2Phrases.add(new Phrase("Pulis ako.", "I am a police officer."));
        lesson1unit2Phrases.add(new Phrase("Pulis ka.", "You are a police officer."));
        lesson1unit2Phrases.add(new Phrase("Pulis siya.", "She/He is a police officer."));
        lesson1unit2Phrases.add(new Phrase("Pulis kami.", "We are police officers (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Pulis tayo.", "We are police officers (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Pulis kayo.", "You are police officers."));
        lesson1unit2Phrases.add(new Phrase("Pulis sila.", "They are police officers."));
        // conjugations: Abugado
        lesson1unit2Phrases.add(new Phrase("Abugado ako.", "I am a lawyer."));
        lesson1unit2Phrases.add(new Phrase("Abugado ka.", "You are a lawyer."));
        lesson1unit2Phrases.add(new Phrase("Abugado siya.", "She/He is a lawyer."));
        lesson1unit2Phrases.add(new Phrase("Abugado kami.", "We are lawyers (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Abugado tayo.", "We are lawyers (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Abugado kayo.", "You are lawyers."));
        lesson1unit2Phrases.add(new Phrase("Abugado sila.", "They are lawyers."));
        // conjugations: Negosyante
        lesson1unit2Phrases.add(new Phrase("Negosyante ako.", "I am a businessperson."));
        lesson1unit2Phrases.add(new Phrase("Negosyante ka.", "You are a businessperson."));
        lesson1unit2Phrases.add(new Phrase("Negosyante siya.", "She/He is a businessperson."));
        lesson1unit2Phrases.add(new Phrase("Negosyante kami.", "We are businesspeople (exclusive)."));
        lesson1unit2Phrases.add(new Phrase("Negosyante tayo.", "We are businesspeople (inclusive)."));
        lesson1unit2Phrases.add(new Phrase("Negosyante kayo.", "You are businesspeople."));
        lesson1unit2Phrases.add(new Phrase("Negosyante sila.", "They are businesspeople."));

        //ikaw/ka examples
        lesson1unit2Phrases.add(new Phrase("Ikaw si Dalisay / Si Dalisay ka", "You are Dalisay."));
        lesson1unit2Phrases.add(new Phrase("Ikaw si Mariel / Si Mariel ka.", "You are Mariel."));
        lesson1unit2Phrases.add(new Phrase("Ikaw si Angelo / Si Angelo ka.", "You are Angelo."));

        //si examples
        lesson1unit2Phrases.add(new Phrase("Guro si Allan", "Allan is a teacher."));
        lesson1unit2Phrases.add(new Phrase("Magsasaka si Avery", "Avery is a farmer."));
        lesson1unit2Phrases.add(new Phrase("Pintor si Ligaya", "Ligaya is a painter."));
        lesson1unit2Phrases.add(new Phrase("Manunulat si Joshua", "Joshua is a writer."));

        List<UnitQuestion> lesson1unit2Questions = new ArrayList<>();
        lesson1unit2Questions.add(new UnitQuestion(
                "They are farmers.",
                "Magsasaka sila."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "I am a carpenter.",
                "Karpintero ako."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "Thomas is an engineer.",
                "Inhinyero si Thomas."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "Maria is a writer.",
                "Manunulat si Maria."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "You are a hairdresser.",
                "Tagapaggupit ka."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "We (exclusive) are students.",
                "Estudyante kami."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "We (inclusive) are citizens.",
                "Mamamayan tayo."
        ));
        lesson1unit2Questions.add(new UnitQuestion(
                "You are voters.",
                "Botante kayo."
        ));

        units.put("lesson1_unit2", new Unit(
                "Pronouns and Jobs",
                "Translate these sentences to Tagalog, then say them aloud:",
                lesson1unit2Phrases,
                lesson1unit2Questions
        ));

        // -------------------
        // UNIT 3: Possessive Pronouns
        // -------------------
        List<Phrase> lesson1unit3Phrases = new ArrayList<>();
        lesson1unit3Phrases.add(new Phrase("Ko", "My"));
        lesson1unit3Phrases.add(new Phrase("Mo", "Your"));
        lesson1unit3Phrases.add(new Phrase("Niya", "Her/His"));
        lesson1unit3Phrases.add(new Phrase("Namin", "Our (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Natin", "Our (inclusive)"));
        lesson1unit3Phrases.add(new Phrase("Ninyo", "Your (plural)"));
        lesson1unit3Phrases.add(new Phrase("Nila", "Their (plural)"));

        //relationships
        lesson1unit3Phrases.add(new Phrase("Kapatid", "Sibling"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay", "Neighbor"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan", "Friend"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho", "Co-worker"));
        lesson1unit3Phrases.add(new Phrase("Kaklase", "Classmate"));
        lesson1unit3Phrases.add(new Phrase("Kababayan", "Fellow countryperson"));
        lesson1unit3Phrases.add(new Phrase("Kasambahay", "Maid"));

        // sibling
        lesson1unit3Phrases.add(new Phrase("Kapatid ko", "My sibling"));
        lesson1unit3Phrases.add(new Phrase("Kapatid mo", "Your sibling"));
        lesson1unit3Phrases.add(new Phrase("Kapatid niya", "Her/His sibling"));
        lesson1unit3Phrases.add(new Phrase("Kapatid namin", "Our sibling (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kapatid natin", "Our sibling (inclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kapatid ninyo", "Your sibling (plural)"));
        lesson1unit3Phrases.add(new Phrase("Kapatid nila", "Their sibling"));
        // neighbor
        lesson1unit3Phrases.add(new Phrase("Kapitbahay ko", "My neighbor"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay mo", "Your neighbor"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay niya", "Her/His neighbor"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay namin", "Our neighbor (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay natin", "Our neighbor (inclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay ninyo", "Your neighbor (plural)"));
        lesson1unit3Phrases.add(new Phrase("Kapitbahay nila", "Their neighbor"));
        // friend
        lesson1unit3Phrases.add(new Phrase("Kaibigan ko", "My friend"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan mo", "Your friend"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan niya", "Her/His friend"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan namin", "Our friend (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan natin", "Our friend (inclusive)"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan ninyo", "Your friend (plural)"));
        lesson1unit3Phrases.add(new Phrase("Kaibigan nila", "Their friend"));
        // coworker
        lesson1unit3Phrases.add(new Phrase("Katrabaho ko", "My co-worker"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho mo", "Your co-worker"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho niya", "Her/His co-worker"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho namin", "Our co-worker (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho natin", "Our co-worker (inclusive)"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho ninyo", "Your co-worker (plural)"));
        lesson1unit3Phrases.add(new Phrase("Katrabaho nila", "Their co-worker"));

        //extras
        lesson1unit3Phrases.add(new Phrase("Sino", "Who"));
        lesson1unit3Phrases.add(new Phrase("Ito", "This"));
        lesson1unit3Phrases.add(new Phrase("Gawin", "Do/Make"));
        lesson1unit3Phrases.add(new Phrase("Isama", "Include/Bring"));

        //sino
        lesson1unit3Phrases.add(new Phrase("Sino ang Kaklase mo?", "Who is your Classmate?"));
        lesson1unit3Phrases.add(new Phrase("Kaklase ko si _____.", "My classmate is _____."));
        lesson1unit3Phrases.add(new Phrase("Isama ang kapatid mo.", "Bring your sister/brother."));

        // activities
        lesson1unit3Phrases.add(new Phrase("______ ko ito.", "This is my ______."));
        lesson1unit3Phrases.add(new Phrase("______ mo ito.", "This is your ______."));
        lesson1unit3Phrases.add(new Phrase("______ niya ito.", "This is her/his ______."));
        lesson1unit3Phrases.add(new Phrase("______ namin ito.", "This is our (exclusive) ______."));
        lesson1unit3Phrases.add(new Phrase("______ natin ito.", "This is our (inclusive) ______."));
        lesson1unit3Phrases.add(new Phrase("______ ninyo ito.", "This is your (plural) ______."));
        lesson1unit3Phrases.add(new Phrase("______ nila ito.", "This is their ______."));

        // objects
        lesson1unit3Phrases.add(new Phrase("Pitaka", "Wallet"));
        lesson1unit3Phrases.add(new Phrase("Sapatos", "Shoes"));
        lesson1unit3Phrases.add(new Phrase("Pagkain", "Food"));
        lesson1unit3Phrases.add(new Phrase("Basura", "Trash"));
        lesson1unit3Phrases.add(new Phrase("Pagdiriwang", "Party"));
        lesson1unit3Phrases.add(new Phrase("Keyk", "Cake"));
        lesson1unit3Phrases.add(new Phrase("Telepono", "Phone"));
        lesson1unit3Phrases.add(new Phrase("Kwarto", "Room"));
        lesson1unit3Phrases.add(new Phrase("Pera", "Money"));
        lesson1unit3Phrases.add(new Phrase("Bahay", "House"));

        lesson1unit3Phrases.add(new Phrase("Pitaka mo ito.", "This is your wallet."));
        lesson1unit3Phrases.add(new Phrase("Sapatos niya ito.", "These are his shoes."));
        lesson1unit3Phrases.add(new Phrase("Pagkain natin ito.", "This is our food (inclusive)."));
        lesson1unit3Phrases.add(new Phrase("Basura nila ito.", "This is their trash (plural)."));
        lesson1unit3Phrases.add(new Phrase("Pagdiriwang ninyo ito.", "This is your party."));
        lesson1unit3Phrases.add(new Phrase("Keyk namin ito.", "This is our cake (exclusive)."));
        lesson1unit3Phrases.add(new Phrase("Telepono ko ito.", "This is my phone."));

        lesson1unit3Phrases.add(new Phrase("Kwarto namin ito.", "This is our room. (exclusive)"));
        lesson1unit3Phrases.add(new Phrase("Pera niya ito.", "This is her/his money."));
        lesson1unit3Phrases.add(new Phrase("Bahay nila ito.", "This is their house."));

        lesson1unit3Phrases.add(new Phrase("Bag niya ito.", "This is her bag."));

        //adjectives
        lesson1unit3Phrases.add(new Phrase("Ma-", "Insert in front of a noun to turn it into an adjective."));
        lesson1unit3Phrases.add(new Phrase("Laki", "Size"));
        lesson1unit3Phrases.add(new Phrase("Liit", "Small"));
        lesson1unit3Phrases.add(new Phrase("Lamig", "Cold"));
        lesson1unit3Phrases.add(new Phrase("Init", "Hot"));
        lesson1unit3Phrases.add(new Phrase("Malaki", "It's big"));
        lesson1unit3Phrases.add(new Phrase("Maliit", "It's small"));
        lesson1unit3Phrases.add(new Phrase("Malamig", "It's cold"));
        lesson1unit3Phrases.add(new Phrase("Mainit", "It's hot"));

        List<UnitQuestion> lesson1unit3Questions = new ArrayList<>();
        lesson1unit3Questions.add(new UnitQuestion(
                "Kapatid natin si Sally.",
                "Sally is our sibling. (inclusive)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Kaklase ka namin.",
                "You are our classmate. (exclusive)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Isama ninyo si Jerry. Kaibigan ninyo siya.",
                "Include Jerry. He is your friend. (plural) / Bring Jerry. He is your friend. (plural)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Malaki ang bahay.",
                "The house is big."
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Kababayan nila tayo.",
                "We are their fellow countrymen."
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Kwarto namin ito. (exclusive)",
                "This is our room. (exclusive)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Keyk natin ito.",
                "This is our cake. (inclusive)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "Katrabaho ninyo si Monique",
                "Monique is your co-worker (plural)"
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "I am _____. I work at _____. I am a _____.",
                "Ako si _____. Nag-tatrabaho ako sa _____. _____ ako."
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "She is _____. She studies at _____. She is a _____.",
                "Siya si _____. Nag-aaral siya sa _____. _____ siya."
        ));
        lesson1unit3Questions.add(new UnitQuestion(
                "This is _____. He works at _____. He is a _____.",
                "Ito si _____. Nagtatrabaho siya sa _____. _____ siya."
        ));

        units.put("lesson1_unit3", new Unit(
                "Possessive Pronouns",
                "Translate these sentences to English, then say each question aloud:",
                lesson1unit3Phrases,
                lesson1unit3Questions
        ));

        // -------------------
        // UNIT 4:
        // -------------------
        List<Phrase> lesson1unit4Phrases = new ArrayList<>();

        // -------------------------
        // ANONG (CONTRACTION NOTE)
        // -------------------------
        lesson1unit4Phrases.add(new Phrase(
                "Anong",
                "What is"
        ));
        lesson1unit4Phrases.add(new Phrase("Anong klase mo?","What is your class?"));
        lesson1unit4Phrases.add(new Phrase("Anong pagkain ito?","What food is this?"));
        lesson1unit4Phrases.add(new Phrase("Anong bahay ito?","What house is this?"));

        // -------------------------
        // WHERE + GOING + TIME/Planning
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Saan", "Where"));
        lesson1unit4Phrases.add(new Phrase("Pupunta", "Will go"));
        lesson1unit4Phrases.add(new Phrase("Mamaya", "Later"));

        lesson1unit4Phrases.add(new Phrase("Saan ka pupunta mamaya?","Where will you go later?"));
        lesson1unit4Phrases.add(new Phrase("Pupunta ako mamaya.", "I will go later."));

        // -------------------------
        // PAST WORDS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Bukas", "Tomorrow / Open"));
        lesson1unit4Phrases.add(new Phrase("Ibinigay", "Gave"));
        lesson1unit4Phrases.add(new Phrase("Umalis", "Left"));

        lesson1unit4Phrases.add(new Phrase("Bukas tayo pupunta.", "Tomorrow we (inclusive) will go."));
        lesson1unit4Phrases.add(new Phrase("Ibinigay mo ito.", "You gave this."));
        lesson1unit4Phrases.add(new Phrase("Umalis siya.", "She/he left."));

        // -------------------------
        // PEOPLE + PURPOSE
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Para", "For"));
        lesson1unit4Phrases.add(new Phrase("Nanay", "Mom"));
        lesson1unit4Phrases.add(new Phrase("Tatay", "Dad"));
        lesson1unit4Phrases.add(new Phrase("Asawa", "Wife / Husband"));
        lesson1unit4Phrases.add(new Phrase("Bisita", "Visitor"));

        lesson1unit4Phrases.add(new Phrase("Para sa Nanay ito.","This is for Mom."));
        lesson1unit4Phrases.add(new Phrase("Para sa Tatay ito.","This is for Dad."));
        lesson1unit4Phrases.add(new Phrase("Para sa asawa niya ito.","This is for her/his spouse."));
        lesson1unit4Phrases.add(new Phrase("Para sa bisita ito.", "This is for the visitor."));

        // -------------------------
        // FUTURE ACTIONS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Bibili", "Will buy"));
        lesson1unit4Phrases.add(new Phrase("Magluluto", "Will cook"));
        lesson1unit4Phrases.add(new Phrase("Maglilinis", "Will clean"));
        lesson1unit4Phrases.add(new Phrase("Kukuha", "Will get"));
        lesson1unit4Phrases.add(new Phrase("Gagawa", "Will make"));
        lesson1unit4Phrases.add(new Phrase("Magdadala", "Will bring"));

        lesson1unit4Phrases.add(new Phrase("Bibili ako mamaya.","I will buy later."));
        lesson1unit4Phrases.add(new Phrase("Magluluto siya mamaya.","She/he will cook later."));
        lesson1unit4Phrases.add(new Phrase("Maglilinis tayo bukas.","We (inclusive) will clean tomorrow."));
        lesson1unit4Phrases.add(new Phrase("Kukuha siya mamaya.","She/he will get it later."));
        lesson1unit4Phrases.add(new Phrase("Gagawa ako bukas.","I will do it tomorrow."));
        lesson1unit4Phrases.add(new Phrase("Magdadala tayo mamaya.","We (inclusive) will bring (something) later."));

        lesson1unit4Phrases.add(new Phrase("Magluluto ako mamaya.", "I will cook later."));
        lesson1unit4Phrases.add(new Phrase("Maglilinis siya bukas.", "She/he will clean tomorrow."));
        lesson1unit4Phrases.add(new Phrase("Bibili tayo.", "We (inclusive) will buy."));

        // -------------------------
        // AFTER CLASS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Pagkatapos", "Afterward"));
        lesson1unit4Phrases.add(new Phrase("Klase", "Class"));

        lesson1unit4Phrases.add(new Phrase("Pupunta ako pagkatapos ng klase.", "I will go after class."));
        lesson1unit4Phrases.add(new Phrase("Saan ka pupunta pagkatapos ng klase?", "Where will you go after class?"));

        // -------------------------
        // ACTIVITIES
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Magshoshopping", "Will go shopping"));
        lesson1unit4Phrases.add(new Phrase("Magbabakasyon", "Will go on vacation"));

        lesson1unit4Phrases.add(new Phrase("Magshoshopping ako mamaya.", "I will go shopping later."));
        lesson1unit4Phrases.add(new Phrase("Magbabakasyon tayo.", "We (inclusive) will go on vacation."));

        // -------------------------
        // DATES — DAYS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Araw", "Day / Sun"));

        lesson1unit4Phrases.add(new Phrase("Lunes", "Monday"));
        lesson1unit4Phrases.add(new Phrase("Martes", "Tuesday"));
        lesson1unit4Phrases.add(new Phrase("Miyerkules", "Wednesday"));
        lesson1unit4Phrases.add(new Phrase("Huwebes", "Thursday"));
        lesson1unit4Phrases.add(new Phrase("Biyernes", "Friday"));
        lesson1unit4Phrases.add(new Phrase("Sabado", "Saturday"));
        lesson1unit4Phrases.add(new Phrase("Linggo", "Sunday"));

        // -------------------------
        // DATES — MONTHS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Buwan", "Month / Moon"));
        lesson1unit4Phrases.add(new Phrase("Enero", "January"));
        lesson1unit4Phrases.add(new Phrase("Pebrero", "February"));
        lesson1unit4Phrases.add(new Phrase("Marso", "March"));
        lesson1unit4Phrases.add(new Phrase("Abril", "April"));
        lesson1unit4Phrases.add(new Phrase("Mayo", "May"));
        lesson1unit4Phrases.add(new Phrase("Hunyo", "June"));
        lesson1unit4Phrases.add(new Phrase("Hulyo", "July"));
        lesson1unit4Phrases.add(new Phrase("Agosto", "August"));
        lesson1unit4Phrases.add(new Phrase("Setyembre", "September"));
        lesson1unit4Phrases.add(new Phrase("Oktubre", "October"));
        lesson1unit4Phrases.add(new Phrase("Nobyembre", "November"));
        lesson1unit4Phrases.add(new Phrase("Disyembre", "December"));

        // -------------------------
        // COLORS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Pula", "Red"));
        lesson1unit4Phrases.add(new Phrase("Kahel", "Orange"));
        lesson1unit4Phrases.add(new Phrase("Dilaw", "Yellow"));
        lesson1unit4Phrases.add(new Phrase("Berde", "Green"));
        lesson1unit4Phrases.add(new Phrase("Bughaw / Asul", "Blue"));
        lesson1unit4Phrases.add(new Phrase("Lila", "Purple"));
        lesson1unit4Phrases.add(new Phrase("Rosas", "Pink"));
        lesson1unit4Phrases.add(new Phrase("Kayumanggi", "Brown"));
        lesson1unit4Phrases.add(new Phrase("Abo", "Gray"));
        lesson1unit4Phrases.add(new Phrase("Puti", "White"));
        lesson1unit4Phrases.add(new Phrase("Itim", "Black"));

        // -------------------------
        // THINGS AND COLORS
        // -------------------------
        lesson1unit4Phrases.add(new Phrase("Bulaklak", "Flower"));
        lesson1unit4Phrases.add(new Phrase("Hardin", "Garden"));
        lesson1unit4Phrases.add(new Phrase("Mesa", "Table"));
        lesson1unit4Phrases.add(new Phrase("Regalo", "Gift"));
        lesson1unit4Phrases.add(new Phrase("Mga Dokumento", "Documents"));

        lesson1unit4Phrases.add(new Phrase("Pula ang bulaklak.", "The flower is red."));
        lesson1unit4Phrases.add(new Phrase("Berde ang hardin.", "The garden is green."));
        lesson1unit4Phrases.add(new Phrase("Mga dokumento ito.", "These are documents."));

        List<UnitQuestion> lesson1unit4Questions = new ArrayList<>();

        lesson1unit4Questions.add(new UnitQuestion(
                "I will go later after class.",
                "Pupunta ako mamaya pagkatapos ng klase."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Ibinigay mo ito para sa Nanay.",
                "You gave this for Mom."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Magluluto tayo bukas para sa Tatay.",
                "We (inclusive) will cook tomorrow for Dad."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Saan ka pupunta sa Lunes?",
                "Where will you go on Monday?"
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "She/he left on Friday.",
                "Umalis siya sa Biyernes."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Pitaka niya ito. Telepono ko ito.",
                "This is her/his wallet. This is my phone."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Magdadala ka ng mga dokumento mamaya.",
                "You will bring the documents later."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Bibili kami ng keyk para sa bisita.",
                "We (exclusive) will buy a cake for the visitor."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "After class, they (plural) will go shopping.",
                "Pagkatapos ng klase, magshoshopping sila."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Berde ang bag sa mesa.",
                "The bag on the table is green."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Our (exclusive) house is blue in January.",
                "Asul ang bahay namin sa Enero."
        ));

        lesson1unit4Questions.add(new UnitQuestion(
                "Magbabakasyon tayo sa Disyembre.",
                "We (inclusive) will go on vacation in December."
        ));

         units.put("lesson1_unit4", new Unit(
                 "Anong, Planning, Dates, Actions, Things and Colors.",
                 "Translate these sentences to Tagalog or English, then say each Tagalog phrase aloud:",
                 lesson1unit4Phrases,
                 lesson1unit4Questions
         ));

        // -------------------
        // UNIT 5: Sa, Kay, and Kina
        // -------------------
        List<Phrase> lesson1unit5Phrases = new ArrayList<>();

        // Personal Pronouns (possessive forms)
        lesson1unit5Phrases.add(new Phrase("Akin", "Mine"));
        lesson1unit5Phrases.add(new Phrase("Iyo", "Yours"));
        lesson1unit5Phrases.add(new Phrase("Kanya", "Her/His"));
        lesson1unit5Phrases.add(new Phrase("Amin", "Ours (exclusive)"));
        lesson1unit5Phrases.add(new Phrase("Atin", "Ours (inclusive)"));
        lesson1unit5Phrases.add(new Phrase("Inyo", "Yours (plural)"));
        lesson1unit5Phrases.add(new Phrase("Kanila", "Theirs / Them"));

        lesson1unit5Phrases.add(new Phrase("Akin ito.", "This is mine."));
        lesson1unit5Phrases.add(new Phrase("Iyo ang sapatos.", "The shoes are yours."));
        lesson1unit5Phrases.add(new Phrase("Kanya ang pitaka.", "The wallet is hers/his."));
        lesson1unit5Phrases.add(new Phrase("Amin ang kwarto.", "The room is ours (exclusive)."));
        lesson1unit5Phrases.add(new Phrase("Atin ang pagkain.", "The food is ours (inclusive)."));
        lesson1unit5Phrases.add(new Phrase("Inyo ang keyk.", "The cake is yours (plural)."));
        lesson1unit5Phrases.add(new Phrase("Kanila ang bahay.", "The house is theirs."));

        lesson1unit5Phrases.add(new Phrase("Ay", "Is"));

        lesson1unit5Phrases.add(new Phrase("Ang pitaka ay kanya.", "The wallet is hers/his."));
        lesson1unit5Phrases.add(new Phrase("Ang kwarto ay amin.", "The room is ours (exclusive)."));
        lesson1unit5Phrases.add(new Phrase("Ang araw ay Lunes.", "The day is Monday."));
        lesson1unit5Phrases.add(new Phrase("Ang bulaklak ay pula.", "The flower is red."));

        // Sa
        lesson1unit5Phrases.add(new Phrase("Sa", "To, At, In, For, On"));

        lesson1unit5Phrases.add(new Phrase("Saan ka nag-aaral?", "Where do you study?"));
        lesson1unit5Phrases.add(new Phrase("Nag-aaral ako sa _____.", "I study at _____."));
        lesson1unit5Phrases.add(new Phrase("Pupunta ako sa _____.", "I will go to _____."));
        lesson1unit5Phrases.add(new Phrase("Ibinigay niya ang pagkain sa kaibigan niya.", "She/he gave the food to her/his friend."));
        lesson1unit5Phrases.add(new Phrase("Umalis siya sa bahay.", "She/he left (from) the house."));
        lesson1unit5Phrases.add(new Phrase("Bibili ako ng pagkain para sa nanay ko.", "I will buy food for my mom."));
        lesson1unit5Phrases.add(new Phrase("Magluluto ako para sa asawa ko.", "I will cook for my spouse (wife/husband)."));
        lesson1unit5Phrases.add(new Phrase("Maglilinis ako ng bahay para sa bisita ko.", "I will clean the house for my visitor."));
        lesson1unit5Phrases.add(new Phrase("Bibili akong adobo para sa Nanay ko.", "I will buy adobo for my mom."));
        lesson1unit5Phrases.add(new Phrase("Magtatrabaho ako sa Lunes.", "I will work on Monday."));
        lesson1unit5Phrases.add(new Phrase("Magcecelebrate kami ng birthday sa Abril 8.", "We will celebrate a birthday on April 8."));
        lesson1unit5Phrases.add(new Phrase("Pupunta ako sa Texas sa Disyembre.", "I will go to Texas in December."));
        lesson1unit5Phrases.add(new Phrase("Magbabakasyon ako sa __(date)__.", "I will go on vacation in __(date)__."));
        lesson1unit5Phrases.add(new Phrase("Saan ka pupunta pagkatapos ng klase?", "Where will you go after class?"));
        lesson1unit5Phrases.add(new Phrase("Pupunta ako sa _____ pagkatapos ng klase.", "I will go _____ after class."));

        lesson1unit5Phrases.add(new Phrase("Sakin ang pera.", "The money is mine."));
        lesson1unit5Phrases.add(new Phrase("Sayo ang keyk.", "The cake is yours."));
        lesson1unit5Phrases.add(new Phrase("Sa kanya ang bahay.", "The house is hers/his."));
        lesson1unit5Phrases.add(new Phrase("Samin ang kwarto.", "The room is ours (exclusive)."));
        lesson1unit5Phrases.add(new Phrase("Satin ang pagkain.", "The food is ours (inclusive)."));
        lesson1unit5Phrases.add(new Phrase("Sanyo ang mga dokumento.", "The documents are yours (plural)."));
        lesson1unit5Phrases.add(new Phrase("Sa kanila ang bulaklak.", "The flowers are theirs."));

        // Kay and Kina
        lesson1unit5Phrases.add(new Phrase("Kay", "Used instead of 'Sa' for one person's name"));
        lesson1unit5Phrases.add(new Phrase("Kina", "Used instead of 'Sa' for more than one person's name"));
        lesson1unit5Phrases.add(new Phrase("At", "And"));

        lesson1unit5Phrases.add(new Phrase("Bag kay Avery", "Avery's Bag"));
        lesson1unit5Phrases.add(new Phrase("Bag kina Avery at Ezio", "Avery and Ezio's bag."));

        lesson1unit5Phrases.add(new Phrase("Bibili ako ng regalo para kay _____.", "I will buy a gift for _____."));
        lesson1unit5Phrases.add(new Phrase("Magluluto ako ng adobo para kay _____.", "I will cook adobo for _____."));
        lesson1unit5Phrases.add(new Phrase("Magshoshopping ako para kina _____ at _____.", "I will go shopping for _____ and _____."));

        lesson1unit5Phrases.add(new Phrase("Bibili ako ng pagkain para kay Lisa.", "I will buy food for Lisa."));
        lesson1unit5Phrases.add(new Phrase("Magluluto ako para kina Lisa at Aaron.", "I will cook for Lisa and Aaron."));

        lesson1unit5Phrases.add(new Phrase("Magdala", "Bring"));
        lesson1unit5Phrases.add(new Phrase("Kotse", "Car"));

        List<UnitQuestion> lesson1unit5Questions = new ArrayList<>();

        lesson1unit5Questions.add(new UnitQuestion(
                "(You) Bring some flowers in the garden for me.",
                "Magdala ka ng bulaklak sa hardin para sa akin. / Magdala ka ng bulaklak sa hardin para sakin."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Ibinigay ko ang regalo kay Nanay.",
                "I gave the gift to Mom."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "We (inclusive) will cook food tomorrow for Dad.",
                "Magluluto tayo ng pagkain bukas para kay Tatay."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "The gift on the table is yours.",
                "Ang regalo sa mesa ay iyo."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Their (plural) house is blue.",
                "Asul ang bahay nila."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Kanya ang pitaka akin ang telepono.",
                "The wallet is her/his, the phone is mine."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Bring the documents to Avery.",
                "Magdadala ka ng mga dokumento kay Avery."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Sa inyo itong kotse?",
                "This car yours. (plural) / This is your car. (plural)"
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Berde ang bag kay Avery sa mesa.",
                "Avery's bag on the table is green."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Yes, this is our car. (exclusive)",
                "Oo, kotse namin ito."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "You gave the documents to her.",
                "Ibinigay mo ang mga dokumento sa kanya."
        ));
        lesson1unit5Questions.add(new UnitQuestion(
                "Para sa atin ang regalo at ang mga bag.",
                "The gift and the bags are for us. (inclusive)"
        ));

        units.put("lesson1_unit5", new Unit(
                "Personal Pronouns, Ay, Sa, Kay and Kina.",
                "Translate these sentences to Tagalog or English, then say each Tagalog phrase aloud:",
                lesson1unit5Phrases,
                lesson1unit5Questions
        ));

        // -------------------
        // UNIT 6: Questions and Answers
        // -------------------
        List<Phrase> lesson1unit6Phrases = new ArrayList<>();

        // Particles
        lesson1unit6Phrases.add(new Phrase("Ba", "?"));

        // Vocabulary from examples
        lesson1unit6Phrases.add(new Phrase("Ni", "Of (used to show ownership)"));
        lesson1unit6Phrases.add(new Phrase("Keyk ba ito ni Jordan?", "Is this Jordan's cake?"));
        lesson1unit6Phrases.add(new Phrase("Estudyante ba kayo?", "Are you (plural) students?"));
        lesson1unit6Phrases.add(new Phrase("Magtatrabaho ba sila sa Lunes?", "Will they work on Monday?"));
        lesson1unit6Phrases.add(new Phrase("Katrabaho ba natin si Elena?", "Is Elena our (inclusive) co-worker?"));
        lesson1unit6Phrases.add(new Phrase("Wallet ba ito ni Adam?", "Is this Adam's wallet?"));
        lesson1unit6Phrases.add(new Phrase("Bibili ba tayo ng pagkain?", "Will we (inclusive) buy food?"));

        lesson1unit6Phrases.add(new Phrase("Mahaba", "Long"));
        lesson1unit6Phrases.add(new Phrase("Damit", "Clothes"));
        lesson1unit6Phrases.add(new Phrase("Mahal", "Expensive"));

        //na
        lesson1unit6Phrases.add(new Phrase("Na", "Used as a linker for adjectives"));
        //lesson1unit6Phrases.add(new Phrase("Na", "Now/Already"));
        lesson1unit6Phrases.add(new Phrase("Asul na bag", "Blue bag"));
        lesson1unit6Phrases.add(new Phrase("Maganda na bulaklak", "Beautiful flowers"));
        lesson1unit6Phrases.add(new Phrase("Berde na sapatos", "Green shoes"));
        lesson1unit6Phrases.add(new Phrase("Malaki na bahay", "Big house"));
        lesson1unit6Phrases.add(new Phrase("Maliit na bag", "Small bag"));
        lesson1unit6Phrases.add(new Phrase("Mahaba na damit", "Long clothes"));
        lesson1unit6Phrases.add(new Phrase("Mahal na pagkain", "Expensive food"));

        // Numbers 1-10
        lesson1unit6Phrases.add(new Phrase("Mga Numero", "Numbers"));
        lesson1unit6Phrases.add(new Phrase("Isa", "One"));
        lesson1unit6Phrases.add(new Phrase("Dalawa", "Two"));
        lesson1unit6Phrases.add(new Phrase("Tatlo", "Three"));
        lesson1unit6Phrases.add(new Phrase("Apat", "Four"));
        lesson1unit6Phrases.add(new Phrase("Lima", "Five"));
        lesson1unit6Phrases.add(new Phrase("Anim", "Six"));
        lesson1unit6Phrases.add(new Phrase("Pito", "Seven"));
        lesson1unit6Phrases.add(new Phrase("Walo", "Eight"));
        lesson1unit6Phrases.add(new Phrase("Siyam", "Nine"));
        lesson1unit6Phrases.add(new Phrase("Sampu", "Ten"));

        //na/-ng
        lesson1unit6Phrases.add(new Phrase("Masayang asawa", "Happy spouse (wife/husband)"));
        lesson1unit6Phrases.add(new Phrase("Magandang bahay", "Beautiful house"));

        lesson1unit6Phrases.add(new Phrase("Bata", "Child"));
        lesson1unit6Phrases.add(new Phrase("Bago", "New"));
        lesson1unit6Phrases.add(new Phrase("Luma", "Old (object)"));
        lesson1unit6Phrases.add(new Phrase("Mataba", "Is fat"));
        lesson1unit6Phrases.add(new Phrase("Aso", "Dog"));
        lesson1unit6Phrases.add(new Phrase("Pusa", "Cat"));

        lesson1unit6Phrases.add(new Phrase("Batang masaya", "Happy baby"));
        lesson1unit6Phrases.add(new Phrase("Bagong bag", "New bag"));
        lesson1unit6Phrases.add(new Phrase("Lumang sapatos", "Old shoes"));
        lesson1unit6Phrases.add(new Phrase("Matabang pusa", "Fat cat"));
        lesson1unit6Phrases.add(new Phrase("Isang aso", "One dog"));

        // NEW WORDS
        lesson1unit6Phrases.add(new Phrase("Eskwelahan/Paaralan", "School"));
        lesson1unit6Phrases.add(new Phrase("Papasok", "Will enter"));
        lesson1unit6Phrases.add(new Phrase("Relo", "Watch"));
        lesson1unit6Phrases.add(new Phrase("Laruan", "Toy"));
        lesson1unit6Phrases.add(new Phrase("Mangga", "Mango"));
        lesson1unit6Phrases.add(new Phrase("Masarap", "Is delicious"));
        lesson1unit6Phrases.add(new Phrase("Maraming", "Is many / A lot"));
        lesson1unit6Phrases.add(new Phrase("Kaunting", "Few / A little"));
        lesson1unit6Phrases.add(new Phrase("Katabi", "Beside / Next to"));

        lesson1unit6Phrases.add(new Phrase("Malaking paaralan ito.", "This is a big school."));
        lesson1unit6Phrases.add(new Phrase("Papasok ang batang masaya.", "The happy child will enter."));
        lesson1unit6Phrases.add(new Phrase("Bagong relo ito.", "This is a new watch."));
        lesson1unit6Phrases.add(new Phrase("Maraming laruan ang bata.", "The child has many toys."));
        lesson1unit6Phrases.add(new Phrase("Bagong mangga ito.", "This is a fresh mango."));
        lesson1unit6Phrases.add(new Phrase("Masarap ang bagong pagkain.", "The new food is delicious."));
        lesson1unit6Phrases.add(new Phrase("Maraming batang masaya.", "There are many happy children."));
        lesson1unit6Phrases.add(new Phrase("Kaunting pagkain ang dala niya.", "She/he brought a small amount of food."));
        lesson1unit6Phrases.add(new Phrase("Katabing bahay ang amin.", "Our (exclusive) house is besides."));

        List<UnitQuestion> lesson1unit6Questions = new ArrayList<>();
        lesson1unit6Questions.add(new UnitQuestion(
                "Papasok ka sa eskwelahan.",
                "Papasok ka ba sa eskwelahan?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Maganda ka.",
                "Maganda ka ba?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Masarap na ang pagkain.",
                "Masarap na ba ang pagkain?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Masarap ang mangga na ito.",
                "Masarap ba ang mangga na ito?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Mahal ang relo niya.",
                "Mahal ba ang relo niya?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Maraming laruan si Josh.",
                "Marami bang laruan si Josh?"
        ));
        lesson1unit6Questions.add(new UnitQuestion(
                "Katabi niya ang nanay niya.",
                "Katabi ba niya ang nanay niya?"
        ));

        units.put("lesson1_unit6", new Unit(
                "Ba, Ni, Na, Numbers, -ng, and More Everyday Words",
                "Translate these sentences into questions by putting 'ba' in the correct place, say each statement and question aloud:",
                lesson1unit6Phrases,
                lesson1unit6Questions
        ));

        // -------------------
        // LESSON 2 - UNIT 1: Existentials: May, Mayroon, Marami, Wala
        // -------------------
        List<Phrase> lesson2unit1Phrases = new ArrayList<>();
        lesson2unit1Phrases.add(new Phrase("May", "Have / There is"));
        lesson2unit1Phrases.add(new Phrase("Mayroon", "Have / There is (alternative of 'May')"));
        lesson2unit1Phrases.add(new Phrase("Wala", "None / Nothing"));

        lesson2unit1Phrases.add(new Phrase("May carabao sa video.", "There is a carabao in the video. (May)"));
        lesson2unit1Phrases.add(new Phrase("Mayroon pagkain sa video.", "There is food in the video. (Mayroon)"));

        lesson2unit1Phrases.add(new Phrase("May laruan ako. / Mayroon ako laruan.", "I have a toy."));
        lesson2unit1Phrases.add(new Phrase("May pusa ako. / Mayroon ako pusa.", "I have a cat."));
        lesson2unit1Phrases.add(new Phrase("May relo ako. / Mayroon ako relo.", "I have a watch."));

        lesson2unit1Phrases.add(new Phrase("Marami akong aso.", "I have many/lots of dogs."));
        lesson2unit1Phrases.add(new Phrase("Wala akong pusa.", "I don't have a cat."));

        lesson2unit1Phrases.add(new Phrase("Ano ang mayroon ka sa bahay mo?", "What do you have in your house?"));
        lesson2unit1Phrases.add(new Phrase("May aso ako sa bahay ko.", "I have a dog in my house. (May)"));
        lesson2unit1Phrases.add(new Phrase("Mayroon akong bata sa bahay ko.", "I have a kid in my house. (Mayroon)"));
        lesson2unit1Phrases.add(new Phrase("Wala ang pusa sa bahay ko. / Walang pusa sa bahay ko.", "There is no cat in my house."));
        lesson2unit1Phrases.add(new Phrase("Marami ang damit sa bahay ko. / Maraming damit sa bahay ko.", "There are lots of clothes in my house."));

        lesson2unit1Phrases.add(new Phrase("Libro", "Book"));

        lesson2unit1Phrases.add(new Phrase("May libro ba si Liza?", "Does Liza have a book?"));
        lesson2unit1Phrases.add(new Phrase("Mayroon bang libro si Liza?", "Does Liza have a book? (Mayroon)"));
        lesson2unit1Phrases.add(new Phrase("Marami bang libro si Liza?", "Does Liza have lots of books?"));
        lesson2unit1Phrases.add(new Phrase("Wala bang libro si Liza?", "Liza does not have a book?"));

        lesson2unit1Phrases.add(new Phrase("Buhok", "Hair"));
        lesson2unit1Phrases.add(new Phrase("Kalbo", "Bald"));
        lesson2unit1Phrases.add(new Phrase("Iskul", "Shortened 'School'"));
        lesson2unit1Phrases.add(new Phrase("Bagyo", "Typhoon"));
        lesson2unit1Phrases.add(new Phrase("Puno", "Tree/Full"));

        lesson2unit1Phrases.add(new Phrase("May buhok si Tito Joel?", "Does Tito Joel have hair?"));
        lesson2unit1Phrases.add(new Phrase("May iskul ba si CJ?", "Does CJ have school?"));
        lesson2unit1Phrases.add(new Phrase("Wala bang puno?", "Is there no tree?"));
        lesson2unit1Phrases.add(new Phrase("Mayroon bang bagyo sa Pinas?", "Is there a typhoon in the Philippines?"));


        List<UnitQuestion> lesson2unit1Questions = new ArrayList<>();

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ buhok ang kalbo. (The bald person has no hair.)",
                "Walang buhok ang kalbo."
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ bagyo sa Pinas. (There is a typhoon in the Philippines.)",
                "May bagyo sa Pinas. / Mayroong bagyo sa Pinas."
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ libro si Liza. (Liza has many books.)",
                "Maraming libro si Liza."
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ libro si Liza. (Liza has no books.)",
                "Walang libro si Liza."
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ bang libro si Liza? (Does Liza have a book?)",
                "May libro ba si Liza? / Mayroon bang libro si Liza?"
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ bang puno? (Is there no tree?)",
                "Wala bang puno?"
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ pusa ako. (I have a cat.)",
                "May pusa ako. / Mayroon akong pusa."
        ));

        lesson2unit1Questions.add(new UnitQuestion(
                "_____ akong relo. (I have a watch.)",
                "Mayroon akong relo."
        ));

        units.put("lesson2_unit1", new Unit(
                "Existentials: May, Mayroon, Marami, Wala",
                "Fill in the blanks with the appropriate existential words (May, Mayroon, Marami, Wala), then say the question and your answer aloud:",
                lesson2unit1Phrases,
                lesson2unit1Questions
        ));

        // -------------------
        // LESSON 2 - UNIT 2: Numbers & Demonstrative Pronouns
        // -------------------
        List<Phrase> lesson2unit2Phrases = new ArrayList<>();

        // Demonstrative pronouns
        lesson2unit2Phrases.add(new Phrase("Ito", "This"));
        lesson2unit2Phrases.add(new Phrase("Iyan", "That (near)"));
        lesson2unit2Phrases.add(new Phrase("Iyon", "That (far)"));

        // Sample sentences
        lesson2unit2Phrases.add(new Phrase("Ano ito?", "What is this?"));
        lesson2unit2Phrases.add(new Phrase("Ito ay puto.", "This is puto."));
        lesson2unit2Phrases.add(new Phrase("Ano iyan?", "What is that? (near)"));
        lesson2unit2Phrases.add(new Phrase("Iyan ay tabo.", "That is a tabo. (near)"));
        lesson2unit2Phrases.add(new Phrase("Ano iyon?", "What is that? (far)"));
        lesson2unit2Phrases.add(new Phrase("Iyon ay walis.", "That is a broom. (far)"));

        // Numbers 1–20
        lesson2unit2Phrases.add(new Phrase("Labing-isa", "Eleven"));
        lesson2unit2Phrases.add(new Phrase("Labing-dalawa", "Twelve"));
        lesson2unit2Phrases.add(new Phrase("Labing-tatlo", "Thirteen"));
        lesson2unit2Phrases.add(new Phrase("Labing-apat", "Fourteen"));
        lesson2unit2Phrases.add(new Phrase("Labing-lima", "Fifteen"));
        lesson2unit2Phrases.add(new Phrase("Labing-anim", "Sixteen"));
        lesson2unit2Phrases.add(new Phrase("Labing-pito", "Seventeen"));
        lesson2unit2Phrases.add(new Phrase("Labing-walo", "Eighteen"));
        lesson2unit2Phrases.add(new Phrase("Labing-siyam", "Nineteen"));
        lesson2unit2Phrases.add(new Phrase("Dalawampu", "Twenty"));

        lesson2unit2Phrases.add(new Phrase("Dalawampu't isa", "Twenty-one"));
        lesson2unit2Phrases.add(new Phrase("Tatlumpu", "Thirty"));
        lesson2unit2Phrases.add(new Phrase("Apatnapu", "Forty"));
        lesson2unit2Phrases.add(new Phrase("Limampu", "Fifty"));
        lesson2unit2Phrases.add(new Phrase("Animnapu", "Sixty"));
        lesson2unit2Phrases.add(new Phrase("Pitumpu", "Seventy"));
        lesson2unit2Phrases.add(new Phrase("Walongpu", "Eighty"));
        lesson2unit2Phrases.add(new Phrase("Siyamnapu", "Ninety"));
        lesson2unit2Phrases.add(new Phrase("Isang daan / Sandaang", "One hundred"));

        lesson2unit2Phrases.add(new Phrase("Tatlongpu't dalawa", "Thirty-two"));
        lesson2unit2Phrases.add(new Phrase("Apatnapu't tatlo", "Forty-three"));
        lesson2unit2Phrases.add(new Phrase("Limampu't apat", "Fifty-four"));
        lesson2unit2Phrases.add(new Phrase("Animnapu't lima", "Sixty-five"));
        lesson2unit2Phrases.add(new Phrase("Pitumpu't anim", "Seventy-six"));
        lesson2unit2Phrases.add(new Phrase("Walongpu't pito", "Eighty-seven"));
        lesson2unit2Phrases.add(new Phrase("Siyamnapu't walo", "Ninety-eight"));
        lesson2unit2Phrases.add(new Phrase("Isang daan at limampu", "One hundred fifty"));

        // ----------- Basic words -----------
        lesson2unit2Phrases.add(new Phrase("Ilan", "How many?"));
        lesson2unit2Phrases.add(new Phrase("Bituin", "Star"));
        lesson2unit2Phrases.add(new Phrase("Watawat", "Flag"));
        lesson2unit2Phrases.add(new Phrase("Pilipinas", "Philippines"));

        // ----------- Questions & Answers -----------
        lesson2unit2Phrases.add(new Phrase("Ilan ang bituin sa watawat ng Pilipinas?", "How many stars are on the flag of the Philippines?"));
        lesson2unit2Phrases.add(new Phrase("Mayroong tatlong bituin sa watawat ng Pilipinas.", "There are three stars on the flag of the Philippines."));

        // ----------- Objects -----------
        lesson2unit2Phrases.add(new Phrase("Kutsara", "Spoon"));
        lesson2unit2Phrases.add(new Phrase("Lumpia", "Spring roll"));
        lesson2unit2Phrases.add(new Phrase("Larawan", "Picture"));
        lesson2unit2Phrases.add(new Phrase("Plato", "Plate"));

        // ----------- Counting examples -----------
        lesson2unit2Phrases.add(new Phrase("Ilan ang kutsara sa larawan?", "How many spoons are in the picture?"));
        lesson2unit2Phrases.add(new Phrase("Mayroong labindalawang kutsara sa larawan.", "There are twelve spoons in the picture."));
        lesson2unit2Phrases.add(new Phrase("Ilan ang lumpia sa plato?", "How many spring rolls are on the plate?"));
        lesson2unit2Phrases.add(new Phrase("May siyam na lumpia sa plato.", "There are nine spring rolls on the plate."));

        // ----------- Descriptions -----------
        lesson2unit2Phrases.add(new Phrase("Bilis", "Speed"));
        lesson2unit2Phrases.add(new Phrase("Mabilis", "Fast"));
        lesson2unit2Phrases.add(new Phrase("Maliit ang mga tricycle.", "The tricycles are small."));
        lesson2unit2Phrases.add(new Phrase("Mabilis ang mga jeepney.", "The jeepneys are fast."));

        // ----------- Demonstratives (this/that) -----------
        lesson2unit2Phrases.add(new Phrase("Ito ay mga damit.", "These are clothes."));
        lesson2unit2Phrases.add(new Phrase("Iyan ay mga mangga.", "Those are mangoes. (near)"));
        lesson2unit2Phrases.add(new Phrase("Iyon ay mga kotse.", "Those are cars. (far)"));

        // ----------- Questions with demonstratives -----------
        lesson2unit2Phrases.add(new Phrase("Ano ang mga ito?", "What are these?"));
        lesson2unit2Phrases.add(new Phrase("Ito ay mga puto.", "These are puto."));
        lesson2unit2Phrases.add(new Phrase("Ano ang mga iyan?", "What are those? (near)"));
        lesson2unit2Phrases.add(new Phrase("Iyan ay mga tabo.", "Those are tabos. (near)"));
        lesson2unit2Phrases.add(new Phrase("Ano ang mga iyon?", "What are those? (far)"));
        lesson2unit2Phrases.add(new Phrase("Iyon ay mga walis.", "Those are brooms. (far)"));

        // ----------- Mixed objects -----------
        lesson2unit2Phrases.add(new Phrase("At", "And"));
        lesson2unit2Phrases.add(new Phrase("Tricycle at Jeepney", "Tricycle and Jeepney"));
        lesson2unit2Phrases.add(new Phrase("Mga tabo at mga walis", "Tabos and brooms"));
        lesson2unit2Phrases.add(new Phrase("Tatlong tabo at anim na walis", "Three tabos and six brooms"));

        // Practice Qs
        List<UnitQuestion> lesson2unit2Questions = new ArrayList<>();
        lesson2unit2Questions.add(new UnitQuestion(
                "One",
                "Isa"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Nineteen",
                "Labing-siyam"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Thirteen",
                "Labing-tatlo"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Eight",
                "Walo"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Twelve",
                "Labing-dalawa"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Sixteen",
                "Labing-anim"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Fourteen",
                "Labing-apat"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Eleven",
                "Labing-isa"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Ten",
                "Sampu"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Three",
                "Tatlo"
        ));
        lesson2unit2Questions.add(new UnitQuestion(
                "Five",
                "Lima"
        ));

        units.put("lesson2_unit2", new Unit(
                "Demonstrative Pronouns, Numbers, Plurals, Linkers 'mga' and 'at'",
                "Translate the Tagalog number equivalents, then say them aloud:",
                lesson2unit2Phrases,
                lesson2unit2Questions
        ));

        // -------------------
        // LESSON 2 - UNIT 3: Pseudo-verbs: Gusto & Ayaw
        // -------------------
        List<Phrase> lesson2unit3Phrases = new ArrayList<>();

        // Core pseudo-verbs
        lesson2unit3Phrases.add(new Phrase("Gusto", "Want/Like"));
        lesson2unit3Phrases.add(new Phrase("Ayaw", "Do not like/Do not want"));
        lesson2unit3Phrases.add(new Phrase("O", "Or"));

        // ----------- Drinks & Food -----------
        lesson2unit3Phrases.add(new Phrase("Tubig", "Water"));
        lesson2unit3Phrases.add(new Phrase("Tsokolate", "Chocolate"));
        lesson2unit3Phrases.add(new Phrase("Sili", "Chili pepper"));

        lesson2unit3Phrases.add(new Phrase("Gusto ko ng adobo.", "I want adobo."));
        lesson2unit3Phrases.add(new Phrase("Gusto ko ng tubig.", "I want water."));
        lesson2unit3Phrases.add(new Phrase("Gusto ko ng tsokolate.", "I want chocolate."));
        lesson2unit3Phrases.add(new Phrase("Ayaw ko ng sili.", "I don't want chili pepper."));
        lesson2unit3Phrases.add(new Phrase("Ayaw ko ng balut.", "I don't want balut."));
        lesson2unit3Phrases.add(new Phrase("Ayaw ko ng salty food.", "I don't want salty food."));

        // ----------- Hobbies / Leisure -----------
        lesson2unit3Phrases.add(new Phrase("Libangan", "Hobby"));
        lesson2unit3Phrases.add(new Phrase("Kahit", "Even if/although/though/whatever"));
        lesson2unit3Phrases.add(new Phrase("Ano ang gusto mong libangan?", "What hobby do you like?"));
        lesson2unit3Phrases.add(new Phrase("Gusto kong _____", "I want to _____"));
        lesson2unit3Phrases.add(new Phrase("Kahit pagod ako, gusto ko ang _____.", "Even though I am tired, I like _____."));

        // ----------- Possession / Ownership -----------
        lesson2unit3Phrases.add(new Phrase("Aso ni Maria", "Maria's dog"));
        lesson2unit3Phrases.add(new Phrase("Tsokolate ni Chrisanto", "Chrisanto's chocolate"));

        lesson2unit3Phrases.add(new Phrase("Tinapay", "Bread"));
        lesson2unit3Phrases.add(new Phrase("Ngayon", "Now"));
        lesson2unit3Phrases.add(new Phrase("Gusto ni Jessa ng tinapay.", "Jessa wants bread."));

        // ----------- Temperature / Food descriptions -----------
        lesson2unit3Phrases.add(new Phrase("Gulay", "Vegetables"));
        lesson2unit3Phrases.add(new Phrase("Digmaan", "War"));
        lesson2unit3Phrases.add(new Phrase("Sumakay", "Ride"));
        lesson2unit3Phrases.add(new Phrase("Alagang hayop", "Pet"));
        lesson2unit3Phrases.add(new Phrase("Anak", "Child (daughter/son)"));

        // ----------- Likes & Dislikes with others -----------
        lesson2unit3Phrases.add(new Phrase("Gusto niya ng tubig.", "She/he wants water."));
        lesson2unit3Phrases.add(new Phrase("Ayaw ni Junjun ng gulay.", "Junjun doesn't want vegetables."));
        lesson2unit3Phrases.add(new Phrase("Gusto ko ng tinapay.", "I want bread."));
        lesson2unit3Phrases.add(new Phrase("Ayaw namin ng digmaan.", "We don't want war."));
        lesson2unit3Phrases.add(new Phrase("Gusto niya ng mainit na tsokolate.", "She/he wants hot chocolate."));

        //Extra
        //lesson2unit3Phrases.add(new Phrase("Manok", "Chicken"));

        // Questions / practice (your prompt text + items)
        List<UnitQuestion> lesson2unit3Questions = new ArrayList<>();
        lesson2unit3Questions.add(new UnitQuestion(
                "Gusto niya ng tsokolate.",
                "She/He wants chocolate. / She/He likes chocolate."
        ));

        lesson2unit3Questions.add(new UnitQuestion(
                "Ayaw ni Joanne ng sili.",
                "Joanne doesn't like chili pepper. / Joanne does not like chili peppers."
        ));

        lesson2unit3Questions.add(new UnitQuestion(
                "We all like water.",
                "Gusto namin lahat ng tubig."
        ));

        lesson2unit3Questions.add(new UnitQuestion(
                "She/he wants bread now.",
                "Gusto niya ng tinapay ngayon. / Gustong niya tinapay ngayon."
        ));

        lesson2unit3Questions.add(new UnitQuestion(
                "Gusto mo ba sumakay sa kotse ko?",
                "Do you want to ride in my car? / Do you want a ride in my car?"
        ));
        lesson2unit3Questions.add(new UnitQuestion(
                "Gusto niya sa amin.",
                "She/He likes us."
        ));
        lesson2unit3Questions.add(new UnitQuestion(
                "Ayaw mo ba ng alagang hayop?",
                "Don't you want a pet?"
        ));
        lesson2unit3Questions.add(new UnitQuestion(
                "My daughter wants a pet.",
                "Gusto ng anak ko ng alagang hayop."
        ));

        units.put("lesson2_unit3", new Unit(
                "Pseudo-verbs and Ni",
                "Translate these sentences using the correct pronouns and pseudo-verbs, then say them aloud:",
                lesson2unit3Phrases,
                lesson2unit3Questions
        ));

// -------------------
// LESSON 2 - UNIT 4: Making Verbs with -UM-
// -------------------
        List<Phrase> lesson2unit4Phrases = new ArrayList<>();
// ----------- Root Words (Base Form) -----------
        lesson2unit4Phrases.add(new Phrase("Kain", "Eat"));
        lesson2unit4Phrases.add(new Phrase("Inom", "Drink"));

        lesson2unit4Phrases.add(new Phrase("Kumain", "Ate"));
        lesson2unit4Phrases.add(new Phrase("Uminom", "Drank"));

        lesson2unit4Phrases.add(new Phrase("Tawa", "Laugh"));
        lesson2unit4Phrases.add(new Phrase("Higa", "Lie down"));
        lesson2unit4Phrases.add(new Phrase("Labas", "Go out"));

// ----------- Conjugated with -um- -----------
        lesson2unit4Phrases.add(new Phrase("Tumawa", "Laughed"));
        lesson2unit4Phrases.add(new Phrase("Bumili", "Bought"));
        lesson2unit4Phrases.add(new Phrase("Humiga", "Laid down"));
        lesson2unit4Phrases.add(new Phrase("Lumabas", "Went out"));

        lesson2unit4Phrases.add(new Phrase("Akyat", "Climb"));
        lesson2unit4Phrases.add(new Phrase("Upo", "Sit"));

        lesson2unit4Phrases.add(new Phrase("Umakyat", "Climbed"));
        lesson2unit4Phrases.add(new Phrase("Umupo", "Sat"));

        lesson2unit4Phrases.add(new Phrase("Gusto kong umakyat sa rock.", "I want to climb the rock."));
        lesson2unit4Phrases.add(new Phrase("Gusto kong lumabas sa bahay.", "I want to go outside the house."));

        lesson2unit4Phrases.add(new Phrase("Takbo", "Run"));
        lesson2unit4Phrases.add(new Phrase("Tulong", "Help"));
        lesson2unit4Phrases.add(new Phrase("Kanta", "Sing"));
        lesson2unit4Phrases.add(new Phrase("Tawag", "Call"));

// ----------- Questions & Sentences -----------
        lesson2unit4Phrases.add(new Phrase("Restawran", "Restaurant"));
        lesson2unit4Phrases.add(new Phrase("Sayaw", "Dance"));
        lesson2unit4Phrases.add(new Phrase("Koro", "Choir"));

        lesson2unit4Phrases.add(new Phrase("Ano ang gusto mong gawin?", "What do you want/like to do?"));

        lesson2unit4Phrases.add(new Phrase("Gusto nilang kumanta sa koro.", "They want to sing in the choir."));
        lesson2unit4Phrases.add(new Phrase("Gusto niyang kumain sa restawran.", "She/He wants to eat in the restaurant."));
        lesson2unit4Phrases.add(new Phrase("Gusto ni Marie na sumayaw.", "Marie likes to dance."));
        lesson2unit4Phrases.add(new Phrase("Gusto kong tumakbo sa labas.", "I like to run outside."));

// ----------- Mag- Verbs -----------
        lesson2unit4Phrases.add(new Phrase("Magluto", "Going to cook"));
        lesson2unit4Phrases.add(new Phrase("Maglaro", "Going to play"));

// ----------- Ma- Verbs -----------
        lesson2unit4Phrases.add(new Phrase("Mabili", "Can be bought"));
        lesson2unit4Phrases.add(new Phrase("Magutom", "Could get hungry"));

        lesson2unit4Phrases.add(new Phrase("darating", "Upcoming"));
        lesson2unit4Phrases.add(new Phrase("eleksyon", "Election"));
        lesson2unit4Phrases.add(new Phrase("sumulat", "Wrote"));
        lesson2unit4Phrases.add(new Phrase("paligid", "Around / Surroundings"));
        lesson2unit4Phrases.add(new Phrase("dagat", "Sea / Ocean"));

        List<UnitQuestion> lesson2unit4Questions = new ArrayList<>();
        lesson2unit4Questions.add(new UnitQuestion(
                "Joseph wrote to Maria.",
                "Sumulat si Joseph kay Maria."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "We (exclusive) want to go to Baguio.",
                "Gusto naming pumunta sa Baguio."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "She wants to sit outside in the garden.",
                "Gusto niyang umupo sa labas ng hardin."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "He doesn't like to swim in the sea.",
                "Ayaw niyang lumangoy sa dagat."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "Georgie wants to eat at Chipotle.",
                "Gusto ni Georgie na kumain sa Chipotle."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "We (inclusive) want to dance outside.",
                "Gusto nating sumayaw sa labas."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "They want to run around Luneta.",
                "Gusto nilang tumakbo sa paligid ng Luneta."
        ));
        lesson2unit4Questions.add(new UnitQuestion(
                "They are going to the upcoming election.",
                "Pupunta sila sa darating na eleksyon."
        ));

        units.put("lesson2_unit4", new Unit(
                "Making Verbs with -UM-, MAG-, and MA-",
                "Translate these sentences while using the appropriate -UM- verbs, then say them aloud:",
                lesson2unit4Phrases,
                lesson2unit4Questions
        ));

        // -------------------
        // LESSON 2 - UNIT 5: Past, Present, and Future Verbs
        // -------------------
        List<Phrase> lesson2unit5Phrases = new ArrayList<>();

        // ----------- Future Tense (Mag-) -----------
        lesson2unit5Phrases.add(new Phrase("Maglalakad", "Going to walk"));

        // ----------- Root Words / Commands -----------
        lesson2unit5Phrases.add(new Phrase("Bangon", "Get up"));
        lesson2unit5Phrases.add(new Phrase("Gising", "Wake up"));
        lesson2unit5Phrases.add(new Phrase("Langoy", "Swim"));
        lesson2unit5Phrases.add(new Phrase("Lakad", "Walk"));

        lesson2unit5Phrases.add(new Phrase("Sisid", "Dive"));
        lesson2unit5Phrases.add(new Phrase("Lipad", "Fly"));
        lesson2unit5Phrases.add(new Phrase("Tayo", "Stand"));
        lesson2unit5Phrases.add(new Phrase("Biyahe", "Travel"));

        // ----------- Past / Present / Future Sets -----------
        // Sumayaw
        lesson2unit5Phrases.add(new Phrase("Sumayaw", "Danced"));
        lesson2unit5Phrases.add(new Phrase("Sumasayaw", "Dancing"));
        lesson2unit5Phrases.add(new Phrase("Sasayaw", "Will dance"));

        // Tumayo
        lesson2unit5Phrases.add(new Phrase("Tumayo", "Stood up"));
        lesson2unit5Phrases.add(new Phrase("Tumatayo", "Standing"));
        lesson2unit5Phrases.add(new Phrase("Tatayo", "Will stand"));

        // Bumiyahe
        lesson2unit5Phrases.add(new Phrase("Bumiyahe", "Traveled"));
        lesson2unit5Phrases.add(new Phrase("Bumabyahe", "Traveling"));
        lesson2unit5Phrases.add(new Phrase("Babiyahe", "Will travel"));

        // Kumain
        lesson2unit5Phrases.add(new Phrase("Kumain", "Ate"));
        lesson2unit5Phrases.add(new Phrase("Kumakain", "Eating"));
        lesson2unit5Phrases.add(new Phrase("Kakain", "Will eat"));

        // Pumunta
        lesson2unit5Phrases.add(new Phrase("Pumunta", "Went"));
        lesson2unit5Phrases.add(new Phrase("Pumupunta", "Going"));
        lesson2unit5Phrases.add(new Phrase("Pupunta", "Will go"));

        // Lumipad
        lesson2unit5Phrases.add(new Phrase("Lumipad", "Flew"));
        lesson2unit5Phrases.add(new Phrase("Lumilipad", "Flying"));
        lesson2unit5Phrases.add(new Phrase("Lilipad", "Will fly"));

        // Uminom
        lesson2unit5Phrases.add(new Phrase("Uminom", "Drank"));
        lesson2unit5Phrases.add(new Phrase("Umiinom", "Drinking"));
        lesson2unit5Phrases.add(new Phrase("Iinom", "Will drink"));

        // Umakyat
        lesson2unit5Phrases.add(new Phrase("Umakyat", "Climbed"));
        lesson2unit5Phrases.add(new Phrase("Umaakyat", "Climbing"));
        lesson2unit5Phrases.add(new Phrase("Aakyat", "Will climb"));

        // Ulan
        lesson2unit5Phrases.add(new Phrase("Ulan", "Rain"));
        lesson2unit5Phrases.add(new Phrase("Umulan", "Rained"));
        lesson2unit5Phrases.add(new Phrase("Umuulan", "Raining"));
        lesson2unit5Phrases.add(new Phrase("Uulan", "Will rain"));

        // Upo
        lesson2unit5Phrases.add(new Phrase("Upo", "Sit"));
        lesson2unit5Phrases.add(new Phrase("Umupo", "Sat down"));
        lesson2unit5Phrases.add(new Phrase("Umuupo", "Sitting"));
        lesson2unit5Phrases.add(new Phrase("Uupo", "Will sit"));

        // ----------- Other Words -----------
        lesson2unit5Phrases.add(new Phrase("Papunta", "On the way/Headed to"));
        lesson2unit5Phrases.add(new Phrase("Bundok", "Mountain"));
        lesson2unit5Phrases.add(new Phrase("Maaga", "Early"));
        lesson2unit5Phrases.add(new Phrase("Ate", "Older sister"));
        lesson2unit5Phrases.add(new Phrase("Kuya", "Older brother"));

        // ----------- Sentences -----------
        //reorder based on occur/appearance i n uni t added papaunta
        lesson2unit5Phrases.add(new Phrase("Umakyat si Ate Cherry sa bundok.", "Ate Cherry climbed the mountain."));
        lesson2unit5Phrases.add(new Phrase("Gigising ako nang maaga.", "I will wake up early."));
        lesson2unit5Phrases.add(new Phrase("Umakyat si Kuya JR sa puno.", "Kuya JR climbed the tree."));
        lesson2unit5Phrases.add(new Phrase("Papunta si Ate at Kuya sa bundok.", "Ate and Kuya are on the way to the mountain."));

        lesson2unit5Phrases.add(new Phrase("Bumangon ako.", "I got up."));
        lesson2unit5Phrases.add(new Phrase("Iinom siya ng tubig.", "She will drink water."));
        lesson2unit5Phrases.add(new Phrase("Tumatakbo siya sa bahay.", "He is running in the house."));
        lesson2unit5Phrases.add(new Phrase("Si Marites ay lumalangoy sa pool.", "Marites is swimming in the pool."));
        lesson2unit5Phrases.add(new Phrase("Umakyat ako sa puno.", "I climbed the tree."));
        lesson2unit5Phrases.add(new Phrase("Maglalakad kami papuntang paaralan bukas.", "We will walk to school tomorrow."));
        lesson2unit5Phrases.add(new Phrase("Uminom ng Coca-Cola sina Lolo at Lola.", "Lolo and Lola drank Coca-Cola."));
        lesson2unit5Phrases.add(new Phrase("Tumakbo ako papuntang supermarket.", "I ran to the supermarket."));

        // New vocabulary
        lesson2unit5Phrases.add(new Phrase("Sumali", "Join"));
        lesson2unit5Phrases.add(new Phrase("Bumoto", "Vote"));
        lesson2unit5Phrases.add(new Phrase("Sumunod", "Obey/follow"));
        lesson2unit5Phrases.add(new Phrase("Umupa", "Rent"));

        // Worksheet / practice
        List<UnitQuestion> lesson2unit5Questions = new ArrayList<>();

        lesson2unit5Questions.add(new UnitQuestion(
                "Join",
                "Sumali, Sumasali, Sali"
        ));
        lesson2unit5Questions.add(new UnitQuestion(
                "Vote",
                "Bumoto, Bumoboto, Boboto"
        ));
        lesson2unit5Questions.add(new UnitQuestion(
                "Obey or Follow",
                "Sumunod, Sumasunod, Susunod"
        ));
        lesson2unit5Questions.add(new UnitQuestion(
                "Rent",
                "Umupa, Umuupa, Uupa"
        ));

        units.put("lesson2_unit5", new Unit(
                "Past, Present, and Future Verbs",
                "Translate these sentences and practice turning root words into past, present, and future tenses, then say each aloud:",
                lesson2unit5Phrases,
                lesson2unit5Questions
        ));

        // -------------------
        // LESSON 2 - UNIT 6: Verb Review
        // -------------------
        List<Phrase> lesson2unit6Phrases = new ArrayList<>();

        lesson2unit6Phrases.add(new Phrase("Maglalakad", "Going to walk"));

        // Worksheet / practice
        List<UnitQuestion> lesson2unit6Questions = new ArrayList<>();

        lesson2unit6Questions.add(new UnitQuestion(
                "Join",
                "Sumali, Sumasali, Sali"
        ));

        units.put("lesson2_unit6", new Unit(
                "Verbs -um-, mag-, ma-, in-, i- Review",
                "Translate these sentences and practice turning root words into past, present, and future tenses, then say each aloud:",
                lesson2unit5Phrases,
                lesson2unit5Questions
        ));

    }

    public static Unit getUnit(String key) {
        return units.getOrDefault(key,
                new Unit("Untitled Unit", "No instructions", new ArrayList<>(), new ArrayList<>()));
    }

    public static String getUnitTitle(String key) {
        Unit u = units.get(key);
        return (u != null) ? u.getTitle() : "Untitled Unit";
    }

}
