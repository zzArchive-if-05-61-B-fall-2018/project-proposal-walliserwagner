package smartshoppinglist.at.smartshoppinglist.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.fragments.GroupFragment;
import smartshoppinglist.at.smartshoppinglist.objects.Group;
import smartshoppinglist.at.smartshoppinglist.objects.User;
import smartshoppinglist.at.smartshoppinglist.server.Server;

public class AlterGroupActivity extends AppCompatActivity {

    private Group group;
    private AlertDialog addUser;
    private Context context;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_group);
        context = this;
        group = (Group)getIntent().getSerializableExtra("group");
        if (group != null){
            TextView name = findViewById(R.id.activity_alter_group_groupname);
            name.setText(group.getName());
            if(group.getUsers() != null){
                ListView listView = findViewById(R.id.alter_group_member_list);
                adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_item,group.getUsernames());
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
            }
            ImageButton imageButton = findViewById(R.id.add_user);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popup = inflater.inflate(R.layout.simple_add_popup, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(popup);
                    final EditText name = popup.findViewById(R.id.simple_add_popup_name);
                    name.setHint(R.string.email);
                    Button button = popup.findViewById(R.id.simple_add_popup_add);
                    button.setText(R.string.send);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if(!InputValidator.validInputString(name.getText().toString(),20)) {
                                    name.setError(getString(R.string.invalid_input));
                                    throw new Exception();
                                }
                                if(!Server.getInstance().userExists(name.getText().toString(), MainActivity.getInstance())) {
                                    name.setError(getString(R.string.email_does_not_exist));
                                    throw new Exception();
                                }
                                else{
                                    Server.getInstance().postRequest("/invite", String.format("{\"senderid\":\"%d\", \"receiveremail\":\"%s\", \"groupid\":\"%d\"}", MainActivity.getInstance().getCurrentUser().getId(), name.getText().toString(), group.getId()));
                                }
                                addUser.dismiss();
                            }catch (Exception e) {
                            }
                        }
                    });
                    addUser = alertDialogBuilder.create();
                    addUser.show();
                }
            });
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.options);
        getMenuInflater().inflate(R.menu.member_long_click_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.member_long_click_remove:
                User user = group.getUsers()[index];
                Toast.makeText(getApplicationContext(),R.string.not_implemented_yet,Toast.LENGTH_SHORT).show(); // replace with remove user
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
