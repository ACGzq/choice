package com.example.administrator.choice;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public ListView listView;
    public Button add,start,clean;
    public EditText editText;
    public TextView  show;
    public ArrayList<String> items;
    public BaseAdapter myAdapter ;
    public LayoutInflater mInflater;
    public Handler myHandler;
    private int totalTime;
    private int index;
    private int item_size;
    public boolean isRun;
    public boolean isStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        mInflater = LayoutInflater.from(this);
        listView = findViewById(R.id.list_view);
        add = findViewById(R.id.add);
        start = findViewById(R.id.ok);
        clean = findViewById(R.id.clean);
        editText = findViewById(R.id.text);
        show = findViewById(R.id.show_view);
        show.setVisibility(View.GONE);
        add.setOnClickListener(this);
        start.setOnClickListener(this);
        clean.setOnClickListener(this);
        listView.setAdapter(getAdapter());
        myHandler = new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                String str = editText.getText().toString().trim();
                if(str.isEmpty()){
                    Toast.makeText(this,"输入为空",Toast.LENGTH_SHORT).show();
                }else{
                    items.add(str);
                    editText.setText("");
                    myAdapter.notifyDataSetChanged();
                    show.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ok:
                if(isRun)
                    break;
                if(items == null || items.size()<1){
                    Toast.makeText(this,"没有待觉定的内容",Toast.LENGTH_SHORT).show();
                }else{
                    isRun = true;
                    isStop = false;
                    index = 0;
                    show.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    Random random = new Random();
                    item_size = items.size();
                    totalTime =  random.nextInt(item_size)+ item_size * 5 + random.nextInt(item_size) * 2;
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("flqg","index = " +index+" totalTime = " +totalTime);
                            if(index < totalTime && !isStop){
                                show.setText("神说:"+items.get(index %item_size));
                                myHandler.postDelayed(this,200);
                                index++;
                            }else{
                                isRun = false;
                                isStop = true;
                             }

                        }
                    },200);



                }
                break;
            case R.id.clean:
                isStop = true;
                items.clear();
                myAdapter.notifyDataSetChanged();
                show.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                break;
                default:

        }
    }
    public BaseAdapter getAdapter(){
        if(myAdapter == null){
            myAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return items.size();
                }

                @Override
                public Object getItem(int position) {
                    return items.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, final ViewGroup parent) {
                    ViewHolder holder = null;
                   if (convertView == null){
                       holder = new ViewHolder();
                       convertView = mInflater.inflate(R.layout.item_layout, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.item_text);
                        holder.delButton = (Button) convertView.findViewById(R.id.item_button);
                        holder.delButton.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                // favoriteNewsList是listview List型的的数据源
                                items.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }
                        });
                       convertView.setTag(holder);
                   }else{
                       holder = (ViewHolder) convertView.getTag();
                   }
                   holder.textView.setText(items.get(position));
                   return convertView;
                }
            };
        }
        return myAdapter;

    }
    //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
   private class ViewHolder {
       TextView textView;
       Button delButton;
   }
}
