package com.example.lab08_firebase.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab08_firebase.model.Product;
import com.example.lab08_firebase.repository.ProductRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {
    private final ProductRepository productRepository = new ProductRepository();
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>(new ArrayList<>());

    public ProductViewModel() {
        getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts() {
        Log.d("ProductViewModel", "Dang lay danh sach san pham tu Firebase...");
        productRepository.getAllProducts().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot productSnapshot : task.getResult().getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        // Firebase node key is the true identifier for update/delete operations.
                        product.setProductID(productSnapshot.getKey());
                        products.add(product);
                        Log.d("ProductViewModel", "San pham da duoc tai: " + product.getName());
                    } else {
                        Log.w("ProductViewModel", "Trang thai hien tai cua san pham khong ton tai");
                    }
                }
                productsLiveData.setValue(products);
                Log.d("ProductViewModel", "Tong so san pham da tai: " + products.size());
            } else {
                Log.e("ProductViewModel", "Khong the lay danh sach san pham", task.getException());
            }
        });
        return productsLiveData;
    }

    public LiveData<List<Product>> getProducts() {
        return productsLiveData;
    }

    public void addProduct(Product product) {
        productRepository.addProduct(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Them san pham that bai", task.getException());
            }
        });
    }

    public void updateProduct(Product product) {
        productRepository.updateProduct(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Cap nhat san pham that bai", task.getException());
            }
        });
    }

    public void deleteProduct(String productID) {
        productRepository.deleteProduct(productID).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                refreshProducts();
            } else {
                Log.e("ProductViewModel", "Xoa san pham that bai", task.getException());
            }
        });
    }

    private void refreshProducts() {
        getAllProducts();
    }

    public String generateProductID() {
        return FirebaseDatabase.getInstance().getReference("products").push().getKey();
    }
}
