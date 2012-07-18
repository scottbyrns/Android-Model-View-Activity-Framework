package com.scottbyrns.mva.data;

import android.app.Activity;
import android.app.ProgressDialog;
import com.scottbyrns.mva.ModelViewActivity;

/**
 * A data source is the source of data for a given piece of information.
 * The information is represented as a generic entity in the data source.
 * <p/>
 * When data needs to be loaded from a data source the abstract method
 * {@link com.scottbyrns.mva.data.BaseDataSource#loadEntity()} is called.
 * <p/>
 * It is the responsibility of the implementation to determine how the data is loaded.
 * <p/>
 * When data is loaded it should populate the generic Entity of the data source.
 * <p/>
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
 * Created 7/13/12 3:53 PM
 */
public abstract class BaseDataSource<Entity> implements DataSource<Entity>
{

    private Activity context;
    public static ProgressDialog dialog;

    /**
     * Create a new data source.
     *
     * @param context The activity context is used for interacting with the Android framework.
     *
     * @todo An abstraction would be preferred but at this time the scope of the contexts use is not known so I have coupled to it for speed. Bad practice but ya know.
     */
    public BaseDataSource(Activity context)
    {
        this.context = context;
    }

    /**
     * Get the activity context.
     *
     * @return The activity context shared among data sources.
     */
    public Activity getContext()
    {
        return context;
    }

    /**
     * Set the activity context.
     *
     * @param context An activity context that is active.
     */
    public void setContext(Activity context)
    {
        this.context = context;
    }

    private DataCallback dataLoadFailureCallback;
    private DataCallback dataLoadSuccessCallback;
    private DataCallback dataSaveFailureCallback;
    private DataCallback dataSaveSuccessCallback;

    public DataCallback getDataLoadFailureCallback()
    {
        return dataLoadFailureCallback;
    }

    public void setDataLoadFailureCallback(DataCallback dataLoadFailureCallback)
    {
        this.dataLoadFailureCallback = dataLoadFailureCallback;
    }

    public DataCallback getDataLoadSuccessCallback()
    {
        return dataLoadSuccessCallback;
    }

    public void setDataLoadSuccessCallback(DataCallback dataLoadSuccessCallback)
    {
        this.dataLoadSuccessCallback = dataLoadSuccessCallback;
    }

    public DataCallback getDataSaveFailureCallback()
    {
        return dataSaveFailureCallback;
    }

    public void setDataSaveFailureCallback(DataCallback dataSaveFailureCallback)
    {
        this.dataSaveFailureCallback = dataSaveFailureCallback;
    }

    public DataCallback getDataSaveSuccessCallback()
    {
        return dataSaveSuccessCallback;
    }

    public void setDataSaveSuccessCallback(DataCallback dataSaveSuccessCallback)
    {
        this.dataSaveSuccessCallback = dataSaveSuccessCallback;
    }

    private Entity entity;

    /**
     * Get the entity that the data source will populate.
     *
     * @return Populated entity.
     */
    public Entity getEntity()
    {
        return entity;
    }

    /**
     * Set the data sources entity.
     *
     * @param entity  The data sources entity.
     */
    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    /**
     * Load the data source in a new thread.
     *
     * When data sources are loading a loading indicator will take focus in the view. This indicator will go away on load success and failure.
     */
    public void load()
    {
        ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.LOADING);
        new Thread(new Runnable()
        {
            public void run()
            {
                if (!doLoadEntity())
                {
                    ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.DONE_LOADING);
                    try
                    {
                        getDataLoadFailureCallback().execute();
                    }
                    catch (NullPointerException e)
                    {
                        // NOP No callback set.
                    }
                }
                else
                {
                    try {
                        ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.DONE_LOADING);
                        getDataLoadSuccessCallback().execute();
                    }
                    catch (NullPointerException e) {
                        // NOP No callback set.
                    }
                }
            }
        }).start();

    }

    /**
     * Save the data source in a new thread.
     *
     * When data sources are saving a save indicator will take focus in the view. This indicator will go away on save success and failure.
     */
    public void save()
    {


        ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.LOADING);

        new Thread(new Runnable()
        {
            public void run()
            {
                if (!doSaveEntity())
                {
                    try
                    {
                        ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.DONE_LOADING);
                        getDataSaveFailureCallback().execute();
                    }
                    catch (NullPointerException e)
                    {
                        // NOP No callback set.
                    }
                }
                else
                {
                    try {
                        ((ModelViewActivity)getContext()).handler.sendEmptyMessage(ModelViewActivity.DONE_LOADING);
                        getDataSaveSuccessCallback().execute();
                    }
                    catch (NullPointerException e) {
                        // NOP No callback set.
                    }
                }
            }
        }).start();

    }

    /**
     * Perform a load of a data sources entity.
     */
    protected boolean doLoadEntity()
    {
        try
        {
            beforeEntityLoad();
            loadEntity();
            afterEntityLoad();
            return true;
        }
        catch (Throwable e)
        {
            Throwable asdf = e;
            return false;
        }
    }

    /**
     * Called before {@link com.scottbyrns.mva.data.BaseDataSource#loadEntity()} ()}
     */
    protected void beforeEntityLoad()
    {
    }

    /**
     * Called after {@link com.scottbyrns.mva.data.BaseDataSource#loadEntity()} ()}
     */
    protected void afterEntityLoad()
    {
    }

    /**
     * Perform a load of the data sources data.
     */
    protected abstract void loadEntity() throws Throwable;

    /**
     * Perform a save of the data sources entity.
     */
    private boolean doSaveEntity()
    {
        try
        {
            beforeEntityLoad();
            saveEntity();
            afterEntityLoad();
            return true;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Called before {@link com.scottbyrns.mva.data.BaseDataSource#saveEntity()}
     */
    protected void beforeEntitySave()
    {
    }

    /**
     * Called after {@link com.scottbyrns.mva.data.BaseDataSource#saveEntity()}
     */
    protected void afterEntitySave()
    {
    }

    /**
     * Perform a save of the data sources data.
     */
    protected abstract void saveEntity() throws Throwable;
}
