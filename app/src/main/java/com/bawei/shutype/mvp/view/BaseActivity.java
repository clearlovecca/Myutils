package com.bawei.shutype.mvp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bawei.shutype.mvp.presenter.IPresenter;


public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindLayout());
        initView();
        initData();
    }
}
