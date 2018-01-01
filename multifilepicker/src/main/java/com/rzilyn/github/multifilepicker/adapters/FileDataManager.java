package com.rzilyn.github.multifilepicker.adapters;

import android.util.Log;

import com.rzilyn.github.multifilepicker.model.BaseFile;
import com.rzilyn.github.multifilepicker.model.GeneralFile;
import com.rzilyn.github.multifilepicker.utils.Sort;
import com.rzilyn.github.multifilepicker.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Rizal Fahmi on 19-Dec-17.
 */

public class FileDataManager <T> implements FileContract<T> {

    private List<T> originalFileList;
    private List<T> tempFileList;

    private Map<String, T> selectedFile = new LinkedHashMap<>();

    private List<BaseFileAdapter> adapter;
    private boolean hasFinishedloading = false;

    public FileDataManager(){
        this.adapter = new ArrayList<>();
        this.originalFileList = new ArrayList<>();
        this.tempFileList = new ArrayList<>();
    }

    public void addAdapter(BaseFileAdapter adapter) {
        this.adapter.add(adapter);
    }

    public void replaceData(List<T> fileList, int position) {
        this.originalFileList = fileList;
        this.tempFileList = fileList;
        replaceAdapterData(fileList,position);
    }

    private void replaceAdapterData(List<T> fileList, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter)
            adapter.get(position).replaceData((List<GeneralFile>) fileList);
        Log.d("SearchQuery","Text : "+fileList.size());
        notifyDataChange(position);
    }

    private void addAdapterData(T file, int position){
        if(adapter.get(position) instanceof SimpleFileAdapter)
            adapter.get(position).addData((GeneralFile) file);
        notifyDataInsert(position);
    }

    public void notifyDataInsert(int position){
        adapter.get(position).notifyDataInsert();
    }

    public void notifyDataChange(int position){
        adapter.get(position).notifyDataSetChanged();
    }

    public void notifyItemRangeChanged(){

    }

    @Override
    public BaseFileAdapter getAdapter(int position){
        return adapter.get(position);
    }


    public void recoverOriginalData(int position){
        tempFileList.clear();
        tempFileList.addAll(originalFileList);
        replaceAdapterData(tempFileList,position);
    }

    public void sortFile(Sort.Type type, Sort.Order order, boolean replaceData, int position){
        if(adapter instanceof SimpleFileAdapter) {
            Collections.sort((List<GeneralFile>) tempFileList, new BaseFile.FileComparator(type,order));
            Collections.sort((List<GeneralFile>) originalFileList, new BaseFile.FileComparator(type,order));
            if(replaceData)
               replaceAdapterData(tempFileList, position);
        }
    }

    public void filterFile(final String phrase, boolean replaceData, int position){
        if(adapter instanceof SimpleFileAdapter) {
            List<T> result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Util.filter(originalFileList, new Predicate<T>() {
                    @Override
                    public boolean test(T t) {
                        GeneralFile file = (GeneralFile) t;
                        return phrase.equals("") || file.getFilename().toLowerCase().contains(phrase.toLowerCase());
                    }
                });
            } else
                result = Util.filter(originalFileList, new com.android.internal.util.Predicate<T>() {
                    @Override
                    public boolean apply(T t) {
                        GeneralFile file = (GeneralFile) t;
                        return phrase.equals("") || file.getFilename().toLowerCase().contains(phrase.toLowerCase());
                    }
                });
            if(replaceData)
                replaceTempData(result,position);
        }
    }

    public boolean hasFinishedLoading(){
        return hasFinishedloading;
    }

    public void setHasfinishedLoading(boolean flag){
        this.hasFinishedloading = flag;
    }

    public void replaceTempData(List<T> fileList, int position) {
        tempFileList = fileList;
        replaceAdapterData(fileList,position);
    }

    public void addData(T file, int position) {
        originalFileList.add(file);
        tempFileList.add(file);
        addAdapterData(file,position);
    }


    public void addSelectedFile(String key, T file) {
        selectedFile.put(key,file);
        Log.d("DataManager","Put ? "+selectedFile.containsKey(key));
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

    @Override
    public List<T> getData() {
        return tempFileList;
    }


}
