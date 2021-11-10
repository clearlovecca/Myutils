package com.bawei.shutype.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bawei.shutype.R;
import com.bawei.shutype.entity.Goods;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<Goods.DataBean, BaseViewHolder> {
    public GoodsAdapter(@Nullable List<Goods.DataBean> data) {
        super(R.layout.goods_item, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Goods.DataBean goods) {
        ImageView imageView = baseViewHolder.getView(R.id.goods_img);
        TextView name = baseViewHolder.getView(R.id.goods_name);
        TextView price = baseViewHolder.getView(R.id.goods_price);
        Glide.with(getContext()).load(goods.getPic()).into(imageView);
        name.setText(""+goods.getTitle());
        price.setText("价格:"+goods.getNum());
    }
}
