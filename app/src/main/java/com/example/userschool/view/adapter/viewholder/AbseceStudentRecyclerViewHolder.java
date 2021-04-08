package com.example.userschool.view.adapter.viewholder;import android.view.View;import android.widget.ImageView;import android.widget.TextView;import androidx.annotation.NonNull;import androidx.recyclerview.widget.RecyclerView;import com.example.userschool.R;public class AbseceStudentRecyclerViewHolder extends RecyclerView.ViewHolder {    private View view;    private TextView txtTableLayoutSubject;    private ImageView imgTableLayoutFound;    private TextView txtTableLayoutDate;    public AbseceStudentRecyclerViewHolder(@NonNull View view) {        super( view );        this.view = view;        txtTableLayoutSubject = view.findViewById( R.id.txtTableLayoutSubject );        imgTableLayoutFound = view.findViewById( R.id.imgTableLayoutFound );        txtTableLayoutDate = view.findViewById( R.id.txtTableLayoutDate );    }    public View getView() {        return view;    }    public TextView getTxtTableLayoutSubject() {        return txtTableLayoutSubject;    }    public ImageView getImgTableLayoutFound() {        return imgTableLayoutFound;    }    public TextView getTxtTableLayoutDate() {        return txtTableLayoutDate;    }}