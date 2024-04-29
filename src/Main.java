import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppGUI().setVisible(true);


                //System.out.println(WeatherApp.getLocationData("Manila"));

                System.out.println(WeatherApp.getCurrentTime());
            }
        });
    }
}