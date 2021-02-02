package com.example.android.hrs.blueprints.jumpmeasurementapp.data.source;

import androidx.annotation.NonNull;

import com.example.android.hrs.blueprints.jumpmeasurementapp.data.MediaModel;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.TemplateDb;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.DB_TemplateDao;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.MediumDao;
import com.example.android.hrs.blueprints.jumpmeasurementapp.data.source.local.MediumLocalDataSource;
import com.example.android.hrs.blueprints.jumpmeasurementapp.util.AppExecutors;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class TemplateRepository implements TemplateDataSorce{

    private static TemplateRepository INSTANCE;

    private DB_TemplateDao mDB_TemplateDao;

    private AppExecutors mAppExecutors;

    /**
     * よくわからん
     * */
    Map<String, TemplateDb> mCachedTemplate;


    /*
    *  TOdo よくわからん
     * キャッシュを無効としtrマークし、次ぎぬデータが要求された時に更新します。
     * この変数にはパッケージのローカルの可視性があるため、テストからアクセスできます
     * */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    public TemplateRepository(@NonNull AppExecutors appExecutors,
                              @NonNull DB_TemplateDao db_templateDao) {
        mAppExecutors = appExecutors;
        mDB_TemplateDao = db_templateDao;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     * このクラスの単一インスタンスを返し、必要に応じて作成します。
     * 要するにシングルトン
     * @return the {@link MediumRepository} instance
     */
    public static TemplateRepository getInstance(@NonNull AppExecutors appExecutors,
                                               @NonNull DB_TemplateDao db_templateDao) {
        if (INSTANCE == null) {

            synchronized (TemplateRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemplateRepository(appExecutors, db_templateDao);
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Note: {@link MediumDataSource.LoadMediumCallBack#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTemplates(@NonNull final TemplateRepository.LoadTemplateCallBack callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<TemplateDb> templates = mDB_TemplateDao.getAllDBTemplate();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (templates.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onTemplateLoaded(templates);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link TemplateDataSorce.GetTemplateCallBack#onDataNotAvailable()} is fired if the
     * found.
     */
    @Override
    public void getTemplate(@NonNull final String templateId, @NonNull final TemplateDataSorce.GetTemplateCallBack callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final TemplateDb template = mDB_TemplateDao.getTemplateById(templateId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (template != null) {
                            callback.onTemplateLoaded(template);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }


    @Override
    public void saveTemplate(@NonNull final TemplateDb template) {
        checkNotNull(template);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mDB_TemplateDao.insertTemplate(template);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshTemplates() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTemplate() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
//                mDB_TemplateDao.deletetemplate();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteTemplate(@NonNull final String templateId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mDB_TemplateDao.deleteTemplateId(templateId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
