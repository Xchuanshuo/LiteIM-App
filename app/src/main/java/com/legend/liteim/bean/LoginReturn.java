package com.legend.liteim.bean;

/**
 * @author Legend
 * @data by on 19-1-18.
 * @description
 */
public class LoginReturn {


    /**
     * jwt : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2IiwiZXhwIjoxNTQ4NDE0OTk0fQ.ZQAdB_vHDBPQnzQUj6cm5BuuQHW3Fwlzop3G3Yi5DN0
     * user : {"id":6,"username":"legend","password":"202CB962AC59075B964B07152D234B70","signature":"string","portrait":"/6/portraits/3e3455ec332f4a22af0b5f93218c1eac.jpg","background":"/6/backgrounds/68838a11fe68497aa4961ad8757cd27a.png","sex":0,"email":"string","stuNum":"string","openId":"string","fansNum":0}
     */

    private String jwt;
    private User user;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
