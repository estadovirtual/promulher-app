package br.com.estadovirtual.promulher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import br.com.estadovirtual.promulher.tables.HelpContacts;
import br.com.estadovirtual.promulher.tables.User;

/**
 * Created by Alex on 27/11/2014.
 */
public class DataBase {

    private SQLiteDatabase database;

    public DataBase(Context context){

        DataBaseCore dataBaseCore = new DataBaseCore(context);
        this.database = dataBaseCore.getWritableDatabase();
    }

    public void insertUserInfos(User user){

        ContentValues contentValues = new ContentValues();
        contentValues.put("_unique_id", user.get_unique_id());
        contentValues.put("mac_address", user.getMac_address());

        this.database.insert("user", null, contentValues);
    }

    public void insertHelpContact(HelpContacts helpContacts){

        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_name", helpContacts.getContact_name());
        contentValues.put("contact_number", helpContacts.getContact_number());
        contentValues.put("contact_send_help", helpContacts.getContact_send_help());

        this.database.insert("help_contacts", null, contentValues);
    }

    public void updateUserInfos(User user){

        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name", user.getUser_name());
        contentValues.put("user_email", user.getUser_email());
        contentValues.put("user_cep", user.getUser_cep());
        contentValues.put("user_address", user.getUser_address());
        contentValues.put("user_age", user.getUser_age());
        contentValues.put("user_childrens", user.getUser_childrens());
        contentValues.put("user_income", user.getUser_income());
        contentValues.put("user_scholarity", user.getUser_scholarity());
        contentValues.put("user_accept_terms", user.getUser_accept_terms());

        this.database.update("user", contentValues, "_unique_id = ?", new String[]{user.get_unique_id()});
    }

    /**
     * Usado pra checar se o usuário já usou ou não o aplicativo
     * @return
     */
    public Boolean checkUser(){
        String[] columns = new String[]{"_unique_id"};
        Cursor cursor = this.database.query("user", columns, null, null, null, null, null);
        return (cursor.getCount() == 1);
    }

    /**
     * Busca o usuário que está salvo no banco de dados do celular
     * @return
     */
    public User getUser(){

        User current = new User();

        String[] columns = new String[]{"_unique_id", "user_name", "user_email", "user_cep", "user_address", "user_age", "user_childrens", "user_income", "user_scholarity", "user_accept_terms", "mac_address"};
        Cursor cursor = this.database.query("user", columns, null, null, null, null, null);

        if(cursor.getCount() == 1){

            cursor.moveToFirst();
            current.set_unique_id(cursor.getString(0));
            current.setUser_name(cursor.getString(1));
            current.setUser_email(cursor.getString(2));
            current.setUser_cep(cursor.getString(3));
            current.setUser_address(cursor.getString(4));
            current.setUser_age(cursor.getInt(5));
            current.setUser_childrens(cursor.getInt(6));
            current.setUser_income(cursor.getInt(7));
            current.setUser_scholarity(cursor.getInt(8));
            current.setUser_accept_terms(cursor.getInt(9));
            current.setMac_address(cursor.getString(10));
        }

        return current;
    }

    /**
     * Busca todos os contatos de ajuda cadastrados
     * @return
     */
    public Cursor getContacts(){

        String columns[] = new String[]{"_id", "contact_name", "contact_number", "contact_send_help"};
        return this.database.query("help_contacts", columns, null, null, null, null, null);
    }
}
