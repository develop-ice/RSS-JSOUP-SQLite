package com.android.rss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.rss.jsoup.JsoupActivity;
import com.android.rss.rss.RSSFeedActivity;
import com.android.rss.sqlite.DataBaseActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> rssLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnRediff).setOnClickListener(this);
        findViewById(R.id.btnJsoup).setOnClickListener(this);
        findViewById(R.id.btnSQLite).setOnClickListener(this);

        rssLinks.add("http://www.rediff.com/rss/moviesreviewsrss.xml");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRediff:
                startActivity(new Intent(MainActivity.this, RSSFeedActivity.class).putExtra("rssLink", rssLinks.get(0)));
                break;
            case R.id.btnJsoup:
                startActivity(new Intent(MainActivity.this, JsoupActivity.class));
                break;
            case R.id.btnSQLite:
                startActivity(new Intent(MainActivity.this, DataBaseActivity.class));
                break;
        }
    }
}
