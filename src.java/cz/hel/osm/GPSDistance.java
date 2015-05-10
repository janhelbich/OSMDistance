package cz.hel.osm;


/**
 * Great circle distance viz http://en.wikipedia.org/wiki/Great-circle_distance
 *
 * @author Jan Helbich
 */
public class GPSDistance {

    private static final int EARTH_RADIUS = 6371;
    
    public static double getGPSDistance(GPSCoordinates c1, GPSCoordinates c2) {
    	return getGPSDistance(c1.latitude,  c1.longitude, c2.latitude, c2.longitude);
    }

    public static double getGPSDistance(double latitude1, double longitude1,
                                        double latitude2, double longitude2) {
        double rlat1 = Math.toRadians(latitude1);
        double rlat2 = Math.toRadians(latitude2);
        double rlong1 = Math.toRadians(longitude1);
        double rlong2 = Math.toRadians(longitude2);

        return getAdvancedGreatCircleDistance(rlat1, rlong1, rlat2, rlong2);
    }

    private static double getAdvancedGreatCircleDistance(double rlat1, double rlong1,
                                                         double rlat2, double rlong2) {

        double c1 = Math.pow(Math.cos(rlat2) * Math.sin(rlong2 - rlong1), 2);
        double c2 = Math.cos(rlat1) * Math.sin(rlat2);
        double c3 = Math.pow(c2 - Math.sin(rlat1) * Math.cos(rlat2) * Math.cos(rlong2 - rlong1), 2);
        double citatel = Math.sqrt(c1 + c3);

        double j1 = Math.sin(rlat1) * Math.sin(rlat2);
        double jmenovatel = j1 + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rlong2 - rlong1);

        double result = Math.atan2(citatel, jmenovatel) * EARTH_RADIUS;
        return result;
    }

}