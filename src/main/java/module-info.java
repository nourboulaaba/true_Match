module tn.esprit.pitest11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires jdk.httpserver;
    requires com.fasterxml.jackson.databind;
    requires async.http.client;
    requires org.json.chargebee;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires layout;
    requires kernel;
    requires io;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;

    opens tn.esprit.pitest11 to javafx.fxml;
    exports tn.esprit.pitest11;
    exports tn.esprit.pitest11.Main;              // Exports the main package
    exports tn.esprit.pitest11.Controller;    // Ensures controllers are accessible
    opens tn.esprit.pitest11.Controller to javafx.fxml;
    opens tn.esprit.pitest11.Model to javafx.base;
}