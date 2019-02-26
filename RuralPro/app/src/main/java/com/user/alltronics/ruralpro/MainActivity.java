package com.user.alltronics.ruralpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView TvPost;
    TextView TvPostProb;
    TextView TVView;
    TextView TVViewAllProb;
    TextView TVFilter;
    TextView TVFilterProb;


    String TvPostT;
    String TvPostProbT;
    String TVViewT;
    String TVViewAllProbT;
    String TVFilterT;
    String TVFilterProbT;

    private CardView buttonPost;
    private CardView buttonView;
    private CardView buttonFilter;

    private Spinner language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPost = (CardView) findViewById(R.id.buttonAdd);
        buttonView = (CardView) findViewById(R.id.buttonView);
        buttonFilter = (CardView) findViewById(R.id.buttonFilter);

        language = (Spinner) findViewById(R.id.language);

        TvPost = (TextView) findViewById(R.id.TVPost);
        TvPostProb = (TextView) findViewById(R.id.TVPostProb);
        TVView = (TextView) findViewById(R.id.TVView);
        TVViewAllProb = (TextView) findViewById(R.id.TVViewAllProb);
        TVFilter = (TextView) findViewById(R.id.TVFilter);
        TVFilterProb = (TextView) findViewById(R.id.TVFilterProb);

        TvPostT= TvPost.getText().toString();
        TvPostProbT= TvPostProb.getText().toString();
        TVViewT= TVView.getText().toString();
        TVViewAllProbT= TVViewAllProb.getText().toString();
        TVFilterT= TVFilter.getText().toString();
        TVFilterProbT= TVFilterProb.getText().toString();


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


                    TvPost.setText(TvPostT);
                    TvPostProb.setText(TvPostProbT);
                    TVView.setText(TVViewT);
                    TVViewAllProb.setText(TVViewAllProbT);
                    TVFilter.setText(TVFilterT);
                    TVFilterProb.setText(TVFilterProbT);

                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;
                    TvPost.setText("వివరించు");
                    TvPostProb.setText("మీ సమస్య ని వివరించండి");
                    TVView.setText("సమస్యలు");
                    TVViewAllProb.setText("మీ గ్రామం లొ ఉన్న సమస్యలు మరియు వాటికి తీసుకున్న చర్యలు");
                    TVFilter.setText("సమస్యలు వెతకండి");
                    TVFilterProb.setText("జిల్లా, మండలం మరియు సమస్య వారీగా వెతకండి");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;
                    TvPost.setText("समस्या प्रस्तुत करो");
                    TvPostProb.setText("आपका समस्या के बारे में लिखे");
                    TVView.setText("समस्या");
                    TVViewAllProb.setText("आपका गांव में समस्या और लिए गए पहल");
                    TVFilter.setText("समस्या खोज करे");
                    TVFilterProb.setText("जिला, क्षेत्र और समस्या प्रकारों से खोजें");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonPost.setOnClickListener(this);
        buttonView.setOnClickListener(this);
        buttonFilter.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v == buttonPost){
            Intent intent = new Intent(this, PostActivity.class);

            intent.putExtra("id", Long.toString(System.currentTimeMillis()));

            startActivity(intent);

            //startActivity(new Intent(this,PostActivity.class));
        }

        if(v == buttonView){
            startActivity(new Intent(this,ViewAllProblems.class));
        }

        if(v == buttonFilter){
            startActivity(new Intent(this,FilterProblems.class));
        }



    }
}
