package com.agprincefu.andriod.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUi();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    private void updateUi() {
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(CrimeLab.get(getActivity()).getCrimes());
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
        }

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
            Intent startCrime = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
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
}
