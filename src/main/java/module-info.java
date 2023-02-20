module org.example.wordlecheater {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.bonigarcia.webdrivermanager;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires io.reactivex.rxjava3;
    requires org.jetbrains.annotations;

    opens org.example.wordlecheater to javafx.fxml;
    exports org.example.wordlecheater;
}