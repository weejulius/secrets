package jyu.secret;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;

import jyu.secret.model.DBSession;

/**
 * Created by jyu on 15-5-19.
 */
public class SecretSuggestionProvider extends SearchRecentSuggestionsProvider {


    public static final String AUTHORITY =
            SecretSuggestionProvider.class.getName();

    public static final int MODE = DATABASE_MODE_QUERIES;

    public SecretSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String query = uri.getLastPathSegment();
        if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(query)) {
            return null;
        } else {
            return DBSession.ins(getContext()).getDatabase().
                    query("secret",
                            new String[]{"rowid as _id","id as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "title as " + SearchManager.SUGGEST_COLUMN_TEXT_1},
                            "title like ?", new String[]{query+"%"},
                            null,
                            null,
                            null,
                            "3");

        }
    }
}
