package com.semicolon.spring.entity.feed;

import com.semicolon.spring.entity.feed.feed_medium.FeedMedium;

import java.util.Comparator;

public class MediaComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        int medium1 = ((FeedMedium)o1).getId();
        int medium2 = ((FeedMedium)o2).getId();

        if(medium1 > medium2){
            return 1;
        }else if(medium1 < medium2){
            return -1;
        }else{
            return 0;
        }
    }
}
