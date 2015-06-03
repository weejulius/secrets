package jyu.secret;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import jyu.secret.model.DBSession;
import jyu.secret.model.Secret;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SecretActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private TextView nameTitleTV;
    private Button switchBtn;
    private TextView nameTV;
    private TextView pwdTV;
    private TextView titleTV;
    private Button levelBtn;

    private Long secretId;

    private boolean pwdToggled = false;//是否切换到密码
    private boolean pwdShownFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_secret);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setTitle(null);

        toolbar.setNavigationContentDescription("Back");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        setSupportActionBar(toolbar);


        nameTitleTV = (TextView) findViewById(R.id.input_name);
        nameTV = (TextView) findViewById(R.id.tv_username);
        switchBtn = (Button) findViewById(R.id.switcher);
        pwdTV = (TextView) findViewById(R.id.tv_pwd);
        titleTV = (TextView) findViewById(R.id.tv_title);
        levelBtn = (Button)findViewById(R.id.level);

        secretId = getIntent().getLongExtra("SECRET_ID", 0l);

        final Secret secret = DBSession.ins(this).getSecretDao().load(secretId);

        if (secret != null) {
            nameTV.setText(secret.getName());
            titleTV.setText(secret.getTitle());
            pwdTV.setText(secret.getPwd());
            levelBtn.setText(Level.byId(secret.getLevel()).getName());
            levelBtn.setBackgroundColor(this.getResources().getColor(Level.byId(secret.getLevel()).getColor()));
        }


        pwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pwdShownFlag && secret != null) {
                    pwdTV.setText(secret.getPwd());
                    Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                            new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    pwdTV.setText("********");
                                    pwdShownFlag = false;
                                }
                            }
                    );
                    pwdShownFlag = true;
                } else {
                    pwdTV.setText("********");
                    pwdShownFlag = false;
                }
            }
        });
        switchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = nameTitleTV.getText().toString();
                final String switchedToName = switchBtn.getText().toString();

                switchBtn.setText(name);
                nameTitleTV.setText(switchedToName);

                pwdToggled = !pwdToggled;
                if (pwdToggled) {
                    pwdTV.setVisibility(View.VISIBLE);
                    Observable.timer(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(
                            new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    pwdTV.setText("********");
                                }
                            }
                    );
                    nameTV.setVisibility(View.GONE);
                } else {

                    pwdTV.setVisibility(View.GONE);
                    nameTV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secret, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.m_search).getActionView();

        if (searchManager != null && searchView != null) {


            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.m_edit_secret) {
            Intent intent = new Intent(getApplicationContext(), InputSecretActivity.class);
            intent.putExtra("SECRET_ID", secretId);

            startActivity(intent);
            finish();
        }

        if (id == R.id.m_search) {

            SearchView searchView =
                    (SearchView) item.getActionView();
            searchView.setIconified(false);
        }

        return super.onOptionsItemSelected(item);
    }
}
