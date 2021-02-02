package com.example.android.hrs.blueprints.jumpmeasurementapp.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/*
* これは、アクティビティがUIをロードするのに役立つメソッドを提供します。
* */
public class ActivityUtils {

    /*
    *
    *
    * */
    public static void addFragmentToActivity (@Nonnull FragmentManager fragmentManager,
                                        @Nonnull Fragment fragment,int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(frameId, fragment);
        transaction.replace(frameId, fragment);
        transaction.commit();
//        manager.beginTransaction().replace(fragment, new Fragment1()).commit();
    }

    public static void removeFragmentToActivity (@Nonnull FragmentManager fragmentManager,
                                              @Nonnull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(frameId, fragment);
        transaction.detach(fragment);
//        transaction.commit();
//        manager.beginTransaction().replace(fragment, new Fragment1()).commit();
    }





}
