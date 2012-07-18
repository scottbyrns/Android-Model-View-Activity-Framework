package com.scottbyrns.mva;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.scottbyrns.mva.data.DataCallback;
import com.scottbyrns.mva.data.DataSource;
import com.scottbyrns.mva.data.DataSourceFactory;
import com.scottbyrns.mva.reflection.ModelViewConfiguration;
import com.scottbyrns.mva.view.ViewFactory;
import com.scottbyrns.mva.view.ViewLayer;


/**
 * Create a model & view delegation pattern for Android Activities.
 * <p/>
 * Copyright (C) 2012 by Scott Byrns
 * http://github.com/scottbyrns
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * Created 7/13/12 6:49 PM
 */
public abstract class ModelViewActivity extends Activity
{

    private ModelViewConfiguration modelViewConfiguration;
    private DataSource model;
    private ViewLayer viewLayer;

    public static int LOADING = 3141;
    public static int DONE_LOADING = 3142;
    public static int SHOW_ALERT = 3143;
    public static int DID_SAVE = 3144;
    public static int DID_FAIL_SAVE = 3145;
    public static int DID_LOAD = 3146;
    public static int DID_FAIL_LOAD = 3147;

    /**
     * Handler to allow non UI threads to operate the activity.
     */
    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {

            if (msg.what == DID_SAVE) {
                dataDidSave();
            }
            if (msg.what == DID_FAIL_SAVE) {
                dataDidFailToSave();
            }

            if (msg.what == DID_LOAD) {
                dataDidLoad();
            }

            if (msg.what == DID_FAIL_LOAD) {
                dataDidFailToLoad();
            }

            if (msg.what == LOADING) {
                if (dialog.getContext().equals(ModelViewActivity.this)) {
                    dialog.show();
                }
                else {
                    dialog.dismiss();
                    dialog = ProgressDialog.show(ModelViewActivity.this,
                                                 null,
                                                 "Please wait...",
                                                 true,
                                                 false);
                }

            }
            if (msg.what == DONE_LOADING) {
                dialog.dismiss();
            }
            if (msg.what == SHOW_ALERT) {

                String title = msg.getData().getString("title");
                String text = msg.getData().getString("text");

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ModelViewActivity.this);
                alertBuilder.setTitle(title);
                alertBuilder.setMessage(text);
                alertBuilder.create().show();
            }
            super.handleMessage(msg);    //To change body of overridden methods use File | Settings | File Templates.
        }
    };

    /**
     * Get the data source that was loaded when the ModelViewActivity was created.
     *
     * @return The data source that was loaded when the ModelViewActivity was created.
     */
    public DataSource getDataSource()
    {
        return model;
    }

    /**
     * Set the data source of the ModelViewActivity.
     *
     * @param model The data source to use in this ModelViewActivity.
     */
    public void setDataSource(DataSource model)
    {
        this.model = model;
    }

    /**
     * Get the view layer of the ModelViewActivity.
     *
     * @return The view layer of the ModelViewActivity.
     */
    public ViewLayer getViewLayer()
    {
        return viewLayer;
    }

    /**
     * Set the view layer of the ModelViewActivity.
     *
     * @param viewLayer The view layer of the ModelViewActivity.
     */
    public void setViewLayer(ViewLayer viewLayer)
    {
        this.viewLayer = viewLayer;
    }

    /**
     * Get the configuration of this ModelViewActivity.
     *
     * @return The configuration of this ModelViewActivity.
     */
    public ModelViewConfiguration getModelViewConfiguration()
    {
        return modelViewConfiguration;
    }

    /**
     * Set the configuration of this ModelViewActivity.
     *
     * @param modelViewConfiguration The configuration of this ModelViewActivity.
     */
    public void setModelViewConfiguration(ModelViewConfiguration modelViewConfiguration)
    {
        this.modelViewConfiguration = modelViewConfiguration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ViewLayer.setContext(this);

        setModelViewConfiguration(this.getClass().getAnnotation(ModelViewConfiguration.class));


        bootstrap();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        dialog.dismiss();
    }

    /**
     * The data for the ModelViewActivity did load.
     */
    public abstract void dataDidLoad();

    /**
     * The data for the ModelViewActivity did fail to load.
     */
    public abstract void dataDidFailToLoad();

    /**
     * The data for the ModelViewActivity did save.
     */
    public abstract void dataDidSave();

    /**
     * The data for the ModelViewActivity did fail to save.
     */
    public abstract void dataDidFailToSave();

    /**
     * The view for the ModelViewActivity did load.
     */
    public abstract void viewDidLoad();

    private ProgressDialog dialog;

    /**
     * Bootstrap our delegation pattern into the android lifecycle.
     */
    private void bootstrap()
    {

        dialog = ProgressDialog.show(this,
                                                                      null,
                                                                      "Please wait...",
                                                                      true,
                                                                      false);
        dialog.hide();

        setViewLayer(ViewFactory.produce(getModelViewConfiguration().view()));


        setDataSource(DataSourceFactory.produce(getModelViewConfiguration().dataSource(),
                                                ModelViewActivity.this));


        setContentView(getViewLayer().getViewId());
        getViewLayer().viewDidRender();
        viewDidLoad();

        getDataSource().setDataLoadFailureCallback(new DataCallback()
        {
            public void execute()
            {
                handler.sendEmptyMessage(DID_FAIL_LOAD);
            }
        });
        getDataSource().setDataLoadSuccessCallback(new DataCallback()
        {
            public void execute()
            {
                handler.sendEmptyMessage(DID_LOAD);
            }
        });

        getDataSource().load();

    }

    /**
     * Save the data source.
     */
    public void save()
    {
        getDataSource().setDataSaveFailureCallback(new DataCallback()
        {
            public void execute()
            {
                handler.sendEmptyMessage(DID_FAIL_SAVE);
            }
        });
        getDataSource().setDataSaveSuccessCallback(new DataCallback()
        {
            public void execute()
            {
                handler.sendEmptyMessage(DID_SAVE);
            }
        });
        getDataSource().save();
    }

    /**
     * Show an alert message.
     *
     * @param context Activity context to alert.
     * @param title The title of the alert.
     * @param text The text body of the alert.
     */
    protected void showAlert(Context context, String title, String text)
    {
        Message message = new Message();
        message.what = SHOW_ALERT;
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("text", text);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
