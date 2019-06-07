package smartshoppinglist.at.smartshoppinglist.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Config;
import smartshoppinglist.at.smartshoppinglist.objects.Shoppinglist;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView acoountName = this.findViewById(R.id.settings_account_name);
        acoountName.setText(Config.getInstance().getUser().getName().toString());

        TextView languageType = this.findViewById(R.id.settings_language_type);
        String lang = MainActivity.getInstance().getLanguage();
        languageType.setText(lang );

        LinearLayout language = this.findViewById(R.id.settings_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
        LinearLayout logout = this.findViewById(R.id.settings_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.getInstance().setUser(null);
                Config.getInstance().setCurrentShoppinglist(null);
                MainActivity.getInstance().finish();
                Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                SettingsActivity.this.startActivity(intent);
                finish();
            }
        });

    }
    public void showChangeLanguageDialog(){
        final String[] listItems = MainActivity.getInstance().getLanguages();
        final  String[] locals = MainActivity.getInstance().getLocals();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_language);
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.getInstance().setLocale(locals[which]);
                MainActivity.getInstance().getItemCategorys().addCategoryName(getString(R.string.general),true);
                MainActivity.getInstance().getItemCategorys().getCategoryBought();
                MainActivity.getInstance().getGroups().getDefault().setName(getString(R.string.local));
                Shoppinglist shoppinglist = MainActivity.getInstance().getGroups().getDefault().getDefaultList();
                if(shoppinglist != null) shoppinglist.setName(getString(R.string.shopping_list));
                recreate();
                MainActivity.getInstance().recreate();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
