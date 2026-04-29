package com.example.lab08_firebase.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab08_firebase.R;
import com.example.lab08_firebase.view.adapter.ProductAdapter;
import com.example.lab08_firebase.viewmodel.ProductViewModel;

public class CustomerActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        RecyclerView recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter();
        recyclerViewProducts.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProducts().observe(this, products -> {
            if (products != null && !products.isEmpty()) {
                Log.d("CustomerActivity", "San pham da duoc nhan: " + products.size());
                productAdapter.setProducts(products);
            } else {
                Log.d("CustomerActivity", "Khong co san pham duoc hien thi");
                productAdapter.setProducts(products);
                Toast.makeText(this, R.string.no_products, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
