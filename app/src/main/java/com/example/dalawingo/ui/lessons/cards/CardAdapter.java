package com.example.dalawingo.ui.lessons.cards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dalawingo.R;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int[] TITLE_BG_CYCLE = {R.color.pastel_red, R.color.pastel_orange, R.color.pastel_yellow, R.color.pastel_green, R.color.pastel_blue, R.color.pastel_purple, R.color.pastel_pink};
    private static final int VIEW_IMAGE = 2;
    private static final int VIEW_PHRASES = 1;
    private static final int VIEW_TITLE = 3;
    private final List<CardItem> items = new ArrayList();
    private final OnSpeakClick speakClick;

    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum FontStyle {
        REGULAR,
        BOLD,
        ITALIC,
        BOLD_ITALIC
    }

    public interface OnSpeakClick {
        void onSpeak(String str);
    }

    public CardAdapter(OnSpeakClick speakClick) {
        this.speakClick = speakClick;
    }

    public void submitList(List<CardItem> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        CardItem it = this.items.get(position);
        if (it.titleText != null) {
            return 3;
        }
        return it.imageRes != null ? 2 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.items.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == 3) {
            return new TitleVH(inf.inflate(R.layout.card_phrases, parent, false));
        }
        if (viewType == 2) {
            return new ImageVH(inf.inflate(R.layout.card_phrases, parent, false));
        }
        return new PhraseGroupVH(inf.inflate(R.layout.card_phrases, parent, false), this.speakClick);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CardItem item = this.items.get(position);
        if (holder instanceof TitleVH) {
            ((TitleVH) holder).bind(item.titleText, getTitleIndex(position));
        } else if (holder instanceof ImageVH) {
            ((ImageVH) holder).bind(item.imageRes);
        } else {
            ((PhraseGroupVH) holder).bind(item.phraseItems);
        }
    }

    private int getTitleIndex(int adapterPos) {
        int idx = 0;
        for (int i = 0; i < adapterPos; i++) {
            if (this.items.get(i).titleText != null) {
                idx++;
            }
        }
        return idx;
    }

    static final class TitleVH extends RecyclerView.ViewHolder {
        private final MaterialCardView card;
        private final LinearLayout container;
        private final View row;
        private final TextView tvTagalog1;

        TitleVH(View itemView) {
            super(itemView);
            this.card = (MaterialCardView) itemView;
            this.container = (LinearLayout) itemView.findViewById(R.id.cardContainer);
            this.container.removeAllViews();
            this.row = LayoutInflater.from(itemView.getContext()).inflate(R.layout.card_phrase_item, (ViewGroup) this.container, false);
            this.container.addView(this.row);
            this.tvTagalog1 = (TextView) this.row.findViewById(R.id.tvTagalog1);
        }

        void bind(String titleText, int titleIndex) {
            CardAdapter.resetTitleCardToDefaults(this.card, this.row);
            CardAdapter.applySpeakerLane(this.row, false);
            if (this.tvTagalog1 != null) {
                this.tvTagalog1.setText(titleText);
                this.tvTagalog1.setVisibility(0);
                float sizeSp = titleIndex == 0 ? 30.0f : 20.0f;
                this.tvTagalog1.setTextSize(2, sizeSp);
                this.tvTagalog1.setTypeface(this.tvTagalog1.getTypeface(), 1);
                this.tvTagalog1.setGravity(1);
                this.tvTagalog1.setTextAlignment(4);
            }
            int colorRes = CardAdapter.TITLE_BG_CYCLE[titleIndex % CardAdapter.TITLE_BG_CYCLE.length];
            this.card.setCardBackgroundColor(ContextCompat.getColor(this.itemView.getContext(), colorRes));
        }
    }

    class ImageVH extends RecyclerView.ViewHolder {
        private final LinearLayout container;

        ImageVH(View itemView) {
            super(itemView);
            this.container = (LinearLayout) itemView.findViewById(R.id.cardContainer);
        }

        void bind(Integer imageRes) {
            this.container.removeAllViews();
            ImageView iv = new ImageView(this.itemView.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
            iv.setLayoutParams(lp);
            iv.setAdjustViewBounds(true);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setImageResource(imageRes.intValue());
            this.container.addView(iv);
        }
    }

    static final class PhraseGroupVH extends RecyclerView.ViewHolder {
        private final MaterialCardView card;
        private final LinearLayout container;
        private final OnSpeakClick speakClick;

        PhraseGroupVH(View itemView, OnSpeakClick speakClick) {
            super(itemView);
            this.card = (MaterialCardView) itemView;
            this.speakClick = speakClick;
            this.container = (LinearLayout) itemView.findViewById(R.id.cardContainer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        void bind(List<PhraseItem> phrases) {
            CardAdapter.resetCardToWhite(this.card);
            this.container.removeAllViews();
            if (phrases == null || phrases.isEmpty()) {
                return;
            }
            LayoutInflater inf = LayoutInflater.from(this.itemView.getContext());
            for (PhraseItem pi : phrases) {
                if (pi != null) {
                    if (pi.itemType == PhraseItem.ItemType.INFO) {
                        View infoRow = inf.inflate(R.layout.card_phrase_item_information, (ViewGroup) this.container, false);
                        bindInfoRow(infoRow, pi);
                        this.container.addView(infoRow);
                    } else if (pi.itemType == PhraseItem.ItemType.PYRAMID) {
                        View pv = inf.inflate(R.layout.card_phrase_item_pyramid, (ViewGroup) this.container, false);
                        PhraseItemPyramid.phraseItemPyramid(pv, this.speakClick, pi.py1A, pi.py1AStyle, pi.py2A, pi.py2B, pi.py2C, pi.py3A, pi.py3B, pi.py3C, pi.py4, pi.py4Style);
                        this.container.addView(pv);
                    } else if (pi.itemType == PhraseItem.ItemType.CHART) {
                        View cv = inf.inflate(R.layout.card_phrase_item_chart, (ViewGroup) this.container, false);
                        PhraseItemCharts.phraseItemChart(cv, pi.chartHeader1, pi.chartHeader2, pi.chartHeader3, pi.chartTagalog, pi.chartEnglish);
                        this.container.addView(cv);
                    } else if (pi.itemType == PhraseItem.ItemType.IMAGE) {
                        if (pi.imageResId != null) {
                            ImageView iv = new ImageView(this.itemView.getContext());
                            iv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
                            iv.setAdjustViewBounds(true);
                            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            iv.setImageResource(pi.imageResId.intValue());
                            this.container.addView(iv);
                        }
                    } else if (pi.itemType == PhraseItem.ItemType.TABLE1) {
                        View tv = inf.inflate(R.layout.card_phrase_item_table1, (ViewGroup) this.container, false);
                        PhraseItemCharts.phraseItemTable1(tv, pi.table1);
                        this.container.addView(tv);
                    } else if (pi.itemType == PhraseItem.ItemType.TABLE2) {
                        View tv2 = inf.inflate(R.layout.card_phrase_item_table2, (ViewGroup) this.container, false);
                        PhraseItemCharts.phraseItemTable2(tv2, this.speakClick, pi.table2);
                        this.container.addView(tv2);
                    } else {
                        Object obj = null;
                        if (pi.itemType == PhraseItem.ItemType.TABLE3) {
                            View tv3 = inf.inflate(R.layout.card_phrase_item_table3, (ViewGroup) this.container, false);
                            String[] row1 = (pi.table3 == null || pi.table3.length <= 0) ? null : pi.table3[0];
                            String[] row2 = (pi.table3 == null || pi.table3.length <= 1) ? null : pi.table3[1];
                            if (pi.table3 != null && pi.table3.length > 2) {
                                obj = pi.table3[2];
                            }
                            PhraseItemCharts.phraseItemTable3(tv3, pi.table3RootWord, pi.table3RootMeaning, row1, row2, obj);
                            this.container.addView(tv3);
                        } else if (pi.itemType == PhraseItem.ItemType.TABLE4) {
                            View tv4 = inf.inflate(R.layout.card_phrase_item_table4, (ViewGroup) this.container, false);
                            Object[] objArr = pi.table4Headers;
                            String str = (objArr == null || objArr.length <= 0) ? 0 : objArr[0];
                            String str2 = (objArr == null || objArr.length <= 1) ? 0 : objArr[1];
                            String str3 = (objArr == null || objArr.length <= 2) ? 0 : objArr[2];
                            if (objArr != null && objArr.length > 3) {
                                obj = objArr[3];
                            }
                            PhraseItemCharts.phraseItemTable4(tv4, str, str2, str3, obj, pi.table4);
                            this.container.addView(tv4);
                        } else if (pi.itemType == PhraseItem.ItemType.TABLE5) {
                            View tv5 = inf.inflate(R.layout.card_phrase_item_table5, (ViewGroup) this.container, false);
                            PhraseItemCharts.phraseItemTable5(tv5, pi.table5Headers, pi.table5);
                            this.container.addView(tv5);
                        } else {
                            View row = inf.inflate(R.layout.card_phrase_item, (ViewGroup) this.container, false);
                            bindRow(row, pi);
                            this.container.addView(row);
                        }
                    }
                }
            }
        }

        private void bindInfoRow(View v, final PhraseItem item) {
            TextView tvTagalog = (TextView) v.findViewById(R.id.tvTagalog);
            TextView tvEnglish = (TextView) v.findViewById(R.id.tvEnglish);
            ImageView btnSpeak = (ImageView) v.findViewById(R.id.btnSpeak);
            if (tvTagalog != null) {
                tvTagalog.setText(item.tagalog1 != null ? item.tagalog1 : "");
            }
            if (tvEnglish != null) {
                tvEnglish.setText(item.english1 != null ? item.english1 : "");
            }
            if (btnSpeak != null) {
                if (item.showSpeaker && CardAdapter.hasText(item.tagalog1)) {
                    btnSpeak.setVisibility(0);
                    btnSpeak.setOnClickListener(new View.OnClickListener() { // from class: com.example.dalawingo.ui.lessons.cards.CardAdapter$PhraseGroupVH$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            this.f$0.m2628x3f74804e(item, view);
                        }
                    });
                } else {
                    btnSpeak.setVisibility(8);
                    btnSpeak.setOnClickListener(null);
                }
            }
        }

        /* JADX INFO: renamed from: lambda$bindInfoRow$0$com-example-dalawingo-ui-lessons-cards-CardAdapter$PhraseGroupVH, reason: not valid java name */
        /* synthetic */ void m2628x3f74804e(PhraseItem item, View x) {
            this.speakClick.onSpeak(item.tagalog1);
        }

        private void bindRow(View v, final PhraseItem item) {
            CardAdapter.resetRowToXmlDefaults(v);
            boolean showLane = item.showSpeaker && CardAdapter.hasText(item.tagalog1);
            CardAdapter.applySpeakerLane(v, showLane);
            TextView tvT1 = (TextView) v.findViewById(R.id.tvTagalog1);
            TextView tvT2 = (TextView) v.findViewById(R.id.tvTagalog2);
            TextView tvT3 = (TextView) v.findViewById(R.id.tvTagalog3);
            TextView tvE1 = (TextView) v.findViewById(R.id.tvEnglish1);
            TextView tvE3 = (TextView) v.findViewById(R.id.tvEnglish3);
            TextView tvEx1 = (TextView) v.findViewById(R.id.tvExplain1);
            TextView tvEx2 = (TextView) v.findViewById(R.id.tvExplain2);
            TextView tvEx3 = (TextView) v.findViewById(R.id.tvExplain3);
            ImageView btnSpeak = (ImageView) v.findViewById(R.id.btnSpeak);
            if (CardAdapter.hasText(item.tagalog1)) {
                if (item.tagalog1Align != null || item.tagalog1Style != null) {
                    applyStyledVisible(tvT1, item.tagalog1, item.tagalog1Align != null ? item.tagalog1Align : Align.CENTER, item.tagalog1Style != null ? item.tagalog1Style : FontStyle.REGULAR);
                    if (item.tagalog1SizeSp != null) {
                        CardAdapter.applyTextSize(tvT1, item.tagalog1SizeSp);
                    } else {
                        tvT1.setTextSize(2, 19.0f);
                    }
                } else {
                    setTextVisible(tvT1, item.tagalog1);
                    CardAdapter.applyTextSize(tvT1, item.tagalog1SizeSp);
                }
            }
            if (CardAdapter.hasText(item.english1)) {
                if (item.english1Align != null || item.english1Style != null) {
                    applyStyledVisible(tvE1, item.english1, item.english1Align != null ? item.english1Align : Align.CENTER, item.english1Style != null ? item.english1Style : FontStyle.REGULAR);
                    CardAdapter.applyTextSize(tvE1, item.english1SizeSp);
                } else {
                    setTextVisible(tvE1, item.english1);
                }
            }
            if (btnSpeak != null && item.showSpeaker && CardAdapter.hasText(item.tagalog1)) {
                btnSpeak.setVisibility(0);
                btnSpeak.setOnClickListener(new View.OnClickListener() { // from class: com.example.dalawingo.ui.lessons.cards.CardAdapter$PhraseGroupVH$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.m2629x5f713f1f(item, view);
                    }
                });
            } else if (btnSpeak != null) {
                btnSpeak.setVisibility(8);
                btnSpeak.setOnClickListener(null);
            }
            setTextVisible(tvT2, item.tagalog2);
            setTextVisible(tvT3, item.tagalog3);
            setTextVisible(tvE3, item.english3);
            setTextVisible(tvEx1, item.explanation1);
            setTextVisible(tvEx2, item.explanation2);
            setTextVisible(tvEx3, item.explanation3);
        }

        /* JADX INFO: renamed from: lambda$bindRow$1$com-example-dalawingo-ui-lessons-cards-CardAdapter$PhraseGroupVH, reason: not valid java name */
        /* synthetic */ void m2629x5f713f1f(PhraseItem item, View x) {
            this.speakClick.onSpeak(item.tagalog1);
        }

        private static void setTextVisible(TextView tv, String text) {
            if (tv == null) {
                return;
            }
            if (text != null && !text.trim().isEmpty()) {
                tv.setText(text);
                tv.setVisibility(0);
            } else {
                tv.setText("");
                tv.setVisibility(8);
            }
        }

        private static void applyStyledVisible(TextView tv, String text, Align align, FontStyle style) {
            int tf;
            if (tv == null) {
                return;
            }
            tv.setText(text);
            tv.setVisibility(0);
            switch (align) {
                case LEFT:
                    tv.setGravity(GravityCompat.START);
                    tv.setTextAlignment(5);
                    break;
                case CENTER:
                default:
                    tv.setGravity(1);
                    tv.setTextAlignment(4);
                    break;
                case RIGHT:
                    tv.setGravity(GravityCompat.END);
                    tv.setTextAlignment(6);
                    break;
            }
            switch (style.ordinal()) {
                case 1:
                    tf = 1;
                    break;
                case 2:
                    tf = 2;
                    break;
                case 3:
                    tf = 3;
                    break;
                default:
                    tf = 0;
                    break;
            }
            tv.setTypeface(null, tf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetCardToWhite(MaterialCardView card) {
        card.setCardBackgroundColor(ContextCompat.getColor(card.getContext(), R.color.white));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetTitleCardToDefaults(MaterialCardView card, View row) {
        resetCardToWhite(card);
        resetRowToXmlDefaults(row);
        TextView tvT1 = (TextView) row.findViewById(R.id.tvTagalog1);
        if (tvT1 != null) {
            tvT1.setTextSize(2, 19.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void resetRowToXmlDefaults(View v) {
        TextView tvT1 = (TextView) v.findViewById(R.id.tvTagalog1);
        TextView tvT2 = (TextView) v.findViewById(R.id.tvTagalog2);
        TextView tvT3 = (TextView) v.findViewById(R.id.tvTagalog3);
        TextView tvE1 = (TextView) v.findViewById(R.id.tvEnglish1);
        TextView tvE2 = (TextView) v.findViewById(R.id.tvEnglish2);
        TextView tvE3 = (TextView) v.findViewById(R.id.tvEnglish3);
        TextView tvX1 = (TextView) v.findViewById(R.id.tvExplain1);
        TextView tvX2 = (TextView) v.findViewById(R.id.tvExplain2);
        TextView tvX3 = (TextView) v.findViewById(R.id.tvExplain3);
        ImageView btnSpeak = (ImageView) v.findViewById(R.id.btnSpeak);
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
            btnSpeak.setVisibility(8);
            btnSpeak.setOnClickListener(null);
        }
        if (tvT1 != null) {
            tvT1.setTypeface(tvT1.getTypeface(), 1);
            tvT1.setGravity(1);
            tvT1.setTextAlignment(4);
        }
        if (tvE1 != null) {
            tvE1.setTypeface(tvE1.getTypeface(), 2);
            tvE1.setGravity(1);
            tvE1.setTextAlignment(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyTextSize(TextView tv, String sizeSp) {
        if (tv == null || sizeSp == null) {
            return;
        }
        try {
            float sp = Float.parseFloat(sizeSp.replace("sp", "").trim());
            tv.setTextSize(2, sp);
        } catch (Exception e) {
        }
    }

    private static void clearAndGone(TextView tv) {
        if (tv == null) {
            return;
        }
        tv.setText("");
        tv.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasText(String s) {
        return (s == null || s.trim().isEmpty()) ? false : true;
    }

    private static int dp(View v, int dp) {
        return (int) ((dp * v.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applySpeakerLane(View v, boolean showLane) {
        View speakerBlock = v.findViewById(R.id.speakerBlock);
        View tagalogBlock = v.findViewById(R.id.tagalogBlock);
        View englishBlock = v.findViewById(R.id.englishBlock);
        View explainBlock = v.findViewById(R.id.explainBlock);
        if (speakerBlock != null) {
            speakerBlock.setVisibility(showLane ? 0 : 8);
        }
        int end32 = showLane ? dp(v, 32) : 0;
        int end8 = showLane ? dp(v, 8) : 0;
        if (englishBlock != null) {
            englishBlock.setPadding(englishBlock.getPaddingLeft(), englishBlock.getPaddingTop(), end32, englishBlock.getPaddingBottom());
        }
        if (explainBlock != null) {
            explainBlock.setPadding(explainBlock.getPaddingLeft(), explainBlock.getPaddingTop(), end32, explainBlock.getPaddingBottom());
        }
        if (tagalogBlock != null) {
            if (showLane) {
                tagalogBlock.setPadding(tagalogBlock.getPaddingLeft(), tagalogBlock.getPaddingTop(), end8, tagalogBlock.getPaddingBottom());
            } else {
                tagalogBlock.setPadding(0, tagalogBlock.getPaddingTop(), 0, tagalogBlock.getPaddingBottom());
            }
        }
    }
}
