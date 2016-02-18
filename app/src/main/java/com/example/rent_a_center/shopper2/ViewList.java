package com.example.rent_a_center.shopper2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateItem(id);
                intent = new Intent(ViewList.this, ViewItem.class);
                intent.putExtra("_id", id);
                startActivity(intent);
            }
        });

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


    private void updateItem(long id){

        if(dbHandler.isItemPurchased((int) id) != 0){
            dbHandler.updateItem((int) id);
            Toast.makeText(this, "Item Purchased", Toast.LENGTH_LONG).show();
        }

        int count = dbHandler.getunpurchasedItems((int) this.id);

        if(count == 0){

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Shopper");
            builder.setContentText(shoppingListName + "completed!");

            intent = new Intent(this, ViewList.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(2142, builder.build());
        }

    }

}
