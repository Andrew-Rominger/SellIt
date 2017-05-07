package com.sellit.testdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Josh on 4/29/2017.
 */

//Fragment placed into the item detail page, to show comments list on a specific item.
public class CommentFragment extends Fragment {

    String prevComment;
    RecyclerView recView;
    EditText addCommentBox;
    ImageView addCommentButton;
    String postID;
    String TAG = CommentFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //Removes addCommentBox focus when the page loads
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        postID = getArguments().getString("postID");
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recView = (RecyclerView) view.findViewById(R.id.commentRecView);
        addCommentBox = (EditText) view.findViewById(R.id.addCommentBox);
        addCommentButton = (ImageView) view.findViewById(R.id.addCommentButton);
        prevComment = (String) "Null";
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = addCommentBox.getText().toString();
                if (!comment.isEmpty() && !comment.equals(prevComment)) {
                    Comment c = new Comment(postID, FirebaseAuth.getInstance().getCurrentUser().getUid(), comment, Calendar.getInstance().getTime());
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    String key = mRef.push().getKey();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/comments/" + key, c.toMap());
                    mRef.updateChildren(childUpdates);
                    addCommentBox.setText("");
                    prevComment = (String) comment;
                    addCommentBox.clearComposingText();

                } else if (comment.equals(prevComment)) {
                    Toast.makeText(getActivity().getApplicationContext(), "You cannot post the same comment twice.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Comments cannot be empty.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        setupRec(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRec(View view) {
        getComments();
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(false);
        recView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getComments() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("comments");
        Log.e(TAG, "PostID: " + postID);
        mRef.orderByChild("postID").equalTo(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Comment> listComments = new ArrayList<>();
                Log.e(TAG, "Got Comments");
                HashMap<String, Object> comments = (HashMap<String, Object>) dataSnapshot.getValue();
                if (comments == null) {
                    Log.e(TAG, "Comments Is Null");
                    return;
                }
                Log.e(TAG, "Comments Is not Null");
                final commentListAdapter adapter = new commentListAdapter(listComments, CommentFragment.this.getActivity());
                Object[] commentList = comments.values().toArray();
                for (Object comment : commentList) {
                    HashMap<String, Object> commentMap = (HashMap<String, Object>) comment;
                    final Comment commentTemp = new Comment();
                    commentTemp.uid = (String) commentMap.remove("uid");
                    commentTemp.content = (String) commentMap.remove("content");
                    HashMap<String, Object> dateMap = (HashMap<String, Object>) commentMap.remove("dateCreated");
                    long dateEpoch = (long) dateMap.remove("time");
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(dateEpoch);
                    commentTemp.dateCreated = c.getTime();
                    commentTemp.postID = postID;
                    listComments.add(commentTemp);
                    adapter.notifyDataSetChanged();
                }
                recView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private class commentListAdapter extends RecyclerView.Adapter<commentListAdapter.CommentViewHolder> {

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommentViewHolder(inflater.inflate(R.layout.comment_in_list, parent, false));
        }

        LayoutInflater inflater;
        ArrayList<Comment> listComments;

        public commentListAdapter(ArrayList<Comment> listComments, Activity activity) {
            inflater = activity.getLayoutInflater();
            this.listComments = listComments;
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.setData(listComments.get(position));
        }

        @Override
        public int getItemCount() {
            return listComments.size();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            public TextView userName;
            public TextView commentContent;
            public TextView date;

            public CommentViewHolder(View itemView) {
                super(itemView);
                this.commentContent = (TextView) itemView.findViewById(R.id.commentContent);
                this.date = (TextView) itemView.findViewById(R.id.item_commentDate);
                this.userName = (TextView) itemView.findViewById(R.id.commentUserName);
            }

            public void setData(Comment comment) {
                commentContent.setText(comment.content);
                date.setText(comment.dateCreated.toString());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        if (u == null) {
                            return;
                        }
                        userName.setText(u.userName);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mRef.child("userInfo").child(user.getUid()).addListenerForSingleValueEvent(listener);

            }
        }
    }
}

