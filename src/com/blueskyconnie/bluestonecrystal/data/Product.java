package com.blueskyconnie.bluestonecrystal.data;

import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

	private int id;
	private String name;
	private String description;
	private BigDecimal price;
	private Bitmap image;
	private String imageUrl;
	
	public Product() {}
	
	public Product (Parcel in){
		  this.id = in.readInt();
		  this.name = in.readString();
		  this.description = in.readString();
		  this.imageUrl = in.readString();
		  this.image = in.readParcelable(Bitmap.class.getClassLoader());
		  this.price = new BigDecimal(in.readString());
	 }
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeString(this.imageUrl);
		dest.writeParcelable(this.image, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(this.price.toPlainString());
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in); 
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
