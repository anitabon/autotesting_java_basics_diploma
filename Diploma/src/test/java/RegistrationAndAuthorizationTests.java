import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationAndAuthorizationTests {

    //

    private WebDriver driver;
    private WebDriverWait wait;

    private String authUserName = "masha";
    private String authPassword = "123456";

    private String regUserName = "irina897";
    private String regEmail = "oleg@anya.ru";
    private String regPassword = "123456";

    private By comeInButton = By.cssSelector(".account");  //кнопка Войти на главной странице
    private By userNameField = By.cssSelector("#username"); //поле ввода имени на странице Мой аккаунт
    private By passwordField = By.cssSelector("#password"); //поле ввода пароля на странице Мой аккаунт
    private By myAccountComeInButton = By.cssSelector(".woocommerce-form-login__submit"); //кнопка Войти на странице Мой аккаунт
    private By rememberMeCheckbox = By.cssSelector("#rememberme"); //чекбокс запомни меня на странице Мой аккаунт
    private By myAccountRegisterButton = By.cssSelector(".custom-register-button");  //кнопка Зарегистрироваться на странице Мой аккаунт
    private By lostPasswordLink = By.cssSelector(".lost_password a"); //ссылка Забыли пароль? на странице Мой аккаунт
    private By userLoginField = By.xpath("//*[@id='user_login']"); //поле ввода имени пользователя или почты для восстановления пароля
    private By resetPasswordButton = By.cssSelector("button.woocommerce-Button"); //кнопка Сбросить пароль
    private By resetMessage = By.cssSelector(".woocommerce-message"); //сообщение о сбросе пароля

    private By regUserNameField = By.cssSelector("#reg_username"); //поле ввода имени на странице Регистрация
    private By regEmailField = By.cssSelector("#reg_email"); //поле ввода email на странице Регистрация
    private By regPasswordField = By.cssSelector("#reg_password"); //поле ввода пароля на странице Регистрация
    private By registerButton = By.cssSelector(".woocommerce-form-register__submit"); //кнопка Зарегистрироваться на странице Регистрация

    private By endOfRegistration = By.cssSelector(".content-page div"); //регистрация завершена (текст)

    private By regLinkFooter = By.xpath("(//div[@class='top-footer-block']//li)[last()]//a");  //ссылка на регистрацию в футере
    private By authLinkFooter = By.xpath("(//div[@class='top-footer-block']//li)[4]//a");  //ссылка на авторизацию в футере

    private By catalogPageBasketButtonLocator = By.xpath("(//*[contains(@class, 'add_to_cart_button')])[1]"); //кнопка "В корзину" на странице каталога
    private By catalogPageDetailedButtonLocator = By.xpath("//*[contains(@class, 'added_to_cart')]"); //кнопка подробнее у товара на странице каталога
    private By placeOrderButton = By.cssSelector(".checkout-button"); //кнопка оформить заказ в корзине
    private By authLink = By.cssSelector(".showlogin"); //ссылка авторизуйтесь на странице оформления заказа


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


    //регистрация с главной страницы
    @Test
    public void MyAccountPage_Registration_SuccessfulRegistration() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");

        //act
        driver.findElement(comeInButton).click();
        driver.findElement(myAccountRegisterButton).click();
        driver.findElement(regUserNameField).sendKeys(regUserName);
        driver.findElement(regEmailField).sendKeys(regEmail);
        driver.findElement(regPasswordField).sendKeys(regPassword);
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertTrue(driver.getCurrentUrl().equals("http://intershop5.skillbox.ru/register/"),
                "Не произошел переход на страницу завершения регистрации");
        Assertions.assertEquals("Регистрация завершена",
                driver.findElement(endOfRegistration).getText(),
                "Регистрация не завершилась");
    }

    //регистрация по ссылке в футере
    @Test
    public void MainPage_RegistrationByFooter_SuccessfulRegistration() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");

        //act
        driver.findElement(regLinkFooter).click();
        driver.findElement(regUserNameField).sendKeys(regUserName);
        driver.findElement(regEmailField).sendKeys(regEmail);
        driver.findElement(regPasswordField).sendKeys(regPassword);
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertTrue(driver.getCurrentUrl().equals("http://intershop5.skillbox.ru/register/"),
                "Не произошел переход на страницу завершения регистрации");
        Assertions.assertEquals("Регистрация завершена",
                driver.findElement(endOfRegistration).getText(),
                "Регистрация не завершилась");
    }

    //обработка имени пользователя неверного формата при регистрации
    @Test
    public void RegPage_EnterWrongUserName_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/register/");

        //act
        driver.findElement(regUserNameField).sendKeys("+-=");
        driver.findElement(regEmailField).sendKeys("java@javabox.ru");
        driver.findElement(regPasswordField).sendKeys("123456");
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertEquals("Error: Пожалуйста введите корректное имя пользователя.",
                driver.findElement(By.cssSelector(".woocommerce-error li")).getText(),
                "Отсутствует сообщение о неверном формате имени пользователя");
    }

    //обработка email неверного формата при регистрации
    @Test
    public void RegPage_EnterWrongEmail_GetFocusOnEmailField() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/register/");

        //act
        driver.findElement(regUserNameField).sendKeys("papacarlo");
        driver.findElement(regEmailField).sendKeys("123456");
        driver.findElement(regPasswordField).sendKeys("123456");
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertTrue(driver.findElement(regEmailField).equals(driver.switchTo().activeElement()),
                "Курсор не в поле email");
    }

    //обработка ввода существующего email при регистрации
    @Test
    public void RegPage_EnterExistingEmail_GetWrongMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/register/");

        //act
        driver.findElement(regUserNameField).sendKeys(regUserName);
        driver.findElement(regEmailField).sendKeys(regEmail);
        driver.findElement(regPasswordField).sendKeys(regPassword);
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertEquals("Error: Учетная запись с такой почтой уже зарегистировавана. Пожалуйста авторизуйтесь.",
                driver.findElement(By.cssSelector(".woocommerce-error li")).getText(),
                "Нет сообщения о том, что такой адрес уже зарегистрирован");
    }


    //обработка ввода существующего имени пользователя при регистрации
    @Test
    public void RegPage_EnterExistingUserName_GetWrongMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/register/");

        //act
        driver.findElement(regUserNameField).sendKeys(regUserName);
        driver.findElement(regEmailField).sendKeys("volk@java.ru");
        driver.findElement(regPasswordField).sendKeys(regPassword);
        driver.findElement(registerButton).click();

        //assert
        Assertions.assertEquals("Error: Учетная запись с таким именем пользователя уже зарегистрирована.",
                driver.findElement(By.cssSelector(".woocommerce-error li")).getText(),
                "Нет сообщения о том, что пользователь с таким именем уже зарегистрирован");
    }


    //авторизация с главной страницы
    @Test
    public void MyAccountPage_Authorization_SuccessfulAuthorization() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");

        //act
        driver.findElement(comeInButton).click();
        driver.findElement(userNameField).sendKeys(authUserName);
        driver.findElement(passwordField).sendKeys(authPassword);
        driver.findElement(myAccountComeInButton).click();

        //assert
        Assertions.assertTrue(driver.getCurrentUrl().equals("http://intershop5.skillbox.ru/my-account/"),
                "Не произошел переход на страницу Мой аккаунт. Возможно, введены неверные данные");
        Assertions.assertEquals("Привет " + authUserName + " (Выйти)",
                driver.findElement(By.cssSelector(".woocommerce-MyAccount-content p:first-of-type")).getText(),
                "Указанный пользователь не авторизован или авторизован не тот пользователь");
    }

    //авторизация по ссылке в футере
    @Test
    public void MainPage_AuthorizationByFooter_SuccessfulAuthorization() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");

        //act
        driver.findElement(authLinkFooter).click();
        driver.findElement(userNameField).sendKeys(authUserName);
        driver.findElement(passwordField).sendKeys(authPassword);
        driver.findElement(myAccountComeInButton).click();

        //assert
        Assertions.assertTrue(driver.getCurrentUrl().equals("http://intershop5.skillbox.ru/my-account/"),
                "Не произошел переход на страницу Мой аккаунт. Возможно, введены неверные данные");
        Assertions.assertEquals("Привет " + authUserName + " (Выйти)",
                driver.findElement(By.cssSelector(".woocommerce-MyAccount-content p:first-of-type")).getText(),
                "Указанный пользователь не авторизован или авторизован не тот пользователь");
    }

    //авторизация через оформление заказа
    @Test
    public void OrderPlacementPage_AuthorizationByOrderPlacement_SuccessfulAuthorization() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(placeOrderButton).click();
        driver.findElement(authLink).click();
        driver.findElement(userNameField).sendKeys(authUserName);
        driver.findElement(passwordField).sendKeys(authPassword);
        driver.findElement(myAccountComeInButton).click();

        //assert
        Assertions.assertEquals("Выйти", driver.findElement(By.cssSelector(".logout")).getText(),
                "Не произошла авторизация");
    }


    //обработка неверного имени пользователя при авторизации
    @Test
    public void AuthPage_EnterWrongUserName_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/my-account/");

        //act
        driver.findElement(userNameField).sendKeys("masha123456");
        driver.findElement(passwordField).sendKeys(authPassword);
        driver.findElement(myAccountComeInButton).click();

        //assert
        Assertions.assertEquals("Неизвестное имя пользователя. Попробуйте еще раз или укажите адрес почты.",
                driver.findElement(By.cssSelector(".woocommerce-error li")).getText(),
                "Отсутствует сообщение о неверно введенном имени пользователя");
    }

    //обработка неверного пароля при авторизации
    @Test
    public void AuthPage_EnterWrongPassword_GetErrorMessage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/my-account/");

        //act
        driver.findElement(userNameField).sendKeys(authUserName);
        driver.findElement(passwordField).sendKeys("1234567");
        driver.findElement(myAccountComeInButton).click();

        //assert
        Assertions.assertEquals("Веденный пароль для пользователя " +  authUserName + " неверный. Забыли пароль?",
                driver.findElement(By.cssSelector(".woocommerce-error li")).getText(),
                "Отсутствует сообщение о неверно введенном пароле для пользователя");
    }


    //забыли пароль
    @Test
    public void AuthPage_ForgotPassword_ResetPassword() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/my-account/");

        //act
        driver.findElement(lostPasswordLink).click();
        driver.findElement(userLoginField).sendKeys(authUserName);
        driver.findElement(resetPasswordButton).click();

        //assert
        Assertions.assertEquals("Password reset email has been sent.",
                driver.findElement(resetMessage).getText(),
                "Отсутствует сообщение о сбросе пароля");
    }
}
