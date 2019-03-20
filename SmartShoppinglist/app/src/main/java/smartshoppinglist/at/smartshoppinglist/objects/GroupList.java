package smartshoppinglist.at.smartshoppinglist.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GroupList {
    private static final GroupList instance = new GroupList();
    private List<Group> groups;

    public static GroupList getInstance() {
        return instance;
    }

    private GroupList() {
        groups = new ArrayList<>();
        List<String> user = new LinkedList<String>();
        user.add("User");
        groups.add(new Group("Local",user));
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
    }
    public void removeGroups(Group group){
        groups.remove(group);
    }
    public Group[] getGroups(){
        return groups.toArray(new Group[0]);
    }
}
