package com.blueskyconnie.bluestonecrystal.helper;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;

import com.blueskyconnie.bluestonecrystal.MainActivity;
import com.blueskyconnie.bluestonecrystal.R;
import com.blueskyconnie.bluestonecrystal.data.News;
import com.blueskyconnie.bluestonecrystal.data.Product;
import com.blueskyconnie.bluestonecrystal.data.Shop;
import com.blueskyconnie.bluestonecrystal.exception.BusinessException;

public final class HttpClientHelper {

//	public static InputStream retrieveImage(String strUrl) {
//		
//		if (strUrl == null || strUrl.length() == 0) {
//			return null;
//		}
//		
//		InputStream is = null;
//		BufferedInputStream bis = null;
//		 try{
//			 URL url = new URL(strUrl);
//			 URLConnection conn = url.openConnection();
//			 HttpURLConnection httpConn = (HttpURLConnection)conn;
//			  httpConn.setRequestMethod("GET");
//			  httpConn.connect();
//		 
//			  if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				  is = httpConn.getInputStream();
//				  if (is != null) {
//					  bis = new BufferedInputStream(is);
//					  return bis;
//				  }
//			  } 
//		 }
//		 catch (IOException ex){
//			 ex.printStackTrace();
//		 }
//		 return null;
//	}
	
	public static List<Product> retrieveProducts(Context context, String strUrl, String cms_url) 
		throws BusinessException {

		if (strUrl == null || strUrl.length() == 0) {
			return new ArrayList<Product>();
		}
		
		InputStream is = null;
   	    try{
		 	 URL url = new URL(strUrl);
			 URLConnection conn = url.openConnection();
			 HttpURLConnection httpConn = (HttpURLConnection)conn;
			 httpConn.setRequestMethod("GET");
			 httpConn.connect();
		 
			 if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				  is = httpConn.getInputStream();
				  
				  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				  DocumentBuilder builder = factory.newDocumentBuilder();
				  Document doc = builder.parse(is);
				  List<Product> lstProduct = new ArrayList<Product>();

				  NodeList nodes = doc.getElementsByTagName("product");
				  if (nodes != null) {
						for (int i = 0; i < nodes.getLength(); i++) {
							Product prod = new Product();
							Node node = nodes.item(i);
							NamedNodeMap attrs = node.getAttributes();

							Node attrNode = attrs.getNamedItem("name");
							if (attrNode != null) {
								prod.setName(attrNode.getTextContent());
							}
							
							attrNode = attrs.getNamedItem("description");
							if (attrNode != null) {
								prod.setDescription(attrNode.getTextContent());
							}
							
							attrNode = attrs.getNamedItem("price");
							if (attrNode != null) {
								prod.setPrice(new BigDecimal(attrNode.getTextContent()));
							}
							
							attrNode = attrs.getNamedItem("id");
							if (attrNode != null) {
								prod.setId(Integer.valueOf(attrNode.getTextContent()));
							}

							attrNode = attrs.getNamedItem("url");
							if (attrNode != null) {
								String imgUrl = attrNode.getTextContent();
								if (imgUrl != null && imgUrl.length() > 0) {
									prod.setImageUrl(cms_url + attrNode.getTextContent());
								}
							}
							lstProduct.add(prod);
						}
					}
					return lstProduct;

			  } 
		 } catch (IOException ex){
			 ex.printStackTrace();
			 throw new BusinessException(context.getString(R.string.http_request_error), ex);
		 } catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			 throw new BusinessException(context.getString(R.string.http_request_error), ex);
		 } catch (SAXException ex) {
			ex.printStackTrace();
			 throw new BusinessException(context.getString(R.string.http_request_error), ex);
		 } finally {
			 try {
				 if (is != null) {
					 is.close();
				 }
			 } catch (IOException ex) {
				 ex.printStackTrace();
			 }
		 }
   	     return new ArrayList<Product>();
	}
	
	
	public static List<News> retrieveNews(Context context, String strUrl) 
			throws BusinessException {

			if (strUrl == null || strUrl.length() == 0) {
				return new ArrayList<News>();
			}
			
			InputStream is = null;
	   	    try{
			 	 URL url = new URL(strUrl);
				 URLConnection conn = url.openConnection();
				 HttpURLConnection httpConn = (HttpURLConnection)conn;
				 httpConn.setRequestMethod("GET");
				 httpConn.connect();
			 
				 if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					  is = httpConn.getInputStream();
					  
					  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					  DocumentBuilder builder = factory.newDocumentBuilder();
					  Document doc = builder.parse(is);
					  List<News> lstNews = new ArrayList<News>();

					  NodeList nodes = doc.getElementsByTagName("new");
					  if (nodes != null) {
							for (int i = 0; i < nodes.getLength(); i++) {
								News news = new News();
								Node node = nodes.item(i);
								NamedNodeMap attrs = node.getAttributes();
								
								Node attrNode = attrs.getNamedItem("contents");
								if (attrNode != null) {
									news.setContents(attrNode.getTextContent());
								}
								
								attrNode = attrs.getNamedItem("updated_at");
								if (attrNode != null) {
									try {
										String updated_at = attrNode.getTextContent();
										String strDate = MainActivity.sdf.format(MainActivity.sdf_ymd_hms.parse(updated_at));
										news.setUpdateAt(strDate);
									} catch (ParseException ex) {
										news.setUpdateAt("");
									}
								}
								lstNews.add(news);
							}
					  }
					  return lstNews;
				  } 
			 } catch (IOException ex){
				 ex.printStackTrace();
//				 throw new BusinessException(ex.getMessage(), ex);
				 throw new BusinessException(context.getString(R.string.http_request_error), ex);
			 } catch (ParserConfigurationException ex) {
				ex.printStackTrace();
				 throw new BusinessException(context.getString(R.string.http_request_error), ex);
			 } catch (SAXException ex) {
				ex.printStackTrace();
				throw new BusinessException(context.getString(R.string.http_request_error), ex);
			} finally {
				 try {
					 if (is != null) {
						 is.close();
					 }
				 } catch (IOException ex) {
					 ex.printStackTrace();
				 }
			 }
	   	     return new ArrayList<News>();
		}

	public static Shop retrieveShop(Context context, String shopUrl) throws BusinessException {
		if (shopUrl == null || shopUrl.length() == 0) {
			return new Shop();
		}
		
		InputStream is = null;
   	    try{
		 	 URL url = new URL(shopUrl);
			 URLConnection conn = url.openConnection();
			 HttpURLConnection httpConn = (HttpURLConnection)conn;
			 httpConn.setRequestMethod("GET");
			 httpConn.connect();
		 
			 if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				  is = httpConn.getInputStream();
				  
				  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				  DocumentBuilder builder = factory.newDocumentBuilder();
				  Document doc = builder.parse(is);
				  Shop shop = new Shop();

				  NodeList nodes = doc.getElementsByTagName("shop");
				  if (nodes != null && nodes.getLength() > 0) {
					Node node = nodes.item(0);
					NodeList children = node.getChildNodes();
					if (children != null) {
						for (int i = 0; i < children.getLength(); i++) {
							Node childNode = children.item(i);
							if (childNode != null && childNode.getNodeName().equals("email")) {
								 shop.setEmail(childNode.getTextContent());
							} else if (childNode != null && childNode.getNodeName().equals("address")) {
								 shop.setAddress(childNode.getTextContent());
							} else if (childNode != null && childNode.getNodeName().equals("homepage")) {
								 shop.setHomepage(childNode.getTextContent());
							}
						}
						
					}
				  }
				  return shop;
			  } 
		 } catch (IOException ex){
			 ex.printStackTrace();
			 throw new BusinessException(context.getString(R.string.http_request_error), ex);
		 } catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			throw new BusinessException(context.getString(R.string.http_request_error), ex);
		 } catch (SAXException ex) {
			ex.printStackTrace();
			throw new BusinessException(context.getString(R.string.http_request_error), ex);
		} finally {
			 try {
				 if (is != null) {
					 is.close();
				 }
			 } catch (IOException ex) {
				 ex.printStackTrace();
			 }
		 }
   		 return new Shop();
	}
	
}
