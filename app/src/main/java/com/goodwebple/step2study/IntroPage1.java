package com.goodwebple.step2study;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by j8n3y on 14. 12. 30..
 */
public class IntroPage1 extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    public static IntroPage1 create(int pageNumber) {

        IntroPage1 fragment = new IntroPage1();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroPage1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_intro_page_1, container, false);

        return rootView;
    }

    public int getPageNumber() { return mPageNumber; }


}
