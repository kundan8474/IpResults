package kundan.app.ipresults;

/**
 * Created by kkr on 21-07-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;

import java.util.ArrayList;

import kundan.app.ipresults.Adapters.HomeAdapter;
import kundan.app.ipresults.Adapters.SubjectAdapter;
import kundan.app.ipresults.Models.Subject;


public class ResultFragment extends Fragment {

    public ResultFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_result,container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewMarks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList < Subject > res = HomeAdapter.getRes();
        for(int i =0 ; i < res.size() ; i++){
            Log.v("Result arraylist"," "+i+" "+ res.get(i).getSubjectName());
        }
        try{RecyclerView.Adapter subjectAdapterAdapter= new SubjectAdapter(getActivity().getApplicationContext(), res);recyclerView.setAdapter(subjectAdapterAdapter);}
        catch (Exception e){Log.v("ResultFrag",e.getMessage());e.printStackTrace();};

        return  view;
    }

}