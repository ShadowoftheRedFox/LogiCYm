module cytech.pjava {
    requires transitive javafx.controls;
    requires javafx.fxml;

    exports com.pjava;

    opens com.pjava.controllers to javafx.fxml;
}
