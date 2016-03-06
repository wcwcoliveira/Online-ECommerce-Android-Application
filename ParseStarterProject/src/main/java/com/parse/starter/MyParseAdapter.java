package com.parse.starter;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by shibajyotidebbarma on 06/03/16.
 */
public class MyParseAdapter extends ParseQueryAdapter<ParseObject> {


    public MyParseAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);


    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
