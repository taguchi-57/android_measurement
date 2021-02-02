package com.example.android.hrs.blueprints.jumpmeasurementapp.start;

import androidx.appcompat.view.menu.BaseMenuPresenter;

import com.example.android.hrs.blueprints.jumpmeasurementapp.BasePresenter;
import com.example.android.hrs.blueprints.jumpmeasurementapp.BaseView;

public interface StartContract {

    /*
     * ContractはPresenterとActivity/Fragmentとの間のやり取りを定義したもので、
     * 以下のようにインターフェースとして定義します。
     * 参考:https://qiita.com/ehuthon-kd/items/a495bc8a1e182e079bb0
     */
    interface View  extends BaseView<Presenter> {
        /*
         *StayrtContract.Viewが、Activity/FragmentなどのViewモジュールが実装すべきインタフェースで、
         * Presenterから呼ばれることが想定される関数が定義されています。
         * それらの関数の役割は主にUIの操作です。
         * */

        // todo taguchi
        // 現時点でスタート画面にどういったインターフェースが必要か和kらないので実装しながら作っていく

        void showRegisteredMedium();

        void showMediumSelect();

//        void setLoadingIndicator(boolean active);
//
//        boolean isActive();

        void showSuccessfullySavedMessage();


    }


    interface Presenter extends BasePresenter {
        /*
         * StartContract.Presenterは、Presenterモジュールが実装すべきインタフェースで、
         * Activity/Fragmentから呼ばれることが想定される関数が定義されています。
         * それらの関数はこれまでActivity/Fragmentがやっていたような処理のうち、
         * UIの操作以外、すなわちデータの取得処理を呼び出す、データの保存処理を呼び出すなどの処理です。
         * */
        void start();

        void result(int requestCode, int resultCode);

        void openRegisteredMedium();

        void openMediumSelect();




    }
}
