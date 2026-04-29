package com.example.lab08_firebase.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab08_firebase.R;
import com.example.lab08_firebase.model.Product;
import com.example.lab08_firebase.view.adapter.ProductAdapterAdmin;
import com.example.lab08_firebase.viewmodel.ProductViewModel;

public class AdminActivity extends AppCompatActivity {
    private ProductAdapterAdmin adapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button buttonAddProduct = findViewById(R.id.buttonAddProduct);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);

        adapter = new ProductAdapterAdmin();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProducts().observe(this, products -> adapter.setProducts(products));

        buttonAddProduct.setOnClickListener(v -> openEditProductDialog(null));

        adapter.setOnEditClickListener(this::openEditProductDialog);
        adapter.setOnDeleteClickListener(product -> {
            productViewModel.deleteProduct(product.getProductID());
            Toast.makeText(this, R.string.product_deleted, Toast.LENGTH_SHORT).show();
        });
    }

    private void openEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editTextProductName = dialogView.findViewById(R.id.editTextProductName);
        EditText editTextProductPrice = dialogView.findViewById(R.id.editTextProductPrice);
        EditText editTextProductQuantity = dialogView.findViewById(R.id.editTextProductQuantity);

        if (product != null) {
            editTextProductName.setText(product.getName());
            editTextProductPrice.setText(String.valueOf(product.getPrice()));
            editTextProductQuantity.setText(String.valueOf(product.getQuantity()));
        }

        builder.setPositiveButton(R.string.save, (dialog, which) -> {
            String productName = editTextProductName.getText().toString().trim();
            String priceText = editTextProductPrice.getText().toString().trim();
            String quantityText = editTextProductQuantity.getText().toString().trim();

            if (productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_all, Toast.LENGTH_SHORT).show();
                return;
            }

            int productPrice;
            int productQuantity;
            try {
                productPrice = Integer.parseInt(priceText);
                productQuantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, R.string.error_invalid_number, Toast.LENGTH_SHORT).show();
                return;
            }

            if (product == null) {
                String newProductID = productViewModel.generateProductID();
                if (newProductID == null || newProductID.isEmpty()) {
                    Toast.makeText(this, R.string.error_create_product_id, Toast.LENGTH_SHORT).show();
                    return;
                }

                Product newProduct = new Product(productName, productPrice, newProductID, productQuantity);
                productViewModel.addProduct(newProduct);
                Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show();
            } else {
                product.setName(productName);
                product.setPrice(productPrice);
                product.setQuantity(productQuantity);
                productViewModel.updateProduct(product);
                Toast.makeText(this, R.string.product_updated, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }
}
