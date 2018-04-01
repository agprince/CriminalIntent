package com.agprincefu.andriod.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

/**
 * Created by agprincefu on 2018/1/27.
 */

public class CrimeFragment extends Fragment {

    private static final String TAG = "agtest";

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_FRAGMENT_TAG = " date_fragment";

    private static final int REQUEST_CODE_DATE = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallButton;

    public static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, id);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mDateButton = v.findViewById(R.id.crime_date);
        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSuspectButton = v.findViewById(R.id.crime_suspect);
        mReportButton = v.findViewById(R.id.crime_report);
        mCallButton = v.findViewById(R.id.crime_call);

        mTitleField.setText(mCrime.getTitle());
        mSolvedCheckBox.setChecked(mCrime.isSolved());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
                FragmentManager fragmentManager = getFragmentManager();
                fragment.setTargetFragment(CrimeFragment.this, REQUEST_CODE_DATE);
                fragment.show(fragmentManager, DATE_FRAGMENT_TAG);


            }
        });

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(true);
            }
        });

        final Intent pickIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
       // pickIntent.addCategory(Intent.CATEGORY_HOME);

        PackageManager manager = getActivity().getPackageManager();
        if(manager.resolveActivity(pickIntent,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickIntent,REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder sb = ShareCompat.IntentBuilder.from(getActivity());
                sb.setText(getCrimeReport()).setSubject(getString(R.string.send_report)).setType("text/plain").startChooser();

               startActivity(sb.getIntent());




               // Intent intent = new Intent(Intent.ACTION_SEND);
               // intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                //intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_suspect));
                //intent = Intent.createChooser(intent,getString(R.string.send_report));
                //startActivity(intent);
            }
        });


        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分两步，第一步获得电话号码，第二步，用隐私的intent拨打电话

                String name = mCrime.getSuspect();

                if(name==null||"".equals(name)){
                    Toast.makeText(getActivity(),"no suspect, so no phone namm to call",Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneNumber = getNumber(name);
                Log.d(TAG, "phoneNumber: "+phoneNumber);
                Uri uri = Uri.parse("tel:"+phoneNumber);
                Log.d(TAG,"Uri : "+uri.toString());
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);


            }
        });


        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }else if(requestCode == REQUEST_CONTACT){
            Uri contactUri = data.getData();
            String [] qureyFileds = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c = getActivity().getContentResolver().query(contactUri,qureyFileds,null,null,null);

            try{
                if(c.getCount() == 0){
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }finally {
                c.close();
            }


        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }
    private String getNumber(String name){

        String number= null;

        Uri uri  = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,null,null,null);
        if(cursor!=null &&cursor.moveToFirst()){
            do {
                long id = cursor.getLong(0);
                Log.d(TAG, "id:  "+id);
                String cursorName = cursor.getString(1);
                Log.d(TAG, "cursonNmae:  "+cursorName);

                if(name.equals(cursorName)){

                    String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

                    Cursor phonecursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,phoneProjection,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id,null,null);

                    if(phonecursor!=null&& phonecursor.moveToFirst()){
                        name  =phonecursor.getString(0);
                        Log.d(TAG, "phoneName : "+name);
                        return name;
                    }



                }

            }while (cursor.moveToNext());
        }




        return number;

    }

}
