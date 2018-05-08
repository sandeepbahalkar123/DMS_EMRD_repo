package com.scorg.dms.ui.fragments.book_appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorg.dms.R;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.util.DMSConstants;

public class CoachFragment extends Fragment {
    public CoachFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CoachFragment newInstance() {
        CoachFragment fragment = new CoachFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coach_download_all_patient, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COACHMARK_ALL_PATIENT_DOWNLOAD, DMSConstants.YES, getActivity());
            }
        });
        return rootView;
    }

}
