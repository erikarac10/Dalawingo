package com.example.dalawingo.ui.lessons.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dalawingo.R;

/* JADX INFO: loaded from: classes10.dex */
public final class PhraseItemCharts {
    private PhraseItemCharts() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r20v0, types: [android.widget.TextView] */
    public static void phraseItemChart(View root, String header1, String header2, String header3, String[][] chartTagalog, String[][] chartEnglish) {
        TextView h1 = (TextView) root.findViewById(R.id.headerCol1);
        TextView h2 = (TextView) root.findViewById(R.id.headerCol2);
        TextView h3 = (TextView) root.findViewById(R.id.headerCol3);
        setTextOrGone(h1, header1);
        setTextOrGone(h2, header2);
        setTextOrGone(h3, header3);
        View headerRow = root.findViewById(R.id.rowHeader);
        boolean anyHeader = hasText(header1) || hasText(header2) || hasText(header3);
        if (headerRow != null) {
            headerRow.setVisibility(anyHeader ? 0 : 8);
        }
        int r = 1;
        while (r <= 5) {
            boolean anyInRow = false;
            int c = 1;
            while (true) {
                if (c > 3) {
                    break;
                }
                String t = safeCell(chartTagalog, r - 1, c - 1);
                TextView h4 = h1;
                String e = safeCell(chartEnglish, r - 1, c - 1);
                TextView h5 = h2;
                int cellId = idByName(root, "r" + r + "c" + c);
                TextView h6 = h3;
                int tId = idByName(root, "r" + r + "c" + c + "Tagalog");
                int eId = idByName(root, "r" + r + "c" + c + "English");
                View cell = cellId != 0 ? root.findViewById(cellId) : null;
                TextView tvT = tId != 0 ? (TextView) root.findViewById(tId) : null;
                ?? r20 = eId != 0 ? (TextView) root.findViewById(eId) : null;
                boolean anyInCell = hasText(t) || hasText(e);
                setTextOrGone(tvT, t);
                setTextOrGone(r20, e);
                if (cell != null) {
                    cell.setVisibility(anyInCell ? 0 : 8);
                }
                if (anyInCell) {
                    anyInRow = true;
                }
                c++;
                h1 = h4;
                h2 = h5;
                h3 = h6;
            }
            TextView h7 = h1;
            TextView h8 = h2;
            TextView h9 = h3;
            int rowId = idByName(root, "row" + r);
            View rowView = rowId != 0 ? root.findViewById(rowId) : null;
            if (rowView != null) {
                rowView.setVisibility(anyInRow ? 0 : 8);
            }
            r++;
            h1 = h7;
            h2 = h8;
            h3 = h9;
        }
    }

    public static void phraseItemTable1(View root, String[][] table) {
        bindGenericTable(root, table, 10, 4);
    }

    public static void phraseItemTable2(View root, final CardAdapter.OnSpeakClick speakClick, String[][] table) {
        for (int r = 1; r <= 10; r++) {
            String[] data = safeRow(table, r - 1);
            String c1 = safeCell(data, 0);
            boolean any = true;
            String c2 = safeCell(data, 1);
            if (!hasText(c1) && !hasText(c2)) {
                any = false;
            }
            TextView tv1 = findTvByName(root, "r" + r + "c1");
            TextView tv2 = findTvByName(root, "r" + r + "c2");
            ImageView sp = (ImageView) findByName(root, "r" + r + "Speaker");
            if (!any) {
                setTextGone(tv1);
                setTextGone(tv2);
                if (sp != null) {
                    sp.setVisibility(8);
                    sp.setOnClickListener(null);
                }
            } else {
                setTextOrGone(tv1, c1);
                setTextOrGone(tv2, c2);
                if (sp != null) {
                    sp.setVisibility(0);
                    final String speakText = hasText(c2) ? c2 : c1;
                    sp.setOnClickListener(new View.OnClickListener() { // from class: com.example.dalawingo.ui.lessons.cards.PhraseItemCharts$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            speakClick.onSpeak(speakText);
                        }
                    });
                }
            }
        }
    }

    public static void phraseItemTable3(View root, String rootWord, String rootMeaning, String[][] rows) {
        String[] strArr = null;
        String[] r1 = (rows == null || rows.length <= 0) ? null : rows[0];
        String[] r2 = (rows == null || rows.length <= 1) ? null : rows[1];
        if (rows != null && rows.length > 2) {
            strArr = rows[2];
        }
        String[] r3 = strArr;
        phraseItemTable3(root, rootWord, rootMeaning, r1, r2, r3);
    }

    public static void phraseItemTable3(View root, String rootWord, String rootMeaning, String[] row1, String[] row2, String[] row3) {
        TextView tvRootWord = (TextView) root.findViewById(R.id.tvRootWord);
        TextView tvRootMeaning = (TextView) root.findViewById(R.id.tvRootMeaning);
        setTextOrGone(tvRootWord, rootWord);
        setTextOrGone(tvRootMeaning, rootMeaning);
        bindTable3Row(root, 1, row1);
        bindTable3Row(root, 2, row2);
        bindTable3Row(root, 3, row3);
    }

    public static void phraseItemTable4(View root, String h1, String h2, String h3, String h4, String[][] rows) {
        setTextOrGone((TextView) root.findViewById(R.id.h1), h1);
        setTextOrGone((TextView) root.findViewById(R.id.h2), h2);
        setTextOrGone((TextView) root.findViewById(R.id.h3), h3);
        setTextOrGone((TextView) root.findViewById(R.id.h4), h4);
        for (int r = 1; r <= 6; r++) {
            String[] data = safeRow(rows, r - 1);
            String rWord = safeCell(data, 0);
            String rMean = safeCell(data, 1);
            String pWord = safeCell(data, 2);
            String pMean = safeCell(data, 3);
            String prWord = safeCell(data, 4);
            String prMean = safeCell(data, 5);
            String fWord = safeCell(data, 6);
            String fMean = safeCell(data, 7);
            boolean any = hasText(rWord) || hasText(rMean) || hasText(pWord) || hasText(pMean) || hasText(prWord) || hasText(prMean) || hasText(fWord) || hasText(fMean);
            View c1 = findByName(root, "r" + r + "c1");
            View c2 = findByName(root, "r" + r + "c2");
            View c3 = findByName(root, "r" + r + "c3");
            View c4 = findByName(root, "r" + r + "c4");
            if (c1 != null) {
                c1.setVisibility(any ? 0 : 8);
            }
            if (c2 != null) {
                c2.setVisibility(any ? 0 : 8);
            }
            if (c3 != null) {
                c3.setVisibility(any ? 0 : 8);
            }
            if (c4 != null) {
                c4.setVisibility(any ? 0 : 8);
            }
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

    private static void bindGenericTable(View root, String[][] table, int maxRows, int maxCols) {
        for (int r = 1; r <= maxRows; r++) {
            String[] data = safeRow(table, r - 1);
            boolean anyInRow = false;
            for (int c = 1; c <= maxCols; c++) {
                if (hasText(safeCell(data, c - 1))) {
                    anyInRow = true;
                    break;
                }
            }
            for (int c2 = 1; c2 <= maxCols; c2++) {
                TextView tv = findTvByName(root, "r" + r + "c" + c2);
                if (anyInRow) {
                    setTextOrGone(tv, safeCell(data, c2 - 1));
                } else {
                    setTextGone(tv);
                }
            }
        }
    }

    private static void bindTable3Row(View root, int r, String[] data) {
        String label = safeCell(data, 0);
        String a = safeCell(data, 1);
        String b = safeCell(data, 2);
        String c = safeCell(data, 3);
        String eng = safeCell(data, 4);
        boolean any = hasText(label) || hasText(a) || hasText(b) || hasText(c) || hasText(eng);
        TextView c1 = findTvByName(root, "r" + r + "c1");
        LinearLayout c2 = (LinearLayout) findByName(root, "r" + r + "c2");
        TextView c2a = findTvByName(root, "r" + r + "c2a");
        TextView c2b = findTvByName(root, "r" + r + "c2b");
        TextView c2c = findTvByName(root, "r" + r + "c2c");
        TextView c3 = findTvByName(root, "r" + r + "c3");
        if (!any) {
            setTextGone(c1);
            if (c2 != null) {
                c2.setVisibility(8);
            }
            setTextGone(c2a);
            setTextGone(c2b);
            setTextGone(c2c);
            setTextGone(c3);
            return;
        }
        setTextOrGone(c1, label);
        boolean anyParts = hasText(a) || hasText(b) || hasText(c);
        if (c2 != null) {
            c2.setVisibility(anyParts ? 0 : 8);
        }
        setTextOrGone(c2a, a);
        setTextOrGone(c2b, b);
        setTextOrGone(c2c, c);
        setTextOrGone(c3, eng);
    }

    private static int idByName(View root, String name) {
        return root.getResources().getIdentifier(name, "id", root.getContext().getPackageName());
    }

    private static View findByName(View root, String name) {
        int id = idByName(root, name);
        if (id != 0) {
            return root.findViewById(id);
        }
        return null;
    }

    private static TextView findTvByName(View root, String name) {
        View v = findByName(root, name);
        if (v instanceof TextView) {
            return (TextView) v;
        }
        return null;
    }

    private static String safeCell(String[][] arr, int r, int c) {
        String[] row;
        return (arr != null && r >= 0 && r < arr.length && (row = arr[r]) != null && c >= 0 && c < row.length && row[c] != null) ? row[c] : "";
    }

    private static String[] safeRow(String[][] table, int r) {
        if (table == null) {
            return new String[0];
        }
        if (r < 0 || r >= table.length) {
            return new String[0];
        }
        String[] row = table[r];
        return row != null ? row : new String[0];
    }

    private static String safeCell(String[] row, int c) {
        String s;
        return (row != null && c >= 0 && c < row.length && (s = row[c]) != null) ? s : "";
    }

    private static boolean hasText(String s) {
        return s != null && s.trim().length() > 0;
    }

    private static void setTextGone(TextView tv) {
        if (tv == null) {
            return;
        }
        tv.setText("");
        tv.setVisibility(8);
    }

    private static void setTextOrGone(TextView tv, String text) {
        if (tv == null) {
            return;
        }
        if (hasText(text)) {
            tv.setText(text);
            tv.setVisibility(0);
        } else {
            tv.setText("");
            tv.setVisibility(8);
        }
    }

    public static void phraseItemTable5(View root, String[] headers, String[][] rows) {
        TextView h1 = (TextView) root.findViewById(R.id.hPrefix);
        TextView h2 = (TextView) root.findViewById(R.id.hFocus);
        TextView h3 = (TextView) root.findViewById(R.id.hKeyUse);
        int i = 0;
        setTextOrGone(h1, safeCell(headers, 0));
        int i2 = 1;
        setTextOrGone(h2, safeCell(headers, 1));
        int i3 = 2;
        setTextOrGone(h3, safeCell(headers, 2));
        int r = 1;
        while (r <= 5) {
            String[] data = safeRow(rows, r - 1);
            String c1 = safeCell(data, i);
            String c2 = safeCell(data, i2);
            String c3 = safeCell(data, i3);
            int i4 = (hasText(c1) || hasText(c2) || hasText(c3)) ? i2 : i;
            TextView tv1 = findTvByName(root, "r" + r + "c1");
            TextView tv2 = findTvByName(root, "r" + r + "c2");
            TextView tv3 = findTvByName(root, "r" + r + "c3");
            if (i4 == 0) {
                setTextGone(tv1);
                setTextGone(tv2);
                setTextGone(tv3);
            } else {
                setTextOrGone(tv1, c1);
                setTextOrGone(tv2, c2);
                setTextOrGone(tv3, c3);
            }
            r++;
            i = 0;
            i2 = 1;
            i3 = 2;
        }
    }
}
