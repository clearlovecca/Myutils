package com.bawei.shutype.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.shutype.R;
import com.bawei.shutype.dao.Shop;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyShopsAdapter extends BaseQuickAdapter<Shop, BaseViewHolder> {


    public MyShopsAdapter(@Nullable List<Shop> data) {
        super(R.layout.shop_item, data);
        addChildClickViewIds(R.id.myshop_ck);
        addChildClickViewIds(R.id.jia);
        addChildClickViewIds(R.id.jian);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Shop shop) {
        Glide.with(getContext()).load(shop.getImg()).into((ImageView) baseViewHolder.getView(R.id.myshop_pic));
        baseViewHolder.setText(R.id.myshop_title,shop.getName());
        baseViewHolder.setText(R.id.myshop_price,""+shop.getPrice());
        baseViewHolder.setText(R.id.myshops_num,""+shop.getNum());
        CheckBox checkBox = baseViewHolder.getView(R.id.myshop_ck);
        checkBox.setChecked(shop.getChecked());
    }

}
