package com.seungwoodev.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> names;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Integer> mImages;
    private BasketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        mRecyclerView = findViewById(R.id.recyclerView);

        names = new ArrayList<>();
        prices = new ArrayList<Integer>();
        qty = new ArrayList<Integer>();
        mImages = new ArrayList<>();

        //product list
//        mImages.add(R.drawable.ic_baseline_checkroom_24);
//        names.add("temp");
//        qty.add(1);
//        prices.add(12345);

        //db에서 basket 가져오기
        Retrofit retrofit;
        RetrofitInterface retrofitInterface;
        String BASE_URL = "http:192.249.18.167:80";

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();

        map.put("email", MainActivity_Tab.getUser());

        Call<ProductResult> call = retrofitInterface.getBasket(map);

        call.enqueue(new Callback<ProductResult>(){
            @Override
            public void onResponse(Call<ProductResult> call, retrofit2.Response<ProductResult> response) {
                if(response.code()==200){
                    ProductResult result = response.body();
                    names = result.getName();   //ArrayList
                    prices = result.getPrice();
                    qty = result.getQty();

                    for(int i=0;i<names.size();i++){
                        mImages.add(R.drawable.a);
//                        Log.d("kyung", names.get(i));
                    }

                    adapter = new BasketAdapter(BasketActivity.this, names, prices, qty, mImages);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BasketActivity.this, 1, GridLayoutManager.VERTICAL, false);

                    mRecyclerView.setAdapter(adapter);

                    mRecyclerView.setLayoutManager(gridLayoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }else if(response.code()==404){
                    Toast.makeText(BasketActivity.this,"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(BasketActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}