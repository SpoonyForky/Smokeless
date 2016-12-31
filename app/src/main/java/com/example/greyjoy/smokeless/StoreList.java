package com.example.greyjoy.smokeless;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class   StoreList {

    private static StoreList sStores;

    private List<StoreItem> mStores;

    public static StoreList get(Context context){
    if (sStores == null){
        sStores = new StoreList(context);

    }
        return sStores;
    }

    private StoreList (Context context){
        mStores = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            StoreItem storeA = new StoreItem();
            storeA.setName("Store #" + i);
            mStores.add(storeA);
        }
    }

    public List<StoreItem> getStores(){
        return mStores;
    }

    public StoreItem getStore(UUID id){
        for (StoreItem store : mStores){
            if (store.getName().equals(id)){
                return store;
            }
        }
        return null;
    }






}
