/*
 * Copyright (c) wengelef 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wengelef;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rx.Observable;
import rx.Subscriber;

/**
 * Collection of Utility for the Android File System
 */
public class RxFileUtils {

    /**
     * Read file from your main/assets Directory
     *
     * @param context  Context, not null
     * @param filename  Filename of the File in assets Directory
     * @return <code>Observable<String></code> that gets the File content emitted as <code>String</code> in <code>onNext()</code>.
     *         <code>onError()</code> is emitted if the given Context is null or File operation meets an <code>IOException</code>
     */
    @NonNull
    public static Observable<String> readFileFromAsset(@Nullable final Context context, final String filename) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (context == null) {
                    subscriber.onError(new IllegalArgumentException("Context must not be null"));
                    return;
                }
                if (filename == null) {
                    subscriber.onError(new IllegalArgumentException("Filename must not be null"));
                    return;
                }

                InputStream input = null;
                try {
                    input = context.getAssets().open(filename);

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder content = new StringBuilder(input.available());
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                        content.append(System.getProperty("line.separator"));
                    }
                    subscriber.onNext(content.toString());
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Reads a File from internal Storage
     *
     * @param context  Context, not null
     * @param filename  Filename of the File to read from Internal Storage
     * @return <code>Observable<String></code> that gets the File content emitted as <code>String</code> in <code>onNext()</code>.
     *         <code>onError()</code> is emitted if the given Context is null or File operation meets an <code>IOException</code>
     */
    @NonNull
    public static Observable<String> readInternal(@Nullable final Context context, final String filename) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (context == null) {
                    subscriber.onError(new IllegalArgumentException("Context must not be null"));
                    return;
                }
                if (filename == null) {
                    subscriber.onError(new IllegalArgumentException("Filename must not be null"));
                    return;
                }

                FileInputStream fis = null;
                try {
                    fis = context.openFileInput(filename);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }
                    subscriber.onNext(content.toString());
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    /**
     * Write Content to a File in internal Storage
     *
     * @param context  Context, not null
     * @param filename  Filename of the File that will be saved
     * @param content  Content to write into the File
     * @return <code>Observable<Void></code> that emits <code>onNext()</code> and <code>onCompleted()</code> when the File is saved.
     *         <code>onError()</code> is emitted if the given Context is null or File operation meets an <code>IOException</code>
     */
    @NonNull
    public static Observable<Void> writeInternal(@Nullable final Context context, final String filename, final String content) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (context == null) {
                    subscriber.onError(new IllegalArgumentException("Context must not be null"));
                    return;
                }
                if (filename == null) {
                    subscriber.onError(new IllegalArgumentException("Filename must not be null"));
                    return;
                }
                if (content == null) {
                    subscriber.onError(new IllegalArgumentException("Content must not be null"));
                    return;
                }

                FileOutputStream outputStream = null;
                try {
                    outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
