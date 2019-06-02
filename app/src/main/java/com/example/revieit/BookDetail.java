package com.example.revieit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.revieit.Model.Book;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookDetail extends AppCompatActivity {

    TextView book_name, book_year, book_review;
    ImageView book_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnBack;

    String bookId="";

    FirebaseDatabase database;
    DatabaseReference books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        //firebase
        database = FirebaseDatabase.getInstance();
        books = database.getReference("Books");

        //init view
        btnBack = (FloatingActionButton)findViewById(R.id.btnBack);

        book_review = (TextView) findViewById(R.id.book_review);
        book_name = (TextView) findViewById(R.id.book_name);
        book_year = (TextView) findViewById(R.id.book_year);
        book_image = (ImageView) findViewById(R.id.img_book);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(BookDetail.this, BookList.class);
                startActivity(backIntent);
            }
        });

        //get book id from intent
        if(getIntent() != null)
            bookId = getIntent().getStringExtra("BookId");
        if(!bookId.isEmpty())
        {
            getDetailBook(bookId);
        }

    }

    private void getDetailBook(String bookId) {
        books.child(bookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);

                //Set image
                Picasso.with(getBaseContext()).load(book.getImage()).into(book_image);

                collapsingToolbarLayout.setTitle(book.getName());
                book_year.setText(book.getYear());
                book_name.setText(book.getName());
                book_review.setText(book.getReview());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
