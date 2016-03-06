package com.parse.starter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by shibajyotidebbarma on 06/03/16.
 */
public abstract class EndlessRecyclerView extends RecyclerView.OnScrollListener {


    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    EndlessRecyclerView(LinearLayoutManager mLinearLayoutManager) {

        this.mLinearLayoutManager = mLinearLayoutManager;

    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();

        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();


        if (loading) {


            if (totalItemCount > previousTotal) {


                loading = false;
                previousTotal = totalItemCount;
            }
        }


        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))

        {

            current_page++;
            onLoadMore(current_page);
            loading =true;



        }

    }

    public abstract void onLoadMore(int current_page);





}
