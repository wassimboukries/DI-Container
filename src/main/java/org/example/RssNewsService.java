package org.example;

import org.example.annotation.Autowired;

public class RssNewsService implements NewsService {
    @Autowired
    HttpService httpService;

    @Override
    public HttpService getHttpService() {
        return httpService;
    }
}
