package com.example.dalawingo.ui.lessons.cards;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dalawingo.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public final class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnSpeakClick {
        void onSpeak(@NonNull String tagalogText);
    }

    public enum Align { LEFT, CENTER, RIGHT }
    public enum FontStyle { REGULAR, BOLD, ITALIC, BOLD_ITALIC }

    private static final int VIEW_PHRASES = 1;
    private static final int VIEW_IMAGE   = 2;
    private static final int VIEW_TITLE   = 3;

    private static final int[] TITLE_BG_CYCLE = {
            R.color.pastel_red,
            R.color.pastel_orange,
            R.color.pastel_yellow,
            R.color.pastel_green,
            R.color.pastel_blue,
            R.color.pastel_purple,
            R.color.pastel_pink
    };

    private final List<CardItem> items = new ArrayList<>();
    private final OnSpeakClick speakClick;

    public CardAdapter(@NonNull OnSpeakClick speakClick) {
        this.speakClick = speakClick;
    }

    public void submitList(@NonNull List<CardItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        CardItem it = items.get(position);
        if (it.titleText != null) return VIEW_TITLE;
        if (it.imageRes != null) return VIEW_IMAGE;
        return VIEW_PHRASES;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TITLE) {
            return new TitleVH(inf.inflate(R.layout.card_phrases, parent, false));
        }

        if (viewType == VIEW_IMAGE) {
            return new ImageVH(inf.inflate(R.layout.card_phrases, parent, false));
        }

        return new PhraseGroupVH(
                inf.inflate(R.layout.card_phrases, parent, false),
                speakClick
        );
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            int position
    ) {
        CardItem item = items.get(position);

        if (holder instanceof TitleVH) {
            ((TitleVH) holder).bind(item.titleText, getTitleIndex(position));
            return;
        }

        if (holder instanceof ImageVH) {
            ((ImageVH) holder).bind(item.imageRes);
            return;
        }

        ((PhraseGroupVH) holder).bind(item.phraseItems);
    }

    // 0-based index among TITLE cards only (used for bg cycling + first-title size)
    private int getTitleIndex(int adapterPos) {
        int idx = 0;
        for (int i = 0; i < adapterPos; i++) {
            if (items.get(i).titleText != null) idx++;
        }
        return idx;
    }

    // --------------------------------------------------
    // TITLE VIEW HOLDER
    // --------------------------------------------------

    static final class TitleVH extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final LinearLayout container;

        private final View row;
        private final TextView tvTagalog1;

        TitleVH(@NonNull View itemView) {
            super(itemView);

            card = (MaterialCardView) itemView;
            container = itemView.findViewById(R.id.cardContainer);

            container.removeAllViews();
            row = LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.card_phrase_item, container, false);
            container.addView(row);

            tvTagalog1 = row.findViewById(R.id.tvTagalog1);
        }

        void bind(@NonNull String titleText, int titleIndex) {
            resetTitleCardToDefaults(card, row);
            applySpeakerLane(row, false);

            if (tvTagalog1 != null) {
                tvTagalog1.setText(titleText);
                tvTagalog1.setVisibility(View.VISIBLE);

                float sizeSp = (titleIndex == 0) ? 30f : 20f;
                tvTagalog1.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);

                tvTagalog1.setTypeface(tvTagalog1.getTypeface(), Typeface.BOLD);
                tvTagalog1.setGravity(Gravity.CENTER_HORIZONTAL);
                tvTagalog1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            int colorRes = TITLE_BG_CYCLE[titleIndex % TITLE_BG_CYCLE.length];
            card.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorRes));
        }
    }

    // --------------------------------------------------
    // IMAGE VIEW HOLDER
    // --------------------------------------------------

    class ImageVH extends RecyclerView.ViewHolder {

        private final LinearLayout container;

        ImageVH(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.cardContainer);
        }

        void bind(@NonNull Integer imageRes) {
            container.removeAllViews();

            ImageView iv = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            iv.setLayoutParams(lp);

            iv.setAdjustViewBounds(true);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setImageResource(imageRes);

            container.addView(iv);
        }
    }

    // --------------------------------------------------
    // PHRASE VIEW HOLDER (supports inline INFO rows)
    // --------------------------------------------------

    static final class PhraseGroupVH extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final LinearLayout container;
        private final OnSpeakClick speakClick;

        PhraseGroupVH(@NonNull View itemView, @NonNull OnSpeakClick speakClick) {
            super(itemView);
            card = (MaterialCardView) itemView;
            this.speakClick = speakClick;
            container = itemView.findViewById(R.id.cardContainer);
        }

        void bind(@Nullable List<PhraseItem> phrases) {
            resetCardToWhite(card);

            container.removeAllViews();
            if (phrases == null || phrases.isEmpty()) return;

            LayoutInflater inf = LayoutInflater.from(itemView.getContext());

            for (PhraseItem pi : phrases) {
                if (pi == null) continue;

                if (pi.itemType == PhraseItem.ItemType.INFO) {
                    View infoRow = inf.inflate(
                            R.layout.card_phrase_item_information,
                            container,
                            false
                    );
                    bindInfoRow(infoRow, pi);
                    container.addView(infoRow);
                    continue;
                }
                if (pi.itemType == PhraseItem.ItemType.PYRAMID) {
                    View pv = inf.inflate(R.layout.card_phrase_item_pyramid, container, false);
                    PhraseItemPyramid.phraseItemPyramid(
                            pv,
                            speakClick,
                            pi.py1A, pi.py1AStyle,
                            pi.py2A, pi.py2B, pi.py2C,
                            pi.py3A, pi.py3B, pi.py3C,
                            pi.py4,  pi.py4Style
                    );
                    container.addView(pv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.CHART) {
                    View cv = inf.inflate(R.layout.card_phrase_item_chart, container, false);
                    PhraseItemCharts.phraseItemChart(
                            cv,
                            pi.chartHeader1,
                            pi.chartHeader2,
                            pi.chartHeader3,
                            pi.chartTagalog,
                            pi.chartEnglish
                    );
                    container.addView(cv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.IMAGE) {
                    if (pi.imageResId == null) continue;

                    ImageView iv = new ImageView(itemView.getContext());
                    iv.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    iv.setAdjustViewBounds(true);
                    iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    iv.setImageResource(pi.imageResId);

                    container.addView(iv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.TABLE1) {
                    View tv = inf.inflate(R.layout.card_phrase_item_table1, container, false);
                    PhraseItemCharts.phraseItemTable1(tv, pi.table1);
                    container.addView(tv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.TABLE2) {
                    View tv = inf.inflate(R.layout.card_phrase_item_table2, container, false);
                    PhraseItemCharts.phraseItemTable2(tv, speakClick, pi.table2);
                    container.addView(tv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.TABLE3) {
                    View tv = inf.inflate(R.layout.card_phrase_item_table3, container, false);
                    //PhraseItemCharts
                    //        .phraseItemTable3(tv, pi.table3RootWord, pi.table3RootMeaning, pi.table3);
                    String[] row1 = (pi.table3 != null && pi.table3.length > 0) ? pi.table3[0] : null;
                    String[] row2 = (pi.table3 != null && pi.table3.length > 1) ? pi.table3[1] : null;
                    String[] row3 = (pi.table3 != null && pi.table3.length > 2) ? pi.table3[2] : null;

                    PhraseItemCharts.phraseItemTable3(tv, pi.table3RootWord, pi.table3RootMeaning, row1, row2, row3);

                    container.addView(tv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.TABLE4) {
                    View tv = inf.inflate(R.layout.card_phrase_item_table4, container, false);

                    String[] h = pi.table4Headers;
                    PhraseItemCharts.phraseItemTable4(
                            tv,
                            (h != null && h.length > 0) ? h[0] : null,
                            (h != null && h.length > 1) ? h[1] : null,
                            (h != null && h.length > 2) ? h[2] : null,
                            (h != null && h.length > 3) ? h[3] : null,
                            pi.table4
                    );

                    container.addView(tv);
                    continue;
                }

                if (pi.itemType == PhraseItem.ItemType.TABLE5) {
                    View tv = inf.inflate(R.layout.card_phrase_item_table5, container, false);
                    PhraseItemCharts.phraseItemTable5(tv, pi.table5Headers, pi.table5);
                    container.addView(tv);
                    continue;
                }

                View row = inf.inflate(R.layout.card_phrase_item, container, false);
                bindRow(row, pi);
                container.addView(row);
            }
        }

        private void bindInfoRow(@NonNull View v, @NonNull PhraseItem item) {
            TextView tvTagalog = v.findViewById(R.id.tvTagalog);
            TextView tvEnglish = v.findViewById(R.id.tvEnglish);
            ImageView btnSpeak = v.findViewById(R.id.btnSpeak);

            if (tvTagalog != null) tvTagalog.setText(item.tagalog1 != null ? item.tagalog1 : "");
            if (tvEnglish != null) tvEnglish.setText(item.english1 != null ? item.english1 : "");

            if (btnSpeak != null) {
                if (item.showSpeaker && hasText(item.tagalog1)) {
                    btnSpeak.setVisibility(View.VISIBLE);
                    btnSpeak.setOnClickListener(x -> speakClick.onSpeak(item.tagalog1));
                } else {
                    btnSpeak.setVisibility(View.GONE);
                    btnSpeak.setOnClickListener(null);
                }
            }
        }

        private void bindRow(@NonNull View v, @NonNull PhraseItem item) {
            resetRowToXmlDefaults(v);
            boolean showLane = item.showSpeaker && hasText(item.tagalog1);
            applySpeakerLane(v, showLane);

            TextView tvT1  = v.findViewById(R.id.tvTagalog1);
            TextView tvT2  = v.findViewById(R.id.tvTagalog2);
            TextView tvT3  = v.findViewById(R.id.tvTagalog3);

            TextView tvE1  = v.findViewById(R.id.tvEnglish1);
            TextView tvE2  = v.findViewById(R.id.tvEnglish2);
            TextView tvE3  = v.findViewById(R.id.tvEnglish3);

            TextView tvEx1 = v.findViewById(R.id.tvExplain1);
            TextView tvEx2 = v.findViewById(R.id.tvExplain2);
            TextView tvEx3 = v.findViewById(R.id.tvExplain3);

            ImageView btnSpeak = v.findViewById(R.id.btnSpeak);

            // Tagalog 1
            if (hasText(item.tagalog1)) {
                if (item.tagalog1Align != null || item.tagalog1Style != null) {
                    applyStyledVisible(
                            tvT1,
                            item.tagalog1,
                            item.tagalog1Align != null ? item.tagalog1Align : Align.CENTER,
                            item.tagalog1Style != null ? item.tagalog1Style : FontStyle.REGULAR
                    );

                    if (item.tagalog1SizeSp != null) {
                        applyTextSize(tvT1, item.tagalog1SizeSp);
                    } else {
                        tvT1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f);
                    }
                } else {
                    setTextVisible(tvT1, item.tagalog1);
                    //if (item.tagalog1SizeSp != null) applyTextSize(tvT1, item.tagalog1SizeSp); // optional
                    applyTextSize(tvT1, item.tagalog1SizeSp);
                }
            }

            // English 1
            if (hasText(item.english1)) {
                if (item.english1Align != null || item.english1Style != null) {
                    applyStyledVisible(
                            tvE1,
                            item.english1,
                            item.english1Align != null ? item.english1Align : Align.CENTER,
                            item.english1Style != null ? item.english1Style : FontStyle.REGULAR
                    );
                    applyTextSize(tvE1, item.english1SizeSp);
                } else {
                    setTextVisible(tvE1, item.english1);
                }
            }

            // English 2 (no styling fields currently exposed -> plain)
            //setTextVisible(tvE2, item.english2);

            // Explanations: plain text only
            //setTextVisible(tvEx1, item.explanation1);
            //setTextVisible(tvEx2, item.explanation2);

            // Speaker
            if (btnSpeak != null && item.showSpeaker && hasText(item.tagalog1)) {
                btnSpeak.setVisibility(View.VISIBLE);
                btnSpeak.setOnClickListener(x -> speakClick.onSpeak(item.tagalog1));
            } else if (btnSpeak != null) {
                btnSpeak.setVisibility(View.GONE);
                btnSpeak.setOnClickListener(null);
            }
            setTextVisible(tvT2, item.tagalog2);
            setTextVisible(tvT3, item.tagalog3);
            setTextVisible(tvE3, item.english3);
            setTextVisible(tvEx1, item.explanation1);
            setTextVisible(tvEx2, item.explanation2);
            setTextVisible(tvEx3, item.explanation3);
        }

        private static void setTextVisible(@Nullable TextView tv, @Nullable String text) {
            if (tv == null) return;

            if (text != null && !text.trim().isEmpty()) {
                tv.setText(text);
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setText("");
                tv.setVisibility(View.GONE);
            }
        }


        private static void applyStyledVisible(
                @Nullable TextView tv,
                @NonNull String text,
                @NonNull Align align,
                @NonNull FontStyle style
        ) {
            if (tv == null) return;

            tv.setText(text);
            tv.setVisibility(View.VISIBLE);

            switch (align) {
                case LEFT:
                    tv.setGravity(Gravity.START);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    break;
                case RIGHT:
                    tv.setGravity(Gravity.END);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    break;
                case CENTER:
                default:
                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    break;
            }

            final int tf;
            switch (style) {
                case BOLD: tf = Typeface.BOLD; break;
                case ITALIC: tf = Typeface.ITALIC; break;
                case BOLD_ITALIC: tf = Typeface.BOLD_ITALIC; break;
                case REGULAR:
                default: tf = Typeface.NORMAL; break;
            }
            //tv.setTypeface(tv.getTypeface(), tf);
            tv.setTypeface(null, tf);
        }
    }

    // --------------------------------------------------
    // RESET HELPERS
    // --------------------------------------------------

    private static void resetCardToWhite(@NonNull MaterialCardView card) {
        card.setCardBackgroundColor(ContextCompat.getColor(card.getContext(), R.color.white));
    }

    private static void resetTitleCardToDefaults(@NonNull MaterialCardView card, @NonNull View row) {
        resetCardToWhite(card);
        resetRowToXmlDefaults(row);

        TextView tvT1 = row.findViewById(R.id.tvTagalog1);
        if (tvT1 != null) {
            tvT1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f);
        }
    }

    private static void resetRowToXmlDefaults(@NonNull View v) {

        //View tagalogBlock = v.findViewById(R.id.tagalogBlock);
        TextView tvT1 = v.findViewById(R.id.tvTagalog1);
        TextView tvT2 = v.findViewById(R.id.tvTagalog2);
        TextView tvT3 = v.findViewById(R.id.tvTagalog3);

        //View englishBlock = v.findViewById(R.id.englishBlock);
        TextView tvE1 = v.findViewById(R.id.tvEnglish1);
        TextView tvE2 = v.findViewById(R.id.tvEnglish2);
        TextView tvE3 = v.findViewById(R.id.tvEnglish3);

        //View explainBlock = v.findViewById(R.id.explainBlock);
        TextView tvX1 = v.findViewById(R.id.tvExplain1);
        TextView tvX2 = v.findViewById(R.id.tvExplain2);
        TextView tvX3 = v.findViewById(R.id.tvExplain3);

        ImageView btnSpeak = v.findViewById(R.id.btnSpeak);

        clearAndGone(tvT1);
        clearAndGone(tvT2);
        clearAndGone(tvT3);

        clearAndGone(tvE1);
        clearAndGone(tvE2);
        clearAndGone(tvE3);

        clearAndGone(tvX1);
        clearAndGone(tvX2);
        clearAndGone(tvX3);

        if (btnSpeak != null) {
            btnSpeak.setVisibility(View.GONE);
            btnSpeak.setOnClickListener(null);
        }

        if (tvT1 != null) {
            tvT1.setTypeface(tvT1.getTypeface(), Typeface.BOLD);
            tvT1.setGravity(Gravity.CENTER_HORIZONTAL);
            tvT1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        if (tvE1 != null) {
            tvE1.setTypeface(tvE1.getTypeface(), Typeface.ITALIC);
            tvE1.setGravity(Gravity.CENTER_HORIZONTAL);
            tvE1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }
    private static void applyTextSize(@Nullable TextView tv, @Nullable String sizeSp) {
        if (tv == null || sizeSp == null) return;
        try {
            float sp = Float.parseFloat(sizeSp.replace("sp", "").trim());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        } catch (Exception ignored) {}
    }

    private static void clearAndGone(@Nullable TextView tv) {
        if (tv == null) return;
        tv.setText("");
        tv.setVisibility(View.GONE);
    }

    private static boolean hasText(@Nullable String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static int dp(@NonNull View v, int dp) {
        return (int) (dp * v.getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void applySpeakerLane(@NonNull View v, boolean showLane) {
        View speakerBlock = v.findViewById(R.id.speakerBlock);
        View tagalogBlock = v.findViewById(R.id.tagalogBlock);
        View englishBlock = v.findViewById(R.id.englishBlock);
        View explainBlock = v.findViewById(R.id.explainBlock);

        if (speakerBlock != null) {
            speakerBlock.setVisibility(showLane ? View.VISIBLE : View.GONE);
        }

        // When lane is gone, remove the fake "reserved speaker space" paddings
        int end32 = showLane ? dp(v, 32) : 0;
        int end8  = showLane ? dp(v, 8)  : 0;

        if (englishBlock != null) {
            englishBlock.setPadding(
                    englishBlock.getPaddingLeft(),
                    englishBlock.getPaddingTop(),
                    end32,
                    englishBlock.getPaddingBottom()
            );
        }

        if (explainBlock != null) {
            explainBlock.setPadding(
                    explainBlock.getPaddingLeft(),
                    explainBlock.getPaddingTop(),
                    end32,
                    explainBlock.getPaddingBottom()
            );
        }

        if (tagalogBlock != null) {
            if (showLane) {
                tagalogBlock.setPadding(
                        tagalogBlock.getPaddingLeft(),
                        tagalogBlock.getPaddingTop(),
                        end8,
                        tagalogBlock.getPaddingBottom()
                );
            } else {
                // hard reset so the title is truly centered
                tagalogBlock.setPadding(
                        0,
                        tagalogBlock.getPaddingTop(),
                        0,
                        tagalogBlock.getPaddingBottom()
                );
            }
        }

    }

}
