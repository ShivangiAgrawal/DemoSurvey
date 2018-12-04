package demo.com.demosurvey.adapters;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.meg7.widget.CircleImageView;
import com.meg7.widget.RectangleImageView;

import java.util.List;

import demo.com.demosurvey.R;
import demo.com.demosurvey.models.MultiSelectionPojo;
import demo.com.demosurvey.utils.PopUtils;

public class MultiSelectionAdapter extends RecyclerView.Adapter<MultiSelectionAdapter.ViewHolder> {

    private final static String TAG = "MultiSelectionAdapter";
    private final int doubleClickTimeout;
    private List<MultiSelectionPojo> listModels;
    private Context context;
    private String type;
    private Handler handler;
    private long firstClickTime;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;

    public MultiSelectionAdapter(Context context, List<MultiSelectionPojo> listModels, String type) {
        this.listModels = listModels;
        this.context = context;
        this.type = type;
        doubleClickTimeout = ViewConfiguration.getDoubleTapTimeout();
        firstClickTime = 0L;
        handler = new Handler(Looper.getMainLooper());
        mShortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (type.equalsIgnoreCase("CIRCULAR_MULTIPLE_SELECTION")) {
            view = inflater.inflate(R.layout.item_circle_view, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_rectangle_view, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MultiSelectionPojo model = listModels.get(position);
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgSelection/*, imgFullScreen*/;
        public View view;
//        public FrameLayout container;

        ViewHolder(View v) {
            super(v);
            if (type.equalsIgnoreCase("CIRCULAR_MULTIPLE_SELECTION")) {
                view = v.findViewById(R.id.item_circle_view_img);
            } else {
                view = v.findViewById(R.id.item_rectangle_view_img);
            }
            imgSelection = v.findViewById(R.id.item_img_selection);
//            imgFullScreen = v.findViewById(R.id.item_expanded_image);
//            container = v.findViewById(R.id.container);
        }

        public void bind(final MultiSelectionPojo model, final int position) {
            model.setSelected(false);
            if (type.equalsIgnoreCase("CIRCULAR_MULTIPLE_SELECTION")) {
                ((CircleImageView) view).setImageResource(model.getImageId());
            } else {
                ((RectangleImageView) view).setImageResource(model.getImageId());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    long now = System.currentTimeMillis();

                    if (now - firstClickTime < doubleClickTimeout) {
                        handler.removeCallbacksAndMessages(null);
                        firstClickTime = 0L;
//                        onDoubleClick;
                        onDoubleClick(view, model, position);
                    } else {
                        firstClickTime = now;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                onSingleClick;
                                onSingleClick(view, model, position);
                                firstClickTime = 0L;
                            }
                        }, doubleClickTimeout);
                    }
                }
            });
        }

        private void onSingleClick(View view, MultiSelectionPojo model, int position) {
            if (model.isSelected()) {
                model.setSelected(false);
                imgSelection.setVisibility(View.GONE);
            } else {
                PopUtils.alertDialog(context, model.getImageId());
            }
        }

        private void onDoubleClick(View view, MultiSelectionPojo model, int position) {
            if (model.isSelected()) {
                model.setSelected(false);
                imgSelection.setVisibility(View.GONE);
            } else {
                model.setSelected(true);
                imgSelection.setVisibility(View.VISIBLE);
            }
        }
    }
}
