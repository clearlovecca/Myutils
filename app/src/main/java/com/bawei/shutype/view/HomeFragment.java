package com.bawei.shutype.view;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawei.shutype.R;
import com.bawei.shutype.adapter.GoodsAdapter;
import com.bawei.shutype.adapter.ShopsAdapter;
import com.bawei.shutype.api.GoodsApi;
import com.bawei.shutype.dao.DaoMaster;
import com.bawei.shutype.dao.Shop;
import com.bawei.shutype.entity.Goods;
import com.bawei.shutype.mvp.view.BaseFragment;
import com.blankj.utilcode.util.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends BaseFragment {
    int nums=1;
    int page = 1;
     ImageView homeSao;
     EditText homeEt;
     ImageView homeSearch;
     Banner homeBanner;
     SmartRefreshLayout homeSmart;
     RecyclerView homeRecy;
     List<Integer> img = new ArrayList<>();
     List<Goods.DataBean> list = new ArrayList<>();
     List<Shop> list1  = new ArrayList<>();
    ShopsAdapter shopsAdapter;
    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        homeSao = (ImageView) findViewById(R.id.home_Sao);
        homeEt = (EditText) findViewById(R.id.home_et);
        homeSearch = (ImageView) findViewById(R.id.home_search);
        homeBanner = (Banner) findViewById(R.id.home_banner);
        homeSmart = (SmartRefreshLayout) findViewById(R.id.home_smart);
        homeRecy = (RecyclerView) findViewById(R.id.home_recy);
        setbanner();
        show();
        homeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "shop.db");
                DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
                List<Shop> shops = daoMaster.newSession().queryRaw(Shop.class, "where name like ?", new String("%" + homeEt.getText().toString() + "%"));
                list1.clear();
                System.out.println(shops.size());
                list1.addAll(shops);
                shopsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setbanner() {
        img.add(R.drawable.p1);
        img.add(R.drawable.p2);
        img.add(R.drawable.sai);
        homeBanner.setAdapter(new BannerImageAdapter<Integer>(img){
            @Override
            public void onBindView(BannerImageHolder holder, Integer data, int position, int size) {
                    holder.imageView.setImageResource(data);
            }
        })
                .setIndicator(new CircleIndicator(getContext()))
                .setIndicatorNormalColor(Color.BLACK)
                .setIndicatorSelectedColor(Color.WHITE);
    }
    private void show(){
        if (NetworkUtils.isAvailableByPing()){
            Observable<Goods> observable = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://www.qubaobei.com")
                    .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build().create(GoodsApi.class).getdata(page);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Goods>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull Goods goods) {
                            Toast.makeText(getContext(), "有网", Toast.LENGTH_SHORT).show();
                            list.addAll(goods.getData());
                            homeRecy.setLayoutManager(new LinearLayoutManager(getContext()));
                            GoodsAdapter goodsAdapter = new GoodsAdapter(list);
                            homeRecy.setAdapter(goodsAdapter);
                            goodsAdapter.notifyDataSetChanged();
//                            if (nums==1){
//                                DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "shop.db");
//                                DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
//                                for (int i = 0; i < list.size(); i++) {
//                                    daoMaster.newSession().insert(new Shop(null,list.get(i).getTitle(),list.get(i).getPic(),list.get(i).getNum(),false,0));
//                                }
//                            }
//                            nums++;

                            goodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(@androidx.annotation.NonNull BaseQuickAdapter<?, ?> adapter, @androidx.annotation.NonNull View view, int position) {
                                    DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "myshop.db");
                                    DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
                                    daoMaster.newSession().insert(new Shop(null,list.get(position).getTitle(),list.get(position).getPic(),list.get(position).getNum(),false,1));
                                }
                            });

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
        else {
            Toast.makeText(getContext(), "没网", Toast.LENGTH_SHORT).show();
            DaoMaster.DevOpenHelper devOpenHelper2 = new DaoMaster.DevOpenHelper(getContext(), "shop.db");
            DaoMaster daoMaster2 = new DaoMaster(devOpenHelper2.getWritableDb());
//            daoMaster2.newSession().insert(new Food(null));
            list1.clear();
            List<Shop> shops = daoMaster2.newSession().loadAll(Shop.class);
            list1.addAll(shops);
            homeRecy.setLayoutManager(new LinearLayoutManager(getContext()));
            shopsAdapter = new ShopsAdapter(list1);
            homeRecy.setAdapter(shopsAdapter);

        }

    }

    @Override
    public void initData() {

    }
}
