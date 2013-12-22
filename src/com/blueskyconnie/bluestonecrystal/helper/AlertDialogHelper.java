package com.blueskyconnie.bluestonecrystal.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.MainActivity;
import com.blueskyconnie.bluestonecrystal.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public final class AlertDialogHelper {

	public static void showNoInternetDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(context.getString(R.string.no_internet_error));
		builder.setNeutralButton(context.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	public static void showConfirmExitDialog(final Context context, final ImageLoader imageLoader) {
		
		// prompt confirmation dialog before exit
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_NEGATIVE:   // confirm to exit
						// close dialog and do nothing
						if (context != null && context instanceof MainActivity) {
							dialog.dismiss();
							imageLoader.stop();
							Log.i("showConfirmExitDialog", "Stop Universal Image Loader.");
							((MainActivity) context).finish();
						} else {
							Toast.makeText(context, R.string.exit_error, Toast.LENGTH_LONG).show();	
						}
						break;
					case DialogInterface.BUTTON_POSITIVE:   // cancel
						dialog.dismiss();
						break;
				}
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.bluestone_crystal);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(R.string.title_confirm_exit);
		builder.setPositiveButton(R.string.cancel_exit, listener);
		builder.setNegativeButton(R.string.confirm_exit, listener);
		AlertDialog quitDialog = builder.create();
		quitDialog.show();
	}
}