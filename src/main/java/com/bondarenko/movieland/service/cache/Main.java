package com.bondarenko.movieland.service.cache;

import com.bondarenko.movieland.entity.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    private final CopyOnWriteArrayList<Genre> genreCache = new CopyOnWriteArrayList<>();
    public List<Genre> getGenres() {
        return new ArrayList(genreCache);
    }




    public static void main(String[] args) {
        List main=new ArrayList();
        main.add("A");
        main.add("1111");
        main.add("A");

        CopyOnWriteArrayList list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        list.addFirst("B");


        list.addAll(main);


        System.out.println(list.retainAll(main));
    }
}
