package org.syed.zoomdroid;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AppTest {
	AndroidDriver driver;
	Process Proc = null;
	Boolean parent = false;

	public void startAppiumServer() throws IOException, InterruptedException {

		String[] command = { "cmd.exe", "/c",
				"node E:\\Software\\Appium\\node_modules\\appium" };
		ProcessBuilder probuilder = new ProcessBuilder(command);
		probuilder.redirectOutput(new File("./AppiumLog.log"));
		Proc = probuilder.start();

		// System.out.println("-------------------------------------------------------");

		System.out
				.println("-----------------Appium server started-----------------");
		Thread.sleep(5000);
	}

	@BeforeTest
	public void setUp() throws IOException, InterruptedException {
		try {
			startAppiumServer();
			System.out
					.println("----------------------Test Started---------------------");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("deviceName", "XiomiHM Note");
			capabilities.setCapability(CapabilityType.VERSION, "4.4.2");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("androidPackage", "com.zoomcar");
			capabilities.setCapability("appActivity",
					"com.zoomcar.activity.SplashActivity");
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),
					capabilities);
//			driver.manage().timeouts().implicitlyWait(30L, TimeUnit.SECONDS);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Process destroyed");
			Proc.destroy();

		}
		// System.out.println("...............Test service initialized...............");
	}

	public String[] lessThan24() {
		// driver.startActivity("com.zoomcar",
		// "com.zoomcar.activity.","com.zoomcar",driver.currentActivity());
		String eDateTime, sDateTime;
		// System.out.println("-------------------------------------------------------");

		System.out
				.println("---------------Less than 24hrs date picker------------- ");
		SimpleDateFormat sdf = new SimpleDateFormat("MMddhhmma");
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar
				.getInstance();
		int min = cal.get(GregorianCalendar.MINUTE);
		int curMin = min;
		Date date = cal.getTime();
		String curDate = sdf.format(date);
		System.out.println("current time\t:\t" + cal.getTime());
		if (min < 15)
			min = 15;
		else if (min < 30)
			min = 30;
		else if (min < 45)
			min = 45;
		else
			min = 60;
		min = min - curMin;
		cal.add(GregorianCalendar.MINUTE, min);
		date = cal.getTime();
		sDateTime = sdf.format(date);
		System.out.println("start time \t:\t" + date);
		Random x;
		do {
			x = new Random();
			cal.add(GregorianCalendar.DATE, x.nextInt(30));

			// cal.add(GregorianCalendar.DATE, 13);

			date = cal.getTime();
			System.out.println("end time \t:\t" + date);
			eDateTime = sdf.format(date);
		} while (sDateTime.equals(eDateTime));
		String dates[] = { curDate, sDateTime, eDateTime };
		return dates;
	}

	@Test(priority = 1, enabled = true)
	public void within24Booking() throws InterruptedException {
		try {
			swipeLR();
			driver.findElement(By.id("com.zoomcar:id/textViewSearch")).click();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String dates[] = lessThan24();
		String curDate = dates[0], sDateTime = dates[1], eDateTime = dates[2];
		boolean flag = false;
		int curMonth = Integer.parseInt(curDate.substring(0, 2));
		String sMonth = sDateTime.substring(0, 2);
		int smon = Integer.parseInt(sMonth);
		if (smon > curMonth) {
			System.out.println("selecting month");
			for (int i = 0; i < (smon - curMonth); i++)
				driver.findElement(By.id("com.zoomcar:id/calendar_right_arrow"))
						.click();
		}
		String sDay = String
				.valueOf(Integer.parseInt(sDateTime.substring(2, 4)));
		if (Integer.parseInt(sDay) > 24)
			flag = true;
		else
			flag = false;
		String sHour = sDateTime.substring(4, 6);
		String sMin = sDateTime.substring(6, 8);
		String ampm = sDateTime.substring(8, 10).toUpperCase();
		listElements("com.zoomcar:id/calendar_tv", sDay, flag).click();
		// driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
		driver.findElement(By.name(sHour)).click();
		driver.findElement(By.id("com.zoomcar:id/textView" + ampm)).click();
		driver.findElement(By.id("com.zoomcar:id/textView" + sMin)).click();
		// ---------------------------------------------------------------------
		String eMonth = eDateTime.substring(0, 2);
		int emon = Integer.parseInt(eMonth);
		curMonth = Integer.parseInt(sMonth);
		System.out.println("curMonth: " + curMonth);
		System.out.println("emon : " + emon);
		if (emon > curMonth) {
			System.out.println("selecting month");
			for (int i = 0; i < (emon - curMonth); i++)
				driver.findElement(By.id("com.zoomcar:id/calendar_right_arrow"))
						.click();
		}
		String eDay = String
				.valueOf(Integer.parseInt(eDateTime.substring(2, 4)));
		if (Integer.parseInt(eDay) > 24)
			flag = true;
		else
			flag = false;
		String eHour = eDateTime.substring(4, 6);
		String eMin = eDateTime.substring(6, 8);
		ampm = eDateTime.substring(8, 10).toUpperCase();
		listElements("com.zoomcar:id/calendar_tv", eDay, false).click();
		// driver.manage().timeouts().implicitlyWait(60L, TimeUnit.SECONDS);
		driver.findElement(By.name(eHour)).click();
		driver.findElement(By.id("com.zoomcar:id/textView" + ampm)).click();
		driver.findElement(By.id("com.zoomcar:id/textView" + eMin)).click();
	}

	public void clickonHome() throws InterruptedException {
		WebElement up = null;
		try {
			up = driver.findElement(By.id("android:id/up"));
		} catch (Exception e) {
		}
		try {
			up = driver.findElement(By.id("android:id/home"));
		} catch (Exception e) {
		}
		if (up != null) {
			up.click();
		} else
			swipeLR();

	}

	@Test(enabled = true)
	public void locationName() throws Exception {

		WebElement LocMenu;
		String cities[] = { "Bangalore", "Pune", "Delhi NCR" };
		String locText = null;
		clickonHome();
		LocMenu = driver.findElement(By
				.id("com.zoomcar:id/textViewCityLocation"));
		for (int i = 0; i < cities.length; i++) {
			LocMenu.click();
			listElements("com.zoomcar:id/textViewLocationName", cities[i],false).click();
			swipeLR();
			Thread.sleep(2000);
			locText = LocMenu.getText();
			Assert.assertEquals(locText, cities[i]);
			// continue;
		}
		clickonHome();
	}

	public void swipeLR() throws InterruptedException {
		Thread.sleep(1000);
		Dimension size = driver.manage().window().getSize();
		int endx = (int) (size.width * 0.8);
		int startx = (int) (size.width * 0.05);
		int starty = size.height / 2;
		driver.swipe(startx, starty, endx, starty, 200);
		System.out.println("swipeRight");
		Thread.sleep(1000);
	}

	public WebElement listElements(String parentElement, String childElement,
			boolean lastElement) {
		List<WebElement> weList = null;
		WebElement el = null;
		weList = driver.findElements(By.id(parentElement));
		for (WebElement we : weList) {
			// System.out.println("listing elements"+we.getText());
			if (we.getText().toLowerCase().equals(childElement.toLowerCase())) {
				el = we;
				if (lastElement == false)
					break;
			}
		}
		System.out.println("clicking on :" + el.getText());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return(el);
	}

	// @Test()
	public void screenshot() throws InterruptedException, IOException {
		driver.findElement(By.id("android:id/home")).click();
		File srcFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File("./screenshots/fname.png"));
		for (int i = 0; i < 2; i++)
			driver.findElement(By.id("android:id/home")).click();

	}

	@DataProvider
	public Object[][] getData() {
		Object[][] data = { { "Tariff", "Tariff Details" },
				{ "Tariff", "Offers" }, { "Tariff", "Upcoming Peak Season" } ,
				{"How It Works","How to Zoom"},{"How It Works","Zoom in Safety"}, 
				{"How It Works", "Going Outstation"}, {"How It Works","FAQs"},
				{"Policies","Fee Policy"},{"Policies","Eligibility"}, {"Policies","Privacy Policy"}
				};
		return data;
	}

	@Test(dataProvider = "getData", priority = 0)
	public void leftMenu(String parentElementValue, String childElementValue)
			throws InterruptedException {
		WebElement webEl;
		Thread.sleep(5000);
		clickonHome();
		swipeLR();
		try {
			WebDriverWait wait = new WebDriverWait(driver, 5L);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.zoomcar:id/textViewMenuChild")));
			webEl=listElements("com.zoomcar:id/textViewMenuChild", childElementValue,false);
			Assert.assertEquals(childElementValue,webEl.getText());
			webEl.click();
		} catch (Exception e) {
			webEl=listElements("com.zoomcar:id/textViewMenuParent",parentElementValue, false);
			Assert.assertEquals(parentElementValue,webEl.getText());
			webEl.click();
			webEl=listElements("com.zoomcar:id/textViewMenuChild", childElementValue,false);
			Assert.assertEquals(childElementValue,webEl.getText());
			webEl.click();
		}
	
		
	}

	@AfterClass
	public void tearDown() throws InterruptedException {
		if (driver != null) {
			driver.quit();
			// System.out.println("-------------------------------------------------------");
			System.out
					.println("---------------------End of test-----------------------");
		}
		if (Proc != null) {
			Proc.destroy();
			System.out
					.println("--------------Appium server destroyed------------------");
		}
		// Thread.sleep(10000);
	}
}