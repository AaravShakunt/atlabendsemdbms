package com.example.basicdatabaseimplementationendsem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone;
    private Button buttonAdd;
    private ListView listViewContacts, listViewContacts2;
    private ArrayAdapter<String> adapter, adapter2;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] items = {"Banana", "Lime"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, items);
//
//        listViewContacts2 = findViewById(R.id.listViewContacts2);
//
//        listViewContacts2.setAdapter(adapter2);




        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewContacts = findViewById(R.id.listViewContacts);

        db = new DatabaseHandler(this);
        updateListView();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();

                if (!name.isEmpty() && !phone.isEmpty()) {
                    long result = db.addContact(new Contact(name, phone));
                    if (result != -1) {
                        updateListView();
                        editTextName.setText("");
                        editTextPhone.setText("");
                        Toast.makeText(MainActivity.this, "Contact added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter name and phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedName = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Update Contact");

                final EditText input = new EditText(MainActivity.this);
                input.setText(selectedName);
                builder.setView(input);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = input.getText().toString().trim();
                        if (!newName.isEmpty()) {
                            int result = db.updateContact(selectedName, newName);
                            if (result != -1) {
                                updateListView();
                                Toast.makeText(MainActivity.this, "Contact updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to update contact", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void updateListView() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, db.getAllContactNames());
        listViewContacts.setAdapter(adapter);
    }
}
