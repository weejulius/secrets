package jyu.secret;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import jyu.secret.model.DBSession;
import jyu.secret.model.Secret;

/**
 * 保存或编辑secret
 * <p/>
 * Created by jyu on 15-5-16.
 */
public class InputSecretActivity extends ActionBarActivity implements Toolbar.OnMenuItemClickListener {

    private TextView nameTV;
    private Button switchBtn;
    private EditText nameEV;
    private EditText pwdEV;
    private EditText titleEV;

    private MenuItem saveSecretMenuItem;

    private Toolbar toolbar;

    private boolean pwdToggled = false;//是否切换到密码输入

    private Secret secret;

    private int level;//帐号级别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_input_secret);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setTitle(null);

        toolbar.setNavigationContentDescription("Back");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        setSupportActionBar(toolbar);

        final Long secretId = getIntent().getLongExtra("SECRET_ID", 0l);

        secret = DBSession.ins(this).getSecretDao().load(secretId);

        nameTV = (TextView) findViewById(R.id.input_name);
        switchBtn = (Button) findViewById(R.id.switcher);
        nameEV = (EditText) findViewById(R.id.et_username);
        pwdEV = (EditText) findViewById(R.id.et_pwd);
        titleEV = (EditText) findViewById(R.id.et_title);

        if (secret != null) {
            titleEV.setText(secret.getTitle());
            nameEV.setText(secret.getName());
            pwdEV.setText(secret.getPwd());
        }


        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = nameTV.getText().toString();
                final String switchedToName = switchBtn.getText().toString();

                switchBtn.setText(name);
                nameTV.setText(switchedToName);

                pwdToggled = !pwdToggled;
                if (pwdToggled) {
                    pwdEV.setVisibility(View.VISIBLE);
                    pwdEV.requestFocus();
                    nameEV.setVisibility(View.GONE);
                } else {

                    pwdEV.setVisibility(View.GONE);
                    nameEV.setVisibility(View.VISIBLE);
                    nameEV.requestFocus();
                }
            }
        });


        nameEV.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enabledSaveSecret();
            }
        });

        pwdEV.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enabledSaveSecret();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {

        getMenuInflater().inflate(R.menu.menu_input_secret, optionMenu);

        saveSecretMenuItem = optionMenu.findItem(R.id.m_save);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) optionMenu.findItem(R.id.m_search).getActionView();

        if (searchManager != null && searchView != null) {

            searchView.setIconified(false);

            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }


        enabledSaveSecret();

        return true;
    }


    /**
     * 保存secret
     */
    private void saveSecret() {

        Secret secretToUpdated = secret;
        final String title = titleEV.getText().toString();

        if (secretToUpdated == null) {

            List<Secret> secrets = SessionManager.ins().querySecretByTitle(this, title);

            if (secrets != null && !secrets.isEmpty()) {
                Toast.makeText(this, "secret is existing:" + title, Toast.LENGTH_SHORT).show();
                return;
            }

            secretToUpdated = new Secret();
            secretToUpdated.setCreatedDate(new Date());

        }


        final String name = nameEV.getText().toString();
        final String pwd = pwdEV.getText().toString();

        secretToUpdated.setUpdatedDate(new Date());
        secretToUpdated.setTitle(title);
        secretToUpdated.setName(name);
        secretToUpdated.setPwd(pwd);
        secretToUpdated.setLevel((long)level);
        secretToUpdated.setUserId(1l);

        Long sId = DBSession.ins(this).getSecretDao().insertOrReplace(secretToUpdated);

        secretToUpdated.setId(sId);

        secret = secretToUpdated;

        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), SecretActivity.class);
        intent.putExtra("SECRET_ID", sId);

        startActivity(intent);
        finish();


    }


    private void enabledSaveSecret() {

        final String name = nameEV.getText().toString();
        final String pwd = pwdEV.getText().toString();
        final String title = titleEV.getText().toString();

        final boolean isEnabled = !name.isEmpty() && !pwd.isEmpty() && !title.isEmpty();
        if (saveSecretMenuItem != null) {
            saveSecretMenuItem.setEnabled(isEnabled);
        }
        if (isEnabled && toolbar != null) {
            toolbar.setOnMenuItemClickListener(this);
        }


    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.m_save) {
            saveSecret();
            return true;
        }

        if (menuItem.getItemId() == R.id.m_search) {

            SearchView searchView =
                    (SearchView) menuItem.getActionView();
            searchView.setIconified(false);

            return true;
        }

        return true;
    }

    public void selectLevel(View view) {

        if (view == null) {
            return;
        }

        switch (view.getId()) {
            case R.id.important:
                level = 1;
                break;
            case R.id.pri:
                level = 2;
                break;
            default:
                level = 0;
                break;
        }
    }
}
