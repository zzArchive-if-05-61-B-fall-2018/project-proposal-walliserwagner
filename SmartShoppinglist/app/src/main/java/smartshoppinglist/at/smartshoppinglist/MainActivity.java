package smartshoppinglist.at.smartshoppinglist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Shoppinglist shoppinglist = new Shoppinglist("Haushalt");
        Category<ItemContainer> category;
        category = new Category<ItemContainer>("Obst & Gemüse");
        category.addObject(new ItemContainer(new Item("Apfel"),3));
        category.addObject(new ItemContainer(new Item("Gurke"),1));
        category.addObject(new ItemContainer(new Item("Tomate"),1,"Pak"));
        category.addObject(new ItemContainer(new Item("Karotten"),1,"Kg"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Fisch und Fleisch");
        category.addObject(new ItemContainer(new Item("Hering"),500,"G"));
        category.addObject(new ItemContainer(new Item("Extrawurscht"),20,"Dag"));
        category.addObject(new ItemContainer(new Item("Kalbsfleisch"),"Kg"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Milchprodukte");
        category.addObject(new ItemContainer(new Item("Schlagobers"),"Pak"));
        category.addObject(new ItemContainer(new Item("Milch"),"L"));
        category.addObject(new ItemContainer(new Item("Joghurt"),1,"Pak"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Süsigkeiten");
        category.addObject(new ItemContainer(new Item("Schokolade"),1));
        category.addObject(new ItemContainer(new Item("Gummibären"),2,"Pak"));
        category.addObject(new ItemContainer(new Item("Rumkugeln"),"Pak"));
        shoppinglist.addCategory(category);
        /*
        Item i1 = new Item("Apfel");
        ItemContainer c1 = new ItemContainer(i1,3,i1.getDefaultunit());
        Category<ItemContainer> categroy = new Category<ItemContainer>("Obst");
        categroy.addObject(c1);
        ItemContainer[] ic = categroy.getElements();
        Shoppinglist sl = new Shoppinglist("Haushalt");
        sl.addCategory(categroy);
        */


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        expListView = (ExpandableListView) findViewById(R.id.listView);
        //prepareListData();
        listAdapter = new ExpandableListAdapter(this, shoppinglist);
        expListView.setAdapter(listAdapter);



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_shoppinglsit) {
            // Handle the camera action
        } else if (id == R.id.nav_group) {

        } else if (id == R.id.nav_recipe) {

        } else if (id == R.id.nav_item) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Obst und Gemüse");
        listDataHeader.add("Milchprodukte");
        listDataHeader.add("Getränke");

        // Adding child data
        List<String> category1 = new ArrayList<String>();
        category1.add("Banane");
        category1.add("Salat");
        category1.add("Gurke");
        category1.add("Orange");

        List<String> category2 = new ArrayList<String>();
        category2.add("Milch");
        category2.add("Rahm");
        category2.add("Käse");

        List<String> category3 = new ArrayList<String>();
        category3.add("Eistee");
        category3.add("Orangensaft");

        listDataChild.put(listDataHeader.get(0), category1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), category2);
        listDataChild.put(listDataHeader.get(2), category3);
    }

}
