import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class WeatherAppGUI extends JFrame {
    public WeatherAppGUI(){
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents(){
        JTextField searchField = new JTextField();
        searchField.setBounds(15,15,351,45);
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);

        JButton searchBtn = new JButton(loadImage("src/assets/search.png"));
        searchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBtn.setBounds(375, 13, 47, 45);
        add(searchBtn);

        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(8,125,450,217);
        add(weatherConditionImage);

        //Temperature Display
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,350,450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // Weather Description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.BOLD, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // Humidity Image
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);

        //Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500,85,55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        //Windspeed image
        JLabel windSpeedImg = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImg.setBounds(220, 500, 74, 66);
        add(windSpeedImg);

        //WindSpeed text
        JLabel windSpeedTxt = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windSpeedTxt.setBounds(310, 500, 85, 55);
        windSpeedTxt.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedTxt);
    }

    private ImageIcon loadImage(String resourcePath) {
        try{
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e){
                e.printStackTrace();
        }
        System.out.println("Could not find resources");
        return null;
    }
}
