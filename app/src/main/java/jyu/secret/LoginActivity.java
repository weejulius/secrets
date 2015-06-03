package jyu.secret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    private EditText pwdET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setTitle(null);

        toolbar.setLogo(R.drawable.secret_logo);

        setSupportActionBar(toolbar);

        if (!SessionManager.ins().isRegistered(this)) {
            Intent i = new Intent(this, RegistryActivity.class);
            startActivity(i);
            finish();
            return;
        }
        if (SessionManager.ins().isLogined()) {
            Intent i = new Intent(this, SearchResult.class);
            startActivity(i);
            finish();
            return;
        }

        pwdET = (EditText) findViewById(R.id.et_pwd);

        pwdET.requestFocus();

        pwdET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        login();
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.m_login) {

            login();

            return true;
        }

        if (id == R.id.m_reg) {

            Intent i = new Intent(this, RegistryActivity.class);
            startActivity(i);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void login() {

        final String pwd = pwdET.getText().toString();

        boolean isLogined = SessionManager.ins().tryLogin(this, pwd);

        if (isLogined) {
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, SearchResult.class);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show();

        }

    }
}
