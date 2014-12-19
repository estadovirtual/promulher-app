package br.com.estadovirtual.promulher;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

import br.com.estadovirtual.promulher.classes.EvPost;
import br.com.estadovirtual.promulher.classes.Singleton;
import br.com.estadovirtual.promulher.database.DataBase;
import br.com.estadovirtual.promulher.tables.User;

/**
 * Created by Phil on 27/11/2014.
 */
public class MenuFragment_3 extends Fragment{

    private ProgressDialog progressDialog = null;
    View view;
    EditText user_name, user_email, user_cep, user_address, user_age, user_childrens;
    Spinner user_income, user_scholarity;
    CheckBox user_accept_terms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.menu_fragment_3, container, false);

        this.setFields();
        this.setValues();

        Button saveButton = (Button) view.findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!user_accept_terms.isChecked()){
                    user_accept_terms.setError("É obrigatório aceitar os termos.");
                }else{

                    try {

                        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        // Pega os valores
                        User user = getValues();

                        // Atualiza o usuario no banco de dados do aparelho
                        DataBase dataBase = new DataBase(getActivity().getBaseContext());
                        dataBase.updateUserInfos(user);

                        // Realoca o usuario no singleton
                        Singleton.getInstance().reloadUser(user);

                        // Send Post to API
                        // Call auth function
                        progressDialog = ProgressDialog.show(getActivity(), "", "Registrando a solicitação. Aguarde...", false);

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("device_android_id", Singleton.getInstance().getUser().get_unique_id());
                        requestParams.put("device_mac_address", user.getMac_address());
                        requestParams.put("user_name", user.getUser_name());
                        requestParams.put("user_email", user.getUser_email());
                        requestParams.put("user_cep", user.getUser_cep());
                        requestParams.put("user_address", user.getUser_address());
                        requestParams.put("user_age", user.getUser_age());
                        requestParams.put("user_childrens", user.getUser_childrens());
                        requestParams.put("user_income", user.getUser_income());
                        requestParams.put("user_scholarity", user.getUser_scholarity());

                        // Manda um POST para o sistema armazenar mais uma usuária
                        EvPost.post("api/user/manager", requestParams, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // If the response is JSONObject instead of expected JSONArray
                                Log.d("Philippe", "JSON OBJECT!!");
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Log.d("Philippe", "Deu Erro: " + statusCode + " | " + responseString);
                                progressDialog.dismiss();
                            }
                        });

                        Toast toast = Toast.makeText(view.getContext(), "Informações salvas com sucesso!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                    }catch (Exception e){
                        Toast toast = Toast.makeText(view.getContext(), "Erro: " + e.getMessage(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }
            }
        });

        return this.view;
    }

    private User getValues(){

        User user = Singleton.getInstance().getUser();

        if(!user_name.getText().toString().equals("")){
            user.setUser_name(user_name.getText().toString());
        }

        if(!user_email.getText().toString().equals("")){
            user.setUser_email(user_email.getText().toString());
        }

        if(!user_cep.getText().toString().equals("")){
            user.setUser_cep(user_cep.getText().toString());
        }

        if(!user_address.getText().toString().equals("")){
            user.setUser_address(user_address.getText().toString());
        }

        if(!user_age.getText().toString().equals("")){
            user.setUser_age(Integer.valueOf(user_age.getText().toString()));
        }

        if(!user_childrens.getText().toString().equals("")){
            user.setUser_childrens(Integer.valueOf(user_childrens.getText().toString()));
        }

        user.setUser_income(user_income.getSelectedItemPosition());
        user.setUser_scholarity(user_scholarity.getSelectedItemPosition());
        user.setUser_accept_terms((user_accept_terms.isChecked()) ? 1 : 0);

        return user;
    }

    private void setValues(){

        User user = Singleton.getInstance().getUser();

        user_name.setText((user.getUser_name() != null)? user.getUser_name(): "");
        user_email.setText((user.getUser_email() != null)? user.getUser_email(): "");
        user_cep.setText((user.getUser_cep() != null)? user.getUser_cep(): "");
        user_address.setText(String.valueOf((user.getUser_address() != null) ? user.getUser_address() : ""));
        user_age.setText(String.valueOf((user.getUser_age() != null && user.getUser_age() != 0)? user.getUser_age(): ""));
        user_childrens.setText(String.valueOf((user.getUser_childrens() != null && user.getUser_childrens() != 0)? user.getUser_childrens(): ""));
        user_income.setSelection((user.getUser_income() != null)? user.getUser_income() : 0);
        user_scholarity.setSelection((user.getUser_scholarity() != null) ? user.getUser_scholarity() : 0);

        user_accept_terms.setChecked(user.getUser_accept_terms() == 1);
    }

    private void setFields(){

        user_name = (EditText) view.findViewById(R.id.field_name);
        user_email = (EditText) view.findViewById(R.id.field_email);
        user_cep = (EditText) view.findViewById(R.id.field_cep);
        user_address = (EditText) view.findViewById(R.id.field_address);
        user_age = (EditText) view.findViewById(R.id.field_age);
        user_childrens = (EditText) view.findViewById(R.id.field_childrens);
        user_income = (Spinner) view.findViewById(R.id.field_income);
        user_scholarity = (Spinner) view.findViewById(R.id.field_scholarity);
        user_accept_terms = (CheckBox) view.findViewById(R.id.checkbox_accept_terms);

    }
}
