package com.example.semiwiki.Board;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semiwiki.R;

import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(@NonNull BoardItem item, int position);
    }

    private final List<BoardItem> items = new ArrayList<>();
    private OnItemClickListener itemClickListener;

    public BoardAdapter(List<BoardItem> initial) {
        if (initial != null) items.addAll(initial);
    }

    /** 필요하면 데이터 교체용 */
    public void submitList(List<BoardItem> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.itemClickListener = l;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        BoardItem it = items.get(position);

        h.tvTitle.setText(it.getTitle());
        h.tvEditor.setText(it.getEditor());

        h.layoutChips.removeAllViews();
        List<String> cats = it.getCategories();
        if (cats != null && !cats.isEmpty()) {
            for (String label : cats) {
                TextView chip = makeChip(h.itemView.getContext(), label);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.setMarginEnd(dp(h.itemView, 4));
                h.layoutChips.addView(chip, lp);
            }
            h.layoutChips.setVisibility(View.VISIBLE);
        } else {
            h.layoutChips.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                int pos = h.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(items.get(pos), pos);
                }
            }
        });
    }
    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvEditor;
        LinearLayout layoutChips;
        VH(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvEditor = v.findViewById(R.id.tvEditor);
            layoutChips = v.findViewById(R.id.layoutChips);
        }
    }
    private TextView makeChip(Context ctx, String text) {
        TextView tv = new TextView(ctx);
        tv.setText(text);
        tv.setTextSize(10);
        tv.setTextColor(Color.parseColor("#E0E0E0"));
        tv.setIncludeFontPadding(false);
        tv.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_chip));
        return tv;
    }

    private static int dp(View v, int dp) {
        float d = v.getResources().getDisplayMetrics().density;
        return (int) (dp * d +0.5f);
    }
}
