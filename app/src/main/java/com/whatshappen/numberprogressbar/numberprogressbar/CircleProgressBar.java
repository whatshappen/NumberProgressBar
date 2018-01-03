package com.whatshappen.numberprogressbar.numberprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.whatshappen.numberprogressbar.R;

/**
 * @author ww ;
 * @data on 2017/12/27.
 */
public class CircleProgressBar extends View {

    private static final int STANDARD = 0;
    private static final int CENTER = 1;

    private static final String TAG = "CircleProgressBar";
    /**
     * 进度提示文字颜色
     */
    private int percentTextColor = 0xff1C8BFB;

    /**
     * 进度值字体大小
     */
    private int percentTextSize;
    /**
     * 进度条背景颜色
     */
    private int progressBarBgColor = 0x661f1f1f;
    /**
     * 进度颜色
     */
    private int progressColor = 0xff1C8BFB;
    /**
     * 最大进度值
     */
    private int progressMax = 100;
    /**
     * 进度值与进度条外边界距离
     */
    private int distanceTextBar;
    /**
     * 进度条宽度
     */
    private int strokeWidth;
    /**
     * 进度条中心X坐标
     **/
    private int centerX;
    /**
     * 进度条中心Y坐标
     **/
    private int centerY;
    /**
     * 圆形进度条半径
     **/
    private int radius;
    private Paint paint;
    private int progress;//当前进度

    private int startRound = 0;
    private int minWidth;
    private Context context;

    private int progressBarStyle;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //设置默认数据
        percentTextSize = UIUtils.dp2px(context, 12);
        distanceTextBar = UIUtils.dp2px(context, 5);
        strokeWidth = UIUtils.dp2px(context, 3);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        percentTextColor = typedArray.getColor(R.styleable.CircleProgressBar_percent_color, percentTextColor);
        percentTextSize = (int) typedArray.getDimension(R.styleable.CircleProgressBar_percent_size, percentTextSize);
        progressBarBgColor = typedArray.getColor(R.styleable.CircleProgressBar_progress_bar_bg, progressBarBgColor);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBar_progress_bg, progressColor);
        progressMax = typedArray.getInt(R.styleable.CircleProgressBar_progress_max, progressMax);
        distanceTextBar = (int) typedArray.getDimension(R.styleable.CircleProgressBar_percent_size, distanceTextBar);
        strokeWidth = (int) typedArray.getDimension(R.styleable.CircleProgressBar_stroke_width_cir, strokeWidth);
        startRound = typedArray.getInt(R.styleable.CircleProgressBar_progress_bar_start_round, startRound);
        progressBarStyle = typedArray.getInt(R.styleable.CircleProgressBar_progress_style, progressBarStyle);
        typedArray.recycle();
        //初始化paint
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);//设置抗锯齿
        minWidth = (int) (strokeWidth + getTextWidth("100%") + distanceTextBar + getTextHeight("100%"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (progressBarStyle == STANDARD) {
            drawStandard(canvas);
        } else if (progressBarStyle == CENTER) {
            drawCenter(canvas);
        }
    }

    private void drawCenter(Canvas canvas) {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = centerX - strokeWidth / 2;

        // 初始化画笔属性
        paint.setColor(progressBarBgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        // 画圆
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(centerX, centerY, radius, paint);

        // 画圆形进度
        paint.setColor(progressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        RectF oval = new RectF(centerX - radius, centerY - radius, radius + centerX, radius + centerY);
        canvas.drawArc(oval, startRound, 360 * progress / progressMax, false, paint);
        // 画进度文字
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(progressColor);
        paint.setTextSize(percentTextSize);

        String percent = (int) (progress * 100 / progressMax) + "%";
        Rect rect = new Rect();
        paint.getTextBounds(percent, 0, percent.length(), rect);
        float textWidth = rect.width();
        if (textWidth >= radius * 2) {
            textWidth = radius * 2;
        }
        //计算文字在圆中心的y坐标
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float baseline = (getMeasuredHeight() - metrics.bottom + metrics.top) / 2 - metrics.top;
        //计算文字的偏移量
        //绘制进度
        canvas.drawText(percent, centerX - textWidth / 2,
                baseline, paint);
    }

    private void drawStandard(Canvas canvas) {
        Log.e(TAG, "onDraw: getHeight() =" + getHeight() + "===minWidth =" + minWidth);
        //判断，如果整体高度小于textHeight+progressBarHeight+distanceTextBar
        if (minWidth > getHeight()) {
            throw new IllegalStateException("NumberHorizontalProgressBar" +
                    " Height is too small，最小高度值为：" +
                    UIUtils.px2dp(context, minWidth + 1) + "dp");
        }
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (int) (centerX - strokeWidth - getTextWidth("100%") - distanceTextBar - getTextHeight("100%"));//要满足x轴全部显示
        // 初始化画笔属性
        paint.setColor(progressBarBgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        // 画圆
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawCircle(centerX, centerY, radius, paint);

        // 画圆形进度
        paint.setColor(progressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        RectF oval = new RectF(centerX - radius, centerY - radius, radius + centerX, radius + centerY);
        canvas.drawArc(oval, startRound, 360 * progress / progressMax, false, paint);
        // 画进度文字
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(progressColor);
        paint.setTextSize(percentTextSize);

        String percent = (progress * 100 / progressMax) + "%";
        Rect rect = new Rect();
        paint.getTextBounds(percent, 0, percent.length(), rect);
        float textWidth = rect.width();
        float textHeight = rect.height();
        if (textWidth >= radius * 2) {
            textWidth = radius * 2;
        }
        //计算文字的偏移量
        int currentRound = startRound + 360 * progress / progressMax;
        float x1 = (float) ((radius + strokeWidth + distanceTextBar + textWidth / 2) * Math.cos(currentRound * Math.PI / 180));
        float y1 = (float) ((radius + strokeWidth + distanceTextBar + textWidth / 2) * Math.sin(currentRound * Math.PI / 180));

        //绘制进度
        canvas.drawText(percent, centerX + x1 - textWidth / 2, centerY + y1 + textHeight / 2, paint);
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
     * 设置最大进度
     *
     * @param max
     */
    public void setProgressMax(int max) {
        this.progressMax = max;
    }

    /**
     * 获取进度最大值
     *
     * @return
     */
    public int getProgressMax() {
        return progressMax;
    }

    /**
     * 获取圆环进度条宽度
     *
     * @return
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * 设置圆环款宽度
     *
     * @param strokeWidth
     * @return
     */
    public CircleProgressBar setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
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
    public CircleProgressBar setTextSize(int textSize) {
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
    public CircleProgressBar setTextColor(int textColor) {
        this.percentTextColor = textColor;
        return this;
    }

    /**
     * 获取进度条背景颜色
     *
     * @return
     */
    public int getProgressBarBgColor() {
        return progressBarBgColor;
    }

    /**
     * 设置进度条背景
     *
     * @param progressBarBgColor
     * @return
     */
    public CircleProgressBar setProgressBarBgColor(int progressBarBgColor) {
        this.progressBarBgColor = progressBarBgColor;
        return this;
    }

    /**
     * 设置进度背景颜色
     *
     * @param progressColor
     * @return
     */
    public CircleProgressBar setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public int getProgressColor() {
        return progressColor;
    }

    /**
     * 设置初始角度
     *
     * @param round
     * @return
     */
    public CircleProgressBar setStartRound(int round) {
        this.startRound = round;
        return this;
    }

    /**
     * 返回初始角度
     *
     * @return
     */
    public int getStartRound() {
        return startRound;
    }

    /**
     * 设置文字与进度条之间的距离
     *
     * @param distance
     * @return
     */
    public CircleProgressBar setTextBarDistance(int distance) {
        this.distanceTextBar = distance;
        return this;
    }

    public int getTextBarDistance() {
        return distanceTextBar;
    }

    private float getTextHeight(String percent) {
        Rect rect = new Rect();
        paint.setTextSize(percentTextSize);
        paint.getTextBounds(percent, 0, percent.length(), rect);
        return rect.height();
    }

    private float getTextWidth(String percent) {
        Rect rect = new Rect();
        paint.setTextSize(percentTextSize);
        paint.getTextBounds(percent, 0, percent.length(), rect);
        return rect.width();
    }
}
