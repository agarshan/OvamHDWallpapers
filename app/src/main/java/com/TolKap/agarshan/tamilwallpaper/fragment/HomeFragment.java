package com.TolKap.agarshan.tamilwallpaper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.TolKap.agarshan.tamilwallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private LinearLayout recentTab,allTab,popularTab;
    private RelativeLayout popularTabColorRl,allTabColorRl,recentTabColorRl;
    private AllCategoryFragment allCategoryFragment;
    private RecentImagesFragment recentImagesFragment;
    private PopularImagesFragment popularImagesFragment;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allCategoryFragment = new AllCategoryFragment();
        recentImagesFragment=new RecentImagesFragment();
        popularImagesFragment=new PopularImagesFragment();

        recentTab =view.findViewById(R.id.recentTab);
        allTab = view.findViewById(R.id.allTab);
        popularTab = view.findViewById(R.id.popularTab);
        popularTabColorRl = view.findViewById(R.id.popularTabColorRl);
        allTabColorRl = view.findViewById(R.id.allTabColorRl);
        recentTabColorRl = view.findViewById(R.id.recentTabColorRl);
        pushFragment(allCategoryFragment, R.id.homeContainer, false, "home_fragment");

        allTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTabColorRl.setVisibility(View.VISIBLE);
                popularTabColorRl.setVisibility(View.INVISIBLE);
                recentTabColorRl.setVisibility(View.INVISIBLE);
                pushFragment(allCategoryFragment, R.id.homeContainer, false, "home_fragment");

            }
        });

        recentTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTabColorRl.setVisibility(View.INVISIBLE);
                popularTabColorRl.setVisibility(View.INVISIBLE);
                recentTabColorRl.setVisibility(View.VISIBLE);
                pushFragment(recentImagesFragment, R.id.homeContainer, false, "home_fragment");

            }
        });

        popularTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allTabColorRl.setVisibility(View.INVISIBLE);
                popularTabColorRl.setVisibility(View.VISIBLE);
                recentTabColorRl.setVisibility(View.INVISIBLE);
//                pushFragment(allCategoryFragment, R.id.dashboardContainer, false, "home_fragment");
                pushFragment(popularImagesFragment, R.id.homeContainer, false, "home_fragment");

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void pushFragment(Fragment fragment, int container, boolean shouldAddToBackStack, String tag){
        try{
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(container, fragment);

            if(shouldAddToBackStack){
                transaction.addToBackStack(tag);
            }else {
                transaction.addToBackStack(null);
            }
            transaction.commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
