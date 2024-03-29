package br.com.estadovirtual.promulher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Preferences extends Activity {

    Button register, without_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        register = (Button) findViewById(R.id.button_with_id);
        without_id = (Button) findViewById(R.id.button_without_id);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Preferences.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        without_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO aviso de que pode cadastrar depois a qualquer hora
                // TODO salvar nas preferências que esse cara ainda não se cadastrou (Toda vez que dar load no app, buscar essa opção para mostrar ou não o menu de cadastro)

                Intent intent = new Intent(Preferences.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
