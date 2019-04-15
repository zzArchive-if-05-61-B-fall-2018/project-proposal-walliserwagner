package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.localsave.Save;

public class GroupList {
    private List<Group> groups;

    public GroupList() {
        groups = new ArrayList<>();
        groups.add(new Group("Local",Config.getInstance().getUser()));
    }
    public Group findGroupByName(String name){
        for (Group group:groups) {
            if (group.getName().equals(name)){
                return group;
            }
        }
        return null;
    }
    public void addGroup(Group group){
        groups.add(group);
        setChanges();
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
