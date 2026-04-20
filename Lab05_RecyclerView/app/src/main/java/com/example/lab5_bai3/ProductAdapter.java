package com.example.lab5_bai3;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String DEFAULT_IMAGE_URL = "https://via.placeholder.com/150";
    private final List<Product> productList;
    private final Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(String.format(Locale.getDefault(), "Giá: %.2f", product.getPrice()));

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> Toast.makeText(
                context,
                "Sản phẩm: " + product.getName() + " - Giá: " + product.getPrice(),
                Toast.LENGTH_SHORT
        ).show());

        holder.buttonEdit.setOnClickListener(v -> showEditDialog(product, holder.getAdapterPosition()));
        holder.buttonDelete.setOnClickListener(v -> showDeleteConfirmationDialog(product, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void addProduct(Product product) {
        productList.add(product);
        notifyItemInserted(productList.size() - 1);
    }

    public void showProductDialog(@Nullable Product product, int position, boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        EditText editTextImageUrl = dialogView.findViewById(R.id.editTextImageUrl);

        if (isEdit && product != null) {
            dialogTitle.setText("Sửa thông tin sản phẩm");
            editTextName.setText(product.getName());
            editTextPrice.setText(String.valueOf(product.getPrice()));
            editTextImageUrl.setText(product.getImageUrl());
        } else {
            dialogTitle.setText("Thêm sản phẩm mới");
        }

        builder.setPositiveButton(isEdit ? "Lưu" : "Thêm", null);
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String newName = editTextName.getText().toString().trim();
            String newPriceStr = editTextPrice.getText().toString().trim();
            String newImageUrl = editTextImageUrl.getText().toString().trim();

            if (newName.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double newPrice = Double.parseDouble(newPriceStr);
                String finalImageUrl = newImageUrl.isEmpty() ? DEFAULT_IMAGE_URL : newImageUrl;

                if (isEdit && product != null && position >= 0 && position < productList.size()) {
                    product.setName(newName);
                    product.setPrice(newPrice);
                    product.setImageUrl(finalImageUrl);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật sản phẩm!", Toast.LENGTH_SHORT).show();
                } else {
                    Product newProduct = new Product(newName, newPrice, finalImageUrl);
                    addProduct(newProduct);
                    Toast.makeText(context, "Đã thêm sản phẩm mới!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Giá không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(Product product, int position) {
        showProductDialog(product, position, true);
    }

    private void showDeleteConfirmationDialog(Product product, int position) {
        if (position < 0 || position >= productList.size()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm \"" + product.getName() + "\" không?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productList.size());
            Toast.makeText(context, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;
        Button buttonEdit;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
