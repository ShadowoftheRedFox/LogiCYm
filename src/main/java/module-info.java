module cytech.pjava {
    // transitive to make the compiler happy
    // (it's a requirement by javafx and how the structure has been made)
    requires transitive javafx.controls;
    requires javafx.fxml;

    // export our package
    exports com.pjava;

    // to authorize FXML files and FXMLLoader to see
    // classes from our package
    opens com.pjava.controllers to javafx.fxml;
}
