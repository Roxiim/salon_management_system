module com.roxana.salonoop {
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
    requires org.postgresql.jdbc;

    opens com.roxana.salonoop to javafx.fxml;
    exports com.roxana.salonoop;

    opens com.roxana.salonoop.model to javafx.base;
    exports com.roxana.salonoop.model;

    opens com.roxana.salonoop.connection to javafx.fxml;
    exports com.roxana.salonoop.connection;

    opens com.roxana.salonoop.repository to java.sql;
    exports com.roxana.salonoop.repository;

    opens com.roxana.salonoop.controller to javafx.fxml;
    exports com.roxana.salonoop.controller;
}