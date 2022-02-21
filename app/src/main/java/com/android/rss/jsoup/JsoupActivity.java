package com.android.rss.jsoup;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.android.rss.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class JsoupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);

        Content content = new Content(this);
        content.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_item, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        // Get the search view and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); //Do not iconfy the widget; expand it by default

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<ParseItem> newList = new ArrayList<>();
                for (ParseItem parseItem : parseItems) {
                    String title = parseItem.getTitle().toLowerCase();

                    // you can specify as many conditions as you like
                    if (title.contains(newText)) {
                        newList.add(parseItem);
                    }
                }
                // create method in adapter
                adapter.setFilter(newList);

                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);

        return true;

    }

    private static class Content extends AsyncTask<Void, Void, Void> {
        private WeakReference<JsoupActivity> weakReference;

        Content(JsoupActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            JsoupActivity activity = weakReference.get();
            activity.progressBar.setVisibility(View.VISIBLE);
            activity.progressBar.startAnimation(AnimationUtils.loadAnimation(activity.getApplicationContext(), android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            JsoupActivity activity = weakReference.get();
            activity.progressBar.setVisibility(View.GONE);
            activity.progressBar.startAnimation(AnimationUtils.loadAnimation(activity.getApplicationContext(), android.R.anim.fade_out));
            activity.adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "https://www.cinemaqatar.com/";

                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("span.thumbnail");
                int size = data.size();
                Log.d("doc", "doc: " + doc);
                Log.d("data", "data: " + data);
                Log.d("size", "" + size);
                for (int i = 0; i < size; i++) {

                    String imgUrl = data.get(i).getElementsByTag("img").attr("data-src");

                    String title = data.select("h4.gridminfotitle")
                            .select("span")
                            .eq(i)
                            .text();

                    String detailUrl = data.select("h4.gridminfotitle")
                            .select("a")
                            .eq(i)
                            .attr("href");

                    Log.d("*Items", "img: " + imgUrl + " . title: " + title);

                    JsoupActivity activity = weakReference.get();
                    activity.parseItems.add(new ParseItem(imgUrl, title, detailUrl));
                    Log.d("items", "img: " + imgUrl + " . title: " + title);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
