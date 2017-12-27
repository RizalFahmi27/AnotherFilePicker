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

    private BaseFileAdapter adapter;
    private boolean hasFinishedloading = false;

    public FileDataManager(){
        this.originalFileList = new ArrayList<>();
        this.tempFileList = new ArrayList<>();
    }

    public void addAdapter(BaseFileAdapter adapter) {
        this.adapter = adapter;
    }

    public void replaceData(List<T> fileList) {
        this.originalFileList = fileList;
        this.tempFileList = fileList;
        replaceAdapterData(fileList);
    }

    private void replaceAdapterData(List<T> fileList){
        if(adapter instanceof SimpleFileAdapter)
            adapter.replaceData((List<GeneralFile>) fileList);
        notifyDataChange();
    }

    private void addAdapterData(T file){
        if(adapter instanceof SimpleFileAdapter)
            adapter.addData((GeneralFile) file);
        notifyDataInsert();
    }

    public void notifyDataInsert(){
        adapter.notifyDataInsert();
    }

    public void notifyDataChange(){
        adapter.notifyDataChange();
    }

    @Override
    public BaseFileAdapter getAdapter(){
        return adapter;
    }

    public void recoverOriginalData(){
        tempFileList.clear();
        tempFileList.addAll(originalFileList);
        replaceAdapterData(tempFileList);
    }

    public void sortFile(Sort.Type type, Sort.Order order){
        if(adapter instanceof SimpleFileAdapter) {
            Collections.sort((List<GeneralFile>) tempFileList, new BaseFile.FileComparator(type,order));
            Collections.sort((List<GeneralFile>) originalFileList, new BaseFile.FileComparator(type,order));
            replaceAdapterData(tempFileList);
            notifyDataChange();
        }
    }

    public void filterFile(final String phrase){
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
                result = (List<T>) Util.filter(originalFileList, new com.android.internal.util.Predicate<T>() {
                    @Override
                    public boolean apply(T t) {
                        GeneralFile file = (GeneralFile) t;
                        return phrase.equals("") || file.getFilename().toLowerCase().contains(phrase.toLowerCase());
                    }
                });
            replaceTempData(result);
            notifyDataChange();
        }
    }

    public boolean hasFinishedLoading(){
        return hasFinishedloading;
    }

    public void setHasfinishedLoading(boolean flag){
        this.hasFinishedloading = flag;
    }

    public void replaceTempData(List<T> fileList) {
        this.tempFileList = fileList;
        replaceAdapterData(fileList);
    }

    public void addData(T file) {
        originalFileList.add(file);
        tempFileList.add(file);
        addAdapterData(file);
    }

    public void addTempData(T file) {
        tempFileList.add(file);
        notifyDataInsert();
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