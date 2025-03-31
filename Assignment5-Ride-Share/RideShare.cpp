#include <iostream>
#include <string>
#include <vector>

using namespace std;

class Ride {
protected:
    int rideID;
    string pickupLocation;
    string dropoffLocation;
    double distance;
    double baseFare;
public:
    Ride(int id, const string& pickup, const string& dropoff, double dist)
        : rideID(id), pickupLocation(pickup), dropoffLocation(dropoff),
          distance(dist), baseFare(2.0) {}
    virtual ~Ride() {}

    virtual double fare() const = 0;
    
    virtual void rideDetails() const {
        cout << "Ride " << rideID << ": " 
             << pickupLocation << " -> " << dropoffLocation 
             << ", " << distance << " mi, $" << fare() << "\n";
    }
};

class StandardRide : public Ride {
    double ratePerMile;
public:
    StandardRide(int id, const string& pickup, const string& dropoff, double dist)
        : Ride(id, pickup, dropoff, dist), ratePerMile(1.5) {}
    
    double fare() const {
        return baseFare + (distance * ratePerMile);
    }
    
    void rideDetails() const {
        cout << "Standard ";
        Ride::rideDetails();
    }
};

class PremiumRide : public Ride {
    double ratePerMile;
    double luxuryFee;
    bool priorityPickup;
public:
    PremiumRide(int id, const string& pickup, const string& dropoff, double dist, bool priority = false)
        : Ride(id, pickup, dropoff, dist), ratePerMile(2.5), luxuryFee(10.0), priorityPickup(priority) {}
    
    double fare() const {
        double totalFare = baseFare + (distance * ratePerMile) + luxuryFee;
        if (priorityPickup) {
            totalFare += 5.0;
        }
        return totalFare;
    }
    
    void rideDetails() const {
        cout << "Premium ";
        Ride::rideDetails();
    }
};

class Driver {
    int driverID;
    string name;
    double rating;
    vector<Ride*> assignedRides;
public:
    Driver(int id, const string& driverName, double initialRating)
        : driverID(id), name(driverName), rating(initialRating) {}
    
    void addRide(Ride* ride) {
        assignedRides.push_back(ride);
    }
    
    void getDriverInfo() const {
        cout << "Driver " << driverID << ": " << name << ", Rating: " << rating 
             << ", Rides: " << assignedRides.size() << "\n";
        for (int i = 0; i < assignedRides.size(); i++) {
            assignedRides[i]->rideDetails();
        }
    }
};

class Rider {
    int riderID;
    string name;
    vector<Ride*> requestedRides;
public:
    Rider(int id, const string& riderName) : riderID(id), name(riderName) {}
    
    void requestRide(Ride* ride) {
        requestedRides.push_back(ride);
    }
    
    void viewRides() const {
        cout << "Rider " << riderID << ": " << name << ", Rides: " 
             << requestedRides.size() << "\n";
        for (int i = 0; i < requestedRides.size(); i++) {
            requestedRides[i]->rideDetails();
        }
    }
};

void demoPolymorphism(const vector<Ride*>& rides) {
    cout << "Polymorphism Demo:\n";
    for (int i = 0; i < rides.size(); i++) {
        rides[i]->rideDetails();
        cout << "Direct fare: $" << rides[i]->fare() << "\n";
    }
}

int main() {
    StandardRide* ride1 = new StandardRide(101, "123 East", "456 Oak", 5.2);
    PremiumRide* ride2 = new PremiumRide(102, "194 Pine", "321 Elm", 3.8, true);
    
    vector<Ride*> rides;
    rides.push_back(ride1);
    rides.push_back(ride2);
    
    cout << "Polymorphism Demo:\n";
    for (int i = 0; i < rides.size(); i++) {
        rides[i]->rideDetails();
        cout << "Direct fare: $" << rides[i]->fare() << "\n";
    }
    
    Driver driver1(201, "John", 4.8);
    driver1.addRide(ride1);
    driver1.addRide(ride2);
    
    Rider rider1(301, "Jane");
    rider1.requestRide(ride1);
    rider1.requestRide(ride2);
    
    cout << "\nDriver Info:\n";
    driver1.getDriverInfo();
    
    cout << "\nRider Info:\n";
    rider1.viewRides();
    
    delete ride1;
    delete ride2;
    return 0;
}