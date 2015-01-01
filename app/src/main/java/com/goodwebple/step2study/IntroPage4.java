package com.goodwebple.step2study;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by j8n3y on 14. 12. 31..
 */
public class IntroPage4 extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    public static IntroPage4 create(int pageNumber) {

        IntroPage4 fragment = new IntroPage4();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;


    }

    public IntroPage4() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    private void findViewById(int button) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_intro_page_4, container, false);
        Button btn = (Button) rootView.findViewById(R.id.button4);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }

    public int getPageNumber() { return mPageNumber; }
}
