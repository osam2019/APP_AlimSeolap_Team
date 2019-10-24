package com.alimseolap.view.Dialog;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.alimseolap.R;
import com.alimseolap.model.DataDTO;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>  {


    // adapter에 들어갈 list 입니다.
    public ArrayList<DataDTO> listData = new ArrayList<>();

    // 버튼에 따라 백그라운드 색상 분기
    int item_layout_id = 0;

    public RecyclerAdapter(int item_layout_id){
        this.item_layout_id = item_layout_id;
    }
    @Override
    public ItemViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(item_layout_id, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder( ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(DataDTO data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public ArrayList<DataDTO> getListData(){
        return listData;
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView TitleText;
        private TextView SubText;
        private ImageView IconView;

        ItemViewHolder(View itemView) {
            super(itemView);

            TitleText = itemView.findViewById(R.id.TitleText);
            SubText = itemView.findViewById(R.id.SubText);
            IconView = itemView.findViewById(R.id.IconView);
        }

        void onBind(DataDTO data) {
            TitleText.setText(data.getTitle());
            SubText.setText(data.getContent());
            IconView.setImageResource(data.getResId());
        }
    }

}
