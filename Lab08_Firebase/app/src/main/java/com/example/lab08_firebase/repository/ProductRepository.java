package com.example.lab08_firebase.repository;

import android.util.Log;

import com.example.lab08_firebase.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductRepository {
    private final DatabaseReference database;

    public ProductRepository() {
        database = FirebaseDatabase.getInstance().getReference("products");
    }

    public Task<DataSnapshot> getAllProducts() {
        Log.d("ProductRepository", "Dang lay tat ca san pham...");
        return database.get();
    }

    public Task<Void> addProduct(Product product) {
        if (product.getProductID() == null || product.getProductID().isEmpty()) {
            String productId = database.push().getKey();
            if (productId != null) {
                product.setProductID(productId);
                Log.d("ProductRepository", "ID san pham moi duoc tao: " + productId);
            } else {
                Log.e("ProductRepository", "Khong the tao ID san pham");
                return Tasks.forException(new Exception("Khong the tao ID san pham"));
            }
        }
        Log.d("ProductRepository", "Them san pham: " + product.getName());
        return database.child(product.getProductID()).setValue(product);
    }

    public Task<Void> updateProduct(Product product) {
        if (product.getProductID() != null && !product.getProductID().isEmpty()) {
            Log.d("ProductRepository", "Cap nhat san pham: " + product.getName());
            return database.child(product.getProductID()).setValue(product);
        } else {
            Log.e("ProductRepository", "ID san pham khong hop le khi cap nhat");
            return Tasks.forException(new Exception("ID san pham khong hop le"));
        }
    }

    public Task<Void> deleteProduct(String productID) {
        if (productID != null && !productID.isEmpty()) {
            Log.d("ProductRepository", "Xoa san pham voi ID: " + productID);
            return database.child(productID).removeValue();
        } else {
            Log.e("ProductRepository", "ID san pham khong hop le khi xoa");
            return Tasks.forException(new Exception("ID san pham khong hop le"));
        }
    }
}
