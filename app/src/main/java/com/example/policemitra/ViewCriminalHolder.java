package com.example.policemitra;

        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class ViewCriminalHolder extends RecyclerView.ViewHolder {

    TextView rTitletv, rDesctv;
    LinearLayout details;
    View mView;

    public ViewCriminalHolder(@NonNull View itemView) {
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
