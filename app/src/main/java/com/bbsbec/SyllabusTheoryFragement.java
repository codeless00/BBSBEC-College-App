package com.bbsbec;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyllabusTheoryFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyllabusTheoryFragement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SyllabusSubjectAdapter syllabusSubjectAdapter;
    RecyclerView recyclerView;

    String basefile;

    public SyllabusTheoryFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SyllabusTheoryFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static SyllabusTheoryFragement newInstance(String param1, String param2) {
        SyllabusTheoryFragement fragment = new SyllabusTheoryFragement();
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

        Window window = requireActivity().getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.tab_selected_background_color));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_syllabus_theory_fragement, container, false);
        recyclerView = view.findViewById(R.id.syllabus_theory_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        basefile = getArguments().getString("BASEFILE");
        String[] sub_file = InternalStorage.deserializeStringArray(getContext(),basefile+"_Theory_Sub");
        Log.d("SYB", Arrays.toString(sub_file));
        syllabusSubjectAdapter = new SyllabusSubjectAdapter(getContext(),InternalStorage.deserializeStringArray(getContext(),getArguments().getString("BASEFILE") + "_Theory_Sub"),getLayoutInflater());
        recyclerView.setAdapter(syllabusSubjectAdapter);
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FIRE","onviewcreated view is restored");

//        assert getArguments() != null;
//        ((TextView)view.findViewById(R.id.fragment_theory)).setText(getArguments().getString("BASEFILE") + "_Theory_Sub");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("FIRE","onresume view is restored");
//        recyclerView.setAdapter(syllabusSubjectAdapter);
//        syllabusSubjectAdapter.notifyDataSetChanged();
    }



    public class SyllabusSubjectAdapter extends RecyclerView.Adapter<SyllabusSubjectAdapter.viewHolder> {

        Context context;
        String[] subjects;
        LayoutInflater inflater;

        public SyllabusSubjectAdapter(Context ctx, String[] subject, LayoutInflater layoutInflater) {
            this.subjects = subject;
            this.inflater = layoutInflater;
            this.context = ctx;
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.syllabus_subject_recycleview_list,parent,false);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull viewHolder holder, int position) {
            holder.subText.setText(subjects[position]);
//            holder.subText.setBackgroundColor(Color.TRANSPARENT);
//            holder.subText.setTextColor(holder.subText.getResources().getColor(R.color.color_grey));
            holder.cardView_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.subText.setTextColor(holder.subText.getResources().getColor(R.color.purple_200));
                    Intent intent = new Intent(context, DetailedSyllabus.class);
                    intent.putExtra("BASEFILE",basefile + "_Theory_Val");
                    intent.putExtra("POSITION",String.valueOf(holder.getAdapterPosition()));
                    intent.putExtra("THEORYFILE",basefile + "_Theory_Sub");
                    startActivity(intent);
                }
            });
//        holder.dateText.setText(date[position]);
//        holder.notificationImage.setImageResource(R.drawable.main_achievement);

        }

        @Override
        public int getItemCount() {
            return subjects.length;
        }

        public class viewHolder extends RecyclerView.ViewHolder{
            TextView subText;
            CardView cardView_sub;

            public viewHolder(@NonNull View itemView) {
                super(itemView);

                subText = itemView.findViewById(R.id.subjectDetail);
                cardView_sub = itemView.findViewById(R.id.subject_detail_cardview);
//            dateText = itemView.findViewById(R.id.notification_date);
//            notificationImage = itemView.findViewById(R.id.notification_image);
            }
        }

    }

}