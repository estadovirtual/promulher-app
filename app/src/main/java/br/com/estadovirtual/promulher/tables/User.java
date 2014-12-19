package br.com.estadovirtual.promulher.tables;

import org.w3c.dom.Text;

/**
 * Created by Alex on 27/11/2014.
 */
public class User {

    private String _unique_id;
    private String user_name;
    private String user_email;
    private String user_cep;
    private String user_address;
    private String mac_address;
    private Integer user_age;
    private Integer user_childrens;
    private Integer user_income;
    private Integer user_scholarity;
    private Integer user_accept_terms;

    public String get_unique_id() {
        return _unique_id;
    }

    public void set_unique_id(String _unique_id) {
        this._unique_id = _unique_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_cep() {
        return user_cep;
    }

    public void setUser_cep(String user_cep) {
        this.user_cep = user_cep;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public Integer getUser_age() {
        return user_age;
    }

    public void setUser_age(Integer user_age) {
        this.user_age = user_age;
    }

    public Integer getUser_childrens() {
        return user_childrens;
    }

    public void setUser_childrens(Integer user_childrens) {
        this.user_childrens = user_childrens;
    }

    public Integer getUser_income() {
        return user_income;
    }

    public void setUser_income(Integer user_income) {
        this.user_income = user_income;
    }

    public Integer getUser_scholarity() {
        return user_scholarity;
    }

    public void setUser_scholarity(Integer user_scholarity) {
        this.user_scholarity = user_scholarity;
    }

    public Integer getUser_accept_terms() {
        return user_accept_terms;
    }

    public void setUser_accept_terms(Integer user_accept_terms) {
        this.user_accept_terms = user_accept_terms;
    }
}
