module tn.esprit.pitest11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;


    opens tn.esprit.pitest11 to javafx.fxml;
    exports tn.esprit.pitest11;
    opens tn.esprit.pitest11.Controller to javafx.fxml;

    // Open Main package to JavaFX
    opens tn.esprit.pitest11.Main to javafx.graphics, javafx.fxml;
    exports tn.esprit.pitest11.Main;
}