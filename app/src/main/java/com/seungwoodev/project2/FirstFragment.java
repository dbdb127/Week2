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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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

        data = new ArrayList<>();
        adapter = new ExpandableListAdapter(data);

        //get titles, prices, qty from database
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<CategoryResult> call = retrofitInterface.getCategory();

        call.enqueue(new Callback<CategoryResult>(){
            @Override
            public void onResponse(Call<CategoryResult> call, retrofit2.Response<CategoryResult> response) {
                CategoryResult result = response.body();
                if(result.getCode()==200){
                    ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
                    tmp = result.getName();

//                    for(int i=0; i<tmp.size();i++){
//                        data.add(new Item(0, tmp.get(i).get(0)));
//                        for(int j=1;j<tmp.get(i).size();j++){
//                            data.add(new Item(1, tmp.get(i).get(j)));
//                        }
//                    }
                    for(int i=0;i<tmp.size();i++){
                        Item main = new Item(0, tmp.get(i).get(0));
                        for(int j=1;j<tmp.get(i).size();j++){
                            main.invisibleChildren.add(new Item(1, tmp.get(i).get(j)));
                        }
                        data.add(main);
                    }

                    LinearLayoutManager mLineaerLayoutManager = new LinearLayoutManager(getActivity());
                    mRecyclerView.setLayoutManager(mLineaerLayoutManager);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.setHasFixedSize(true);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLineaerLayoutManager.getOrientation());
                    mRecyclerView.addItemDecoration(dividerItemDecoration);

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

        adapter.setOnItemClickListener(new ExpandableListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Item item) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProductActivity.class);
                intent.putExtra("subCategory", item.getText());
                startActivity(intent);
            }
        });
    }
}