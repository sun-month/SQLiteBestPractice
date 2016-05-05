package com.han.sqlitebest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.han.sqlitebest.db.MyDBHelper;

public class DatabaseProvider extends ContentProvider {

	public static final int BOOK_DIR = 0;

	public static final int BOOK_ITEM = 1;

	public static final int CATEGORY_DIR = 2;

	public static final int CATEGORY_ITEM = 3;

	public static final String AUTHORITY = "com.han.sqlitebest.provider";

	private static UriMatcher uriMatcher;

	private MyDBHelper dbHelper;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
		uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
		uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
		uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new MyDBHelper(getContext(), "BookStore.db", null, 2);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			cursor = db.query("book", projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case BOOK_ITEM:
			String bookID = uri.getPathSegments().get(1);
			cursor = db.query("book", projection, "id = ?",
					new String[] { bookID }, null, null, sortOrder);
			break;
		case CATEGORY_DIR:
			break;
		case CATEGORY_ITEM:
			break;
		}

		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Uri returnUri = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
		case BOOK_ITEM:
			long newBookId = db.insert("book", null, values);
			returnUri = Uri.parse("content://" + AUTHORITY + "/book/"
					+ newBookId);
			break;
		case CATEGORY_DIR:
		case CATEGORY_ITEM:
			long newCategoryId = db.insert("category", null, values);
			returnUri = Uri.parse("content://" + AUTHORITY + "/category/"
					+ newCategoryId);
			break;
		default:
			break;
		}
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deleteRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			deleteRows = db.delete("book", selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			deleteRows = db.delete("book", "id = ?", new String[] { bookId });
			break;
		case CATEGORY_DIR:
			deleteRows = db.delete("category", selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String newCategoryId = uri.getPathSegments().get(1);
			deleteRows = db.delete("category", "id = ?",
					new String[] { newCategoryId });
			break;
		default:
			break;
		}

		return deleteRows;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int updateRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			updateRows = db.update("book", values, selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String newBookId = uri.getPathSegments().get(1);
			updateRows = db.update("book", values, "id=?",
					new String[] { newBookId });
			break;
		case CATEGORY_DIR:
			updateRows = db
					.update("category", values, selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String newCategoryId = uri.getPathSegments().get(1);
			updateRows = db.update("category", values, "id = ?",
					new String[] { newCategoryId });
			break;
		default:
			break;
		}

		return updateRows;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			return "vnd.android.cursor.dir/vnd.com.han.sqlitebest.provider.book";
		case BOOK_ITEM:
			return "vnd.android.cursor.item/vnd.com.han.sqlitebest.provider.book";
		case CATEGORY_DIR:
			return "vnd.android.cursor.dir/vnd.com.han.sqlitebest.provider.category";
		case CATEGORY_ITEM:
			return "vnd.android.cursor.item/vnd.com.han.sqlitebest.provider.category";

		}
		return null;
	}
}
