package com.example.stockcalendar;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class LinearRecycleViewActivity extends AppCompatActivity {
    private RecyclerView mRvMain;
    //    private LinkedList<String[]> mList;
    private String[][] mList;
    private ProgressBar mPB;
    private Button btnAddCal;
    private ArrayList<Integer> pos = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_recycle_view);

        btnAddCal = findViewById(R.id.addToCal);

        mList = (String[][]) getIntent().getExtras().getSerializable("array");


        mRvMain = findViewById(R.id.rv_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LinearRecycleViewActivity.this);
        mRvMain.setLayoutManager(layoutManager);
        mRvMain.addItemDecoration(new MyDecoration());
        final LinearAdapter mAdapter = new LinearAdapter(LinearRecycleViewActivity.this, mList);
        mRvMain.setAdapter(mAdapter);



        btnAddCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Integer,Boolean> map = mAdapter.getAllMap();
                if(map.isEmpty()){
                    Toast.makeText(LinearRecycleViewActivity.this,"You don't choose anything!",Toast.LENGTH_LONG).show();

                }else{

                    for(Map.Entry<Integer,Boolean> entry : map.entrySet()) {
                        pos.add(entry.getKey());
                        String title = mList[entry.getKey()][0];
                        String description = mList[entry.getKey()][2];
                        String date = mList[entry.getKey()][1];
                        String[] dateArray = date.split("-");
                        int[] dateIntArray = new int[3];
                        for(int i=0 ;i<3;i++){
                            dateIntArray[i] = Integer.valueOf(dateArray[i]);
                        }
                        AddCalendar.addCalendarEvent(LinearRecycleViewActivity.this,title,description,dateIntArray);

                    }

                    mAdapter.initialAll();
                    for(int item : pos) {
                        mAdapter.notifyItemChanged(item,0);
                    }
                    pos.clear();
                    Toast.makeText(LinearRecycleViewActivity.this,"Add Success",Toast.LENGTH_LONG).show();
                }

            }


        });



    }
    class MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}

