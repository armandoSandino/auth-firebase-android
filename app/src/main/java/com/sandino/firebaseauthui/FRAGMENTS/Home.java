package com.sandino.firebaseauthui.FRAGMENTS;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sandino.firebaseauthui.ADAPTERS.ProductAdapter;
import com.sandino.firebaseauthui.MODELS.Product;
import com.sandino.firebaseauthui.MODELS.product_getter_setter;
import com.sandino.firebaseauthui.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {

    RecyclerView recyclerView;
    Query query1;
    FirebaseRecyclerAdapter<product_getter_setter, BlogViewHolder> firebaseRecyclerAdapter;

    List<Product> listProduct;
    private DatabaseReference mdatabasereference;
    private ProgressDialog progressDialog;
    //LinearLayoutManager mLayoutManager;
    RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mProductAdapter;
    private RecyclerView mRecyclerViewProduct;
    Context contextFragInicio;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog( getActivity());
        progressDialog.setMessage("Loading Products Please Wait...");
        progressDialog.setCancelable( false );
        progressDialog.show();


        //mdatabasereference = FirebaseDatabase.getInstance().getReference("products").child("accessories");
        mdatabasereference = FirebaseDatabase.getInstance().getReference().child("products").child("accessories");
        mRecyclerViewProduct = view.findViewById(R.id.rcListProduct );
        mLayoutManager = new LinearLayoutManager( getContext() );

        contextFragInicio = getActivity();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if ( dataSnapshot.exists() ) {

                    //
                    listProduct  = new ArrayList<Product>();
                    //

                     for (DataSnapshot  item:dataSnapshot.getChildren()  ) {
                         Product product = item.getValue( Product.class );
                         listProduct.add(product);
                     }
                     //
                    mProductAdapter = new ProductAdapter( listProduct, R.layout.item_product , getContext(), new ProductAdapter.MiClickItemListener() {
                        @Override
                        public void MiClickItem(Product product, int position) {

                        }
                    });
                     mRecyclerViewProduct.setItemAnimator( new DefaultItemAnimator() );
                     mRecyclerViewProduct.setLayoutManager( mLayoutManager );
                     mRecyclerViewProduct.setAdapter(mProductAdapter );

                    //
                } else {
                    Toast.makeText( getActivity() , " No existen datos ", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(  "Upss....", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        //add listener
        mdatabasereference.addValueEventListener(postListener);

        //this.loadList();

    }
    private void loadList() {

        query1 = FirebaseDatabase.getInstance().getReference().child("products").child("accessories");
        FirebaseRecyclerOptions<product_getter_setter> options =
                new FirebaseRecyclerOptions.Builder<product_getter_setter>()
                        .setQuery(query1, product_getter_setter.class)
                        .build();

        Log.d("Options"," data : "+options);
        Toast.makeText( getActivity() , " OPCIONES "+ options.toString() , Toast.LENGTH_SHORT).show();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<product_getter_setter, BlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder blogViewHolder, final int i, @NonNull product_getter_setter product_get_set_v) {
                blogViewHolder.setname(product_get_set_v.getName());
                String image_url =blogViewHolder.setimage(product_get_set_v.getImage());
                String link= product_get_set_v.getLink();
                Log.d("LINKDATA"," data : "+link);
                blogViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productid=getRef(i).getKey();
                        Log.d("productid"," data : "+productid);
                        mdatabasereference.child(productid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String finallink = dataSnapshot.child("link").getValue(String.class);
                                Log.d("productLink"," data : "+finallink);
                                if(finallink!=null)
                                {
                                    Uri uriUrl = Uri.parse(finallink);
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.customlayout, parent, false);
                progressDialog.dismiss();
                return new BlogViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        GridLayoutManager gridLayoutManager = new GridLayoutManager( getActivity() , 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mView=itemView;
        }
        public void setname(String name)
        {
            TextView ename=(TextView)mView.findViewById(R.id.text1);
            ename.setText(name);
        }
        public String setimage(String url)
        {
            ImageView image = (ImageView)mView.findViewById(R.id.productimage);
            Picasso.get().load(url).into(image);
            return url;
        }
    }

}