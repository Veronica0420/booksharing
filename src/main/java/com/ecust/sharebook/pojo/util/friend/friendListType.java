package com.ecust.sharebook.pojo.util.friend;

import java.util.List;

public class friendListType implements Comparable<friendListType> {

    private List<friendList> friendLists;

    private String firstChar;

    public  friendListType(String firstChar , List<friendList> friendLists){
        this.firstChar = firstChar;
        this.friendLists = friendLists;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }


    public List<friendList> getFriendLists() {
        return friendLists;
    }

    public void setFriendLists(List<friendList> friendLists) {
        this.friendLists = friendLists;
    }


    @Override
    public int compareTo(friendListType o) {
        if (this.firstChar.compareTo(o.firstChar) > 0)
            return 1;
        else
            return -1;
    }

}
