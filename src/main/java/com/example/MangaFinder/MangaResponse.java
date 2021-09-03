package com.example.MangaFinder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "request_hash",
        "request_cached",
        "request_cache_expiry",
        "results"
})
public class MangaResponse {
    @JsonProperty("request_hash")
    private String requestHash;
    @JsonProperty("request_cached")
    private boolean requestCached;
    @JsonProperty("request_cache_expiry")
    private int requestCacheExpiry;
    @JsonProperty("results")
    private List<Datum> results = null;
    @JsonProperty("last_page")
    private int lastPage;

    @JsonProperty("request_hash")
    public String getRequestHash() {
        return requestHash;
    }

    @JsonProperty("request_hash")
    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    @JsonProperty("request_cached")
    public boolean isRequestCached() {
        return requestCached;
    }

    @JsonProperty("request_cached")
    public void setRequestCached(boolean requestCached) {
        this.requestCached = requestCached;
    }

    @JsonProperty("request_cache_expiry")
    public int getRequestCacheExpiry() {
        return requestCacheExpiry;
    }

    @JsonProperty("request_cache_expiry")
    public void setRequestCacheExpiry(int requestCacheExpiry) {
        this.requestCacheExpiry = requestCacheExpiry;
    }

    @JsonProperty("results")
    public List<Datum> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Datum> results) {
        this.results = results;
    }

    @JsonProperty("last_page")
    public int getLastPage() {
        return lastPage;
    }

    @JsonProperty("last_page")
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}
