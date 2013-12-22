package com.blueskyconnie.bluestonecrystal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blueskyconnie.bluestonecrystal.R;
import com.blueskyconnie.bluestonecrystal.data.News;

public class NewsAdapter extends ArrayAdapter<News> {

	private Context context;
	private List<News> lstNews;
	private int resource;
	
	public NewsAdapter(Context context, int resource, List<News> lstNews) {
		super(context, resource, lstNews);
		this.context = context;
		this.lstNews =  lstNews;
		this.resource = resource;
	}

	@Override
	public int getCount() {
		return lstNews.size();
	}

	@Override
	public News getItem(int position) {
		return position >= getCount() ? null : lstNews.get(position);
	}

	@Override
	public long getItemId(int position) {
		News news = getItem(position);
		return news == null ? -1 : news.hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		NewsHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(resource, null);
			TextView tvNews = (TextView) view.findViewById(R.id.tvNews);
			TextView tvNewsUpdateTime = (TextView) view.findViewById(R.id.tvNewsUpdateTime);
			holder = new NewsHolder();
			holder.tvNews = tvNews;
			holder.tvUpdateTime = tvNewsUpdateTime;
			view.setTag(holder);
		} else {
			holder = (NewsHolder) view.getTag();
		}
		
		News news = getItem(position);
		holder.tvNews.setText(news.getContents());
		holder.tvUpdateTime.setText(news.getUpdateAt());
		return view;
	}
	
	private static class NewsHolder {
		private TextView tvNews;
		private TextView tvUpdateTime;
	}
}
