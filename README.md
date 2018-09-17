
#### 导入配置

app-> build.gradle

```java
android {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [appid: applicationId]
            }
        }

    }
}

dependencies {
	...
    implementation 'com.lu.lib:lplatform:1.0.2'
    annotationProcessor 'com.lu.lib:lplatform-processor:1.0'
}
```

AndroidManifest.xml

```xml
  <!--微信支付回调-->
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop" />
        <activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:launchMode="singleTop" />

```

> 将上述配置导入项目中即可开始使用，在项目源码的demo里也有详细的使用方式


#### 配置各个平台参数

```java
//下面的初始化配置为各自平台的信息
//第一个参数都是appid,第二个参数是app_secret,第三个参数是新浪需求的redirect_url
//如果不需要调用某个平台的sdk，不配置参数即可
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //下面的初始化配置为各自平台的信息
        PlatformConfigurator.getInstance()
                .isDebug(BuildConfig.DEBUG)
                .withContext(this)
                .setSINAConfig("", "", "")
                .setQQConfig("", "")
                .setMiniProgramConfig("")
                .setWXConfig("", "")
                .initialize();
    }
}
```

#### [使用参照博客](https://www.jianshu.com/p/11a8f8a1c3de),也可观看demo
