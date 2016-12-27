package joke.recyclerdemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

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
//        layout1(recycler);
        detachAndScrapAttachedViews(recycler);
        lastVisiblePosition = -1;
//        layout3(recycler, state);

        layout4(recycler, state, 0);
    }

    private void layout4(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        int startLeft;
        int startTop;
        if (dy == 0) {
            startLeft = 0;
            startTop = 0;
        } else if (dy > 0) {
            View lastChild = getChildAt(getChildCount() - 1);
            Rect rect = new Rect();
            getDecoratedBoundsWithMargins(lastChild, rect);
            startLeft = rect.right;
            startTop = rect.top;
        } else {
            View lastChild = getChildAt(0);
            Rect rect = new Rect();
            getDecoratedBoundsWithMargins(lastChild, rect);
            startLeft = rect.right;
            startTop = rect.top;
        }

        SparseArray<Rect> rectArray = new SparseArray<>();
        if (dy >= 0) {
            for (int i = lastVisiblePosition + 1; i < getItemCount(); i++) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int marginWidth = getMarginWidth(view);
                int marginHeight = getMarginHeight(view);

                int end = startLeft + marginWidth;
                if (end > getWidth()) {
                    startLeft = 0;
                    startTop += marginHeight;
                    if (startTop > getHeight()) {
                        removeAndRecycleView(view, recycler);
                        break;
                    }
                }
                Rect rect = new Rect(startLeft, startTop, startLeft + marginWidth, startTop + marginHeight);
                rectArray.put(i, rect);
                startLeft += marginWidth;

            }
            lastVisiblePosition += rectArray.size();

            for (int i = 0; i < rectArray.size(); i++) {
                View view = getChildAt(getChildCount() - i - 1);
                Rect rect = rectArray.valueAt(rectArray.size() - i - 1);
                layoutDecoratedWithMargins(view, rect.left, rect.top, rect.right, rect.bottom);
            }
        } else {


            for (int i = firstVisiblePosition - 1; i >= 0; i--) {
                Log.e(TAG, "layout4: " + firstVisiblePosition );
                View view = recycler.getViewForPosition(i);
                addView(view,0);
                measureChildWithMargins(view, 0, 0);
                int marginWidth = getMarginWidth(view);
                int marginHeight = getMarginHeight(view);
                int end = startLeft + marginWidth;
                if (end > getWidth()) {
                    startLeft = 0;
                    startTop -= marginHeight;
                    if (startTop <0) {
                        removeAndRecycleView(view, recycler);
                        break;
                    }
                }
                Rect rect = new Rect(startLeft, startTop-marginHeight, startLeft + marginWidth, startTop );
                rectArray.put(i,rect);
            }
            firstVisiblePosition -=rectArray.size();
            for (int i = 0;i<rectArray.size();i++){
                View view = getChildAt(i);
                Rect rect = rectArray.valueAt(i);
                layoutDecoratedWithMargins(view, rect.left, rect.top, rect.right, rect.bottom);
            }

        }


//        for (int i = 0; i < rectArray.size(); i++) {
//            Rect rect = rectArray.valueAt(i);
//            layoutDecoratedWithMargins(getChildAt(lastVisiblePosition + i), rect.left, rect.top, rect.right, rect.bottom);
//
//        }


    }

    private void layout3(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (lastVisiblePosition == getItemCount() - 1) {
            return;
        }
        int startLeft;
        int startTop;
        if (lastVisiblePosition < 0) {
            startLeft = 0;
            startTop = 0;
        } else {
            View lastChild = getChildAt(getChildCount() - 1);

            Rect rect = new Rect();
            getDecoratedBoundsWithMargins(lastChild, rect);
            startLeft = rect.right;
            startTop = rect.top;
        }


        View view = recycler.getViewForPosition(lastVisiblePosition + 1);
        addView(view);
        lastVisiblePosition++;
        measureChildWithMargins(view, 0, 0);

        int end = startLeft + getMarginWidth(view);
        if (end > getWidth()) {
            startLeft = 0;
            end = startLeft + getMarginWidth(view);
            startTop += getMarginHeight(view);
            if (startTop > getHeight()) {
                lastVisiblePosition--;
                removeAndRecycleView(view, recycler);
                return;
            }
        }

        layoutDecoratedWithMargins(view, startLeft, startTop, end, startTop + getMarginHeight(view));

        layout3(recycler, state);
    }

    private void scrollBy2(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0 || getChildCount() == getItemCount()) {
            return;
        }

        int i = getChildCount();

        View lastChild = getChildAt(getChildCount() - 1);
        Rect rect = new Rect();
        getDecoratedBoundsWithMargins(lastChild, rect);

        int startLeft = rect.right;
        int startTop = rect.top;

        View view = recycler.getViewForPosition(i);
        addView(view);

        measureChildWithMargins(view, 0, 0);
        int end = startLeft + getMarginWidth(view);
        if (end > getWidth()) {
            startLeft = 0;
            end = startLeft + getMarginWidth(view);
            startTop += getMarginHeight(view);
            if (startTop > getHeight() + getMarginHeight(view)) {
                removeAndRecycleView(view, recycler);
                return;
            }
        }
        layoutDecoratedWithMargins(view, startLeft, startTop, end, startTop + getMarginHeight(view));
        scrollBy2(dy, recycler, state);

    }

    private void layout2(RecyclerView.Recycler recycler, RecyclerView.State state) {

        int start = 0;
        int verticalOffset = 0;

        detachAndScrapAttachedViews(recycler);

        for (int i = 0; i < getItemCount(); i++) {

            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int end = start + getMarginWidth(view);
            if (end > getWidth()) {
                start = 0;
                end = start + getMarginWidth(view);
                verticalOffset += getMarginHeight(view);

            }


            layoutDecoratedWithMargins(view, start, verticalOffset, end, verticalOffset + getMarginHeight(view));
            Rect outBounds = new Rect();
            getDecoratedBoundsWithMargins(view, outBounds);

            start = outBounds.right;

            if (outBounds.top > getHeight()) {
                removeAndRecycleView(view, recycler);
                break;
            }

        }
    }

    private void scrollBy3(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int n = getChildCount();
        if (dy > 0) {
            for (int i = 0; i < n; i++) {
                View view = getChildAt(i);
                Rect rect = new Rect();
                getDecoratedBoundsWithMargins(view, rect);
                if (rect.bottom <= 0) {

                    removeAndRecycleView(view, recycler);
                } else {
                    firstVisiblePosition = getPosition(view);
                    return;
                }
            }
        } else if (dy < 0) {
            for (int i = n - 1; i >= 0; i--) {
                View view = getChildAt(i);
                Rect rect = new Rect();
                getDecoratedBoundsWithMargins(view, rect);
                if (rect.top > getHeight()) {
                    removeAndRecycleView(view, recycler);
                } else {
                    lastVisiblePosition = getPosition(view);
                    return;
                }
            }
        }

        Log.e(TAG, "scrollBy3: " + getChildCount());

    }

    int firstVisiblePosition = 0;
    int lastVisiblePosition = -1;


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

//        scrollBy2(dy, recycler, state);
        scrollBy3(dy, recycler, state);
//        layout3(recycler, state);

        layout4(recycler, state, dy);

        mOrientationHelper.offsetChildren(-dy);
//       retrun scrollBy1(dy, recycler, state)
        return dy;
    }


    private int scrollBy1(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consume = 0;
        if (getChildCount() == 0 || dy == 0) {
            return 0;
        } else if (dy < 0) {
            consume = mOrientationHelper.getDecoratedStart(getChildAt(0));
            dy = Math.max(dy, consume);
        } else if (dy > 0) {
            consume = mOrientationHelper.getDecoratedEnd(getChildAt(getChildCount() - 1)) - mOrientationHelper.getTotalSpace();
            dy = Math.min(consume, dy);
            dy = Math.max(dy, 0);
        }
        mOrientationHelper.offsetChildren(-dy);

        return dy;
    }


    private int getMarginWidth(View view) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + layoutParams.leftMargin + layoutParams.rightMargin;
    }

    private int getMarginHeight(View view) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + layoutParams.topMargin + layoutParams.bottomMargin;
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
