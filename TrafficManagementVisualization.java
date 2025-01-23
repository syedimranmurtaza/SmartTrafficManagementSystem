package helloworld;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TrafficManagementVisualization extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Intersections (A, B, C, D, E)
        Circle intersectionA = createIntersection(100, 100, "A");
        Circle intersectionB = createIntersection(300, 100, "B");
        Circle intersectionC = createIntersection(100, 300, "C");
        Circle intersectionD = createIntersection(300, 300, "D");
        Circle intersectionE = createIntersection(500, 200, "E");

        // Roads (Lines connecting intersections with labels)
        Line roadAB = createRoad(intersectionA, intersectionB, "A-B");
        Line roadAC = createRoad(intersectionA, intersectionC, "A-C");
        Line roadBD = createRoad(intersectionB, intersectionD, "B-D");
        Line roadCD = createRoad(intersectionC, intersectionD, "C-D");
        Line roadDE = createRoad(intersectionD, intersectionE, "D-E");
        Line roadEA = createRoad(intersectionE, intersectionA, "E-A");

        // Traffic lights
        Circle trafficLightA = createTrafficLight(100, 130);
        Circle trafficLightB = createTrafficLight(300, 130);
        Circle trafficLightC = createTrafficLight(100, 330);
        Circle trafficLightD = createTrafficLight(300, 330);
        Circle trafficLightE = createTrafficLight(500, 230);

        // Vehicles (Represented by small circles)
        Circle vehicle1 = createVehicle(100, 100, "V1", Color.BLUE);
        Circle vehicle2 = createVehicle(300, 100, "V2", Color.RED);
        Circle vehicle3 = createVehicle(500, 200, "V3", Color.GREEN);

        // Simulate vehicle movement
        animateVehicle(vehicle1, 200, 0);
        animateVehicle(vehicle2, 0, 200);
        animateVehicle(vehicle3, -400, -100);

        // Add elements to the pane
        root.getChildren().addAll(
            intersectionA, intersectionB, intersectionC, intersectionD, intersectionE,
            roadAB, roadAC, roadBD, roadCD, roadDE, roadEA,
            trafficLightA, trafficLightB, trafficLightC, trafficLightD, trafficLightE,
            vehicle1, vehicle2, vehicle3
        );

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Smart Traffic Management System Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create an intersection
    private Circle createIntersection(double x, double y, String label) {
        Circle circle = new Circle(x, y, 20, Color.LIGHTGRAY);
        Text text = new Text(x - 5, y + 5, label);
        text.setFill(Color.BLACK);
        circle.setUserData(text);
        return circle;
    }

    // Helper method to create a road (line between intersections)
    private Line createRoad(Circle from, Circle to, String label) {
        Line road = new Line(from.getCenterX(), from.getCenterY(), to.getCenterX(), to.getCenterY());
        road.setStrokeWidth(3);
        road.setStroke(Color.BLACK);
        Text roadLabel = new Text((from.getCenterX() + to.getCenterX()) / 2, 
                                  (from.getCenterY() + to.getCenterY()) / 2, label);
        roadLabel.setFill(Color.DARKGREEN);
        road.setUserData(roadLabel);
        return road;
    }

    // Helper method to create a vehicle
    private Circle createVehicle(double x, double y, String id, Color color) {
        Circle vehicle = new Circle(x + 30, y, 10, color);
        Text text = new Text(x + 20, y - 15, id);
        text.setFill(color);
        vehicle.setUserData(text);
        return vehicle;
    }

    // Helper method to create traffic lights
    private Circle createTrafficLight(double x, double y) {
        Circle trafficLight = new Circle(x, y, 10, Color.RED);
        return trafficLight;
    }

    // Method to animate vehicle movement
    private void animateVehicle(Circle vehicle, double toX, double toY) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), vehicle);
        transition.setByX(toX);
        transition.setByY(toY);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

