module com.cheftime.cheftimeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;


    opens com.cheftime.cheftimeapp to javafx.fxml;
    exports com.cheftime.cheftimeapp;
}