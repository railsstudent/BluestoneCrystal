package com.blueskyconnie.bluestonecrystal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;

import com.blueskyconnie.bluestonecrystal.adapter.TabspagerAdapter;
import com.blueskyconnie.bluestonecrystal.data.BatchData;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.data.Shop;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;

public class MainActivity extends BaseFragmentActivity implements
	TabListener {
	
	public static final String BATCH_DATA = "batchData";
	public static final int SHOP_ID = 1;
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss aa", Locale.US);
	public static final SimpleDateFormat sdf_ymd_hms = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	/**
	 * 
	 */
	private static final int[] TAB_NAMES = { R.string.tab_news, R.string.tab_product, 
		 R.string.tab_facebook, R.string.tab_map, R.string.tab_contact };

	private static final String[] TAB_TAGS = { "news_tag", "product_tag", "facebook_tag", 
			"map_tag", "contact_tag" };
	
	private static final int[] TAB_ICONS = { R.drawable.ic_news, R.drawable.ic_product  
		, R.drawable.ic_facebook, R.drawable.ic_location, R.drawable.ic_contact };

	private static final String CURRENT_TAG_KEY = "current_tag";

	//private ActionBar actionBar;
	private ViewPager viewPager;
	private TabspagerAdapter pageAdapter;
	private List<Fragment> lstFragment = new ArrayList<Fragment>();
	private int currentPosition;
	
	// data models
	private List<Product> lstProducts;
	private List<News> lstNews;
	private long lastProductUpdateTime;
	private long lastNewsUpdateTime;
	private Shop shop;

	@Override
	protected void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);
	//	actionBar = getActionBar();
		
		// initialize viewpager
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setId(R.id.pager);
		// initialize fragmentpageradapter
		lstFragment.add(new NewsFragment());
		lstFragment.add(new ProductFragment());
		lstFragment.add(new FacebookFragment());
		lstFragment.add(new StoreMapFragment());
		lstFragment.add(new ContactFragment());
		pageAdapter = new TabspagerAdapter(getSupportFragmentManager(), lstFragment);
		viewPager.setAdapter(pageAdapter);
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
				// http://stackoverflow.com/questions/10853611/viewpager-with-fragments-onpause-onresume
				FragmentManager fragment_manager = MainActivity.this.getSupportFragmentManager();
				Fragment new_fragment = MainActivity.this.getActiveFragment(fragment_manager, viewPager.getId(), position);
				Fragment old_fragment = MainActivity.this.getActiveFragment(fragment_manager, viewPager.getId(), currentPosition);
				if (old_fragment != null) {
					old_fragment.setUserVisibleHint(false);
					old_fragment.onPause();
				}
				if (new_fragment != null) {
					new_fragment.setUserVisibleHint(true);
					new_fragment.onResume();
				}
				currentPosition = position;
			}
		});
		
		// add tabs
		for (int i = 0; i < TAB_NAMES.length; i++) {
			int tab_id = TAB_NAMES[i];
			int icon_id = TAB_ICONS[i];
			String tab_tag = TAB_TAGS[i];
			Tab tab = actionBar.newTab()
					.setText(this.getString(tab_id))
					.setTag(tab_tag);
			if (icon_id >= 0) {
				tab.setIcon(TAB_ICONS[i]);
			}
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
		
		Intent intent = getIntent();
		if (intent != null) {
			BatchData data = (BatchData) intent.getParcelableExtra(BATCH_DATA);
			if (data != null) {
				this.setLstProducts(data.getLstProduct());
				this.setLstNews(data.getLstNews());
				this.setLastProductUpdateTime(data.getLastProductUpdateTime());
				this.setLastNewsUpdateTime(data.getLastNewsUpdateTime());
				this.setShop(data.getShop());
			}
		}
	}

	private Fragment getActiveFragment(FragmentManager fragmentManager, int viewPagerId, int position) {
		return fragmentManager.findFragmentByTag("android:switcher:" + viewPagerId + ":" + position);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
        // show selected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		currentPosition = tab.getPosition();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	} 

	@Override
	protected void onPause() {
		super.onPause();
		// save last visit tab in shared preference
		int current_tab = actionBar.getSelectedNavigationIndex();
		Editor editor = getPreferences(MODE_PRIVATE).edit();
		editor.putInt(CURRENT_TAG_KEY, current_tab);
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// restore last visit tab from shared preference
		SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
		int current_tab = preferences.getInt(CURRENT_TAG_KEY, 0);
		actionBar.setSelectedNavigationItem(current_tab);
	}

	@Override
	public void onBackPressed() {
		AlertDialogHelper.showConfirmExitDialog(this, imageLoader);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, actionBar.getSelectedNavigationIndex());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
				int position = savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM);
				actionBar.setSelectedNavigationItem(position);
			}
		}
	}

	public List<Product> getLstProducts() {
		return lstProducts;
	}

	public void setLstProducts(List<Product> lstProducts) {
		this.lstProducts = lstProducts;
	}

	public List<News> getLstNews() {
		return lstNews;
	}

	public void setLstNews(List<News> lstNews) {
		this.lstNews = lstNews;
	}

	public long getLastProductUpdateTime() {
		return lastProductUpdateTime;
	}

	public void setLastProductUpdateTime(long lastProductUpdateTime) {
		this.lastProductUpdateTime = lastProductUpdateTime;
	}

	public long getLastNewsUpdateTime() {
		return lastNewsUpdateTime;
	}

	public void setLastNewsUpdateTime(long lastNewsUpdateTime) {
		this.lastNewsUpdateTime = lastNewsUpdateTime;
	}
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
}
