package com.client.vpman.weatherwall.CustomeUsefullClass;

public class RandomQuotes
{
    public String getQuotes() {
        return quotes;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String quotes,author;

    public RandomQuotes(String quotes, String author) {
        this.quotes = quotes;
        this.author = author;
    }

}
