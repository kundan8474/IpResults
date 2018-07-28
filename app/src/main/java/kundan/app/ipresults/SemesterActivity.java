package kundan.app.ipresults;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kundan.app.ipresults.Adapters.HomeAdapter;
import kundan.app.ipresults.Models.Semester;
import kundan.app.ipresults.Models.Subject;

public class SemesterActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private ArrayList<Semester> datalist = null;
    private HomeAdapter homeAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isPreviousDataAvailable =null;
    private boolean isConnected = false;
    private boolean isRollAvailable = false;
    String Roll=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);

        FloatingActionButton floatingActionButton= (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemesterActivity.this,FabWeb.class);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SemesterActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        progressDialog = new ProgressDialog(SemesterActivity.this);
        progressDialog.setMessage("please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SemesterActivity.this);
        isPreviousDataAvailable = sharedPreferences.getString("result_array",null);
        isRollAvailable = sharedPreferences.getBoolean("roll_status",false);
        Roll = sharedPreferences.getString("roll_number",null);
        String s = sharedPreferences.getString("resultString",null);

        if(s !=null)
        {
            Log.v("s : ",s);
            parseResult(s);
        }
        else if(isPreviousDataAvailable !=null){Log.v("s : ",isPreviousDataAvailable);
            parseResult(isPreviousDataAvailable);}
        else
            {
                try{getResult(Roll);}catch (Exception e) {Log.v("getResult function"," SemsesterAct");e.printStackTrace();}
            }


    }

    private void getResult(String rollNumber) {

        Log.v("roll in function",rollNumber);
        JsonArrayRequest result = new JsonArrayRequest(Request.Method.GET,"http://outcome-ipu.herokuapp.com/"+rollNumber,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v("response ",response.toString());

                        editor = sharedPreferences.edit();
                        editor.putString("result_array",response.toString());
                        isPreviousDataAvailable=response.toString();
                        parseResult(isPreviousDataAvailable);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SemesterActivity.this,"Unable to connect to server",Toast.LENGTH_SHORT).show();
                if(progressDialog !=null && progressDialog.isShowing()){progressDialog.cancel();}
            }
        })
        {
            @Override
            public HashMap<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(result);
    }

    private void parseResult(String s1) {
        JSONArray response = null;
        progressDialog.setTitle("Fetching result");
        progressDialog.setMessage("this may take up to 2 minutes");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        try {response = new JSONArray(s1);} catch (JSONException e) {e.printStackTrace();}
        if (response !=null)
        {
            ArrayList < Semester > semList = new ArrayList<>(response.length());
            for(int l=0;l<response.length();l++){semList.add(null);}
            try
            {

                Semester semester =null;
                for(int j = 0; j<response.length(); j++){
                    ArrayList <Subject> marksList =new ArrayList<>();
                    JSONObject subObject = response.getJSONObject(j);
                    JSONArray marks = subObject.getJSONArray("Marks");
                    if(marks !=null){
                        for(int k =0; k<marks.length(); k++) {
                            JSONObject marksNo = marks.getJSONObject(k);
                            Subject s= new Subject(marksNo.getString("Id"),marksNo.getString("Credits"),marksNo.getString("Internal"),marksNo.getString("External"),marksNo.getInt("Total"),marksNo.getString("Name"));
                            try{
                                    marksList.add(s);
                            }catch (NullPointerException e){e.printStackTrace();}
                        }
                    }

                    semester = new Semester(marksList,subObject.getString("Semester"),subObject.getString("Programme"),subObject.getString("Batch"),
                            subObject.getString("Examination"),subObject.getString("EnrollmentNumber"),subObject.getString("Name"),subObject.getString("CreditsSecured"),
                            subObject.getString("CollegeCode"),subObject.getString("Score"));
                    semList.set((Integer.parseInt(subObject.getString("Semester")))-1,semester);
                }
                homeAdapter = new HomeAdapter(SemesterActivity.this, semList, response.length());
                recyclerView.setAdapter(homeAdapter);
                if(progressDialog !=null && progressDialog.isShowing()){progressDialog.cancel();}
            }catch (JSONException e) {e.printStackTrace();if(progressDialog !=null && progressDialog.isShowing()){progressDialog.cancel();}}
        }
    }

}
