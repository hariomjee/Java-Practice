package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;

public class URLExtractor {
    public static void main(String[] args) {
        // Set the path to the WebDriver executable for Chrome
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to the URL you provided
            String url = "https://example.com";  // Replace with your URL
            driver.get(url);

            // Find the element containing the embed URL
            WebElement embedElement = driver.findElement(By.id("embedUrl"));  // Replace with the actual element locator

            // Extract the URL from the element
            String embedUrl = embedElement.getAttribute("src");

            System.out.println("Extracted URL: " + embedUrl);
        } finally {
            // Close the driver
            driver.quit();
        }
    }
}
