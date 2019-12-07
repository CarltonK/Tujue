package handler;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class DatabaseLocal {

    // Shared Preferences
    private final SharedPreferences pref;

    // Editor for Shared preferences
    private final SharedPreferences.Editor editor;

    // Context
    private final Context _context;

    // Shared pref mode
    private final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Tujue";


    // All Shared Preferences Keys
    private static final String IS_LOGIN = "loggedIn";

    public static final String KEY_SOURCE = "source";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static  final String KEY_URL = "url";
    public static final String KEY_IMAGEURL = "urlToImage";
    public static final String KEY_DATE = "publishedAt";
    public static final String KEY_CONTENT = "content";
    public static final String TAG_TOKEN = "tagtoken";


    public DatabaseLocal(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void saveArticle(String source, String author, String title, String content,
                                   String description, String url, String urlimage, String date){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_SOURCE, source);
        editor.putString(KEY_AUTHOR, author);
        editor.putString(KEY_TITLE, title);
        editor.putString(KEY_CONTENT, content);
        editor.putString(KEY_DESCRIPTION, description);
        editor.putString(KEY_URL, url);
        editor.putString(KEY_IMAGEURL, urlimage);
        editor.putString(KEY_DATE, date);
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getDbArticle(){
        HashMap<String, String> article = new HashMap<>();
        article.put(KEY_SOURCE, pref.getString(KEY_SOURCE, null));
        article.put(KEY_AUTHOR, pref.getString(KEY_AUTHOR, null));
        article.put(KEY_TITLE, pref.getString(KEY_TITLE, null));
        article.put(KEY_DESCRIPTION, pref.getString(KEY_DESCRIPTION, null));
        article.put(KEY_CONTENT, pref.getString(KEY_CONTENT, null));
        article.put(KEY_URL, pref.getString(KEY_URL, null));
        article.put(KEY_IMAGEURL, pref.getString(KEY_IMAGEURL, null));
        article.put(KEY_DATE, pref.getString(KEY_DATE, null));

        // return user
        return article;
    }
}
