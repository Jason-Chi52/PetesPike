package petespike.view;

import javafx.scene.control.TextField;
import petespike.model.PetesPike;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TextFieldEventHandler implements EventHandler<ActionEvent>{

    private TextField text;

    public TextFieldEventHandler(PetesPike game, TextField text){
        this.text = text;
    }
    @Override
    public void handle(ActionEvent event) {
        try{
            new PetesPike(text.getText());
            

            // System.out.println(this.game.getCol());
            // new PetesPikeCLI().startGame();
            
        } catch (IOException e){
            System.out.println("The file is not available " + e.getMessage());
        // } catch (PetesPikeException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
    
    }
}
