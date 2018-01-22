package com.pixel.dessin;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TabHost myTabs;
    private Draw draw;
    private ListView colors;
    private ListView lines;
    Integer[] clrs;
    Integer[] lns;
    private String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        myTabs = findViewById(R.id.tabhost);
        myTabs.setup();

        draw = findViewById(R.id.draw);
        colors = findViewById(R.id.colors);
        lines = findViewById(R.id.lines);


        clrs = new Integer[]{Color.BLACK, Color.DKGRAY, Color.LTGRAY, Color.WHITE, Color.YELLOW,  Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
        ArrayAdapter<Integer> clrAdapter = new ArrayAdapter<Integer>(getApplicationContext(), android.R.layout.simple_list_item_1, clrs){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(clrs[position]);
                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setBackgroundColor(clrs[position]);
                tv.setTextColor(clrs[position]);
                // Generate ListView Item using TextView
                return view;
            }
        };
        colors.setAdapter(clrAdapter);

        colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Integer selectedItem = (Integer) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                //draw.onSizeChanged(draw.width,draw.height, draw.width, draw.height);
                draw.paint.setColor(selectedItem);
                myTabs.setCurrentTab(2);
            }
        });

        lns = new Integer[]{2, 4, 5, 7, 10, 15, 20, 25, 30, 40, 50};
        ArrayAdapter<Integer> lnAdapter = new ArrayAdapter<Integer>(this.getApplicationContext(), android.R.layout.simple_list_item_1, lns){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);
                // Generate ListView Item using TextView
                return view;
            }
        };

        lines.setAdapter(lnAdapter);

        lines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Integer selectedItem = (Integer) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                //draw.onSizeChanged(draw.width,draw.height, draw.width, draw.height);
                draw.paint.setStrokeWidth(selectedItem);
                myTabs.setCurrentTab(2);
            }
        });

        //Toolbar myTB = findViewById(R.id.toolbar);
        //setSupportActionBar(myTB);

        TabHost.TabSpec spec = myTabs.newTabSpec("colors");
        spec.setContent(R.id.colors);
        spec.setIndicator("",
                getResources().getDrawable(R.drawable.colors));
        myTabs.addTab(spec);

        spec = myTabs.newTabSpec("paths");
        spec.setContent(R.id.lines);
        spec.setIndicator("",
                getResources().getDrawable(R.drawable.lines));
        myTabs.addTab(spec);

        spec = myTabs.newTabSpec("draw");
        spec.setContent(R.id.draw);
        spec.setIndicator("",
                getResources().getDrawable(R.drawable.draw));

        myTabs.addTab(spec);
        myTabs.setCurrentTab(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemSave : Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                String uri = MediaStore.Images.Media.insertImage(this.getContentResolver(), draw.onSave(), "title", null);
                break;
            case R.id.itemClear : Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
                draw.clear();
                break;
            case R.id.itemUndo : Toast.makeText(getApplicationContext(), "Undo", Toast.LENGTH_SHORT).show();
                draw.undo();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission () {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {permission};
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
