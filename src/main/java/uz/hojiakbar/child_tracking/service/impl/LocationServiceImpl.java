package uz.hojiakbar.child_tracking.service.impl;

import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371e3; /// yer radiusi ekan
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                   Math.cos(phi1) * Math.cos(phi2) *
                   Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
