package kundan.app.ipresults.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kundan.app.ipresults.DetailedResult;
import kundan.app.ipresults.Models.Semester;
import kundan.app.ipresults.Models.Subject;
import kundan.app.ipresults.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private Context mContext;
    private List<Subject> subList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView SubName, SubID, SubScore, SubExternal, SubInternal, SubCredit;

        MyViewHolder(View view) {
            super(view);
            SubName = (TextView) view.findViewById(R.id.SubjectName);
            SubID = (TextView) view.findViewById(R.id.SubjectID);
            SubScore = (TextView) view.findViewById(R.id.SubjectScore);
            SubExternal = (TextView) view.findViewById(R.id.SubjectExternal);
            SubInternal = (TextView) view.findViewById(R.id.SubjectInternal);
            SubCredit = (TextView) view.findViewById(R.id.SubjectCredit);
        }
    }

    public SubjectAdapter(Context mContext, List<Subject> subList) {
        this.mContext = mContext;
        this.subList = subList;
    }

    @Override
    public SubjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_marks, parent, false);

        return new SubjectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int Position) {

        Subject subject = subList.get(Position);

        holder.SubName.setText(subject.getSubjectName());
        holder.SubID.setText(subject.getSubjectId());
        holder.SubScore.setText(String.valueOf(subject.getTotal()));
        holder.SubExternal.setText(subject.getExternal());
        holder.SubInternal.setText(subject.getInternal());
        holder.SubCredit.setText(subject.getCredits());
    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

}