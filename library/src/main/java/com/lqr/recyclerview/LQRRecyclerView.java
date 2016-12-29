package com.lqr.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;

/**
 * @创建者 CSDN_LQR
 * @描述 方便使用的RecyclerView（默认是竖起列表）
 * <p>
 * 不需要管理LayouManger，可以通过xml文件或setter方法设置
 */
public class LQRRecyclerView extends RecyclerView {

    /*------------------ 常量 begin ------------------*/
    //类型
    public static final int TYPE_GRID = 0;
    public static final int TYPE_STAGGER = 1;
    //方向
    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;
    /*------------------ 常量 end ------------------*/

    //类型、方向、列数
    private int type = TYPE_GRID;
    private int orientation = ORIENTATION_VERTICAL;
    private int column = 1;

    //分割线
    private int dividerSize = 0;
    private int dividerColor = Color.BLACK;
    private Drawable dividerDrawable = null;

    private boolean move = false;
    private int mIndex = 0;

    private Context mContext;
    private LayoutManager mLayoutManager;
    private LQRItemDecoration mItemDecoration;

    public LQRRecyclerView(Context context) {
        super(context, null);
    }

    public LQRRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        /*================== 获取自定义属性 begin ==================*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LQRRecyclerView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LQRRecyclerView_rv_type) {
                type = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.LQRRecyclerView_rv_orientation) {
                orientation = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.LQRRecyclerView_rv_column) {
                column = typedArray.getInt(attr, 1);
            } else if (attr == R.styleable.LQRRecyclerView_rv_divider_size) {
                dividerSize = (int) typedArray.getDimension(attr, 0f);
            } else if (attr == R.styleable.LQRRecyclerView_rv_divider_drawable) {
                dividerDrawable = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.LQRRecyclerView_rv_divider_color) {
                dividerColor = typedArray.getColor(attr, Color.BLACK);
            }
        }
        typedArray.recycle();
        /*================== 获取自定义属性 end ==================*/

        init();
    }

    /**
     * 根据属性初始化RecyclerView
     */
    private void init() {
        //1、设置RecyclerView的类型和方向
        switch (type) {
            case TYPE_GRID:
                switch (orientation) {
                    case ORIENTATION_VERTICAL:
                        mLayoutManager = new GridLayoutManager(mContext, column);
                        break;
                    case ORIENTATION_HORIZONTAL:
                        mLayoutManager = new GridLayoutManager(mContext, column, GridLayoutManager.HORIZONTAL, false);
                        break;
                }
                break;
            case TYPE_STAGGER:
                switch (orientation) {
                    case ORIENTATION_VERTICAL:
                        mLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
                        break;
                    case ORIENTATION_HORIZONTAL:
                        mLayoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.HORIZONTAL);
                        break;
                }
                break;
        }
        this.setLayoutManager(mLayoutManager);

        //2、设置RecyclerView的分割线样式
        this.removeItemDecoration(mItemDecoration);
        mItemDecoration = new LQRItemDecoration(mContext, orientation, dividerSize, dividerColor, dividerDrawable);
        this.addItemDecoration(mItemDecoration);

        //3、设置滚动监听（用于平滑滚动）
        addOnScrollListener(new RecyclerViewListener());
    }

    /**
     * 提醒LQRRecyclerView，当前的列表样式已经更改（一般是用代码动态修改了type和orientation后调用，如果修改了分割线样式，也需要调用该方法进行刷新）
     */
    public void notifyViewChanged() {
        init();
        //重新设置布局管理器后需要设置适配器
        Adapter adapter = this.getAdapter();
        if (adapter != null)
            this.setAdapter(adapter);
    }


    /**
     * 平滑滚动到指定位置（注意：对瀑布流无效果）
     *
     * @param position
     */
    public void smoothMoveToPosition(int position) {
        if (type != TYPE_GRID) {
            return;
        }

        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e("CSDN_LQR", "超出范围了");
            return;
        }
        mIndex = position;
        stopScroll();

        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();
        if (position <= firstItem) {
            this.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            int top = this.getChildAt(position - firstItem).getTop();
            this.smoothScrollBy(0, top);
        } else {
            this.smoothScrollToPosition(position);
            move = true;
        }

    }

    /**
     * 滚动到指定位置（注意：对瀑布流无效果）
     *
     * @param position
     */
    public void moveToPosition(int position) {
        if (type != TYPE_GRID) {
            return;
        }

        if (position < 0 || position >= getAdapter().getItemCount()) {
            Log.e("CSDN_LQR", "超出范围了");
            return;
        }
        mIndex = position;
        stopScroll();

        GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
        int firstItem = glm.findFirstVisibleItemPosition();
        int lastItem = glm.findLastVisibleItemPosition();
        if (position <= firstItem) {
            this.scrollToPosition(position);
        } else if (position <= lastItem) {
            int top = this.getChildAt(position - firstItem).getTop();
            this.scrollBy(0, top);
        } else {
            this.scrollToPosition(position);
            move = true;
        }

    }

    /**
     * @创建者 CSDN_LQR
     * @描述 RecyclerView的滚动监听, 用于平滑滚动条目置顶
     */
    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (mOnScrollListenerExtension != null) {
                mOnScrollListenerExtension.onScrollStateChanged(recyclerView, newState);
            }

            if (type != TYPE_GRID) {
                return;
            }
            GridLayoutManager glm = (GridLayoutManager) mLayoutManager;

            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - glm.findFirstVisibleItemPosition();
                if (0 <= n && n < LQRRecyclerView.this.getChildCount()) {
                    int top = LQRRecyclerView.this.getChildAt(n).getTop();
                    LQRRecyclerView.this.smoothScrollBy(0, top);
                }

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mOnScrollListenerExtension != null) {
                mOnScrollListenerExtension.onScrolled(recyclerView, dx, dy);
            }

            if (type != TYPE_GRID) {
                return;
            }
            GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
            if (move) {
                move = false;
                int n = mIndex - glm.findFirstVisibleItemPosition();
                if (0 <= n && n < LQRRecyclerView.this.getChildCount()) {
                    int top = LQRRecyclerView.this.getChildAt(n).getTop();
                    LQRRecyclerView.this.scrollBy(0, top);
                }
            }
        }
    }

    /*================== LQRRecyclerView的滚动事件拓展 begin ==================*/
    private OnScrollListenerExtension mOnScrollListenerExtension;

    public OnScrollListenerExtension getOnScrollListenerExtension() {
        return mOnScrollListenerExtension;
    }

    public void setOnScrollListenerExtension(OnScrollListenerExtension onScrollListenerExtension) {
        mOnScrollListenerExtension = onScrollListenerExtension;
    }

    /**
     * @创建者 CSDN_LQR
     * @描述 LQRRecyclerView的滚动事件拓展（原滚动事件被用于平滑滚动）
     */
    public interface OnScrollListenerExtension {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }
    /*================== LQRRecyclerView的滚动事件拓展 end ==================*/

    /**
     * @创建者 CSDN_LQR
     * @描述 分割线
     * <p>
     * 当同时设置了颜色和图片时，以图片为主
     * 当不设置size时，分割线以图片的厚度为标准或不显示分割线（size默认为0）。
     */
    class LQRItemDecoration extends ItemDecoration {
        private Context mContext;
        private int mOrientation;
        private int mDividerSize = 0;
        private int mDividerColor = Color.BLACK;
        private Drawable mDividerDrawable;
        private Paint mPaint;

        public LQRItemDecoration(Context context, int orientation, int dividerSize, int dividerColor, Drawable dividerDrawable) {
            mContext = context;
            mOrientation = orientation;
            mDividerSize = dividerSize;
            mDividerColor = dividerColor;
            mDividerDrawable = dividerDrawable;

            //绘制纯色分割线
            if (dividerDrawable == null) {
                //初始化画笔(抗锯齿)并设置画笔颜色和画笔样式为填充
                mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                mPaint.setColor(mDividerColor);
                mPaint.setStyle(Paint.Style.FILL);
                //绘制图片分割线
            } else {
                //如果没有指定分割线的size，则默认是图片的厚度
                if (mDividerSize == 0) {
                    if (mOrientation == LQRRecyclerView.ORIENTATION_VERTICAL) {
                        mDividerSize = dividerDrawable.getIntrinsicHeight();
                    } else {
                        mDividerSize = dividerDrawable.getIntrinsicWidth();
                    }
                }
            }

        }

        /**
         * 绘制item分割线
         *
         * @param c
         * @param parent
         * @param state
         */
        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            //纵向列表画横线，横向列表画竖线
            if (mOrientation == LQRRecyclerView.ORIENTATION_VERTICAL) {
                drawHorientationDivider(c, parent, state);
            } else {
                drawVerticalDivider(c, parent, state);
            }
        }

        /**
         * 根据分割线的size设置item偏移量
         *
         * @param outRect
         * @param view
         * @param parent
         * @param state
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            if (mOrientation == LQRRecyclerView.ORIENTATION_VERTICAL) {
                outRect.set(0, 0, 0, mDividerSize);
            } else {
                outRect.set(0, 0, mDividerSize, 0);
            }
        }

        /**
         * 画横线
         */
        private void drawHorientationDivider(Canvas c, RecyclerView parent, State state) {
            //得到分割线的四个点：左、上、右、下
            //画横线时左右可以根据parent得到
            int left = parent.getPaddingLeft();
            int right = parent.getMeasuredWidth() - parent.getPaddingRight();

            //上下需要根据每个孩子控件计算
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + dividerSize;
                //得到四个点后开始画
                if (mDividerDrawable == null) {
                    c.drawRect(left, top, right, bottom, mPaint);
                } else {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            }
        }

        /**
         * 画竖线
         */
        private void drawVerticalDivider(Canvas c, RecyclerView parent, State state) {
            //画竖线时上下可以根据parent得到
            int top = parent.getPaddingTop();
            int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();

            //左右需要根据孩子控件计算
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                int left = child.getRight() + params.rightMargin;
                int right = left + dividerSize;
                //得到四个点后开始画
                if (mDividerDrawable == null) {
                    c.drawRect(left, top, right, bottom, mPaint);
                } else {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(c);
                }
            }
        }
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getDividerSize() {
        return dividerSize;
    }

    public void setDividerSize(int dividerSize) {
        this.dividerSize = dividerSize;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public Drawable getDividerDrawable() {
        return dividerDrawable;
    }

    public void setDividerDrawable(Drawable dividerDrawable) {
        this.dividerDrawable = dividerDrawable;
    }

    @Override
    public LayoutManager getLayoutManager() {
        return mLayoutManager;
    }
}
