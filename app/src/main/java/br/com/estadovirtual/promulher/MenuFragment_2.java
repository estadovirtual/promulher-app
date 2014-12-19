package br.com.estadovirtual.promulher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import br.com.estadovirtual.promulher.database.DataBase;
import br.com.estadovirtual.promulher.tables.HelpContacts;

/**
 * Created by Phil on 27/11/2014.
 */
public class MenuFragment_2 extends Fragment{

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.menu_fragment_2, container, false);

        getContacts();

        Button button = (Button) view.findViewById(R.id.button_register_contact);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View promptView = layoutInflater.inflate(R.layout.prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);

                final EditText contact_name = (EditText) promptView.findViewById(R.id.contact_name);
                final EditText contact_phone = (EditText) promptView.findViewById(R.id.contact_phone);
                final CheckBox contact_notify = (CheckBox) promptView.findViewById(R.id.contact_notify);
                final DataBase dataBase = new DataBase(v.getContext());

                // setup a dialog window
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // get user input and set it to result
                            //editTextMainScreen.setText(input.getText());
                            HelpContacts helpContacts = new HelpContacts();
                            helpContacts.setContact_name(contact_name.getText().toString());
                            helpContacts.setContact_number(contact_phone.getText().toString());
                            helpContacts.setContact_send_help((contact_notify.isChecked())? 1 : 0);

                            dataBase.insertHelpContact(helpContacts);

                            Toast toast = Toast.makeText(getActivity(), "Contato cadastrado com sucesso!", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();

                            getContacts();
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();
            }
        });


        return this.view;
    }

    public void getContacts(){

        DataBase dataBase = new DataBase(getActivity());

        Cursor contacts = dataBase.getContacts();

        String columns[] = new String[]{"_id", "contact_name", "contact_number"};

        SimpleCursorAdapter dataSource = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.row, contacts, columns, new int[]{R.id.contactName, R.id.contactPhone, R.id.contactNotify}, 0);

        ListView listView = (ListView) view.findViewById(R.id.list);

        listView.setAdapter(dataSource);
    }
}
