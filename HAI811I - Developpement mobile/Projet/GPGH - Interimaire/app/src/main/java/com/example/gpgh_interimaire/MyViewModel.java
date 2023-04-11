package com.example.gpgh_interimaire;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> mUserTypeCompte;

    public MyViewModel() {
        mUserTypeCompte = new MutableLiveData<>();
    }

    public void setUserTypeCompte(String typeCompte) {
        mUserTypeCompte.setValue(typeCompte);
    }

    public LiveData<String> getUserTypeCompteLiveData() {
        return mUserTypeCompte;
    }


}

