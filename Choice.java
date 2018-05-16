import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Choice extends Application {

    private String input;

    @Override
    public void start(Stage primaryStage) throws IOException{

        // Writer logifaili loomiseks.

        BufferedWriter writer = new BufferedWriter(new FileWriter("Choose.txt"));

        // Esilehekülje loomine.

        GridPane startPage = new GridPane();
        startPage.setAlignment(Pos.CENTER);
        startPage.setHgap(10);
        startPage.setVgap(10);
        startPage.setPadding(new Insets(25,25,25,25));

        Text start_title = new Text("Welcome!");
        start_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        startPage.add(start_title, 0, 0, 2, 1);

        Label name = new Label("Your Name:");
        startPage.add(name, 0, 1);

        TextField userTextField = new TextField();
        startPage.add(userTextField, 1, 1);

        Button btn = new Button("Start Adventure!");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        startPage.add(btn, 1, 3);

        Scene scene = new Scene(startPage, 1000, 500);


        // Loome leheküljed, millest "raamat" koosneb.

        Page fork = new Page(new GridPane(),
                new Text("You are walking along a forest path,\nyou see a fork in the road."),
                new Button("Left"),
                new Button("Right"));

        Page forkLeft = new Page(new GridPane(),
                new Text("You see a wolf.\nWhat do you do?"),
                new Button("Fight"),
                new Button("Run"));

        Page wolfWin = new Page(new GridPane(),
                new Text("Congratulations?\nYou beat up a wolf.\n...but you got arrested for hunting without a permit."),
                new Button("Try again"),
                new Button("Give up"));

        Page wolfLose = new Page(new GridPane(),
                new Text("You died."),
                new Button("Try again"),
                new Button("Give up"));

        Page forkRight = new Page(new GridPane(),
                new Text("You reach a lake,\nyou see something shimmering underwater."),
                new Button("Jump in"),
                new Button("Poke it with a stick"));

        Page lakeJumped = new Page(new GridPane(),
                new Text("You can't swim,\nyou drowned."),
                new Button("Try again"),
                new Button("Give up"));


        // Loome sündmused igale nupule.

        // Esilehe sündmused.

        btn.setOnAction(event -> {
            input = userTextField.getText();
            pageTurn(writer, fork.getLayout(), scene);
        });
        userTextField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                input = userTextField.getText();
                pageTurn(writer, fork.getLayout(), scene);
            }
        });

        // fork lehekülje sündmused.

        choiceButton(writer, forkLeft.getLayout(), scene, fork.getLeft());
        leftBtn(writer, forkLeft.getLayout(), scene, fork.getLeft());

        choiceButton(writer, forkRight.getLayout(), scene, fork.getRight());
        rightBtn(writer, forkRight.getLayout(), scene, fork.getRight());

        // forkLeft lehekülje sündmused.

        forkLeft.getLeft().setOnAction(event -> {
            input = forkLeft.getLeft().getText().toLowerCase();
            try {
                writer.append(input).append("\n");
                writer.flush();
            } catch (IOException e2) {
            }
            int result = (int)(Math.random()*2);
            coinToss(writer, scene, wolfWin.getLayout(), wolfLose.getLayout(), result);
        });
        forkLeft.getLeft().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                input = forkLeft.getLeft().getText().toLowerCase();
                try {
                    writer.append(input).append("\n");
                    writer.flush();
                } catch (IOException e2) {
                }
                int result = (int)(Math.random()*2);
                coinToss(writer, scene, wolfWin.getLayout(), wolfLose.getLayout(), result);
            }
        });

        forkLeft.getRight().setOnAction(e -> btnBack(writer, scene, fork, forkLeft));
        forkLeft.getRight().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnBack(writer, scene, fork, forkLeft);
            }
        });

        // wolfLose lehekülje sündmused.

        btnReset(writer, startPage, scene, wolfLose.getLeft());
        btnResetLeft(writer, startPage, scene, wolfLose.getLeft());

        btnClose(writer, primaryStage, wolfLose.getRight());
        btnCloseRight(writer, primaryStage, wolfLose.getRight());

        // wolfWin lehekülje sündmused.

        btnReset(writer, startPage, scene, wolfWin.getLeft());
        btnResetLeft(writer, startPage, scene, wolfWin.getLeft());

        btnClose(writer, primaryStage, wolfWin.getRight());
        btnCloseRight(writer, primaryStage, wolfWin.getRight());

        // forkRight lehekülje sündmused.

        choiceButton(writer, lakeJumped.getLayout(), scene, forkRight.getLeft());
        leftBtn(writer, lakeJumped.getLayout(), scene, forkRight.getLeft());

        forkRight.getRight().setOnAction(event -> btnBack(writer, scene, fork, forkRight));
        forkRight.getRight().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                btnBack(writer, scene, fork, forkRight);
            }
        });

        // lakeJumped lehekülje sündmused.

        btnReset(writer, startPage, scene, lakeJumped.getLeft());
        btnResetLeft(writer, startPage, scene, lakeJumped.getLeft());

        btnClose(writer, primaryStage, lakeJumped.getRight());
        btnCloseRight(writer, primaryStage, lakeJumped.getRight());


        // Loome lava.

        primaryStage.setScene(scene);
        primaryStage.setTitle("Choose");
        primaryStage.isResizable();
        primaryStage.show();
    }


    // Abimeetodid erinevate nuppude sündmuste loomiseks.

    // sündmus, millega liikuda tagasi mingile eelnevale lehele, uurides logifailist kas mängija on sel lehel juba käinud.
    private void btnBack(BufferedWriter writer, Scene scene, Page pageTo, Page pageFrom) {
        try {
            writer.append(pageFrom.getRight().getText().toLowerCase()).append("\n");
            writer.flush();
            Scanner sc = new Scanner(new FileReader("Choose.txt"));
            sc.useDelimiter("\n");
            while (sc.hasNextLine()) {
                String line = sc.next();
                if (line.equals(pageTo.getRight().getText().toLowerCase()) || line.equals(pageTo.getLeft().getText().toLowerCase())) scene.setRoot(pageTo.getLayout());
            }
            sc.close();
        }catch (IOException | NoSuchElementException e){
        }
    }

    // sündmus, millega alustada mängu algusest.
    private void btnReset(BufferedWriter writer, GridPane start, Scene scene, Button button) {
        button.setOnAction(e -> {
            try {
                new PrintWriter("choose.txt").close();
            } catch (IOException e2) {

            }
            scene.setRoot(start);
        });
    }

    // sündmus, millega klaviatuuri vasaku nooleklahviga alustada mängu algusest.
    private void btnResetLeft(BufferedWriter writer, GridPane start, Scene scene, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                try {
                    new PrintWriter("choose.txt").close();
                } catch (IOException e2) {

                }
                scene.setRoot(start);
            }
        });
    }

    // sündmus, millega klaviatuuri parema nooleklahviga alustada mängu algusest.
    private void btnResetRight(BufferedWriter writer, GridPane start, Scene scene, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                try {
                    writer.close();
                } catch (IOException e2) {

                }
                scene.setRoot(start);
            }
        });
    }

    // sündmus, millega sulgeda mäng.
    private void btnClose(BufferedWriter writer, Stage stage, Button button) {
        button.setOnAction(e -> {
            try {
                writer.close();
            } catch (IOException e2) {

            }
            stage.close();
        });
    }

    // sündmus, millega sulgeda mäng klaviatuuri vasaku nooleklahviga.
    private void btnCloseLeft(BufferedWriter writer, Stage stage, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                try {
                    writer.close();
                } catch (IOException e2) {

                }
                stage.close();
            }
        });
    }

    // sündmus, millega sulgeda mäng klaviatuuri parema nooleklahviga.
    private void btnCloseRight(BufferedWriter writer, Stage stage, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                try {
                    writer.close();
                } catch (IOException e2) {

                }
                stage.close();
            }
        });
    }

    // sündmis, millega saab liikuda järgmisele leheküljele.
    private void choiceButton(BufferedWriter writer, GridPane page, Scene scene, Button button) {
        button.setOnAction(e -> {
            input = button.getText().toLowerCase();
            try {
                writer.append(input).append("\n");
                writer.flush();
            } catch (IOException e2) {

            }
            scene.setRoot(page);
        });
    }

    // sündmus, millega liikuda järgmisele leheküljele klaviatuuri parema nooleklahviga.
    private void rightBtn(BufferedWriter writer, GridPane pane, Scene scene, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                input = button.getText().toLowerCase();
                try {
                    writer.append(input).append("\n");
                    writer.flush();
                } catch (IOException e2) {

                }
                scene.setRoot(pane);
            }
        });
    }

    // sündmus, millega liikuda järgmisele leheküljele klaviatuuri vasaku nooleklahviga.
    private void leftBtn(BufferedWriter writer, GridPane pane, Scene scene, Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                input = button.getText().toLowerCase();
                try {
                    writer.append(input).append("\n");
                    writer.flush();
                } catch (IOException e2) {

                }
                scene.setRoot(pane);
            }
        });
    }

    // sündmus, millega liikuda 50/50 võimalusega ühele määratud lehtedest.
    private void coinToss(BufferedWriter writer, Scene scene, GridPane paneLose, GridPane paneWin, int result) {
        if(result == 0) scene.setRoot(paneLose);
        else scene.setRoot(paneWin);
    }

    // sündmus, millega saab liikuda järgmisele lehele, kui tekstikast nime jaoks ei ole tühi.
    private void pageTurn(BufferedWriter writer, GridPane page, Scene scene) {
        if (!input.equals("")) {
            try {
                writer.append(input).append("\n");
                writer.flush();
            } catch (IOException e) {
            }
            scene.setRoot(page);
        }
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
