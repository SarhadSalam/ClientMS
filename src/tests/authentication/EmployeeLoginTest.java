package authentication;

import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class EmployeeLoginTest extends ApplicationTest{
    @Override
    public void start(Stage primaryStage) throws Exception {

        EmployeeLogin el = new EmployeeLogin();
        el.start(primaryStage);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
    }

    @Test
    public void testEntireApplication() throws TimeoutException {
        System.out.println("Starting Tests");


        signInButton();
        searchUsingFileNo();
        searchTodayVisits();

        System.out.println("All Tests ok");
    }

    private void searchTodayVisits() {
        clickOn("#refreshAmountButton");
        sleep(5000);
        clickOn("#viewVisitsButton");
        sleep(5000);
        closeCurrentWindow();
    }

    private void searchUsingFileNo() {
        clickOn("#searchBar");
        write("17");
        clickOn("#fileNoRadio");
        clickOn("#searchButton");


        //test add visits information
        clickOn("#infoTab");
        clickOn("#fileNo");
        write("Do you think I give a lil shit?");

        clickOn("#addTab");
        clickOn("#servicesText");
        write("Testing Application");
        clickOn("#amount");
        write("23");
        clickOn("#recordButton");

    }

    private void signInButton() {
        clickOn("#username");
        write("martin");
        clickOn("#password");
        write("martin");
        clickOn("#signIn");
    }
}