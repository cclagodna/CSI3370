package javafxapplication5;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TestController{

    @FXML
    private Button btn_start, btn_pause, btn_name;
    @FXML
    private Label label_name;
    
    @FXML
    void play(ActionEvent event) {
        System.out.println("start");
        Main.mp.play();
        label_name.setText(Main.currSong);
    }
    
    @FXML
    void pause(ActionEvent event) {
        System.out.println("pause");
        Main.mp.pause();
    }
    
    @FXML
    void change(ActionEvent event) {
        label_name.setText(Main.currSong);
    }

    @FXML
    void initialize() {

    }
}
