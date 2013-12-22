package com.blueskyconnie.bluestonecrystal.helper;


public final class ImageDecodeHelper {
	
	// use third-party universal image loader to retrieve images from Internet	
//	private static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//	    // Raw height and width of image
//	    final int height = options.outHeight;
//	    final int width = options.outWidth;
//	    int inSampleSize = 1;
//
//	    if (height > reqHeight || width > reqWidth) {
//	        if (width > height) {
//	        	inSampleSize = Math.round((float)height / (float)reqHeight);   
//	        } else {
//	        	inSampleSize = Math.round((float)width / (float)reqWidth);   
//	        }   
//       }
//	    return inSampleSize;
//	}
//	
//	public static Bitmap decodeSampledBitmapFromStream (String strUrl,
//	        int reqWidth, int reqHeight)  {
//
//	    // First decode with inJustDecodeBounds=true to check dimensions
//	    InputStream is = null;
//	    InputStream decodeStream = null;
//	    
//	    try  {
//	    
//	    	final BitmapFactory.Options options = new BitmapFactory.Options();
//		    options.inJustDecodeBounds = true;
//		    
//		    is = HttpClientHelper.retrieveImage(strUrl);
//		    BitmapFactory.decodeStream (is, null, options);
//
//		    // Calculate inSampleSize
//		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//		    // Decode bitmap with inSampleSize set
//		    options.inJustDecodeBounds = false;
//	    
//		    // according to http://stackoverflow.com/questions/2503628/bitmapfactory-decodestream-returning-null-when-options-are-set/2505357#2505357
//		    is.close();
//		    is = null;
//		    decodeStream = HttpClientHelper.retrieveImage(strUrl); 
//		    Bitmap scaleBitmap =  BitmapFactory.decodeStream(decodeStream, null, options);
//		    return scaleBitmap;
//	    } catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//	    	if (decodeStream != null) {
//	    		try {
//	    			decodeStream.close();
//	    		} catch (IOException ex) {
//	    			ex.printStackTrace();
//	    		}
//	    	}
//	    	
//	    	try {
// 	    	  if (is != null) {
//	    		  is.close();
//	    	  }
//	    	} catch (IOException ex) {
//	    		ex.printStackTrace();
//	    	}
//	    }
//	    return null;
//	}
//	
//	public static Bitmap decodeSampledBitmapFromByteArray(String strUrl,
//	        int reqWidth, int reqHeight) {
//
//	    // First decode with inJustDecodeBounds=true to check dimensions
//	    InputStream is = null;
//	    
//	    try  {
//		    // First decode with inJustDecodeBounds=true to check dimensions
//	    	final BitmapFactory.Options options = new BitmapFactory.Options();
//		    options.inJustDecodeBounds = true;
//		    
//		    is = HttpClientHelper.retrieveImage(strUrl);
//		    if (is == null) {
//		    	return null;
//		    }
//		    
//		    byte[] buf = new byte[1024];
//		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		    int byteRead = 0;
//
//		    byteRead = is.read(buf, 0, 1024);
//		    while (byteRead != -1) {
//		    	baos.write(buf, 0, byteRead);
//			    byteRead = is.read(buf, 0, 1024);
//		    }
//
//		    is.close();
//		    is = null;
//		    
//		    byte[] data = new byte[baos.size()];
//		    byte[] data2 = new byte[baos.size()];
//		    
//		    System.arraycopy(baos.toByteArray(), 0, data, 0, data.length);
//		    System.arraycopy(data , 0, data2, 0, data2.length);
//
//		    BitmapFactory.decodeByteArray (data, 0, data.length, options);
//		    
//		    // Calculate inSampleSize
//		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//		    // Decode bitmap with inSampleSize set
//		    options.inJustDecodeBounds = false;
//	    
//		    Bitmap scaleBitmap =  BitmapFactory.decodeByteArray(data2, 0, data2.length, options);
//		    return scaleBitmap;
//	    } catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//	    	try {
// 	    	  if (is != null) {
//	    		  is.close();
//	    	  }
//	    	} catch (IOException ex) {
//	    		ex.printStackTrace();
//	    	}
//	    }
//	    return null;
//	}
}
