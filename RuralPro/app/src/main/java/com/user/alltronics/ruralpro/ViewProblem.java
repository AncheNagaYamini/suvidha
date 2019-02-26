package com.user.alltronics.ruralpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ViewProblem extends AppCompatActivity implements View.OnClickListener{

    private TextView editTextSubject;
    private TextView editTextDescription;
    private TextView editTextDistrict;
    private TextView editTextMandal;
    private TextView editTextVillage;
    private TextView editTextSolution;
    private TextView editTextInitiative;
    private TextView TvInitiative;
    private TextView TvSolution;

    private Button viewUserSolutions;

    MediaPlayer mediaPlayer;

    private String id;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private static String audioLink;

    String TvInitiativeT;
    String TvSolutionT;
    String audio_yes_no;

    ImageButton playPause;
    ImageButton reset;

    private boolean paused = true;

    Button button1;

    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    private Spinner language;


    String request_url = "https://pro10997.000webhostapp.com/RuralPro/getImages.php?id=";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problem);

        Intent intent = getIntent();

        id = intent.getStringExtra(Config.TAG_ID);

        request_url = request_url + id;

        Log.e("id",id);

        playPause = (ImageButton) findViewById(R.id.playPause);

        playPause.setOnClickListener(this);

        reset = (ImageButton) findViewById(R.id.reset);

        reset.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();



        language = (Spinner) findViewById(R.id.language);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("English");
        spinnerArray.add("Telugu");
        spinnerArray.add("Hindi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item_1, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);

        editTextSubject = (TextView) findViewById(R.id.editTextSubject);
        editTextDescription = (TextView) findViewById(R.id.editTextDescription);
        editTextDistrict = (TextView) findViewById(R.id.editTextDistrict);

        editTextMandal = (TextView) findViewById(R.id.editTextMandal);
        editTextVillage = (TextView) findViewById(R.id.editTextVillage);
        editTextSolution = (TextView) findViewById(R.id.editTextSolution);
        editTextInitiative = (TextView) findViewById(R.id.editTextInitiative);
        TvInitiative = (TextView) findViewById(R.id.TvInitiative);
        TvSolution = (TextView) findViewById(R.id.TvSolution);

        TvInitiativeT = TvInitiative.getText().toString();
        TvSolutionT = TvSolution.getText().toString();




        rq = CustomVolleyRequest.getInstance(this).getRequestQueue();

        sliderImg = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //     button1 = (Button) findViewById(R.id.button);

        //       button1.setOnClickListener(this);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        sendRequest();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getAudio();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                int icon;

                icon = R.drawable.ic_play_button_3;

                playPause.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), icon));


                //mPlayButton.setText("Play");
            }
        });


        getProblem();






        viewUserSolutions = (Button) findViewById(R.id.buttonUser);

        viewUserSolutions.setOnClickListener(this);

        final String viewUserSolutionsT = viewUserSolutions.getText().toString();


        language.setSelection(LanguageId.language_id);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", Integer.toString(position));


                if ( position == 0 ) {

                    //if(mediaPlayer.isPlaying())
                      //  mediaPlayer.pause();


                    //mediaPlayer.reset();
                    getProblem();

                    LanguageId.language_id = 0;

                    TvSolution.setText(TvSolutionT);

                    TvInitiative.setText(TvInitiativeT);

                    viewUserSolutions.setText(viewUserSolutionsT);

                }

                if ( position == 1 ) {

                    //if(mediaPlayer.isPlaying())
                      //  mediaPlayer.pause();


                    //mediaPlayer.reset();

                    LanguageId.language_id = 1;
                    getProblem();

                    TvSolution.setText("పరిష్కారం");

                    TvInitiative.setText("తీసుకున్న చర్య");

                    viewUserSolutions.setText("ఇతరుల పరిష్కారములు");


                }

                if ( position == 2 ) {

                    //if(mediaPlayer.isPlaying())
                      //  mediaPlayer.pause();


                    //mediaPlayer.reset();

                    LanguageId.language_id = 2;
                    getProblem();

                    TvSolution.setText("उपाय");

                    TvInitiative.setText("पहल");

                    viewUserSolutions.setText("अन्य उपाय");


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void sendRequest() {

        JsonArrayRequest req = new JsonArrayRequest(request_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i < response.length(); i++){

                            SliderUtils sliderUtils = new SliderUtils();

                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                sliderUtils.setSliderImageUrl(jsonObject.getString("image_url"));

                                Log.e("image_url", sliderUtils.getSliderImageUrl());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            sliderImg.add(sliderUtils);

                        }


                        if ( response.length() == 0 ) {

                            viewPager.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }

                        viewPagerAdapter = new ViewPagerAdapter(sliderImg, ViewProblem.this);



                        viewPager.setAdapter(viewPagerAdapter);

                        dotscount = viewPagerAdapter.getCount();

                        if( dotscount > 0 ) {
                            dots = new ImageView[dotscount];

                            for (int i = 0; i < dotscount; i++) {

                                dots[i] = new ImageView(ViewProblem.this);
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                params.setMargins(8, 0, 8, 0);

                                sliderDotspanel.addView(dots[i], params);

                            }

                            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        CustomVolleyRequest.getInstance(this).addToRequestQueue(req);


    }

    private void getAudio(){
        class GetAudio extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewProblem.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getAudioLink(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_PROB,id);
                return s;
            }
        }
        GetAudio ge = new GetAudio();
        ge.execute();
    }

    private void getAudioLink(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);


            audioLink = c.getString("audio");
            audio_yes_no = c.getString("audio_yes_no");

            if ( audio_yes_no.equals("no") ) {
                playPause.setVisibility(View.GONE);
                reset.setVisibility(View.GONE);
            }

            else if ( audio_yes_no.equals("yes") ) {

                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(audioLink);
                    mediaPlayer.prepare();
                } catch (IOException e) {

                }
            }



            Log.e("link", audioLink);







        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getProblem(){
        class GetProblem extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewProblem.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showProblem(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_PROB,id);
                return s;
            }
        }
        GetProblem ge = new GetProblem();
        ge.execute();
    }



    private void showProblem(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String problem_subject = c.getString(Config.KEY_EMP_PROB_SUBJECT);
            String extra_subject = c.getString(Config.KEY_EMP_SUBJECT);

            String subject = problem_subject + " - " +extra_subject;
            String description = c.getString(Config.KEY_EMP_DESCRIPTION);
            String district = c.getString(Config.TAG_DISTRICT_NAME);
            String district_name_telugu = c.getString("district_name_telugu");
            String district_name_hindi = c.getString("district_name_hindi");
            String mandal = c.getString(Config.TAG_MANDAL_NAME);
            String mandal_name_telugu = c.getString("mandal_name_telugu");
            String mandal_name_hindi = c.getString("mandal_name_hindi");
            String village = c.getString(Config.TAG_VILLAGE_NAME);
            String village_name_telugu = c.getString("village_name_telugu");
            String village_name_hindi = c.getString("village_name_hindi");
            village = village + ", "+ mandal + ", " + district;
            village_name_telugu = village_name_telugu + ", "+ mandal_name_telugu + ", " + district_name_telugu;
            village_name_hindi = village_name_hindi + ", "+ mandal_name_hindi + ", " + district_name_hindi;
            String solution = c.getString(Config.TAG_SOLUTION);
            String initiative = c.getString(Config.TAG_INITIATIVE);

            audioLink = c.getString("audio");



            Log.e("link", audioLink);



            editTextSubject.setText(subject);
            editTextDescription.setText(description);
            editTextDistrict.setText(district);

            editTextMandal.setText(mandal);

            if ( LanguageId.language_id == 0 )
            editTextVillage.setText(village);

            else if ( LanguageId.language_id == 1 )
                editTextVillage.setText(village_name_telugu);

            else if ( LanguageId.language_id == 2 )
                editTextVillage.setText(village_name_hindi);


            editTextSolution.setText(solution);
            editTextInitiative.setText(initiative);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buttonPressed(View view) {

        ImageButton button = (ImageButton) view;
        int icon;

        if (paused) {
            paused = false;


            icon = R.drawable.ic_pause_1;
        }
    else {
                paused = true;
                icon = R.drawable.ic_play_button_3;
        }

            button.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));


        }


    @Override
    public void onClick(View v) {
        int icon;

        if ( v == viewUserSolutions ) {

            Intent intent = new Intent(this, ViewUserSolutions.class);
            intent.putExtra("problem_id",id);
            startActivity(intent);
        }

        if ( v == playPause ) {




            if (paused) {
                paused = false;




                mediaPlayer.start();


                icon = R.drawable.ic_pause_1;

                reset.setVisibility(View.INVISIBLE);
            }

            else {
                mediaPlayer.pause();
                paused = true;


                icon = R.drawable.ic_play_button_3;

                reset.setVisibility(View.VISIBLE);
            }

            playPause.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));
        }

        if ( v == reset ) {

            if(mediaPlayer.isPlaying())
                mediaPlayer.pause();


            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(audioLink);
                mediaPlayer.prepare();
            } catch (IOException e) {

            }
        }
    }
}
