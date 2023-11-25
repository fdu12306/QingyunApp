package com.example.qingyun.fragment;

import android.content.Intent;
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
import android.widget.Spinner;

import com.example.qingyun.R;
import com.example.qingyun.activity.LoginActivity;
import com.example.qingyun.activity.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PublishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublishFragment extends Fragment implements View.OnClickListener{
    private EditText editTextDescription;
    private ImageView imageViewSelected;
    private Button btnAddImage;
    private Spinner spinnerCampus;
    private Spinner spinnerCategory;
    private EditText editTextPrice;
    private Button btnPublish;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PublishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublishFragment newInstance(String param1, String param2) {
        PublishFragment fragment = new PublishFragment();
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
        View view=inflater.inflate(R.layout.fragment_publish, container, false);
        editTextDescription=view.findViewById(R.id.editTextDescription);
        imageViewSelected=view.findViewById(R.id.imageViewSelected);
        btnAddImage=view.findViewById(R.id.btnAddImage);
        spinnerCampus=view.findViewById(R.id.spinnerCampus);
        spinnerCategory=view.findViewById(R.id.spinnerCategory);
        editTextPrice=view.findViewById(R.id.editTextPrice);
        btnPublish=view.findViewById(R.id.btnPublish);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    private void initListener() {
        btnAddImage.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int viewId=v.getId();
        if(viewId==R.id.btnAddImage){//添加图片

        } else if (viewId==R.id.btnPublish) {//发布

        }
    }
}