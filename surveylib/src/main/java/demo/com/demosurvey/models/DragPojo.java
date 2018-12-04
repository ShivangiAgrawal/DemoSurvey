package demo.com.demosurvey.models;

import android.view.View;

public class DragPojo {

    private int imageId;
    private boolean isDropped;
    private boolean isTarget;
    private View view;
    private boolean isSet;

    public DragPojo(int imageId, boolean isDropped, boolean isTarget, View view) {
        this.imageId = imageId;
        this.isDropped = isDropped;
        this.isTarget = isTarget;
        this.view = view;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isDropped() {
        return isDropped;
    }

    public void setDropped(boolean dropped) {
        isDropped = dropped;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }
}
