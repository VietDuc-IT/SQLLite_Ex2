package com.levietduc.sqllite_ex2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.levietduc.adapter.ProductAdapter;
import com.levietduc.models.Product;
import com.levietduc.sqllite_ex2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyDBHelper db;
    ProductAdapter adapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prePareDB();
        loadData();
    }

    //---------------- Create Database ---------------------
    private void prePareDB() {
        // Create database and create table
        db = new MyDBHelper(MainActivity.this);
        db.createSampleData();
    }

    //---------------- Load Database ---------------------
    private void loadData() {
        products = new ArrayList<>();

        // Get data from db
        Cursor c = db.queryData("SELECT * FROM " + MyDBHelper.TBL_NAME);
        int code;
        String name;
        double price;
        //Product p;
        while (c.moveToNext()){
            code = c.getInt(0);
            name = c.getString(1);
            price = c.getDouble(2);
            Product p = new Product(code, name, price);
            //p = new Product(c.getInt(0),c.getString(1),c.getDouble(2));
            products.add(p);
        }
        c.close();

        adapter = new ProductAdapter(MainActivity.this,R.layout.item_list,products);
        binding.lvProduct.setAdapter(adapter);
    }

    //---------------- Edit ---------------------
    public void openEditDialog(Product p){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_edit);

        EditText edtName, edtPrice;
        edtName = dialog.findViewById(R.id.edtProductName);
        edtPrice = dialog.findViewById(R.id.edtProductPrice);

        edtName.setText(p.getProductName());
        edtPrice.setText(String.valueOf(p.getProductPrice()));

        Button btnSave, btnClose;
        btnSave = dialog.findViewById(R.id.btnSave);
        btnClose = dialog.findViewById(R.id.btnClose);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update data ...
                db.execSql("UPDATE " + MyDBHelper.TBL_NAME + " SET " + MyDBHelper.COL_NAME + "='" + edtName.getText().toString() + "', " + MyDBHelper.COL_PRICE + "=" + edtPrice.getText().toString() + " WHERE " + MyDBHelper.COL_CODE + "=" + p.getProductCode());

                // UPDATE Product SET ProductName='Test', ProductPrice=21000 WHERE ProductCode=2

                dialog.dismiss();
                loadData();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //---------------- Delete ---------------------
    public void openDeleteDialog(Product p){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_delete);

        Button btnDelete, btnClose;
        btnDelete = dialog.findViewById(R.id.btnDelete);
        btnClose = dialog.findViewById(R.id.btnClose);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete data into db
                db.execSql("DELETE FROM " +MyDBHelper.TBL_NAME+ " WHERE " + MyDBHelper.COL_CODE + "=" + p.getProductCode());
                dialog.dismiss();
                loadData();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //===================== MENU ===================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //---------------- Add Product ----------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnAdd){
            // Open dialog for adding product
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_add);

            EditText edtName, edtPrice;
            edtName = dialog.findViewById(R.id.edtProductName);
            edtPrice = dialog.findViewById(R.id.edtProductPrice);

            Button btnSave = dialog.findViewById(R.id.btnSave);
            Button btnClose = dialog.findViewById(R.id.btnClose);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Insert data into db
                    db.execSql("INSERT INTO " +MyDBHelper.TBL_NAME+ " VALUES(null, '"+edtName.getText().toString()+ "'," +edtPrice.getText().toString() + ")");
                    dialog.dismiss();
                    loadData();
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}