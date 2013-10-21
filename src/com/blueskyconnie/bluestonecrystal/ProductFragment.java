package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.adapter.ProductAdapter;
import com.blueskyconnie.bluestonecrystal.data.Product;

public class ProductFragment extends ListFragment {

	private WeakReference<RetrieveProductTask> asyncTaskWeakRef;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	    startNewAsyncTask();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_product, container, false);
		
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefresh);
		imgBtnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startNewAsyncTask();
			}
		});
		return rootView;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getActivity(), "List item clicked", Toast.LENGTH_SHORT).show();
		
		// go to detail fragment
		Product product = (Product) l.getItemAtPosition(position);
		DetailFragment detailFragment = new DetailFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable("currentProduct", product);
	    detailFragment.setArguments(bundle);
	    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	    // replace product fragment with detail fragment
	    fragmentTransaction.replace(R.layout.fragment_product, detailFragment);
	    fragmentTransaction.addToBackStack(null);
	    fragmentTransaction.commit();
	}
	
	private void startNewAsyncTask() {
		RetrieveProductTask asyncTask = new RetrieveProductTask(this);
	    this.asyncTaskWeakRef = new WeakReference<RetrieveProductTask >(asyncTask);
	    asyncTaskWeakRef.get().execute("");
	}
	
	private class RetrieveProductTask extends AsyncTask<String,Void, List<Product>> {

		private final ProgressDialog dialog = new ProgressDialog(ProductFragment.this.getActivity()); 
		private WeakReference<ProductFragment> fragmentWeakRef;

        private RetrieveProductTask (ProductFragment fragment) {
            this.fragmentWeakRef = new WeakReference<ProductFragment>(fragment);
        }

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage(ProductFragment.this.getString(R.string.dowloading));
			dialog.show();
		}

		@Override
		protected List<Product> doInBackground(String... params) {
//			String url = params[0];
			// create thousand products
			List<Product> lstProduct = new ArrayList<Product>();
			BitmapFactory.Options op = new BitmapFactory.Options();
		    op.inPurgeable = true;
			for (int i = 0; i < 1000; i++) {
				Product prod = new Product();
				prod.setId(i);
				prod.setName("Super Very Long, Long, Long, Long, Long, Long Name " + i);
				prod.setPrice(new BigDecimal(i + 0.25));
				prod.setDescription("Description " + i);
				prod.setImageUrl("");
				if (prod.getImageUrl() != null && prod.getImageUrl().length() > 0) {
					// get image stream
					//HttpClient client = new DefaultHttpClient();
					
				}
				lstProduct.add(prod);
			}
			return lstProduct;
		}
		
		@Override
		protected void onPostExecute(List<Product> lstProducts) {
			super.onPostExecute(lstProducts);
			if (this.fragmentWeakRef.get() != null) {
//				ProductAdapter adapter = new ProductAdapter(ProductFragment.this.getActivity(),
//						R.layout.product_row_layout, lstProducts);
				//ProductFragment.this.setListAdapter(adapter);
				//Toast.makeText(ProductFragment.this.getActivity(), "onPostExecute", Toast.LENGTH_SHORT).show();
				ProductAdapter adapter = new ProductAdapter(fragmentWeakRef.get().getActivity(),
						R.layout.product_row_layout, lstProducts);
				fragmentWeakRef.get().setListAdapter(adapter);
				dialog.dismiss();
			}
		}
	}
	
}
