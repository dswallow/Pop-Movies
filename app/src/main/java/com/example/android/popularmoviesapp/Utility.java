package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.popularmoviesapp.data.MovieContract.VideoEntry;

import java.io.ByteArrayOutputStream;

import static com.example.android.popularmoviesapp.data.MovieContract.MovieEntry;

/**
 * Created by David on 22/08/16.
 */
public class Utility {
    private static final String LOG_TAG = Utility.class.getSimpleName();

    private static final String SORT_ORDER_PREFERENCE = "sort_order";
    private static final String SELECTION_PREFERENCE = "selection";
    private static final String SELECTION_ARG_PREFERENCE = "selection_arg";
    private static final String POSITION_PREFERENCE = "position";
    public static final String SORT_BY_POPULARITY_DESC = MovieEntry.COLUMN_POPULARITY + " DESC";
    public static final String SORT_BY_RATING_DESC = MovieEntry.COLUMN_RATING + " DESC";

    // TODO: workaround for savedInstanceState
    public static void setSelection(Context context, String selection) {
        setPreferenceText(context, selection, R.string.selection_key, SELECTION_PREFERENCE);
    }

    // TODO: workaround for savedInstanceState
    public static String getSelection(Context context) {
        return getPreferenceText(context, SELECTION_PREFERENCE, null, R.string.selection_key);
    }

    // TODO: workaround for savedInstanceState
    public static void setSelectionArg(Context context, String selectionArg) {
        setPreferenceText(context, selectionArg, R.string.selection_arg_key,
                SELECTION_ARG_PREFERENCE);
    }

    // TODO: workaround for savedInstanceState
    public static String getSelectionArg(Context context) {
        return getPreferenceText(context, SELECTION_ARG_PREFERENCE, null,
                R.string.selection_arg_key);
    }

    // TODO: workaround for savedInstanceState
    public static void setSortOrder(Context context, String sortOrder) {
        setPreferenceText(context, sortOrder, R.string.sort_order_key, SORT_ORDER_PREFERENCE);
    }

    // TODO: workaround for savedInstanceState
    public static String getSortOrder(Context context) {
        return getPreferenceText(context, SORT_ORDER_PREFERENCE,
                SORT_BY_POPULARITY_DESC, R.string.sort_order_key);
    }

    // TODO: workaround for savedInstanceState
    public static void setPosition(Context context, int position) {
        setPreferenceText(context, Integer.toString(position),
                R.string.position_key, POSITION_PREFERENCE);
    }

    // TODO: workaround for savedInstanceState
    public static String getPosition(Context context) {
        return getPreferenceText(context, POSITION_PREFERENCE,
                Integer.toString(-1), R.string.position_key);
    }

    static String formatRating(Context context, double ratingFromDb) {
        int formatId = R.string.format_user_rating;
        return String.format(context.getString(formatId), ratingFromDb);
    }

    // http://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    public static byte[] convertBitmapIntoBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    // http://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    static Bitmap getBitmapFromBlob(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER);
        final byte[] posterByteArray = cursor.getBlob(columnIndex);
        return BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length);
    }

    static String getPosterPathFrom(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER);
        return cursor.getString(columnIndex);
    }

    static String getVideoKeyFrom(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex(VideoEntry.COLUMN_VIDEO_KEY);
        return cursor.getString(columnIndex);
    }

    static long getMovieIdFromMovieKey(Context context, long movieKey, int position) {
        final Cursor cursor = getSingleMovieCursorAndMoveToPosition(context, position, movieKey);
        int columnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID);
        return cursor.getLong(columnIndex);
    }

    public static Cursor querySingleMovieUri(Context context, long movieRowId) {
        final Uri uri = MovieEntry.buildMovieUri(movieRowId);
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = null;

        return context.getContentResolver().query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    @NonNull
    static Cursor getSingleMovieCursorAndMoveToPosition(Context context, int position, long movieKey) {
        final Cursor cursor = Utility.querySingleMovieUri(context, movieKey);
        cursor.moveToPosition(position);
        return cursor;
    }

    static boolean isVideosView(Cursor cursor) {
        return VideoEntry.COLUMN_VIDEO_ID.equals(getColumnNameOf12thColumn(cursor));
    }

    // The 12th column is the first column that differs between reviews and videos
    private static String getColumnNameOf12thColumn(Cursor cursor) {
        final int columnIndex = 11;
        return cursor.getColumnName(columnIndex);
    }

    /* Helper methods */
    private static void setPreferenceText(
            Context context, String text, int key, String preference) {
        SharedPreferences settings = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(context.getString(key), text);
        editor.commit();
    }

    private static String getPreferenceText(
            Context context, String preference, String defValue, int key) {
        SharedPreferences settings = context.getSharedPreferences(
                preference, Context.MODE_PRIVATE);
        return settings.getString(context.getString(key), defValue);
    }


}
