package com.alimseolap.model;

public class NounDTO {

    public int id;
    public String noun;
    public int weight;
    public int views;

    public NounDTO(int id, String noun, int weight, int views) {
        this.id = id;
        this.noun = noun;
        this.weight = weight;
        this.views = views;
    }

}
