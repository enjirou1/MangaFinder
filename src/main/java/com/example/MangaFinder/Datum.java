package com.example.MangaFinder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "mal_id",
        "url",
        "image_url",
        "title",
        "publishing",
        "synopsis",
        "type",
        "chapters",
        "volumes",
        "score",
        "start_date",
        "end_date",
        "members"
})
public class Datum {
    @JsonProperty("mal_id")
    private int id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("title")
    private String title;
    @JsonProperty("publishing")
    private boolean publishing;
    @JsonProperty("synopsis")
    private String synopsis;
    @JsonProperty("type")
    private String type;
    @JsonProperty("chapters")
    private int chapters;
    @JsonProperty("volumes")
    private int volumes;
    @JsonProperty("score")
    private double score;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("members")
    private int members;

    @JsonProperty("mal_id")
    public int getId() {
        return id;
    }

    @JsonProperty("mal_id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("publishing")
    public boolean isPublishing() {
        return publishing;
    }

    @JsonProperty("publishing")
    public void setPublishing(boolean publishing) {
        this.publishing = publishing;
    }

    @JsonProperty("synopsis")
    public String getSynopsis() {
        return synopsis;
    }

    @JsonProperty("synopsis")
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("chapters")
    public int getChapters() {
        return chapters;
    }

    @JsonProperty("chapters")
    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    @JsonProperty("volumes")
    public int getVolumes() {
        return volumes;
    }

    @JsonProperty("volumes")
    public void setVolumes(int volumes) {
        this.volumes = volumes;
    }

    @JsonProperty("score")
    public double getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(double score) {
        this.score = score;
    }

    @JsonProperty("start_date")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("start_date")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("end_date")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("end_date")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("members")
    public int getMembers() {
        return members;
    }

    @JsonProperty("members")
    public void setMembers(int members) {
        this.members = members;
    }
}
