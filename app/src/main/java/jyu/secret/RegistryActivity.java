package jyu.secret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class RegistryActivity extends ActionBarActivity {

    private EditText pwdET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_registry);

        if (SessionManager.ins().isLogined()) {
            Intent i = new Intent(this, SearchResult.class);
            startActivity(i);
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setTitle(null);


        toolbar.setLogo(R.drawable.secret_logo);

        setSupportActionBar(toolbar);


        pwdET = (EditText) findViewById(R.id.et_pwd);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.m_create) {

            SessionManager.ins().tryCreate(this, pwdET.getText().toString());
            pwdET.setText("");

            if (SessionManager.ins().isLogined()) {
                Intent i = new Intent(this, SearchResult.class);
                startActivity(i);
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
