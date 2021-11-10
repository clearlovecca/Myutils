package com.bawei.shutype.view;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bawei.shutype.R;
import com.bawei.shutype.adapter.MyShopsAdapter;
import com.bawei.shutype.dao.DaoMaster;
import com.bawei.shutype.dao.Shop;
import com.bawei.shutype.mvp.view.BaseFragment;
import com.bawei.shutype.pay.AuthResult;
import com.bawei.shutype.pay.PayResult;
import com.bawei.shutype.pay.util.OrderInfoUtil2_0;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopFragment extends BaseFragment {
    private RecyclerView shopRecy;
    private CheckBox shopAll;
    private CheckBox shopFan;
    private TextView shopPrice;
    private Button pay;
    List<Shop> list = new ArrayList<>();
    MyShopsAdapter myShopsAdapter;
    int allprice = 0;
    int payprice;

    @Override
    public int bindLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    public void initView() {

        shopRecy = (RecyclerView) findViewById(R.id.shop_recy);
        shopAll = (CheckBox) findViewById(R.id.shop_all);
        shopFan = (CheckBox) findViewById(R.id.shop_fan);
        shopPrice = (TextView) findViewById(R.id.shop_price);
        pay = (Button) findViewById(R.id.pay);
        shopRecy.setLayoutManager(new LinearLayoutManager(getContext()));

        myShopsAdapter = new MyShopsAdapter(list);
        shopRecy.setAdapter(myShopsAdapter);

        myShopsAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()){
                    case R.id.jia:
                        int num = list.get(position).getNum();
                        num++;
                        list.get(position).setNum(num);
                        myShopsAdapter.notifyDataSetChanged();
                        break;
                    case R.id.jian:
                        int nums = list.get(position).getNum();
                        nums--;
                        if (nums<1){
                            nums = 1;
                        }
                        list.get(position).setNum(nums);
                        myShopsAdapter.notifyDataSetChanged();
                        break;
                    case R.id.myshop_ck:
                        CheckBox checkBox = (CheckBox) view;
                        if (checkBox.isChecked()){
                            list.get(position).setChecked(true);
                            int price = list.get(position).getNum()*list.get(position).getPrice();
                            shopPrice.setText("价格"+price);
                        }
                        else {
                            list.get(position).setChecked(false);
                            shopPrice.setText("价格"+0);
                        }
                        myShopsAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        shopAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setChecked(true);
                        allprice+=list.get(i).getPrice()*list.get(i).getNum();
                    }
                    payprice = allprice;
                    shopPrice.setText("总价"+allprice);
                    allprice=0;
                }
                else {
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setChecked(false);
                    }
                    shopPrice.setText("总价"+0);
                }
                myShopsAdapter.notifyDataSetChanged();
            }
        });

       shopFan.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               for (int i = 0; i < list.size(); i++) {
                   if (list.get(i).getChecked()){
                       list.get(i).setChecked(false);
                   }
                   else {
                       list.get(i).setChecked(true);
                   }
               }
               myShopsAdapter.notifyDataSetChanged();
           }
       });
       pay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
               payV2(payprice);
           }
       });

    }

    @Override
    public void initData() {
        list.clear();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "myshop.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        List<Shop> shops = daoMaster.newSession().loadAll(Shop.class);
        list.addAll(shops);
        myShopsAdapter.notifyDataSetChanged();


    }
    /**
     * 支付宝支付业务示例
     */
    public void payV2(int pr) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            ToastUtils.showLong("ok");
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,pr);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2021000118611330";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCFU1L5Jdh2YZa9CuhY92lXRhCTVQiLoacN2H1XybIOA7pVstWGpn5tXUxyHBSUfMHbHmmG6i4WMm1511+qsSAdATa5dZL8nGQzJ7AwjXN6Iq0bbPBsrB8WrAY2ORnWWF5NTO5weLFtx6WZ2vLm37/DJKdUEVt6EiFOiEu1VvkfPUCffK3wyL591jnv+JKtNuIBOAk6XQCchcjj48zvQcX5ufppchbwd6nPhEsR6vOFbTjmt9TNV78U0Sw6nvGx9nm99Zlt/+aMArAhnnpaSUV+L9mwIhWTqTFplZ6jDq5tRYpkEw72WiijwqA52EUf1/EHQ3zfWPPj94GAcroAb80DAgMBAAECggEAVcEPaFNQVGxWkUc3xSl1pv3Y4XrbITybf4tUq6d+kAcTzdVWA+KJBPzyk56+GffYlICOLYnolLxgr/K4hMiiuCU0trsKDNLmnASWDdSdhtxQ7Yf3aF6ZLHL/F9dIaBa0qRGnd84ZiVvVhw1OGPOnmvOzcJ4tcWnB5IpD7+J+H/nKOMFWoIbo8EPxQWRz1+FR+flFv2xGhmM7LGQN9ofrjsNNRkzReFh46Wv1i03y1D7KBRO/V2pRuIy1B7fUtRWGmm2hg7HwIwW8rqhY+TTjGNhQHbhOyS9Xdm+N/A9RxCSOWxjWeH8bpTKxNSGtFLZcCMocb6ykYWsz2vTd53TLuQKBgQDioOsjjtROonqYPKnhmEgOLY8VgwFH4xmzAjFuq/6yX6CdPBf6s575TV5kew9+UbnQQ3AXH1lX4yuw5nds/swi1gya+IC3RWEdBJD6YhyeRJQRWJh8q0fNBX7SEfXUtcAx/vjerC0552nTKOpOd7Ry/f6P26Tfcu5+mhB85gf3FwKBgQCWmsvF+Qz4MO9Wuc0jfC5Snpdx5jGqY0Iik6TJXVjE6h6JvO6X4RUAAZASNftlQLY080ny8iqh/RGg1W1ym8tstvi3thZ0b9BPH2DpB+xAh8E/dChSNrRiJr6SiNFMTsOGXtoVXZgE2JjSRAqlQaiB2WZq7f3X8/Szf8MOn6bM9QJ/MnWh5Ujn/JowsbTQHmrJoF6EfUWuA1ONZU4Xs4yYKR8ho+C9dhSIUPnpCnZb/MQ1myloy70vJ2JtV/X4oUwaJ6eBJwAtqKDJ/qwFm7o8pQCf9zzCtv9r/To8Aon9Ml9Fa9xZjTrI1xuvdFSn1tDQXMow49cD3ry4XXfwM98D5wKBgD22ir/CET12l+ya0GO1FCpI0Fgc8scWS1ht1b2p4aE3KnJYmBBmbDuHaGj989kGzmMeOnQyJaQhVmLociPfkVkoFPz6wXLPCrYRbp2u4HGE73w39fnSbtI1fCwBono5YEekgIr2wj9iHiUzOoWDRTCITD6O7VDbp68SMHTCmRAtAoGBAN71/mXkmubvG5hjd74SSa3n0tzFJvWwCq0X8lkLnDqL+c5BW87q2dCJcXuSk5OJwTOE6n0KILbq/vHtaKfkV4QBdOzS8AQCDIY26zxauR4+fAhtMeBXjc4bjoHv6/6WRuCUZysBxYbOe7LEkEr1jmsN6SrYAGBRZlyPcAd31ccf";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.showLong("ok");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showLong("ok");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        ToastUtils.showLong("ok");
                    } else {
                        // 其他状态值则为授权失败
                        ToastUtils.showLong("ok");
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


}
