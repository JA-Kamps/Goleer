package com.example.webapp;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    @FXML
    private Canvas gameCanvas;
    @FXML
    public Button terugKnop;

    private GraphicsContext gc;
    private int boardWidth = 360;
    private int boardHeight = 640;

    // Images
    private Image backgroundImg;
    private Image birdImg;
    private Image topPipeImg;
    private Image bottomPipeImg;
    private Image betweenPipeImg;

    // Bird class
    private int birdX = boardWidth / 12;
    private int birdY = boardWidth / 4;
    private int birdWidth = 34;
    private int birdHeight = 24;

    private class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    // Pipe class
    private int pipeX = boardWidth;
    private int pipeY = 0;
    private int pipeWidth = 64; // scaled by 1/6
    private int pipeHeight = 512;

    private class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;
        boolean isVisible = true;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game logic
    private Bird bird;
    private double velocityX = -4; // move pipes to the left speed (simulates bird moving right)
    private double velocityY = 0; // move bird up/down speed
    private double gravity = 0.5;

    private ArrayList<Pipe> pipes;
    private AnimationTimer gameLoop;
    private boolean gameOver = false;
    private double score = 0;

    private int currentPipesRows = -1;
    private int attempts = 1;
    private boolean isCompleted = false;

    public void db() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Answers, CorrectAnswer, Question FROM AnswerValue WHERE Module_idModule = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            int moduleID = ModuleIDHolder.getInstance().getModuleID();
            statement.setInt(1, moduleID);
            ResultSet resultSet = statement.executeQuery();
            List<Integer> correctAnswerList = new ArrayList<>();
            while (resultSet.next()) {
                questions.add(resultSet.getString("Question"));
                String[] splitAnswers = resultSet.getString("Answers").split(",");
                answers.add(splitAnswers);
                correctAnswerList.add(resultSet.getInt("CorrectAnswer"));
            }
            correctAnswers = correctAnswerList.stream().mapToInt(i -> i).toArray();
            maxPipesRows = questions.size();
        }
    }
    // Question system
    private List<String> questions = new ArrayList<>();
    private List<String[]> answers = new ArrayList<String[]>();




    private int[] correctAnswers; // Indexes of correct answers in each question
    private int currentQuestionIndex = 0;
    private int maxPipesRows;

    @FXML
    public void initialize() throws SQLException {
        Platform.runLater(() -> gameCanvas.requestFocus());
        db();

        gc = gameCanvas.getGraphicsContext2D();

        // Load images
        backgroundImg = new Image(getClass().getResourceAsStream("flappybirdbg.png"));
        birdImg = new Image(getClass().getResourceAsStream("flappybird.png"));
        topPipeImg = new Image(getClass().getResourceAsStream("toppipe.png"));
        bottomPipeImg = new Image(getClass().getResourceAsStream("bottompipe.png"));
        betweenPipeImg = new Image(getClass().getResourceAsStream("betweenpipe.png"));

        // Bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();

        // Game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    try {
                        move();
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    draw();
                }
            }
        };
        gameLoop.start();

        // Timer for placing pipes
        Timeline pipePlacer = new Timeline(
                new KeyFrame(
                        Duration.seconds(4),
                        event -> placePipes()
                )
        );
        pipePlacer.setCycleCount(Animation.INDEFINITE);
        pipePlacer.play();

        gameCanvas.setOnKeyPressed(this::handleKeyPress);
        gameCanvas.setFocusTraversable(true);
    }


    private void placePipes() {
        if (currentPipesRows >= maxPipesRows) {
            isCompleted = true;

            return;
        } else {
            currentPipesRows++;
        }

        // Top pipe
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = -480;
        pipes.add(topPipe);

        // Middle pipes
        int[] middlePipeYPositions = {145, 380}; // Specify the exact y-positions of the middle pipes
        int middlePipeHeight = pipeHeight / 4; // Define height of the middle pipes

        for (int yPosition : middlePipeYPositions) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = yPosition;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            pipes.add(betweenPipe);
        }
        int correctAnswerIndex = correctAnswers[currentQuestionIndex]; // Get the correct answer for the current question

        // Set booleans for answers
        boolean antwoord1 = correctAnswerIndex == 0;
        boolean antwoord2 = correctAnswerIndex == 1;
        boolean antwoord3 = correctAnswerIndex == 2;

        if (!antwoord1) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 33;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }
        if (!antwoord2) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 150 + 128;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }
        if (!antwoord3) {
            Pipe betweenPipe = new Pipe(betweenPipeImg);
            betweenPipe.y = 380 + 128;
            betweenPipe.height = middlePipeHeight;
            betweenPipe.width = pipeWidth; // Adjust width if needed
            betweenPipe.isVisible = false;
            pipes.add(betweenPipe);
        }

        // Bottom pipe
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = 630;
        pipes.add(bottomPipe);
    }

    private void draw() {
        // Background
        gc.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight);

        // Bird
        gc.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height);

        // Pipes
        for (Pipe pipe : pipes) {
            if (pipe.isVisible) {
                gc.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height);
            }
        }

        // Score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 32));
        gc.fillText("Score: " + (int) score, 10, 35);

        // Question and answers
        if (!gameOver) {
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 12));
            gc.fillText("Question: " + questions.get(currentQuestionIndex), 10, 100);

            // Display answers at the pipe openings
            String[] currentAnswers = answers.get(currentQuestionIndex);
            gc.setFont(new Font("Arial", 18));
            gc.fillText("A: " + currentAnswers[0], 10, 65 + 20); // Adjust the y-position as needed
            gc.fillText("B: " + currentAnswers[1], 10, 300 + 20); // Adjust the y-position as needed
            gc.fillText("C: " + currentAnswers[2], 10, 580 + 20); // Adjust the y-position as needed
        }

        // Game over messages
        if (gameOver) {
            gc.setFont(new Font("Arial", 50));
            if (isCompleted) {
                gc.fillText("Uitgespeeld!", 80, 150);
            } else {
                gc.fillText("Game Over", 80, 150);
            }
            gc.setFont(new Font("Arial", 30));
            gc.fillText("Attempts: " + attempts, 10, 360);
        }
    }

    private void move() throws SQLException, IOException {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.16666667;
                pipe.passed = true;


                // Update question index based on the integer part of the score
                if ((int) score > currentQuestionIndex) {
                    currentQuestionIndex = Math.min((int) score, questions.size() - 1);
                }
            }

            if (collision(bird, pipe)) {
                pipes.clear();
                gameOver = true;
                return;
            }
        }

        if (score >= maxPipesRows) {
            saveScore();
            gameOver = true;
            isCompleted = true;
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    private void saveScore() throws SQLException {
        int teacherId = 0;
        int classId = 0;
        int studentId = StudentHolder.getInstance().getStudentId();
        int moduleID = ModuleIDHolder.getInstance().getModuleID();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT Teacher_idTeacher, Class_idClass FROM Student WHERE idStudent = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                teacherId = resultSet.getInt("Teacher_idTeacher");
                classId = resultSet.getInt("Class_idClass");
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Module_has_Class_has_Student (Module_has_Class_Module_idModule, Module_has_Class_Class_idClass, Module_has_Class_Class_Teacher_idTeacher, Student_idStudent, Score) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, moduleID);
            statement.setInt(2, classId);
            statement.setInt(3, teacherId);
            statement.setInt(4, studentId);
            statement.setDouble(5, score);
            statement.execute();

        }


    }

    private boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width && // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x && // a's top right corner passes b's top left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y; // a's bottom left corner passes b's top left corner
    }

    private void handleKeyPress(KeyEvent e) {


        switch (e.getCode()) {
            case SPACE:
                if (isCompleted) {
                    return; // Prevent restarting the game after completing all pipes
                }

                velocityY = -6; // Make the bird jump

                if (gameOver) {
                    bird.y = birdY;
                    velocityY = 0;
                    pipes.clear();
                    gameOver = false;
                    score = 0;
                    currentPipesRows = -1;
                    attempts++;
                    currentQuestionIndex = 0;

                    gameLoop.start();
                }
                break;
            case DIGIT1:
                handleAnswer(0);
                break;
            case DIGIT2:
                handleAnswer(1);
                break;
            case DIGIT3:
                handleAnswer(2);
                break;
            default:
                break;
        }
    }

    private void handleAnswer(int answerIndex) {
        if (answerIndex == correctAnswers[currentQuestionIndex]) {
            score++;
            currentQuestionIndex = Math.min((int) score, questions.size() - 1); // Move to the next question
        } else {
            gameOver = true; // End the game if the answer is wrong
        }
    }
    public void gaTerug(ActionEvent event) throws IOException {
        ModuleIDHolder.getInstance().clearModuleID();
        Parent root = FXMLLoader.load(getClass().getResource("Leerling-Modules.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}