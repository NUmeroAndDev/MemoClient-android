package com.numero.memoclient.MemoApiClient;

public class Memo {
    public long ID;
    public String title;
    public String value;
    public String created;
    public String update;
    public String URLString;

    public Memo(){
    }

    public static Memo init(){
        return new Memo();
    }

    public Memo setID(long ID){
        this.ID = ID;
        return this;
    }

    public Memo setValue(String value){
        this.value = value;
        this.title = value;
        return this;
    }

    public Memo setCreated(String created){
        this.created = created;
        return this;
    }

    public Memo setUpdate(String update){
        this.update = update;
        return this;
    }

    public Memo setURL(String url){
        this.URLString = url;
        return this;
    }
}
