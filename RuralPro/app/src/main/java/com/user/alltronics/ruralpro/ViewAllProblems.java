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

public class ViewAllProblems extends AppCompatActivity implements ListView.OnItemClickListener,View.OnClickListener {

    TextView TvDistrict;
    TextView TvMandal;
    TextView TvVillage;


    String TvDistrictT;
    String TvMandalT;
    String TvVillageT;

    private ListView listView;

    private String JSON_STRING;

    //Declaring an Spinner
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;

    //An ArrayList for Spinner Items
    private ArrayList<String> districts;

    private ArrayList<String> mandals;
    private ArrayList<String> villages;

    //JSON Array
    private JSONArray result;
    private JSONArray mandal;
    private JSONArray village;

    private Spinner language;

    String district_name;
    String mandal_name;
    String village_name;



    private Button buttonFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_problems);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        TvDistrict = (TextView) findViewById(R.id.TvDistrict);
        TvMandal = (TextView) findViewById(R.id.TvMandal);
        TvVillage = (TextView) findViewById(R.id.TvVillage);


        TvDistrictT = TvDistrict.getText().toString();
        TvMandalT = TvMandal.getText().toString();
        TvVillageT = TvVillage.getText().toString();


        //Initializing the ArrayList
        districts = new ArrayList<String>();

        mandals = new ArrayList<String>();
        villages = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);

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
                    TvDistrict.setText(TvDistrictT);
                    TvMandal.setText(TvMandalT);
                    TvVillage.setText(TvVillageT);


                    getData();

                    buttonFind.setText(buttonFindT);


                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;

                    TvDistrict.setText("జిల్లా");
                    TvMandal.setText("మండలం");
                    TvVillage.setText("గ్రామం");


                    getData();

                    buttonFind.setText("సమస్యలు వెతకండి");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;

                    TvDistrict.setText("जिला");
                    TvMandal.setText("क्षेत्र");
                    TvVillage.setText("गाँव");


                    getData();

                    buttonFind.setText("खोज");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getJSON();
    }


    private void getData(){
        //Creating a string request
        districts.clear();
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getdistricts to get the districts from the JSON Array
                            getDistricts(result);
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

    private void getDistricts(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                if( LanguageId.language_id == 0 )
                districts.add(json.getString(Config.TAG_DISTRICT_NAME));

                else if ( LanguageId.language_id == 1 )
                    districts.add(json.getString("district_name_telugu"));

                else if ( LanguageId.language_id == 2 )
                    districts.add(json.getString("district_name_hindi"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(ViewAllProblems.this, android.R.layout.simple_spinner_dropdown_item, districts));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district_name = getDistrictId(position);
                Log.e("District_name", district_name);
                mandals.clear();
                getDataMandal(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getDataMandal(int position){
        //Creating a string request
        String district_id = getDistrictId(position);
        Log.d("District Id", district_id);
        StringRequest stringRequest = new StringRequest(Config.DATA_URL_MANDALS+district_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            mandal = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getdistricts to get the districts from the JSON Array
                            getMandals(mandal);
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

        Log.d("1", Integer.toString(position));
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    private void getMandals(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list

                if (LanguageId.language_id == 0 )
                mandals.add(json.getString(Config.TAG_MANDAL_NAME));

                else if (LanguageId.language_id == 1)
                    mandals.add(json.getString("mandal_name_telugu"));
                else if (LanguageId.language_id == 2)
                    mandals.add(json.getString("mandal_name_hindi"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner2.setAdapter(new ArrayAdapter<String>(ViewAllProblems.this, android.R.layout.simple_spinner_dropdown_item, mandals));

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mandal_name = null;
                mandal_name = getMandalId(position);
                Log.e("Mandal_name", mandal_name);

                villages.clear();
                getDataVillages(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void getDataVillages(int position){

        String mandal_id = getMandalId(position);

        Log.d("Mandal_id", mandal_id);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_URL_VILLAGES+mandal_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            village = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getdistricts to get the districts from the JSON Array
                            getVillages(village);
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

        Log.d("Mandal_position", Integer.toString(position));



    }

    private void getVillages(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                if (LanguageId.language_id == 0 )
                    villages.add(json.getString(Config.TAG_VILLAGE_NAME));

                else if (LanguageId.language_id == 1)
                    villages.add(json.getString("village_name_telugu"));

                else if (LanguageId.language_id == 2)
                    villages.add(json.getString("village_name_hindi"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner3.setAdapter(new ArrayAdapter<String>(ViewAllProblems.this, android.R.layout.simple_spinner_dropdown_item, villages));

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                village_name = getVillageId(position);
                Log.e("Village_name", village_name);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private String getDistrictId(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_DISTRICT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private String getDistrictName(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_DISTRICT_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }
    private String getMandalId(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = mandal.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_MANDAL_ID);

            Log.e("Name", name );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private String getMandal_name(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = mandal.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_MANDAL_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }


    private String getVillageId(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = village.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_VILLAGE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private String getVillage_name(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = village.getJSONObject(position);

            //Fetching name from that object
            name = json.getString(Config.TAG_VILLAGE_NAME);
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
                ViewAllProblems.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_INITIATIVE_STATUS},
                new int[]{R.id.problem, R.id.initiative_status});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewAllProblems.this,"Fetching Data","Wait...",false,false);
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
                loading = ProgressDialog.show(ViewAllProblems.this,"Fetching Data","Wait...",false,false);
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
                String s = rh.sendGetRequest(Config.URL_GET_ALL_FILT+district_name+"&m="+mandal_name+"&v="+village_name);
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
