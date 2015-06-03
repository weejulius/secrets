package jyu.secret;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jyu.secret.model.Secret;


public class SearchResult extends ActionBarActivity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private SecretAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean isFromIntent;
    private List<Secret> secrets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_result);

        if (!SessionManager.ins().isLogined()) {

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
            return;

        }


        toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setTitle(null);

        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.secret_logo);


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_secrets);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SecretAdapter(secrets);
        mRecyclerView.setAdapter(mAdapter);

        SwipeDismissRecycleViewTouchListener touchListener = new SwipeDismissRecycleViewTouchListener(mRecyclerView, new SwipeDismissRecycleViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {

                for (int position : reverseSortedPositions) {
                    mAdapter.secrets.remove(position);
                }
                // do not call notifyItemRemoved for every item, it will cause gaps on deleting items
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setOnTouchListener(touchListener);
        mRecyclerView.setOnScrollListener(touchListener.makeScrollListener());

        isFromIntent = handleIntent(getIntent());

        if (!isFromIntent && mAdapter != null) {

            mAdapter.setData(SessionManager.ins().queryRecentSecrets(this));
        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!isFromIntent) {
            mAdapter.setData(SessionManager.ins().queryRecentSecrets(this));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);


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


        if (id == R.id.m_add) {

            Intent i = new Intent(this, InputSecretActivity.class);
            startActivity(i);
        }
        if (id == R.id.m_search) {

            SearchView searchView =
                    (SearchView) item.getActionView();
            searchView.setIconified(false);
        }

        if (id == R.id.m_logout) {

            SessionManager.ins().tryLogout();

            Intent i = new Intent(this, LoginActivity.class);

            startActivity(i);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }


    private boolean handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            secrets = SessionManager.ins().querySecretByTitle(this, query);

            if (mAdapter != null) {
                mAdapter.setData(secrets);
            }
            return true;
        } else if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            String query = intent.getData().getLastPathSegment();

            if (query != null) {

                Intent secretIntent = new Intent(getApplicationContext(), SecretActivity.class);
                secretIntent.putExtra("SECRET_ID", Long.parseLong(query));
                startActivity(secretIntent);
            }

            finish();
            return true;
        }
        return false;
    }


    public class SecretAdapter extends RecyclerView.Adapter<SecretAdapter.SecretHolder> {

        private List<Secret> secrets = new ArrayList<Secret>();

        public class SecretHolder extends RecyclerView.ViewHolder {

            public Long secretId;
            public TextView titleTV;
            public TextView nameTV;
            public View levelView;

            public SecretHolder(View v) {
                super(v);
            }
        }

        public SecretAdapter(List<Secret> secrets) {
            this.secrets = secrets;
        }

        @Override
        public SecretHolder onCreateViewHolder(final ViewGroup parent,
                                               int viewType) {


            final ViewGroup vg = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.lo_rv_secret, parent, false);


            final SecretHolder vh = new SecretHolder(vg);

            vh.nameTV = (TextView) vg.findViewById(R.id.secret_name);
            vh.titleTV = (TextView) vg.findViewById(R.id.secret_title);
            vh.levelView = vg.findViewById(R.id.secret_level);


            vh.titleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(parent.getContext(), SecretActivity.class);
                    i.putExtra("SECRET_ID", vh.secretId);

                    startActivity(i);
                }
            });
//            vg.setOnTouchListener(new View.OnTouchListener() {
//
//                float dis = 0;
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            dis = event.getX();
//                            return true;
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
//                            dis = event.getX() - dis;
//
//                            if (Math.abs(dis) < 5) {
//
//                                return true;
//                            }
//
//                    }
//                    return false;
//
//                }
//            });


            return vh;
        }

        public void setData(List<Secret> secrets) {
            if (secrets != null && this.secrets != null) {
                this.secrets.clear();
                this.secrets.addAll(secrets);
                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(SecretHolder holder, int position) {

            final Secret secret = secrets.get(position);

            if (secret == null) {
                return;
            }
            holder.titleTV.setText(secret.getTitle());
            holder.nameTV.setText(secret.getName());
            holder.secretId = secret.getId();
            holder.levelView.setBackgroundColor(SearchResult.this.getResources().getColor(Level.byId(secret.getLevel()).getColor()));

        }


        @Override
        public int getItemCount() {
            return secrets == null ? 0 : secrets.size();
        }
    }
}
