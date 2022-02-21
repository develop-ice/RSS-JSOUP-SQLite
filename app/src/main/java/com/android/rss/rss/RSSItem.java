package com.android.rss.rss;


class RSSItem {

    String title;
    String link;
    String description;
    String pubdate;
    String guid;

    RSSItem(String title, String link, String description, String pubdate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubdate = pubdate;
        this.guid = guid;
    }
}
