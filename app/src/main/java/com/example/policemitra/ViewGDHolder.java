package com.example.policemitra;

        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class ViewGDHolder extends RecyclerView.ViewHolder {

    TextView rTitletv, rDesctv,status;
    View mView;
    ImageButton approve,decline,download;
    LinearLayout layoutDiv;

    public ViewGDHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        rTitletv = itemView.findViewById(R.id.rTitletv);
        rDesctv = itemView.findViewById(R.id.rDescriptiontv);
        status = itemView.findViewById(R.id.complaintsStatus);
        approve = itemView.findViewById(R.id.approve);
        decline = itemView.findViewById(R.id.decline);
        download = itemView.findViewById(R.id.download);
        layoutDiv = itemView.findViewById(R.id.ViewComplaint);
    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
