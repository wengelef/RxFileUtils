package com.wengelef.rxfileutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.wengelef.RxFileUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.textview);
        textView.setText("Processing file...");

        textView.setMovementMethod(new ScrollingMovementMethod());

        RxFileUtils.writeInternal(this, "hello_test.txt", "test test test")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap(new Function<Object, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Object o) throws Exception {
                        Timber.i("internal File written");
                        return RxFileUtils.readInternal(MainActivity.this, "hello_test.txt")
                                .subscribeOn(Schedulers.io());
                    }
                })
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Timber.i("internal onNext(%s)", s);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.e(throwable, "onError");
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.i("onCompleted");
                            }
                        }
                );

        RxFileUtils.readFileFromAsset(this, "hello_world.txt")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                textView.setText(s);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Timber.e(throwable, "onError");
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.i("onCompleted");
                            }
                        }
                );
    }
}
