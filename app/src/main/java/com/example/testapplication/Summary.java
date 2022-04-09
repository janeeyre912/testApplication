package com.example.testapplication;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Summary {

    public Summary() {
    }

    private final static String GENDER_FEMALE = "female";
    public double calculateTotalDistance(Workout workout) {
        if (workout != null) {
            double totalDistance = 0;
            double[] distance = new double[workout.getSplitList().size()];
            for (int i = 0; i < workout.getSplitList().size(); i++) {
                distance[i] = workout.getSplitList().get(i).getDistance();
//                Log.i("distance","is" + distance[i]);
                totalDistance += distance[i];
            }
            return totalDistance;
        } else System.out.println("Cannot calculate totalDistance because workout is null.");
        return 0;
    }

    public double calculateAverageSpeed(Workout workout) {
        if (workout != null) {
            double averageSpeed;
            double sumSpeed = 0;
            double[] splitSpeed = new double[workout.getSplitList().size()];
            for (int i = 0; i < workout.getSplitList().size(); i++) {
                splitSpeed[i] = workout.getSplitList().get(i).getAverageSpeed();
                sumSpeed += splitSpeed[i];
//                Log.i("averageSpeed","is" + sumSpeed);
            }
            averageSpeed = sumSpeed / splitSpeed.length;

            return averageSpeed;
        } else {
            System.out.println("Error, workout is null");
            return 0;
        }
    }

    /**
     * The METS values are provided by "The Compendium of Physical Activities 2011".
     * Calorie(Kcal) ＝ BMR x Mets / 24 x hour　
     *
     * @param user
     * @param workout
     * @return The calories burns.
     */

    public double calculateCalories(User user, Workout workout) throws ParseException {
        if (workout != null) {
            double totalCalories = 0;
            double splitSpeed = 0;
            double[] splitCalories = new double[workout.getSplitList().size()];
            for (int i = 0; i < workout.getSplitList().size(); i++) {
                splitSpeed = workout.getSplitList().get(i).getDistance();
                splitCalories[i] = getBMR(user) * getMetForActivity(convertKphToMph(splitSpeed)) / 24;
                totalCalories += splitCalories[i];
            }
            return totalCalories;
        } else {
            System.out.println("Error, workout is null");
            return 0;
        }

    }

    public static float convertKphToMph(double kph){
        return (float) ((float)kph * 0.62);
    }

    /**
     * Gets the MET value for an activity. Based on https://sites.google.com/site/compendiumofphysicalactivities/Activity-Categories/bicycling .
     *
     * @param speedInMph The speed in miles per hour
     * @return The met value.
     */
    private static float getMetForActivity(float speedInMph) {
        if (speedInMph < 9.4) {
            return 4.0f;
        } else if (Float.compare(speedInMph, 9.4f) == 0) {
            return 5.8f;
        } else if (Float.compare(speedInMph, 10.0f) >= 0 && Float.compare(speedInMph, 11.9f) <= 0) {
            return 6.8f;
        } else if (Float.compare(speedInMph, 12.0f) >= 0 && Float.compare(speedInMph, 13.9f) <= 0) {
            return 8.0f;
        } else if (Float.compare(speedInMph, 14.0f) >= 0 && Float.compare(speedInMph, 15.9f) <= 0) {
            return 10.0f;
        } else if (Float.compare(speedInMph, 16.0f) >= 0 && Float.compare(speedInMph, 19.0f) <= 0) {
            return 12.0f;
        } else if (Float.compare(speedInMph, 20.0f) > 0) {
            return 15.8f;
        }
        return 0;
    }

    /**
     * Calculates the BMR value for an entity. Based on above calculation for Com
     *
     * @param user
     * @return BMR value.
     */
    private static float getBMR(User user) throws ParseException {
        String  gender = user.getGender();
        double weightKg = user.getWeight();

        Date bornDate = new SimpleDateFormat("dd/MM/yyyy").parse(user.getDob());
        Calendar currentDate = Calendar.getInstance();
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(bornDate);

        float age = (float) currentDate.get(Calendar.YEAR) -dateOfBirth.get(Calendar.YEAR);


        if (gender.equals(GENDER_FEMALE))) {
            return (float) (655.0955f + (1.8496f * 165) + (9.5634f * weightKg) - (4.6756f * age));
        } else {
            return (float) (66.4730f + (5.0033f * 175) + (13.7516f * weightKg) - (6.7550f * age));
        }
    }
   /* Calculate the MET "metabolic equivalents"
    @reutrn  met 
   */
   
	 private static double getMet(double weight) {
			double met = getWeight()*3.5;
			return met;
		}
     /*Calculate the total burnt calories during the duration of the cycling training
    * reference: https://codereview.stackexchange.com/questions/62371/calculating-total-number-of-calories-burned
    * @param totalCalories
    */
	public void calculateBurntCalories (Workout workout){
		totalCalories = Workout.caloiresPerMunite()*hoursToMinutes();
		
	}
    /*Calculate the total burnt calories per minute
    * reference: https://codereview.stackexchange.com/questions/62371/calculating-total-number-of-calories-burned
    */
	 public static double caloriesPerMinute( double weight, double time) {
	       
			return value * getMet(weight) * poundToKilogram(weight) * time;
	    }

	   /*
       convert pound to kilogram method 
       */
		public static double poundToKilogram(double pound) {
	        return pound / 2.2;
	    }
    /*
       convert hours to minutes method 
       */

	    public static double hoursToMinutes(double hours) {
	        return hours * 60;
	    }
}
