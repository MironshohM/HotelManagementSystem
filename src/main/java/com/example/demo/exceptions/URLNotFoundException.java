package com.example.demo.exceptions;

public class URLNotFoundException extends Exception {

    public URLNotFoundException(String urlNotFound) {
        super(urlNotFound);
    }
}
