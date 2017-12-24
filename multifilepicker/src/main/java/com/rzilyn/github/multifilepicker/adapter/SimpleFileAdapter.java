package com.rzilyn.github.multifilepicker.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rzilyn.github.multifilepicker.R;
import com.rzilyn.github.multifilepicker.model.GeneralFile;
import com.rzilyn.github.multifilepicker.utils.BaseAdapterListener;
import com.rzilyn.github.multifilepicker.utils.Util;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class SimpleFileAdapter extends BaseFileAdapter{

    private GeneralFile mCurrentItem;
    private BaseAdapterListener<GeneralFile> mAdapterListener;
    private boolean enableCheckBox;

    public SimpleFileAdapter(List<GeneralFile> fileList, Context context, int[] colorScheme, boolean enableCheckBox){
        super(fileList,context,colorScheme);
        this.fileList = fileList;
        this.enableCheckBox = enableCheckBox;
        if(!(mContext instanceof View.OnCreateContextMenuListener))
            throw new IllegalStateException("Context must implement OnCreateContextMenuListener");
        if(!(mContext instanceof BaseAdapterListener))
            throw new IllegalStateException("Context must implement AdapterListener");
        else mAdapterListener = (BaseAdapterListener<GeneralFile>) mContext;
    }

    public GeneralFile getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(GeneralFile file) {
        this.mCurrentItem = file;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View viewBrowser = LayoutInflater.from(mContext).inflate(R.layout.layout_open_explorer,null,false);
            return new ViewHolderBrowser(viewBrowser);
        }
        else if(viewType == 1){
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_simple_item_file,parent,false);
            return new ViewHolder(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final GeneralFile file = fileList.get(position);
        Log.d("Adapter","Unselected currentFileId : "+file.getId());
        if(holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;

            if(!enableCheckBox)
                viewHolder.checkBox.setVisibility(View.GONE);

            if(colorScheme.length > 2)
                viewHolder.checkBox.setCheckedColor(colorScheme[2]);

            if(mAdapterListener.isFileSelected(file)) {
                viewHolder.checkBox.setChecked(true, false);
                viewHolder.itemView.setSelected(true);
            }
            else {
                viewHolder.checkBox.setChecked(false, false);
                viewHolder.itemView.setSelected(false);
            }

            viewHolder.textFileSize.setText(Util.getFileSizeString(file.getFileSize()));
            viewHolder.textFileName.setText(file.getFilename());
            viewHolder.textDate.setText(file.getDateModified());
            viewHolder.textFileType.setText(file.getFileType());
            viewHolder.imageFileIcon.setColorFilter(ContextCompat.getColor(mContext,Util.getFileIconColor(file.getFileType()))
                    , android.graphics.PorterDuff.Mode.MULTIPLY);
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setCurrentItem(file);
                    return false;
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!mAdapterListener.isFileSelected(file) && mAdapterListener.onItemSelected(file)){
                            view.setSelected(true);
                            viewHolder.checkBox.setChecked(true, true);
                    }
                    else {
                        viewHolder.checkBox.setChecked(false,true);
                        mAdapterListener.onItemUnselected(file);
                        view.setSelected(false);
                    }
                }
            });

        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        if(fileList.get(position).getFileType().equals(Util.FILE_DUMMY)){
            return 0;
        }
        else return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textFileName;
        TextView textFileSize;
        TextView textDate;
        TextView textFileType;
        ImageView imageFileIcon;
        CustomCheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textFileType = itemView.findViewById(R.id.text_fileType);
            this.textFileName = itemView.findViewById(R.id.text_fileName);
            this.textFileSize = itemView.findViewById(R.id.text_fileSize);
            this.textDate = itemView.findViewById(R.id.text_date);
            this.imageFileIcon = itemView.findViewById(R.id.image_fileIcon);
            this.checkBox = itemView.findViewById(R.id.checkbox_selectedFile);
            this.checkBox.setEnabled(false);
            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) mContext);
        }

    }

    static class ViewHolderBrowser extends RecyclerView.ViewHolder{
        public ViewHolderBrowser(View itemView) {
            super(itemView);
        }
    }

}