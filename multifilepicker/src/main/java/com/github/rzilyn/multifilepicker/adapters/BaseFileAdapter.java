package com.github.rzilyn.multifilepicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;


import com.github.rzilyn.multifilepicker.model.BaseFile;

import java.util.List;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class BaseFileAdapter<T extends BaseFile> extends RecyclerView.Adapter {
    public List<T> fileList;
    public Context mContext;
    public int[] colorScheme;

    protected void replaceData(List<T> fileList){
        this.fileList = fileList;
    }

    protected void addData(T file){
        this.fileList.add(file);
    }

    protected void notifyDataChange(){
        notifyDataSetChanged();
    }

    protected void notifyDataInsert(){
        notifyItemInserted(fileList.size() - 1);
    }

    protected List<T> getFileList(){
        return fileList;
    }

    public BaseFileAdapter(List<T> fileList, Context context, int[] colorScheme){
        this.fileList = fileList;
        this.mContext = context;
        this.colorScheme = colorScheme;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
