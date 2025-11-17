package com.mb.kibeti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShoppingBudget extends AppCompatActivity {

    private EditText etItemPrice;
    private EditText etQnty;
    private TextInputEditText etBudget;
    private TextView tvRemainingBudget;
    private ListView lvItems;
    private Button btnAddItem, btnRemoveItem;
    DecimalFormat formatter;
    AutoCompleteTextView autoCompleteTextView, etItemName;
    ArrayList<String> categories = new ArrayList<String>();

    private double budget = 0.0;
    private double remainingBudget = 0.0;
    private ArrayList<String> itemList;
    private ArrayList<String> shopItemList;
    private ArrayAdapter<String> itemAdapter;
    ArrayAdapter<String> shopListAdapter;
    Spinner shopList;

//        String[] itemArray = new String[]{"bread","milk","Sugar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_budget);

        etBudget = findViewById(R.id.etBudget);
        etItemName = findViewById(R.id.etItemName);
        etQnty = findViewById(R.id.etQnty);
        etItemPrice = findViewById(R.id.etItemPrice);
        tvRemainingBudget = findViewById(R.id.tvRemainingBudget);
        lvItems = findViewById(R.id.lvItems);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnRemoveItem = findViewById(R.id.btnRemoveItem);
        shopList = findViewById(R.id.idShop);
//            autoCompleteTextView =  findViewById(R.id.autoCompleteTextView);


        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("###,###,###,###");

        categories.add("-Select Shop-");
        categories.add("Carrefour");
        categories.add("Naivas");
        categories.add("Quickmart");
        categories.add("Cleanshelf");
        categories.add("Home shop");
        categories.add("Other");

         shopListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);


        shopListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopList.setAdapter(shopListAdapter);

        itemList = new ArrayList<>();
        shopItemList = new ArrayList<>();
        boolean isConnected = ConnectivityHelper.isConnectedToNetwork(this);

        if (isConnected) {
//            Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show();
            getItemList();
            getShopList();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, itemList);
        lvItems.setAdapter(itemAdapter);
        lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShoppingBudget.this,
                android.R.layout.simple_list_item_1, shopItemList);
        etItemName.setAdapter(arrayAdapter);


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                new AlertDialog.Builder(ShoppingBudget.this)
                        .setTitle("Do you want to remove this shopping from the list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });


        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    budget = Double.parseDouble(s.toString());
                    remainingBudget = budget - calculateTotalItemPrices();
                    updateRemainingBudget();
                } catch (NumberFormatException e) {
                    budget = 0.0;
                    remainingBudget = 0.0;
                    updateRemainingBudget();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etQnty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateRemainingBudget();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etItemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateRemainingBudget();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        btnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem();
            }
        });
    }

    private void getItemList() {


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/shop_list_items.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                shopItemList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){

                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {

                        String object1 = jsonArray.getString(i);

                        shopItemList.add(object1);
                    }

//                    }
                    itemAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ShoppingBudget.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


        };

        queue.add(request);
    }

    private void getShopList() {


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, "https://mwalimubiashara.com/app/shop_list.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);

                categories.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

//                    if(success.equals("1")){

                    int l = 0;
                    l = jsonArray.length();

                    for (int i = 0; i < l; i++) {

                        String object1 = jsonArray.getString(i);

                        categories.add(object1);
                    }

//                    }
                    shopListAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ShoppingBudget.this, "Fail to get response ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {

                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


        };

        queue.add(request);
    }

    private void addItem() {
        String itemName = etItemName.getText().toString();
        String itemPriceInput = etItemPrice.getText().toString();

        if (itemName.isEmpty() || itemPriceInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double itemPrice = Double.parseDouble(itemPriceInput);

        if (itemPrice > (budget - calculateTotalItemPrices())) {
            Toast.makeText(this, "Insufficient budget", Toast.LENGTH_SHORT).show();
            return;
        }

        String qntyInput = etQnty.getText().toString();
        double qnty = 0.0;
        if (qntyInput.isEmpty()) {
            qnty = 1;
        } else {
            qnty = Double.parseDouble(qntyInput);

        }

        itemList.add(itemName + " - " + (itemPrice*qnty));
        itemAdapter.notifyDataSetChanged();

        remainingBudget -= itemPrice;
        updateRemainingBudget();
        clearItemFields();
    }

    private void removeItem() {
        int position = lvItems.getCheckedItemPosition();
        if (position >= 0 && position < itemList.size()) {
            String selectedItem = itemList.get(position);
            String[] itemParts = selectedItem.split(" - ");
            double itemPrice = Double.parseDouble(itemParts[1]);

            itemList.remove(position);
            itemAdapter.notifyDataSetChanged();

            remainingBudget += itemPrice;
            updateRemainingBudget();
        } else {
            Toast.makeText(this, "Please select an item to remove", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRemainingBudget() {
        tvRemainingBudget.setText("Remaining Budget: " + String.format("%.2f", remainingBudget));
    }

    private void calculateRemainingBudget() {
        String itemPriceInput = etItemPrice.getText().toString();
        if (itemPriceInput.isEmpty()) {
            remainingBudget = budget - calculateTotalItemPrices();
        } else {
            String qntyInput = etQnty.getText().toString();
            double qnty = 0.0;
            if (qntyInput.isEmpty()) {
                etQnty.setText("1");
                qnty = 1;
            } else {
                qnty = Double.parseDouble(qntyInput);

            }
            double itemPrice = Double.parseDouble(itemPriceInput);
            remainingBudget = budget - calculateTotalItemPrices() - (itemPrice*qnty);
        }
        updateRemainingBudget();
    }

    private double calculateTotalItemPrices() {

        double total = 0.0;
        for (String item : itemList) {
            String[] itemParts = item.split(" - ");
            total += Double.parseDouble(itemParts[1]);
        }
        return total;
    }

    private void clearItemFields() {
        etItemName.setText("");
        etItemPrice.setText("");
        etQnty.setText("");
        lvItems.clearChoices();
    }
}
