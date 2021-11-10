package com.bawei.shutype.api;

import com.bawei.shutype.entity.Goods;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GoodsApi {
    @GET("/ios/cf/dish_list.php?stage_id=1&limit=20&")
    Observable<Goods> getdata(@Query("page")int page);
}
