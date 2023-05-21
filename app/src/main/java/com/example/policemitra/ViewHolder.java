package com.example.policemitra;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView fileNo,rTitletv, rDesctv;
    View mView;
    ImageButton edit,delete;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        fileNo = itemView.findViewById(R.id.criminalFileNo);
        rTitletv = itemView.findViewById(R.id.rTitletv);
        rDesctv = itemView.findViewById(R.id.rDescriptiontv);
        edit=itemView.findViewById(R.id.edit);
        delete=itemView.findViewById(R.id.delete);
    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
