package com.recyclerview.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.recyclerview.gallery.transform.DiscreteScrollItemTransformer;
import com.recyclerview.gallery.util.ScrollListenerAdapter;

import me.relex.photodraw.R;

/**
 * Created by yarolegovich on 18.02.2017.
 */
@SuppressWarnings("unchecked")
public class DiscreteScrollView extends RecyclerView {

    private static final int DEFAULT_ORIENTATION = RecyclerView.HORIZONTAL;

    private DiscreteScrollLayoutManager layoutManager;

    private ScrollStateChangeListener scrollStateChangeListener;
    private OnItemChangedListener onItemChangedListener;

    public DiscreteScrollView(Context context) {
        super(context);
        init(null);
    }

    public DiscreteScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DiscreteScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int orientation = DEFAULT_ORIENTATION;
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.DiscreteScrollView);
            orientation = ta.getInt(R.styleable.DiscreteScrollView_dsv_orientation, DEFAULT_ORIENTATION);
            ta.recycle();
        }

        layoutManager = new DiscreteScrollLayoutManager(
                getContext(), new ScrollStateListener(),
                com.recyclerview.gallery.Orientation.values()[orientation]);
        setLayoutManager(layoutManager);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof DiscreteScrollLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException(getContext().getString(R.string.dsv_ex_msg_dont_set_lm));
        }
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean isFling = super.fling(velocityX, velocityY);
        if (isFling) {
            layoutManager.onFling(velocityX, velocityY);
        } else {
            layoutManager.returnToCurrentPosition();
        }
        return isFling;
    }

    @Nullable
    public ViewHolder getViewHolder(int position) {
        View view = layoutManager.findViewByPosition(position);
        return view != null ? getChildViewHolder(view) : null;
    }

    /**
     * @return adapter position of the current item or -1 if nothing is selected
     */
    public int getCurrentItem() {
        return layoutManager.getCurrentPosition();
    }

    public void setItemTransformer(DiscreteScrollItemTransformer transformer) {
        layoutManager.setItemTransformer(transformer);
    }

    public void setItemTransitionTimeMillis(@IntRange(from = 10) int millis) {
        layoutManager.setTimeForItemSettle(millis);
    }

    public void setOrientation(com.recyclerview.gallery.Orientation orientation) {
        layoutManager.setOrientation(orientation);
    }

    public void setOffscreenItems(int items) {
        layoutManager.setOffscreenItems(items);
    }

    public void setScrollStateChangeListener(ScrollStateChangeListener<?> scrollStateChangeListener) {
        this.scrollStateChangeListener = scrollStateChangeListener;
    }

    public void setScrollListener(ScrollListener<?> scrollListener) {
        setScrollStateChangeListener(new ScrollListenerAdapter(scrollListener));
    }

    public void setOnItemChangedListener(OnItemChangedListener<?> onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }

    private class ScrollStateListener implements DiscreteScrollLayoutManager.ScrollStateListener {

        @Override
        public void onIsBoundReachedFlagChange(boolean isBoundReached) {
            setOverScrollMode(isBoundReached ? OVER_SCROLL_ALWAYS : OVER_SCROLL_NEVER);
        }

        @Override
        public void onScrollStart() {
            if (scrollStateChangeListener != null) {
                int current = layoutManager.getCurrentPosition();
                ViewHolder holder = getViewHolder(current);
                if (holder != null) {
                    scrollStateChangeListener.onScrollStart(holder, current);
                }
            }
        }

        @Override
        public void onScrollEnd() {
            ViewHolder holder = null;
            int current = layoutManager.getCurrentPosition();
            if (scrollStateChangeListener != null) {
                holder = getViewHolder(current);
                if (holder == null) {
                    return;
                }
                scrollStateChangeListener.onScrollEnd(holder, current);
            }
            if (onItemChangedListener != null) {
                if (holder == null) {
                    holder = getViewHolder(current);
                }
                if (holder != null) {
                    onItemChangedListener.onCurrentItemChanged(holder, current);
                }
            }
        }

        @Override
        public void onScroll(float currentViewPosition) {
            if (scrollStateChangeListener != null) {
                int current = getCurrentItem();
                ViewHolder currentHolder = getViewHolder(getCurrentItem());

                int newCurrent = current + (currentViewPosition < 0 ? 1 : -1);
                ViewHolder newCurrentHolder = getViewHolder(newCurrent);

                if (currentHolder != null && newCurrentHolder != null) {
                    scrollStateChangeListener.onScroll(
                            currentViewPosition, currentHolder,
                            newCurrentHolder);
                }
            }
        }

        @Override
        public void onCurrentViewFirstLayout() {
            if (onItemChangedListener != null) {
                int current = layoutManager.getCurrentPosition();
                ViewHolder currentHolder = getViewHolder(current);
                if (currentHolder != null) {
                    onItemChangedListener.onCurrentItemChanged(currentHolder, current);
                }
            }
        }
    }

    public interface ScrollStateChangeListener<T extends ViewHolder> {

        void onScrollStart(@NonNull T currentItemHolder, int adapterPosition);

        void onScrollEnd(@NonNull T currentItemHolder, int adapterPosition);

        void onScroll(float scrollPosition, @NonNull T currentHolder, @NonNull T newCurrent);
    }

    public interface ScrollListener<T extends ViewHolder> {

        void onScroll(float scrollPosition, @NonNull T currentHolder, @NonNull T newCurrent);
    }

    public interface OnItemChangedListener<T extends ViewHolder> {
        /*
         * This method will be also triggered when view appears on the screen for the first time.
         */
        void onCurrentItemChanged(@NonNull T viewHolder, int adapterPosition);

    }
}
