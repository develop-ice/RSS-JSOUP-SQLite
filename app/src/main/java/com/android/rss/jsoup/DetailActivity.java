package com.android.rss.jsoup;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rss.R;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTExtView, detailTextView;
    private String detailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);
        titleTExtView = findViewById(R.id.textView);
        detailTextView = findViewById(R.id.detailTextView);

        titleTExtView.setText(getIntent().getStringExtra("title"));
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(imageView);
        Content content = new Content();
        content.execute();
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            detailTextView.setText(detailString);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String baseUrl = "https://www.cinemaqatar.com/";
                String detailUrl = getIntent().getStringExtra("detailUrl");

                String url = baseUrl + detailUrl;

                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("div.detailinfo");

                detailString = data.select("div.detailinfo")
                        .text();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
