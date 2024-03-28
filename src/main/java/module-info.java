module com.cheftime.cheftimeapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cheftime.cheftimeapp to javafx.fxml;
    exports com.cheftime.cheftimeapp;
}