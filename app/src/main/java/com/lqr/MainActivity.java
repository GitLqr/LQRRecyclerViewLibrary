package com.lqr;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqr.recyclerview.LQRRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 CSDN_LQR
 * @描述 LQRRecyclerView的Demo
 */
public class MainActivity extends AppCompatActivity {

    private int[] mListIcons = new int[]{R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4,
            R.mipmap.g5, R.mipmap.g6, R.mipmap.g7, R.mipmap.g8, R.mipmap.g9, R.mipmap.g10, R
            .mipmap.g11, R.mipmap.g12, R.mipmap.g13, R.mipmap.g14, R.mipmap.g15, R.mipmap
            .g16, R.mipmap.g17, R.mipmap.g18, R.mipmap.g19, R.mipmap.g20, R.mipmap.g21, R
            .mipmap.g22, R.mipmap.g23, R.mipmap.g24, R.mipmap.g25, R.mipmap.g26, R.mipmap
            .g27, R.mipmap.g28, R.mipmap.g29};
    private int[] mStaggeredIcons = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R
            .mipmap.p4, R.mipmap.p5, R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R
            .mipmap.p10, R.mipmap.p11, R.mipmap.p12, R.mipmap.p13, R.mipmap.p14, R.mipmap
            .p15, R.mipmap.p16, R.mipmap.p17, R.mipmap.p18, R.mipmap.p19, R.mipmap.p20, R
            .mipmap.p21, R.mipmap.p22, R.mipmap.p23, R.mipmap.p24, R.mipmap.p25, R.mipmap
            .p26, R.mipmap.p27, R.mipmap.p28, R.mipmap.p29, R.mipmap.p30, R.mipmap.p31, R
            .mipmap.p32, R.mipmap.p33, R.mipmap.p34, R.mipmap.p35, R.mipmap.p36, R.mipmap
            .p37, R.mipmap.p38, R.mipmap.p39, R.mipmap.p40, R.mipmap.p41, R.mipmap.p42, R
            .mipmap.p43, R.mipmap.p44};

    private List<DataBean> mListData;
    private List<DataBean> mStaggerData;
    private LQRRecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mRv = (LQRRecyclerView) findViewById(R.id.rv);
    }

    private void initData() {
        mListData = new ArrayList<>();
        for (int i = 0; i < mListIcons.length; i++) {
            DataBean bean = new DataBean();
            bean.setText("item " + i);
            bean.setResId(mListIcons[i]);
            mListData.add(bean);
        }

        mStaggerData = new ArrayList<>();
        for (int i = 0; i < mStaggeredIcons.length; i++) {
            DataBean bean = new DataBean();
            bean.setText("item " + i);
            bean.setResId(mStaggeredIcons[i]);
            mStaggerData.add(bean);
        }

        MyAdapter myAdapter = new MyAdapter(this, mStaggerData);
        mRv.setAdapter(myAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLinearV:
                mRv.setType(LQRRecyclerView.TYPE_GRID);
                mRv.setColumn(1);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_VERTICAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemLinearH:
                mRv.setType(LQRRecyclerView.TYPE_GRID);
                mRv.setColumn(1);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_HORIZONTAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemGridV:
                mRv.setType(LQRRecyclerView.TYPE_GRID);
                mRv.setColumn(2);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_VERTICAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemGridH:
                mRv.setType(LQRRecyclerView.TYPE_GRID);
                mRv.setColumn(2);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_HORIZONTAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemStaggerV:
                mRv.setType(LQRRecyclerView.TYPE_STAGGER);
                mRv.setColumn(2);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_VERTICAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemStaggerH:
                mRv.setType(LQRRecyclerView.TYPE_STAGGER);
                mRv.setColumn(2);
                mRv.setOrientation(LQRRecyclerView.ORIENTATION_HORIZONTAL);
                mRv.notifyViewChanged();
                break;
            case R.id.itemMoveTo5:
                mRv.moveToPosition(5);
                break;
            case R.id.itemSmoothTo8:
                mRv.smoothMoveToPosition(8);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private Context mContext;
        private List<DataBean> mData;

        public MyAdapter(Context context, List<DataBean> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = View.inflate(mContext, R.layout.item_list, null);
            View view = View.inflate(mContext, R.layout.item_stagger, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.iv.setImageResource(mData.get(position).getResId());
            holder.tv.setText(mData.get(position).getText());
        }

        @Override
        public int getItemCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv;
            TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.iv);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
