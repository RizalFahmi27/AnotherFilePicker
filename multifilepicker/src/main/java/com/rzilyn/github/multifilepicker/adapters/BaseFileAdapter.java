package com.rzilyn.github.multifilepicker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rzilyn.github.multifilepicker.model.GeneralFile;

import java.util.List;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class BaseFileAdapter extends RecyclerView.Adapter {
    public List<GeneralFile> fileList;
    public Context mContext;
    public int[] colorScheme;

    protected void replaceData(List<GeneralFile> fileList){
        this.fileList = fileList;
    }

    protected void addData(GeneralFile file){
        this.fileList.add(file);
    }

    protected void notifyDataChange(){
        notifyDataSetChanged();
    }

    protected void notifyDataInsert(){
        notifyItemInserted(fileList.size() - 1);
    }

    public BaseFileAdapter(List<GeneralFile> fileList, Context context, int[] colorScheme){
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
