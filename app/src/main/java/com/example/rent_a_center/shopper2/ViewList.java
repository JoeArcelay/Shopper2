package com.example.rent_a_center.shopper2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class ViewList extends AppCompatActivity {

    Bundle bundle;
    long id;
    DBHandler dbHandler;
    String shoppingListName;
    Intent intent;
    ShoppingListItems shoppingListItemsAdapter;
    ListView itemListView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = this.getIntent().getExtras();

        id = bundle.getLong("_id");

        dbHandler = new DBHandler(this, null);

        String shoppingListName = dbHandler.getShoppingListName((int) id);

        this.setTitle(shoppingListName);

        Cursor cursor = dbHandler.getShoppingListItem((int) id);

        shoppingListItemsAdapter = new ShoppingListItems(this, cursor, 0);

        itemListView = (ListView) this.findViewById(R.id.itemListView);

        itemListView.setAdapter(shoppingListItemsAdapter);

        toolbar.setSubtitle("Total Cost: " + dbHandler.getShoppingListTotalCost((int) id));
    }

    public void openAddItem(View view){

        intent = new Intent(this, AddItem.class);
        intent.putExtra("_id", id);
        startActivity(intent);

    }

    public void deleteShoppingList(MenuItem menuItem){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_list:
                intent = new Intent(this, CreateList.class);
                startActivity(intent);
                return true;
            case R.id.action_add_item:
                intent = new Intent(this, AddItem.class);
                intent.putExtra("_id", id);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
