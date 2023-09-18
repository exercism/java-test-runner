public class Lasagna {
    public int expectedMinutesInOven() {
        return 40;
    }

    public int remainingMinutesInOven(int actualMinutesInOven) {
        return expectedMinutesInOven() - actualMinutesInOven;
    }

    public int preparationTimeInMinutes(int numberOfLayers) {
        return numberOfLayers * 2;
    }

    public int totalTimeInMinutes(int numberOfLayers, int actualMinutesInOven) {
        var preparationTimeInMinutes = preparationTimeInMinutes(numberOfLayers);
        System.out.println("Preparation time: " + preparationTimeInMinutes);
        return preparationTimeInMinutes + actualMinutesInOven;
    }
}