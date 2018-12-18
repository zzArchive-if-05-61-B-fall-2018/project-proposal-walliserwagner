package smartshoppinglist.at.smartshoppinglist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Category<T> {
    private List<T>  categrorizedObjects;
    private  String name;

    public Category(String name){
        this.name = name;
        categrorizedObjects = new ArrayList<T>();
    }

    public void addObject(T object){
        categrorizedObjects.add(object);
    }
    public <T> T[] getElements(){
        T[] array = (T[]) Array.newInstance(categrorizedObjects.get(0).getClass(),categrorizedObjects.size());
        return categrorizedObjects.toArray(array);
    }
    public void removeObject(T object){
        categrorizedObjects.remove(object);
    }

    public String getName() {
        return name;
    }
}
