package com.agprincefu.andriod.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by agprincefu on 2018/1/27.
 */

public class Crime {

    private UUID mId;
    private Date mDate;
    private String mTitle;
    private boolean mSolved;

    public Crime(){
        mId =  UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }


}
