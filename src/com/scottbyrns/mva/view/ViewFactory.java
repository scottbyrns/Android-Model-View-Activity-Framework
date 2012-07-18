package com.scottbyrns.mva.view;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Produce view layers.
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
 * Created 7/13/12 7:58 PM
 */
public class ViewFactory
{

    private static Map registeredViews = new HashMap<String, Class>();

    /**
     * Register a view layer.
     *
     * @param viewName The name of the view.
     * @param viewLayer The view layer class.
     */
   	public static void registerViewLayer (String viewName, Class viewLayer)
   	{
   		registeredViews.put(viewName, viewLayer);
   	}

   	private static ViewLayer createView(String viewName)
   	{
   		Class productClass = (Class)registeredViews.get(viewName);
        try {
            Constructor viewConstructor = productClass.getConstructor();
            return (ViewLayer)viewConstructor.newInstance();
        }
        catch (Throwable e) {
            e.printStackTrace();
            return null;
        }


   	}

    /**
     * Produce a view layer of the specified name.
     *
     * @param viewName The name of the view layer to create.
     * @return The specified view layer.
     */
    public static ViewLayer produce(String viewName) {
        return createView(viewName);
    }
}