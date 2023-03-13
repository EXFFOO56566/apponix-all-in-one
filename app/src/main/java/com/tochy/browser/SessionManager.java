package com.tochy.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.tochy.browser.Model.AdvertisementRoot;
import com.tochy.browser.Model.AppItem;
import com.tochy.browser.retrofit.Const;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    public static final String KEY_APP_LANGUAGE = "en";
    private static final String PREFER_NAME = "AppLangPref";
    Context _context;
    int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.pref = context.getSharedPreferences(Const.PREF_NAME, MODE_PRIVATE);
        this.editor = this.pref.edit();
    }


    public void saveBooleanValue(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        return pref.getBoolean(key, false);
    }

    public void saveStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return pref.getString(key, "");
    }

    public void AppLangSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLanguage(String lang) {
        editor.putString(KEY_APP_LANGUAGE, lang);
        editor.commit();
    }


    public void saveAds(AdvertisementRoot ads) {
        editor.putString(Const.ADS, new Gson().toJson(ads));
        editor.apply();
    }

    public AdvertisementRoot getAdsKeys() {
        String userString = pref.getString(Const.ADS, "");
        if (userString != null && !userString.isEmpty()) {
            return new Gson().fromJson(userString, AdvertisementRoot.class);
        }
        return null;
    }


    public int toogleFav(AppItem a) {
        String app = new Gson().toJson(a);
        ArrayList<String> fav = getFAv();
        if (fav != null) {
            if (fav.contains(app)) {
                fav.remove(app);
                editor.putString(Const.FAV, new Gson().toJson(fav));
                editor.apply();
                Log.d("TAG", "toogleFav:  removed " + app.toString());
                return 0;
            } else {
                fav.remove(app);
                fav.add(app);
                editor.putString(Const.FAV, new Gson().toJson(fav));
                editor.apply();
                Log.d("TAG", "toogleFav:  added " + app.toString());
                return 1;
            }
        } else {
            fav = new ArrayList<>();
            fav.add(app);
            editor.putString(Const.FAV, new Gson().toJson(fav));
            Log.d("TAG", "toogleFav:  created ff  " + app.toString());
            editor.apply();
            return 1;
        }

    }

    public ArrayList<String> getFAv() {
        String userString = pref.getString(Const.FAV, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }


    public int toggleBookmark(String id) {
        ArrayList<String> fav = getBookamrks();
        if (fav != null) {
            if (fav.contains(id)) {
                fav.remove(id);
                editor.putString(Const.BOOKMARK, new Gson().toJson(fav));
                editor.apply();
                return 0;
            } else {
                fav.add(id);
                editor.putString(Const.BOOKMARK, new Gson().toJson(fav));
                editor.apply();
                return 1;
            }
        } else {
            fav = new ArrayList<>();
            fav.add(id);
            editor.putString(Const.BOOKMARK, new Gson().toJson(fav));
            editor.apply();
            return 1;
        }

    }

    public ArrayList<String> getBookamrks() {
        String userString = pref.getString(Const.BOOKMARK, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    public void addToHistory(String id) {
        ArrayList<String> fav = getHistory();
        if (fav != null) {

            fav.add(id);

        } else {
            fav = new ArrayList<>();
            fav.add(id);

        }
        editor.putString(Const.HISTORY, new Gson().toJson(fav));
        editor.apply();
    }

    public void removefromHistory(String id) {
        ArrayList<String> fav = getHistory();
        if (fav != null) {
            if (fav.contains(id)) {
                fav.remove(id);
                Toast.makeText(_context, "Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(_context, "Bookmarked", Toast.LENGTH_SHORT).show();
            }
        } else {
            fav = new ArrayList<>();

        }
        editor.putString(Const.HISTORY, new Gson().toJson(fav));
        editor.apply();
    }

    public ArrayList<String> getHistory() {
        String userString = pref.getString(Const.HISTORY, "");
        if (!userString.isEmpty()) {
            return new Gson().fromJson(userString, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }


}
