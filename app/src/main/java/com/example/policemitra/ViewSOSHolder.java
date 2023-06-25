package com.example.policemitra;

import android.view.View;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class ViewSOSHolder extends RecyclerView.ViewHolder {

    TextView fileNo,rTitletv;
    View mView;
    ImageButton edit,approve,download;

    public ViewSOSHolder(@NonNull View itemView) {
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
        download=itemView.findViewById(R.id.call);
    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
