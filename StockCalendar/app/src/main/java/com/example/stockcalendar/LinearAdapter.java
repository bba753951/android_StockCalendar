package com.example.stockcalendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {
    private Context mContext;
    //    private final LinkedList<String[]> mWordList;
    private String[][] mWordList;
    private Map<Integer, Boolean> map = new HashMap<>();
    //    private OnItemClickListener mListener;
//
    public LinearAdapter(Context context,String[][] mList){
        this.mContext=context;
        this.mWordList = mList;
//        Log.d("bbb",mWordList.getFirst()[0]);
        Log.d("bbb","adapter");
//        this.mListener=listener;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("bbb","onCreateViewHolder");
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_linear_item,viewGroup,false));

    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder viewHolder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(viewHolder, position, payloads);
        if (payloads.isEmpty()) {
            //payloads为空 即不是调用notifyItemChanged(position,payloads)方法执行的
            //在这里进行初始化item全部控件

            String[] mCurrent = mWordList[position];

            viewHolder.textView.setText(mCurrent[0]);
            viewHolder.twDate.setText(mCurrent[1]);
            viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        map.put(position, true);
                    } else {
                        map.remove(position);
                    }
                }
            });

            if (map != null && map.containsKey(position)) {
                viewHolder.mCheckBox.setChecked(true);
            } else {
                viewHolder.mCheckBox.setChecked(false);
            }

        }else{
            //payloads不为空 即调用notifyItemChanged(position,payloads)方法后执行的
//            int type= (int) payloads.get(0);// 刷新哪个部分 标志位
            if (map != null && map.containsKey(position)) {
                viewHolder.mCheckBox.setChecked(true);
            } else {
                viewHolder.mCheckBox.setChecked(false);
            }

        }
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder viewHolder, final int position) {


    }
    public void initialAll(){
        map.clear();
        Log.d("map",""+map);
    }

    @Override
    public int getItemCount() {
        return mWordList.length;
    }


    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private TextView twDate;
        private CheckBox mCheckBox;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
            twDate = itemView.findViewById(R.id.tv_date);
            mCheckBox = itemView.findViewById(R.id.chb);
        }
    }
    public Map<Integer,Boolean>getAllMap(){
        return map;
    }
}
