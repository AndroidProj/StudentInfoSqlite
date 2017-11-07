package com.example.vasundhara.studentinfosqlite;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    public static final String TAG = DisplayActivity.class.getSimpleName();
    public static boolean status = false;
    DatabaseHelper databaseHelper;
    ListView listView;
    public static String dbId;
    ArrayList<StudentItems> studentItemsesList;
    EditText filter_et;
    CustomCursorAdapter adapter;
    ImageView search_img;
    RelativeLayout search_block_rel;
    LinearLayout filter_block_lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        databaseHelper = new DatabaseHelper(DisplayActivity.this);
        listView = (ListView) findViewById(R.id.list_display);
        filter_et = (EditText) findViewById(R.id.myFilter);
        search_img = (ImageView) findViewById(R.id.img_search);
        search_block_rel = (RelativeLayout) findViewById(R.id.rel_search_block);
        filter_block_lin = (LinearLayout) findViewById(R.id.lin_filter_block);
        studentItemsesList = new ArrayList<StudentItems>();
//        String[] from = {databaseHelper.FNAME, databaseHelper.LNAME, databaseHelper.MOBILE, databaseHelper.EMAIL};
//        int[] to = {R.id.text_firstname, R.id.text_lastname, R.id.text_mobile, R.id.text_email};
//        adapter = new ViewAdapter(studentItemsesList);

        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_block_rel.setVisibility(View.GONE);
                filter_block_lin.setVisibility(View.VISIBLE);
            }
        });

        Cursor cursor = databaseHelper.getData();
//        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(DisplayActivity.this, R.layout.raw_list, cursor, from, to);
        adapter = new CustomCursorAdapter(DisplayActivity.this, cursor);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        while (cursor.moveToNext()) {
            StudentItems studentItems = new StudentItems(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            studentItemsesList.add(studentItems);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentItems items = studentItemsesList.get(position);
                dbId = items.getid();
                Log.e(TAG, "onItemClick Clicked : " + position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemLongClick Clicked : " + position);
                StudentItems items = studentItemsesList.get(position);
                dbId = items.getid();
                return false;
            }
        });

        filter_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return databaseHelper.fetchByFirstName(constraint.toString());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_display) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            String[] menuItems = getResources().getStringArray(R.array.array);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.e(TAG, String.valueOf(item.getItemId()));
        if (item.getItemId() == 0) {
            Log.e(TAG, "clicked for update");
            status = true;
            Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == 1) {
            Log.e(TAG, "clicked for delete");
            Log.e(TAG, dbId);
            boolean res = databaseHelper.deleteData(dbId);
            if (res) {
                finish();
            } else {
                Log.e(TAG, "There is problem in deletion...");
            }
        }
        return super.onContextItemSelected(item);
    }

//    public class ViewAdapter extends BaseAdapter {
//        ArrayList<StudentItems> studentListItemses;
//        LayoutInflater layoutInflater;
//
//        public ViewAdapter(ArrayList<StudentItems> studentListItemses) {
//            this.studentListItemses = studentListItemses;
//        }
//
//        @Override
//        public int getCount() {
//            return studentListItemses.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return studentListItemses.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return studentListItemses.indexOf(studentListItemses.get(position));
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = convertView;
//            ViewHolder holder;
//
//            if (view == null) {
//                holder = new ViewHolder();
//                layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                view = layoutInflater.inflate(R.layout.raw_list, null);
//                holder.id_tv = (TextView) view.findViewById(R.id.text_id);
//                holder.first_tv = (TextView) view.findViewById(R.id.text_firstname);
//                holder.last_tv = (TextView) view.findViewById(R.id.text_lastname);
//                holder.mobile_tv = (TextView) view.findViewById(R.id.text_mobile);
//                holder.email_tv = (TextView) view.findViewById(R.id.text_email);
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//            StudentItems student = studentListItemses.get(position);
//            holder.id_tv.setText(student.getid());
//            holder.first_tv.setText(student.getFirst());
//            holder.last_tv.setText(student.getLast());
//            holder.mobile_tv.setText(student.getMobile());
//            holder.email_tv.setText(student.getEmail());
//
//            return view;
//        }
//
//        public class ViewHolder {
//            TextView id_tv, first_tv, last_tv, mobile_tv, email_tv;
//        }
//    }

    public class CustomCursorAdapter extends CursorAdapter {
        public CustomCursorAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.raw_list, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            View v = view;
            ViewHolder holder;
            holder = new ViewHolder();

            holder.id_tv = (TextView) v.findViewById(R.id.text_id);
            holder.first_tv = (TextView) v.findViewById(R.id.text_firstname);
            holder.last_tv = (TextView) v.findViewById(R.id.text_lastname);
            holder.mobile_tv = (TextView) v.findViewById(R.id.text_mobile);
            holder.email_tv = (TextView) v.findViewById(R.id.text_email);

            holder.id_tv.setText(cursor.getString(1));
            holder.first_tv.setText(cursor.getString(2));
            holder.last_tv.setText(cursor.getString(3));
            holder.mobile_tv.setText(cursor.getString(4));
            holder.email_tv.setText(cursor.getString(5));

        }

        public class ViewHolder {
            TextView id_tv, first_tv, last_tv, mobile_tv, email_tv;
        }
    }

    public class StudentItems {

        String id;
        String first;
        String last;
        String mobile;
        String email;

        public StudentItems(String id, String first, String last, String mobile, String email) {
            this.id = id;
            this.first = first;
            this.last = last;
            this.mobile = mobile;
            this.email = email;
        }

        public String getid() {
            return id;
        }

        public void setid(String id) {
            this.id = id;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
