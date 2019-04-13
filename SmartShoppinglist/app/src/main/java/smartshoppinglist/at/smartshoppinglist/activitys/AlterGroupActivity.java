package smartshoppinglist.at.smartshoppinglist.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.fragments.GroupFragment;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.uiadapters.GroupListAdapter;

public class AlterGroupActivity extends AppCompatActivity {

    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_group);

        group = (Group)getIntent().getSerializableExtra("group");
        if (group != null){
            String groupName = group.getName();
            EditText name = findViewById(R.id.activity_alter_group_groupname);
            name.setText(group.getName());
            if(group.getUsers() != null){
                ListView listView = findViewById(R.id.alter_group_member_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_item,group.getUsernames());
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
            }

            Button save = findViewById(R.id.alter_group_save_button);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setName(name.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("group", group);
                    intent.putExtra("name",groupName);
                    setResult(GroupFragment.REQUEST_ID, intent);
                    finish();
                }
            });
        }
    }
}
