/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package car.station;

/**
 *
 * @author jimman2003
 */
public class UserIDSingleton {

    private int user_id;

    private UserIDSingleton() {
    }

    public static UserIDSingleton getInstance() {
        return UserIDSingletonHolder.INSTANCE;
    }

    public void setUser(int u) {
        this.user_id = u;
    }

    public int getUser() {
        return this.user_id;
    }

    private static class UserIDSingletonHolder {

        private static final UserIDSingleton INSTANCE = new UserIDSingleton();
    }
}
