package br.com.estadovirtual.promulher.tables;

/**
 * Created by Alex on 27/11/2014.
 */
public class HelpContacts {

    private String contact_name;
    private String contact_number;
    private int contact_send_help;

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public int getContact_send_help() {
        return contact_send_help;
    }

    public void setContact_send_help(int contact_send_help) {
        this.contact_send_help = contact_send_help;
    }
}
