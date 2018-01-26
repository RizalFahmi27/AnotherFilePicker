package com.github.rzilyn.multifilepicker.adapters;

import android.util.Log;

import com.github.rzilyn.multifilepicker.model.Album;
import com.github.rzilyn.multifilepicker.model.BaseFile;
import com.github.rzilyn.multifilepicker.model.RawFile;
import com.github.rzilyn.multifilepicker.model.GenericFiles;
import com.github.rzilyn.multifilepicker.utils.Sort;
import com.github.rzilyn.multifilepicker.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class FileDataManager <T extends BaseFile> implements FileContract<T> {

    private List<T> originalFileList;

    private Map<String, T> selectedFile = new LinkedHashMap<>();

    private List<BaseFileAdapter> adapter;
    private boolean hasFinishedloading = false;

    public FileDataManager(){
        this.adapter = new ArrayList<>();
        this.originalFileList = new ArrayList<>();
    }

    public void addAdapter(BaseFileAdapter adapter) {
        this.adapter.add(adapter);
    }

    public void replaceData(List<T> fileList) {
        this.originalFileList = fileList;
        for(int i=0;i<adapter.size();i++)
            replaceAdapterData(fileList,i);
    }

    private void replaceAdapterData(List<T> fileList, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter) {
            SimpleFileAdapter simpleAdapter = (SimpleFileAdapter) adapter.get(position);
            String projection = simpleAdapter.getProjection();
            List<T> newData = new ArrayList<>();
            if(projection != null && !projection.equals("")){
                for(T t : fileList){
                    GenericFiles file = (GenericFiles) t;
                    if(file.getFileType()!=null &&
                            file.getFileType().equals(projection))
                        newData.add(t);
                }
            }
            else newData.addAll(fileList);
            simpleAdapter.replaceData((List<GenericFiles>) newData);

            if(newData.size() < 1)
                simpleAdapter.noticeEmptyData();
        }
        else if(adapter.get(position) instanceof AlbumAdapter){
          AlbumAdapter albumAdapter = (AlbumAdapter) adapter.get(position);
          albumAdapter.replaceData((List<Album>) fileList);
        }
        notifyDataChange(position);
    }


    private void addAdapterData(T t){
        for(int i=0;i<adapter.size();i++){
            if(t instanceof GenericFiles){
                GenericFiles file = (GenericFiles) t;
                if(file.getFileType().equals(((SimpleFileAdapter)adapter.get(i)).getProjection()))
                    adapter.get(i).addData(t);
            }
        }
    }

    public void notifyDataInsert(int position){
        adapter.get(position).notifyDataInsert();
    }

    public void notifyDataChange(int position){
        adapter.get(position).notifyDataSetChanged();
    }


    @Override
    public BaseFileAdapter getAdapter(int position){
        return adapter.get(position);
    }

    @Override
    public void setData(String projection, Sort.Type sortType, Sort.Order sortOrder, String searchQuery, int position) {
        filterFile(projection,searchQuery,true,position);
        sortFile(sortType,sortOrder,true,position);
    }


    public void recoverOriginalData(int position){
        replaceAdapterData(originalFileList,position);
    }

    public void sortFile(Sort.Type type, Sort.Order order, boolean replaceData, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter) {
            List fileList = adapter.get(position).getFileList();
            Collections.sort((List<GenericFiles>) fileList, new BaseFile.FileComparator(type,order));
            Collections.sort(originalFileList, new BaseFile.FileComparator(type,order));
            if(replaceData)
               replaceAdapterData(fileList, position);
        }
    }

    public void filterFile(final String phrase, boolean replaceData, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter) {
            List<T> result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Util.filter(originalFileList, new Predicate<T>() {
                    @Override
                    public boolean test(T t) {
                        GenericFiles file = (GenericFiles) t;
                        return phrase.equals("") || file.getFilename().toLowerCase().contains(phrase.toLowerCase());
                    }
                });
            } else
                result = Util.filter(originalFileList, new com.android.internal.util.Predicate<T>() {
                    @Override
                    public boolean apply(T t) {
                        GenericFiles file = (GenericFiles) t;
                        return phrase.equals("") || file.getFilename().toLowerCase().contains(phrase.toLowerCase());
                    }
                });
            if(replaceData)
                replaceAdapterData(result,position);
        }
    }

    public void filterFile(final String projection, final String phrase, boolean replaceData, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter) {
            List<T> result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Util.filter(originalFileList, new Predicate<T>() {
                    @Override
                    public boolean test(T t) {
                        GenericFiles file = (GenericFiles) t;
                        return phrase.equals("") || ( file.getFilename().toLowerCase().contains(phrase.toLowerCase())
                        && file.getFileType().equals(projection));
                    }
                });
            } else
                result = Util.filter(originalFileList, new com.android.internal.util.Predicate<T>() {
                    @Override
                    public boolean apply(T t) {
                        GenericFiles file = (GenericFiles) t;
                        return phrase.equals("") || ( file.getFilename().toLowerCase().contains(phrase.toLowerCase())
                                && file.getFileType().equals(projection));
                    }
                });
            if(replaceData)
                replaceAdapterData(result,position);
        }
    }

    public void notifyItemChanged(int itemPosition, int adapterPosition){
        adapter.get(adapterPosition).notifyItemChanged(itemPosition);
    }

    public boolean hasFinishedLoading(){
        return hasFinishedloading;
    }

    public void setHasfinishedLoading(boolean flag){
        this.hasFinishedloading = flag;
    }


    public void addData(T file) {
        originalFileList.add(file);
        addAdapterData(file);
    }


    public void addSelectedFile(String key, T file) {
        selectedFile.put(key,file);
    }

    public void removeSelectedFile(String key) {
        selectedFile.remove(key);
    }

    public Map<String, T> getSelectedFile() {
        return selectedFile;
    }

    public boolean isFileSelected(String key){
        return selectedFile.containsKey(key);
    }

}
