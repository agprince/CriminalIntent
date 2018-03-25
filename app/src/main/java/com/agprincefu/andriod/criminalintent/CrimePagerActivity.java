package com.agprincefu.andriod.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.agprincefu.android.criminalintent.crime_id";

    private ViewPager mCrimeViewPager;
    private List<Crime> mCrimes;
    private Button mFristButton;
    private Button mLastButton;

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mCrimeViewPager = findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        final UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mFristButton = findViewById(R.id.Jump_to_first);
        mLastButton = findViewById(R.id.Jump_to_last);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mCrimeViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                Crime crime = mCrimes.get(position);

                CrimeFragment crimeFragment = CrimeFragment.newInstance(crime.getId());

                return crimeFragment;
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mCrimeViewPager.setCurrentItem(i);
                break;
            }
        }

     mCrimeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         }

         @Override
         public void onPageSelected(int position) {
             if(position == 0){
                 mFristButton.setEnabled(false);
             }else{
                 mFristButton.setEnabled(true);
             }
             if(position==mCrimes.size()-1){
                 mLastButton.setEnabled(false);
             }else {
                 mLastButton.setEnabled(true);
             }



         }

         @Override
         public void onPageScrollStateChanged(int state) {

         }
     });

        mFristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    mCrimeViewPager.setCurrentItem(0);

            }

        });
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mCrimeViewPager.setCurrentItem(mCrimes.size() - 1);
            }

        });
    }
}
