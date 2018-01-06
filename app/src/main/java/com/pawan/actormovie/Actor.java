package com.pawan.actormovie;

/**
 * Created by PAWAN on 5/29/2017.
 */

class Actor {
    String aid;
    String aName;
    String movie;
    public Actor() {
    }



    public Actor(String aName, String aid, String movie) {
        this.aName = aName;
        this.aid = aid;
        this.movie = movie;

    }

    public String getaid() {
        return aid;
    }

    public String getaName() {
        return aName;
    }

    public String getMovie() {
        return movie;
    }

}
