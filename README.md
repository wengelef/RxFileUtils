# RxFileUtils (Android)

Collection of Utility for the Android File System

## Usage

##### Read File from `assets/` Folder:

```java
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

```

##### Write to Internal storage:

```java
RxFileUtils.writeInternal(this, "hello_test.txt", "test test test")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        Timber.i("Write internal complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "onError");
                    }

                    @Override
                    public void onNext(Object o) {
                        Timber.i("Write internal complete");
                    }
                });
```

##### Read from Internal storage:

```java
RxFileUtils.readInternal(this, "hello_test.txt")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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
                        Timber.i("Internal File onNext(%s)", s);
                    }
                });
```

## Download

##### Gradle:

```java
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```

```java
dependencies {
	        compile 'com.github.wengelef:RxFileUtils:1.1.1'
}
```

##### Maven:

```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

```
    <dependency>
        <groupId>com.github.wengelef</groupId>
        <artifactId>RxFileUtils</artifactId>
        <version>1.1.1</version>
    </dependency>
```

## License

Copyright 2016 Florian Wengelewski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
