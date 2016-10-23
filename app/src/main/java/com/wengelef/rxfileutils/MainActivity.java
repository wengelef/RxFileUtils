package com.wengelef.rxfileutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.wengelef.RxFileUtils;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView )findViewById(R.id.textview);
        textView.setText("Processing file...");

        textView.setMovementMethod(new ScrollingMovementMethod());

        RxFileUtils.writeInternal(this, "hello_test.txt", "test test test")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void aVoid) {
                        Timber.i("internal File written");
                        return RxFileUtils.readInternal(MainActivity.this, "hello_test.txt")
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.i("internal onNext(%s)", s);
                    }
                });

        RxFileUtils.readFileFromAsset(this, "hello_world.txt")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError()");
                    }

                    @Override
                    public void onNext(String s) {
                        //Timber.i("onNext(%s)", s.trim());
                        textView.setText(s);
                    }
                });
    }
}
