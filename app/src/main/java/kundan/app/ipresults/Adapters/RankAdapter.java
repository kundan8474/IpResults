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
import kundan.app.ipresults.Models.Rank;
import kundan.app.ipresults.Models.Semester;
import kundan.app.ipresults.Models.Subject;
import kundan.app.ipresults.R;

/**
 * Created by kkr on 26-07-2017.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

    private  ArrayList <Rank> RankList = new ArrayList<>();
    private Context mContext;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRank, textViewName, score ;

        MyViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.RankName);
            textViewRank = (TextView) view.findViewById(R.id.RankStudent);
            score = (TextView) view.findViewById(R.id.RankScore);
        }
    }


    public RankAdapter(Context mContext, ArrayList<Rank> rankList ) {
        this.mContext = mContext;
        RankList = rankList;
    }

    @Override
    public RankAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_rank, parent, false);
        return new RankAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rank rank = RankList.get(position);

        holder.textViewRank.setText(String.valueOf(position+1));
        holder.textViewName.setText(rank.getName());
        holder.score.setText(rank.getScore());

    }


    @Override
    public int getItemCount() {
        return RankList.size();
    }

}
