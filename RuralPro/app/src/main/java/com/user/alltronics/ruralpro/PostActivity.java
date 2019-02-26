package com.user.alltronics.ruralpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    TextView TvDistrict;
    TextView TvMandal;
    TextView TvVillage;
    TextView TvSubject;
    TextView TvShortDescription;
    TextView TVDetailedDescription;


    String TvDistrictT;
    String TvMandalT;
    String TvVillageT;
    String TvSubjectT;
    String TvShortDescriptionT;
    String TVDetailedDescriptionT;

    //Declaring an Spinner
    private Spinner spinner;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;



    //An ArrayList for Spinner Items
    private ArrayList<String> districts;

    private ArrayList<String> mandals;
    private ArrayList<String> villages;
    private ArrayList<String> problems;


    //JSON Array
    private JSONArray result;
    private JSONArray mandal;
    private JSONArray village;
    private JSONArray problem;





    private EditText editTextSubject;
    private EditText editTextDescription;

     String district_name;
     String mandal_name;
     String village_name;
     String problem_id;
     String problem_subject;
     String id;

    Button buttonStart, buttonStop, buttonPlayLastRecordAudio,
            buttonStopPlayingRecording, buttonReset ;

    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;

    boolean recording = false;


    private Button buttonPost;

    private Spinner language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");

        buttonStart = (Button) findViewById(R.id.ButtonRecord);
        buttonStop = (Button) findViewById(R.id.ButtonStop);
        buttonPlayLastRecordAudio = (Button) findViewById(R.id.ButtonPlay);
        buttonStopPlayingRecording = (Button)findViewById(R.id.ButtonStopPlay);
        buttonReset = (Button)findViewById(R.id.ButtonDelete);

        Log.e("language_id", Integer.toString(LanguageId.language_id));

        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(false);
        buttonStopPlayingRecording.setEnabled(false);

        language = (Spinner) findViewById (R.id.language);


        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("English");
        spinnerArray.add("Telugu");
        spinnerArray.add("Hindi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);

        language.setSelection(LanguageId.language_id);




        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    long time = System.currentTimeMillis();
                    String fil = Long.toString(time);
                    id = fil;
                    recording = true;

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    fil + ".mp3";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);

                    Toast.makeText(PostActivity.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);

                Log.d("Audio", AudioSavePathInDevice);

                Toast.makeText(PostActivity.this, "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStart.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStart.setEnabled(true);
                buttonStopPlayingRecording.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(true);

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recording = false;
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonPlayLastRecordAudio.setEnabled(false);
                buttonStopPlayingRecording.setEnabled(false);

            }
        });




        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        //Initializing the ArrayList
        districts = new ArrayList<String>();

        mandals = new ArrayList<String>();
        villages = new ArrayList<String>();
        problems = new ArrayList<String>();


        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinner);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);


        buttonPost = (Button) findViewById(R.id.buttonPost);

        buttonPost.setOnClickListener(this);



        TvDistrict = (TextView) findViewById(R.id.TvDistrict);
        TvMandal = (TextView) findViewById(R.id.TvMandal);
        TvVillage = (TextView) findViewById(R.id.TvVillage);
        TvSubject = (TextView) findViewById(R.id.TvSubject);
        TvShortDescription = (TextView) findViewById(R.id.TvShortDescription);
        TVDetailedDescription = (TextView) findViewById(R.id.TVDetailedDescription);

         TvDistrictT = TvDistrict.getText().toString();
         TvMandalT = TvMandal.getText().toString();
         TvVillageT = TvVillage.getText().toString();
         TvSubjectT = TvSubject.getText().toString();
         TvShortDescriptionT = TvShortDescription.getText().toString();
         TVDetailedDescriptionT= TVDetailedDescription.getText().toString();

          final String buttonPostT = buttonPost.getText().toString();

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", Integer.toString(position));


                if ( position == 0 ) {
                    LanguageId.language_id = 0;
                    getData();

                    TvDistrict.setText(TvDistrictT);
                    TvMandal.setText(TvMandalT);
                    TvVillage.setText(TvVillageT);
                    TvSubject.setText(TvSubjectT);
                    TvShortDescription.setText(TvShortDescriptionT);
                    TVDetailedDescription.setText(TVDetailedDescriptionT);

                    buttonPost.setText(buttonPostT);

                    getProblemData();



                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;
                    getData();

                    //జిల్లా మండలం గ్రామం విషయం వివరణ

                    TvDistrict.setText("జిల్లా");
                    TvMandal.setText("మండలం");
                    TvVillage.setText("గ్రామం");
                    TvSubject.setText("సమస్య రకం");
                    TvShortDescription.setText("విషయం");
                    TVDetailedDescription.setText("వివరణ");

                    buttonPost.setText("సమస్య పోస్ట్ చేయండి");

                    getProblemData();



                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;
                    getData();

                    //జిల్లా మండలం గ్రామం విషయం వివరణ

                    TvDistrict.setText("जिला");
                    TvMandal.setText("क्षेत्र");
                    TvVillage.setText("गाँव");
                    TvSubject.setText("समस्या प्रकार");
                    TvShortDescription.setText("विषय");
                    TVDetailedDescription.setText("विवरण");

                    buttonPost.setText("समस्या प्रस्तुत करो");

                    getProblemData();



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PostActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(PostActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PostActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(PostActivity.this, "Uploading..", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
               /*textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());*/
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(AudioSavePathInDevice);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
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
        spinner.setAdapter(new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_spinner_dropdown_item, districts));

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
        spinner2.setAdapter(new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_spinner_dropdown_item, mandals));

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
        spinner3.setAdapter(new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_spinner_dropdown_item, villages));

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
        spinner4.setAdapter(new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_spinner_dropdown_item, problems));

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


    private void addProblem(){

        final String subject = editTextSubject.getText().toString().trim();
        final String description = editTextDescription.getText().toString().trim();

        class AddProblem extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(PostActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(PostActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put(Config.KEY_EMP_SUBJECT,subject);
                params.put(Config.KEY_EMP_DESCRIPTION,description);
                params.put(Config.KEY_EMP_TYPE_ID,problem_id);
                params.put(Config.TAG_DISTRICT_NAME,district_name);
                params.put(Config.TAG_MANDAL_NAME,mandal_name);
                params.put(Config.TAG_VILLAGE_NAME,village_name);

                String audio_yes_no;

                if ( recording == true ) {
                    audio_yes_no = "yes";
                }
                else {
                    audio_yes_no = "no";
                }
                params.put("audio", audio_yes_no);

                Log.e("string", id);




                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddProblem ap = new AddProblem();
        ap.execute();
    }
    @Override
    public void onClick(View v) {

        if(v == buttonPost) {

            String subject = editTextSubject.getText().toString();


            if (problem_subject.matches("other") && subject.matches("")) {
                Toast.makeText(PostActivity.this, "Enter a short description",
                        Toast.LENGTH_LONG).show();
            }
            else {

                if( recording == true ) {
                    uploadVideo();
                }
                addProblem();
                startActivity(new Intent(this,MainActivity.class));

            }
        }

    }
}
