package com.yuzhou.l2.prework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends Activity
{
    private EditText editText;
    private Intent intent;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        intent = getIntent();
        // file
        //String title = intent.getStringExtra("title");
        // SQLite
        user = (User) intent.getSerializableExtra("user");
        String title = user.getName();

        editText = (EditText) findViewById(R.id.editText);
        editText.setText(title);
        editText.setSelection(title.length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void saveTitle(View view)
    {
        // file
        //intent.putExtra("new_title", editText.getText().toString());
        // SQLite
        user.setName(editText.getText().toString());
        intent.putExtra("new_user", user);

        setResult(RESULT_OK, intent);
        finish();
    }

}
