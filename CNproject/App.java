import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        
        Properties prop = new Properties();
        OutputStream output = null;
        
        try {
            
            output = new FileOutputStream("C:/mywork/config.properties");
            
            // set the properties value
            prop.setProperty("0", "9000");
            prop.setProperty("1", "900125");
            prop.setProperty("2", "900231");
            prop.setProperty("3", "900342");
            prop.setProperty("4", "900453");
            prop.setProperty("5", "900514");
            
            // save properties to project root folder
            prop.store(output, null);
            
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
        }
    }
}
