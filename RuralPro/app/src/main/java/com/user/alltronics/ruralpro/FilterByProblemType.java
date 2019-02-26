package com.user.alltronics.ruralpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterByProblemType extends AppCompatActivity implements ListView.OnItemClickListener,View.OnClickListener {

    private ListView listView;


    TextView TvSubject;
    String TvSubjectT;


    private String JSON_STRING;

    //Declaring an Spinner
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner4;

    //An ArrayList for Spinner Items
    private ArrayList<String> districts;

    private ArrayList<String> mandals;
    private ArrayList<String> problems;

    //JSON Array
    private JSONArray result;
    private JSONArray mandal;
    private JSONArray problem;

    private Spinner language;

    String problem_id;
    String problem_subject;
    String problem_name;



    private Button buttonFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by_problem_type);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        TvSubject = (TextView) findViewById (R.id.TvSubject);

        TvSubjectT = TvSubject.getText().toString();


        //Initializing the ArrayList
        districts = new ArrayList<String>();

        mandals = new ArrayList<String>();
        problems = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner4 = (Spinner) findViewById(R.id.spinner4);

        language = (Spinner) findViewById (R.id.language);

        buttonFind = (Button) findViewById(R.id.buttonFind);

        buttonFind.setOnClickListener(this);

        final String buttonFindT = buttonFind.getText().toString();

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

                    TvSubject.setText(TvSubjectT);

                    getProblemData();

                    buttonFind.setText(buttonFindT);


                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;
                    getProblemData();
                    TvSubject.setText("సమస్య రకం");


                    buttonFind.setText("సమస్యలు వెతకండి");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;
                    getProblemData();

                    TvSubject.setText("समस्या प्रकार");

                    buttonFind.setText("खोज");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getJSON();
    }



    private void getProblemData(){
        //Creating a string request
        problems.clear();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL_PROBLEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            problem = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getdistricts to get the districts from the JSON Array
                            getProblems(problem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getProblems(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list

                if (LanguageId.language_id == 0)
                    problems.add(json.getString(Config.KEY_EMP_PROB_SUBJECT));

                else if (LanguageId.language_id == 1)
                    problems.add(json.getString("problem_subject_telugu"));

                else if (LanguageId.language_id == 2)
                    problems.add(json.getString("problem_subject_hindi"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner4.setAdapter(new ArrayAdapter<String>(FilterByProblemType.this, android.R.layout.simple_spinner_dropdown_item, problems));

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                problem_id = getProblemType(position);
                problem_subject = getProblemSubject(position);
                Log.e("Problem Type", problem_id);
                Log.e("Problem Subject", problem_subject);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private String getProblemType(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = problem.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.KEY_EMP_TYPE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private String getProblemSubject(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = problem.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.KEY_EMP_PROB_SUBJECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }




    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String problem_subject = jo.getString(Config.KEY_EMP_PROB_SUBJECT);
                String extra_subject = jo.getString(Config.KEY_EMP_SUBJECT);
                String subject = problem_subject + " - " + extra_subject;

                String description = jo.getString(Config.KEY_EMP_DESCRIPTION);
                String district = jo.getString(Config.TAG_DISTRICT_NAME);
                String initiative_status = jo.getString(Config.TAG_INITIATIVE_STATUS);

                String n = "\nSubject: "+ subject + " \n\nInitiative Status: " + initiative_status+"\n";


                HashMap<String,String> problems = new HashMap<>();
                problems.put(Config.TAG_ID,id);
                problems.put(Config.TAG_INITIATIVE_STATUS,n);
                list.add(problems);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                FilterByProblemType.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_INITIATIVE_STATUS},
                new int[]{R.id.problem, R.id.initiative_status});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FilterByProblemType.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getJSONFilter(){
        listView.setAdapter(null);
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FilterByProblemType.this,"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            String district_name, mandal_name;

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("https://pro10997.000webhostapp.com/RuralPro/rural/Filter/getAllProbFiltProTyp.php?t="+problem_id);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewProblem.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Config.TAG_ID).toString();
        intent.putExtra(Config.TAG_ID,empId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonFind) {
            getJSONFilter();
        }
    }
}
