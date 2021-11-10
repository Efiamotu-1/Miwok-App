package com.example.android.miwok;

public class Word {
    private  String mDefaultTranslation;

    private String mMiwokTranslation;

    private int mAudio;

    private int mImageResource = NO_IMAGE_PROVIDED;

    public static final int NO_IMAGE_PROVIDED = -1;


    public Word(String defaultTranslation, String MiwokTranslation, int Audio){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = MiwokTranslation;
        mAudio = Audio;


    }

    public Word(String defaultTranslation, String MiwokTranslation, int ImageResource, int Audio){
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = MiwokTranslation;
        mImageResource = ImageResource;
        mAudio = Audio;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getmImageResource() {return mImageResource;}

    public int getmAudio(){return mAudio;}

    public boolean hasImage(){
        return mImageResource != NO_IMAGE_PROVIDED;
    }
}
