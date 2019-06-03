package com.TolKap.agarshan.tamilwallpaper.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.TolKap.agarshan.tamilwallpaper.R;
import com.TolKap.agarshan.tamilwallpaper.adapter.AllImagesAdapter;
import com.TolKap.agarshan.tamilwallpaper.adapter.FavouriteImagesAdapter;
import com.TolKap.agarshan.tamilwallpaper.database.Database;
import com.TolKap.agarshan.tamilwallpaper.model.FavouriteImage;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView favoriteImageRv;
    private FavouriteImagesAdapter favoriteImagesAdapter;
    private AVLoadingIndicatorView avi;
    private LayoutAnimationController controller;
    private Database localdb;
    private List<FavouriteImage> favouriteImages;
    private List<FavouriteImage> favouriteImagesFinalList;
    private TextView noItemsTv;



    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteImageRv = view.findViewById(R.id.favoriteImageRv);
        avi = view.findViewById(R.id.avi);
        noItemsTv = view.findViewById(R.id.noItemsTv);





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

    private void runAnimation(RecyclerView allImagesRv) {

        Context context = allImagesRv.getContext();
        controller = null;

        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);

    }


    @Override
    public void onResume() {
        super.onResume();
        avi.show();
        favouriteImages = new ArrayList<>();
        favouriteImagesFinalList = new ArrayList<>();
        favouriteImages.clear();
        favouriteImagesFinalList.clear();
        favouriteImagesFinalList = new Database(getActivity()).getfavoriteImage();
        localdb= new Database(getActivity());


            for (int i=0; i < favouriteImagesFinalList.size();i++){
                if (localdb.isFavorite(new FavouriteImage(favouriteImagesFinalList.get(i).getUrlName()))){
                        favouriteImages.add(favouriteImagesFinalList.get(i));

                }
            }


        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        favoriteImageRv.setLayoutManager(staggeredGridLayoutManager);

        runAnimation(favoriteImageRv);
        favoriteImagesAdapter = new FavouriteImagesAdapter(getContext(), favouriteImages);
        favoriteImagesAdapter.notifyDataSetChanged();
        favoriteImageRv.setAdapter(favoriteImagesAdapter);

        favoriteImageRv.setLayoutAnimation(controller);
        favoriteImageRv.getAdapter().notifyDataSetChanged();

        favoriteImageRv.scheduleLayoutAnimation();

        avi.hide();
        noItemsTv.setVisibility(View.INVISIBLE);

        if (favoriteImagesAdapter.getItemCount()==0){
            noItemsTv.setVisibility(View.VISIBLE);

        }else {
            noItemsTv.setVisibility(View.INVISIBLE);

        }

    }
}
