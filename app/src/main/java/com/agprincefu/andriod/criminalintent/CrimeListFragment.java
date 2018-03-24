package com.agprincefu.andriod.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by agprincefu on 2018/2/2.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.fragment_crime_list,container,false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeAdapter = new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);


        return view;
    }


    private class CrimeHolder extends RecyclerView.ViewHolder{

        private Crime mCrime;
        private TextView mCrimeTitle;
        private TextView mCrimeDate;

       public CrimeHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            mCrimeTitle = itemView.findViewById(R.id.crime_title);
            mCrimeDate = itemView.findViewById(R.id.crime_date);
        }
        public void bind(Crime crime){
           mCrime = crime;
           mCrimeTitle.setText(mCrime.getTitle());
           mCrimeDate.setText(mCrime.getDate().toString());
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            CrimeHolder crimeHolder = new CrimeHolder(inflater,parent);
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
}
