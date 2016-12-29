package joke.recyclerdemo.widget;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

/**
 * Created by SHF on 2016/12/29.
 */

public class TextNumberItemAnimator extends SimpleItemAnimator {
    private static final String TAG = "TextNumberItemAnimator";
    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        return false;
    }

    @Override
    public boolean animateChange(final RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, final int toLeft, int toTop) {
        if (oldHolder!=newHolder) {
            View newView =  newHolder.itemView;
            View oldView =  oldHolder.itemView;
            newView.setTranslationY(newView.getHeight());
            Animator.AnimatorListener listener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    dispatchChangeStarting(oldHolder, true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    dispatchChangeFinished(oldHolder, true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
            newView.animate().translationY(0).setDuration(150).setListener(listener).start();
            oldView.animate().translationY(-oldView.getHeight()).setDuration(150).setListener(listener).start();
        }
        return false;
    }

    @Override
    public void runPendingAnimations() {

    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

        dispatchChangeFinished(item,false);
    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }


}
