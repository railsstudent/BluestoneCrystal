package com.blueskyconnie.bluestonecrystal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.helper.AlertDialogHelper;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;

public class ContactFragment extends Fragment {

	private Button btnSendMail;
	private Button btnTellAFriend;
	private TextView tvAddress;
	private TextView tvEmail;
	private String shopEmail = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
		
		btnSendMail = (Button) rootView.findViewById(R.id.btnSendMail);
		btnTellAFriend = (Button) rootView.findViewById(R.id.btnTellAFriend);
		tvAddress = (TextView) rootView.findViewById(R.id.tvAddress);
		tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
		OnClickListener listener = new MyOnClickListener();
		btnSendMail.setOnClickListener(listener);
		btnTellAFriend.setOnClickListener(listener);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		MainActivity activity = (MainActivity) getActivity();
		if (activity != null) {
			tvAddress.setText(activity.getShop().getAddress());
			tvEmail.setText(activity.getShop().getEmail());
			shopEmail = activity.getShop().getEmail();
		} else {
			tvAddress.setText("");
			tvEmail.setText("");
		}
	}



	private final class MyOnClickListener implements
		OnClickListener {
		@Override
		public void onClick(View view) {
			
			ConnectionDetector detector = new ConnectionDetector(getActivity());
			if (!detector.isConnectingToInternet()) {
				AlertDialogHelper.showNoInternetDialog(getActivity());
			} else {
				switch (view.getId()) {
					case R.id.btnSendMail:
						Intent itEmail = new Intent();
						itEmail.setAction(Intent.ACTION_VIEW);
						itEmail.setData(Uri.parse("mailto:" + shopEmail));
						itEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
						itEmail.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text));
						try {
							startActivity(Intent.createChooser(itEmail, getString(R.string.choose_email_app)));
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(ContactFragment.this.getActivity(), 
									getString(R.string.email_app_not_installed), Toast.LENGTH_SHORT).show();
						}
						break;
					case R.id.btnTellAFriend:
						Intent tellFriend = new Intent();
						tellFriend.setAction(Intent.ACTION_SEND);
						tellFriend.setType("text/plain");
						tellFriend.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
						tellFriend.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
						try {
							startActivity(Intent.createChooser(tellFriend, getString(R.string.choose_share_app)));
						} catch (ActivityNotFoundException ex) {
							Toast.makeText(ContactFragment.this.getActivity(), 
									getString(R.string.share_app_not_installed), Toast.LENGTH_SHORT).show();
						}
						break;
				}
			}
		}
	}
}
