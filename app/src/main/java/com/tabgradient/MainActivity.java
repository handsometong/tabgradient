package com.tabgradient;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity implements MyFragment.OnFragmentInteractionListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab1)
    tabView tab1;
    @BindView(R.id.tab2)
    tabView tab2;
    @BindView(R.id.tab3)
    tabView tab3;
    @BindView(R.id.tab4)
    tabView tab4;
    private List<Fragment> fragments;
    private List<tabView> tabViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();

    }

    private void initFragment() {
        tabViews=new ArrayList<tabView>();
        tabViews.add(tab1);
        tabViews.add(tab2);
        tabViews.add(tab3);
        tabViews.add(tab4);
        tab1.setOnClickListener(new SelectOnClickListener(0));
        tab2.setOnClickListener(new SelectOnClickListener(1));
        tab3.setOnClickListener(new SelectOnClickListener(2));
        tab4.setOnClickListener(new SelectOnClickListener(3));
        fragments = new ArrayList<Fragment>();
        for (int i = 0; i < 4; i++) {
            MyFragment fragment = new MyFragment();
            fragments.add(fragment);
        }
        viewPager.setAdapter(new SelectFragmentPagerAdapter(
                getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new SelectOnPageChangeListener());


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SelectFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public SelectFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

    }
    private class SelectOnClickListener implements View.OnClickListener {
        private int index = 0;

        public SelectOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            viewPager.setCurrentItem(index);
            changeTabViewColor(index);
        }

    }

    private void changeTabViewColor(int index) {
        tab1.setDirection(0);
        tab1.setProgress(((index == 0) ? 1.0f : 0));
        tab2.setDirection(0);
        tab2.setProgress(((index == 1) ? 1.0f : 0));
        tab3.setDirection(0);
        tab3.setProgress(((index == 2) ? 1.0f : 0));
        tab4.setDirection(0);
        tab4.setProgress(((index == 3) ? 1.0f : 0));


    }

    public class SelectOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private boolean  checkIfScroll = false;

        public void onPageScrollStateChanged(int arg0) {

            if(arg0 == 1){
                checkIfScroll = true;
            } else if (arg0 == 0){
                checkIfScroll = false;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if(checkIfScroll){
                if (positionOffset > 0) {
                   tabView left = tabViews.get(position);
                    tabView right = tabViews.get(position + 1);

                    left.setDirection(1);
                    right.setDirection(0);

                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
                }
            }
        }

        public void onPageSelected(int arg0) {

        }

    }

}
