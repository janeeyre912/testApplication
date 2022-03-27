package com.example.testapplication;

public class Summary {

    public Summary() {
    }

    public double calculateTotalDistance(Workout workout){
        if(workout!=null) {
            double totalDistance = 0;
			double[] distance = new double[workout.getSplitList().size()];
			for (int i = 0; i < workout.getSplitList().size(); i++){
            	distance[i] = workout.getSplitList().get(i).getDistance();
//                Log.i("distance","is" + distance[i]);
                totalDistance += distance[i];
			}
            return totalDistance;
        }
        else System.out.println("Cannot calculate totalDistance because workout is null.");
        return 0;
    }

	public double calculateAverageSpeed(Workout workout) {
		if(workout != null) {
			double averageSpeed;
			double sumSpeed = 0;
			double [] splitSpeed = new double[workout.getSplitList().size()];
            for (int i = 0; i < workout.getSplitList().size(); i++){
                splitSpeed[i] = workout.getSplitList().get(i).getDistance();
                sumSpeed += splitSpeed[i];
//                Log.i("averageSpeed","is" + sumSpeed);
            }
            averageSpeed = sumSpeed/splitSpeed.length;

			return averageSpeed;
		}
		else {
			System.out.println("Error, workout is null");
			return 0;
		}
	}
}
