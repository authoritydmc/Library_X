package settings;

import alert.AlertMaker;
import database.DatabaseHandler;
import org.apache.commons.codec.digest.DigestUtils;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Preferences {

    public static final String CONFIG_FILE = "config.txt";

    int nDaysWithoutFine;
    float finePerDay;
    String username;
    String password;

    public Preferences() {
        nDaysWithoutFine = 14;
        finePerDay = 2;
        username = "admin";
        setPassword("admin");
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 16) {
            this.password = DigestUtils.sha256Hex(password);
        }else
            this.password = password;
    }

    public static void initConfig() {

        try {
            Preferences preference = new Preferences();
            PreparedStatement statement=DatabaseHandler.getInstance().getConnection().prepareStatement("INSERT INTO preferences VALUES(?,?,?,?)");
            statement.setInt(1,preference.getnDaysWithoutFine());
            statement.setFloat(2,preference.getFinePerDay());
            statement.setString(3,preference.getUsername());
            statement.setString(4,preference.getPassword());
            if (!(statement.executeUpdate()>0))
            {
                AlertMaker.showSimpleAlert("Error", "Settings Can not be Initialised");
            }
        } catch (Exception e) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static Preferences getPreferences() {

        Preferences preferences = new Preferences();
        try {

            String query="SELECT * FROM preferences";
            ResultSet resultSet=DatabaseHandler.getInstance().execQuery(query);
            if (resultSet.next())
            {
                preferences.password=resultSet.getString("password");
                preferences.finePerDay=resultSet.getFloat("finePerDay");
                preferences.username=resultSet.getString("username");
                preferences.nDaysWithoutFine=resultSet.getInt("ndayswithoutfine");
            }else  ///there is no data in table Preferences
            {
                System.out.println("initilaisng preferences ");
                initConfig();
                preferences.password=resultSet.getString("password");
                preferences.finePerDay=resultSet.getFloat("finePerDay");
                preferences.username=resultSet.getString("username");
                preferences.nDaysWithoutFine=resultSet.getInt("ndayswithoutfine");
            }
        } catch (Exception e) {

            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, e);
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference) {

       String query="UPDATE preferences SET username=?,password=?,ndayswithoutfine=?,fineperday=?";

        try {
            PreparedStatement statement=DatabaseHandler.getInstance().getConnection().prepareStatement(query);
            statement.setInt(3,preference.getnDaysWithoutFine());
            statement.setFloat(4,preference.getFinePerDay());
            statement.setString(1,preference.getUsername());
            statement.setString(2,preference.getPassword());
        if (statement.executeUpdate()>0)
        {
            AlertMaker.showSimpleAlert("Success", "Settings updated");

        }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
