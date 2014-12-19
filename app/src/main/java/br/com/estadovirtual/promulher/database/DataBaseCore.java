package br.com.estadovirtual.promulher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Phil on 27/11/2014.
 */
public class DataBaseCore extends SQLiteOpenHelper{

    /**
     * Database Name (pro_mulher)
     */
    private static final String DATABASE_NAME = "pro_mulher";

    /**
     * Database Version (pro_mulher)
     */
    private static final int DATABASE_VERSION = 9;

    public DataBaseCore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela do usuário
        db.execSQL("CREATE TABLE user (_unique_id VARCHAR(50) PRIMARY KEY, user_name VARCHAR(150) NULL, user_email VARCHAR(100) NULL, user_cep VARCHAR(8) NULL, user_address VARCHAR(255) NULL, user_age INTEGER NULL, user_childrens INTEGER NULL, user_income INTEGER NULL, user_scholarity INTEGER NULL, user_accept_terms INTEGER NULL, mac_address VARCHAR(100) NULL)");

        // Cria a tabela dos contatos de ajuda
        db.execSQL("CREATE TABLE help_contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, contact_name VARCHAR(150) NOT NULL, contact_number VARCHAR(50) NOT NULL, contact_send_help INTEGER NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Essa função é chamada quando a versão do banco de dados é mudada
        db.execSQL("DROP TABLE user");
        db.execSQL("DROP TABLE help_contacts");

        // Cria denovo
        onCreate(db);
    }
}
