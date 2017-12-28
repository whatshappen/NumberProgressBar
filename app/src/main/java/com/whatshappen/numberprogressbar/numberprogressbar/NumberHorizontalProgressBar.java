package com.whatshappen.numberprogressbar.numberprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.whatshappen.numberprogressbar.R;

/**
 * @author ww ;
 * @data on 2016/12/25.
 */
public class NumberHorizontalProgressBar extends View {

    /**
     * 进度提示文字颜色
     */
    private int percentTextColor = 0xffFFFFFF;

    /**
     * 进度提示文字大小
     */
    private int percentTextSize;

    /**
     * 进度条背景
     */
    private int progressBarColor = 0x661F1F1F;

    /**
     * 进度背景
     */
    private int progressColor = 0xff1C8BFB;

    /**
     * 进度条背景圆角弧度
     */
    private float progressRound = 0;

    /**
     * 是否显示进度值
     */
    private boolean showPercent = true;

    /**
     * 进度条高度
     */
    private int progressBarHeight;

    /**
     * 最大进度值
     */
    private int progressMax = 100;
    /**
     * 设置进度值背景
     */
    private int percentBgColor = 0xff1C8BFB;

    private static final int TOP = 0;
    private static final int CENTER = 1;
    private static final int BOTTOM = 2;
    /**
     * 自定义进度条的风格
     */
    private int progressBarStyle = TOP;
    private Context context;
    //画笔
    private Paint paint;
    //当前进度
    private int progress;

    private int distanceTextBar;
    private float textHeight;//进度值高度

    //private int percentPaddingHor;//设置百分比的起始位置

    private int percentPadding;//进度值背景边界


    public NumberHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public NumberHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置默认字体
        percentTextSize = UIUtils.dp2px(context, 12);
        progressBarHeight = UIUtils.dp2px(context, 2);
        distanceTextBar = UIUtils.dp2px(context, 3);
        //percentPaddingHor = UIUtils.dp2px(context, 1.5f);
        percentPadding = UIUtils.dp2px(context, 2);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberHorizontalProgressBar);
        percentTextColor = typedArray.getColor(R.styleable.NumberHorizontalProgressBar_percent_color, percentTextColor);
        percentTextSize = (int) typedArray.getDimension(R.styleable.NumberHorizontalProgressBar_percent_size, percentTextSize);
        progressBarColor = typedArray.getColor(R.styleable.NumberHorizontalProgressBar_progress_bar_bg, progressBarColor);
        progressColor = typedArray.getColor(R.styleable.NumberHorizontalProgressBar_progress_bg, progressColor);
        progressRound = typedArray.getDimension(R.styleable.NumberHorizontalProgressBar_progress_round, progressRound);
        showPercent = typedArray.getBoolean(R.styleable.NumberHorizontalProgressBar_show_percent, showPercent);
        progressBarHeight = (int) typedArray.getDimension(R.styleable.NumberHorizontalProgressBar_progress_bar_height, progressBarHeight);
        progressMax = typedArray.getInt(R.styleable.NumberHorizontalProgressBar_progress_max, progressMax);
        distanceTextBar = (int) typedArray.getDimension(R.styleable.NumberHorizontalProgressBar_distance_text_bar, distanceTextBar);
        percentBgColor = typedArray.getColor(R.styleable.NumberHorizontalProgressBar_percent_bg_color, percentBgColor);
        progressBarStyle = typedArray.getInt(R.styleable.NumberHorizontalProgressBar_progress_bar_style, progressBarStyle);
        percentPadding = (int) typedArray.getDimension(R.styleable.NumberHorizontalProgressBar_percent_padding, percentPadding);
        typedArray.recycle();
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);//设置抗锯齿
        textHeight = getTextHeight("0%");
    }

    //设置进度条的最小宽度
    @Override
    protected int getSuggestedMinimumWidth() {
        return percentTextSize + 2 * percentPadding;
    }

    //设置进度条的最小高度
    @Override
    protected int getSuggestedMinimumHeight() {
        Log.e("NumberHorProgressBar", "getHeight()1 =" + getHeight());
        if (getHeight() >= percentTextSize + progressBarHeight) {
            return getHeight();
        } else {
            return percentTextSize + progressBarHeight + distanceTextBar + 2 * percentPadding;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth()
                    : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }


    private int startX;//设置绘制X轴的起始点
    private int startY;//设置绘制Y轴的起始点

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断，如果整体高度小于textHeight+progressBarHeight+distanceTextBar
        if (textHeight + progressBarHeight + distanceTextBar > getHeight()) {
            throw new IllegalStateException("NumberHorizontalProgressBar" +
                    " Height is too small，最小高度值为：" +
                    UIUtils.px2dp(context, getSuggestedMinimumHeight()) + "dp");
        }
        startX = getWidth() / 2;
        startY = getHeight();
        if (showPercent) {
            if (progressBarStyle == TOP)
                drawTopStyle(canvas);
            else if (progressBarStyle == BOTTOM)
                drawBottomStyle(canvas);
            else if (progressBarStyle == CENTER)
                drawCenterStyle(canvas);
        } else {
            drawCenterStyle(canvas);
        }

    }

    /**
     * 绘制居中的进度条
     *
     * @param canvas
     */
    private void drawCenterStyle(Canvas canvas) {
        startX = getWidth() / 2;
        startY = getHeight() / 2;
        //绘制背景
        paint.setColor(progressBarColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, startY - progressBarHeight / 2,
                startX + getWidth() / 2, startY + progressBarHeight / 2), progressRound, progressRound, paint);
        //绘制进度背景
        float rectWidth = progress * getWidth() / progressMax;
        paint.setColor(progressColor);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, startY - progressBarHeight / 2,
                rectWidth, startY + progressBarHeight / 2), progressRound, progressRound, paint);

        if (showPercent) {//如果不显示文字，则不用绘制
            //计算percent宽高
            String percent = progress + "%";
            paint.setTextSize(percentTextSize);
            Rect rect = new Rect();
            paint.getTextBounds(percent, 0, percent.length(), rect);
            int percentWidth = rect.width();
            int percentHeight = rect.height();
            int x = progress * (getWidth() - percentWidth - percentPadding) / progressMax;
            if (x + percentWidth + percentPadding > getWidth()) {//判断如果进度值越界，设置最大值
                x = getWidth() - percentWidth - percentPadding;
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(percentBgColor);
            canvas.drawRect(new RectF(x - percentPadding, startY,
                    x + percentWidth, startY), paint);
            canvas.drawRect(new RectF(x - percentPadding, startY - percentHeight / 2 - percentPadding, x + percentWidth + percentPadding, startY + startY - percentHeight / 2 + percentPadding), paint);

            //绘制文字
            paint.setColor(percentTextColor);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setTextSize(percentTextSize);
            canvas.drawText(percent, x, startY + percentHeight / 2, paint);
        }
    }

    /**
     * 绘制进度百分比在底部的进度条
     *
     * @param canvas
     */
    private void drawBottomStyle(Canvas canvas) {
        //绘制进度条背景
        paint.setColor(progressBarColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, 0,
                startX + getWidth() / 2, progressBarHeight), progressRound, progressRound, paint);
        float rectWidth = progress * getWidth() / progressMax;
        //绘制进度背景
        paint.setColor(progressColor);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, 0,
                rectWidth, progressBarHeight), progressRound, progressRound, paint);
        //计算百分比
        paint.setTextSize(percentTextSize);
        String percent = progress + "%";
        Rect rect = new Rect();
        paint.getTextBounds(percent, 0, percent.length(), rect);
        int percentHeight = rect.height();
        int percentWidth = rect.width();
        int x = progress * (getWidth() - percentWidth - percentPadding) / progressMax;
        if (x + percentWidth + percentPadding > getWidth()) {//判断如果进度值越界，设置最大值
            x = getWidth() - percentWidth - percentPadding;
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(percentBgColor);
        canvas.drawRect(new RectF(x - percentPadding, 0 + progressBarHeight + distanceTextBar - percentPadding,
                x + percentWidth + percentPadding, 0 + progressBarHeight + distanceTextBar + percentPadding + percentHeight), paint);
        //绘制文字
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(percentTextSize);
        paint.setColor(percentTextColor);
        canvas.drawText(percent, x, 0 + progressBarHeight + distanceTextBar + percentHeight, paint);
    }

    /**
     * 绘制进度百分比在顶部的进度条
     *
     * @param canvas
     */
    private void drawTopStyle(Canvas canvas) {
        //绘制进度条背景
        paint.setColor(progressBarColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, startY - progressBarHeight,
                startX + getWidth() / 2, startY), progressRound, progressRound, paint);
        float rectWidth = progress * getWidth() / progressMax;

        //绘制进度背景
        paint.setColor(progressColor);
        canvas.drawRoundRect(new RectF(startX - getWidth() / 2, startY - progressBarHeight,
                rectWidth, startY), progressRound, progressRound, paint);

        //绘制进度值
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(percentTextColor);
        paint.setTextSize(percentTextSize);
        String percent = progress + "%";
        //计算进度值的宽高
        Rect rect = new Rect();
        paint.getTextBounds(percent, 0, percent.length(), rect);
        //获取到文字的宽高
        int percentWidth = rect.width();
        int percentHeight = rect.height();


        //绘制文字背景
        paint.setColor(percentBgColor);
        paint.setStyle(Paint.Style.FILL);
        float x = progress * (getWidth() - percentHeight - percentPadding) / progressMax;
        if (x + percentWidth + percentPadding >= getWidth()) {
            x = getWidth() - percentWidth - percentPadding;
        }
        canvas.drawRect(new RectF(x - percentPadding, startY - percentHeight - progressBarHeight - distanceTextBar - percentPadding,
                x + percentWidth + percentPadding, startY - progressBarHeight - distanceTextBar + percentPadding), paint);
        //绘制文字
        paint.setColor(percentTextColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(percentTextSize);
        canvas.drawText(percent, x, startY - progressBarHeight - distanceTextBar, paint);
    }

    private float getTextHeight(String percent) {
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.setTextSize(percentTextSize);
        paint.getTextBounds(percent, 0, percent.length(), rect);
        return rect.height();
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress > progressMax) {
            this.progress = progressMax;
        } else {
            this.progress = progress;
            postInvalidate();
        }
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 设置样式
     *
     * @param style
     * @return
     */
    public NumberHorizontalProgressBar setProgressBarStyle(int style) {
        if (style != TOP && style != CENTER && style != BOTTOM)
            style = TOP;
        this.progressBarStyle = style;
        return this;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getProgressBarStyle() {
        return progressBarStyle;
    }

    /**
     * 获取进度值字体大小：px
     *
     * @return
     */
    public int getTextSize() {
        return percentTextSize;
    }

    /**
     * 设置进去值字体大小：px
     *
     * @param textSize
     * @return
     */
    public NumberHorizontalProgressBar setTextSize(int textSize) {
        this.percentTextSize = textSize;
        return this;
    }

    /**
     * 获取进度值颜色值
     *
     * @return
     */
    public int getTextColor() {
        return percentTextColor;
    }

    /**
     * 设置进度值颜色值
     *
     * @param textColor
     * @return
     */
    public NumberHorizontalProgressBar setTextColor(int textColor) {
        this.percentTextColor = textColor;
        return this;
    }

    /**
     * 获取进度条背景颜色
     *
     * @return
     */
    public int getProgressBarBgColor() {
        return progressBarColor;
    }

    /**
     * 设置进度条背景
     *
     * @param progressBarBgColor
     * @return
     */
    public NumberHorizontalProgressBar setProgressBarBgColor(int progressBarBgColor) {
        this.progressBarColor = progressBarBgColor;
        return this;
    }

    /**
     * 设置进度背景颜色
     *
     * @param progressColor
     * @return
     */
    public NumberHorizontalProgressBar setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    /**
     * 设置进度条圆角弧度
     *
     * @param round
     * @return
     */
    public NumberHorizontalProgressBar setProgressRound(float round) {
        this.progressRound = round;
        return this;
    }

    public float getProgressRound() {
        return progressRound;
    }

    /**
     * 设置是否显示进度值
     *
     * @param show
     * @return
     */
    public NumberHorizontalProgressBar setShowPrecent(boolean show) {
        this.showPercent = show;
        return this;
    }

    public boolean getShowPrecent() {
        return showPercent;
    }

    /**
     * 设置进度条高度
     *
     * @param height
     * @return
     */
    public NumberHorizontalProgressBar setBarHeight(int height) {
        this.progressBarHeight = height;
        return this;
    }

    public int getBarHeight() {
        return progressBarHeight;
    }

    /**
     * 设置最大进度值
     *
     * @param max
     * @return
     */
    public NumberHorizontalProgressBar setMax(int max) {
        this.progressMax = max;
        return this;
    }

    public int getProgressMax() {
        return progressMax;
    }

    /**
     * 设置文字与进度条之间的距离
     *
     * @param distance
     * @return
     */
    public NumberHorizontalProgressBar setTextBarDistance(int distance) {
        this.distanceTextBar = distance;
        return this;
    }

    public int getTextBarDistance() {
        return distanceTextBar;
    }

    /**
     * 设置文字与背景的边距
     *
     * @param padding
     * @return
     */
    public NumberHorizontalProgressBar setPadding(int padding) {
        this.percentPadding = padding;
        return this;
    }

    public int getPadding() {
        return percentPadding;
    }

}
