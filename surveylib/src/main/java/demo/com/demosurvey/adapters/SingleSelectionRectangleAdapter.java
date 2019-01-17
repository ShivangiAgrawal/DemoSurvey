package demo.com.demosurvey.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import demo.com.demosurvey.R;
import demo.com.demosurvey.models.QuestionPojo;
import demo.com.demosurvey.models.SelectionStatePojo;
import demo.com.demosurvey.utils.Constants;

public class SingleSelectionRectangleAdapter extends RecyclerView.Adapter<SingleSelectionRectangleAdapter.ViewHolder> {

    private final static String TAG = "SingleSelectionAdapter";
    private List<SelectionStatePojo> listSingleSelection;
    private Context context;
    private QuestionPojo questionPojo;

    public SingleSelectionRectangleAdapter(Context context, List<SelectionStatePojo> listSingleSelection,
                                           QuestionPojo question) {
        this.listSingleSelection = listSingleSelection;
        this.context = context;
        this.questionPojo = question;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_least_most_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SelectionStatePojo selectionStatePojo = listSingleSelection.get(position);
        holder.bind(selectionStatePojo, position);

        if (selectionStatePojo.getType().equals("0")) {
            holder.txtLeast.setText(Constants.LEAST_APPEALING);
            holder.txtMost.setText(Constants.MOST_APPEALING);
            holder.txtLeast.setBackgroundResource(0);
            holder.txtMost.setBackgroundResource(0);
            holder.txtOption.setText("");
            holder.imgLeast.setVisibility(View.GONE);
            holder.imgMost.setVisibility(View.GONE);
        } else {
            holder.txtLeast.setBackgroundResource(R.color.colorLight);
            holder.txtMost.setBackgroundResource(R.color.colorLight);
            holder.txtLeast.setText("");
            holder.txtMost.setText("");
            holder.txtOption.setText(selectionStatePojo.getTextOption());
            holder.imgLeast.setVisibility(selectionStatePojo.isLeastSelected() ? View.VISIBLE : View.GONE);
            holder.imgMost.setVisibility(selectionStatePojo.isMostSelected() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listSingleSelection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtLeast;
        private TextView txtMost;
        private TextView txtOption;
        private ImageView imgLeast;
        private ImageView imgMost;

        ViewHolder(View v) {
            super(v);

            txtLeast = v.findViewById(R.id.layout_least_most_selection_txt_view_least);
            txtMost = v.findViewById(R.id.layout_least_most_selection_txt_view_most);
            txtOption = v.findViewById(R.id.layout_least_most_selection_txt_option);
            imgLeast = v.findViewById(R.id.layout_least_most_selection_img_least);
            imgMost = v.findViewById(R.id.layout_least_most_selection_img_most);
        }

        public void bind(final SelectionStatePojo selectionStatePojo, final int position) {
            final StringBuilder stringBuilder = new StringBuilder();
            txtLeast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectionStatePojo.getType().equals("0"))
                        return;

                    for (SelectionStatePojo state : listSingleSelection) {
                        state.setSelected(false);
                        state.setLeastSelected(false);
                        if (listSingleSelection.get(position).isMostSelected()) {
                            state.setMostSelected(false);
                        }
                    }
                    selectionStatePojo.setLeastSelected(true);
                    selectionStatePojo.setSelected(true);
                    notifyDataSetChanged();
                }
            });

            txtMost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectionStatePojo.getType().equals("0"))
                        return;

                    for (SelectionStatePojo statePojo : listSingleSelection) {
                        statePojo.setSelected(false);
                        statePojo.setMostSelected(false);
                        if (listSingleSelection.get(position).isLeastSelected()) {
                            statePojo.setLeastSelected(false);
                        }
                    }
                    selectionStatePojo.setMostSelected(true);
                    selectionStatePojo.setSelected(true);
                    notifyDataSetChanged();
                    String mostSelected = null;
                    for (int i=0; i<listSingleSelection.size(); i++){
                        if (listSingleSelection.get(i).isMostSelected()){
                            mostSelected = "Most: "+i;
                        }
                    }
                }
            });

            for (int i=0; i<listSingleSelection.size(); i++){
                if (listSingleSelection.get(i).isLeastSelected()) {
                    stringBuilder.append(" Least: " + i);
                }

                if (listSingleSelection.get(i).isMostSelected()) {
                    stringBuilder.append(" Most: " + i);
                }
            }

            Log.e(TAG, "bind:: "+ stringBuilder);
            questionPojo.setAnswer(stringBuilder.toString());
        }
    }
}
