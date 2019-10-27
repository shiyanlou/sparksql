package com.shiyanlou.shinelon.Domain;

public class MovieRate {
    private Integer movieId;
    private Integer rate;
    public Integer getMovieId() {
        return movieId;
    }
    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
    public Integer getRate() {
        return rate;
    }
    public void setRate(Integer rate) {
        this.rate = rate;
    }
    @Override
    public String toString() {
        return "MovieRate [movieId=" + movieId + ", rate=" + rate + "]";
    }


}
