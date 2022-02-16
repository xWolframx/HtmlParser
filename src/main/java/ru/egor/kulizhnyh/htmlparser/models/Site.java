package ru.egor.kulizhnyh.htmlparser.models;

public class Site {
    private long id;
    private String url;
    private String domain;
    private String title;
    private int countWords;

    public Site() {}

    public Site(String url, String domain, String title, int countWords) {
        this.url = url;
        this.domain = domain;
        this.title = title;
        this.countWords = countWords;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCountWords() {
        return countWords;
    }

    public void setCountWords(int countWords) {
        this.countWords = countWords;
    }
}
