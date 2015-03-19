package org.syed.zoomdroid;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AppTestEmulator {

		WebDriver driver;

		@BeforeClass
		public void setUp() throws MalformedURLException {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "XiaomiHM NOTE 1W");
			capabilities.setCapability(CapabilityType.VERSION, "4.2.2");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("androidPackage", "com.zoomcar");
			capabilities.setCapability("appActivity", "com.zoomcar.activity.SplashActivity");
			driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
		}

		@Test
		public void Cal() throws InterruptedException {
			Thread.sleep(5000);
		}

		@AfterClass
		public void tearDown() {
			driver.quit();
		}
}
