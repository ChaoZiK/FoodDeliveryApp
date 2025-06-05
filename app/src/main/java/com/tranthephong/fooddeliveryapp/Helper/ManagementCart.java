package com.tranthephong.fooddeliveryapp.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tranthephong.fooddeliveryapp.Model.ItemsModel;

import java.util.ArrayList;

public class ManagementCart {
    private static final String TAG = "ManagementCart";
    private TinyDB tinyDB;
    private Context context;

    public ManagementCart(Context context) {
        tinyDB = new TinyDB(context);
        this.context = context;
    }

    public void insertItems(ItemsModel item) {
        Log.d(TAG, "Inserting item: " + item.getTitle());
        ArrayList<ItemsModel> listFood = getListCart();
        Log.d(TAG, "Current cart size: " + listFood.size());
        
        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                index = i;
                Log.d(TAG, "Item already exists at index: " + i);
                break;
            }
        }

        if (existAlready) {
            listFood.get(index).setNumberInCart(item.getNumberInCart());
            Log.d(TAG, "Updated existing item quantity to: " + item.getNumberInCart());
        } else {
            listFood.add(item);
            Log.d(TAG, "Added new item to cart");
        }
        
        tinyDB.putListObject("CartList", listFood);
        Log.d(TAG, "Saved cart. New size: " + listFood.size());
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ItemsModel> getListCart() {
        ArrayList<ItemsModel> cart = tinyDB.getListObject("CartList");
        Log.d(TAG, "Getting cart list. Size: " + (cart != null ? cart.size() : 0));
        return cart;
    }

    public void minusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        Log.d(TAG, "Minus item at position: " + position);
        if (listItems.get(position).getNumberInCart() == 1) {
            listItems.remove(position);
            Log.d(TAG, "Removed item from cart");
        } else {
            listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() - 1);
            Log.d(TAG, "Decreased item quantity to: " + listItems.get(position).getNumberInCart());
        }
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public void plusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        Log.d(TAG, "Plus item at position: " + position);
        listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() + 1);
        Log.d(TAG, "Increased item quantity to: " + listItems.get(position).getNumberInCart());
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    public double getTotalFee() {
        ArrayList<ItemsModel> listFood = getListCart();
        double fee = 0.0;
        for (ItemsModel item : listFood) {
            fee += item.getPrice() * item.getNumberInCart();
        }
        Log.d(TAG, "Calculated total fee: " + fee);
        return fee;
    }
}
