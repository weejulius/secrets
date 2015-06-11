package jyu.secret;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import jyu.secret.model.DBSession;
import jyu.secret.model.Secret;


public class SecretActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private TextView nameTitleTV;
    private Button switchBtn;
    private TextView nameTV;
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
        titleTV = (TextView) findViewById(R.id.tv_title);
        levelBtn = (Button) findViewById(R.id.level);

        secretId = getIntent().getLongExtra("SECRET_ID", 0l);

        final Secret secret = DBSession.ins(this).getSecretDao().load(secretId);

        Encrypts.ins().decryptSecret(secret);

        if (secret != null) {
            nameTV.setText(secret.getName());
            titleTV.setText(secret.getTitle());
            levelBtn.setText(Level.byId(secret.getLevel()).getName());
            levelBtn.setBackgroundColor(this.getResources().getColor(Level.byId(secret.getLevel()).getColor()));
        }


        switchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                popupPwdDialog();
            }
        });

    }

    private void popupPwdDialog() {


        final View v = LayoutInflater.from(this).inflate(R.layout.lo_pwd_hint, null);

        final TextView pwdText = (TextView) v.findViewById(R.id.et_pwd);

        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("输入密码").setView(v);

        alert.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                boolean isOK = SessionManager.ins().confirmPwd(SecretActivity.this, pwdText.getText().toString());
                String hint = "";
                if (isOK) {

                    hint = Encrypts.ins().decryptSecretPwd(pwdText.getText().toString(),
                            DBSession.ins(SecretActivity.this).getSecretDao().load(secretId).getPwd());

                } else {
                    hint = "密码错误";
                }

                Toast.makeText(SecretActivity.this, hint, Toast.LENGTH_LONG).show();
                hint = "";
            }
        });
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
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
