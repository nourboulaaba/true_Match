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
    requires java.sql;

    opens org.example.pifinal to javafx.fxml;
    exports org.example.pifinal;
    exports org.example.pifinal.Main;              // Exports the main package
    exports org.example.pifinal.Controller;    // Ensures controllers are accessible
    opens org.example.pifinal.Controller to javafx.fxml;
}