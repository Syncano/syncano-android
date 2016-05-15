/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.syncano.library;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

public class PlatformType {
    private static final PlatformType PLATFORM_TYPE = getPlatformType();

    public static PlatformType get() {
        return PLATFORM_TYPE;
    }

    private static PlatformType getPlatformType() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new AndroidPlatform();
            }
        } catch (ClassNotFoundException ignored) {
        }
        return new PlatformType();
    }

    public void runOnCallbackThread(Runnable runnable) {
        if (getDefaultCallbackExecutor() != null) {
            getDefaultCallbackExecutor().execute(runnable);
        } else {
            runnable.run();
        }
    }

    public static boolean isAndroid() {
        return PlatformType.get() instanceof PlatformType.AndroidPlatform;
    }

    public Executor getDefaultCallbackExecutor() {
        return null;
    }

    public static class AndroidPlatform extends PlatformType {
        private MainThreadExecutor mainThreadExecutor = new MainThreadExecutor();

        @Override
        public Executor getDefaultCallbackExecutor() {
            return mainThreadExecutor;
        }

        static class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable r) {
                handler.post(r);
            }
        }
    }
}
