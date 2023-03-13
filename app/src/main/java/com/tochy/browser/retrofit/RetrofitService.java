package com.tochy.browser.retrofit;

import com.tochy.browser.Model.AdvertisementRoot;
import com.tochy.browser.Model.AppsModel;
import com.tochy.browser.Model.CatagoryAppModel;
import com.tochy.browser.Model.NewsApiRoot;
import com.tochy.browser.Model.PaperRoot;
import com.tochy.browser.Model.ProductKRoot;
import com.tochy.browser.Model.RingToneRoot;
import com.tochy.browser.Model.TopAppRoot;
import com.tochy.browser.Model.WallpaperRoot;
import com.tochy.browser.Model.WetherApiRoot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/data/2.5/weather")
    Call<WetherApiRoot> getWetherApi(@Query("q") String City_Name, @Query("appid") String Appid, @Query("units") String Metrics);

    @GET("/apponix/api/categoryData")
    Call<CatagoryAppModel> getCatagory();

    @GET("/apponix/api/topApp")
    Call<TopAppRoot> gettopApp();

    @FormUrlEncoded
    @POST("/apponix/api/searchCategory")
    Call<AppsModel> getApps(@Field("category_id") String catid);

    @GET("/apponix/api/WallpaperList")
    Call<WallpaperRoot> getWallpaper();

    @GET("/apponix/api/RingtoneList")
    Call<RingToneRoot> getRingTone();

    @GET("/v1/search")
    Call<NewsApiRoot> getNews(@Query("q") String contry, @Query("lang") String lanuage, @Header("x-rapidapi-key") String hederkey, @Query("page") int page);

    @GET("/apponix/api/showAllAdd")
    Call<AdvertisementRoot> getAds();

    @GET("/apponix/api/ShowAdmin")
    Call<PaperRoot> getPapers();

    @GET("/api/clientpackage")
    Call<ProductKRoot> getProducts(@Query("key") String key);

}
