package com.han.sqlitebest.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.han.sqlitebest.R;
import com.han.sqlitebest.db.MyDBHelper;

public class MainActivity extends Activity implements OnClickListener {

	private Button createDB;
	private Button selectData;
	private Button updateData;
	private Button deleteData;
	private Button addData;
	private Button replaceData;
	private MyDBHelper helper;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		helper = new MyDBHelper(this, "BookStore.db", null, 3);
		createDB = (Button) findViewById(R.id.create_db);
		addData = (Button) findViewById(R.id.add_data);
		deleteData = (Button) findViewById(R.id.delete_data);
		updateData = (Button) findViewById(R.id.update_data);
		selectData = (Button) findViewById(R.id.select_data);
		replaceData = (Button) findViewById(R.id.replace_data);
		createDB.setOnClickListener(this);
		addData.setOnClickListener(this);
		deleteData.setOnClickListener(this);
		updateData.setOnClickListener(this);
		selectData.setOnClickListener(this);
		replaceData.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (R.id.create_db == v.getId()) {
			db = helper.getWritableDatabase();
		} else if (R.id.add_data == v.getId()) {
			db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("name", "Java");
			values.put("author", "han");
			values.put("price", 12.0);
			values.put("pages", 500);
			db.insert("book", null, values);
			values.clear();
			values.put("name", "Android");
			values.put("author", "han");
			values.put("price", 20);
			values.put("pages", 600);
			db.insert("book", null, values);
		} else if (R.id.delete_data == v.getId()) {
			db = helper.getWritableDatabase();
			db.delete("book", "name=?", new String[] { "Java" });
		} else if (R.id.update_data == v.getId()) {
			db = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("price", 32.0);
			db.update("book", values, "id = ?", new String[] { "1" });

		} else if (R.id.select_data == v.getId()) {
			db = helper.getWritableDatabase();
			Cursor cursor = db
					.query("book", null, null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					String name = cursor.getString(cursor
							.getColumnIndex("name"));
					String author = cursor.getString(cursor
							.getColumnIndex("author"));
					double price = cursor.getDouble(cursor
							.getColumnIndex("price"));
					int pages = cursor.getInt(cursor.getColumnIndex("pages"));
					Log.e(getLocalClassName(), "book name is " + name);
					Log.e(getLocalClassName(), "book author is " + author);
					Log.e(getLocalClassName(), "book price is " + price);
					Log.e(getLocalClassName(), "book pages is " + pages);
				} while (cursor.moveToNext());
				cursor.close();
			}
		} else if (R.id.replace_data == v.getId()) {
			db = helper.getWritableDatabase();
			db.beginTransaction();
			try {
				db.execSQL("delete from book");
				// if (true) {
				// throw new NullPointerException();
				// }
				db.execSQL(
						"insert into book (author,price,name,pages)values(?,?,?,?)",
						new Object[] { "chen", 13.0, "C”Ô—‘", 200 });

				db.setTransactionSuccessful();
				Toast.makeText(this, "transaction execute succeed",
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Log.e(getLocalClassName(), e.getMessage());
			} finally {
				db.endTransaction();
			}
		}
	}
}
