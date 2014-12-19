package br.com.estadovirtual.evponto.classes;

import org.json.JSONObject;

/**
 * Created by Alex on 23/10/2014.
 */
public class Singleton {

    private static Singleton _instance = null;

    private String _loginURL = "http://phil.estadovirtual.net/api/login/index/";
    private String _pointManagerURL = "http://phil.estadovirtual.net/api/checkManager/index/";
    private Long _lastPointTime;
    private Integer _lastPointType;

    private JSONObject _userInfo;

    private Singleton() {
    }

    public static Singleton getInstance() {

        if (_instance == null) {
            _instance = new Singleton();
        }

        return _instance;
    }

    public String get_pointManagerURL() { return _pointManagerURL; }

    public String get_loginURL() {
        return _loginURL;
    }

    public JSONObject get_userInfo() {
        return _userInfo;
    }

    public void set_userInfo(JSONObject _userInfo) {
        this._userInfo = _userInfo;
    }

    public Long get_lastPointTime() { return _lastPointTime; }

    public void set_lastPointTime(Long _lastPointTime) { this._lastPointTime = _lastPointTime; }

    public Integer get_lastPointType() { return _lastPointType; }

    public void set_lastPointType(Integer _lastPointType) { this._lastPointType = _lastPointType; }
}
