package kundan.app.ipresults.Adapters;

/**
 * Created by kkr on 20-07-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import kundan.app.ipresults.SemesterActivity;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Semester> semList;
    private  int responseLength=-1;

    public static ArrayList<Subject> getRes() {
        return res;
    }

    private  static  ArrayList <Subject>  res = new ArrayList<>();

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSem, textViewName, textViewEnroll, textViewExam, textViewBatch, score, credit;

        MyViewHolder(View view) {
            super(view);
            textViewSem = (TextView) view.findViewById(R.id.semNumber);
            textViewName = (TextView) view.findViewById(R.id.StudentName);
            textViewEnroll = (TextView) view.findViewById(R.id.enrollment);
            textViewExam = (TextView) view.findViewById(R.id.Examination);
            textViewBatch = (TextView) view.findViewById(R.id.batch);
            score = (TextView) view.findViewById(R.id.Score);
            credit = (TextView) view.findViewById(R.id.CreditScore);
        }
    }

    private void sendResponseResult(ArrayList<Subject> subjects) {
        res =  subjects;
    }

    public HomeAdapter(Context mContext, List<Semester> semList, int length) {
        this.mContext = mContext;
        this.semList = semList;
        responseLength = length;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_semester, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int Position) {
        final Semester semester = semList.get(responseLength - Position-1);
        if (semester !=null)
        {
            holder.textViewSem.setText(mContext.getResources().getText(R.string.sem) + " : " + semester.getSem());
            holder.textViewName.setText(semester.getStudentName());
            holder.textViewEnroll.setText(semester.getEnrollmentNumber());
            holder.textViewExam.setText(semester.getExamination());
            holder.textViewBatch.setText("Batch " + semester.getBatch());
            holder.credit.setText(semester.getCreditsScored());
            holder.score.setText(String.valueOf(semester.getScore()));

            View view = holder.itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:: attach new activity to show sub wise result
                    sendResponseResult(semester.getSubjects());
                    JSONObject resultAttr = new JSONObject();
                    try
                    {
                        resultAttr.put("Examination",semester.getExamination());
                        resultAttr.put("Semester",semester.getSem());
                        resultAttr.put("Programme",semester.getProgramme());
                        resultAttr.put("Batch",semester.getBatch());
                        resultAttr.put("CollegeCode",semester.getCollegeCode());
                        Log.v("JsonBody",resultAttr.toString());
                    }catch (JSONException e){e.printStackTrace();}
                    Intent intent = new Intent( mContext.getApplicationContext(), DetailedResult.class);
                    intent.putExtra("semester_number",semester.getSem());
                    intent.putExtra("reqBody",resultAttr.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return semList.size();
    }

}