package com.ecust.sharebook.pojo.util.friend;

import com.ecust.sharebook.pojo.FriendInf;

import java.util.List;

public class friendListType implements Comparable<friendListType> {

    private List<FriendInf> friendLists;

    private String firstChar;

    public  friendListType(String firstChar , List<FriendInf> friendLists){
        this.firstChar = firstChar;
        this.friendLists = friendLists;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }


    public List<FriendInf> getFriendLists() {
        return friendLists;
    }

    public void setFriendLists(List<FriendInf> friendLists) {
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
