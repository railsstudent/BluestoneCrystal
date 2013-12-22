package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class ProductFragment extends ListFragment {
	
	private ProgressDialog dialog;
	private WeakReference<RetrieveProductTask> asyncTaskWeakRef;
	private boolean hasClickedItem = false;
	private String cmsUrl;
	private TextView tvUpdateTime;
	private ImageButton imgBtnRefresh;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		dialog = new ProgressDialog(getActivity()); 
		cmsUrl = getString(R.string.cms_url);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		tvUpdateTime = (TextView) rootView.findViewById(R.id.tvLastUpdate);
		imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ConnectionDetector detector = new ConnectionDetector(getActivity());
				if (detector.isConnectingToInternet()) {
					startNewAsyncTask(tvUpdateTime);
				} else {
					AlertDialogHelper.showNoInternetDialog(getActivity());
				}
			}
		});
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// restore products 
		MainActivity activity = (MainActivity) getActivity();
		ProductAdapter adapter = new ProductAdapter(getActivity(), R.layout.product_row_layout, 
				activity.getLstProducts());
		setListAdapter(adapter);
		tvUpdateTime.setText(getString(R.string.last_update_time) + 
				MainActivity.sdf.format(new Date(activity.getLastProductUpdateTime())));
	//	Log.i("Product Fragment", "onActivityCreated");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ListView listview = getListView();
		if (listview != null) {
			listview.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));
		}
//		Log.i("Product Fragment", "onResume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (!hasClickedItem) {
			// go to detail activity
			Product product = (Product) l.getItemAtPosition(position);
		    Intent intent = new Intent(this.getActivity(), DetailActivity.class);
		    intent.putExtra("currentProduct", product);
		    hasClickedItem = true;
		    startActivityForResult(intent, 1);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		hasClickedItem = false;
	}

	private void startNewAsyncTask(TextView tvUpdateTime) {
		RetrieveProductTask asyncTask = new RetrieveProductTask(this, tvUpdateTime);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveProductTask >(asyncTask);
	    asyncTaskWeakRef.get().execute(cmsUrl + "products_android.php?id=" + MainActivity.SHOP_ID);
	}
	
	private class RetrieveProductTask extends AsyncTask<String,Void, List<Product>> {

		private WeakReference<ProductFragment> fragmentWeakRef;
		private WeakReference<TextView> tvLastUpdate;
		private Exception exception;

        private RetrieveProductTask (ProductFragment fragment, TextView lblUpdate) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
            this.tvLastUpdate = new WeakReference<TextView>(lblUpdate);
            exception = null;
        }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (dialog != null) {
				dialog.setTitle(fragmentWeakRef.get().getString(R.string.bluestone_crystal));
				dialog.setMessage(fragmentWeakRef.get().getString(R.string.downloading));
				dialog.show();
			}
		}

		@Override
		protected List<Product> doInBackground(String... params) {
			try {
				List<Product> lstProduct = HttpClientHelper.retrieveProducts(fragmentWeakRef.get().getActivity(), 
						params[0], cmsUrl);
				return lstProduct;
			} catch (BusinessException ex) {
				ex.printStackTrace();
				exception = ex;
			}
			return new ArrayList<Product>();
		}
		
		@Override
		protected void onPostExecute(List<Product> lstProducts) {
			super.onPostExecute(lstProducts);
			if (fragmentWeakRef.get() != null) {
				MainActivity activity = (MainActivity) fragmentWeakRef.get().getActivity();
				if (exception != null) {
					Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_SHORT).show();
				}

				ProductAdapter adapter = new ProductAdapter(activity, R.layout.product_row_layout, lstProducts);
				// update list adapter
				fragmentWeakRef.get().setListAdapter(adapter);
				// display current time
				if (tvLastUpdate.get() != null) {
					long curTime = Calendar.getInstance().getTimeInMillis();
					tvLastUpdate.get().setText(getString(R.string.last_update_time) + MainActivity.sdf.format(new Date(curTime)));
					activity.setLastProductUpdateTime(curTime);
				}
				activity.setLstProducts(lstProducts);
				
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		}
	}
}
