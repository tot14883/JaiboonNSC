package test.jaiboondemand;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeShop extends Fragment {
    private RecyclerView mSList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private View x;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x =  inflater.inflate(R.layout.activity_home_shop,null);

        mSList = (RecyclerView) x.findViewById(R.id.Shop_list);
        mSList.setHasFixedSize(true);
        mSList.setLayoutManager(new GridLayoutManager(getActivity(),2));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ShopJaiboon");

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

        FirebaseRecyclerAdapter<Insta,RecycleAdapter> FBRA = new FirebaseRecyclerAdapter<Insta, RecycleAdapter>(
                Insta.class,
                R.layout.content_main2,
                RecycleAdapter.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final RecycleAdapter viewHolder, Insta model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setPTitle(model.getNameproduct());
                viewHolder.setCount(model.getPriceproduct());
                viewHolder.setProImage(getActivity().getApplicationContext(),model.getImageproduct());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
                    }
                });
              /*  viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      /*  Intent shop_activity = new Intent(getActivity(),Shopdetail.class);
                     //   singleInstaActivity.putExtra("PostID", post_key);
                        startActivity(shop_activity);*/
                      ///  Toast.makeText(getActivity(),"Clicked",Toast.LENGTH_LONG).show();
                 /*   }
                });*/
            }
        };
        mSList.setAdapter(FBRA);
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static class RecycleAdapter extends RecyclerView.ViewHolder{
        View mView;
        public RecycleAdapter(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setPTitle(String Ptitle){
            TextView title_product = (TextView) mView.findViewById(R.id.title_Product);
            title_product.setText(Ptitle);
        }
        public void setCount(String PCount){
            TextView Count_product = (TextView) mView.findViewById(R.id.count_Product);
            Count_product.setText(PCount+" บาท");

        }
        public void setProImage(Context ctx,String image){
            ImageView img_product = (ImageView) mView.findViewById(R.id.Img_Product);
            Picasso.with(ctx).load(image).into(img_product);
        }
    }
}
