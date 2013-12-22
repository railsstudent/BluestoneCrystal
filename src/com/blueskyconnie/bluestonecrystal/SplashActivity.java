package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.data.BatchData;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.data.Shop;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class SplashActivity extends Activity {

//	private static final int SPLASH_TIMEOUT = 1000;
	
	private WeakReference<LoadDataTask> asyncTaskWeakRef;
	private String cmsUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create an async task to load data
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		}, SPLASH_TIMEOUT);

		cmsUrl = getString(R.string.cms_url);
		ConnectionDetector detector = new ConnectionDetector(this);
		if (detector.isConnectingToInternet()) {
  		    startNewAsyncTask();
		} else {
			setContentView(R.layout.activity_splash);
			showNoInternetDialogStartActivity(this);
		}
	}
	
	private void showNoInternetDialogStartActivity(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.no_internet_error));
		builder.setNeutralButton(context.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					BatchData data = new BatchData();
					Calendar cal = Calendar.getInstance();
					data.setLstNews(new ArrayList<News>());
					data.setLstProduct(new ArrayList<Product>());
					data.setLastProductUpdateTime(cal.getTimeInMillis());
					data.setLastNewsUpdateTime(cal.getTimeInMillis());
					data.setShop(new Shop());
		
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					intent.putExtra(MainActivity.BATCH_DATA, data);
					startActivity(intent);
					finish();
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	
	private void startNewAsyncTask() {
		LoadDataTask asyncTask = new LoadDataTask(this);
	    this.asyncTaskWeakRef = new WeakReference<LoadDataTask >(asyncTask);
	    String[] urls = { cmsUrl + "products_android.php?id=" + MainActivity.SHOP_ID, 
	    		cmsUrl + "news_android.php?id=" + MainActivity.SHOP_ID,
	    		cmsUrl + "shopInfo_android.php?id=" + MainActivity.SHOP_ID };
	    asyncTaskWeakRef.get().execute(urls);
	}

	private class LoadDataTask extends AsyncTask<String, Void, BatchData> {

		private WeakReference<SplashActivity> activityWeakRef;
		private Exception exception;
		
		private LoadDataTask(SplashActivity activity) {
			this.activityWeakRef = new WeakReference<SplashActivity>(activity);
			exception = null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			View splash_view = View.inflate(activityWeakRef.get(), R.layout.activity_splash, null);
			setContentView(splash_view);
		}

		@Override
		protected BatchData doInBackground(String... params) {
			String productUrl = params[0];
			String newsUrl = params[1];
			String shopUrl = params[2];

			Calendar cal = Calendar.getInstance();
			BatchData data = new BatchData();
			try {
				List<Product> lstProduct = HttpClientHelper.retrieveProducts(activityWeakRef.get(), 
						productUrl, cmsUrl);
				data.setLstProduct(lstProduct);
				data.setLastProductUpdateTime(cal.getTimeInMillis());
				
				List<News> lstNews = HttpClientHelper.retrieveNews(activityWeakRef.get(), newsUrl);
				data.setLstNews(lstNews);
				data.setLastNewsUpdateTime(cal.getTimeInMillis());
				
				Shop shop = HttpClientHelper.retrieveShop(activityWeakRef.get(), shopUrl);
				data.setShop(shop);
				
				return data;
			} catch (BusinessException ex) {
				ex.printStackTrace();
				exception = ex;
			}
			data.setLstNews(new ArrayList<News>());
			data.setLstProduct(new ArrayList<Product>());
			data.setLastProductUpdateTime(cal.getTimeInMillis());
			data.setLastNewsUpdateTime(cal.getTimeInMillis());
			data.setShop(new Shop());
			return data;
		}

		@Override
		protected void onPostExecute(BatchData result) {
			super.onPostExecute(result);
			if (asyncTaskWeakRef.get() != null) {
				if (exception != null) {
					Toast.makeText(activityWeakRef.get(), exception.getMessage(), Toast.LENGTH_LONG)
						.show();
				}
				Intent intent = new Intent(activityWeakRef.get(), MainActivity.class);
				intent.putExtra(MainActivity.BATCH_DATA, result);
				activityWeakRef.get().startActivity(intent);
				activityWeakRef.get().finish();
			}
		}
	}
	
}
