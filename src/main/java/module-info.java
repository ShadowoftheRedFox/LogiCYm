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
    exports com.pjava.src.components.gates;
    exports com.pjava.src.components.input;
    exports com.pjava.src.components.output;
    exports com.pjava.src.errors;
    // exports com.pjava.src.schema;
    exports com.pjava.src.UI;
    exports com.pjava.src.utils;

    // to authorize FXML files and FXMLLoader to see
    // classes from our package
    opens com.pjava.controllers to javafx.fxml;
    opens com.pjava.src.UI.components to javafx.fxml;
    opens com.pjava.src.UI.components.gates to javafx.fxml;
    // opens com.pjava.src.UI.components.cables to javafx.fxml;
    // opens com.pjava.src.UI.components.input to javafx.fxml;
    // opens com.pjava.src.UI.components.output to javafx.fxml;
}
