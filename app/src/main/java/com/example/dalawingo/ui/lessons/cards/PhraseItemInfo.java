package com.example.dalawingo.ui.lessons.cards;

import androidx.annotation.NonNull;

public final class PhraseItemInfo {

    @NonNull public final String tagalog;
    @NonNull public final String english;
    public final boolean showSpeaker;

    public PhraseItemInfo(
            @NonNull String tagalog,
            @NonNull String english,
            boolean showSpeaker
    ) {
        this.tagalog = tagalog;
        this.english = english;
        this.showSpeaker = showSpeaker;
    }
}
