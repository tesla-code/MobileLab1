package com.example.bankingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TransactionsActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Bundle b=this.getIntent().getExtras();
        String[] array=b.getStringArray("Tlist");

        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array);

        listView = findViewById(R.id.tlist);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {

                String[] splitStrings = listView.getItemAtPosition(index).toString().split("\\|");
                String longClickInfo = splitStrings[1] + splitStrings [2];

                Log.i("long click","Long was clicked");
                Toast.makeText(getApplicationContext(),
                               longClickInfo,//listView.getItemAtPosition(index).toString(),
                               Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }
}
