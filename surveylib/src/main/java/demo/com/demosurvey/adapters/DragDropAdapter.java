package demo.com.demosurvey.adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.meg7.widget.CircleImageView;

import java.util.List;

import demo.com.demosurvey.R;
import demo.com.demosurvey.models.DragPojo;


public class DragDropAdapter extends RecyclerView.Adapter<DragDropAdapter.ViewHolder> {

    private final static String TAG = "DragDropAdapter";
    private List<DragPojo> listModels;
    private DragPojo currentModel;
    private Drawable enterShape, normalShape;
    private Context context;

    public DragDropAdapter(Context context, List<DragPojo> listModels) {
        this.listModels = listModels;
        this.context = context;
        enterShape = context.getResources().getDrawable(R.drawable.shape_circle_new);
        normalShape = context.getResources().getDrawable(R.drawable.shape_circle_white);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_drag_drop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final DragPojo model = listModels.get(position);
        holder.bind(model, position);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;

        ViewHolder(View v) {
            super(v);
            circleImageView = v.findViewById(R.id.item_drag_drop_img);
        }

        public void bind(final DragPojo model, final int position) {
            circleImageView.setImageResource(model.getImageId());
            circleImageView.setTag(TAG + "" + position);

            circleImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    Log.i(TAG, "DRAG CHECK onTouch: " + model.getPosition() + " " + view.getTag().toString());
                    return myTouch(model, view, position, motionEvent);
                }
            });

            circleImageView.setOnDragListener(null);
            if (model.isTarget()) {
//                Log.i(TAG, "DRAG CHECK Setting DragListener: " + model.getPosition());
                circleImageView.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
//                        Log.i(TAG, "DRAG CHECK onDrag: " + model.getPosition() + " " + view.getTag().toString());
                        return myDrag(model, view, position, dragEvent);
                    }
                });
            } else circleImageView.setVisibility(model.isSet() ? View.INVISIBLE : View.VISIBLE);
        }

        public boolean myTouch(DragPojo model, View view, int position, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                currentModel = model;
                return true;
            }
            return false;

        }

        public boolean myDrag(DragPojo model, View view, int position, DragEvent event) {
            Log.e(TAG, "onDrag method call: " + position + " " + view.getTag().toString());
            int action = event.getAction();
            if (!model.isTarget()) return false;
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.e(TAG, "DRAG CHECK onDrag ENTERED: " + position);
//                    view.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.e(TAG, "DRAG CHECK onDrag EXITED: " + position);
//                    view.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    Log.e(TAG, "onDrag::::Drag DROP " + model.isDropped());
                    int oldModelImageId = model.getImageId();
                    int oldCurrentModelImageId = currentModel.getImageId();

                    if (model.isDropped()) {
                        if (model.isTarget() && currentModel.isTarget()) {
                            int currentImageId = currentModel.getImageId();
                            currentModel.setImageId(model.getImageId());
                            model.setImageId(currentImageId);
                        }
                        else{
                            return true;
                        }
//                        return true;
                    } else {
                        currentModel.setSet(true);
//                Log.e(TAG, "onDrag::: Position " + position);
                        model.setImageId(currentModel.getImageId());
                        model.setDropped(true);

                        if (currentModel.isTarget()) {
                            currentModel.setImageId(R.drawable.shape_circle_white);
                            currentModel.setDropped(false);
                        }
                    }
                    notifyDataSetChanged();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Dropped, reassign View to ViewGroup
//                    view.setBackgroundDrawable(normalShape);
//                    isDragged = true;
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}