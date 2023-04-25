import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderPlacementTests {
    private WebDriver driver;
    private WebDriverWait wait;

    private String authUserName = "masha";
    private String authPassword = "123456";
    private String authEmail = "masha@anya.ru";
    private String discount = "sert500";
    private List<WebElement> inputFields = driver.findElements(By.xpath("//input[contains(@id, 'billing')]"));

    private By userNameField = By.cssSelector("#username"); //поле ввода имени на странице Мой аккаунт
    private By passwordField = By.cssSelector("#password"); //поле ввода пароля на странице Мой аккаунт
    private By myAccountComeInButton = By.cssSelector(".woocommerce-form-login__submit"); //кнопка Войти на странице Мой аккаунт

    private By catalogPageBasketButtonLocator = By.xpath("(//*[contains(@class, 'add_to_cart_button')])[1]"); //кнопка "В корзину" на странице каталога
    private By catalogPageDetailedButtonLocator = By.xpath("//*[contains(@class, 'added_to_cart')]"); //кнопка подробнее у товара на странице каталога
    private By placeOrderButton = By.cssSelector(".checkout-button"); //кнопка оформить заказ в корзине
    private By authLink = By.cssSelector(".showlogin"); //ссылка авторизуйтесь на странице оформления заказа

    private By firstNameField = By.cssSelector("#billing_first_name"); //поле Имя
    private By lastNameField = By.cssSelector("#billing_last_name"); //поле Имя
    private By countryField = By.cssSelector("#select2-billing_country-container"); //селект страна
    private By addressField = By.cssSelector(".address-field:nth-of-type(4) input"); //адрес
    private By cityField = By.cssSelector("#billing_city"); //город
    private By stateField = By.cssSelector("#billing_state"); //область
    private By postcodeField = By.cssSelector("#billing_postcode"); //индекс
    private By phoneField = By.cssSelector("#billing_phone"); //телефон
    private By emailField = By.cssSelector("#billing_email"); // эл. почта
    private By bankPay = By.cssSelector("input#payment_method_bacs"); //радиобаттон Прямой банковский перевод
    private By afterDeliveryPay = By.xpath("//*[@for='payment_method_cod']"); //радиобаттон Оплата после доставки
    private By placeOrderButtonFinal = By.cssSelector("button#place_order"); //кнопка Оформить заказ
    private By postTitle = By.cssSelector("h2.post-title"); //заголовок Заказ получен
    private By coupon = By.cssSelector(".showcoupon"); //ссылка для ввода купона
    private By couponField = By.cssSelector("#coupon_code"); //поле для ввода купона
    private By applyCouponButton = By.xpath("//*[@name='apply_coupon']"); //кнопка Применить купон
    private By errorMessage = By.cssSelector(".woocommerce-error"); //сообщение о неверно заполненных полях


    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        // driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    private void authBeforeOrderPlacement() {
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(placeOrderButton).click();
        driver.findElement(authLink).click();
        driver.findElement(userNameField).sendKeys(authUserName);
        driver.findElement(passwordField).sendKeys(authPassword);
        driver.findElement(myAccountComeInButton).click();
    }

    private void fillTheFormFields() {
        driver.findElement(firstNameField).sendKeys("Мария");
        driver.findElement(lastNameField).sendKeys("Лебедева");
        driver.findElement(addressField).sendKeys("Ленина, 1");
        driver.findElement(cityField).sendKeys("Екатеринбург");
        driver.findElement(stateField).sendKeys("Свердловская область");
        driver.findElement(postcodeField).sendKeys("620000");
        driver.findElement(phoneField).sendKeys("+79000000001");
        driver.findElement(emailField).sendKeys(authEmail);
    }

    private void clearTheFormFields(List<WebElement> webElements) {
        for (WebElement webElement : webElements) {
            webElement.clear();
        }

    }

    private void checkFillingRequiredFields(List<WebElement> webElements) {
        for (WebElement webElement : webElements) {
            Assertions.assertTrue(!webElement.getAttribute("value").isEmpty(),
                    "Одно из обязательных полей не заполнено");
        }
    }


    //Оформление заказа изначально неавторизованного пользователя с оплатой банковским переводом
    @Test
    public void PlaceOrderPage_PlaceOrderBankPay_SuccessfulOrderPlacement() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        fillTheFormFields();
        checkFillingRequiredFields(inputFields);
        driver.findElement(bankPay).click();
        driver.findElement(placeOrderButtonFinal).click();
        wait.until(ExpectedConditions.urlContains("http://intershop5.skillbox.ru/checkout/order-received"));

        //assert
        Assertions.assertEquals("Заказ получен", driver.findElement(postTitle).getText(),
                "Заказ не оформлен");
    }


    //Оформление заказа изначально неавторизованного пользователя с оплатой после доставки
    @Test
    public void PlaceOrderPage_PlaceOrderPayAfterDelivery_SuccessfulOrderPlacement() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        fillTheFormFields();
        checkFillingRequiredFields(inputFields);
        driver.findElement(afterDeliveryPay).click();
        driver.findElement(placeOrderButtonFinal).click();
        wait.until(ExpectedConditions.urlContains("http://intershop5.skillbox.ru/checkout/order-received"));

        //assert
        Assertions.assertEquals("Заказ получен", driver.findElement(postTitle).getText(),
                "Заказ не оформлен");
    }

    //Оформление заказа с применением купона
    @Test
    public void PlaceOrderPage_PlaceOrderWithDiscount_SuccessfulOrderPlacement() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        fillTheFormFields();
        checkFillingRequiredFields(inputFields);
        driver.findElement(bankPay).click();
        driver.findElement(coupon).click();
        driver.findElement(couponField).sendKeys(discount);
        driver.findElement(applyCouponButton).click();

        //assert
        Assertions.assertEquals("Купон успешно добавлен.",
                driver.findElement(By.cssSelector(".woocommerce-message")).getText(),
                "Нет сообщения о применении купона");

        Assertions.assertEquals("500,00₽",
                driver.findElement(By.cssSelector(".cart-discount .woocommerce-Price-amount")).getText(),
                "Скидка не равна скидке по купону");
        Assertions.assertEquals("34490,00₽",
                driver.findElement(By.xpath("(//*[@class = 'woocommerce-Price-amount amount']//bdi)[3]")).getText(),
                "Купон применился неверно");

        //act
        driver.findElement(placeOrderButtonFinal).click();
        wait.until(ExpectedConditions.urlContains("http://intershop5.skillbox.ru/checkout/order-received"));

        //assert
        Assertions.assertEquals("Заказ получен", driver.findElement(postTitle).getText(),
                "Заказ не оформлен");
    }


    //Оформление заказа изначально неавторизованного пользователя. Пользователь не заполнил форму
    @Test
    public void PlaceOrderPage_PlaceOrderWithEmptyFields_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        driver.findElement(bankPay).click();
        driver.findElement(placeOrderButtonFinal).click();

        //assert
        Assertions.assertEquals("Имя для выставления счета обязательное поле.\n" +
                        "Фамилия для выставления счета обязательное поле.\n" +
                        "Адрес для выставления счета обязательное поле.\n" +
                        "Город / Населенный пункт для выставления счета обязательное поле.\n" +
                        "Область для выставления счета обязательное поле.\n" +
                        "Почтовый индекс для выставления счета обязательное поле.\n" +
                        "неверный номер телефона.\n" +
                        "Телефон для выставления счета обязательное поле.\n" +
                        "Адрес почты для выставления счета обязательное поле.",
                driver.findElement(errorMessage).getText(),
                "Нет сообщения об ошибке одного из незаполненного обязательного поля");
    }


    //Обработка неверного формата номера телефона при оформлении заказа
    @Test
    public void PlaceOrderPage_PlaceOrderWithWrongPhone_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        fillTheFormFields();
        driver.findElement(phoneField).sendKeys("телефон");
        driver.findElement(bankPay).click();
        checkFillingRequiredFields(inputFields);
        driver.findElement(placeOrderButtonFinal).click();

        //assert
        Assertions.assertEquals("Телефон для выставления счета не валидный номер телефона.",
                driver.findElement(errorMessage).getText(),
                "Сообщение о неверном формате телефона не появилось или некорртектно");
    }

    //Обработка неверного формата email при оформлении заказа
    @Test
    public void PlaceOrderPage_PlaceOrderWithWrongEmail_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        authBeforeOrderPlacement();
        clearTheFormFields(inputFields);
        fillTheFormFields();
        driver.findElement(emailField).sendKeys("100");
        driver.findElement(bankPay).click();
        checkFillingRequiredFields(inputFields);
        driver.findElement(placeOrderButtonFinal).click();

        //assert
        Assertions.assertEquals("Invalid billing email address",
                driver.findElement(errorMessage).getText(),
                "Сообщение о неверном формате email не появилось или некорртектно");
    }


}
