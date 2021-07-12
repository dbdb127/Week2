package com.seungwoodev.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public ArrayList<Item> data;
    private ExpandableListAdapter adapter;
    private FloatingActionButton button;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http:192.249.18.167";

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview);
        button = view.findViewById(R.id.fab);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BasketActivity.class);
                startActivity(intent);
            }
        });

        data = new ArrayList<Item>();

        //get titles, prices, qty from database
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<CategoryResult> call = retrofitInterface.getMainCategory();

        call.enqueue(new Callback<CategoryResult>(){
            @Override
            public void onResponse(Call<CategoryResult> call, retrofit2.Response<CategoryResult> response) {
                CategoryResult result = response.body();
                if(result.getCode()==200){
                    ArrayList<String> tmp;
                    tmp = result.getName();

//                    for(int i=0; i<tmp.size();i++){
//                        data.add(new Item(0, tmp.get(i).get(0)));
//                        for(int j=1;j<tmp.get(i).size();j++){
//                            data.add(new Item(1, tmp.get(i).get(j)));
//                        }

                    Log.d("kyung", tmp.toString());
//                    }
                    for(int i=0;i<tmp.size();i++){
                        Item main = new Item(0, tmp.get(i));
                        data.add(main);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("category_main", tmp.get(i));
//                        Log.d("kyung", tmp.get(i));
                        Call<CategoryResult> callSub = retrofitInterface.getSubCategory(map);

                        callSub.enqueue(new Callback<CategoryResult>() {
                            @Override
                            public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                                CategoryResult res = response.body();
                                Log.d("kyung code", res.getCode().toString()+map.get("category_main"));

                                if(res.getCode()==200){
                                    ArrayList<String> temp;
                                    temp = res.getName();
                                    Log.d("kyung", temp.toString());

                                    Item addItem = data.get(0);
                                    for(int a=0;a<tmp.size();a++){
                                        if(data.get(a).text.equals(map.get("category_main"))){
                                            addItem = data.get(a);
                                            addItem.invisibleChildren = new ArrayList<Item>();
                                            break;
                                        }
                                    }
                                    Log.d("kyung", addItem.text);
                                    for(int j=0;j<temp.size();j++){
                                        addItem.invisibleChildren.add(new Item(1, temp.get(j)));
                                    }
                                    Log.d("addItem", addItem.invisibleChildren.toString());

                                }else if(res.getCode()==404){
//                                    Toast.makeText(getActivity().getApplicationContext(),"으앙앙", Toast.LENGTH_SHORT).show();
                                }

                                adapter = new ExpandableListAdapter(data);
                                adapter.setOnItemClickListener(new ExpandableListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, Item item) {
                                        Intent intent = new Intent(getActivity().getApplicationContext(), ProductActivity.class);
                                        intent.putExtra("subCategory", item.getText());
                                        startActivity(intent);
                                    }
                                });

                                LinearLayoutManager mLineaerLayoutManager = new LinearLayoutManager(getActivity());
                                mRecyclerView.setLayoutManager(mLineaerLayoutManager);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setHasFixedSize(true);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLineaerLayoutManager.getOrientation());
                                mRecyclerView.addItemDecoration(dividerItemDecoration);
                            }
                            @Override
                            public void onFailure(Call<CategoryResult> call, Throwable t) {
                                Log.d("kyung fail", map.get("category_main"));
                            }
                        });
                    }

                    Log.d("kyung", String.valueOf(data.size()));
//                    for(int a=0;a<data.size();a++){
//                        Log.d("kyung", data);
//                    }
//                    adapter = new ExpandableListAdapter(data);
//
//                    adapter.setOnItemClickListener(new ExpandableListAdapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(int position, Item item) {
//                            Intent intent = new Intent(getActivity().getApplicationContext(), ProductActivity.class);
//                            intent.putExtra("subCategory", item.getText());
//                            startActivity(intent);
//                        }
//                    });
//
//                    LinearLayoutManager mLineaerLayoutManager = new LinearLayoutManager(getActivity());
//                    mRecyclerView.setLayoutManager(mLineaerLayoutManager);
//                    mRecyclerView.setAdapter(adapter);
//                    mRecyclerView.setHasFixedSize(true);
//                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLineaerLayoutManager.getOrientation());
//                    mRecyclerView.addItemDecoration(dividerItemDecoration);

                }else if(result.getCode()==404){
                    Toast.makeText(getActivity().getApplicationContext(),"No Products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoryResult> call, Throwable t){
                Log.d("failed", "connection "+call);
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}