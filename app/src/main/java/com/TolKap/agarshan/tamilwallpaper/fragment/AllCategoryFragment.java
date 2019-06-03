package com.TolKap.agarshan.tamilwallpaper.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.adapter.AllCategoryAdapter;
import com.TolKap.agarshan.tamilwallpaper.model.AllCategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCategoryFragment extends Fragment {


    private RecyclerView allCategoryRv;
    private AllCategoryAdapter allCategoryAdapter;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref;
    private AllCategoryModel model;
    private ArrayList<AllCategoryModel> allCategoryModellist;
    private LayoutAnimationController controller;
    private AVLoadingIndicatorView avi;
    private TextView noInternetTv;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCategoryFragment newInstance(String param1, String param2) {
        AllCategoryFragment fragment = new AllCategoryFragment();
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


        View view= inflater.inflate(R.layout.fragment_all_category, container, false);

        return view;
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
        allCategoryRv = view.findViewById(R.id.allCategoryRv);


        avi = view.findViewById(R.id.avi);
        noInternetTv = view.findViewById(R.id.noInternetTv);
        setUpView();
    }


    private void setUpView() {



        if (isNetworkAvailable()){
            noInternetTv.setVisibility(View.INVISIBLE);
        avi.show();


            final GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            allCategoryRv.setLayoutManager(staggeredGridLayoutManager);
            runAnimation(allCategoryRv);
        allCategoryModellist = new ArrayList<AllCategoryModel>();
        allCategoryAdapter = new AllCategoryAdapter(getContext(), allCategoryModellist);

            retrieveCategoryDetails();



    }else
    {
        avi.hide();
        noInternetTv.setVisibility(View.VISIBLE);
    }

    }

    private void retrieveCategoryDetails() {
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");

        mref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allCategoryModellist.clear();
                allCategoryAdapter.notifyDataSetChanged();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    model = dataSnapshot1.getValue(AllCategoryModel.class);
                    allCategoryModellist.add(model);

                }
                allCategoryAdapter.notifyDataSetChanged();
                allCategoryRv.setAdapter(allCategoryAdapter);
                allCategoryRv.setLayoutAnimation(controller);
                allCategoryRv.getAdapter().notifyDataSetChanged();
                avi.hide();
                allCategoryRv.scheduleLayoutAnimation();





            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();

            }


        });


    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    private void runAnimation(RecyclerView allImagesRv) {

        Context context = allImagesRv.getContext();
        controller = null;

        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

