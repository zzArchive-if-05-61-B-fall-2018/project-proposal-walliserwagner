package smartshoppinglist.at.smartshoppinglist;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Shoppinglist shoppinglist = new Shoppinglist("Haushalt");
        Category<ItemContainer> category;
        category = new Category<ItemContainer>("Obst & Gemüse");
        category.addObject(new ItemContainer(new Item("Avocado", R.drawable.avocado),3));
        category.addObject(new ItemContainer(new Item("Chili", R.drawable.chilipepper),1));
        category.addObject(new ItemContainer(new Item("Mais", R.drawable.corn),1,"Pak"));
        category.addObject(new ItemContainer(new Item("Paprika", R.drawable.paprika),1,"kg"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Fisch & Fleisch");
        category.addObject(new ItemContainer(new Item("Speck", R.drawable.bacon),500,"g"));
        category.addObject(new ItemContainer(new Item("Forelle", R.drawable.fish),200,"dag"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Gewürze");
        category.addObject(new ItemContainer(new Item("Salz", R.drawable.saltshaker),"Pak"));
        category.addObject(new ItemContainer(new Item("Pfeffer", R.drawable.spice),"Pak"));
        category.addObject(new ItemContainer(new Item("Kümmel", R.drawable.ic_questionmark),1,"Pak"));
        shoppinglist.addCategory(category);
        category = new Category<ItemContainer>("Süßigkeiten");
        category.addObject(new ItemContainer(new Item("Schokodonute", R.drawable.doughnut),1));
        category.addObject(new ItemContainer(new Item("Kekse", R.drawable.cookies),2,"Pak"));
        category.addObject(new ItemContainer(new Item("Zuckerl", R.drawable.christmascandy),"Pak"));
        shoppinglist.addCategory(category);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        expListView = (ExpandableListView) v.findViewById(R.id.listView);
        listAdapter = new ExpandableListAdapter(getActivity().getApplicationContext(), shoppinglist);
        expListView.setAdapter(listAdapter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(shoppinglist.getName());
        return v;
    }

}
