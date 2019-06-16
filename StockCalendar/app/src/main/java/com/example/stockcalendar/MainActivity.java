package com.example.stockcalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Button mbtnSend;
    TextView mtwLoading;
    EditText mEtStartDate;
    EditText mEtEndDate;

    ProgressBar mPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mbtnSend = findViewById(R.id.send);
        mtwLoading = findViewById(R.id.twLoading);
        mEtStartDate = findViewById(R.id.etStartDate);
        mEtEndDate = findViewById(R.id.etEndDate);
        mPB = findViewById(R.id.PB);


        mEtStartDate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘
        mEtEndDate.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘

        mEtStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        mEtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    showDatePickerDialog(v);
                }
            }
        });

        mEtEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        mEtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    showDatePickerDialog(v);
                }
            }
        });



        mbtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String startDate = mEtStartDate.getText().toString();
                String endDate = mEtEndDate.getText().toString();
                Log.d("date",startDate+":"+endDate);


                /*
                AsyncTask
                confirm NetWork
                 */
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connMgr != null) {
                    networkInfo = connMgr.getActiveNetworkInfo();
                }

                if (networkInfo != null && networkInfo.isConnected() && startDate.length() !=0 && endDate.length() != 0) {

                    mtwLoading.setText("Loading...");
                    mPB.setVisibility(View.VISIBLE);

                    String[] queryString = {startDate,endDate};
                    readJson json_return = new readJson(MainActivity.this,mPB);
                    json_return.execute(queryString);

                }else if(startDate.length() == 0 || endDate.length() == 0){
                    mtwLoading.setText("Incorrect Input !");
                }else {
                    mtwLoading.setText("Unavailable Network !");
                }

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mtwLoading.setText("");
        mPB.setProgress(0);
        mPB.setVisibility(View.INVISIBLE);
    }

    private void showDatePickerDialog(final View v) {
        Calendar c = Calendar.getInstance();

        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                String month;
                String day;
                if (monthOfYear < 9){
                    month = "0"+(monthOfYear+1);
                }else{
                    month = ""+(monthOfYear+1);
                }
                if (dayOfMonth < 9){
                    day = "0"+dayOfMonth;
                }else{
                    day = ""+dayOfMonth;
                }
                switch (v.getId()){
                    case R.id.etStartDate:
                        mEtStartDate.setText(year+"-"+month+"-"+day);
                        break;
                    case R.id.etEndDate:
                        mEtEndDate.setText(year+"-"+month+"-"+day);
                }


            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

    }

}
