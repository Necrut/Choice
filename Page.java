import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

// Klass lehekülgede loomiseks.

public class Page {
    private GridPane layout;
    private Text text;
    private Button left;
    private Button right;

    // Konstruktor, seab lehekülje üles, hetkel ainult üht tüüpi leheküljed.

    public Page(GridPane layout, Text text, Button left, Button right) {
        this.layout = layout;
        this.text = text;
        this.left = left;
        this.right = right;
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(25,25,25,25));
        layout.add(text, 1,0);
        layout.add(right, 2, 1);
        layout.add(left, 0, 1);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    }

    // Getterid ja Setterid lehekülgede elementide kättesaamiseks või muutmiseks.

    public GridPane getLayout() {
        return layout;
    }

    public Text getText() {
        return text;
    }

    public Button getLeft() {
        return left;
    }

    public Button getRight() {
        return right;
    }

    public void setLayout(GridPane layout) {
        this.layout = layout;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setLeft(Button left) {
        this.left = left;
    }

    public void setRight(Button right) {
        this.right = right;
    }
}
