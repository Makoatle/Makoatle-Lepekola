package com.example.demo9;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {
    private List<Question> questions;
    private int currentQuestionIndex;
    private int score;

    private Label questionLabel;
    private VBox optionsContainer;
    private Label feedbackLabel;
    private Label scoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        questions = createQuestions();
        currentQuestionIndex = 0;
        score = 0;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 600, 400);

        questionLabel = new Label();
        questionLabel.setFont(Font.font(18));
        questionLabel.getStyleClass().add("question-label");

        optionsContainer = new VBox(10);

        feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("feedback-label");

        scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score-label");

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(questionLabel, optionsContainer, feedbackLabel);

        VBox scoreBox = new VBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.getChildren().add(scoreLabel);

        root.setCenter(centerBox);
        root.setBottom(scoreBox);
        BorderPane.setAlignment(scoreBox, Pos.CENTER);

        displayQuestion();

        ImageView imageView = new ImageView();

        centerBox.getChildren().add(imageView);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.show();
    }

    private List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is the capital city of Lesotho?", "Maseru",
                List.of("Maseru", "Mohale's Hoek", "Mafeteng"),
                "maseru.jpg"));
        questions.add(new Question("Which is highest mountain of Lesotho?", "Thabana Ntlenyana",
                List.of("Qiloane", "Thabana Ntlenyana", "Thaba Bosiu"),
                "thaba.jpg"));
        questions.add(new Question("Where is Maletsunyane Falls found?", "Semonkong",
                List.of("'Muela", "Tshehlanyane", "Semonkong"),
                "falls.jpg"));
        questions.add(new Question("Which one of this three districts is Katse-Dam found?", "Thaba-Tseka",
                List.of("Quthing", "Thaba-Tseka", "Butha-Bothe"),
                "katse.jpg"));
        questions.add(new Question("What is the name of the Basotho hat?", "Mokorotlo",
                List.of("Mokorotlo", "Seshoeshoe", "Thethana"),
                "hat.jpg"));
        return questions;
    }

    private void displayQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getQuestion());
        optionsContainer.getChildren().clear();

        // Load and display the image
        // Load and display the image
        String imagePath = currentQuestion.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            InputStream inputStream = getClass().getResourceAsStream("images" + imagePath); // Add a leading slash to ensure the correct path
            if (inputStream != null) {
                Image image = new Image(inputStream);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300); // Set the desired width of the image
                imageView.setFitHeight(200); // Set the desired height of the image
                imageView.setPreserveRatio(true); // Maintain aspect ratio
                optionsContainer.getChildren().add(imageView);
            } else {
                System.err.println("Failed to load image: " + imagePath);
            }
        } else {
            System.err.println("Invalid image path");
        }

        for (String option : currentQuestion.getOptions()) {
            Button optionButton = new Button(option);
            optionButton.setOnAction(e -> selectAnswer(option, currentQuestion.getCorrectAnswer()));
            optionButton.getStyleClass().add("option-button");
            optionsContainer.getChildren().add(optionButton);
        }

        feedbackLabel.setText("");
    }

    private void selectAnswer(String selectedOption, String correctAnswer) {
        if (selectedOption.equals(correctAnswer)) {
            score++;
            feedbackLabel.setText("Correct!");
            feedbackLabel.setTextFill(Color.GREEN);
        } else {
            feedbackLabel.setText("Incorrect! Correct answer is: " + correctAnswer);
            feedbackLabel.setTextFill(Color.RED);
        }

        currentQuestionIndex++;
        scoreLabel.setText("Score: " + score);

        if (currentQuestionIndex < questions.size()) {
            displayQuestion();
        } else {
            endGame();
        }
    }

    private void endGame() {
        questionLabel.setText("Quiz completed!");
        optionsContainer.getChildren().clear();
        feedbackLabel.setText("You scored " + score + " out of " + questions.size());
    }

    private static class Question {
        private final String question;
        protected String correctAnswer;
        private final List<String> options;
        private final String imagePath; // Path to the image file

        public Question(String question, String correctAnswer, List<String> options, String imagePath) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.options = options;
            this.imagePath = imagePath;
        }

        public String getQuestion() {
            return question;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
