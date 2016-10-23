package com.wengelef;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Flo on 23.10.2016.
 */
public class RxFileUtils {

    @NonNull
    public static Observable<String> readFileFromAsset(final Context context, final String filename) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
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

    public static Observable<String> readInternal(final Context context, final String filename) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
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

    public static Observable<Void> writeInternal(final Context context, final String filename, final String content) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                FileOutputStream outputStream;
                try {
                    outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable writeExternal(final Context context, final String content) {
        return Observable.empty();
    }
}
