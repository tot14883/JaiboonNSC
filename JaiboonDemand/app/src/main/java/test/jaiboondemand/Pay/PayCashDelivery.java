package test.jaiboondemand.Pay;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import test.jaiboondemand.DonateMain.ProfileCustomer;
import test.jaiboondemand.DonateMain.ProfileFoundation;
import test.jaiboondemand.DonateMain.ProfileTemple;
import test.jaiboondemand.R;

public class PayCashDelivery extends AppCompatActivity {
    private TextView text_edit_local,text_total;
    private TextView text_Name,text_Phone,text_Address;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private DatabaseReference mDataSend;
    private String txt_price="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_cash_delivery);
        text_edit_local = (TextView) findViewById(R.id.edit_local);
        text_Name = (TextView) findViewById(R.id.name_own_id);
        text_Phone = (TextView) findViewById(R.id.phone_own_id);
        text_Address = (TextView) findViewById(R.id.text_place_current);
        text_total = (TextView) findViewById(R.id.total_price_send);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_pay_cash_delivery);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(PayCashDelivery.this));

        mDataSend = FirebaseDatabase.getInstance().getReference().child("Cart");
        mDataSend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Integer[] total_price = {0};
               for(DataSnapshot d: dataSnapshot.getChildren()){
                   if (d.child("uid").getValue().equals(mAuth.getCurrentUser().getUid())){
                       String Total_price = (String) String.valueOf(d.child("priceproduct").getValue());
                       String name_price = (String) String.valueOf(d.child("nameproduct").getValue());
                       String amount = (String) String.valueOf(d.child("amount").getValue());
                       int price =Integer.parseInt(Total_price)*Integer.parseInt(amount);
                       total_price[0] = (total_price[0]+price);
                       txt_price+="- "+name_price+" ราคา "+Total_price +" บาท จำนวน "+amount+" ชิ้น รวม "+String.valueOf(price)+" บาท\n";
                   }
               }
               text_total.setText(String.valueOf(total_price[0]));
                txt_price+="ราคารวม\t"+String.valueOf(total_price[0])+" บาท";

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    ShowInfomation();
                    text_edit_local.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Selected").exists()) {
                                        if (dataSnapshot.child("Selected").getValue().equals("Customer")) {
                                            Intent intent = new Intent(PayCashDelivery.this, ProfileCustomer.class);
                                            intent.putExtra("Current", "Yes");
                                            startActivity(intent);
                                        }
                                        if (dataSnapshot.child("Selected").getValue().equals("Temple")) {
                                            Intent intent = new Intent(PayCashDelivery.this, ProfileTemple.class);
                                            intent.putExtra("Current", "Yes");
                                            startActivity(intent);
                                        }
                                        if (dataSnapshot.child("Selected").getValue().equals("Foundation")) {
                                            Intent intent = new Intent(PayCashDelivery.this, ProfileFoundation.class);
                                            intent.putExtra("Current", "Yes");
                                            startActivity(intent);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
                else if(firebaseAuth.getCurrentUser() == null){

                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<ProductSend,PayCashViewHolder> adapter = new FirebaseRecyclerAdapter<ProductSend, PayCashViewHolder>(
                ProductSend.class,
                R.layout.row_pay_cash,
                PayCashViewHolder.class,
                mDataSend.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid())

        ) {
            @Override
            protected void populateViewHolder(PayCashViewHolder viewHolder, ProductSend model, int position) {
                     viewHolder.setName(model.getNameproduct());
                     viewHolder.setImage(getApplicationContext(),model.getImageproduct());
                     viewHolder.setprice("ราคาชิ้นละ "+model.getPriceproduct());
                     viewHolder.setAmout("จำนวน "+model.getAmount()+" ชิ้น");
            }
        };
        recyclerView.setAdapter(adapter);
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void btnCashsend(View view) {
        EmailSetting getemail = new EmailSetting();
        String email = mAuth.getCurrentUser().getEmail();
        final String Subject,text;
        Subject="แจ้งรายการสั่งซื้อสินค้า"+email;
        text="คุณ "+text_Name.getText().toString()+"\nที่อยู่ "+text_Address.getText().toString()+"\nเบอร์ "+text_Phone.getText().toString()+"\nรายการสั่งซื้อสินค้าทั้งหมด\n"+txt_price;
        BackgroundMail send = BackgroundMail.newBuilder(getApplicationContext())
                .withUsername(getemail.Username)
                .withPassword(getemail.Password)
                .withSenderName("JaiboonDemand")
                .withMailTo(email)
                .withMailBcc(getemail.Username)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(Subject)
                .withBody(text)
                .withProcessVisibility(false)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"เรียบร้อย กรุณาตรวจสอบอีเมลของท่าน",Toast.LENGTH_LONG);
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {

                    }
                })
                .send();

    }

    public static class PayCashViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public PayCashViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setAmout(String amount){
           TextView text_amount = (TextView) mView.findViewById(R.id.text_amount);
           text_amount.setText(amount);
        }
        public void setName(String nameproduct){
            TextView text_name = (TextView) mView.findViewById(R.id.product_name_send);
            text_name.setText(nameproduct);
        }
        public void setprice(String priceproduct){
            TextView text_price = (TextView) mView.findViewById(R.id.product_price_send);
            text_price.setText(priceproduct+" บาท");
        }
        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_photo_send);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
    public void ShowInfomation(){
        mDatabase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  if(dataSnapshot.child("Selected").exists()){
                      if(dataSnapshot.child("Selected").getValue().equals("Customer")){
                          String post_name = (String)dataSnapshot.child("Name_Cus").getValue();
                          String address = (String)dataSnapshot.child("Address_Cus").getValue();
                          String post = (String)dataSnapshot.child("Post_Cus").getValue();
                          String country = (String)dataSnapshot.child("Country_Cus").getValue();
                          String Phone = (String)dataSnapshot.child("Phone_Cus").getValue();
                          text_Name.setText(post_name);
                          text_Phone.setText("เบอร์โทร"+Phone);
                          text_Address.setText("ที่อยู่"+address+"\n"+"รหัสไปรษณีย์"+post+"\n"+"จังหวัด"+country);
                      }
                      if(dataSnapshot.child("Selected").getValue().equals("Temple")){
                          String post_name = (String)dataSnapshot.child("Name_Owner").getValue();
                          String address = (String)dataSnapshot.child("Address").getValue();
                          String post = (String)dataSnapshot.child("Post").getValue();
                          String country = (String)dataSnapshot.child("Country").getValue();
                          String Phone = (String)dataSnapshot.child("Phone").getValue();
                          text_Name.setText(post_name);
                          text_Phone.setText(Phone);
                          text_Address.setText(address+"\n"+post+"\n"+country);
                      }
                      if(dataSnapshot.child("Selected").getValue().equals("Foundation")){
                          String post_name = (String)dataSnapshot.child("Name_Owner").getValue();
                          String address = (String)dataSnapshot.child("Address").getValue();
                          String post = (String)dataSnapshot.child("Post").getValue();
                          String country = (String)dataSnapshot.child("Country").getValue();
                          String Phone = (String)dataSnapshot.child("Phone").getValue();
                          text_Name.setText(post_name);
                          text_Phone.setText(Phone);
                          text_Address.setText(address+"\n"+post+"\n"+country);
                      }
                  }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

