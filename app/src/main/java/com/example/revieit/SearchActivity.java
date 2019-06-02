package com.example.revieit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.revieit.Interface.ItemClickListener;
import com.example.revieit.Model.Book;
import com.example.revieit.ViewHolder.BookViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    FirebaseRecyclerAdapter<Book, BookViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    RecyclerView recyclerView;

    String categoryId="";

    FirebaseRecyclerAdapter<Book, BookViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //firebase
        database = FirebaseDatabase.getInstance();
        bookList = database.getReference("Books");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_search);


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

        //search all books
        loadAllBooks();

    }

    private void loadAllBooks() {

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
                        Intent bookDetail = new Intent(SearchActivity.this,BookDetail.class);
                        //send food id to new activity
                        bookDetail.putExtra("BookId",adapter.getRef(position).getKey());
                        startActivity(bookDetail);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);

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
                        Intent bookDetail = new Intent(SearchActivity.this,BookDetail.class);
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
        bookList.addListenerForSingleValueEvent(new ValueEventListener() {
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

}
