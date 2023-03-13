package com.tochy.browser.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tochy.browser.Activity.browser.WebActivity;
import com.tochy.browser.Adaptor.AppsAdaptor;
import com.tochy.browser.Adaptor.NewsAdaptor;
import com.tochy.browser.AppsAddActivity;
import com.tochy.browser.LocationUtils.GpsTracker;
import com.tochy.browser.Model.AppItem;
import com.tochy.browser.Model.NewsApiRoot;
import com.tochy.browser.Model.TopAppRoot;
import com.tochy.browser.Model.WetherApiRoot;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.FragmentHomeFregmentBinding;
import com.tochy.browser.retrofit.Const;
import com.tochy.browser.retrofit.RetrofitBuilder;
import com.tochy.browser.retrofit.RetrofitBuilder2;
import com.tochy.browser.retrofit.RetrofitBuilder3;
import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;


public class HomeFregment extends Fragment implements Listener, LocationData.AddressCallBack {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int MY_REQUEST_ID = 1001;
    private static final String TAG = "tttttttttttttttt";
    FragmentHomeFregmentBinding binding;
    List<AppItem> appsDataModels = new ArrayList<>();
    SessionManager sessionManager;
    private GetLocationDetail getLocationDetail;
    private EasyWayLocation easyWayLocation;
    private List<TopAppRoot.DataItem> data = new ArrayList<>();
    private LocationData location;
    private NewsAdaptor adaptornews = new NewsAdaptor();
    private int page = 1;
    private boolean isAllPost = true;
    AlertDialog.Builder builder;
    private List<AppItem> tempList;
    private List<AppItem> favourites;
    private boolean isLoding = false;


    public HomeFregment() {
        // Required empty public constructor
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {
            Log.d(TAG, "removeDuplicates: stp 1");
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                Log.d(TAG, "removeDuplicates:  yeeh ");
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public boolean permissionIsGranted() {

        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;

    }

    private void doLocationWork() {
        //
    }

    private void openGoogleActivity() {
        binding.rlgoogle.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_fregment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        return binding.getRoot();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.shimmer.setVisibility(View.VISIBLE);
        binding.rvapps.setVisibility(View.GONE);

        builder = new AlertDialog.Builder(getActivity());


        initmain();

        sessionManager = new SessionManager(getActivity());
        if (permissionIsGranted()) {
            doLocationWork();
        }


        getLocationDetail = new GetLocationDetail(this, getActivity());
        easyWayLocation = new EasyWayLocation(getActivity(), false, true, this);


        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "onActivityCreated: permisson  not");
            return;
        }
        Log.d(TAG, "onActivityCreated: " + tm.getSimOperator());
        Log.d(TAG, "onActivityCreated: " + tm.getSimOperator());


    }

    private void setnews() {


        binding.nested.getViewTreeObserver().addOnScrollChangedListener((ViewTreeObserver.OnScrollChangedListener) () -> {
            View view = (View) binding.nested.getChildAt(binding.nested.getChildCount() - 1);

            int diff = (view.getBottom() - (binding.nested.getHeight() + binding.nested
                    .getScrollY()));

            if (diff == 0) {
                if (isAllPost) {
                    binding.pd.setVisibility(View.VISIBLE);
                    page++;
                    getNews(page);
                }
                isLoding = true;

            }
        });


        binding.rvnews.setAdapter(adaptornews);


    }


    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            Log.d(TAG, "getMyLocation: no loc");
            return;
        }
        GpsTracker gpsTracker = new GpsTracker(getActivity());
        Log.d(TAG, "getMyLocation: " + gpsTracker.toString());
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Log.d(TAG, "getMyLocation:======= " + latitude);
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);
                if (address.isEmpty()) return;
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }
            } catch (IOException e) {
                Log.d(TAG, "getMyLocation: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.d(TAG, "getMyLocation: " + e.getMessage());
            }

        } else {
            gpsTracker.showSettingsAlert();
        }

    }


    private void getWetherData(String city) {
        Call<WetherApiRoot> call = RetrofitBuilder2.create().getWetherApi(city, Const.WetherApi, "metric");
        call.enqueue(new Callback<WetherApiRoot>() {
            @Override
            public void onResponse(Call<WetherApiRoot> call, Response<WetherApiRoot> response) {

                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getMain() != null && response.body().getWeather() != null) {
                        binding.llWether.setVisibility(View.VISIBLE);
                        setdata(response.body().getMain(), response.body().getWeather(), city);
                        Log.d(TAG, "onResponse:" + response.body().getMain().getTemp());


                    }

                }


            }

            @Override
            public void onFailure(Call<WetherApiRoot> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void initmain() {
        getLocation();
        openGoogleActivity();
        gettopapp();
    }

    private void setdata(WetherApiRoot.Main main, List<WetherApiRoot.WeatherItem> weather, String city) {
        binding.city.setText(city);
        binding.tem.setText(Integer.toString(main.getTemp()));
        binding.cloud.setText(weather.get(0).getDescription());
    }

    private void gettopapp() {
        Call<TopAppRoot> call = RetrofitBuilder.create().gettopApp();
        call.enqueue(new Callback<TopAppRoot>() {
            @Override
            public void onResponse(Call<TopAppRoot> call, Response<TopAppRoot> response) {
                if (response.code() == 200 && response.body() != null) {
                    data = response.body().getData();
                    binding.shimmer.setVisibility(View.GONE);
                    binding.rvapps.setVisibility(View.VISIBLE);
                    getData();


                }


            }

            @Override
            public void onFailure(Call<TopAppRoot> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    private void getData() {

        favourites = new ArrayList<>();
        ArrayList<String> tempfav = sessionManager.getFAv();
        for (int i = 0; i < tempfav.size(); i++) {
            Log.d(TAG, "getData: '" + tempfav.get(i));
            favourites.add(new Gson().fromJson(tempfav.get(i), AppItem.class));
            Log.d(TAG, "getData: app " + favourites.get(i).getAppName());
        }

        tempList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            TopAppRoot.DataItem d = data.get(i);
            AppItem appItem = new AppItem(d.getAppIcon(), d.getAppName(), d.getAppUrl());
            tempList.add(appItem);
        }


        for (AppItem app : favourites) {

            for (int i = 0; i < tempList.size(); i++) {

                if (app.getAppName().equalsIgnoreCase(tempList.get(i).getAppName())) {
                    Log.d(TAG, "getData: removed---------------------- " + app.getAppName());
                    tempList.remove(i);
                }
            }

        }
        Log.d(TAG, "getData: ___________________________________________________________________________");
        favourites.addAll(tempList);

        favourites.add(favourites.size(), null);
        AppsAdaptor appsAdaptor = new AppsAdaptor(favourites);
        binding.rvapps.setAdapter(appsAdaptor);
        appsAdaptor.setMainAppsClickListner(new AppsAdaptor.MainAppsClickListner() {
            @Override
            public void onAddClick() {
                startActivityForResult(
                        new Intent(getActivity(), AppsAddActivity.class),
                        MY_REQUEST_ID);
            }

            @Override
            public void onAppLongclick(AppItem s, int position) {
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {

                            sessionManager.toogleFav(s);
                            appsAdaptor.removeOneItem(position);


                            Toast.makeText(getActivity().getApplicationContext(), "Removed",
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Removed Apps");
                alert.show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_ID && resultCode == RESULT_OK) {
            String ss = data.getStringExtra("title");

            AppItem appsDataModel = new Gson().fromJson(ss, AppItem.class);
            appsDataModels.add(appsDataModels.size() - 1, appsDataModel);
            getData();

        }
        if (requestCode == LOCATION_SETTING_REQUEST_CODE) {
            easyWayLocation.onActivityResult(resultCode);
        }
    }


    @Override
    public void locationOn() {
        Toast.makeText(getActivity(), "Location On", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(Location location) {
        StringBuilder data = new StringBuilder();
        data.append(location.getLatitude());
        data.append(" , ");
        data.append(location.getLongitude());
        Log.d(TAG, "currentLocation: " + data);
        getLocationDetail.getAddress(location.getLatitude(), location.getLongitude(), "xyz");
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(getActivity(), "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void locationData(LocationData locationData) {


        location = locationData;
        setnews();
        getNews(page);
        binding.city.setText(locationData.getCity());
        getWetherData(locationData.getCity());
        Log.d(TAG, "locationData: " + locationData.getCity());

    }

    private void getNews(int page) {
        Call<NewsApiRoot> call = RetrofitBuilder3.create().getNews(location.getCountry(), Const.language, Const.HederKey, page);
        call.enqueue(new Callback<NewsApiRoot>() {
            @Override
            public void onResponse(Call<NewsApiRoot> call, Response<NewsApiRoot> response) {
                if (response.code() == 200 && response.body().getArticles() != null && !response.body().getArticles().isEmpty()) {
                    Log.d(TAG, "onResponse: =======" + response.body().getArticles().size());
                    isLoding = false;
                    adaptornews.addata(response.body().getArticles());
                    isAllPost = true;

                    binding.pd.setVisibility(View.GONE);


                }
            }

            @Override
            public void onFailure(Call<NewsApiRoot> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        initmain();

    }
}