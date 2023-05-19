module com.example.gamesoflife {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens com.simosera.gamesoflife to javafx.fxml;
    exports com.simosera.gamesoflife;
}