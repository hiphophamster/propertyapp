package com.example.hansi.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button uploadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);



        //initialize recyclerview and FIrebase objects
        uploadBtn = (Button)findViewById(R.id.uploadBtn);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(uploadIntent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("HappyHomes");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mAuth.getCurrentUser()==null){
                    Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerOptions<HappyHomes> options =
                new FirebaseRecyclerOptions.Builder<HappyHomes>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("HappyHomes").limitToLast(1000), HappyHomes.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<HappyHomes, HappyHomesViewHolder>
                FBRA = new FirebaseRecyclerAdapter<HappyHomes, HappyHomesViewHolder>(options) {

            @Override
            public HappyHomesViewHolder onCreateViewHolder(ViewGroup group, int position) {
                return new HappyHomesViewHolder(LayoutInflater.from(group.getContext())
                    .inflate(R.layout.card_items, group, false));
            }

            @Override
            protected void onBindViewHolder(HappyHomesViewHolder viewHolder, int position, HappyHomes model) {
                 final String post_key = getRef(position).getKey().toString();
                 viewHolder.setTitle(model.getLocation());
                 viewHolder.setDesc(model.getDesc());
                 viewHolder.setImageUrl(getApplicationContext(), model.getImageUrl());
                 viewHolder.setUserName(model.getUsername());
                 viewHolder.setPrice(model.getPrice());
                 viewHolder.mView.setOnClickListener(new View.OnClickListener() {

                     @Override
                     public void onClick(View view) {
                         Intent singleActivity = new Intent(MainActivity.this, SinglePostActivity.class);
                         singleActivity.putExtra("PostID", post_key);
                         startActivity(singleActivity);

                     }
                 });
            }
         };

        recyclerView.setAdapter(FBRA);

    }

    public static class HappyHomesViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public HappyHomesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setTitle(String title){
            TextView post_title = mView.findViewById(R.id.property_location_textview);
            post_title.setText(title);

        }
        public void setDesc(String desc){
            TextView post_desc = mView.findViewById(R.id.property_description_textview);
            post_desc.setText(desc);

        }
        public void setImageUrl(Context ctx, String imageUrl){
            ImageView post_image = mView.findViewById(R.id.post_image);
            Picasso.get().load(imageUrl).into(post_image);

        }
        public void setUserName(String userName){
            TextView postUserName = mView.findViewById(R.id.post_user);
            postUserName.setText(userName);

        }
        public void setPrice(String price) {
            TextView postPrice = mView.findViewById(R.id.property_price_textview);
            postPrice.setText(price);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;

        }
        else if (id == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, UploadActivity.class));

        }
        else if (id == R.id.logout){
            mAuth.signOut();
            Intent logouIntent = new Intent(MainActivity.this, RegisterActivity.class);
            logouIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logouIntent);

        }
        return super.onOptionsItemSelected(item);

    }
}