package com.agprincefu.andriod.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/**
 * Created by agprincefu on 2018/2/1.
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    private Hashtable<UUID,Crime> mCrimeHash;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        mCrimeHash = new Hashtable<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime # " + i);
            crime.setSolved(i % 2 == 0);
            mCrimeHash.put(crime.getId(),crime);
            mCrimes.add(crime);
        }

    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;

    }
    public Crime getCrimeQuick(UUID id){
        return mCrimeHash.get(id);
    }


    public List<Crime> getCrimes() {
        return mCrimes;
    }


}
