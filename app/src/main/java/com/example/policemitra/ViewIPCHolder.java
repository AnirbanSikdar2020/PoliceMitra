package com.example.policemitra;

import android.view.View;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class ViewIPCHolder extends RecyclerView.ViewHolder {

    TextView titleTV, sectionTV;
    LinearLayout details;
    View mView;

    public ViewIPCHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        titleTV = itemView.findViewById(R.id.titleTV);
        sectionTV = itemView.findViewById(R.id.sectionTV);
        details= itemView.findViewById(R.id.details);
    }
    private ViewHolder.ClickListener mClickListener;
    public interface ClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

