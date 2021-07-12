package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public ArrayList<String> names;
    public ArrayList<Integer> prices;
    public ArrayList<Integer> qty;
    public ArrayList<Integer> mImages;
    private ProductAdapter adapter;
    private String subCategory;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        subCategory = intent.getStringExtra("subCategory");

        mRecyclerView = findViewById(R.id.recyclerview);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<Integer>();

        //get titles, prices, qty from database
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("subCategory", subCategory);
        Call<ProductResult> call = retrofitInterface.getProduct(map);

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                ProductResult result = response.body();
                if(result.getCode()==200){
                    names = result.getName();   //ArrayList
                    prices = result.getPrice();
                    qty = result.getQty();

                    for(int i=0;i<names.size();i++){
                        mImages.add(R.drawable.a);
//                        Log.d("kyung", names.get(i));
                    }

                    adapter = new ProductAdapter(ProductActivity.this, names, prices, qty, mImages);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductActivity.this, 2, GridLayoutManager.VERTICAL, false);

                    mRecyclerView.setAdapter(adapter);

                    mRecyclerView.setLayoutManager(gridLayoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }else if(result.getCode()==404){
                    Toast.makeText(ProductActivity.this.getApplicationContext(),"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(ProductActivity.this.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
