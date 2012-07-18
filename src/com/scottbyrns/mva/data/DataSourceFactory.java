package com.scottbyrns.mva.data;

import android.app.Activity;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
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
 * Created 7/13/12 6:54 PM
 */
public class DataSourceFactory
{


    private static Map registeredDataSources = new HashMap<String, Class>();

    /**
     * Register a view layer.
     *
     * @param viewName The name of the view.
     * @param viewLayer The view layer class.
     */
   	public static void registerDataSource(String viewName, Class viewLayer)
   	{
   		registeredDataSources.put(viewName,
                               viewLayer);
   	}

   	private static DataSource createDataSource(String viewName, Activity context)
   	{
   		Class productClass = (Class) registeredDataSources.get(viewName);
        try {
            Constructor viewConstructor = productClass.getConstructor(new Class[] {Activity.class});
            return (DataSource)viewConstructor.newInstance(context);
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
   	}

    /**
     * Produce a data source of the specified name.
     *
     * @param dataSource The name of the data source to create.
     * @return The specified data source.
     */
    public static DataSource produce(String dataSource, Activity context) {
        return createDataSource(dataSource, context);
    }


}
