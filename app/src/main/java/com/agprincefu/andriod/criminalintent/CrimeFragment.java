package com.agprincefu.andriod.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by agprincefu on 2018/1/27.
 */

public class CrimeFragment extends Fragment {

    private static final String TAG = "agtest";

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_FRAGMENT_TAG = " date_fragment";
    private static final String IMAGE_FRAGEMETN_TAG="ImageView_fragment";

    private static final int REQUEST_CODE_DATE = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private File mPhotoFile;

    private int mPhotoWidth = 0;
    private int mPhotoHeight=0 ;


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
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
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
        mPhotoButton = v.findViewById(R.id.crime_camera);
        mPhotoView = v.findViewById(R.id.crime_photo);


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
        if (manager.resolveActivity(pickIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickIntent, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile !=null && captureImage.resolveActivity(manager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),"com.agprincefu.andriod.criminalintent.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo activity:cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });



        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mPhotoWidth = mPhotoView.getMeasuredWidth();
                     mPhotoHeight = mPhotoView.getMeasuredHeight();

                    //setPhotoWidth(mPhotoView.getMeasuredWidth());
                    //setPhotoHeight(mPhotoView.getMeasuredHeight());

                    Log.d(TAG, "onGlobalLayout: mPhotoWidth = " + mPhotoWidth);
                    Log.d(TAG, "onGlobalLayout: mPhotoHeight = " + mPhotoHeight);
                }
            });

            Log.d(TAG, "After onGlobalLayout: mPhotoWidth = " + mPhotoWidth);
            Log.d(TAG, "After onGlobalLayout: mPhotoHeight = " + mPhotoHeight);
        }





        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImageViewFragment fragment =ImageViewFragment.newInstance(mCrime);
                FragmentManager fragmentManager = getFragmentManager();
                fragment.show(fragmentManager,IMAGE_FRAGEMETN_TAG);
            }
        });





        updataPhotoView();




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
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            String[] qureyFileds = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c = getActivity().getContentResolver().query(contactUri, qureyFileds, null, null, null);

            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }


        }else if(requestCode ==REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),"com.agprincefu.andriod.criminalintent.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updataPhotoView();
        }
    }

    private void updataPhotoView(){
        if(mPhotoFile==null||!mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else if(mPhotoHeight>0&& mPhotoWidth>0){
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),mPhotoWidth,mPhotoHeight);
            Log.d(TAG, "updataPhotoView: 自动适应");
            mPhotoView.setImageBitmap(bitmap);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            Log.d(TAG, "updataPhotoView: 非自动适应,采用默认的activity");
            mPhotoView.setImageBitmap(bitmap);
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
}
