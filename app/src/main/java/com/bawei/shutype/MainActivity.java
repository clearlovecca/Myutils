package com.bawei.shutype;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bawei.shutype.adapter.ViewPagerAdapter;
import com.bawei.shutype.view.HomeFragment;
import com.bawei.shutype.view.MyFragment;
import com.bawei.shutype.view.ShopFragment;
import com.bawei.shutype.view.TypeFragment;
import com.blankj.utilcode.util.BarUtils;
import com.chaychan.library.BottomBarLayout;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp;
    private BottomBarLayout mainBt;
    List<Fragment> fragments = new ArrayList<>();
    private SlidingTabLayout tab;
    private ViewPager mainFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BarUtils.setStatusBarLightMode(this, true);
        BarUtils.transparentStatusBar(this);

        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new ShopFragment());
        fragments.add(new MyFragment());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(viewPagerAdapter);
        mainBt.setViewPager(vp);
        mainBt.setUnread(1, 101);
        mainBt.showNotify(2);
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.main_fr);
        mainBt = (BottomBarLayout) findViewById(R.id.main_bt);
        tab = (SlidingTabLayout) findViewById(R.id.tab);
        mainFr = (ViewPager) findViewById(R.id.main_fr);
    }
}