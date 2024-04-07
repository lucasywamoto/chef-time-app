module com.cheftime.cheftimeapp {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;

    exports com.cheftime.cheftimeapp.controllers;
    opens com.cheftime.cheftimeapp.controllers to javafx.fxml;
    exports com.cheftime.cheftimeapp.entities;
    opens com.cheftime.cheftimeapp.entities to javafx.fxml;
    exports com.cheftime.cheftimeapp.services;
    opens com.cheftime.cheftimeapp.services to javafx.fxml;
    exports com.cheftime.cheftimeapp.application;
    opens com.cheftime.cheftimeapp.application to javafx.fxml;
}