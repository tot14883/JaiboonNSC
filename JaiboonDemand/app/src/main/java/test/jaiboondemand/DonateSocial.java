package test.jaiboondemand;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DonateSocial extends Fragment {
    private RecyclerView mIBstaList;
    private DatabaseReference mDatabase,databaseReference;
    private Query mDatatype;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View x;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_donate_social,container,false);

        mIBstaList = (RecyclerView) x.findViewById(R.id.insta_list);
        mIBstaList.setHasFixedSize(true);
        mIBstaList.setLayoutManager(new GridLayoutManager(getActivity(),2));


       databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
       mDatatype = FirebaseDatabase.getInstance().getReference().child("Jaiboon").orderByChild("Type").equalTo("Temple");


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
            }
        };



        return x;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Insta,InstaViewHolde> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHolde>(
                Insta.class,
                R.layout.insta_row,
                InstaViewHolde.class,
                mDatatype
        ) {
            @Override
            protected void populateViewHolder(final InstaViewHolde viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getName());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleInstaActivity = new Intent(getActivity(), SingleInstaActivity.class);
                        singleInstaActivity.putExtra("PostID", post_key);
                        startActivity(singleInstaActivity);
                    }
                });
                viewHolder.setImageView();

            }
        };
        mIBstaList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);

    }
    public static class InstaViewHolde extends RecyclerView.ViewHolder{
        View mView;
        private ImageView imageView;
        public InstaViewHolde(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setImageView(){
            imageView = (ImageView) mView.findViewById(R.id.overflow);
            imageView.setVisibility(View.INVISIBLE);
        }
        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }
        public void setDesc(String Name){
            TextView post_Name = (TextView) mView.findViewById(R.id.text_Name);
            post_Name.setText(Name);
        }
        public void setImage(Context ctx,String image){
           ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
           Picasso.with(ctx).load(image).into(post_image);
        }

    }

}

