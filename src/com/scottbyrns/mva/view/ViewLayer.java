package com.scottbyrns.mva.view;

import android.app.Activity;

/**
 * A wrapper around a set of view elements attached to an activity.
 *
 * This solves the problem of direct coupling to your views in an activity.
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
 * Created 7/13/12 7:01 PM
 */
public abstract class ViewLayer
{
    private static Activity context;

    public static Activity getContext()
    {
        return context;
    }

    public static void setContext(Activity context)
    {
        ViewLayer.context = context;
    }

    public abstract int getViewId();
    public abstract void viewDidRender();
}
