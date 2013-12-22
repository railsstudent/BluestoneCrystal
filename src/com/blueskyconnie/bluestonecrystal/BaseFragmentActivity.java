package com.blueskyconnie.bluestonecrystal;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class BaseFragmentActivity extends FragmentActivity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.item_clear_memory_cache:
				imageLoader.clearMemoryCache();
				Log.i("BaseActivity", "Clear memory cache of image loader.");
				return true;
			case R.id.item_clear_disc_cache:
				imageLoader.clearDiscCache();
				Log.i("BaseActivity", "Clear disc cache of image loader.");
				return true;
			default:
				return false;
		}
	}
}
