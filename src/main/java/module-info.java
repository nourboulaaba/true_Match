module org.example.pifinal {
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
    requires java.sql;
    requires org.json.chargebee;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires layout;
    requires kernel;
    requires io;
    requires java.desktop;

    opens org.example.pifinal to javafx.fxml;
    exports org.example.pifinal;
    exports org.example.pifinal.Main;              // Exports the main package
    exports org.example.pifinal.Controller;    // Ensures controllers are accessible
    opens org.example.pifinal.Controller to javafx.fxml;
    opens org.example.pifinal.Model to javafx.base;
}