package com.epam.advent_hotel.apartments;

import java.util.Locale;

/**
 * Created by Elizaveta Kapitonova on 01.02.16.
 */
public class LocalAptDescription {
    private int textNumber;
    private Locale locale;
    private String text;

    public int getTextNumber() {
        return textNumber;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
