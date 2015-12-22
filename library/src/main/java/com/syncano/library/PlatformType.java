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

class PlatformType {
    private static final PlatformType PLATFORM_TYPE = getPlatformType();

    static PlatformType get() {
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

    Executor getDefaultCallbackExecutor() {
        return null;
    }

    static class AndroidPlatform extends PlatformType {
        @Override
        Executor getDefaultCallbackExecutor() {
            return new MainThreadExecutor();
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
