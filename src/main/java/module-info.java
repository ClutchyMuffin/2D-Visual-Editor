module com.example.cmpt3812024a3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmpt3812024a3 to javafx.fxml;
    exports com.example.cmpt3812024a3;
}