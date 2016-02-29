package testSuite;

import java.io.IOException;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import com.galenframework.testng.GalenTestNgTestBase;

public class WelcomePageTest extends GalenTestNgTestBase {

    @Override
    public WebDriver createDriver(Object[] args) {
        return new FirefoxDriver();
    }

    @Test
    public void welcomePage_shouldLookGood_onDesktopDevice() {
        load("http://mail.synechron.com", 614,480);
        try {
			checkLayout("/specs/homepage.gspec", Arrays.asList("desktop"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}