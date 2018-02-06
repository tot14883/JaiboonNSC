package test.jaiboondemand.Admin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.NewsFuction.News;
import test.jaiboondemand.NewsFuction.NewsDetail;
import test.jaiboondemand.R;

public class AdminManager extends Fragment {

    View x;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x = inflater.inflate(R.layout.activity_admin_manager, container, false);
        recyclerView = (RecyclerView) x.findViewById(R.id.recycle_admin_manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.hasFixedSize();

        floatingActionButton = (FloatingActionButton) x.findViewById(R.id.fab_admin);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AdminNews.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        mData = FirebaseDatabase.getInstance().getReference().child("News");
        return x;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<News,AdminPostViewHoler> adapter = new FirebaseRecyclerAdapter<News, AdminPostViewHoler>(
                News.class,
                R.layout.row_news,
                AdminPostViewHoler.class,
                mData
        ) {
            @Override
            protected void populateViewHolder(final AdminPostViewHoler viewHolder, News model, int position) {
                final String post_key = getRef(position).getKey().toString();
                final int position1 = position;
                viewHolder.setTitleNews(model.getTopicname());
                viewHolder.setImageNews(getActivity().getApplicationContext(),model.getImagenews());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent news = new Intent(getActivity(),NewsDetail.class);
                        news.putExtra("PostNews",post_key);
                        startActivity(news);
                    }
                });
                viewHolder.setImageInView();
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(getActivity(),viewHolder.imageView);
                        popup.getMenuInflater().inflate(R.menu.menu_delete_post,popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getTitle().equals("Delete")) {
                                    getRef(position1).removeValue();
                                }
                                Toast.makeText(getActivity(), "Delete Success !!!", Toast.LENGTH_LONG).show();
                                return true;
                            }
                        });
                        popup.show();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
         mAuth.addAuthStateListener(mAuthListener);
    }
    public static class AdminPostViewHoler extends RecyclerView.ViewHolder{
        private ImageView imageView;
        View mView;
        public AdminPostViewHoler(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setImageInView(){
            imageView = (ImageView) mView.findViewById(R.id.overflow_news);
        }

        public void setTitleNews(String title){
            TextView titlenews = (TextView) mView.findViewById(R.id.textTitle_news);
            titlenews.setText(title);
        }
        public void setImageNews(final Context ctx, final String image){
            final ImageView image_news = (ImageView) mView.findViewById(R.id.post_image_news);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(image_news, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
    }
}