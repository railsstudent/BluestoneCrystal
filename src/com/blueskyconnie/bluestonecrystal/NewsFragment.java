package com.blueskyconnie.bluestonecrystal;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.adapter.NewsAdapter;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;
import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.blueskyconnie.bluestonecrystal.helper.HttpClientHelper;

public class NewsFragment extends ListFragment {

	private ProgressDialog dialog;
	private WeakReference<RetrieveNewsTask> asyncTaskWeakRef;
	private String cmsUrl;
	private TextView tvUpdateTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRetainInstance(true);
		dialog = new ProgressDialog(getActivity()); 
		cmsUrl = getString(R.string.cms_url);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_news, container, false);
		tvUpdateTime = (TextView) rootView.findViewById(R.id.tvNewsLastUpdate);
		
		ImageButton imgBtnRefresh = (ImageButton) rootView.findViewById(R.id.imgRefreshNews);
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
		
//		Log.i("News Fragment", "onCreateView called."); 
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		MainActivity activity = (MainActivity) this.getActivity();
		if (activity != null) {
			List<News> lstNews = activity.getLstNews();
			NewsAdapter newsAdapter = new NewsAdapter(getActivity(), R.layout.news_row_layout, lstNews);
			setListAdapter(newsAdapter);
			tvUpdateTime.setText(getString(R.string.last_update_time) + 
					MainActivity.sdf.format(new Date(activity.getLastNewsUpdateTime())));
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
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	private void startNewAsyncTask(TextView tvUpdateTime) {
		RetrieveNewsTask asyncTask = new RetrieveNewsTask(this, tvUpdateTime);
	    asyncTaskWeakRef = new WeakReference<RetrieveNewsTask >(asyncTask);
	    asyncTaskWeakRef.get().execute(cmsUrl + "news_android.php?id=" + MainActivity.SHOP_ID);
	}

	private class RetrieveNewsTask extends AsyncTask<String, Void, List<News>> {

		private WeakReference<NewsFragment> fragmentWeakRef;
		private WeakReference<TextView> tvLastUpdateReference;
		private Exception exception;
		
		private RetrieveNewsTask(NewsFragment fragment, TextView tvUpdateTime) {
			fragmentWeakRef = new WeakReference<NewsFragment>(fragment);
            tvLastUpdateReference = new WeakReference<TextView>(tvUpdateTime);
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
		protected List<News> doInBackground(String... params) {
			try {
				List<News> lstNews = HttpClientHelper.retrieveNews(fragmentWeakRef.get().getActivity(), params[0]);
				return lstNews;
			} catch (BusinessException ex) {
				ex.printStackTrace();
				exception = ex;
			}
			return new ArrayList<News>();
		}

		@Override
		protected void onPostExecute(List<News> lstNews) {
			super.onPostExecute(lstNews);
			if (fragmentWeakRef.get() != null) {
				MainActivity activity = (MainActivity) fragmentWeakRef.get().getActivity();
				if (exception != null) {
					Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
				}

				NewsAdapter newsAdapter = new NewsAdapter(activity, R.layout.news_row_layout, lstNews);
				activity.setLstNews(lstNews);
				// update list adapter
				fragmentWeakRef.get().setListAdapter(newsAdapter);
				// display current time
				if (tvLastUpdateReference.get() != null) {
					long curTime = Calendar.getInstance().getTimeInMillis();
					tvLastUpdateReference.get().setText(getString(R.string.last_update_time) + 
							MainActivity.sdf.format(new Date(curTime)));
					activity.setLastNewsUpdateTime(curTime);
				}
			}
			
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
}
