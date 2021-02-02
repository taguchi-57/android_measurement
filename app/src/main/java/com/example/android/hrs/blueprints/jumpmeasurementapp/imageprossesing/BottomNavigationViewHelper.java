package com.example.android.hrs.blueprints.jumpmeasurementapp.imageprossesing;

//import android.support.design.internal.BottomNavigationItemView;
//import android.support.design.internal.BottomNavigationMenuView;
//import android.support.design.widget.BottomNavigationView;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper {

    /**
     * BottomNavigationViewのアイテムのサイズの調整、アイコンサイズ調整、タイトルの削除
     *
     * @param view
     */
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {

            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {

                /**
                 * アイテムの幅調整
                 */
                BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) menuView.getChildAt(i);
                // noinspection RestrictedApi
//                bottomNavigationItemView.setShiftingMode(false);
//                // チェックされた値を設定すると、ビューが更新されるみたい
//                // noinspection RestrictedApi
//                bottomNavigationItemView.setChecked(false);

            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
