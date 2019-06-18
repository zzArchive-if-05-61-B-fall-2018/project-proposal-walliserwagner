package smartshoppinglist.at.smartshoppinglist.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.fragments.CategoryFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.GroupFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.HomeFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.InviteFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.ItemsFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.ListFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.RecipeFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.RecipeListFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.RecipeViewFragment;
import smartshoppinglist.at.smartshoppinglist.fragments.SearchFragment;
import smartshoppinglist.at.smartshoppinglist.localsave.Read;
import smartshoppinglist.at.smartshoppinglist.localsave.Save;
import smartshoppinglist.at.smartshoppinglist.objects.Category;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.GroupList;
import smartshoppinglist.at.smartshoppinglist.objects.Invite;
import smartshoppinglist.at.smartshoppinglist.objects.InviteList;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemCategoryList;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;
import smartshoppinglist.at.smartshoppinglist.objects.RecipeList;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;
import smartshoppinglist.at.smartshoppinglist.objects.User;
import smartshoppinglist.at.smartshoppinglist.server.Server;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction fragmentTransaction;
    private Shoppinglist shoppinglist;
    private ItemList items;
    private GroupList groupList;
    private InviteList inviteList;
    private RecipeList recipeList;
    private ItemCategoryList itemCategorys;
    private Config config;
    private static MainActivity mainActivity;
    private User currentUser;

    private String[] languages = new String[]{"English", "Deutsch"};
    private String[] locals = new String[]{"en", "de"};




    public static MainActivity getInstance(){
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = Config.getInstance();
        mainActivity = this;
        currentUser = config.getUser();
        Save.context = getApplicationContext();
        Read.context = getApplicationContext();
        Save.id = config.getUser().getId();
        Read.id = config.getUser().getId();
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
                else if (f instanceof ListFragment){
                    ((ListFragment)f).addListPooup();
                }
                else if (f instanceof CategoryFragment){
                    ((CategoryFragment)f).createItem();
                }
                else if (f instanceof ItemsFragment){
                    ((ItemsFragment)f).createItem();
                }
                else if (f instanceof InviteFragment){
                    ((InviteFragment)f).changeFragment();
                }
                else if(f instanceof RecipeListFragment){
                    ((RecipeListFragment)f).createRecipe();
                }
                else if(f instanceof RecipeFragment){
                    ((RecipeFragment)f).createRecipe();
                }
                else if(f instanceof RecipeViewFragment){
                    ((RecipeViewFragment)f).addItemsToList();
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
        if(getSupportFragmentManager().findFragmentById(R.id.main_container) != null) fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.main_container));
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(R.string.home);

        ItemContainer.setDefaultUnit(getString(R.string.stk));
        Item.setDefaultDefaultUnit(getString(R.string.stk));

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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_reload){
            reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void reload(){
        getInviteList();
        Group[] groups = getGroups().getGroups();
        for (int i = 0; i < groups.length; i++) {
            Server.getInstance().getGroupChanges(groups[i], this);
        }
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (f instanceof HomeFragment){
            ((HomeFragment)f).reload();
        }
        else if (f instanceof GroupFragment){
            ((GroupFragment)f).reload();
        }
        else if (f instanceof ListFragment){
            ((ListFragment)f).reload();
        }
        else if (f instanceof InviteFragment){
            ((InviteFragment)f).reload();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new HomeFragment());
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.smart_shopping_list);
            item.setChecked(true);
        } else if (id == R.id.nav_shoppinglsit) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new ListFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.shopping_lists);
            item.setChecked(true);
        } else if (id == R.id.nav_group) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new GroupFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.groups);
            item.setChecked(true);
        } else if (id == R.id.nav_recipe) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new RecipeListFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.recipes);
            item.setChecked(true);
        } else if (id == R.id.nav_item) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new ItemsFragment(), "item");
            fragmentTransaction.addToBackStack("item");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.items);
            item.setChecked(true);
        } else if (id == R.id.nav_invite) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, new InviteFragment());
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.invites);
            item.setChecked(true);
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
            else if(f instanceof HomeFragment){
                ((HomeFragment)f).onBackPressed();
            }
            else if(f instanceof CategoryFragment){
                ((CategoryFragment)f).onBackPressed();
            }
            else if(f instanceof ItemsFragment){
                ((ItemsFragment)f).onBackPressed();
            }
            else if(f instanceof RecipeFragment){
                ((RecipeFragment)f).onBackPressed();
            }
            else if(f instanceof RecipeViewFragment){
                ((RecipeViewFragment)f).onBackPressed();
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


    public User getCurrentUser(){
        return currentUser;
    }

    public Shoppinglist getShoppinglist() {
        if(shoppinglist == null){
            Shoppinglist sl = Config.getInstance().getCurrentShoppinglist();
            if(sl != null){
                setShoppinglist( getGroups().findGroupById(sl.getGroup().getId()).findListByName(sl.getName()));
            }
            if(shoppinglist == null){
                boolean found = false;
                for (Shoppinglist s:getGroups().getDefault().getShoppinglists()) {
                    if(s.isDefault()){
                        found = true;
                        setShoppinglist(s);
                    }
                }
                if(!found)
                    setShoppinglist(getGroups().getDefault().createList(getString(R.string.shopping_list),true));
                    getItemCategorys().addCategoryName(getString(R.string.general),true);
            }
        }
        renewPriories(shoppinglist.getItems());
        shoppinglist.sort();
        return shoppinglist;
    }

    public ItemList getItems() {
        if(items == null){
            items = Read.readItemList();
        }
        return items;
    }

    public ItemCategoryList getItemCategorys() {
        if(itemCategorys == null){
            itemCategorys = Read.readCategoryList();
        }
        return itemCategorys;
    }


    public GroupList getGroups(){
        if(groupList == null){
            groupList = Read.readGroupList();
            groupList = Server.getInstance().getGrouplist(groupList, this);
            groupList.populateGroups();
            boolean found = false;
            for (Group g:groupList.getGroups()) {
                if(g.isDefault()){
                    found = true;
                }
            }
            if (!found){
                groupList.addGroup(new Group(getString(R.string.local), config.getUser(),true));
            }
        }
        return groupList;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == GroupFragment.REQUEST_ID) {
                Group group = (Group)data.getSerializableExtra("group");
                if (group != null){
                    Group oldGroup = groupList.findGroupById(group.getId());
                    groupList.removeGroups(oldGroup);
                    groupList.addGroup(group);
                    Fragment f = getSupportFragmentManager().getFragments().get(0);
                    if(f instanceof GroupFragment)((GroupFragment)f).notifyDatasetChanged();
                }
            }
    }

    public void setShoppinglist(Shoppinglist shoppinglist) {
        this.shoppinglist = shoppinglist;
        Config.getInstance().setCurrentShoppinglist(shoppinglist);
        if(shoppinglist == null) getShoppinglist();
    }
    private void renewPriories(Category[] categories){
        for (Category c: categories) {
            Integer p = getItemCategorys().getPriorityByName(c.getName());
            if(p == null && c.getPriority() > 0) getItemCategorys().addCategoryName(c.getName());
            if(p != null)c.setPriority(p);
        }
    }
    public InviteList getInviteList(){
        if(inviteList == null) {
            inviteList = new InviteList();
        }
        String invites = Server.getInstance().getRequest(String.format("/invite?userid=%d", MainActivity.getInstance().getCurrentUser().getId()));
        try {
            JSONArray jsonArray = new JSONArray(invites);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                inviteList.addInvite(new Invite(jsonObject.getString("name"),jsonObject.getInt("groupid"), jsonObject.getString("email")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inviteList;
    }
    public RecipeList getRecipeList(){
        if(recipeList == null){
            recipeList = Read.readRecipeList();
        }
        return recipeList;
    }
    public void setLocale(String string){
        Locale locale = new Locale(string);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("Lang",string);
        editor.apply();
        MainActivity.getInstance().getItemCategorys().addCategoryName(getString(R.string.general),true);
        MainActivity.getInstance().getItemCategorys().getCategoryBought();
        MainActivity.getInstance().getGroups().getDefault().setName(getString(R.string.local));
        Shoppinglist shoppinglist = MainActivity.getInstance().getGroups().getDefault().getDefaultList();
        if(shoppinglist != null) shoppinglist.setName(getString(R.string.shopping_list));
        recreate();
    }

    public void loadLocal(){

        SharedPreferences preferences = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String lang = preferences.getString("Lang","");
        setLocale(lang);
    }
    public String getLanguage(){
        SharedPreferences preferences = getSharedPreferences("Settings",Activity.MODE_PRIVATE);
        String lang = preferences.getString("Lang","");
        if(lang.equals("en")) return "English";
        if(lang.equals("de")) return "Deutsch";
        return lang;
    }
    public String getLocal(){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Lang","");
    }

    public String[] getLanguages() {
        return languages;
    }

    public String[] getLocals() {
        return locals;
    }
}
