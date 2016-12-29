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
        if (state.isPreLayout()){
            return;
        }
        int startLeft = 0;
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
            startLeft = rect.left;
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
                    if (startTop > getHeight() + dy) {
                        removeAndRecycleView(view, recycler);
                        break;
                    }
                }
                Rect rect = new Rect(startLeft, startTop, startLeft + marginWidth, startTop + marginHeight);
                layoutDecoratedWithMargins(view, rect.left, rect.top, rect.right, rect.bottom);
                startLeft += marginWidth;
                lastVisiblePosition++;
            }
        } else {
            startLeft = 0;
            for (int i = firstVisiblePosition - 1; i >= 0; i--) {
                Log.e(TAG, "layout4: " + firstVisiblePosition);
                View view = recycler.getViewForPosition(i);
                addView(view, 0);
                measureChildWithMargins(view, 0, 0);
                int marginWidth = getMarginWidth(view);
                int marginHeight = getMarginHeight(view);
                int end = startLeft + marginWidth;
                if (end > getWidth()) {
                    startLeft = 0;
                    startTop -= marginHeight;
                    if (startTop < dy) {
                        removeAndRecycleView(view, recycler);
                        break;
                    }
                } else {

                    Rect rectNow = new Rect(startLeft, startTop - marginHeight, startLeft + marginWidth, startTop);
                    rectArray.put(i, rectNow);

                    for (int j = 0; j < rectArray.size(); j++) {
                        Rect rect = rectArray.valueAt(j);
                        if (rect != null) {
                            rect.offset(marginWidth, 0);
                            rectNow.offset(-rect.width(), 0);
                        }
                    }
                }
                startLeft += marginWidth;
                firstVisiblePosition--;

            }
            for (int j = 0; j < rectArray.size(); j++) {
                View viewNow = getChildAt(j);
                Rect rect = rectArray.valueAt(j);
                layoutDecoratedWithMargins(viewNow, rect.left, rect.top, rect.right, rect.bottom);
            }
            rectArray.clear();

        }


        Log.e(TAG, "layout4: " + getChildCount());
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
                if (rect.bottom <= -dy) {

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
                if (rect.top > getHeight() - dy) {
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

        layout4(recycler, state, dy);
        scrollBy3(dy, recycler, state);
        return scrollBy1(dy, recycler, state);


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


    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }
}
