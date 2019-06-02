package com.example.revieit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.revieit.Model.Account;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDetails extends AppCompatActivity {

    TextView account_name, account_password;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnBack;

    String accountId="";

    FirebaseDatabase database;
    DatabaseReference accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        //firebase
        database = FirebaseDatabase.getInstance();
        accounts = database.getReference("User");

        btnBack = (FloatingActionButton)findViewById(R.id.btnBack);

        account_name = (TextView) findViewById(R.id.account_name);
        account_password = (TextView) findViewById(R.id.account_password);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(AccountDetails.this, BookList.class);
                startActivity(backIntent);
            }
        });

        //get book id from intent
        if(getIntent() != null)
            accountId = getIntent().getStringExtra("AccountId");
        if(!accountId.isEmpty())
        {
            getDetailAccount(accountId);
        }
    }

    private void getDetailAccount(String accountId) {
        accounts.child(accountId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);

                collapsingToolbarLayout.setTitle(account.getName());
                account_name.setText(account.getName());
                account_password.setText(account.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
