package com.levietduc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.levietduc.models.Product;
import com.levietduc.sqllite_ex2.MainActivity;
import com.levietduc.sqllite_ex2.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    MainActivity activity;
    int itemLayout;
    List<Product> products;

    // Constructor
    public ProductAdapter(MainActivity activity, int itemLayout, List<Product> products) {
        this.activity = activity;
        this.itemLayout = itemLayout;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout,null);

            //Binding views
            holder.txtProductInfo = view.findViewById(R.id.txtProductInfo);
            holder.imvEdit = view.findViewById(R.id.imvEdit);
            holder.imvDelete = view.findViewById(R.id.imvDelete);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        //Binding Data
        Product p = products.get(i);
        holder.txtProductInfo.setText(p.getProductName() + " - " + p.getProductPrice());

        holder.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open dialog for editing product
                activity.openEditDialog(p);
            }
        });

        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openDeleteDialog(p);
            }
        });

        return view;
    }

    public static class ViewHolder{
        TextView txtProductInfo;
        ImageView imvEdit,imvDelete;
    }
}
