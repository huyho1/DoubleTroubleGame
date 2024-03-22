module com.example.doubletroublegame {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.doubletroublegame to javafx.fxml;
    exports com.example.doubletroublegame;
}