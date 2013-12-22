package com.blueskyconnie.bluestonecrystal.data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class BatchData implements Parcelable {

	private List<Product> lstProduct;
	private List<News> lstNews;
	private long lastProductUpdateTime;
	private long lastNewsUpdateTime;
	private Shop shop;
	
	public BatchData() {}
	
	public BatchData (Parcel in){
		lstProduct = new ArrayList<Product>();
		lstNews = new ArrayList<News>();
		in.readTypedList(lstProduct, Product.CREATOR);
		in.readTypedList(lstNews, News.CREATOR);
		this.lastProductUpdateTime = in.readLong();
		this.lastNewsUpdateTime = in.readLong();
		this.shop = in.readParcelable(Shop.class.getClassLoader());
	}
	
	public List<Product> getLstProduct() {
		return lstProduct;
	}
	
	public void setLstProduct(List<Product> lstProduct) {
		this.lstProduct = lstProduct;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (lastNewsUpdateTime ^ (lastNewsUpdateTime >>> 32));
		result = prime
				* result
				+ (int) (lastProductUpdateTime ^ (lastProductUpdateTime >>> 32));
		result = prime * result + ((lstNews == null) ? 0 : lstNews.hashCode());
		result = prime * result
				+ ((lstProduct == null) ? 0 : lstProduct.hashCode());
		result = prime * result + ((shop == null) ? 0 : shop.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BatchData other = (BatchData) obj;
		if (lastNewsUpdateTime != other.lastNewsUpdateTime)
			return false;
		if (lastProductUpdateTime != other.lastProductUpdateTime)
			return false;
		if (lstNews == null) {
			if (other.lstNews != null)
				return false;
		} else if (!lstNews.equals(other.lstNews))
			return false;
		if (lstProduct == null) {
			if (other.lstProduct != null)
				return false;
		} else if (!lstProduct.equals(other.lstProduct))
			return false;
		if (shop == null) {
			if (other.shop != null)
				return false;
		} else if (!shop.equals(other.shop))
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(lstProduct);
		dest.writeTypedList(lstNews);
		dest.writeLong(lastProductUpdateTime);
		dest.writeLong(lastNewsUpdateTime);
		dest.writeParcelable(shop, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
	}
	
	public static final Parcelable.Creator<BatchData> CREATOR = new Parcelable.Creator<BatchData>() {
        public BatchData createFromParcel(Parcel in) {
            return new BatchData(in); 
        }

        public BatchData[] newArray(int size) {
            return new BatchData[size];
        }
    };
    
    public Shop getShop() {
    	return this.shop;
    }

	public void setShop(Shop shop) {
		this.shop = shop;
	}
}
