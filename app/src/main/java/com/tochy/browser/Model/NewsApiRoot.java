package com.tochy.browser.Model;

import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class NewsApiRoot {

    @SerializedName("total_hits")
    private int totalHits;

    @SerializedName("user_input")
    private UserInput userInput;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("articles")
    private List<ArticlesItem> articles;

    @SerializedName("status")
    private String status;

    @SerializedName("page_size")
    private int pageSize;

    public int getTotalHits() {
        return totalHits;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<ArticlesItem> getArticles() {
        return articles;
    }

    public String getStatus() {
        return status;
    }

    public int getPageSize() {
        return pageSize;
    }

    public static class ArticlesItem {

        @SerializedName("summary")
        private String summary;

        @SerializedName("country")
        private String country;

        @SerializedName("author")
        private String author;

        @SerializedName("link")
        private String link;

        @SerializedName("language")
        private String language;

        @SerializedName("media")
        private String media;

        @SerializedName("title")
        private String title;

        @SerializedName("_score")
        private double score;

        @SerializedName("clean_url")
        private String cleanUrl;

        @SerializedName("published_date_precision")
        private String publishedDatePrecision;

        @SerializedName("rights")
        private String rights;

        @SerializedName("is_opinion")
        private boolean isOpinion;

        @SerializedName("rank")
        private int rank;

        @SerializedName("topic")
        private String topic;

        @SerializedName("twitter_account")
        private String twitterAccount;

        @SerializedName("_id")
        private String id;

        @SerializedName("published_date")
        private String publishedDate;

        @SerializedName("authors")
        private List<String> authors;

        public String getSummary() {
            return summary;
        }

        public String getCountry() {
            return country;
        }

        public String getAuthor() {
            return author;
        }

        public String getLink() {
            return link;
        }

        public String getLanguage() {
            return language;
        }

        public String getMedia() {
            return media;
        }

        public String getTitle() {
            return title;
        }

        public double getScore() {
            return score;
        }

        public String getCleanUrl() {
            return cleanUrl;
        }

        public String getPublishedDatePrecision() {
            return publishedDatePrecision;
        }

        public String getRights() {
            return rights;
        }

        public boolean isIsOpinion() {
            return isOpinion;
        }

        public int getRank() {
            return rank;
        }

        public String getTopic() {
            return topic;
        }

        public String getTwitterAccount() {
            return twitterAccount;
        }

        public String getId() {
            return id;
        }

        public String getPublishedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                long time = sdf.parse(publishedDate).getTime();
                long now = System.currentTimeMillis();
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
                Log.d(TAG, "getDate: " + ago);
                return ago.toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return publishedDate;
        }

        public List<String> getAuthors() {
            return authors;
        }
    }

    public static class UserInput {

        @SerializedName("q")
        private String Q;

        @SerializedName("size")
        private int size;

        @SerializedName("from")
        private String from;

        @SerializedName("sort_by")
        private String sortBy;

        @SerializedName("page")
        private int page;

        @SerializedName("lang")
        private String lang;

        public String getQ() {
            return Q;
        }

        public int getSize() {
            return size;
        }

        public String getFrom() {
            return from;
        }

        public String getSortBy() {
            return sortBy;
        }

        public int getPage() {
            return page;
        }

        public String getLang() {
            return lang;
        }
    }
}