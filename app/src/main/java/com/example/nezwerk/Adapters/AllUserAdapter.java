package com.example.nezwerk.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nezwerk.ActivityCalling;
import com.example.nezwerk.Models.User;
import com.example.nezwerk.R;

import java.util.ArrayList;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.AllUsersViewHolder> {
    Activity context;
    ArrayList<User> userArrayList;
    public AllUserAdapter(Activity context, ArrayList<User> userArrayList){
        this.context=context;
        this.userArrayList=userArrayList;
    }

    public AllUserAdapter(ActivityCalling context, ArrayList<com.google.firebase.firestore.auth.User> usersArrayList) {

    }


    @NonNull
    @Override
    public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users,parent,false);
        AllUsersViewHolder allUserAdapter=new AllUsersViewHolder(view);
        return allUserAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position) {
        User nuser=userArrayList.get(position);
        holder.textViewName.setText(nuser.getName());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class AllUsersViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        Button button;
        public AllUsersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=(TextView)itemView.findViewById(R.id.itemName);
            button=itemView.findViewById(R.id.callButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User nuser=userArrayList.get(getAdapterPosition());
                    ((ActivityCalling)context).callUser(nuser);
                    //((ActivityCalling)context().callUser
                }
            });

        }
    }
}
