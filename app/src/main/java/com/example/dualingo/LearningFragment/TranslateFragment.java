package com.example.dualingo.LearningFragment;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dualingo.R;
public class TranslateFragment extends Fragment {

    ImageButton btnBack ;
    Button btnCheck , btnNext;

    TextView answer;
    public TranslateFragment() {
        // Required empty public constructor
    }
    public static TranslateFragment newInstance(String param1, String param2) {
        TranslateFragment fragment = new TranslateFragment();
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
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        btnBack = view.findViewById(R.id.btnback);
        answer = view.findViewById(R.id.answerInput);
        btnCheck = view.findViewById(R.id.checkAnswer);
        btnNext = view.findViewById(R.id.btnNext);


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = answer.getText().toString().toLowerCase();


                // Tạo LayoutInflater và View cho popup từ popup_bottom_layout.xml
                LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_check_answer, null);

                // Tạo PopupWindow
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        false
                );

                // Đặt background bo góc cho PopupWindow
                popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.popup_background));
                popupWindow.setElevation(10); // Tùy chọn: thêm độ nổi cho PopupWindow

                // Thiết lập sự kiện cho nút đóng trong popup
                Button btnClosePopup = popupView.findViewById(R.id.btnClosePopup);
                btnClosePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss(); // Đóng popup
                    }
                });
                TextView txtTitle , txtMessage;
                txtTitle = popupView.findViewById(R.id.tvPopupTitle);
                txtMessage = popupView.findViewById(R.id.tvPopupMessage);

                // Hiển thị popup ở cuối màn hình
                if(txt.equals("this is my dog")){
                    txtTitle.setText("Dung me roi");
                    txtMessage.setText("Trinh cao day chu");
                    popupWindow.showAtLocation(requireActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                }else{
                    txtTitle.setText("Sai roi");
                    txtMessage.setText("Sai la sai , Bo bao con nay");
                    popupWindow.showAtLocation(requireActivity().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                }
                btnNext.setVisibility(View.VISIBLE);
                btnCheck.setVisibility(View.GONE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}