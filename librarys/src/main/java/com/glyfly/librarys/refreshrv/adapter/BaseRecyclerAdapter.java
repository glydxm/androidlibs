package com.glyfly.librarys.refreshrv.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    protected Activity activity;
    protected List<T> data;
    protected int layout;
    protected LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BaseRecyclerAdapter(Activity activity, int layout) {
        this.activity = activity;
        this.layout = layout;
        this.data = new ArrayList<>();
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        BaseViewHolder viewHolder = BaseViewHolder.getHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        bindViewHolder((BaseViewHolder) holder, data.get(position), position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract void bindViewHolder(BaseViewHolder holder, T t, int position);

    public void setListData(List<T> list) {
        this.data.clear();
        if (list != null) {
            this.data.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addListData(List<T> list) {
        if (list != null) {
            data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addItemData(int index, T item) {
        if (item != null) {
            data.add(index, item);
            notifyDataSetChanged();
        }
    }

    public T removeItemData(int index) {
        T item = null;
        if (index > 0 && index < data.size()) {
            item = data.remove(index);
            notifyDataSetChanged();
        }
        return item;
    }

    public boolean removeItemData(T item) {
        boolean result = false;
        if (item != null) {
            result = data.remove(item);
            notifyDataSetChanged();
        }
        return result;
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }
}
