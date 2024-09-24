module petespike {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;
    requires javafx.base;

    opens petespike.view to javafx.fxml;
    exports petespike.view;
    exports petespike.model;
    exports backtracker;
}
