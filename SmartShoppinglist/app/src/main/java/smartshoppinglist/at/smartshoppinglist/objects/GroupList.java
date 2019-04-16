package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class GroupList {
    private List<Group> groups;

    public GroupList() {
        groups = new ArrayList<>();
    }

    public void populateGroups(){
        for (Group g:groups) {
            g.populateShoppinglist();
        }
    }

    public Group findGroupByName(String name){
        for (Group group:groups) {
            if (group.getName().equals(name)){
                return group;
            }
        }
        return null;
    }
    public boolean addGroup(Group group){
        if(findGroupByName(group.getName())!= null) return false;
        groups.add(group);
        setChanges();
        return true;
    }
    public void removeGroups(Group group){
        groups.remove(group);
        setChanges();
    }
    public Group[] getGroups(){
        return groups.toArray(new Group[0]);
    }

    public String[] getGroupNames(){
        List<String> result = new ArrayList<>();
        for (Group group:groups) {
                result.add(group.getName());
        }
        return result.toArray(new String[0]);
    }

    private void setChanges(){
        Save.save(this);
    }
}
