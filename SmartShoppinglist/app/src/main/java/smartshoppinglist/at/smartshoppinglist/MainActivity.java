package smartshoppinglist.at.smartshoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmentTransaction;
    private Shoppinglist shoppinglist;
    private ItemList items;
    private CategoryList<ItemContainer> itemCategorys;
    private static MainActivity mainActivity;

    public static MainActivity getInstance(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getVisibleFragment();
                if (f instanceof HomeFragment){
                    ((HomeFragment)f).searchItem(getApplication());
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");



    }
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

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

        if (id == R.id.nav_home) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new HomeFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("SmartShoppinglist");
            item.setChecked(true);
        } else if (id == R.id.nav_shoppinglsit) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new ListFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Einkaufslisten");
            item.setChecked(true);
        } else if (id == R.id.nav_group) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new GroupFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Gruppen");
            item.setChecked(true);
        } else if (id == R.id.nav_recipe) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new RecipeFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Recepte");
            item.setChecked(true);
        } else if (id == R.id.nav_item) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new ItemsFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle("Artikel");
            item.setChecked(true);
        } else if (id == R.id.nav_invite) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        View v = findViewById(R.id.fab);
        v.setVisibility(View.VISIBLE);
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public Shoppinglist getShoppinglist() {
        if(shoppinglist == null){
            shoppinglist = new Shoppinglist("Haushalt");
            Category<ItemContainer> category;
            category = new Category<ItemContainer>(ItemContainer.class,"Obst & Gemüse", true);
            category.addElement(new ItemContainer(new Item("Avocado", R.drawable.avocado,"Obst & Gemüse"),3));
            category.addElement(new ItemContainer(new Item("Chili", R.drawable.chilipepper,"Obst & Gemüse"),1));
            category.addElement(new ItemContainer(new Item("Mais", R.drawable.corn,"Obst & Gemüse"),1,"Pak"));
            category.addElement(new ItemContainer(new Item("Paprika", R.drawable.paprika,"Obst & Gemüse"),1,"kg"));
            category.sort();
            shoppinglist.addCategory(category);
            category = new Category<ItemContainer>(ItemContainer.class,"Fisch & Fleisch",true);
            category.addElement(new ItemContainer(new Item("Speck", R.drawable.bacon,"Fisch & Fleisch"),500,"g"));
            category.addElement(new ItemContainer(new Item("Forelle", R.drawable.fish,"Fisch & Fleisch"),200,"dag"));
            category.sort();
            shoppinglist.addCategory(category);
            category = new Category<ItemContainer>(ItemContainer.class,"Gewürze",true);
            category.addElement(new ItemContainer(new Item("Salz", R.drawable.saltshaker,"Gewürze"),"Pak"));
            category.addElement(new ItemContainer(new Item("Pfeffer", R.drawable.spice,"Gewürze"),"Pak"));
            category.addElement(new ItemContainer(new Item("Kümmel", R.drawable.ic_questionmark,"Gewürze"),1,"Pak"));
            category.sort();
            shoppinglist.addCategory(category);
            category = new Category<ItemContainer>(ItemContainer.class,"Süßigkeiten",true);
            category.addElement(new ItemContainer(new Item("Schokodonute", R.drawable.doughnut,"Süßigkeiten"),1));
            category.addElement(new ItemContainer(new Item("Kekse", R.drawable.cookies,"Süßigkeiten"),2,"Pak"));
            category.addElement(new ItemContainer(new Item("Zuckerl", R.drawable.christmascandy,"Süßigkeiten"),"Pak"));
            category.sort();
            shoppinglist.addCategory(category);
            ItemContainer ic = new ItemContainer(new Item("Wurst","Fisch & Fleisch"));
            shoppinglist.addItem(ic);
            shoppinglist.tickItem(ic);
            shoppinglist.addItem(new ItemContainer(new Item("Lego","Spielzeug")));
            //shoppinglist.unTickItem(ic);
            shoppinglist.removeItem(ic);
            shoppinglist.removeTickedItems();
        }
        return shoppinglist;
    }

    public ItemList getItems() {
        if(items == null){
            items = new ItemList();
            items.addItem(new Item("Apfle"));
            items.addItem(new Item("Birne"));
            items.addItem(new Item("Speck"));
            items.addItem(new Item("Käse"));
            items.addItem(new Item("Milch"));
            items.addItem(new Item("Ei"));
            items.addItem(new Item("Huhn"));
            items.addItem(new Item("Pute"));
        }
        return items;
    }

    public CategoryList<ItemContainer> getItemCategorys() {
        if(itemCategorys == null){
            itemCategorys = new CategoryList<>();
            itemCategorys.addCategory(new Category<ItemContainer>(ItemContainer.class,"Obst & Gemüse", true));
            itemCategorys.addCategory(new Category<ItemContainer>(ItemContainer.class,"Fisch & Fleisch",true));
            itemCategorys.addCategory(new Category<ItemContainer>(ItemContainer.class,"Gewürze",true));
            itemCategorys.addCategory(new Category<ItemContainer>(ItemContainer.class,"Süßigkeiten",true));
        }
        return itemCategorys;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                if (extra != null){
                    ItemContainer itemContainer = new Gson().fromJson(extra.getString("itemContainer"), ItemContainer.class);
                    shoppinglist.addItem(itemContainer);
                }
            }
    }
}
