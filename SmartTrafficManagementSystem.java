package SmartTrafficManagementSystem;

import java.util.*;

public class SmartTrafficManagementSystem {

    private final Map<String, List<Road>> graph;
    private final Map<String, Queue<Vehicle>> vehicleQueues;
    private final Map<String, String> trafficLights;
    private final PriorityQueue<Vehicle> emergencyQueue;

    public SmartTrafficManagementSystem() {
        graph = new HashMap<>();
        vehicleQueues = new HashMap<>();
        trafficLights = new HashMap<>();
        emergencyQueue = new PriorityQueue<>(Comparator.comparingInt(Vehicle::getPriority).reversed());
    }

    // Add a road between intersections
    public void addRoad(String from, String to, int travelTime) {
        graph.putIfAbsent(from, new ArrayList<>());
        graph.putIfAbsent(to, new ArrayList<>());
        graph.get(from).add(new Road(to, travelTime));
    }

    // Add a vehicle to a specific intersection
    public void addVehicle(String intersection, Vehicle vehicle) {
        vehicleQueues.putIfAbsent(intersection, new LinkedList<>());
        vehicleQueues.get(intersection).offer(vehicle);
    }

    // Simulate traffic flow at a given intersection
    public void simulateTrafficFlow(String intersection) {
        Queue<Vehicle> queue = vehicleQueues.get(intersection);
        if (queue == null || queue.isEmpty()) {
            System.out.println("No vehicles at intersection " + intersection + "\n");
            return;
        }

        System.out.println("Simulating traffic flow at intersection " + intersection);
        while (!queue.isEmpty()) {
            Vehicle vehicle = queue.poll();
            String lightStatus = trafficLights.getOrDefault(intersection, "Green");

            if (lightStatus.equals("Red")) {
                System.out.println("Traffic light at " + intersection + " is Red. Vehicle " + vehicle + " is waiting.");
                queue.offer(vehicle); // Put the vehicle back in queue for another attempt
                return;
            } else if (lightStatus.equals("Yellow")) {
                System.out.println("Traffic light at " + intersection + " is Yellow. Vehicle " + vehicle + " is preparing to move.");
                travelToNextIntersection(vehicle, intersection);
            } else {
                System.out.println("Traffic light at " + intersection + " is Green. Vehicle " + vehicle + " is proceeding.");
                travelToNextIntersection(vehicle, intersection);
            }
        }
    }

    // Travel to the next intersection and handle vehicle prioritization
    private void travelToNextIntersection(Vehicle vehicle, String currentIntersection) {
        List<Road> roads = graph.get(currentIntersection);
        if (roads == null || roads.isEmpty()) return;

        Road nextRoad = roads.get(0); // For simplicity, selecting the first road (can be optimized)
        int travelTime = nextRoad.travelTime;

        if (vehicle.isEmergency()) {
            travelTime /= 2; // Emergency vehicles travel faster
            System.out.println(vehicle.getType() + " (Emergency) is traveling faster from " + currentIntersection + " to " + nextRoad.to + " (Travel time: " + travelTime + " minutes)");
        } else {
            System.out.println(vehicle.getType() + " (Regular) traveling from " + currentIntersection + " to " + nextRoad.to + " (Travel time: " + travelTime + " minutes)");
        }

        addVehicle(nextRoad.to, vehicle);
    }

    // Change traffic light status
    public void changeTrafficLight(String intersection) {
        String currentStatus = trafficLights.getOrDefault(intersection, "Green");
        String newStatus = switch (currentStatus) {
            case "Green" -> "Yellow";
            case "Yellow" -> "Red";
            default -> "Green";
        };

        trafficLights.put(intersection, newStatus);
        System.out.println("Traffic light at " + intersection + " changed to " + newStatus + "\n");
    }

    // Handle emergency vehicles using a priority queue
    public void processEmergencyVehicles() {
        while (!emergencyQueue.isEmpty()) {
            Vehicle emergencyVehicle = emergencyQueue.poll();
            System.out.println("Processing emergency vehicle: " + emergencyVehicle + " with priority.");
            // Process vehicle as needed, similar to simulateTrafficFlow
        }
    }

    // Class for representing roads between intersections
    private static class Road {
        String to;
        int travelTime;

        Road(String to, int travelTime) {
            this.to = to;
            this.travelTime = travelTime;
        }
    }

    // Class for representing vehicles
    public static class Vehicle {
        private final String type;  // Type of vehicle (e.g., Emergency or Regular)
        private final String id;    // Unique vehicle ID
        private final String origin;
        private final String destination;

        public Vehicle(String type, String id, String origin, String destination) {
            this.type = type;
            this.id = id;
            this.origin = origin;
            this.destination = destination;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getOrigin() {
            return origin;
        }

        public String getDestination() {
            return destination;
        }

        public boolean isEmergency() {
            return "Emergency".equalsIgnoreCase(type);
        }

        public int getPriority() {
            return isEmergency() ? 1 : 2; // Emergency vehicles have higher priority
        }

        @Override
        public String toString() {
            return type + " Vehicle " + id + " (From " + origin + " to " + destination + ")";
        }
    }

    public static void main(String[] args) {
        SmartTrafficManagementSystem stms = new SmartTrafficManagementSystem();

        // Initialize the city layout (graph of intersections and roads)
        stms.addRoad("A", "B", 2);
        stms.addRoad("A", "C", 3);
        stms.addRoad("B", "D", 4);
        stms.addRoad("C", "D", 1);
        stms.addRoad("D", "E", 5);
        stms.addRoad("E", "A", 6);

        // Add vehicles to intersections
        stms.addVehicle("A", new Vehicle("Regular", "V1", "A", "D"));
        stms.addVehicle("A", new Vehicle("Emergency", "V2", "A", "E"));
        stms.addVehicle("B", new Vehicle("Regular", "V3", "B", "C"));
        stms.addVehicle("C", new Vehicle("Emergency", "V4", "C", "A"));

        // Simulate traffic flow and handle emergency vehicles
        stms.simulateTrafficFlow("A");
        stms.changeTrafficLight("A");
        stms.simulateTrafficFlow("A");
        stms.processEmergencyVehicles();  // Process emergencies
        stms.changeTrafficLight("B");
        stms.simulateTrafficFlow("B");
        stms.simulateTrafficFlow("C");
        stms.changeTrafficLight("C");
    }
}
