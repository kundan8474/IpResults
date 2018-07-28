package kundan.app.ipresults;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import java.util.HashMap;

import kundan.app.ipresults.Models.Semester;

/**
 * Created by kkr on 27-07-2017.
 */

public class Splash extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{


    private SharedPreferences sharedPreferences;
    boolean isConnected = false;
    private ProgressDialog progressDialog;
    private String isPreviousDataAvailable =null;
    private boolean isRollAvailable =false;
    String Roll=null;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressDialog = new ProgressDialog(Splash.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        isPreviousDataAvailable = sharedPreferences.getString("result_array",null);
        isRollAvailable = sharedPreferences.getBoolean("roll_status",false);

        Log.v("isRollAvailable",String.valueOf(isRollAvailable));
        isConnected = checkConnection();
        if (isConnected)
        {

            if (!isRollAvailable){
                LayoutInflater li = LayoutInflater.from(Splash.this);
                View promptsView = li.inflate(R.layout.get_roll_number, null);
                final EditText roll = (EditText) promptsView.findViewById(R.id.rn);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Splash.this);
                alertDialogBuilder.setView(promptsView);

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String rollNumber =roll.getText().toString().trim();
                        Log.v("editText value",rollNumber);
                        Log.v("RollNumber", rollNumber);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("roll_status",true);
                        editor.putString("roll_number",rollNumber);
                        editor.commit();
                        getResult(rollNumber);
                        progressDialog.setTitle("Fetching result");
                        progressDialog.setMessage("this may take up to 2 minutes");
                        progressDialog.setCancelable(false);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                    }
                }).setCancelable(false);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }

            //else {Roll = sharedPreferences.getString("roll_number",null);Log.v("Roll",Roll);new ResultChecker().execute();}
        //}

        else {
                Roll = sharedPreferences.getString("roll_number",null);Log.v("Roll",Roll);new ResultChecker().execute();
            }
        }

        else {
            if (isPreviousDataAvailable != null) {
                Intent intent = new Intent(Splash.this, SemesterActivity.class);
                intent.putExtra("resultString", isPreviousDataAvailable);
                System.out.println(isPreviousDataAvailable);
                startActivity(intent);
                finish();
                //parseResult(isPreviousDataAvailable);
            } else {
                showSnack(isConnected);
            }
        }

    }

    private boolean checkConnection() {
        boolean connected = ConnectivityReceiver.isConnected();
        showSnack(connected);
        Log.e("is network connected",String.valueOf(connected));
        return connected;
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! nnected to Internet";
            color = Color.WHITE;
        } else {
            message = "Not connected to internet";
            color = Color.RED;
            Toast.makeText(Splash.this,message, Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(Splash.this, SemesterActivity.class);
                        intent.putExtra("resultString", response.toString());
                        startActivity(intent);
                        finish();
                        //parseResult(isPreviousDataAvailable);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Splash.this,"Unable to connect to server",Toast.LENGTH_SHORT).show();
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

    protected void onResume() {
        super.onResume();

        // register connection status listener
        if(progressDialog !=null && progressDialog.isShowing()){progressDialog.cancel();}
        MyApplication.getInstance().setConnectivityListener(Splash.this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private class ResultChecker extends AsyncTask<Object, Object, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            getResult(Roll);
            return null;
        }
    }
}
