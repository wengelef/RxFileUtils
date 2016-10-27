package com.wengelef.rxfileutils;

import android.content.Context;
import android.os.Build;

import com.wengelef.RxFileUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.Notification;
import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by fwengelewski on 10/26/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN)
public class FileUtilsTest {

    private String textFileContent = "I'm a TextFile";
    private String testFileName = "test.txt";

    private Context mContext;


    @Before public void setUp() {
        mContext = RuntimeEnvironment.application;
    }

    @Test public void test_validWriteInternal() {
        Observable<Void> writeInternalObservable = RxFileUtils.writeInternal(mContext, testFileName, textFileContent);
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();

        writeInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<Void> onNextEvents = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, onNextEvents.size());

        List<Notification<Void>> onCompletedEvents = testSubscriber.getOnCompletedEvents();
        Assert.assertEquals(1, onCompletedEvents.size());
    }

    @Test public void test_invalidWriteInternal() {
        Observable<Void> writeInternalObservable = RxFileUtils.writeInternal(null, testFileName, textFileContent);
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();

        writeInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertError(IllegalArgumentException.class);

        testSubscriber = new TestSubscriber<>();
        writeInternalObservable = RxFileUtils.writeInternal(mContext, null, null);
        writeInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertError(IllegalArgumentException.class);

        testSubscriber = new TestSubscriber<>();
        writeInternalObservable = RxFileUtils.writeInternal(mContext, testFileName, null);
        writeInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertError(IllegalArgumentException.class);
    }

    @Test public void test_validReadInternal() {
        Observable<Void> writeInternalObservable = RxFileUtils.writeInternal(mContext, testFileName, textFileContent);
        TestSubscriber<Void> writeSubscriber = new TestSubscriber<>();

        writeInternalObservable.subscribe(writeSubscriber);
        writeSubscriber.assertNoErrors();
        writeSubscriber.assertCompleted();

        Observable<String> readInternalObservable = RxFileUtils.readInternal(mContext, testFileName);
        TestSubscriber<String> readSubscriber = new TestSubscriber<>();
        readInternalObservable.subscribe(readSubscriber);
        readSubscriber.assertNoErrors();
        readSubscriber.assertCompleted();

        List<String> onNextEvents = readSubscriber.getOnNextEvents();
        Assert.assertEquals(1, onNextEvents.size());

        Assert.assertEquals(textFileContent, onNextEvents.get(0));
    }

    @Test public void test_invalidReadInternal() {
        Observable<String> readInternalObservable = RxFileUtils.readInternal(null, testFileName);
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        readInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertError(IllegalArgumentException.class);
        testSubscriber.assertNotCompleted();

        testSubscriber = new TestSubscriber<>();
        readInternalObservable = RxFileUtils.readInternal(mContext, null);
        readInternalObservable.subscribe(testSubscriber);
        testSubscriber.assertError(IllegalArgumentException.class);
        testSubscriber.assertNotCompleted();
    }

}
