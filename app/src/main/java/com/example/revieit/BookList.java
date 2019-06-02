package com.example.revieit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.revieit.Interface.ItemClickListener;
import com.example.revieit.Model.Book;
import com.example.revieit.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BookList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference bookList;

    String categoryId="";

    FirebaseRecyclerAdapter<Book, BookViewHolder> adapter;

    //Search function
    FirebaseRecyclerAdapter<Book, BookViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //firebase
        database = FirebaseDatabase.getInstance();
        bookList = database.getReference("Books");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_book);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null)
        {
            loadListBook(categoryId);
        }

        //search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search a book...");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //when the user type their text, suggestion list will change
                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))suggest.add(search);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //restore original adapter when search bar is closed
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //show result of search adapter when search is finished
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(
                Book.class,
                R.layout.book_item,
                BookViewHolder.class,
                bookList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(BookViewHolder viewHolder, Book model, int position) {
                viewHolder.book_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.book_image);

                final Book local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //star new activity
                        Intent bookDetail = new Intent(BookList.this,BookDetail.class);
                        //send food id to new activity
                        bookDetail.putExtra("BookId",searchAdapter.getRef(position).getKey());
                        startActivity(bookDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        bookList.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Book item = postSnapshot.getValue(Book.class);
                    //adding suggested food name
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListBook(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Book, BookViewHolder>(Book.class, R.layout.book_item, BookViewHolder.class,
                bookList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(BookViewHolder viewHolder, Book model, int position) {
                viewHolder.book_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.book_image);

                final Book local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onclick(View view, int position, boolean isLongClick) {
                        //star new activity
                        Intent bookDetail = new Intent(BookList.this,BookDetail.class);
                        //send food id to new activity
                        bookDetail.putExtra("BookId",adapter.getRef(position).getKey());
                        startActivity(bookDetail);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }
}
