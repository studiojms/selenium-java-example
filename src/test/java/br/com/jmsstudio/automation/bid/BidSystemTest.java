package br.com.jmsstudio.automation.bid;

import br.com.jmsstudio.automation.config.EnvironmentManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BidSystemTest {

    @BeforeEach
    public void setupBrowser() {
        EnvironmentManager.init();
    }

    @AfterAll
    public static void tearDown() {
        EnvironmentManager.shutDown();
    }

    @Test
    public void shouldCreateANewUser() {
        WebDriver driver = EnvironmentManager.getDriver();

        driver.get("http://localhost:8080/usuarios");
        driver.findElement(By.linkText("Novo Usuário")).click();

        final String userName = "John Armless";
        final String userEmail = "john.armless@johnarmless.com";

        final WebElement nameField = driver.findElement(By.name("usuario.nome"));
        final WebElement emailField = driver.findElement(By.name("usuario.email"));
        final WebElement saveButton = driver.findElement(By.id("btnSalvar"));

        nameField.sendKeys(userName);
        emailField.sendKeys(userEmail);
        saveButton.click();

        new WebDriverWait(driver, 5).until(ExpectedConditions.textToBe(By.xpath("//*[@id=\"content\"]/h1"), "Todos os Usuários"));

        assertTrue(driver.getPageSource().contains(userName));
        assertTrue(driver.getPageSource().contains(userEmail));
    }

    @Test
    public void shouldShowErrorMessageWhenTryingToCreateAUserWithoutName() {
        WebDriver driver = EnvironmentManager.getDriver();

        driver.get("http://localhost:8080/usuarios/new");

        final String userName = "";
        final String userEmail = "john.armless@johnarmless.com";

        final WebElement nameField = driver.findElement(By.name("usuario.nome"));
        final WebElement emailField = driver.findElement(By.name("usuario.email"));
        final WebElement saveButton = driver.findElement(By.id("btnSalvar"));

        nameField.sendKeys(userName);
        emailField.sendKeys(userEmail);
        saveButton.click();

        assertEquals("Nome obrigatorio!", driver.findElement(By.xpath("//*[@id=\"content\"]")).getText().split("\n")[0]);

    }

    @Test
    public void shouldShowErrorMessageWhenTryingToCreateAUserWithoutNameAndEmail() {
        WebDriver driver = EnvironmentManager.getDriver();

        driver.get("http://localhost:8080/usuarios/new");

        final WebElement saveButton = driver.findElement(By.id("btnSalvar"));

        saveButton.click();

        assertEquals("Nome obrigatorio!", driver.findElement(By.xpath("//*[@id=\"content\"]")).getText().split("\n")[0]);
        assertEquals("E-mail obrigatorio!", driver.findElement(By.xpath("//*[@id=\"content\"]")).getText().split("\n")[1]);

    }

}