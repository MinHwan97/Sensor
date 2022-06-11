package com.talentica.wifiindoorpositioning.wifiindoorpositioning.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.talentica.wifiindoorpositioning.wifiindoorpositioning.R;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.adapter.ProjectsListAdapter;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.model.IndoorProject;
import com.talentica.wifiindoorpositioning.wifiindoorpositioning.utils.RecyclerItemClickListener;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemClickListener.OnItemClickListener {

    private Realm realm;
    private RealmResults<IndoorProject> projects;
    private RecyclerView mRecyclerView;
    private ProjectsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;

    //유저
    private int user = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        // Create a new empty instance of Realm

        // Clear the realm from last time
//        Realm.deleteRealm(realmConfiguration);

        realm = Realm.getInstance(realmConfiguration);

        projects = realm.where(IndoorProject.class).findAll();
        if (projects.isEmpty()) {
            Snackbar.make(fab, "Empty List, Try creating project", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        // specify an adapter
        mAdapter = new ProjectsListAdapter(projects);
        mRecyclerView.setAdapter(mAdapter);

        if(user==0) {
            Intent intent2 = new Intent(this, SelectUser.class);
            startActivityForResult(intent2, 101);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }


    private void initUI() {
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.projects_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,mRecyclerView, this));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == fab.getId()) {
            Intent intent = new Intent(this, NewProjectActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        IndoorProject project = projects.get(position);
        intent.putExtra("id", project.getId());
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
    //유저
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if (data != null){
                user = data.getIntExtra("user", 0);
            }
        }
    }
}
