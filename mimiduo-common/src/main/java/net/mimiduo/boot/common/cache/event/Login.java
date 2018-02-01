package net.mimiduo.boot.common.cache.event;

import java.util.Date;


public final class Login implements EventData {

    public static final String TOPIC = "web.login.topic";

    private final Date date;
    private final String login;

    public Login(String login, Date date) {
        this.login = login;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getLogin() {
        return login;
    }
}
