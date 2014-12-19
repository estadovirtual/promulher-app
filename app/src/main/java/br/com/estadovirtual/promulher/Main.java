package br.com.estadovirtual.promulher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.estadovirtual.promulher.classes.Singleton;

public class Main extends Activity {

    private void goToActivity(final Intent intent){

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Esconde a Action Bar
        getActionBar().hide();

        // Checa se existe usuário (Se existir, é porque ela já executou alguma vez o aplicativo)
        Boolean checkUser = Singleton.getInstance().checkUser(this);

        // Para debug da tela de tutorial utilizar o ! na condição
        if(checkUser){

            // Vai direto para a activity de disfarce (Imagem da Frase do Dia)
            Intent intent = new Intent(Main.this, Disguise.class);
            this.goToActivity(intent);

        }else{

            // Vai para a activity de tutorial
            Intent intent = new Intent(Main.this, Tutorial.class);
            this.goToActivity(intent);
            // Mostra a tela de tutorial
            //Toast.makeText(this, "Não Existe (Criou): " + Singleton.getInstance().getUser().get_unique_id(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
