package com.blueskyconnie.bluestonecrystal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;

public class FacebookFragment extends Fragment {

	private static final String DEFAULT_HOMEPAGE = "https://www.facebook.com/metroame";
	private static final String TAROT_HOMEPAGE = "https://www.facebook.com/tarot.selina";

	private String homepage;
	
	private OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			
			ConnectionDetector detector = new ConnectionDetector(getActivity());
			switch (v.getId()) {
				case R.id.tvAmeFB:
					if (detector.isConnectingToInternet()) {
						Intent ameIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEFAULT_HOMEPAGE));
						startActivity(ameIntent);
					} else {
						AlertDialogHelper.showNoInternetDialog(FacebookFragment.this.getActivity());
					}
					break;
				case R.id.tvSelinaFB:
					if (detector.isConnectingToInternet()) {
						Intent tarotIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TAROT_HOMEPAGE));
						startActivity(tarotIntent);
					} else {
						AlertDialogHelper.showNoInternetDialog(FacebookFragment.this.getActivity());
					}
					break;
				case R.id.tvBluestoneFB:
					if (detector.isConnectingToInternet()) {
						Intent homeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(homepage));
						startActivity(homeIntent);
					} else {
						AlertDialogHelper.showNoInternetDialog(FacebookFragment.this.getActivity());
					}
					break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_facebook, container, false);
		TextView tvAmeFB = (TextView) rootView.findViewById(R.id.tvAmeFB);
		TextView tvTarotFB = (TextView) rootView.findViewById(R.id.tvSelinaFB);
		TextView tvBluestoneFB = (TextView) rootView.findViewById(R.id.tvBluestoneFB);

		MainActivity activity = (MainActivity) getActivity();
		if (activity != null) {
			homepage = activity.getShop().getHomepage();
		} 
		
		tvAmeFB.setOnClickListener(listener);
		tvTarotFB.setOnClickListener(listener);
		tvBluestoneFB.setOnClickListener(listener);

		return rootView;
	}
	
}
