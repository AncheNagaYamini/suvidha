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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteASolution extends AppCompatActivity implements View.OnClickListener {

    //Defining views
    private EditText editTextUserSolution;


    private Button buttonPostSolution;

    private String id;
    private Spinner language;

    TextView TvType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_asolution);

        Intent intent = getIntent();

        id = intent.getStringExtra("problem_id");

        language = (Spinner) findViewById(R.id.language);

        TvType = (TextView) findViewById(R.id.TvType);


        //Initializing views
        editTextUserSolution = (EditText) findViewById(R.id.editTextUserSolution);


        buttonPostSolution = (Button)findViewById(R.id.buttonPostSolution);

        //Setting listeners to button
        buttonPostSolution.setOnClickListener(this);

        final String buttonPostSolutionT = buttonPostSolution.getText().toString();

        final String TvTypeT = TvType.getText().toString();



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

                    buttonPostSolution.setText(buttonPostSolutionT);

                    TvType.setText(TvTypeT);

                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;

                    TvType.setText("మీ పరిష్కారం");

                    buttonPostSolution.setText("మీ పరిష్కారము తెలపండి");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;

                    TvType.setText("आपका समाधान");

                    buttonPostSolution.setText("भेजें");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


    //Adding an UserSolution
    private void addUserSolution(){

        final String user_solution = editTextUserSolution.getText().toString().trim();

        class AddUserSolution extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WriteASolution.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(WriteASolution.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_EMP_PROB_ID,id);
                params.put(Config.KEY_EMP_SOLUTION,user_solution);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD_USER, params);
                return res;
            }
        }

        AddUserSolution ae = new AddUserSolution();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonPostSolution){

            addUserSolution();
            Intent intent = new Intent(this, ViewUserSolutions.class);
            intent.putExtra("problem_id",id);
            startActivity(intent);
        }
    }
}
