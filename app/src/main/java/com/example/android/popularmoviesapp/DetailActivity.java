package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String POSTER = "poster";
    private static final String TITLE = "title";
    private static final String OVERVIEW = "overview";
    private static final String RATING = "rating";
    private static final String RELEASE = "release";

    private Uri singleMovieUri;
    private MovieInfoParser movieInfoParser;

    public static Intent newIntent(Context context, Uri uri) {
        Log.d(LOG_TAG, "newIntent");
        final Intent intent = new Intent(context, DetailActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, movieJsonStr);
//        intent.putExtra("position", position);
        intent.setData(uri);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extractUriFromIntent();
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(singleMovieUri.toString());

//        parseMovieInfo();

//        loadViews();
    }

    private void extractUriFromIntent() {
        final Intent intent = this.getIntent();
        if (null != intent) {
            singleMovieUri = intent.getData();
        }
    }


    private void parseMovieInfo() {

        final Intent intent = this.getIntent();

        // get position of movie in grid (default to -1)
        final int position = intent.getIntExtra("position", -1);

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String dataJsonStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            try {
                movieInfoParser = new MovieInfoParser(dataJsonStr, position);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    private void loadViews() {
        loadPosterIntoView();
        loadTitleIntoView();
        loadOverviewIntoView();
        loadRatingIntoView();
        loadReleaseIntoView();
    }

    private void loadPosterIntoView() {
        final ImageView imageView = (ImageView) findViewById(R.id.detail_image_view);
        final String posterUrl = parse(POSTER);

        if (posterUrl != null) {
            Picasso.with(this).load(posterUrl).into(imageView);
        }
    }

    private void loadTitleIntoView() {
        final String title = parse(TITLE);
        final TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
    }

    private void loadOverviewIntoView() {
        final String overview = parse(OVERVIEW);
        final TextView overviewView = (TextView) findViewById(R.id.overview);
        overviewView.setText(overview);

    }

    private void loadRatingIntoView() {
        final String userRating = parse(RATING);
        final TextView userRatingView = (TextView) findViewById(R.id.rating);
        userRatingView.setText(userRating);
    }

    private void loadReleaseIntoView() {
        final String releaseDate = parse(RELEASE);
        final TextView releaseDateView = (TextView) findViewById(R.id.release);
        releaseDateView.setText(releaseDate);
    }


    private String parse(String movieInfoAttribute) {
        try {
            if (movieInfoAttribute == POSTER) {
                return movieInfoParser.parsePosterUrl();
            } else if (movieInfoAttribute == TITLE) {
                return movieInfoParser.parseOriginalTitle();
            } else if (movieInfoAttribute == OVERVIEW) {
                return movieInfoParser.parseOverview();
            } else if (movieInfoAttribute == RATING) {
                return movieInfoParser.parseRating();
            } else if (movieInfoAttribute == RELEASE) {
                return movieInfoParser.parseRelease();
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
