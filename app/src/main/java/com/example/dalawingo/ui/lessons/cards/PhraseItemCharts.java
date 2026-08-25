// PhraseItemCharts.java  (REWRITE)
// Put this file at:
// app/src/main/java/com/example/dalawingo/ui/lessons/cards/PhraseItemCharts.java
package com.example.dalawingo.ui.lessons.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dalawingo.R;

/**
 * ONE place for:
 * 1) "factory" helpers that RETURN PhraseItem (so your Lesson files stay clean)
 * 2) binders that APPLY data onto XML views (CardAdapter uses these)
 *
 * IMPORTANT:
 * - These factories intentionally live here (NOT in PhraseItem).
 * - PhraseItem must expose package-private constructors used below (remove "private").
 */
public final class PhraseItemCharts {

    private PhraseItemCharts() {}

    // ==========================================================
    // FACTORIES (return PhraseItem)  <-- what you call in Lesson files
    // ==========================================================

    // ==========================================================
    // BINDERS (CardAdapter calls these)
    // ==========================================================

    // --------------------------
    // CHART (card_phrase_item_chart.xml)
    // --------------------------
    public static void phraseItemChart(
            @NonNull View root,
            @Nullable String header1,
            @Nullable String header2,
            @Nullable String header3,
            @Nullable String[][] chartTagalog,
            @Nullable String[][] chartEnglish
    ) {
        // Header
        TextView h1 = root.findViewById(R.id.headerCol1);
        TextView h2 = root.findViewById(R.id.headerCol2);
        TextView h3 = root.findViewById(R.id.headerCol3);

        setTextOrGone(h1, header1);
        setTextOrGone(h2, header2);
        setTextOrGone(h3, header3);

        View headerRow = root.findViewById(R.id.rowHeader);
        boolean anyHeader = hasText(header1) || hasText(header2) || hasText(header3);
        if (headerRow != null) headerRow.setVisibility(anyHeader ? View.VISIBLE : View.GONE);

        // Rows 1..5, Cols 1..3
        for (int r = 1; r <= 5; r++) {
            boolean anyInRow = false;

            for (int c = 1; c <= 3; c++) {
                String t = safeCell(chartTagalog, r - 1, c - 1);
                String e = safeCell(chartEnglish, r - 1, c - 1);

                int cellId = idByName(root, "r" + r + "c" + c);
                int tId    = idByName(root, "r" + r + "c" + c + "Tagalog");
                int eId    = idByName(root, "r" + r + "c" + c + "English");

                View cell = (cellId != 0) ? root.findViewById(cellId) : null;
                TextView tvT = (tId != 0) ? root.findViewById(tId) : null;
                TextView tvE = (eId != 0) ? root.findViewById(eId) : null;

                boolean anyInCell = hasText(t) || hasText(e);

                setTextOrGone(tvT, t);
                setTextOrGone(tvE, e);

                if (cell != null) cell.setVisibility(anyInCell ? View.VISIBLE : View.GONE);
                if (anyInCell) anyInRow = true;
            }

            int rowId = idByName(root, "row" + r);
            View rowView = (rowId != 0) ? root.findViewById(rowId) : null;
            if (rowView != null) rowView.setVisibility(anyInRow ? View.VISIBLE : View.GONE);
        }
    }

    // --------------------------
    // TABLE 1 (card_phrase_item_table1.xml)
    // table[r][c] => r(1..10)c(1..4)  (TextViews rNcM)
    // --------------------------
    public static void phraseItemTable1(@NonNull View root, @Nullable String[][] table) {
        bindGenericTable(root, table, 10, 4);
    }

    // --------------------------
    // TABLE 2 (card_phrase_item_table2.xml)
    // table[r][0]=left bold col, table[r][1]=main col
    // Speakers: rNSpeaker speaks c2 if present else c1
    // --------------------------
    public static void phraseItemTable2(
            @NonNull View root,
            @NonNull CardAdapter.OnSpeakClick speakClick,
            @Nullable String[][] table
    ) {
        for (int r = 1; r <= 10; r++) {
            String[] data = safeRow(table, r - 1);
            String c1 = safeCell(data, 0);
            String c2 = safeCell(data, 1);

            boolean any = hasText(c1) || hasText(c2);

            TextView tv1 = findTvByName(root, "r" + r + "c1");
            TextView tv2 = findTvByName(root, "r" + r + "c2");
            ImageView sp = (ImageView) findByName(root, "r" + r + "Speaker");

            if (!any) {
                setTextGone(tv1);
                setTextGone(tv2);
                if (sp != null) {
                    sp.setVisibility(View.GONE);
                    sp.setOnClickListener(null);
                }
                continue;
            }

            setTextOrGone(tv1, c1);
            setTextOrGone(tv2, c2);

            if (sp != null) {
                sp.setVisibility(View.VISIBLE);
                final String speakText = hasText(c2) ? c2 : c1;
                sp.setOnClickListener(v -> speakClick.onSpeak(speakText));
            }
        }
    }

    // --------------------------
    // TABLE 3 (card_phrase_item_table3.xml)
    // header: tvRootWord + tvRootMeaning
    // rows: 3 rows
    // row data: [label, a, b, c, english]
    // --------------------------
    public static void phraseItemTable3(
            @NonNull View root,
            @Nullable String rootWord,
            @Nullable String rootMeaning,
            @Nullable String[][] rows // rows[r][0..4] = label,a,b,c,eng
    ) {
        String[] r1 = (rows != null && rows.length > 0) ? rows[0] : null;
        String[] r2 = (rows != null && rows.length > 1) ? rows[1] : null;
        String[] r3 = (rows != null && rows.length > 2) ? rows[2] : null;

        phraseItemTable3(root, rootWord, rootMeaning, r1, r2, r3);
    }

    public static void phraseItemTable3(
            @NonNull View root,
            @Nullable String rootWord,
            @Nullable String rootMeaning,
            @Nullable String[] row1,
            @Nullable String[] row2,
            @Nullable String[] row3
    ) {
        TextView tvRootWord = root.findViewById(R.id.tvRootWord);
        TextView tvRootMeaning = root.findViewById(R.id.tvRootMeaning);

        setTextOrGone(tvRootWord, rootWord);
        setTextOrGone(tvRootMeaning, rootMeaning);

        bindTable3Row(root, 1, row1);
        bindTable3Row(root, 2, row2);
        bindTable3Row(root, 3, row3);
    }

    // --------------------------
    // TABLE 4 (card_phrase_item_table4.xml)
    // header: h1..h4
    // rows: up to 6
    // row data: [rootWord, rootMeaning, pastWord, pastMeaning, presWord, presMeaning, futWord, futMeaning]
    // --------------------------
    public static void phraseItemTable4(
            @NonNull View root,
            @Nullable String h1,
            @Nullable String h2,
            @Nullable String h3,
            @Nullable String h4,
            @Nullable String[][] rows
    ) {
        setTextOrGone(root.findViewById(R.id.h1), h1);
        setTextOrGone(root.findViewById(R.id.h2), h2);
        setTextOrGone(root.findViewById(R.id.h3), h3);
        setTextOrGone(root.findViewById(R.id.h4), h4);

        for (int r = 1; r <= 6; r++) {
            String[] data = safeRow(rows, r - 1);

            String rWord  = safeCell(data, 0);
            String rMean  = safeCell(data, 1);
            String pWord  = safeCell(data, 2);
            String pMean  = safeCell(data, 3);
            String prWord = safeCell(data, 4);
            String prMean = safeCell(data, 5);
            String fWord  = safeCell(data, 6);
            String fMean  = safeCell(data, 7);

            boolean any =
                    hasText(rWord) || hasText(rMean) ||
                            hasText(pWord) || hasText(pMean) ||
                            hasText(prWord) || hasText(prMean) ||
                            hasText(fWord) || hasText(fMean);

            View c1 = findByName(root, "r" + r + "c1");
            View c2 = findByName(root, "r" + r + "c2");
            View c3 = findByName(root, "r" + r + "c3");
            View c4 = findByName(root, "r" + r + "c4");

            if (c1 != null) c1.setVisibility(any ? View.VISIBLE : View.GONE);
            if (c2 != null) c2.setVisibility(any ? View.VISIBLE : View.GONE);
            if (c3 != null) c3.setVisibility(any ? View.VISIBLE : View.GONE);
            if (c4 != null) c4.setVisibility(any ? View.VISIBLE : View.GONE);

            setTextOrGone(findTvByName(root, "r" + r + "c1a"), rWord);
            setTextOrGone(findTvByName(root, "r" + r + "c1b"), rMean);

            setTextOrGone(findTvByName(root, "r" + r + "c2a"), pWord);
            setTextOrGone(findTvByName(root, "r" + r + "c2b"), pMean);

            setTextOrGone(findTvByName(root, "r" + r + "c3a"), prWord);
            setTextOrGone(findTvByName(root, "r" + r + "c3b"), prMean);

            setTextOrGone(findTvByName(root, "r" + r + "c4a"), fWord);
            setTextOrGone(findTvByName(root, "r" + r + "c4b"), fMean);
        }
    }

    // ==========================================================
    // INTERNAL HELPERS
    // ==========================================================

    private static void bindGenericTable(
            @NonNull View root,
            @Nullable String[][] table,
            int maxRows,
            int maxCols
    ) {
        for (int r = 1; r <= maxRows; r++) {
            String[] data = safeRow(table, r - 1);

            boolean anyInRow = false;
            for (int c = 1; c <= maxCols; c++) {
                if (hasText(safeCell(data, c - 1))) {
                    anyInRow = true;
                    break;
                }
            }

            for (int c = 1; c <= maxCols; c++) {
                TextView tv = findTvByName(root, "r" + r + "c" + c);
                if (!anyInRow) setTextGone(tv);
                else setTextOrGone(tv, safeCell(data, c - 1));
            }
        }
    }

    private static void bindTable3Row(@NonNull View root, int r, @Nullable String[] data) {
        String label = safeCell(data, 0);
        String a     = safeCell(data, 1);
        String b     = safeCell(data, 2);
        String c     = safeCell(data, 3);
        String eng   = safeCell(data, 4);

        boolean any = hasText(label) || hasText(a) || hasText(b) || hasText(c) || hasText(eng);

        TextView c1 = findTvByName(root, "r" + r + "c1");
        LinearLayout c2 = (LinearLayout) findByName(root, "r" + r + "c2");
        TextView c2a = findTvByName(root, "r" + r + "c2a");
        TextView c2b = findTvByName(root, "r" + r + "c2b");
        TextView c2c = findTvByName(root, "r" + r + "c2c");
        TextView c3 = findTvByName(root, "r" + r + "c3");

        if (!any) {
            setTextGone(c1);
            if (c2 != null) c2.setVisibility(View.GONE);
            setTextGone(c2a); setTextGone(c2b); setTextGone(c2c);
            setTextGone(c3);
            return;
        }

        setTextOrGone(c1, label);

        boolean anyParts = hasText(a) || hasText(b) || hasText(c);
        if (c2 != null) c2.setVisibility(anyParts ? View.VISIBLE : View.GONE);
        setTextOrGone(c2a, a);
        setTextOrGone(c2b, b);
        setTextOrGone(c2c, c);

        setTextOrGone(c3, eng);
    }

    private static int idByName(@NonNull View root, @NonNull String name) {
        return root.getResources().getIdentifier(name, "id", root.getContext().getPackageName());
    }

    @Nullable
    private static View findByName(@NonNull View root, @NonNull String name) {
        int id = idByName(root, name);
        return id != 0 ? root.findViewById(id) : null;
    }

    @Nullable
    private static TextView findTvByName(@NonNull View root, @NonNull String name) {
        View v = findByName(root, name);
        return (v instanceof TextView) ? (TextView) v : null;
    }

    @NonNull
    private static String safeCell(@Nullable String[][] arr, int r, int c) {
        if (arr == null) return "";
        if (r < 0 || r >= arr.length) return "";
        String[] row = arr[r];
        if (row == null) return "";
        if (c < 0 || c >= row.length) return "";
        return row[c] != null ? row[c] : "";
    }

    @NonNull
    private static String[] safeRow(@Nullable String[][] table, int r) {
        if (table == null) return new String[0];
        if (r < 0 || r >= table.length) return new String[0];
        String[] row = table[r];
        return row != null ? row : new String[0];
    }

    @NonNull
    private static String safeCell(@Nullable String[] row, int c) {
        if (row == null) return "";
        if (c < 0 || c >= row.length) return "";
        String s = row[c];
        return s != null ? s : "";
    }

    private static boolean hasText(@Nullable String s) {
        return s != null && s.trim().length() > 0;
    }

    private static void setTextGone(@Nullable TextView tv) {
        if (tv == null) return;
        tv.setText("");
        tv.setVisibility(View.GONE);
    }

    private static void setTextOrGone(@Nullable TextView tv, @Nullable String text) {
        if (tv == null) return;
        if (hasText(text)) {
            tv.setText(text);
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setText("");
            tv.setVisibility(View.GONE);
        }
    }

    // =====================================================
// 2) PhraseItemCharts.java ✅ binder for TABLE5
//    (your table5 XML ids: hPrefix/hFocus/hKeyUse and r1c1..r5c3)
// =====================================================

    public static void phraseItemTable5(
            @NonNull View root,
            @Nullable String[] headers,   // length 3
            @Nullable String[][] rows     // up to 5 rows, 3 cols
    ) {
        // headers
        TextView h1 = root.findViewById(R.id.hPrefix);
        TextView h2 = root.findViewById(R.id.hFocus);
        TextView h3 = root.findViewById(R.id.hKeyUse);

        setTextOrGone(h1, safeCell(headers, 0));
        setTextOrGone(h2, safeCell(headers, 1));
        setTextOrGone(h3, safeCell(headers, 2));

        // rows (your XML has 5 rows)
        for (int r = 1; r <= 5; r++) {
            String[] data = safeRow(rows, r - 1);

            String c1 = safeCell(data, 0);
            String c2 = safeCell(data, 1);
            String c3 = safeCell(data, 2);

            boolean any = hasText(c1) || hasText(c2) || hasText(c3);

            TextView tv1 = findTvByName(root, "r" + r + "c1");
            TextView tv2 = findTvByName(root, "r" + r + "c2");
            TextView tv3 = findTvByName(root, "r" + r + "c3");

            if (!any) {
                setTextGone(tv1);
                setTextGone(tv2);
                setTextGone(tv3);
            } else {
                setTextOrGone(tv1, c1);
                setTextOrGone(tv2, c2);
                setTextOrGone(tv3, c3);
            }
        }
    }

}
