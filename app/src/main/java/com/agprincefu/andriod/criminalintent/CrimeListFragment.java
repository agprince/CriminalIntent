package com.agprincefu.andriod.criminalintent;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

/**
 * Created by agprincefu on 2018/2/2.
 */

public class CrimeListFragment extends Fragment {
    private static final String TAG = "agtest";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private LinearLayout mLinearLayout ;
    private Button mAddCrimeButton;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mLinearLayout = view.findViewById(R.id.add_crime_view);
        mAddCrimeButton = view.findViewById(R.id.add_crime_button);


        updateUi();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    private void updateUi() {

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(crimes.size()==0){
            mLinearLayout.setVisibility(View.VISIBLE);
            mAddCrimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addCrime();
                }
            });
        }else {
            mLinearLayout.setVisibility(View.GONE);
        }


        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitile();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem menuItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            menuItem.setTitle(R.string.hide_subtitle);
        }else{
            menuItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_crime:
                addCrime();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;

                getActivity().invalidateOptionsMenu();
                updateSubtitile();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void addCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private TextView mCrimeTitle;
        private TextView mCrimeDate;
        private ImageView mCrimeSolved;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mCrimeTitle = itemView.findViewById(R.id.crime_title);
            mCrimeDate = itemView.findViewById(R.id.crime_date);
            mCrimeSolved = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mCrimeTitle.setText(mCrime.getTitle());
            mCrimeDate.setText(mCrime.getDate().toString());
            mCrimeSolved.setVisibility(mCrime.isSolved() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(),mCrime.getTitle(),Toast.LENGTH_SHORT).show();
            //Intent startCrime = new Intent(getActivity(),CrimeActivity.class);
            //Intent startCrime = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            Intent startCrime = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(startCrime);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            CrimeHolder crimeHolder = new CrimeHolder(inflater, parent);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            holder.bind(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {

            return mCrimes.size();

        }
    }

    private void updateSubtitile(){
        int countCrime = CrimeLab.get(getActivity()).getCrimes().size();

        String subTitle = getResources().getQuantityString(R.plurals.subtitle_plurals,countCrime,countCrime,countCrime);

        Log.d(TAG,"subtilte: "+subTitle);

        if(!mSubtitleVisible){
            subTitle =null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        activity.getSupportActionBar().setSubtitle(subTitle);
    }
}
