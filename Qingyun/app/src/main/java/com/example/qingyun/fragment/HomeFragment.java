package com.example.qingyun.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.qingyun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private EditText editTextSearch;
    private ImageView btnSearch;
    private Button book;
    private Button material;
    private Button electronic;
    private Button cosmetic;
    private Button equipment;
    private Button others;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        editTextSearch=view.findViewById(R.id.editTextSearch);
        btnSearch=view.findViewById(R.id.btnSearch);
        book=view.findViewById(R.id.book);
        material=view.findViewById(R.id.material);
        electronic=view.findViewById(R.id.electronic);
        cosmetic=view.findViewById(R.id.cosmetic);
        equipment=view.findViewById(R.id.equipment);
        others=view.findViewById(R.id.others);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        btnSearch.setOnClickListener(this);
        book.setOnClickListener(this);
        material.setOnClickListener(this);
        electronic.setOnClickListener(this);
        cosmetic.setOnClickListener(this);
        equipment.setOnClickListener(this);
        others.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if(viewId==R.id.btnSearch){//搜索

        } else if (viewId==R.id.book) {//二手图书

        }else if (viewId==R.id.material) {//资料笔记

        }else if (viewId==R.id.electronic) {//电子数码

        }else if (viewId==R.id.cosmetic) {//美容妆造

        }else if (viewId==R.id.equipment) {//健身器材

        }else if (viewId==R.id.others) {//其他

        }
    }
}