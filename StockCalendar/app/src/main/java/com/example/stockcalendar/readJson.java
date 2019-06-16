package com.example.stockcalendar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class readJson extends AsyncTask<String, Void, String> {

    private WeakReference<ProgressBar> determinateBar;
    private String[][] mList;
    private Context mContext;



    readJson(Context context, ProgressBar dt) {
        this.mContext = context;
        determinateBar = new WeakReference<>(dt);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("bbb", "post");
        try {
            //...

            JSONArray jsonarray = new JSONArray(s);


            String title = null;
            String start = null;
            String description = null;
            int jsonLength = jsonarray.length();
            mList = new String[jsonLength][3];
            int count = 0;

            if (jsonLength > 0) {
                int increase = 100 / jsonLength;
                if (increase == 0){
                    increase = 1;
                }


                for (int i = 0; i < jsonLength; i++) {
                    count += increase;
                    determinateBar.get().incrementProgressBy(increase);
                    JSONObject job = jsonarray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                    start = job.getString("start");  // 得到 每个对象中的属性值
                    title = job.getString("title");
                    description = job.getString("description");
                    String[] array = {title, start ,description};
                    mList[i]=array;
                }
                if (count > 0) {
                    determinateBar.get().setProgress(100);
                }
                Intent intent = new Intent(mContext, LinearRecycleViewActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("array", mList);
                intent.putExtras(mBundle);
                mContext.startActivity(intent);


            }
//            if (mList != null) {
//                Log.d("bbb", mList.get(0)[0]);
//                asyncResponse.onDataReceivedSuccess(mList);//将结果传给回调接口中的函数
//            } else {
//                asyncResponse.onDataReceivedFailed();
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String[] strings) {

        return NetworkUtils.getBookInfo(strings);
    }
//    protected static LinkedList<String[]> getmList(){
//        Log.d("bbb",mList.getFirst()[0]);

//        return mList;
//    }
}

