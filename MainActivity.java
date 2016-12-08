package com.example.asus.myload;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<String> data = new ArrayList<>();
    private LinearLayoutManager layoutManager;


    private boolean isReady = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recy);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        for (int i = 0; i < 50; i++) {
            data.add("Data" + i);
        }
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(new MyRecyclerViewAdapter());

        mRecyclerView.addOnScrollListener(new MyScroll());
    }

    class MyScroll extends RecyclerView.OnScrollListener {
        int minBottom;
        int bottom;
        boolean isReady = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if ((layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1)) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//滑动
                    isReady = true;
                    //判断footer是否还是完全显示
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                        Log.e("DRAGGING", "松手加载");
                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        View lastItemView = layoutManager.findViewByPosition(position);
                        bottom = lastItemView.getBottom();
                        Log.e("DRAGGING", "Bottom" + bottom);
                        if (minBottom > bottom) {
                            minBottom = bottom;
                        }
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//抛
                    Log.e("SETTLING", "抛");
                    if ((bottom >= minBottom) && isReady) {
                        Log.e("SETTLING", "加载，上滑");
                        isReady = false;
                    } else {
                        Log.e("SETTLING", "不加载");
                        isReady = false;
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {//闲置
                    Log.e("IDLE", "Bottom" + bottom + "---" + minBottom);
                    if ((bottom >= minBottom) && isReady) {
                        Log.e("IDLE", "加载，上滑");
                        isReady = false;
                    } else {
                        Log.e("IDLE", "不加载");
                        isReady = false;
                    }
                }
            } else if (layoutManager.findLastCompletelyVisibleItemPosition() != layoutManager.getItemCount() - 1) {//没有在底部变化的时候
                isReady = false;
            } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {//抛
                isReady = false;
                Log.e("Yunr", "外部抛");
            }
        }
    }


    class MyRecyclerViewAdapter extends RecyclerView.Adapter<MainActivity.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recy_item, parent, false);
                return new ViewHolder(view);
            } else if (viewType == 2) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recy_item, parent, false);
                return new ViewHolder(view);
            } else {
                return null;
            }

        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == 1) {
                holder.textView.setText(data.get(position));
            } else {
                holder.textView.setText("上拉加载");
            }

        }


        @Override
        public int getItemCount() {

            return data.size() + 1;
        }


        @Override
        public int getItemViewType(int position) {

            if (position == getItemCount() - 1)
                return 2;

            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;


        public ViewHolder(View itemView) {

            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycler_tv_item);
        }
    }

}
