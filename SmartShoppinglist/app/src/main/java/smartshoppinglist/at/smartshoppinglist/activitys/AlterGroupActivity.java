package smartshoppinglist.at.smartshoppinglist.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import smartshoppinglist.at.smartshoppinglist.InputValidator;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.fragments.GroupFragment;
import smartshoppinglist.at.smartshoppinglist.objects.Group;

public class AlterGroupActivity extends AppCompatActivity {

    private Group group;
    private AlertDialog addUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_group);
        context = this;
        group = (Group)getIntent().getSerializableExtra("group");
        if (group != null){
            EditText name = findViewById(R.id.activity_alter_group_groupname);
            name.setText(group.getName());
            if(group.getUsers() != null){
                ListView listView = findViewById(R.id.alter_group_member_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list_item,group.getUsernames());
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
                                /*if(!UserExists()) {
                                    name.setError(getString(R.string.email_does_not_exist));
                                    throw new Exception();
                                }*/
                                addUser.dismiss();
                            }catch (Exception e) {
                            }
                        }
                    });
                    addUser = alertDialogBuilder.create();
                    addUser.show();
                }
            });

            Button save = findViewById(R.id.alter_group_save_button);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setName(name.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("group", group);
                    setResult(GroupFragment.REQUEST_ID, intent);
                    finish();
                }
            });
        }
    }
}
