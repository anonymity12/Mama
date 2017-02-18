package com.example.t26wodiv1;

import android.content.Context;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.RecyclerView;


/**
 * Created by paul on 2/11/17.
 */

public class StaggeredHomeAdapter extends RecyclerView.Adapter<StaggeredHomeAdapter.MyViewHolder> {
    private List<String> mDatas;
    private LayoutInflater mInflater;
    private List<Integer> mHeights;


    //这个接口里的方法全部在MainActivity里再实现；他们是我们自己捏造的，功能还没有呢，要在main里实现
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;


    //The method that will be used in MainActivity to set OnClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //The constructor
    //Which receive the context and data (in this case ,
    //'data' is A-z and its ASCII number)
    public StaggeredHomeAdapter(Context context, List<String> datas)
    {
        //Obtains the LayoutInflater from the given context.
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        Log.d("GameA",">>>>>>>>>>>the datas.size is : "+mDatas.size());

        //initialize the mHeight variable for each mDatas.
        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < mDatas.size(); i++)
        {
            mHeights.add( (int) (100 + Math.random() * 300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_staggered_home, parent, false));
        return holder;
    }


    //Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        //get the TextView's LayoutParams,then set its height attribute.
        ViewGroup.LayoutParams lp = holder.tv.getLayoutParams();
        lp.height = mHeights.get(position);

        //using out layout params to specify the corresponding height attribute.
        holder.tv.setLayoutParams(lp);
        holder.tv.setText(mDatas.get(position));

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
//                    removeData(pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public void addData(int position)
    {
        mDatas.add(position, "Insert One");
        mHeights.add( (int) (100 + Math.random() * 300));
        notifyItemInserted(position);
    }

    public void removeData(int position)
    {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tv;

        public MyViewHolder(View view)
        {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);

        }
    }
}
