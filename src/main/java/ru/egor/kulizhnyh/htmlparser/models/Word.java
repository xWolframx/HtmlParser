package ru.egor.kulizhnyh.htmlparser.models;

public class Word {
    private long id;
    private String name;
    private int count;
    private long id_site;

    public Word() {}

    public Word(String name, int count, long id_site) {
        this.name = name;
        this.count = count;
        this.id_site = id_site;
    }

    public Word(long id, String name, int count, long id_site) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.id_site = id_site;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSiteId() {
        return id_site;
    }

    public void setSiteId(long id_site) {
        this.id_site = id_site;
    }
}
