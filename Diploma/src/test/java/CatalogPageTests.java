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

public class CatalogPageTests {

    private WebDriver driver;
    private WebDriverWait wait;

    private By catalogTitleLocator = By.cssSelector(".entry-title");
    private By catalogPageSearchFieldLocator = By.cssSelector(".search-field");
    private By catalogPageSearchButtonLocator = By.cssSelector(".searchsubmit");
    private By catalogPageBasketButtonLocator = By.xpath("(//*[contains(@class, 'add_to_cart_button')])[1]");
    private By catalogPageDetailedButtonLocator = By.xpath("//*[contains(@class, 'added_to_cart')]");
    private By catalogPageToBasketLocator = By.cssSelector(".current");
    private By searchResultGoodNameLocator = By.xpath("(//*[contains(@class, 'collection_title')])[1]//h3");
    private By catalogPageGoodNameBasketLocator = By.cssSelector(".product-name a");
    private By catalogPageGoodNameLocator = By.xpath("(//*[contains(@class, 'instock')])[1]//h3");
    private By catalogPageCategoryPhonesLocator = By.xpath("//*[contains(@class, 'cat-item')]//*[text() = 'Телефоны']");
    private By catalogPageItemFromGoodsLocator = By.cssSelector(".product_list_widget li:nth-of-type(2) .product-title");

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


    //нахождение на странице каталога
    @Test
    public void catalogPage_hasCatalogTitle_titleIsDisplayed() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");

        //assert
        Assertions.assertTrue(driver.findElement(catalogTitleLocator).isDisplayed(),
                "Находимся не на странице каталога");
    }


    //корректный поиск и добавление в корзину через поле поиска
    @Test
    public void catalogPage_searchGoodsBySearchField_successSearchingGoods() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");
        By changePageTitles = By.cssSelector(".entry-title");
        String needGood = "Телефон";
        String result = "РЕЗУЛЬТАТЫ ПОИСКА: ";

        //act
        driver.findElement(catalogPageSearchFieldLocator).sendKeys(needGood);
        driver.findElement(catalogPageSearchButtonLocator).click();

        //arrange
        Assertions.assertEquals(result.concat("\u201C").concat(needGood).concat("\u201D").compareToIgnoreCase(driver.findElement(changePageTitles).getText()),
                0, "Произошел переход на страницу поиска другого товара");

        //act
        String goodName = driver.findElement(searchResultGoodNameLocator).getText();
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();


        //arrange
        Assertions.assertTrue(driver.findElement(catalogPageToBasketLocator).isDisplayed(),
                "Не произошел переход в корзину");
        Assertions.assertEquals(goodName,
                driver.findElement(catalogPageGoodNameBasketLocator).getText(), "Не тот товар добавился в корзину");
    }


    //корректный поиск и добавление в корзину через каталог
    @Test
    public void catalogPage_addGoodToBasketFromCatalog_successAdditional() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");


        //act
        String goodName = driver.findElement(catalogPageGoodNameLocator).getText();
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();

        //assert
        Assertions.assertTrue(driver.findElement(catalogPageToBasketLocator).isDisplayed(),
                "Не произошел переход в корзину");
        Assertions.assertEquals(goodName,
                driver.findElement(catalogPageGoodNameBasketLocator).getText(), "Не тот товар добавился в корзину");
    }


    //корректный поиск и добавление в корзину через категории товаров
    @Test
    public void catalogPage_addGoodToBasketFromCategories_successAdditional() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");


        //act //assert
        String categoryName = driver.findElement(catalogPageCategoryPhonesLocator).getText().toUpperCase();
        driver.findElement(catalogPageCategoryPhonesLocator).click();
        Assertions.assertEquals(categoryName,
                driver.findElement(catalogTitleLocator).getText(), "Произошел переход не в ту категорию");
        //находим тел, кликаем на него, проверяем, что он в корзине
        String goodName = driver.findElement(catalogPageGoodNameLocator).getText();
        driver.findElement(catalogPageBasketButtonLocator).click();
        driver.findElement(catalogPageDetailedButtonLocator).click();


        //assert
        Assertions.assertTrue(driver.findElement(catalogPageToBasketLocator).isDisplayed(),
                "Не произошел переход в корзину");
        Assertions.assertEquals(goodName,
                driver.findElement(catalogPageGoodNameBasketLocator).getText(), "Не тот товар добавился в корзину");

    }

    //корректный поиск и добавление в корзину через список ТОВАРЫ (под категориями)
    @Test
    public void catalogPage_addGoodToBasketFromGoods_successAdditional() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/product-category/catalog/");


        //act
        driver.findElement(catalogPageItemFromGoodsLocator).click();
        String goodName = driver.findElement(By.cssSelector(".product_title")).getText();
        driver.findElement(By.cssSelector(".single_add_to_cart_button")).click();
        driver.findElement(By.cssSelector(".button.wc-forward")).click();


        //assert
        Assertions.assertTrue(driver.findElement(catalogPageToBasketLocator).isDisplayed(),
                "Не произошел переход в корзину");
        Assertions.assertEquals(goodName,
                driver.findElement(catalogPageGoodNameBasketLocator).getText(), "Не тот товар добавился в корзину");
    }
}
