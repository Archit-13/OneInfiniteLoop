package com.example.booksies.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class FirestoreHandler {
    FirebaseFirestore db;
    ArrayList<Books> booksList = new ArrayList<Books>();
    ArrayList<Books> filteredList = new ArrayList<Books>();
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    private String filterString = "NO FILTER";
    String sortString = "Title";

    public FirestoreHandler(RecyclerView recyclerView,  RecyclerView.LayoutManager layoutManager, Context context){
        this.recyclerView = recyclerView;

        this.layoutManager = layoutManager;
        this.context = context;
    }

    public void listBooks(){
        db = FirebaseFirestore.getInstance();

        db = FirebaseFirestore.getInstance();

        db.collection("Books").whereEqualTo("owner", getCurrentUserEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    Log.w("error", "Listen failed.", e);
                    return;
                }
                booksList.clear();

                for (QueryDocumentSnapshot book : value) {
                    Books b = new Books(book.getString("isbn").toUpperCase(),
                                        book.getString("author").toUpperCase(),
                                        book.getString("title").toUpperCase());
                    if ((book.getString("status")).toUpperCase().equals("AVAILABLE")){
                        b.setStatus(book_status.AVAILABLE);
                    } else if ((book.getString("status").toUpperCase()).equals("REQUESTED")){
                        b.setStatus(book_status.REQUESTED);

                    } else if((book.getString("status")).toUpperCase().equals("ACCEPTED")){
                        b.setStatus(book_status.ACCEPTED);

                    } else if ((book.getString("status")).toUpperCase().equals("BORROWED")){
                        b.setStatus(book_status.BORROWED);

                    }
                    b.setImageUrl(book.getString("imageUrl"));

                    booksList.add(b);

                }

                filter();
                sort();


            }

        });
    }

    public void setFilterString(String f){
        this.filterString = f;

    }

    public void setSortString(String s){
        this.sortString = s;
    }

    public void filter(){
        if(!filterString.equals("NO FILTER")){
            filteredList.clear();
            for (Books book:booksList){
                if ((book.getStatus().toString().toUpperCase()).equals(filterString)){
                    filteredList.add(book);
                }
            }
            mAdapter = new MyAdapter(filteredList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

        } else {
            mAdapter = new MyAdapter(booksList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setHasFixedSize(true);

        }

    }

    public void sort (){
        switch (sortString){
            case "Author":
                Comparator<Books> compareById = (Books o1, Books o2) -> o1.getAuthor().compareTo( o2.getAuthor() );

                Collections.sort(booksList, compareById);
                Collections.sort(filteredList, compareById);
                mAdapter.notifyDataSetChanged();
                break;
            case "Title":
                Comparator<Books> compareByTitle = (Books o1, Books o2) -> o1.getTitle().compareTo( o2.getTitle() );

                Collections.sort(booksList, compareByTitle);
                Collections.sort(filteredList, compareByTitle);
                mAdapter.notifyDataSetChanged();
                break;

            case "ISBN":
                Comparator<Books> compareByISBN = (Books o1, Books o2) -> o1.getISBN().compareTo( o2.getISBN() );

                Collections.sort(booksList, compareByISBN);
                Collections.sort(filteredList, compareByISBN);
                mAdapter.notifyDataSetChanged();
                break;
        }


    }


    public static String getCurrentUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {

                String email = user.getEmail();
                return email;

            }
        }
        return "";

    }



}
