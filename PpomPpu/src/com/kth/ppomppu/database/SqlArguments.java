package com.kth.ppomppu.database;

import android.net.Uri;
import android.text.TextUtils;

public class SqlArguments {
    public String table;
    public final String where;
    public final String[] args;

    /** Operate on existing rows. */
    SqlArguments(Uri uri, String where, String[] args) {
        if (uri.getPathSegments().size() == 1) {
            this.table = uri.getPathSegments().get(0);
            this.where = where;
            this.args = args;
        } else if (uri.getPathSegments().size() != 2) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        } else if (!TextUtils.isEmpty(where)) {
            throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
        } else{
        	throw new UnsupportedOperationException("WHERE clause not supported: " + uri);
        }
    }

    /** Insert new rows (no where clause allowed). */
    SqlArguments(Uri uri) {
        if (uri.getPathSegments().size() == 1) {
            this.table = uri.getPathSegments().get(0);
            this.where = null;
            this.args = null;
        } else {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }
}
