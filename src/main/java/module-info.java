module com.example.gamesoflife {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens com.example.gamesoflife to javafx.fxml;
    exports com.example.gamesoflife;
}