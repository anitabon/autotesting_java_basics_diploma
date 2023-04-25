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

public class MainPageTests {

    private WebDriver driver;
    private WebDriverWait wait;


    private By mainPageLogoLocator = By.cssSelector(".site-title");
    private By mainPageContactsLocator = By.cssSelector(".header-callto");
    private By mainPageAccountLocator = By.cssSelector(".account");

    private By mainPageLocator = By.xpath("//*[contains(text(), 'Главная')]");
    private By mainPageCatalogLocator = By.xpath("//*[contains(text(), 'Каталог')]");
    private By mainPageMyAccountLocator = By.xpath("//*[@id='menu-primary-menu']//*[contains(text(), 'Мой аккаунт')]");
    private By mainPageBasketLocator = By.xpath("//*[@id='menu-primary-menu']//*[contains(text(), 'Корзина')]");
    private By mainPageOrderPlacementLocator = By.xpath("//*[@id='menu-primary-menu']//*[contains(text()," +
            " 'Оформление заказа')]");

    private By mainPageFirstCardsRowLocator = By.xpath("//*[contains(@id, 'accesspress_storemo')]" +
            "//*[contains(@class, 'caption wow fadeIn')]");
    private By mainPageSearchFieldLocator = By.cssSelector(".search-field");
    private By mainPageSearchButtonLocator = By.cssSelector(".searchsubmit");
    private By mainPageSaleCardsLocator = By.cssSelector("#product1 .slick-active");
    private By mainPageSaleTitleLocator = By.cssSelector("#product1 .prod-title");
    private By mainPageSaleLabelLocator = By.cssSelector("#product1 .slick-active .onsale");
    private By mainPagePromoLocator = By.cssSelector(".promo-widget-wrap-full");
    private By mainPageNewGoodsLocator = By.cssSelector("#product2 .slick-active");
    private By mainPageNewLabelLocator = By.cssSelector("#product2 .slick-active .label-new");
    private By mainPageNewGoodsTitleLocator = By.cssSelector("#product2 .prod-title");
    private By mainPageItemsViewedTitleLocator = By.cssSelector(".ap-cat-list .widget-title");
    private By mainPageItemsViewedCardLocator = By.cssSelector(".product_list_widget li");
    private By mainPageFooterLocator = By.cssSelector("#top-footer");
    private By mainPageArrowUpLocator = By.cssSelector("#ak-top");


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



    @Test
    public void mainPage_hasLogoContactsAccount_logoContactsAccountAreDisplayed () {

        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");

        //assert
        Assertions.assertTrue(driver.findElement(mainPageLogoLocator).isDisplayed(),"Нет логотипа на главной странице");
        Assertions.assertTrue( driver.findElement(mainPageContactsLocator).isDisplayed(), "Нет контактов на главной странице");
        Assertions.assertTrue(driver.findElement(mainPageAccountLocator).isDisplayed(), "Нет логотипа на главной странице");
    }

    @Test
    public void mainPage_goMenuItems_successGoMenuItems () {
        //arrange
        By catalogLocator = By.cssSelector(".entry-title");
        By myAccountLocator = By.cssSelector(".current");
        By basketLocator = By.cssSelector(".current");
        By orderPlacementLocator = By.cssSelector(".current");

        driver.navigate().to("http://intershop5.skillbox.ru/");

        //act //assert
        driver.findElement(mainPageCatalogLocator).click();
        Assertions.assertEquals( "КАТАЛОГ",
                driver.findElement(catalogLocator).getText(), "Не произошел переход на страницу каталога");

        //act //assert
        driver.findElement(mainPageMyAccountLocator).click();
        Assertions.assertEquals( "Мой Аккаунт",
                driver.findElement(myAccountLocator).getText(), "Не произошел переход на страницу аккаунта");

        //act //assert
        driver.findElement(mainPageBasketLocator).click();
        Assertions.assertEquals("Корзина",
                driver.findElement(basketLocator).getText(), "Не произошел переход в корзину");

        //act //assert
        driver.findElement(mainPageOrderPlacementLocator).click();
        Assertions.assertEquals("Корзина",
                driver.findElement(orderPlacementLocator).getText(),
                "Не произошел переход на страницу оформления заказа");
    }

    //".promo-product1 .widget-title" заголовки

    @Test
    public void mainPage_goToFirstCards_successGoingToFirstCards () {

        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        List<WebElement> firstCardsList = driver.findElements(mainPageFirstCardsRowLocator);
        By changePageTitles = By.cssSelector(".entry-title");

        //act //assert
        for (int i=1; i<=firstCardsList.size(); i++) {
            firstCardsList = driver.findElements(mainPageFirstCardsRowLocator);
            wait.until(ExpectedConditions.visibilityOf(firstCardsList.get(i-1)));
            By titleCardLocator = By.xpath("(//*[contains(@class, 'promo-product1')]//h4[contains(@class, 'widget-title')])[" + i +"]");
            String titleCardText = driver.findElement(titleCardLocator).getText();
            firstCardsList.get(i-1).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(changePageTitles));
            String changedPageTitleText = driver.findElement(changePageTitles).getText();
            if (i == firstCardsList.size()) {
                Assertions.assertEquals("ФОТО/ВИДЕО", changedPageTitleText,
                        "Не произошел переход на нужную страницу");
            } else {
                Assertions.assertEquals(titleCardText, changedPageTitleText,
                        "Не произошел переход на нужную страницу");
                driver.navigate().back();
            }
        }
    }



    @Test
    public void mainPage_searchGoods_successSearchingGoods () {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        By changePageTitles = By.cssSelector(".entry-title");
        String needGood = "iPad";
        String result = "РЕЗУЛЬТАТЫ ПОИСКА: ";

        //act
        driver.findElement(mainPageSearchFieldLocator).sendKeys(needGood);
        driver.findElement(mainPageSearchButtonLocator).click();

        //arrange
        Assertions.assertEquals(result.concat("\u201C").concat(needGood).concat("\u201D")
                        .compareToIgnoreCase(driver.findElement(changePageTitles).getText()),0,
                "Произошел переход на страницу поиска другого товара");
    }

    @Test
    public void mainPage_goToSaleCards_successGoingToSaleCards () {

        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        List<WebElement> saleCardsList = driver.findElements(mainPageSaleCardsLocator);
        By productTitleLocator = By.cssSelector(".product_title");

        //act //assert
        for (int i=1; i<=saleCardsList.size(); i++) {
            saleCardsList = driver.findElements(mainPageSaleCardsLocator);
            wait.until(ExpectedConditions.visibilityOf(saleCardsList.get(i-1)));
            Assertions.assertTrue(driver.findElement(mainPageSaleLabelLocator).isDisplayed(),
                    "Нет лейбла скидки на карточке");

            By titleCardLocator = By.xpath("(//*[contains(@class, 'slick-active')]//h3)[" + i +"]");
            String titleCardText = driver.findElement(titleCardLocator).getText();
            saleCardsList.get(i-1).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(productTitleLocator));
            String changedPageTitleText = driver.findElement(productTitleLocator).getText();
            Assertions.assertEquals(titleCardText.compareToIgnoreCase(changedPageTitleText), 0,
                    "Не произошел переход на нужную страницу");
            driver.navigate().back();
        }
        Assertions.assertTrue( driver.findElement(mainPageSaleTitleLocator).isDisplayed(),
                "Нет заголовка Распродажа");
    }


    @Test
    public void mainPage_hasPromo_promoIsDisplayed () {

        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        By allGoodsTitle = By.cssSelector(".entry-title");
        String allGoods = "Все товары";

        //act
        driver.findElement(mainPagePromoLocator).click();

        //assert
        Assertions.assertEquals(driver.findElement(allGoodsTitle).getText().compareToIgnoreCase(allGoods), 0,
                "Не осуществлен переход на Все товары");

    }


    @Test
    public void mainPage_goNewGoods_successGoingToNewGoods () {

        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        List<WebElement> newGoodsList = driver.findElements(mainPageNewGoodsLocator);
        By productTitleLocator = By.cssSelector(".product_title.entry-title");


        //act //assert
        for (int i=1; i<=newGoodsList.size(); i++) {
            newGoodsList = driver.findElements(mainPageNewLabelLocator);
            wait.until(ExpectedConditions.visibilityOf(newGoodsList.get(i-1)));
            Assertions.assertTrue(driver.findElement(mainPageNewLabelLocator).isDisplayed(),
                    "Нет лейбла Новый на карточке");

            By titleCardLocator = By.xpath("(//*[@id = 'product2']//*[@aria-hidden = 'false']//h3)[" + i + "]");
            String titleCardText = driver.findElement(titleCardLocator).getText();
            newGoodsList.get(i-1).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(productTitleLocator));
            String changedPageTitleText = driver.findElement(productTitleLocator).getText();
            Assertions.assertEquals(titleCardText.compareToIgnoreCase(changedPageTitleText), 0,
                    "Не произошел переход на нужную страницу");
            driver.navigate().back();
        }
        Assertions.assertTrue(driver.findElement(mainPageNewGoodsTitleLocator).isDisplayed(),
                "Нет заголовка Новые Поступления");
    }


    @Test
    public void mainPage_hasItemsViewed_itemsViewedDisplayed () {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        List<WebElement> itemsViewedList = driver.findElements(mainPageItemsViewedCardLocator);


        //act //assert
        driver.findElement(mainPagePromoLocator).click();
        driver.navigate().to("http://intershop5.skillbox.ru/");
        driver.findElement(mainPageItemsViewedTitleLocator);

        for (int i=1; i<=itemsViewedList.size(); i++) {
            itemsViewedList = driver.findElements(mainPageItemsViewedCardLocator);
            By mainPageItemsViewedCard = By.cssSelector(".product_list_widget li[" + i + "]");
            Assertions.assertTrue(driver.findElement(mainPageItemsViewedCard).isDisplayed(),
                    "Не отображается карточка просмотренных товаров");
        }

        Assertions.assertTrue(driver.findElement(mainPageItemsViewedTitleLocator).isDisplayed(),
                "Нет заголовка блока просмотренных товаров");

    }

    @Test
    public void mainPage_hasFooter_footerDisplayed () {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");


        //act
        driver.findElement(mainPageFooterLocator);

        //assert
        Assertions.assertTrue(driver.findElement(mainPageFooterLocator).isDisplayed(), "Нет футера");

    }

    @Test
    public void mainPage_clickArrowUp_arrowGoUp () {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");


        //act
        driver.findElement(mainPageArrowUpLocator).click();

        //assert
        Assertions.assertTrue(driver.findElement(mainPageLogoLocator).isDisplayed(),
                "Не произошел переъод к хэдеру по клику на стрелку");

    }
}
