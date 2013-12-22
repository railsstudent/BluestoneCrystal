package com.blueskyconnie.bluestonecrystal.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabspagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> lstFragment;
	
	public TabspagerAdapter(FragmentManager fm, List<Fragment> lstFragment) {
		super(fm);
		this.lstFragment = lstFragment;
	}

	@Override
	public Fragment getItem(int index) {
		if (index < getCount())  {
			return lstFragment.get(index);
		}
		return null;
	}

	@Override
	public int getCount() {
		return lstFragment.size();
	}

}
