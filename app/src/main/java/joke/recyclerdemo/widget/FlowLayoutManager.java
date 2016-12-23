package joke.recyclerdemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by SHF on 2016/11/25.
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private int totalHeight;
    private int verticalScrollOffset = 0;
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    private static final String TAG = "FlowLayoutManager";
    private OrientationHelper mOrientationHelper;

    public FlowLayoutManager() {
        mOrientationHelper = OrientationHelper.createVerticalHelper(this);
        OrientationHelper.createHorizontalHelper(this);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

//        layout2(recycler, state);


        layout1(recycler);
    }

    private void layout2(RecyclerView.Recycler recycler, RecyclerView.State state) {

        RecyclerView.State state1 = state;
        Log.e(TAG, "layout2: " + state);
        int start;
        int end;
        for (int i = 0; i < 1; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            int right = getDecoratedRight(view);


            Rect outBounds = new Rect();
            getDecoratedBoundsWithMargins(view, outBounds);

            outBounds.inset(-width, -height);

            layoutDecoratedWithMargins(view, outBounds.left, outBounds.top, outBounds.right, outBounds.bottom);

            int right2 = getDecoratedRight(view);
            Log.e(TAG, "layout2: " +right2 );

        }



    }


    private void layout1(RecyclerView.Recycler recycler) {
        int left = 0;
        int top = 0;
        for (int i = 0, n = getItemCount(); i < n; i++) {
            View view1 = recycler.getViewForPosition(i);
            addView(view1);
            measureChildWithMargins(view1, 0, 0);

            int width = getDecoratedMeasuredWidth(view1);
            int height = getDecoratedMeasuredHeight(view1);

            Rect outBounds = new Rect();
            getDecoratedBoundsWithMargins(view1, outBounds);

            int right = left + width;
            if (right > getWidth() - getPaddingRight()) {
                left = 0;
                right = left + width;
                top = top + height;
            }

            int bottom = top + height;
//            layoutDecorated(view1, left, top, right, bottom);

            layoutDecoratedWithMargins(view1, left, top, right, bottom);
            left += width;
        }
    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consume = 0;
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        } else if (dy < 0) {
            consume = getDecoratedTop(getChildAt(0));
            dy = Math.max(dy, consume);
        } else if (dy > 0) {
            consume = getDecoratedBottom(getChildAt(getChildCount() - 1)) - mOrientationHelper.getTotalSpace();
            dy = Math.min(consume, dy);
            dy = Math.max(dy, 0);
        }
        mOrientationHelper.offsetChildren(-dy);
        return dy;
    }


    private void layoutChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //如果没有item，直接返回
        if (getItemCount() <= 0) return;
        // 跳过preLayout，preLayout主要用于支持动画
        if (state.isPreLayout()) {
            return;
        }
        //在布局之前，将所有的子View先Detach掉，放入到Scrap缓存中
        detachAndScrapAttachedViews(recycler);
        //定义竖直方向的偏移量
        int offsetY = 0;
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {

            //这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            //将View加入到RecyclerView中
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            totalHeight += height;
            Rect frame = allItemFrames.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(0, offsetY, width, offsetY + height);
            // 将当前的Item的Rect边界数据保存
            allItemFrames.put(i, frame);

            //将竖直方向偏移量增大height
            offsetY += height;
        }
        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());
//         fixScrollOffset();
        recycleAndFillItems(recycler, state);
    }

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) { // 跳过preLayout，preLayout主要用于支持动画
            return;
        }

        // 当前scroll offset状态下的显示区域
        Rect displayFrame = new Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace());

        /**
         * 将滑出屏幕的Items回收到Recycle缓存中
         */
        Rect childFrame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childFrame.left = getDecoratedLeft(child);
            childFrame.top = getDecoratedTop(child);
            childFrame.right = getDecoratedRight(child);
            childFrame.bottom = getDecoratedBottom(child);
            //如果Item没有在显示区域，就说明需要回收
            if (!Rect.intersects(displayFrame, childFrame)) {
                //回收掉滑出屏幕的View
                removeAndRecycleView(child, recycler);

            }
        }

        //重新显示需要出现在屏幕的子View
        for (int i = 0; i < getItemCount(); i++) {

            if (Rect.intersects(displayFrame, allItemFrames.get(i))) {

                View scrap = recycler.getViewForPosition(i);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);

                Rect frame = allItemFrames.get(i);
                //将这个item布局出来
                layoutDecorated(scrap,
                        frame.left,
                        frame.top - verticalScrollOffset,
                        frame.right,
                        frame.bottom - verticalScrollOffset);

            }
        }
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
