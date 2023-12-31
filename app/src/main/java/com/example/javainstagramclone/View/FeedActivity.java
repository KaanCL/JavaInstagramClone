package com.example.javainstagramclone.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.javainstagramclone.Adapter.PostAdapter;
import com.example.javainstagramclone.Model.Posts;
import com.example.javainstagramclone.R;
import com.example.javainstagramclone.databinding.ActivityFeedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Posts> postArrayList;
    private ActivityFeedBinding binding;
    PostAdapter postAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<Posts>();

        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getData();



        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter=new PostAdapter(postArrayList);
        binding.RecyclerView.setAdapter(postAdapter);



    }

    private void getData(){

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null){
                    Toast.makeText(FeedActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                }

                if(value!=null){

                    for(DocumentSnapshot snapshot: value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();

                        String comment =(String) data.get("comment");
                        String userEmail=(String) data.get("useremail");
                        String dowloadUrl=(String) data.get("dowloadurl");

                        Posts posts = new Posts(userEmail,comment,dowloadUrl);
                       postArrayList.add(posts);

                    }
                    postAdapter.notifyDataSetChanged();


                }






            }
        });






    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.add_post){
            Intent intenttoupload = new Intent(FeedActivity.this, UploadActivity.class);
            startActivity(intenttoupload);
        }
        else if (item.getItemId()==R.id.signout){
            auth.signOut();

            Intent intenttomain = new Intent(FeedActivity.this,MainActivity.class);
            startActivity(intenttomain);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


}