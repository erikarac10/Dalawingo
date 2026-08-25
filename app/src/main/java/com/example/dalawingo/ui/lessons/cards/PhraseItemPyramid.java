package com.example.dalawingo.ui.lessons.cards;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dalawingo.R;

public final class PhraseItemPyramid {

    private PhraseItemPyramid() {}

    public enum FontStyle {
        REGULAR, BOLD, ITALIC, BOLD_ITALIC
    }

    // ------------------------------------------------------------
    // Overload 3: full pyramid (rows 1-4) + styles for 1A and 4
    // phraseItemPyramid(view, speakClick,
    //   "1A", style1A,
    //   "2A","2B","2C",
    //   "3A","3B","3C",
    //   "4", style4)
    // ------------------------------------------------------------
    public static void phraseItemPyramid(
            @NonNull View v,
            @NonNull CardAdapter.OnSpeakClick speakClick,
            @Nullable String tvTagalog1A,
            @NonNull FontStyle tvTagalog1AStyle,
            @Nullable String tvTagalog2A,
            @Nullable String tvTagalog2B,
            @Nullable String tvTagalog2C,
            @Nullable String tvTagalog3A,
            @Nullable String tvTagalog3B,
            @Nullable String tvTagalog3C,
            @Nullable String tvTagalog4,
            @NonNull FontStyle tvTagalog4Style
    ) {
        resetGone(v);
        restoreXmlDefaults(v);

        setTextVisible(v, R.id.tvTagalog1A, tvTagalog1A);
        applyFontStyle(v.findViewById(R.id.tvTagalog1A), tvTagalog1AStyle);

        setTextVisible(v, R.id.tvTagalog2A, tvTagalog2A);
        setTextVisible(v, R.id.tvTagalog2B, tvTagalog2B); // stays bold
        setTextVisible(v, R.id.tvTagalog2C, tvTagalog2C);

        setTextVisible(v, R.id.tvTagalog3A, tvTagalog3A);
        setTextVisible(v, R.id.tvTagalog3B, tvTagalog3B); // stays bold
        setTextVisible(v, R.id.tvTagalog3C, tvTagalog3C);

        setTextVisible(v, R.id.tvTagalog4, tvTagalog4);
        applyFontStyle(v.findViewById(R.id.tvTagalog4), tvTagalog4Style);
        TextView t4 = v.findViewById(R.id.tvTagalog4);
        applyFontStyle(t4, tvTagalog4Style);

        if (t4 != null) {
            t4.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    (tvTagalog4Style == FontStyle.BOLD) ? 19f : 17f
            );
        }
        collapseFlexIfEmpty(v, R.id.tagalogFlex1, R.id.tvTagalog2A, R.id.tvTagalog2B, R.id.tvTagalog2C);
        collapseFlexIfEmpty(v, R.id.tagalogFlex2, R.id.tvTagalog3A, R.id.tvTagalog3B, R.id.tvTagalog3C);

        // speaker speaks tvTagalog3B (only row with speaker in this XML)
        //if(tvTagalog4Style != null) {
        //    setSpeaker(v, speakClick, tvTagalog4);
        //} else {
            setSpeaker(v, speakClick, tvTagalog3B);
        //}
    }

    // -------------------- RESET --------------------

    public static void resetGone(@NonNull View v) {
        goneText(v, R.id.tvTagalog1A);

        goneText(v, R.id.tvTagalog2A);
        goneText(v, R.id.tvTagalog2B);
        goneText(v, R.id.tvTagalog2C);

        goneText(v, R.id.tvTagalog3A);
        goneText(v, R.id.tvTagalog3B);
        goneText(v, R.id.tvTagalog3C);

        goneText(v, R.id.tvTagalog4);

        View flex1 = v.findViewById(R.id.tagalogFlex1);
        if (flex1 != null) flex1.setVisibility(View.GONE);

        View flex2 = v.findViewById(R.id.tagalogFlex2);
        if (flex2 != null) flex2.setVisibility(View.GONE);

        ImageView btnSpeak = v.findViewById(R.id.btnSpeak);
        if (btnSpeak != null) {
            btnSpeak.setVisibility(View.GONE);
            btnSpeak.setOnClickListener(null);
        }
    }

    // KEY: wipe recycler carryover, then restore intended XML defaults (bold tokens)
    private static void goneText(@NonNull View v, int id) {
        TextView tv = v.findViewById(id);
        if (tv == null) return;
        tv.setText("");
        tv.setVisibility(View.GONE);
        //tv.setTypeface(null, Typeface.NORMAL);
    }

    private static void restoreXmlDefaults(@NonNull View v) {
        // These are bold in your XML. Reset above nukes them, so restore here.
        TextView t2b = v.findViewById(R.id.tvTagalog2B);
        if (t2b != null) t2b.setTypeface(null, Typeface.BOLD);

        TextView t3b = v.findViewById(R.id.tvTagalog3B);
        if (t3b != null) t3b.setTypeface(null, Typeface.BOLD);
    }

    // -------------------- HELPERS --------------------

    private static void setTextVisible(@NonNull View v, int id, @Nullable String text) {
        TextView tv = v.findViewById(id);
        if (tv == null) return;

        if (text != null && text.trim().length() > 0) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
    }

    private static void applyFontStyle(@Nullable TextView tv, @NonNull FontStyle style) {
        if (tv == null) return;
        if (style == null) style = FontStyle.REGULAR;

        switch (style) {
            case BOLD:
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                break;

            case ITALIC:
                tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                break;

            case BOLD_ITALIC:
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);
                break;

            case REGULAR:
            default:
                break;
        }
    }

    private static void collapseFlexIfEmpty(
            @NonNull View root,
            int flexId,
            int aId, int bId, int cId
    ) {
        View flex = root.findViewById(flexId);
        if (flex == null) return;

        View a = root.findViewById(aId);
        View b = root.findViewById(bId);
        View c = root.findViewById(cId);

        boolean any =
                (a != null && a.getVisibility() == View.VISIBLE) ||
                        (b != null && b.getVisibility() == View.VISIBLE) ||
                        (c != null && c.getVisibility() == View.VISIBLE);

        flex.setVisibility(any ? View.VISIBLE : View.GONE);
    }

    private static void setSpeaker(
            @NonNull View v,
            @NonNull CardAdapter.OnSpeakClick speakClick,
            @Nullable String speakText
    ) {
        ImageView btnSpeak = v.findViewById(R.id.btnSpeak);
        if (btnSpeak == null) {
            return;
        }

        if (speakClick == null) {
            btnSpeak.setVisibility(View.GONE);
            btnSpeak.setOnClickListener(null);
            return;
        }

        if (speakText != null && speakText.trim().length() > 0) {
            btnSpeak.setVisibility(View.VISIBLE);
            btnSpeak.setOnClickListener(x -> speakClick.onSpeak(speakText));
        } else {
            btnSpeak.setVisibility(View.GONE);
            btnSpeak.setOnClickListener(null);
        }
    }
}
