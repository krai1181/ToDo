package com.avi.todo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avi.todo.MyAssignment;
import com.avi.todo.R;
import com.avi.todo.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "MyRecyclerAdapter";
    private Context mContext;
   // public ArrayList<String> myList;
    public ArrayList<MyAssignment> myList;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference myReference;
    String userID;



    public MyAssignment item;


    //constructor
    public MyRecyclerAdapter(Context context) {
        this.myList = new ArrayList<>();
        this.mContext = context;
    }

    //add a new item to recycler
    public void add(MyAssignment ass) {
        myList.add(ass);
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        myList.remove(position);
        notifyItemRangeChanged(position, myList.size());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View item = inflater.inflate(R.layout.item, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
      //  myViewHolder.txtItem.setText(myList.get(position));

        final MyAssignment  assignment  = myList.get(position);

        myViewHolder.txtItem.setText(assignment.getItem());
        myViewHolder.txtDate.setText(assignment.getDate());
        myViewHolder.txtTime.setText(assignment.getTime());

        //change a text view
        myViewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // myViewHolder.myItem.setPaintFlags(myViewHolder.myItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });


        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:  " +  myList.get(position));
                removeAt(position);

                mAuth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                userID = user.getUid();

                myReference = database.getReference("Users").child(userID);
              // DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = myReference.child("Assignments").orderByChild("item").equalTo(assignment.getItem());
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });



            }
        });
    }



    @Override
    public int getItemCount() {
        return myList.size();
    }
}



class MyViewHolder extends RecyclerView.ViewHolder {
    public final TextView txtItem, txtDate,txtTime;
    public final ImageButton imgDelete;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtItem = itemView.findViewById(R.id.txtItem);
        txtDate = itemView.findViewById(R.id.txtDate);
        txtTime = itemView.findViewById(R.id.txtTime);
        imgDelete = itemView.findViewById(R.id.imgDelete);
    }
}


