package kundan.app.ipresults;

/**
 * Created by kkr on 21-07-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kundan.app.ipresults.Adapters.RankAdapter;
import kundan.app.ipresults.Models.Rank;

import static android.view.Gravity.CENTER;


public class RankFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter rankAdapter;
    ArrayList<Rank> RankList = new ArrayList<>();
    JSONObject reqBody;
    TextView rankStudent, name, score;
    TextView button;
    CardView c1, c2;
    boolean isConnected = false;
    RequestQueue requestQueue;
    String Examination, Semester, Programme, Batch, CollegeCode;

    public RankFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        try {
            reqBody = new JSONObject(DetailedResult.getReq());
            Examination = reqBody.getString("Examination");
            Semester = reqBody.getString("Semester");
            Programme = reqBody.getString("Programme");
            Batch = reqBody.getString("Batch");
            CollegeCode = reqBody.getString("CollegeCode");

            Log.v("reqBody", reqBody.toString());
            Log.v("String params ", Examination + Semester + Programme + Batch + CollegeCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        isConnected = checkConnection();
        c1 = (CardView) view.findViewById(R.id.card1);
        c2 = (CardView) view.findViewById(R.id.card2);
        c1.setVisibility(View.INVISIBLE);
        c2.setVisibility(View.INVISIBLE);
        button = (TextView) view.findViewById(R.id.button);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewRank);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        rankStudent = (TextView) view.findViewById(R.id.RankStudent);
        name = (TextView) view.findViewById(R.id.RankName);
        score = (TextView) view.findViewById(R.id.scoreRank);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    getRank();
                    button.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }
    private void getRank() {
        Log.v("req body", reqBody.toString());
        JsonObjectRequest postRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, "http://outcome-ipu.herokuapp.com/rank", reqBody,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.v("post reqbody", reqBody.toString());
                        Log.v("Response length", String.valueOf(response.length()));

                        c2.setVisibility(View.VISIBLE);     c1.setVisibility(View.VISIBLE);
                        try {
                            RankList.clear();
                            JSONArray students = response.getJSONArray("Students");
                            for (int j = 0; j < students.length(); j++) {
                                JSONObject index1 = students.getJSONObject(j);
                                Rank rank = new Rank(index1.getString("Name"), index1.getString("EnrollmentNumber"), index1.getString("Scores"));
                                Log.v("Rank object ", "" + j + 1 + " " + rank.getName() + rank.getEnrollmentNumber() + rank.getScore());
                                RankList.add(rank);
                            }
                            rankAdapter = new RankAdapter(getActivity().getApplicationContext(), RankList);
                            recyclerView.setAdapter(rankAdapter);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                            String enroll = sharedPreferences.getString("roll_number", null);
                            if (enroll != null) {
                                for (int j = 0; j < RankList.size(); j++) {
                                    Rank rank = RankList.get(j);
                                    if (rank.getEnrollmentNumber().equals(enroll)) {
                                        rankStudent.setText(String.valueOf(j + 1));
                                        name.setText(rank.getName());
                                        score.setText(rank.getScore());
                                        break;
                                    }
                                }
                            }

                        } catch (Exception e) {
                            Log.v("getResult Volley", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    private boolean checkConnection() {
        boolean connected = ConnectivityReceiver.isConnected();
        showSnack(connected);
        Log.e("is network connected", String.valueOf(connected));
        return connected;
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "";
        } else {
            message = "Sorry! Not connected to internet";
            Toast snackbar = Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
            snackbar.setGravity(CENTER, 0, 0);
            snackbar.show();
        }
    }

}