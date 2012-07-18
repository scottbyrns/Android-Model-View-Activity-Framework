package com.scottbyrns.mva.data;

/**
 * A DataSource is the source of data for {@link com.somegood.mva.ModelViewActivity}'s.
 *
 * A data source wraps a generic entity that is loaded and saved by the data source.
 *
 * A data source is managed in the {@link com.somegood.mva.ModelViewActivity} lifecycle.
 *
 *
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
 * Created 7/13/12 6:58 PM
 */
public interface DataSource<Entity>
{
    /**
     * Save changes to the data sources entity.
     */
    public void save();

    /**
     * Load the data sources entity.
     */
    public void load();

    /**
     * Get the data sources entity.
     *
     * @return The data sources entity.
     */
    public Entity getEntity();

    /**
     * Set the data sources entity.
     *
     * @param entity  The data sources entity.
     */
    public void setEntity(Entity entity);

    /**
     * Set the data load failure callback.
     *
     * This method is called when data fails to load.
     *
     * @param dataLoadFailureCallback The callback to execute on a data load failure.
     */
    public void setDataLoadFailureCallback(DataCallback dataLoadFailureCallback);

    /**
     * Set the data load success callback.
     *
     * This method is called when the data source has successfully loaded.
     *
     * @param dataLoadSuccessCallback The callback to execute on a data load success.
     */
    public void setDataLoadSuccessCallback(DataCallback dataLoadSuccessCallback);

    /**
     * Set the data save failure callback.
     *
     * This method is called when the data source fails to save your changes to the data sources entity.
     *
     * @param dataSaveFailureCallback The callback to execute on a data save failure.
     */
    public void setDataSaveFailureCallback(DataCallback dataSaveFailureCallback);

    /**
     * Set the data save success callback.
     *
     * This method is called when the data source has successfully saved your changes to the data sources entity.
     *
     * @param dataSaveSuccessCallback The callback to execute when data save has succeeded.
     */
    public void setDataSaveSuccessCallback(DataCallback dataSaveSuccessCallback);
}
