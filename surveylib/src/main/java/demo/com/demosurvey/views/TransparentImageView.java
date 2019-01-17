package demo.com.demosurvey.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import demo.com.demosurvey.R;

/**
 * <p>
 * Custom view, which uses different transparent shapes to create a beautiful view. In this particular rectangles & circles are used.
 */
public class TransparentImageView extends View {

    private final static String TAG = "TransparentImageView";

    private static final int MIN_SIDES = 3;
    ProgressDialog progressDialog = new ProgressDialog(getContext());
    private int noOfSides = MIN_SIDES;
    private float radius, imgRotationAngle;
    private int color;
    private String label;
    private Drawable drawableImg;
    private Paint paint;
    private TextPaint textPaint;
    private Path path;
    private Target mTarget;
    private String imgUrl;
    //Flag for checking whether view is drawn or not.
    private boolean isDrawn = false;

    private OnLayoutListener layoutListener;

    public TransparentImageView(Context context) {
        super(context);
    }

    public TransparentImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TransparentImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TransparentImageView);
        noOfSides = a.getInt(R.styleable.TransparentImageView_noOfSides, MIN_SIDES);
        radius = a.getDimensionPixelSize(R.styleable.TransparentImageView_radius, 100);
        imgRotationAngle = a.getDimensionPixelSize(R.styleable.TransparentImageView_imgRotation, 0);
        color = a.getColor(R.styleable.TransparentImageView_color, getResources().getColor(R.color.colorPrimary));
        label = a.getString(R.styleable.TransparentImageView_label);
        imgUrl = a.getString(R.styleable.TransparentImageView_imgUrl);
        Log.d("DEBUG", "radius: " + radius);
        Log.i(TAG, "label: " + label);
        a.recycle();
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(android.R.color.white));
    }

    /**
     * Calculates required parameters for TransparentCardView creation
     */
    private void defaultAttributes() {
        radius = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isDrawn)
            defaultAttributes();
        isDrawn = true;
        Bitmap bitmap = bitmapDraw();
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        defaultAttributes();

        if (this.layoutListener != null && !isDrawn)
            this.layoutListener.onLayout();

        isDrawn = true;
    }

    /**
     * Creates a bitmap with transparent circle & a card with dynamic height.
     *
     * @return
     */

    private Bitmap bitmapDraw() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);

        final Canvas canvasBitmap = new Canvas(bitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        paint.setColor(color);

        Path path = createPath(noOfSides, radius, imgRotationAngle);
        canvasBitmap.clipPath(path);
        canvasBitmap.drawPath(path, paint);

        final Rect dstRect = new Rect();
        canvasBitmap.getClipBounds(dstRect);

        if (imgUrl != null) {
            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap originalImage, Picasso.LoadedFrom from) {
                    if (originalImage == null) {
//                        Log.e(TAG, "Null");
                    } else {
                        canvasBitmap.drawBitmap(originalImage, null, dstRect, null);
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    progressDialog.dismiss();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                }
            };

            // Mega high res out of memory image
            Picasso.with(getContext()).load(imgUrl).resize(getWidth(), getHeight()).into(mTarget);

//            Bitmap originalImage = BitmapFactory.decodeResource(getResources(), value.resourceId);
        }

        if (label != null)
            canvasBitmap.drawText(label, getWidth() / 2, getHeight() / 2, textPaint);

        return bitmap;
    }

    public Bitmap bitmapResizer(Bitmap bitmap, int newWidth, int newHeight) {

        /*float originalWidth = originalImage.getWidth();
                        float originalHeight = originalImage.getHeight();

                        float scale = getWidth() / originalWidth;

                        float xTranslation = 0.0f;
                        float yTranslation = (getHeight() - originalHeight * scale) / 2.0f;

                        Matrix transformation = new Matrix();
                        transformation.postTranslate(xTranslation, yTranslation);
                        transformation.preScale(scale, scale);

                        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
                        paint.setFilterBitmap(true);*/

        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    private Path createPath(int noOfSides, float radius, float rotate) {
        int cX = getWidth() / 2;
        int cY = getHeight() / 2;
        path.reset();

        double angle = 2.0 * Math.PI / noOfSides;
        double rotationAngle = (Math.PI * rotate) / 180;

        path.moveTo(
                cX + (float) (radius * Math.cos(0.0 + rotationAngle)),
                cY + (float) (radius * Math.sin(0.0 + rotationAngle)));

        for (int i = 0; i < noOfSides; i++) {
            path.lineTo(
                    cX + (float) (radius * Math.cos(angle * i + rotationAngle)),
                    cY + (float) (radius * Math.sin(angle * i + rotationAngle)));
        }
        path.close();
        return path;
    }

    public void setOnLayoutListener(OnLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
    }

    public void setNoOfSides(int noOfSides) {
        this.noOfSides = noOfSides;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setImgRotationAngle(float imgRotationAngle) {
        this.imgRotationAngle = imgRotationAngle;
        invalidate();
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * Listener for notifying view layout is done.
     */
    public interface OnLayoutListener {
        void onLayout();
    }
}
