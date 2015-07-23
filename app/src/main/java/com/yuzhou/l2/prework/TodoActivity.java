package com.yuzhou.l2.prework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends Activity
{
    List<String> items;
    List<User> users;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // read from array
        //items = new ArrayList();
        //items.add("First Item");
        //items.add("Second Item");

        // read from SQLite
        revamp();

        // read from file
        //readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setItemsCanFocus(true);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                // update file
                //String title = data.getStringExtra("new_title");
                //items.set(position, title);
                //saveItems();

                // update SQLite
                User newUser = (User) data.getSerializableExtra("new_user");
                User user = users.get(position);
                user.setName(newUser.getName());
                user.save();
                items.set(position, user.getName());
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addTodoItem(View view)
    {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());

        // write to file
        //saveItems();

        // write to SQLite
        User user = new User(etNewItem.getText().toString());
        user.save();
        users.add(user);

        etNewItem.setText("");
    }

    private void setupListViewListener()
    {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = items.get(position);
                // drop from file
                //saveItems();

                // drop from SQLite
                List<User> delUsers = new Delete().from(User.class).where("NAME = ?", name).execute();

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                intent.putExtra("position", position);
                // file
                //intent.putExtra("title", items.get(position));

                // SQLite
                intent.putExtra("user", users.get(position));
                startActivityForResult(intent, 0);
            }
        });
    }

    @Deprecated
    private void readItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
            e.printStackTrace();
        }
    }

    @Deprecated
    private void saveItems()
    {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void revamp()
    {
        items = new ArrayList<>();
        users = new ArrayList<>();
        List<User> data = new Select().from(User.class).execute();
        users.addAll(data);
        for (User user : users) {
            items.add(user.getName());
        }
    }
}
