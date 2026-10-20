package com.example.dalawingo.ui.lessons.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dalawingo.R;

/* JADX INFO: loaded from: classes10.dex */
public final class PhraseItemPyramid {

    public enum FontStyle {
        REGULAR,
        BOLD,
        ITALIC,
        BOLD_ITALIC
    }

    private PhraseItemPyramid() {
    }

    public static void phraseItemPyramid(View v, CardAdapter.OnSpeakClick speakClick, String tvTagalog1A, FontStyle tvTagalog1AStyle, String tvTagalog2A, String tvTagalog2B, String tvTagalog2C, String tvTagalog3A, String tvTagalog3B, String tvTagalog3C, String tvTagalog4, FontStyle tvTagalog4Style) {
        resetGone(v);
        restoreXmlDefaults(v);
        setTextVisible(v, R.id.tvTagalog1A, tvTagalog1A);
        applyFontStyle((TextView) v.findViewById(R.id.tvTagalog1A), tvTagalog1AStyle);
        setTextVisible(v, R.id.tvTagalog2A, tvTagalog2A);
        setTextVisible(v, R.id.tvTagalog2B, tvTagalog2B);
        setTextVisible(v, R.id.tvTagalog2C, tvTagalog2C);
        setTextVisible(v, R.id.tvTagalog3A, tvTagalog3A);
        setTextVisible(v, R.id.tvTagalog3B, tvTagalog3B);
        setTextVisible(v, R.id.tvTagalog3C, tvTagalog3C);
        setTextVisible(v, R.id.tvTagalog4, tvTagalog4);
        applyFontStyle((TextView) v.findViewById(R.id.tvTagalog4), tvTagalog4Style);
        TextView t4 = (TextView) v.findViewById(R.id.tvTagalog4);
        applyFontStyle(t4, tvTagalog4Style);
        if (t4 != null) {
            t4.setTextSize(2, tvTagalog4Style == FontStyle.BOLD ? 19.0f : 17.0f);
        }
        collapseFlexIfEmpty(v, R.id.tagalogFlex1, R.id.tvTagalog2A, R.id.tvTagalog2B, R.id.tvTagalog2C);
        collapseFlexIfEmpty(v, R.id.tagalogFlex2, R.id.tvTagalog3A, R.id.tvTagalog3B, R.id.tvTagalog3C);
        setSpeaker(v, speakClick, tvTagalog3B);
    }

    public static void resetGone(View v) {
        goneText(v, R.id.tvTagalog1A);
        goneText(v, R.id.tvTagalog2A);
        goneText(v, R.id.tvTagalog2B);
        goneText(v, R.id.tvTagalog2C);
        goneText(v, R.id.tvTagalog3A);
        goneText(v, R.id.tvTagalog3B);
        goneText(v, R.id.tvTagalog3C);
        goneText(v, R.id.tvTagalog4);
        View flex1 = v.findViewById(R.id.tagalogFlex1);
        if (flex1 != null) {
            flex1.setVisibility(8);
        }
        View flex2 = v.findViewById(R.id.tagalogFlex2);
        if (flex2 != null) {
            flex2.setVisibility(8);
        }
        ImageView btnSpeak = (ImageView) v.findViewById(R.id.btnSpeak);
        if (btnSpeak != null) {
            btnSpeak.setVisibility(8);
            btnSpeak.setOnClickListener(null);
        }
    }

    private static void goneText(View v, int id) {
        TextView tv = (TextView) v.findViewById(id);
        if (tv == null) {
            return;
        }
        tv.setText("");
        tv.setVisibility(8);
    }

    private static void restoreXmlDefaults(View v) {
        TextView t2b = (TextView) v.findViewById(R.id.tvTagalog2B);
        if (t2b != null) {
            t2b.setTypeface(null, 1);
        }
        TextView t3b = (TextView) v.findViewById(R.id.tvTagalog3B);
        if (t3b != null) {
            t3b.setTypeface(null, 1);
        }
    }

    private static void setTextVisible(View v, int id, String text) {
        TextView tv = (TextView) v.findViewById(id);
        if (tv == null) {
            return;
        }
        if (text != null && text.trim().length() > 0) {
            tv.setText(text);
            tv.setVisibility(0);
            tv.setGravity(17);
            tv.setTextAlignment(4);
            return;
        }
        tv.setText("");
        tv.setVisibility(8);
    }

    private static void applyFontStyle(TextView tv, FontStyle style) {
        if (tv == null) {
        }
        if (style == null) {
            style = FontStyle.REGULAR;
        }
        switch (style.ordinal()) {
            case 1:
                tv.setTypeface(tv.getTypeface(), 1);
                break;
            case 2:
                tv.setTypeface(tv.getTypeface(), 2);
                break;
            case 3:
                tv.setTypeface(tv.getTypeface(), 3);
                break;
        }
    }

    private static void collapseFlexIfEmpty(View root, int flexId, int aId, int bId, int cId) {
        View flex = root.findViewById(flexId);
        if (flex == null) {
            return;
        }
        View a = root.findViewById(aId);
        View b = root.findViewById(bId);
        View c = root.findViewById(cId);
        boolean any = (a != null && a.getVisibility() == 0) || (b != null && b.getVisibility() == 0) || (c != null && c.getVisibility() == 0);
        flex.setVisibility(any ? 0 : 8);
    }

    private static void setSpeaker(View v, final CardAdapter.OnSpeakClick speakClick, final String speakText) {
        ImageView btnSpeak = (ImageView) v.findViewById(R.id.btnSpeak);
        if (btnSpeak == null) {
            return;
        }
        if (speakClick == null) {
            btnSpeak.setVisibility(8);
            btnSpeak.setOnClickListener(null);
        } else if (speakText != null && speakText.trim().length() > 0) {
            btnSpeak.setVisibility(0);
            btnSpeak.setOnClickListener(new View.OnClickListener() { // from class: com.example.dalawingo.ui.lessons.cards.PhraseItemPyramid$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    speakClick.onSpeak(speakText);
                }
            });
        } else {
            btnSpeak.setVisibility(8);
            btnSpeak.setOnClickListener(null);
        }
    }
}
