package com.blueskyconnie.bluestonecrystal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.helper.ConnectionDetector;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class DetailActivity extends Activity {

//	private static final int REQ_WIDTH = 320;
//	private static final int REQ_HEIGHT = 320;

	private ProgressDialog dialog; 
//	private WeakReference<DownloadImageTask> asyncTaskWeakRef;
	private Product currentProduct = null;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private ImageView imgView;
	private TextView tvName;
	private TextView tvDesc; 
	private TextView tvPrice; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detail);
		
		dialog = new ProgressDialog(this);
		imgView = (ImageView) findViewById(R.id.imgItem);
		tvName = (TextView) findViewById(R.id.tvItemName);
		tvDesc = (TextView) findViewById(R.id.tvItemDescription);
		tvPrice = (TextView) findViewById(R.id.tvItemPrice);

		if (getIntent() != null)  {
			currentProduct = (Product) getIntent().getParcelableExtra("currentProduct");
			if (currentProduct != null) { 
				ConnectionDetector detector = new ConnectionDetector(this);
				if (detector.isConnectingToInternet()) {
					// retrieve image
					imageLoader.displayImage(currentProduct.getImageUrl(), imgView, 
							new SimpleImageLoadingListener(){
								@Override
								public void onLoadingStarted(String imageUri, View view) {
									if (dialog != null) {
										dialog.setTitle(DetailActivity.this.getString(R.string.bluestone_crystal));
										dialog.setMessage(DetailActivity.this.getString(R.string.downloading));
										dialog.show();
									}
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									super.onLoadingFailed(imageUri, view, failReason);
									
									String message = null;
									switch (failReason.getType()) {
										case IO_ERROR:
											message = "Input/Output error";
											break;
										case DECODING_ERROR:
											message = "Image can't be decoded";
											break;
										case NETWORK_DENIED:
											message = "Downloads are denied";
											break;
										case OUT_OF_MEMORY:
											message = "Out Of Memory error";
											break;
										case UNKNOWN:
											message = "Unknown error";
											break;
									}
									if (dialog != null) {
										dialog.dismiss();
									}
									Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view, loadedImage);
									if (dialog != null) {
										dialog.dismiss();
									}
								}
							});
					tvName.setText(currentProduct.getName());
					tvDesc.setText(currentProduct.getDescription());
					tvPrice.setText(currentProduct.getPrice().toPlainString());
				}
			}
		}
		Log.i("Detail Activity" ,"onCreate"); 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// http://stackoverflow.com/questions/3378102/error-view-not-attached-to-window-manager/11448457#11448457
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// http://stackoverflow.com/questions/3378102/error-view-not-attached-to-window-manager/11448457#11448457
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	
//	private void startNewAsyncTask(Product product) {
//		DownloadImageTask asyncTask = new DownloadImageTask(this, product);
//	    this.asyncTaskWeakRef = new WeakReference<DownloadImageTask >(asyncTask);
//	    // image url
//	    asyncTaskWeakRef.get().execute();
//	}

//	private class DownloadImageTask extends AsyncTask<String, Void, Product> {
//
//		private WeakReference<DetailActivity> fragmentWeakRef;
//		private Product product;
//		
//		private DownloadImageTask (DetailActivity fragment, Product product) {
//            this.fragmentWeakRef = new WeakReference<DetailActivity>(fragment);
//            this.product = product;
//        }
//		
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			if (fragmentWeakRef.get() != null) {
//				ImageView imgView = (ImageView) fragmentWeakRef.get().findViewById(R.id.imgItem);
//				TextView tv1 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemName);
//				TextView tv2 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemDescription);
//				TextView tv3 = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemPrice);
//				TextView lbl1 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblItemName);
//				TextView lbl2 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblDescription);
//				TextView lbl3 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblPrice);
//
//				if (imgView != null) {
//					imgView.setVisibility(ImageView.GONE);
//				}
//				
//				if (tv1 != null) {
//					tv1.setVisibility(TextView.GONE);
//				}
//				
//				if (tv2 != null) {
//					tv2.setVisibility(TextView.GONE);
//				}
//				
//				if (tv3 != null) {
//					tv3.setVisibility(TextView.GONE);
//				}
//				
//				if (lbl1 != null) {
//					lbl1.setVisibility(TextView.GONE);
//				}
//				
//				if (lbl2 != null) {
//					lbl2.setVisibility(TextView.GONE);
//				}
//				
//				if (lbl3 != null) {
//					lbl3.setVisibility(TextView.GONE);
//				}
//			}
//			
//			if (dialog != null) {
//				dialog.setTitle(fragmentWeakRef.get().getString(R.string.bluestone_crystal));
//				dialog.setMessage(fragmentWeakRef.get().getString(R.string.downloading));
//				dialog.show();
//			}
//		}
//
//		@Override
//		protected Product doInBackground(String... params) {
//
//			Product product = new Product();
//			product.setName(this.product.getName());
//			product.setDescription(this.product.getDescription());
//			product.setPrice(this.product.getPrice());
//			product.setImageUrl(this.product.getImageUrl());
//
//			Bitmap bitmapProduct = ImageDecodeHelper.decodeSampledBitmapFromByteArray(
//					product.getImageUrl(), REQ_WIDTH, REQ_HEIGHT);
//			if (bitmapProduct != null) {
//				product.setImage(bitmapProduct);
//			} else {
//				product.setImage(null);
//			}
//			return product;
//		}
//
//		@Override
//		protected void onPostExecute(Product result) {
//			super.onPostExecute(result);
//			if (this.fragmentWeakRef.get() != null) {
//				TextView tvItemName = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemName);
//				if (tvItemName != null) {
//					tvItemName.setText(result.getName());
//					tvItemName.setVisibility(TextView.VISIBLE);
//				}
//				
//				TextView tvItemDesc = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemDescription);
//				if (tvItemDesc != null) {
//					tvItemDesc.setText(result.getDescription());
//					tvItemDesc.setVisibility(TextView.VISIBLE);
//				}
//				
//				TextView tvItemPrice = (TextView) fragmentWeakRef.get().findViewById(R.id.tvItemPrice);
//				if (tvItemPrice != null) {
//					tvItemPrice.setText(result.getPrice().toPlainString());
//					tvItemPrice.setVisibility(TextView.VISIBLE);
//				}
//				
//				ImageView imgView = (ImageView) fragmentWeakRef.get().findViewById(R.id.imgItem);
//				if (result.getImage() != null) {
//					imgView.setImageBitmap(result.getImage());
//					fragmentWeakRef.get().currentProduct.setImage(result.getImage());
//				} 
//				imgView.setVisibility(ImageView.VISIBLE);
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//				
//				TextView lbl1 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblItemName);
//				TextView lbl2 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblDescription);
//				TextView lbl3 = (TextView) fragmentWeakRef.get().findViewById(R.id.lblPrice);
//				if (lbl1 != null) {
//					lbl1.setVisibility(TextView.VISIBLE);
//				}
//				if (lbl2 != null) {
//					lbl2.setVisibility(TextView.VISIBLE);
//				}
//				if (lbl3 != null) {
//					lbl3.setVisibility(TextView.VISIBLE);
//				}
//			}
//			Log.i("DownloadImageTask - onPostExecute", "Load detail image...");
//		}
//	}
}
