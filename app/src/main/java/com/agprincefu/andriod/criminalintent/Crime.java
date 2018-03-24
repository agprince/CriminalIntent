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



    private boolean mPolice;

    public Crime(){
        mId =  UUID.randomUUID();
        mDate = new Date();
        mPolice =false;
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
    public boolean isPolice() {
        return mPolice;
    }

    public void setPolice(boolean police) {
        mPolice = police;
    }
}
