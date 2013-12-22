package com.blueskyconnie.bluestonecrystal;

import java.io.File;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.showImageForEmptyUri(R.drawable.img_stub)
			.showStubImage(R.drawable.img_stub)
			.showImageOnFail(R.drawable.img_error)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.build();
		
		File cacheDir = new File(Environment.getExternalStorageDirectory(), "bluestone/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.threadPoolSize(1)
			.denyCacheImageMultipleSizesInMemory()
			.memoryCache(new WeakMemoryCache())
			.discCache(new UnlimitedDiscCache(cacheDir))   // write to bluestone/Cache in SD card
			.defaultDisplayImageOptions(options)
			.build();
//			.writeDebugLogs()
		
		ImageLoader.getInstance().init(config);
		Log.i("MyApplication", "Initialize Universal Image Loader....");
	}
	
}
