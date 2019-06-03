package com.TolKap.agarshan.tamilwallpaper.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.TolKap.agarshan.tamilwallpaper.adapter.AllImagesAdapter;
import com.TolKap.agarshan.tamilwallpaper.model.AllImagesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecentImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentImagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recentImagesRv;
    private AllImagesAdapter recentImagesAdapter;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mref;
    private ArrayList<AllImagesModel> recentImagesModelList;
    private AllImagesModel model;
    private Query qry;
    private LayoutAnimationController controller;
    private AVLoadingIndicatorView avi;
    private TextView noInternetTv;




    private OnFragmentInteractionListener mListener;

    public RecentImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecentImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecentImagesFragment newInstance(String param1, String param2) {
        RecentImagesFragment fragment = new RecentImagesFragment();
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
        return inflater.inflate(R.layout.fragment_recent_images, container, false);
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

        recentImagesRv = view.findViewById(R.id.recentImagesRv);
        avi = view.findViewById(R.id.avi);
        noInternetTv = view.findViewById(R.id.noInternetTv);
        if (isNetworkAvailable()){
            noInternetTv.setVisibility(View.INVISIBLE);
        avi.show();

        final GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recentImagesRv.setLayoutManager(staggeredGridLayoutManager);


        runAnimation(recentImagesRv);




        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mref = mfirebaseDatabase.getReference("Data");
        qry = mref.orderByChild("allimages");


        recentImagesModelList = new ArrayList<AllImagesModel>();
        recentImagesAdapter = new AllImagesAdapter(getContext(), recentImagesModelList);

        qry.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentImagesModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()){

                    model = dataSnapshot3.getValue(AllImagesModel.class);

                    recentImagesModelList.add(model);

                }}}
                Collections.sort(recentImagesModelList, new Comparator<AllImagesModel>() {
                    @Override
                    public int compare(AllImagesModel allImagesModel, AllImagesModel t1) {

                        return Integer.valueOf(String.valueOf(t1.getImageNumber())).compareTo(Integer.valueOf(String.valueOf(allImagesModel.getImageNumber())));
                    }
                });
                recentImagesAdapter.notifyDataSetChanged();
                recentImagesRv.setAdapter(recentImagesAdapter);
                recentImagesRv.setLayoutAnimation(controller);
                recentImagesRv.getAdapter().notifyDataSetChanged();

                recentImagesRv.scheduleLayoutAnimation();
                avi.hide();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();

            }


        });
        final FloatingActionButton FAB = (FloatingActionButton) view.findViewById(R.id.upFAB);
        FAB.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recentImagesRv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (staggeredGridLayoutManager.findFirstVisibleItemPosition()>1){
                                FAB.setVisibility(View.VISIBLE);
                        }else{
                            FAB.setVisibility(View.INVISIBLE);
                        }
                }
            });
        }
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recentImagesRv != null){
                    recentImagesRv.smoothScrollToPosition(0);
                }
            }
        });
        }else
        {
            avi.hide();
            noInternetTv.setVisibility(View.VISIBLE);
        }

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
}
