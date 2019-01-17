/*
 *
 * Copyright 2013 Matt Joseph
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * This custom view/widget was inspired and guided by:
 *
 * HoloCircleSeekBar - Copyright 2012 Jes�s Manzano
 * HoloColorPicker - Copyright 2012 Lars Werkman (Designed by Marie Schweiz)
 *
 * Although I did not used the code from either project directly, they were both used as
 * reference material, and as a result, were extremely helpful.
 */

package demo.com.demosurvey.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import demo.com.demosurvey.R;

public class CircularSeekBar extends View {

    // Default values
    protected static final float DEFAULT_CIRCLE_X_RADIUS = 30f;
    protected static final float DEFAULT_CIRCLE_Y_RADIUS = 30f;
    protected static final float DEFAULT_POINTER_RADIUS = 7f;
    protected static final float DEFAULT_POINTER_HALO_WIDTH = 6f;
    protected static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2f;
    protected static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5f;
    protected static final float DEFAULT_START_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
    protected static final float DEFAULT_END_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
    protected static final int DEFAULT_MAX = 100;
    protected static final int DEFAULT_PROGRESS = 0;
    protected static final int DEFAULT_CIRCLE_COLOR = Color.DKGRAY;
    protected static final int DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255);
    protected static final int DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
    protected static final int DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255);
    protected static final int DEFAULT_POINTER_HALO_COLOR_ONTOUCH = Color.argb(135, 74, 138, 255);
    protected static final int DEFAULT_CIRCLE_FILL_COLOR = Color.TRANSPARENT;
    protected static final int DEFAULT_POINTER_ALPHA = 135;
    protected static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
    protected static final int DEFAULT_POINTER_ADDITIONAL = 30;
    protected static final boolean DEFAULT_USE_CUSTOM_RADII = false;
    protected static final boolean DEFAULT_MAINTAIN_EQUAL_CIRCLE = true;
    protected static final boolean DEFAULT_MOVE_OUTSIDE_CIRCLE = false;
    protected static final boolean DEFAULT_LOCK_ENABLED = true;

    private static final String TAG = CircularSeekBar.class.getSimpleName();
    /**
     * Used to scale the dp units to pixels
     */
    protected final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;
    /**
     * Minimum touch target size in DP. 48dp is the Android design recommendation
     */
    protected final float MIN_TOUCH_TARGET_DP = 48;
    public TextPaint textProgress;
    /**
     * {@code Paint} instance used to draw the inactive circle.
     */
    protected Paint mCirclePaint;
    /**
     * {@code Paint} instance used to draw the circle fill.
     */
    protected Paint mCircleFillPaint;
    /**
     * {@code Paint} instance used to draw the active circle (represents progress).
     */
    protected Paint mCircleProgressPaint;
    /**
     * {@code Paint} instance used to draw the glow from the active circle.
     */
    protected Paint mCircleProgressGlowPaint;
    /**
     * {@code Paint} instance used to draw the center of the pointer.
     * Note: This is broken on 4.0+, as BlurMasks do not work with hardware acceleration.
     */
    protected Paint mPointerPaint;
    /**
     * {@code Paint} instance used to draw the halo of the pointer.
     * Note: The halo is the part that changes transparency.
     */
    protected Paint mPointerHaloPaint;
    /**
     * {@code Paint} instance used to draw the border of the pointer, outside of the halo.
     */
    protected Paint mPointerHaloBorderPaint;
    /**
     * The width of the circle (in pixels).
     */
    protected float mCircleStrokeWidth;
    /**
     * The X radius of the circle (in pixels).
     */
    protected float mCircleXRadius;
    /**
     * The Y radius of the circle (in pixels).
     */
    protected float mCircleYRadius;
    /**
     * The radius of the pointer (in pixels).
     */
    protected float mPointerRadius;
    /**
     * The width of the pointer halo (in pixels).
     */
    protected float mPointerHaloWidth;
    /**
     * The width of the pointer halo border (in pixels).
     */
    protected float mPointerHaloBorderWidth;
    /**
     * Start angle of the CircularSeekBar.
     * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted
     * from the mEndAngle to make the circle function properly.
     */
    protected float mStartAngle;
    /**
     * End angle of the CircularSeekBar.
     * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted
     * from the mEndAngle to make the circle function properly.
     */
    protected float mEndAngle;
    /**
     * {@code RectF} that represents the circle (or ellipse) of the seekbar.
     */
    protected RectF mCircleRectF = new RectF();
    protected RectF mCircleArrowRectF = new RectF();
    /**
     * Holds the color value for {@code mPointerPaint} before the {@code Paint} instance is created.
     */
    protected int mPointerColor = DEFAULT_POINTER_COLOR;
    /**
     * Holds the color value for {@code mPointerHaloPaint} before the {@code Paint} instance is created.
     */
    protected int mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;
    /**
     * Holds the color value for {@code mPointerHaloPaint} before the {@code Paint} instance is created.
     */
    protected int mPointerHaloColorOnTouch = DEFAULT_POINTER_HALO_COLOR_ONTOUCH;
    /**
     * Holds the color value for {@code mCirclePaint} before the {@code Paint} instance is created.
     */
    protected int mCircleColor = DEFAULT_CIRCLE_COLOR;
    /**
     * Holds the color value for {@code mCircleFillPaint} before the {@code Paint} instance is created.
     */
    protected int mCircleFillColor = DEFAULT_CIRCLE_FILL_COLOR;
    /**
     * Holds the color value for {@code mCircleProgressPaint} before the {@code Paint} instance is created.
     */
    protected int mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;
    /**
     * Holds the alpha value for {@code mPointerHaloPaint}.
     */
    protected int mPointerAlpha = DEFAULT_POINTER_ALPHA;
    /**
     * Holds the OnTouch alpha value for {@code mPointerHaloPaint}.
     */
    protected int mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;
    /**
     * Distance (in degrees) that the the circle/semi-circle makes up.
     * This amount represents the max of the circle in degrees.
     */
    protected float mTotalCircleDegrees;
    /**
     * Distance (in degrees) that the current progress makes up in the circle.
     */
    protected float mProgressDegrees;
    /**
     * {@code Path} used to draw the circle/semi-circle.
     */
    protected Path mCirclePath;
    protected Path mCircleArrowPath;
    protected Path mCircleArrowSolidPath;
    protected float mPointerAdditionalRadius = DEFAULT_POINTER_ADDITIONAL;
    /**
     * {@code Path} used to draw the progress on the circle.
     */
    protected Path mCircleProgressPath;
    /**
     * Max value that this CircularSeekBar is representing.
     */
    protected int mMax;
    /**
     * Progress value that this CircularSeekBar is representing.
     */
    protected int mProgress;
    /**
     * If true, then the user can specify the X and Y radii.
     * If false, then the View itself determines the size of the CircularSeekBar.
     */
    protected boolean mCustomRadii;
    /**
     * Maintain a perfect circle (equal x and y radius), regardless of view or custom attributes.
     * The smaller of the two radii will always be used in this case.
     * The default is to be a circle and not an ellipse, due to the behavior of the ellipse.
     */
    protected boolean mMaintainEqualCircle;
    /**
     * Once a user has touched the circle, this determines if moving outside the circle is able
     * to change the position of the pointer (and in turn, the progress).
     */
    protected boolean mMoveOutsideCircle;
    /**
     * Used for enabling/disabling the lock option for easier hitting of the 0 progress mark.
     */
    protected boolean lockEnabled = true;
    /**
     * Used for when the user moves beyond the start of the circle when moving counter clockwise.
     * Makes it easier to hit the 0 progress mark.
     */
    protected boolean lockAtStart = true;
    /**
     * Used for when the user moves beyond the end of the circle when moving clockwise.
     * Makes it easier to hit the 100% (max) progress mark.
     */
    protected boolean lockAtEnd = false;
    /**
     * When the user is touching the circle on ACTION_DOWN, this is set to true.
     * Used when touching the CircularSeekBar.
     */
    protected boolean mUserIsMovingPointer = false;
    /**
     * Represents the clockwise distance from {@code mStartAngle} to the touch angle.
     * Used when touching the CircularSeekBar.
     */
    protected float cwDistanceFromStart;
    /**
     * Represents the counter-clockwise distance from {@code mStartAngle} to the touch angle.
     * Used when touching the CircularSeekBar.
     */
    protected float ccwDistanceFromStart;
    /**
     * Represents the clockwise distance from {@code mEndAngle} to the touch angle.
     * Used when touching the CircularSeekBar.
     */
    protected float cwDistanceFromEnd;
    /**
     * Represents the counter-clockwise distance from {@code mEndAngle} to the touch angle.
     * Used when touching the CircularSeekBar.
     * Currently unused, but kept just in case.
     */
    @SuppressWarnings("unused")
    protected float ccwDistanceFromEnd;
    /**
     * The previous touch action value for {@code cwDistanceFromStart}.
     * Used when touching the CircularSeekBar.
     */
    protected float lastCWDistanceFromStart;
    /**
     * Represents the clockwise distance from {@code mPointerPosition} to the touch angle.
     * Used when touching the CircularSeekBar.
     */
    protected float cwDistanceFromPointer;
    /**
     * Represents the counter-clockwise distance from {@code mPointerPosition} to the touch angle.
     * Used when touching the CircularSeekBar.
     */
    protected float ccwDistanceFromPointer;
    /**
     * True if the user is moving clockwise around the circle, false if moving counter-clockwise.
     * Used when touching the CircularSeekBar.
     */
    protected boolean mIsMovingCW;
    /**
     * The width of the circle used in the {@code RectF} that is used to draw it.
     * Based on either the View width or the custom X radius.
     */
    protected float mCircleWidth;
    protected float mCircleArrowWidth;
    /**
     * The height of the circle used in the {@code RectF} that is used to draw it.
     * Based on either the View width or the custom Y radius.
     */
    protected float mCircleHeight;
    protected float mCircleArrowHeight;
    /**
     * Represents the progress mark on the circle, in geometric degrees.
     * This is not provided by the user; it is calculated;
     */
    protected float mPointerPosition;
    /**
     * Pointer position in terms of X and Y coordinates.
     */
    protected float[] mPointerPositionXY = new float[2];
    /**
     * Listener.
     */
    protected OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;
    /**
     * True if user touch input is enabled, false if user touch input is ignored.
     * This does not affect setting values programmatically.
     */
    protected boolean isTouchEnabled = true;
    Bitmap bitmap;
    Paint paint;
    Paint paintSolid;
    TextPaint textPaint;
    String mTextCenter;
    private Matrix mMatrix;


    /*String[] textCenter = new String[]{"Press and rotate circle to your rating", "Not at all", "2", "3", "A little", "5",
            "6", "Totally"};*/

    public CircularSeekBar(Context context) {
        super(context);
        init(null, 0);
    }

    public CircularSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircularSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Initialize the CircularSeekBar with the attributes from the XML style.
     * Uses the defaults defined at the top of this file when an attribute is not specified by the user.
     *
     * @param attrArray TypedArray containing the attributes.
     */
    protected void initAttributes(TypedArray attrArray) {
        mCircleXRadius = attrArray.getDimension(R.styleable.CircularSeekBar_circle_x_radius, DEFAULT_CIRCLE_X_RADIUS * DPTOPX_SCALE);
        mCircleYRadius = attrArray.getDimension(R.styleable.CircularSeekBar_circle_y_radius, DEFAULT_CIRCLE_Y_RADIUS * DPTOPX_SCALE);
        mPointerRadius = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_radius, DEFAULT_POINTER_RADIUS * DPTOPX_SCALE);
        mPointerHaloWidth = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_halo_width, DEFAULT_POINTER_HALO_WIDTH * DPTOPX_SCALE);
        mPointerHaloBorderWidth = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_halo_border_width, DEFAULT_POINTER_HALO_BORDER_WIDTH * DPTOPX_SCALE);
        mCircleStrokeWidth = attrArray.getDimension(R.styleable.CircularSeekBar_circle_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH * DPTOPX_SCALE);

        mPointerColor = attrArray.getColor(R.styleable.CircularSeekBar_pointer_color, DEFAULT_POINTER_COLOR);
        mPointerHaloColor = attrArray.getColor(R.styleable.CircularSeekBar_pointer_halo_color, DEFAULT_POINTER_HALO_COLOR);
        mPointerHaloColorOnTouch = attrArray.getColor(R.styleable.CircularSeekBar_pointer_halo_color_ontouch, DEFAULT_POINTER_HALO_COLOR_ONTOUCH);
        mCircleColor = attrArray.getColor(R.styleable.CircularSeekBar_circle_color, DEFAULT_CIRCLE_COLOR);
        mCircleProgressColor = attrArray.getColor(R.styleable.CircularSeekBar_circle_progress_color, DEFAULT_CIRCLE_PROGRESS_COLOR);
        mCircleFillColor = attrArray.getColor(R.styleable.CircularSeekBar_circle_fill, DEFAULT_CIRCLE_FILL_COLOR);

        mPointerAlpha = Color.alpha(mPointerHaloColor);

        mPointerAlphaOnTouch = attrArray.getInt(R.styleable.CircularSeekBar_pointer_alpha_ontouch, DEFAULT_POINTER_ALPHA_ONTOUCH);
        if (mPointerAlphaOnTouch > 255 || mPointerAlphaOnTouch < 0) {
            mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;
        }

        mPointerAdditionalRadius = attrArray.getDimension(R.styleable.CircularSeekBar_pointer_radius_additional, DEFAULT_POINTER_ADDITIONAL);
        mTextCenter = attrArray.getString(R.styleable.CircularSeekBar_text_center);
        mMax = attrArray.getInt(R.styleable.CircularSeekBar_max, DEFAULT_MAX);
        mProgress = attrArray.getInt(R.styleable.CircularSeekBar_progress, DEFAULT_PROGRESS);
        mCustomRadii = attrArray.getBoolean(R.styleable.CircularSeekBar_use_custom_radii, DEFAULT_USE_CUSTOM_RADII);
        mMaintainEqualCircle = attrArray.getBoolean(R.styleable.CircularSeekBar_maintain_equal_circle, DEFAULT_MAINTAIN_EQUAL_CIRCLE);
        mMoveOutsideCircle = attrArray.getBoolean(R.styleable.CircularSeekBar_move_outside_circle, DEFAULT_MOVE_OUTSIDE_CIRCLE);
        lockEnabled = attrArray.getBoolean(R.styleable.CircularSeekBar_lock_enabled, DEFAULT_LOCK_ENABLED);

        // Modulo 360 right now to avoid constant conversion
        mStartAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_start_angle), DEFAULT_START_ANGLE) % 360f)) % 360f);
        mEndAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_end_angle), DEFAULT_END_ANGLE) % 360f)) % 360f);

        if (mStartAngle == mEndAngle) {
            //mStartAngle = mStartAngle + 1f;
            mEndAngle = mEndAngle - .1f;
        }
    }

    /**
     * Initializes the {@code Paint} objects with the appropriate styles.
     */
    protected void initPaints() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        //Inner circle color
        mCirclePaint.setColor(getResources().getColor(android.R.color.white));
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mCircleFillPaint = new Paint();
        mCircleFillPaint.setAntiAlias(true);
        mCircleFillPaint.setDither(true);
        mCircleFillPaint.setColor(mCircleFillColor);
        mCircleFillPaint.setStyle(Paint.Style.FILL);

        mCircleProgressPaint = new Paint();
        mCircleProgressPaint.setAntiAlias(true);
        mCircleProgressPaint.setDither(true);
        // Progress color
        mCircleProgressPaint.setColor(getResources().getColor(android.R.color.white));
        mCircleProgressPaint.setStrokeWidth(mCircleStrokeWidth);
        mCircleProgressPaint.setStyle(Paint.Style.STROKE);
        mCircleProgressPaint.setStrokeJoin(Paint.Join.ROUND);
        mCircleProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mCircleProgressGlowPaint = new Paint();
        mCircleProgressGlowPaint.set(mCircleProgressPaint);
        mCircleProgressGlowPaint.setMaskFilter(new BlurMaskFilter((5f * DPTOPX_SCALE), BlurMaskFilter.Blur.NORMAL));

        mPointerPaint = new Paint();
        mPointerPaint.setAntiAlias(true);
        mPointerPaint.setDither(true);
        mPointerPaint.setStyle(Paint.Style.FILL);
        mPointerPaint.setColor(getResources().getColor(android.R.color.white));
        mPointerPaint.setStrokeWidth(mPointerRadius);

        mPointerHaloPaint = new Paint();
        mPointerHaloPaint.set(mPointerPaint);
        mPointerHaloPaint.setColor(mPointerHaloColor);
        mPointerHaloPaint.setAlpha(mPointerAlpha);
        mPointerHaloPaint.setStrokeWidth(mPointerRadius + mPointerHaloWidth);

        mPointerHaloBorderPaint = new Paint();
        mPointerHaloBorderPaint.set(mPointerPaint);
        mPointerHaloBorderPaint.setStrokeWidth(mPointerHaloBorderWidth);
        mPointerHaloBorderPaint.setStyle(Paint.Style.STROKE);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 1.0f);
        paint.setPathEffect(dashPathEffect);
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) (mCircleStrokeWidth * 0.20));

        paintSolid = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSolid.setStyle(Paint.Style.FILL);
        paintSolid.setColor(getResources().getColor(android.R.color.white));
        paintSolid.setStyle(Paint.Style.STROKE);
        paintSolid.setStrokeWidth((float) (mCircleStrokeWidth * 0.60));

        textProgress = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textProgress.setAntiAlias(true);
        textProgress.setTextSize(16 * getResources().getDisplayMetrics().density);
        textProgress.setColor(getResources().getColor(R.color.colorWhite));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        textPaint.setColor(getResources().getColor(R.color.colorWhite));
    }

    private float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * Calculates the total degrees between mStartAngle and mEndAngle, and sets mTotalCircleDegrees
     * to this value.
     */
    protected void calculateTotalDegrees() {
        mTotalCircleDegrees = (360f - (mStartAngle - mEndAngle)) % 360f; // Length of the entire circle/arc
        if (mTotalCircleDegrees <= 0f) {
            mTotalCircleDegrees = 360f;
        }
    }

    /**
     * Calculate the degrees that the progress represents. Also called the sweep angle.
     * Sets mProgressDegrees to that value.
     */
    protected void calculateProgressDegrees() {
        mProgressDegrees = mPointerPosition - mStartAngle; // Verified
        mProgressDegrees = (mProgressDegrees < 0 ? 360f + mProgressDegrees : mProgressDegrees); // Verified
    }

    /**
     * Calculate the pointer position (and the end of the progress arc) in degrees.
     * Sets mPointerPosition to that value.
     */
    protected void calculatePointerAngle() {
        float progressPercent = ((float) mProgress / (float) mMax);
//        Log.e(TAG, "progressPercent:::::::: "+ progressPercent);
        mPointerPosition = (progressPercent * mTotalCircleDegrees) + mStartAngle;
        mPointerPosition = mPointerPosition % 360f;
    }

    protected void calculatePointerXYPosition() {
        PathMeasure pm = new PathMeasure(mCircleProgressPath, false);
        boolean returnValue = pm.getPosTan(pm.getLength(), mPointerPositionXY, null);
        if (!returnValue) {
            pm = new PathMeasure(mCirclePath, false);
            returnValue = pm.getPosTan(0, mPointerPositionXY, null);
        }
    }

    /**
     * Initialize the {@code Path} objects with the appropriate values.
     */
    protected void initPaths() {

        mMatrix = new Matrix();
        mCirclePath = new Path();
        mCirclePath.addArc(mCircleRectF, mStartAngle, mTotalCircleDegrees);

        mCircleArrowPath = new Path();
        mCircleArrowPath.addArc(mCircleArrowRectF, mStartAngle, mTotalCircleDegrees);

        mCircleArrowSolidPath = new Path();
        mCircleArrowSolidPath.addArc(mCircleArrowRectF, mStartAngle + mProgressDegrees, 60);

        Log.i("initPaths", "mProgressDegrees: " + mProgressDegrees);

        mCircleProgressPath = new Path();
        mCircleProgressPath.addArc(mCircleRectF, mStartAngle, mProgressDegrees);
    }

    /**
     * Initialize the {@code RectF} objects with the appropriate values.
     */
    protected void initRects() {
        Log.i("initRects", "mCircleWidth: " + mCircleWidth + " mCircleHeight: " + mCircleHeight);
        mCircleArrowRectF.set(-mCircleWidth, -mCircleHeight, mCircleWidth, mCircleHeight);
        Log.i("initRects", "mCircleArrowWidth: " + mCircleArrowWidth + " mCircleArrowHeight: " + mCircleArrowHeight);
        mCircleRectF.set(-mCircleArrowWidth, -mCircleArrowHeight, mCircleArrowWidth, mCircleArrowHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap pBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.circle2);
        double bmpHeight = 2.4 * mCircleHeight;
        double bmpWidth = 2.4 * mCircleWidth;
        Bitmap pBitmap = Bitmap.createScaledBitmap(pBitmap1, (int) bmpWidth, (int) bmpHeight, false);
        if (pBitmap == null) return;
        // Use the same Matrix over and over again to minimize
        // allocation in onDraw.
        mMatrix.reset();

        float rotation = mEndAngle + mProgressDegrees + 90;
//        rotation += mEndAngle + mProgressDegrees + 60;
        float px = this.getWidth() / 2;
        float py = this.getHeight() / 2;
        mMatrix.postTranslate(-pBitmap.getWidth() / 2, -pBitmap.getHeight() / 2);
        mMatrix.postRotate(rotation);
        mMatrix.postTranslate(px, py);
        canvas.drawBitmap(pBitmap, mMatrix, null);
        canvas.translate(this.getWidth() / 2, this.getHeight() / 2);
//        Log.e("CircularSeekBar", "onDraw: " + mProgressDegrees);

        canvas.drawPath(mCirclePath, mCirclePaint);
        canvas.drawPath(mCircleProgressPath, mCircleProgressGlowPaint);

        canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerAdditionalRadius, mPointerPaint);

        float progressDecimal = getProgress() / 30;
        Log.e(TAG, "getProgress::::  " + progressDecimal);

        if (getProgress() / 30 < 1) {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hand_tool);
            canvas.drawBitmap(bmp, mPointerPositionXY[0] - 10 - mPointerAdditionalRadius / 2,
                    mPointerPositionXY[1] - 9 - mPointerAdditionalRadius / 2, null);
        }

        int xPos = (int) mPointerPositionXY[0];
        int yPos = (int) (mPointerPositionXY[1] - ((textProgress.descent() + textProgress.ascent()) / 2));
//((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        if (getProgress() / 30 > 0) {
            canvas.drawText(getProgress() / 30 + "", xPos, yPos, textProgress);
        }

        Rect bounds = canvas.getClipBounds();
        StaticLayout sl = new StaticLayout(mTextCenter, textPaint, bounds.width(), Layout.Alignment.ALIGN_CENTER,
                1, 1, true);
        canvas.save();
        //text height and number of lines to move Y coordinate to center.
        float textHeight = getTextHeight(mTextCenter, textPaint);
        int numberOfTextLines = sl.getLineCount();
        float textYCoordinate = bounds.exactCenterY() - ((numberOfTextLines * textHeight) / 2);

        //text will be drawn from left
        float textXCoordinate = bounds.left;
        canvas.translate(textXCoordinate, textYCoordinate);

        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();
    }


    /**
     * Get the progress of the CircularSeekBar.
     *
     * @return The progress of the CircularSeekBar.
     */
    public int getProgress() {
        int progress = Math.round((float) mMax * mProgressDegrees / mTotalCircleDegrees);
        return progress;
    }

    /**
     * Set the progress of the CircularSeekBar.
     * If the progress is the same, then any listener will not receive a onProgressChanged event.
     *
     * @param progress The progress to set the CircularSeekBar to.
     */
    public void setProgress(int progress) {
        if (mProgress != progress) {
            mProgress = progress;
            if (mOnCircularSeekBarChangeListener != null) {
                mOnCircularSeekBarChangeListener.onProgressChanged(this, progress, false);
            }

            recalculateAll();
            invalidate();
        }
    }


//    protected void onAttachedToWindow() {
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
//    }

    protected void setProgressBasedOnAngle(float angle) {
        mPointerPosition = angle;
        calculateProgressDegrees();
        mProgress = Math.round((float) mMax * mProgressDegrees / mTotalCircleDegrees);
    }

    protected void recalculateAll() {
        calculateTotalDegrees();
        calculatePointerAngle();
        calculateProgressDegrees();

        initRects();

        initPaths();

        calculatePointerXYPosition();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, resolveSize(size, heightMeasureSpec));

        // Set the circle width and height based on the view for the moment
        mCircleHeight = (float) height / 2f - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
        mCircleWidth = (float) width / 2f - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);

        // If it is not set to use custom
        if (mCustomRadii) {
            // Check to make sure the custom radii are not out of the view. If they are, just use the view values
            if ((mCircleYRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth) < mCircleHeight) {
                mCircleHeight = mCircleYRadius - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
            }

            if ((mCircleXRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth) < mCircleWidth) {
                mCircleWidth = mCircleXRadius - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
            }
        }

        if (mMaintainEqualCircle) { // Applies regardless of how the values were determined
            float min = Math.min(mCircleHeight, mCircleWidth);
            mCircleHeight = min;
            mCircleWidth = min;
        }
        mCircleArrowHeight = mCircleHeight - mCircleHeight / 8;
        mCircleArrowWidth = mCircleWidth - mCircleWidth / 8;
        recalculateAll();
    }

    /**
     * Get whether the pointer locks at zero and max.
     *
     * @return Boolean value of true if the pointer locks at zero and max, false if it does not.
     */
    public boolean isLockEnabled() {
        return lockEnabled;
    }

    /**
     * Set whether the pointer locks at zero and max or not.
     * <p>
     * //     * @param boolean value. True if the pointer should lock at zero and max, false if it should not.
     */
    public void setLockEnabled(boolean lockEnabled) {
        this.lockEnabled = lockEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchEnabled) {
            return false;
        }

        // Convert coordinates to our internal coordinate system
        float x = event.getX() - getWidth() / 2;
        float y = event.getY() - getHeight() / 2;

        // Get the distance from the center of the circle in terms of x and y
        float distanceX = mCircleRectF.centerX() - x;
        float distanceY = mCircleRectF.centerY() - y;

        // Get the distance from the center of the circle in terms of a radius
        float touchEventRadius = (float) Math.sqrt((Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));

        float minimumTouchTarget = MIN_TOUCH_TARGET_DP * DPTOPX_SCALE; // Convert minimum touch target into px
        float additionalRadius; // Either uses the minimumTouchTarget size or larger if the ring/pointer is larger

        if (mCircleStrokeWidth < minimumTouchTarget) { // If the width is less than the minimumTouchTarget, use the minimumTouchTarget
            additionalRadius = minimumTouchTarget / 2;
        } else {
            additionalRadius = mCircleStrokeWidth / 2; // Otherwise use the width
        }
        float outerRadius = Math.max(mCircleHeight, mCircleWidth) + additionalRadius; // Max outer radius of the circle, including the minimumTouchTarget or wheel width
        float innerRadius = Math.min(mCircleHeight, mCircleWidth) - additionalRadius; // Min inner radius of the circle, including the minimumTouchTarget or wheel width

        if (mPointerRadius < (minimumTouchTarget / 2)) { // If the pointer radius is less than the minimumTouchTarget, use the minimumTouchTarget
            additionalRadius = minimumTouchTarget / 2;
        } else {
            additionalRadius = mPointerRadius; // Otherwise use the radius
        }

        float touchAngle;
        touchAngle = (float) ((Math.atan2(y, x) / Math.PI * 180) % 360); // Verified
        touchAngle = (touchAngle < 0 ? 360 + touchAngle : touchAngle); // Verified

        cwDistanceFromStart = touchAngle - mStartAngle; // Verified
        cwDistanceFromStart = (cwDistanceFromStart < 0 ? 360f + cwDistanceFromStart : cwDistanceFromStart); // Verified
        ccwDistanceFromStart = 360f - cwDistanceFromStart; // Verified

        cwDistanceFromEnd = touchAngle - mEndAngle; // Verified
        cwDistanceFromEnd = (cwDistanceFromEnd < 0 ? 360f + cwDistanceFromEnd : cwDistanceFromEnd); // Verified
        ccwDistanceFromEnd = 360f - cwDistanceFromEnd; // Verified

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // These are only used for ACTION_DOWN for handling if the pointer was the part that was touched
                float pointerRadiusDegrees = (float) ((mPointerRadius * 180) / (Math.PI * Math.max(mCircleHeight, mCircleWidth)));
                cwDistanceFromPointer = touchAngle - mPointerPosition;
                cwDistanceFromPointer = (cwDistanceFromPointer < 0 ? 360f + cwDistanceFromPointer : cwDistanceFromPointer);
                ccwDistanceFromPointer = 360f - cwDistanceFromPointer;
                // This is for if the first touch is on the actual pointer.
                if (((touchEventRadius >= innerRadius) && (touchEventRadius <= outerRadius)) && ((cwDistanceFromPointer <= pointerRadiusDegrees) || (ccwDistanceFromPointer <= pointerRadiusDegrees))) {
                    setProgressBasedOnAngle(mPointerPosition);
                    lastCWDistanceFromStart = cwDistanceFromStart;
                    mIsMovingCW = true;
                    mPointerHaloPaint.setAlpha(mPointerAlphaOnTouch);
                    mPointerHaloPaint.setColor(mPointerHaloColorOnTouch);
                    recalculateAll();
                    invalidate();
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
                    }
                    mUserIsMovingPointer = true;
                    lockAtEnd = false;
                    lockAtStart = false;
                } else if (cwDistanceFromStart > mTotalCircleDegrees) { // If the user is touching outside of the start AND end
                    mUserIsMovingPointer = false;
                    return false;
                } else if ((touchEventRadius >= innerRadius) && (touchEventRadius <= outerRadius)) { // If the user is touching near the circle
                    setProgressBasedOnAngle(touchAngle);
                    lastCWDistanceFromStart = cwDistanceFromStart;
                    mIsMovingCW = true;
                    mPointerHaloPaint.setAlpha(mPointerAlphaOnTouch);
                    mPointerHaloPaint.setColor(mPointerHaloColorOnTouch);
                    recalculateAll();
                    invalidate();
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener.onStartTrackingTouch(this);
                        mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
                    }
                    mUserIsMovingPointer = true;
                    lockAtEnd = false;
                    lockAtStart = false;
                } else { // If the user is not touching near the circle
                    mUserIsMovingPointer = false;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mUserIsMovingPointer) {
                    if (lastCWDistanceFromStart < cwDistanceFromStart) {
                        if ((cwDistanceFromStart - lastCWDistanceFromStart) > 180f && !mIsMovingCW) {
                            lockAtStart = true;
                            lockAtEnd = false;
                        } else {
                            mIsMovingCW = true;
                        }
                    } else {
                        if ((lastCWDistanceFromStart - cwDistanceFromStart) > 180f && mIsMovingCW) {
                            lockAtEnd = true;
                            lockAtStart = false;
                        } else {
                            mIsMovingCW = false;
                        }
                    }

                    if (lockAtStart && mIsMovingCW) {
                        lockAtStart = false;
                    }
                    if (lockAtEnd && !mIsMovingCW) {
                        lockAtEnd = false;
                    }
                    if (lockAtStart && !mIsMovingCW && (ccwDistanceFromStart > 90)) {
                        lockAtStart = false;
                    }
                    if (lockAtEnd && mIsMovingCW && (cwDistanceFromEnd > 90)) {
                        lockAtEnd = false;
                    }
                    // Fix for passing the end of a semi-circle quickly
                    if (!lockAtEnd && cwDistanceFromStart > mTotalCircleDegrees && mIsMovingCW && lastCWDistanceFromStart < mTotalCircleDegrees) {
                        lockAtEnd = true;
                    }

                    if (lockAtStart && lockEnabled) {
                        mProgress = 0;
                        recalculateAll();
                        invalidate();
                        if (mOnCircularSeekBarChangeListener != null) {
                            mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
                        }

                    } else if (lockAtEnd && lockEnabled) {
                        mProgress = mMax;
                        recalculateAll();
                        invalidate();
                        if (mOnCircularSeekBarChangeListener != null) {
                            mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
                        }
                    } else if ((mMoveOutsideCircle) || (touchEventRadius <= outerRadius)) {
                        if (!(cwDistanceFromStart > mTotalCircleDegrees)) {
                            setProgressBasedOnAngle(touchAngle);
                        }
                        recalculateAll();
                        invalidate();
                        if (mOnCircularSeekBarChangeListener != null) {
                            mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
                        }
                    } else {
                        break;
                    }

                    lastCWDistanceFromStart = cwDistanceFromStart;
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                mPointerHaloPaint.setAlpha(mPointerAlpha);
                mPointerHaloPaint.setColor(mPointerHaloColor);
                if (mUserIsMovingPointer) {
                    mUserIsMovingPointer = false;
                    invalidate();
                    if (mOnCircularSeekBarChangeListener != null) {
                        mOnCircularSeekBarChangeListener.onStopTrackingTouch(this);
                    }
                } else {
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL: // Used when the parent view intercepts touches for things like scrolling
                mPointerHaloPaint.setAlpha(mPointerAlpha);
                mPointerHaloPaint.setColor(mPointerHaloColor);
                mUserIsMovingPointer = false;
                invalidate();
                break;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return true;
    }

    protected void init(AttributeSet attrs, int defStyle) {
        final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularSeekBar, defStyle, 0);
        initAttributes(attrArray);
        attrArray.recycle();

//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.circular1);
        initPaints();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle state = new Bundle();
        state.putParcelable("PARENT", superState);
        state.putInt("MAX", mMax);
        state.putInt("PROGRESS", mProgress);
        state.putInt("mCircleColor", mCircleColor);
        state.putInt("mCircleProgressColor", mCircleProgressColor);
        state.putInt("mPointerColor", mPointerColor);
        state.putInt("mPointerHaloColor", mPointerHaloColor);
        state.putInt("mPointerHaloColorOnTouch", mPointerHaloColorOnTouch);
        state.putInt("mPointerAlpha", mPointerAlpha);
        state.putInt("mPointerAlphaOnTouch", mPointerAlphaOnTouch);
        state.putBoolean("lockEnabled", lockEnabled);
        state.putBoolean("isTouchEnabled", isTouchEnabled);

        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;

        Parcelable superState = savedState.getParcelable("PARENT");
        super.onRestoreInstanceState(superState);

        mMax = savedState.getInt("MAX");
        mProgress = savedState.getInt("PROGRESS");
        mCircleColor = savedState.getInt("mCircleColor");
        mCircleProgressColor = savedState.getInt("mCircleProgressColor");
        mPointerColor = savedState.getInt("mPointerColor");
        mPointerHaloColor = savedState.getInt("mPointerHaloColor");
        mPointerHaloColorOnTouch = savedState.getInt("mPointerHaloColorOnTouch");
        mPointerAlpha = savedState.getInt("mPointerAlpha");
        mPointerAlphaOnTouch = savedState.getInt("mPointerAlphaOnTouch");
        lockEnabled = savedState.getBoolean("lockEnabled");
        isTouchEnabled = savedState.getBoolean("isTouchEnabled");

        initPaints();

        recalculateAll();
    }

    public void setOnSeekBarChangeListener(OnCircularSeekBarChangeListener l) {
        mOnCircularSeekBarChangeListener = l;
    }

    /**
     * Gets the circle color.
     *
     * @return An integer color value for the circle
     */
    public int getCircleColor() {
        return mCircleColor;
    }

    /**
     * Sets the circle color.
     *
     * @param color the color of the circle
     */
    public void setCircleColor(int color) {
        mCircleColor = color;
        mCirclePaint.setColor(mCircleColor);
        invalidate();
    }

    /**
     * Gets the circle progress color.
     *
     * @return An integer color value for the circle progress
     */
    public int getCircleProgressColor() {
        return mCircleProgressColor;
    }

    /**
     * Sets the circle progress color.
     *
     * @param color the color of the circle progress
     */
    public void setCircleProgressColor(int color) {
        mCircleProgressColor = color;
        mCircleProgressPaint.setColor(mCircleProgressColor);
        invalidate();
    }

    /**
     * Gets the pointer color.
     *
     * @return An integer color value for the pointer
     */
    public int getPointerColor() {
        return mPointerColor;
    }

    /**
     * Sets the pointer color.
     *
     * @param color the color of the pointer
     */
    public void setPointerColor(int color) {
        mPointerColor = color;
        mPointerPaint.setColor(mPointerColor);
        invalidate();
    }

    /**
     * Gets the pointer halo color.
     *
     * @return An integer color value for the pointer halo
     */
    public int getPointerHaloColor() {
        return mPointerHaloColor;
    }

    /**
     * Sets the pointer halo color.
     *
     * @param color the color of the pointer halo
     */
    public void setPointerHaloColor(int color) {
        mPointerHaloColor = color;
        mPointerHaloPaint.setColor(mPointerHaloColor);
        invalidate();
    }

    /**
     * Gets the pointer alpha value.
     *
     * @return An integer alpha value for the pointer (0..255)
     */
    public int getPointerAlpha() {
        return mPointerAlpha;
    }

    /**
     * Sets the pointer alpha.
     *
     * @param alpha the alpha of the pointer
     */
    public void setPointerAlpha(int alpha) {
        if (alpha >= 0 && alpha <= 255) {
            mPointerAlpha = alpha;
            mPointerHaloPaint.setAlpha(mPointerAlpha);
            invalidate();
        }
    }

    /**
     * Gets the pointer alpha value when touched.
     *
     * @return An integer alpha value for the pointer (0..255) when touched
     */
    public int getPointerAlphaOnTouch() {
        return mPointerAlphaOnTouch;
    }

    /**
     * Sets the pointer alpha when touched.
     *
     * @param alpha the alpha of the pointer (0..255) when touched
     */
    public void setPointerAlphaOnTouch(int alpha) {
        if (alpha >= 0 && alpha <= 255) {
            mPointerAlphaOnTouch = alpha;
        }
    }

    /**
     * Gets the circle fill color.
     *
     * @return An integer color value for the circle fill
     */
    public int getCircleFillColor() {
        return mCircleFillColor;
    }

    /**
     * Sets the circle fill color.
     *
     * @param color the color of the circle fill
     */
    public void setCircleFillColor(int color) {
        mCircleFillColor = color;
        mCircleFillPaint.setColor(mCircleFillColor);
        invalidate();
    }

    /**
     * Get the current max of the CircularSeekBar.
     *
     * @return Synchronized integer value of the max.
     */
    public synchronized int getMax() {
        return mMax;
    }

    /**
     * Set the max of the CircularSeekBar.
     * If the new max is less than the current progress, then the progress will be set to zero.
     * If the progress is changed as a result, then any listener will receive a onProgressChanged event.
     *
     * @param max The new max for the CircularSeekBar.
     */
    public void setMax(int max) {
        if (!(max <= 0)) { // Check to make sure it's greater than zero
            if (max <= mProgress) {
                mProgress = 0; // If the new max is less than current progress, set progress to zero
                if (mOnCircularSeekBarChangeListener != null) {
                    mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, false);
                }
            }
            mMax = max;

            recalculateAll();
            invalidate();
        }
    }

    public String getmTextCenter() {
        return mTextCenter;
    }

    public void setmTextCenter(String mTextCenter) {
        this.mTextCenter = mTextCenter;
    }

    /**
     * Get whether user touch input is accepted.
     *
     * @return Boolean value of true if user touch input is accepted, false if user touch input is ignored.
     */
    public boolean getIsTouchEnabled() {
        return isTouchEnabled;
    }

    /**
     * Set whether user touch input is accepted or ignored.
     * <p>
     * //     * @param boolean value. True if user touch input is to be accepted, false if user touch input is to be ignored.
     */
    public void setIsTouchEnabled(boolean isTouchEnabled) {
        this.isTouchEnabled = isTouchEnabled;
    }

    /**
     * Listener for the CircularSeekBar. Implements the same methods as the normal OnSeekBarChangeListener.
     */
    public interface OnCircularSeekBarChangeListener {

        public abstract void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser);

        public abstract void onStopTrackingTouch(CircularSeekBar seekBar);

        public abstract void onStartTrackingTouch(CircularSeekBar seekBar);
    }

}