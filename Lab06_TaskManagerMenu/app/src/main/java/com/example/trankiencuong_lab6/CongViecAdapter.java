package com.example.trankiencuong_lab6;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CongViecAdapter extends RecyclerView.Adapter<CongViecAdapter.CongViecViewHolder> {
    private final List<CongViec> danhSachCongViec;
    private List<CongViec> danhSachGoc;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CongViec congViec, int position);

        void onItemLongClick(CongViec congViec, int position, View view);

        void onCheckBoxClick(CongViec congViec, int position);
    }

    public CongViecAdapter(List<CongViec> danhSachCongViec) {
        this.danhSachCongViec = danhSachCongViec;
        this.danhSachGoc = new ArrayList<>(danhSachCongViec);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CongViecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cong_viec, parent, false);
        return new CongViecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CongViecViewHolder holder, int position) {
        holder.bind(danhSachCongViec.get(position), position);
    }

    @Override
    public int getItemCount() {
        return danhSachCongViec.size();
    }

    public void filter(String query) {
        danhSachCongViec.clear();
        if (query == null || query.trim().isEmpty()) {
            danhSachCongViec.addAll(danhSachGoc);
        } else {
            String search = query.toLowerCase();
            for (CongViec cv : danhSachGoc) {
                if (cv.getTenCongViec().toLowerCase().contains(search)) {
                    danhSachCongViec.add(cv);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateDanhSach(List<CongViec> danhSachMoi) {
        this.danhSachCongViec.clear();
        this.danhSachCongViec.addAll(danhSachMoi);
        this.danhSachGoc = new ArrayList<>(danhSachMoi);
        notifyDataSetChanged();
    }

    class CongViecViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxHoanThanh;
        TextView textViewTenCongViec;
        TextView textViewLoaiCongViec;
        ImageView imageViewLoai;

        CongViecViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxHoanThanh = itemView.findViewById(R.id.checkBoxHoanThanh);
            textViewTenCongViec = itemView.findViewById(R.id.textViewTenCongViec);
            textViewLoaiCongViec = itemView.findViewById(R.id.textViewLoaiCongViec);
            imageViewLoai = itemView.findViewById(R.id.imageViewLoai);
        }

        void bind(CongViec congViec, int position) {
            textViewTenCongViec.setText(congViec.getTenCongViec());
            textViewLoaiCongViec.setText(layTenLoaiCongViec(congViec.getLoaiCongViec()));
            checkBoxHoanThanh.setChecked(congViec.isDaHoanThanh());

            if (congViec.isDaHoanThanh()) {
                textViewTenCongViec.setPaintFlags(textViewTenCongViec.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                textViewTenCongViec.setPaintFlags(textViewTenCongViec.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            switch (congViec.getLoaiCongViec()) {
                case "CONG_VIEC":
                    imageViewLoai.setImageResource(android.R.drawable.ic_menu_agenda);
                    break;
                case "CA_NHAN":
                    imageViewLoai.setImageResource(android.R.drawable.ic_menu_mylocation);
                    break;
                case "KHAN_CAP":
                    imageViewLoai.setImageResource(android.R.drawable.ic_dialog_alert);
                    break;
                default:
                    imageViewLoai.setImageResource(android.R.drawable.ic_menu_help);
                    break;
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(congViec, position);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(congViec, position, v);
                }
                return true;
            });

            checkBoxHoanThanh.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCheckBoxClick(congViec, position);
                }
            });
        }

        private String layTenLoaiCongViec(String maLoai) {
            if ("CONG_VIEC".equals(maLoai)) {
                return itemView.getContext().getString(R.string.loai_cong_viec);
            }
            if ("CA_NHAN".equals(maLoai)) {
                return itemView.getContext().getString(R.string.loai_ca_nhan);
            }
            if ("KHAN_CAP".equals(maLoai)) {
                return itemView.getContext().getString(R.string.loai_khan_cap);
            }
            return maLoai;
        }
    }
}
