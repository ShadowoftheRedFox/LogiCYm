/**
 * Manage the modules.
 */
module com.pjava {
    // transitive to make the compiler happy
    // (it's a requirement by javafx and how the structure has been made)
    requires transitive javafx.controls;
    requires javafx.fxml;

    // export our package
    exports com.pjava;
    exports com.pjava.src.components;
    exports com.pjava.src.components.cables;
    exports com.pjava.src.components.input;
    exports com.pjava.src.components.gates;
    exports com.pjava.src.components.ouptut;
    exports com.pjava.src.errors;
    // exports com.pjava.src.schema;
    exports com.pjava.src.UI;
    exports com.pjava.src.utils;

    // to authorize FXML files and FXMLLoader to see
    // classes from our package
    opens com.pjava.controllers to javafx.fxml;
}
