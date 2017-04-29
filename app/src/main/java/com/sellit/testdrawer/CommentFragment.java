package com.sellit.testdrawer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andrew on 4/29/2017.
 */

public class CommentFragment extends Fragment
{
    RecyclerView recView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.comment_fragment_layout, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        recView = (RecyclerView) view.findViewById(R.id.commentRecView);
        setupRec(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRec(View view)
    {

    }
}
