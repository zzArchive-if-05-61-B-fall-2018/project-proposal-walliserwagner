package smartshoppinglist.at.smartshoppinglist.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.fragments.GroupFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.HomeFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.ItemsFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.ListFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.RecipeFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.SearchFragment;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.CategoryList;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmentTransaction;
    private Shoppinglist shoppinglist;
    private ItemList items;
    private GroupList groupList;
    private CategoryList<ItemContainer> itemCategorys;
    private static MainActivity mainActivity;





    public static MainActivity getInstance(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        Save.context = getApplicationContext();
        Read.context = getApplicationContext();
        Save.id = Config.getInstance().getUser().getId();
        Read.id = Config.getInstance().getUser().getId();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_container);
                if (f instanceof SearchFragment){
                    ((SearchFragment)f).createItem();
                }
                else if (f instanceof HomeFragment){
                    ((HomeFragment)f).searchItem(getApplication());
                }
                else if (f instanceof GroupFragment){
                    ((GroupFragment)f).createGroup();
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





        getGroups().addGroup(new Group("Arbeit",null));
        getGroups().findGroupByName("Arbeit").addShoppinglist(new Shoppinglist("Einkauf"));



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
        hideKeyboard(this);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            Fragment f = getTopFragment();
            if (f instanceof SearchFragment){
                ((SearchFragment)f).onBackPressed();
            }
            getFragmentManager().popBackStack();
        }

    }
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void setAddButtonVisible(){
        View v = findViewById(R.id.fab);
        v.setVisibility(View.VISIBLE);
    }
    public Fragment getTopFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }


    public Shoppinglist getShoppinglist() {
        getItems();
        if(shoppinglist == null){
            shoppinglist = new Shoppinglist("Haushalt");
            getGroups().findGroupByName("Local").addShoppinglist(shoppinglist);
        }
        return shoppinglist;
    }

    public ItemList getItems() {
        if(items == null){
            items = ItemList.getInstance();
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
    public GroupList getGroups(){
        if(groupList == null){
            groupList = GroupList.getInstance();
        }
        return groupList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == GroupFragment.REQUEST_ID) {
                Group group = (Group)data.getSerializableExtra("group");
                String name = data.getStringExtra("name");
                if (group != null){
                    Group oldGroup = groupList.findGroupByName(name);
                    groupList.removeGroups(oldGroup);
                    groupList.addGroup(group);
                }
            }
    }

    public void setShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglist = shoppinglist;
    }
}
