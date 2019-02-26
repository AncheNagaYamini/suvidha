package com.user.alltronics.ruralpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewUserSolutions extends AppCompatActivity implements View.OnClickListener,ListView.OnItemClickListener {

    private Button writeSolution;

    private String id;

    private ListView listView;

    private String JSON_STRING;
    private Spinner language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_solutions);

        Intent intent = getIntent();

        id = intent.getStringExtra("problem_id");

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        language = (Spinner) findViewById(R.id.language);

        writeSolution = (Button) findViewById (R.id.buttonWriteASolution);
        writeSolution.setOnClickListener(this);

        final String writeSolutionT = writeSolution.getText().toString();

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("English");
        spinnerArray.add("Telugu");
        spinnerArray.add("Hindi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);

        language.setSelection(LanguageId.language_id);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", Integer.toString(position));


                if ( position == 0 ) {

                    LanguageId.language_id = 0;

                    writeSolution.setText(writeSolutionT);

                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;

                    writeSolution.setText("మీ పరిష్కారము తెలపండి");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;

                    writeSolution.setText("अपना उपाय लिखें");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        getJSON();
    }


    private void showUserSolution(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = "\nSolution id: "+ jo.getString(Config.KEY_EMP_SOL_ID)+"\n";
                String name = "\n" +jo.getString(Config.KEY_EMP_SOLUTION)+"\n";

                HashMap<String,String> solutions = new HashMap<>();
                solutions.put(Config.KEY_EMP_SOL_ID,id);
                solutions.put(Config.KEY_EMP_SOLUTION,name);
                list.add(solutions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewUserSolutions.this, list, R.layout.list_item_sol,
                new String[]{Config.KEY_EMP_SOL_ID,Config.KEY_EMP_SOLUTION},
                new int[]{R.id.problem, R.id.solution});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewUserSolutions.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showUserSolution();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_USER+id);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == writeSolution){

            Intent intent = new Intent(this, WriteASolution.class);
            intent.putExtra("problem_id",id);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
