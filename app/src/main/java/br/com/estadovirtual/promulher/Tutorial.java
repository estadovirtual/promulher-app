package br.com.estadovirtual.promulher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.viewpagerindicator.UnderlinePageIndicator;

import br.com.estadovirtual.promulher.classes.ViewPagerAdapter;


public class Tutorial extends Activity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private UnderlinePageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        String[] labels = new String[]{
                "Tela de Disfarce",
                "Seus Relatos",
                "Seus Dados Pessoais",
                "Sobre o Aplicativo",
                "Rede de Ajuda Pessoal",
                "Disque 180"
        };

        String[] texts = new String[]{
                "A tela de disfarce serve para que ninguém veja o que realmente é o aplicativo. Para sair da tela de disfarce, basta agitar seu aparelho.",
                "Seus relatos serão usados somente para estatísticas. Estas serão usadas para que o governo tome medidas para melehorar o apoio à mulher.",
                "É muito importante que você cadastre seus dados pessoais para que possamos ter precisão em uma possível ajuda futura. Seus dados em nenhum momento serão divulgados sem permissão.",
                "O aplicativo PRÓ mulher tem por objetivo montar gerar benefícios reais as mulheres vítimas de violência e gerar informações geolocalizadas que podem ser utilizadas para nortear políticas públicas.",
                "A rede de ajuda pessoal, é um grupo pessoas próximas à você que poderão te ajudar caso alguma ameaça aconteça. Eles poderão ser avisados sobre eventual problema.",
                "Número de utilidade pública que oferece uma escuta qualificada, informa, auxilia, orienta e encaminha a mulher para os serviços da Rede de Atendimento mais próximos."
        };

        int[] flag = new int[]{
                R.drawable.disguise_screen,
                R.drawable.icon_notifications,
                R.drawable.icon_who_are_you,
                R.drawable.icon_app,
                R.drawable.icon_help_contacts,
                R.drawable.icon_180
        };

        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Pass results to ViewPagerAdapter Class
        ViewPagerAdapter tmp = new ViewPagerAdapter();
        tmp.init(Tutorial.this, labels, texts, flag);

        pagerAdapter = tmp;

        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(pagerAdapter);

        // ViewPager Indicator
        mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tutorial, menu);
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
