package com.rsoftware.findworkapp.ui.responses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResponsesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ResponsesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}