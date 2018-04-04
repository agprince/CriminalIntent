package com.agprincefu.andriod.criminalintent;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.zip.Inflater;


public class ImageViewFragment extends DialogFragment {

    private Crime mCrime;
    private ImageView mImageView;

    private static final String TAG = "agtest";
    private static final String ARG_CRIME="ImageView_crime";

    public static ImageViewFragment newInstance(Crime crime){

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME, crime);

        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setArguments(bundle);
        Log.d(TAG, "Image view create");

        return fragment;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mCrime = (Crime) getArguments().get(ARG_CRIME);

        File file =CrimeLab.get(getActivity()).getPhotoFile(mCrime);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.frament_imageview,null);

        mImageView = v.findViewById(R.id.dialog_image_view);


        if(file==null&&!file.exists()){
            Log.d(TAG, "onCreateDialog: imageView nulll");
            mImageView.setImageBitmap(null);

        }else {
            Bitmap bitmap = PictureUtils.getBigBitmap(file.getPath());

            mImageView.setImageBitmap(bitmap);
            Log.d(TAG, "onCreateDialog: ImageView get");
        }






        return new AlertDialog.Builder(getActivity()).setTitle(mCrime.getPhotoFilename()).setView(v).create();



    }
}
