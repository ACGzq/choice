package com.example.administrator.choice;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import io.reactivex.internal.operators.observable.ObservableOnErrorNext;

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
    public ActionBar mActionBar;
    public View mShareView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
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
    private Intent getMyIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "主题");
        intent.putExtra(Intent.EXTRA_TEXT, "我的文字内容，zhangphil text");
        return intent;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        //搜索框相关
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        //searchItem.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("choice:",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("choice:",newText);
                return true;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("choice:","onMenuItemActionExpand");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("choice:","onMenuItemActionExpand");
                return true;
            }
        });
        //分享菜单相关
        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(getMyIntent());
        //获取分享菜单的View
        mShareView = shareActionProvider.onCreateActionView();

        LinearLayout layout = (LinearLayout) findViewById(R.id.main_layout);
        int wrap = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        //添加到布局中
        layout.addView(mShareView, wrap, wrap);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("choice:","onOptionsItemSelected"+item.getTitle());
        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(getApplicationContext(), "分享", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.setting:
                Toast.makeText(getApplicationContext(), "设置", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.about:
                Toast.makeText(getApplicationContext(), "关于", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
