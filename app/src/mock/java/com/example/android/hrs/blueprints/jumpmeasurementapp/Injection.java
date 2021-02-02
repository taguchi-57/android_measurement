package com.example.android.hrs.blueprints.jumpmeasurementapp;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.FakeMediumRemoteDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.LocalMediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.MediumRepository;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.LocalMediumLocalDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.MediumDatabase;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.MediumLocalDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static MediumRepository provideMediumRepository(@NonNull Context context) {
        checkNotNull(context);
        MediumDatabase database = MediumDatabase.getInstance(context);
        return MediumRepository.getInstance(FakeMediumRemoteDataSource.getInstance(),
                MediumLocalDataSource.getInstance(new AppExecutors(),
                        database.mediumDao()));
    }

    public static LocalMediumRepository provideLocalMediumRepository(@NonNull Context context) {
        checkNotNull(context);
        MediumDatabase database = MediumDatabase.getInstance(context);
        //　ローカルのファイルを扱っているのでデータベースのクラスはなしで設計していきます。
        return LocalMediumRepository.getInstance(LocalMediumLocalDataSource.getInstance(new AppExecutors(),database.mediumDao()));
    }
}

