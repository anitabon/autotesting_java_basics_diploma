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

public class BasketTests {

    private WebDriver driver;
    private WebDriverWait wait;

    private By basketPageCurrentPageLocator = By.cssSelector(".current");  //Главная - Корзина
    private By basketPageEmptyCartText = By.cssSelector(".cart-empty");  //Текст "Корзина пуста"
    private By basketPageBackToShopButton = By.cssSelector(".wc-backward");  //Назад в магазин
    private By catalogPageGoodNameLocator = By.xpath("(//*[contains(@class, 'instock')])[1]//h3"); //имя товара в каталоге
    private By catalogPageBasketButtonLocator = By.xpath("(//*[contains(@class, 'add_to_cart_button')])[1]"); //кнопка "В корзину"
    private By catalogPageDetailedButtonLocator = By.xpath("//*[contains(@class, 'added_to_cart')]"); //кнопка подробнее
    private By catalogPageGoodNameBasketLocator = By.cssSelector(".product-name a"); //товар, который мы добавили в корзину
    private By catalogPageGoodPrice = By.xpath("(//*[contains(@class, 'woocommerce-Price-amount')])[4]/bdi"); //цена товара в каталоге
   // private By basketPageGoodCount = By.xpath("//*[@type='number']"); //количество товара в корзине
    private By basketPageGoodPrice = By.xpath("//*[contains(@class, 'product-price')]//bdi"); //цена в корзине
  //  private By basketPageGoodSubtotal = By.xpath("//*[contains(@class, 'product-subtotal')]//bdi"); //общая стоимость в корзине
    private By basketPageCartTotal = By.xpath("//*[contains(@class, 'cart_totals')]//bdi"); //к оплате
    private By basketPageCouponInput = By.cssSelector("#coupon_code"); //поле ввода купона
    private By basketPageCouponButton = By.cssSelector(".coupon .button"); //кнопка Применить купон
    private By basketPageCouponDiscount = By.cssSelector(".cart-discount .woocommerce-Price-amount"); //скидка, кот. применилась по купону
    private By basketPageCouponCartTotal = By.xpath("(//*[@class = 'woocommerce-Price-amount amount']//bdi)[3]"); //к оплате после применения купона
    private By basketPageRemoveCoupon = By.cssSelector(".woocommerce-remove-coupon"); //удалить купон
    private By basketPagePlaceOrderButton = By.cssSelector(".checkout-button"); //кнопка Оформить заказ
    private By basketPageCouponErrorAlert = By.cssSelector(".woocommerce-error li"); //Неверный купон.
    private By basketPageRemoveButton = By.cssSelector(".remove");  //крестик удаления товара из корзины
    private By basketPageGoodGoBack = By.cssSelector(".restore-item"); //Вернуть?
    private By basketPageCartGoodsTable = By.cssSelector(".shop_table.cart"); //таблиа товаров в корзине


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


    //мы находимся на странице корзины

    @Test
    public void basketPage_hasBasketTitle_titleIsDisplayed() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/cart/");

        //assert
        Assertions.assertEquals("Корзина", driver.findElement(basketPageCurrentPageLocator).getText(), "Неверное " +
                "название в заголовке страницы");
        Assertions.assertTrue(driver.findElement(basketPageCurrentPageLocator).isDisplayed(),
                "Находимся не в Корзине");
    }


    //если в корзине нет товаров, то она пуста и можно перейти в каталог
    @Test
    public void basketPage_noGoodsInBasket_basketIsEmptyAndGoBackToShop () {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/cart/");

        //assert
        Assertions.assertEquals("Корзина пуста.", driver.findElement(basketPageEmptyCartText).getText(),
                "Нет уведомления о том, что корзина пуста при отсутствии товаров в ней");

        //act
        driver.findElement(basketPageBackToShopButton).click();

        //assert
        Assertions.assertEquals("http://intershop5.skillbox.ru/shop/", driver.getCurrentUrl());
    }


    //в корзину добавился верный товар (например,из каталога), инфо о товаре верная, цена верная
    //и так как это акционный товар, то все проверки с купонами и без как бы косвенно проверяют, что цена
    //акционного товара после применения и отмены купона не возвращается к доакционной
    @Test
    public void basketPage_addGoodToBasket_rightGoodWithRightPriceInBasket() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        String goodName = driver.findElement(catalogPageGoodNameLocator).getText();
        String goodPrice = driver.findElement(catalogPageGoodPrice).getText();
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();


        //assert

        Assertions.assertTrue(driver.findElement(basketPageCurrentPageLocator).isDisplayed(),
                "Не произошел переход в корзину");
        Assertions.assertEquals(goodName,
                driver.findElement(catalogPageGoodNameBasketLocator).getText(), "Не тот товар добавился в корзину");
        Assertions.assertEquals(goodPrice, driver.findElement(basketPageGoodPrice).getText(), "Цена в каталоге не " +
                "соответствует цене в корзине");
    }


    //Применение купона

    @Test
    public void basketPage_addCoupon_correctCouponApplying() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(basketPageCouponInput).sendKeys("sert500");
        driver.findElement(basketPageCouponButton).click();
        driver.findElement(basketPageRemoveCoupon).click();

        //assert
        Assertions.assertEquals("500,00₽", driver.findElement(basketPageCouponDiscount).getText(),
                "Скидка не равна скидке по купону");
        Assertions.assertEquals("34490,00₽", driver.findElement(basketPageCouponCartTotal).getText(),
                "Купон применился неверно");
    }

    //Применение неверного купона

    @Test
    public void basketPage_addWrongCoupon_couponNotApplying() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(basketPageCouponInput).sendKeys("sert600");
        driver.findElement(basketPageCouponButton).click();

        //assert
        Assertions.assertEquals("Неверный купон.", driver.findElement(basketPageCouponErrorAlert).getText(),
                "Применен случайный неверный купон");
    }



    //Удаление купона
    @Test
    public void basketPage_addCoupon_correctCouponDeleting() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(basketPageCouponInput).sendKeys("sert500");
        driver.findElement(basketPageCouponButton).click();

        //assert
        Assertions.assertEquals("34990,00₽", driver.findElement(basketPageCartTotal).getText(),
                "Купон не удалился");
    }

    //Переход на страницу оформления заказа
    @Test
    public void basketPage_goPlaceOrder_correctRedirectingToPlacingOrder() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(basketPagePlaceOrderButton).click();

        //assert
        Assertions.assertEquals("Оформление Заказа", driver.findElement(basketPageCurrentPageLocator).getText(),
                "Не произошел переход на страницу оформления заказа");
    }

    //Удаление товара из корзины и его возврат
    @Test
    public void basketPage_deleteGood_correctGoodDeleting() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //act
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();
        driver.findElement(basketPageRemoveButton).click();

        //assert
        Assertions.assertTrue(driver.findElement(basketPageEmptyCartText).isDisplayed(), "Товар не удалился из корзины");


        //act
        driver.findElement(basketPageGoodGoBack).click();

        //assert
        Assertions.assertTrue(driver.findElement(basketPageCartGoodsTable).isDisplayed(), "Товар не вернулся в корзину");

    }
}
