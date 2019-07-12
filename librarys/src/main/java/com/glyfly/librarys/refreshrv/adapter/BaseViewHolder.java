package com.glyfly.librarys.refreshrv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2017/7/26.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;
    private View convertView;
    protected Context mContext;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        convertView = itemView;
        views = new SparseArray<>();
        convertView.setTag(this);
    }

    public static BaseViewHolder getHolder(View convertView){
        if (convertView == null || convertView.getTag() == null){
            return new BaseViewHolder(convertView);
        }else {
            return (BaseViewHolder) convertView.getTag();
        }

    }

    public View findViewById(int viewId){
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return view;
    }

    public View getConvertView(){
        return convertView;
    }
}
