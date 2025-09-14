package com.example.semiwiki;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int heightPx;
    private final int insetStartPx;
    private final int insetEndPx;

    public DividerDecoration(Context context, int color, float heightDp, float insetStartDp, float insetEndDp) {
        float d = context.getResources().getDisplayMetrics().density;
        this.heightPx = (int) (heightDp * d + 0.5f);
        this.insetStartPx = (int) (insetStartDp * d + 0.5f);
        this.insetEndPx = (int) (insetEndDp * d + 0.5f);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int width = parent.getWidth();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) { // 마지막 줄엔 선 X
            View child = parent.getChildAt(i);
            float left = insetStartPx;
            float right =  width - insetEndPx;
            float top = child.getBottom();
            float bottom = top + heightPx;
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}
