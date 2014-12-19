package br.com.estadovirtual.promulher.classes;

/**
 * Created by Phil on 03/11/2014.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.estadovirtual.promulher.Disguise;
import br.com.estadovirtual.promulher.R;

public class ViewPagerAdapter extends PagerAdapter {

    // Declare Variables
    private Context context;
    private String[] labels;
    private String[] texts;
    private int[] flag;
    private LayoutInflater inflater;
    private Button get_out;
    private Boolean check = false;

    public ViewPagerAdapter() {
        Log.d("Phil", "View Pager Adapter Iniciado...");
    }

    public void init(Context context, String[] labels, String[] texts, int[] flag) {

        // Set the variables
        this.context = context;
        this.texts = texts;
        this.flag = flag;
        this.labels = labels;

    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        //
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);

        // Get button "Go To App"
        get_out = (Button) itemView.findViewById(R.id.button_go_to_app);

        // On Click Listener
        get_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Activity) context, Disguise.class);

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        // Set the view text
        TextView info_label = (TextView) itemView.findViewById(R.id.info_label);
        info_label.setText(labels[position]);

        // Set the view text
        TextView info_text = (TextView) itemView.findViewById(R.id.info_text);
        info_text.setText(texts[position]);

        // Set the view image
        ImageView info_image = (ImageView) itemView.findViewById(R.id.info_image);
        info_image.setImageResource(flag[position]);

        // Set button visibility if the tutorial page is the number 3
        if((position + 1) == this.getCount()){
            get_out.setVisibility(View.VISIBLE);
        }else{
            get_out.setVisibility(View.INVISIBLE);
            get_out.setVisibility(View.GONE);
        }

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
