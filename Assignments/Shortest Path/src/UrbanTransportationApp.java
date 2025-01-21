import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to
     * the desired destination point, taking into consideration the hyperloop train
     * network.
     *
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        List<Station> locations = new ArrayList<>();

        for (TrainLine trainLine : network.lines) locations.addAll(trainLine.trainLineStations);

        for (Station location : locations) {
            double distance = findDistance(location, network.startPoint);
            location.durationToStart = distance / network.averageWalkingSpeed;
            location.prevStation = network.startPoint;
        }
        locations.sort(Comparator.comparingDouble(s -> s.durationToStart));
        locations.add(network.destinationPoint);
        network.destinationPoint.durationToStart = findDistance(network.destinationPoint, network.startPoint) / network.averageWalkingSpeed;
        network.destinationPoint.prevStation = network.startPoint;

        for (int i = 1; i < locations.size(); i++) {
            Station current = locations.get(i);
            for (int j = 0; j < i; j++) {
                Station control = locations.get(j);

                String descriptionI = current.description.substring(0, current.description.lastIndexOf(" "));
                String descriptionJ = control.description.substring(0, control.description.lastIndexOf(" "));

                boolean sameLine = descriptionI.equals(descriptionJ);

                double distance = findDistance(current, control);
                double lineDistance = 0;

                if (sameLine) {
                    for (TrainLine trainLine : network.lines) {
                        if (trainLine.trainLineStations.contains(current)) {
                            boolean add = control.description.length() > current.description.length() || (control.description.length() == current.description.length() && control.description.compareTo(current.description) > 0);

                            int currentIndex = trainLine.trainLineStations.indexOf(current);
                            int controlIndex = trainLine.trainLineStations.indexOf(control);

                            while (currentIndex != controlIndex) {
                                int nextIndex = add ? currentIndex + 1 : currentIndex - 1;
                                lineDistance += findDistance(trainLine.trainLineStations.get(currentIndex), trainLine.trainLineStations.get(nextIndex));
                                currentIndex = nextIndex;
                            }
                        }
                    }
                }

                double potDuration = (distance / network.averageWalkingSpeed) + control.durationToStart;
                boolean walk = true;
                if (sameLine && (lineDistance / network.averageTrainSpeed + control.durationToStart) < potDuration) {
                    potDuration = lineDistance / network.averageTrainSpeed + control.durationToStart;
                    walk = false;
                }

                if (potDuration < current.durationToStart) {
                    if (sameLine && !walk) {
                        for (TrainLine trainLine : network.lines) {
                            if (trainLine.trainLineStations.contains(current)) {
                                boolean add = control.description.length() > current.description.length() || (control.description.length() == current.description.length() && control.description.compareTo(current.description) > 0);

                                int nextIndex = add ? trainLine.trainLineStations.indexOf(current) + 1 : trainLine.trainLineStations.indexOf(current) - 1;
                                current.durationToStart = potDuration;
                                current.prevStation = trainLine.trainLineStations.get(nextIndex);
                            }
                        }
                    } else {
                        current.durationToStart = potDuration;
                        current.prevStation = control;
                    }
                }
            }
        }


        Station station = network.destinationPoint;
        while (!station.equals(network.startPoint)) {
            double duration = station.durationToStart - station.prevStation.durationToStart;
            boolean trainRide = station.description.substring(0, station.description.lastIndexOf(" ")).equals(station.prevStation.description.substring(0, station.prevStation.description.lastIndexOf(" ")));
            routeDirections.add(new RouteDirection(station.prevStation.description, station.description, duration, trainRide));
            station = station.prevStation;
        }
        Collections.reverse(routeDirections);

        return routeDirections;
    }

    double findDistance(Station current, Station control) {
        int x1 = current.coordinates.x;
        int y1 = current.coordinates.y;
        int x2 = control.coordinates.x;
        int y2 = control.coordinates.y;

        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double totalDuration = directions.stream().mapToDouble(direction -> direction.duration).sum();
        System.out.println("The fastest route takes " + Math.round(totalDuration) + " minute(s).");
        System.out.println("Directions\n----------");

        for (int i = 1; i <= directions.size(); i++) {
            RouteDirection direction = directions.get(i - 1);
            String action = direction.trainRide ? "Get on the train " : "Walk ";
            System.out.println(i + ". " + action + "from \"" + direction.startStationName + "\" to \"" + direction.endStationName + "\" for " + String.format("%.2f", direction.duration) + " minutes.");
        }
    }
}