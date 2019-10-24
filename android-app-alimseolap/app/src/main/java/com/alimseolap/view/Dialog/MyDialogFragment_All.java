package com.alimseolap.view.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimseolap.R;
import com.alimseolap.model.DataDTO;
import com.alimseolap.model.NotificationDTO;
import com.alimseolap.presenter.DBManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// 모든 알림 팝업 프래그먼트
public class MyDialogFragment_All extends DialogFragment {
    RecyclerView rv;
    ImageButton close;
    private RecyclerAdapter adapter;
    private static int direction = 0;
    DBManager database;

    public MyDialogFragment_All(DBManager database){
        this.database = database;
    }

    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.fragment_all, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recyclerView);
        close = (ImageButton) v.findViewById(R.id.close);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        init();
        getData();
        return v;
    }


    public void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(R.layout.item);
        rv.setAdapter(adapter);

    }


    // :TODO: ===================================== 노티 데이터 받아서 저장하는 부분
    private void getData() {

        ArrayList<NotificationDTO> notiData = database.getNotificationList();

        // 임의의 데이터
//        List<String> listTitle = Arrays.asList("카카오톡", "페이스북", "카카오톡", "페이코", "페이스북", "카카오톡", "카카오톡", "페이스북",
//                "인스타그램", "인스타그램", "인스타그램", "페이코", "카카오톡", "페이스북", "페이코");
//        List<String> listContent = Arrays.asList(
//                "모자 특가 할인 중!",
//                "선착순 1000명 족발 할인 쿠폰 쏜다!",
//                "오늘 한일전 8시, 치킨 먹지 않을래?",
//                "제 5회 공항철도 공모전 개최 확인하기",
//                "마라탕이 논란이 된 이유",
//                "마카롱 더이상 먹지 마세요.",
//                "8월 작가와의 만남 신청하기",
//                "화제의 그 화장품, 피부에 엄청 안좋다고?",
//                "유럽인들이 한국에 오면 꼭 사가는 화장품 리스트",
//                "오프라인 모임에 가입해보세요.",
//                "영상시스템님이 시급 만원의 새 일감을 등록하였습니다.",
//                "서현동 근처에서 이번 주 핫했던 인기 매물을 만나보세요.",
//                "오늘은 말복이니까 치킨 할인",
//                "이번 주 마음의 소리 보러가기",
//                "새로운 친구 추천이 있습니다:조수은님"
//        );




        for (int i = 0; i < notiData.size(); i++) {
            // 각 List의 값들을 data 객체에 set
            DataDTO data = new DataDTO();
            data.setTitle(notiData.get(i).title);
            data.setContent(notiData.get(i).msg);
            //data.setResId(listResId.get(i));
            // 각 값이 들어간 data를 adapter에 추가
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줌
        adapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            // :TODO: 스와이프 함수!
            //스와이프 방향값을 정수형으로 얻어옴.
            MyDialogFragment_All.direction = direction;
            runAnimationAgain(viewHolder);
            System.out.println((MyDialogFragment_All.direction - 6)/2);



            // database.updateNotiWeight();

        }

    };

    private void runAnimationAgain(@NonNull RecyclerView.ViewHolder viewHolder) {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_down_to_up);

        adapter.getListData().remove(viewHolder.getAdapterPosition());
        adapter.notifyDataSetChanged();
        rv.setLayoutAnimation(controller);
    }

}
