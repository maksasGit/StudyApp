module main.demo {
    requires javafx.controls;
    requires javafx.fxml;
            
                        requires org.kordamp.bootstrapfx.core;
            
    opens main.demo to javafx.fxml;
    exports main.demo;
}