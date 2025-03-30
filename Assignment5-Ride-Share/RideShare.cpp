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
        : rideID(id), pickupLocation(pickup), dropoffLocation(dropoff), distance(dist), baseFare(2.0) {
    }

    virtual ~Ride() {}

    int getRideID() const { return rideID; }
    string getPickupLocation() const { return pickupLocation; }
    string getDropoffLocation() const { return dropoffLocation; }
    double getDistance() const { return distance; }
    
    virtual double fare() const = 0;
    
    virtual void rideDetails() const {
        cout << "Ride ID: " << rideID << endl;
        cout << "Pickup: " << pickupLocation << endl;
        cout << "Dropoff: " << dropoffLocation << endl;
        cout << "Distance: " << distance << " miles" << endl;
        cout << "Fare: $" << fare() << endl;
    }
};

// StandardRide class
class StandardRide : public Ride {
private:
    double ratePerMile;

public:
    StandardRide(int id, const string& pickup, const string& dropoff, double dist)
        : Ride(id, pickup, dropoff, dist), ratePerMile(1.5) {
    }
    
    double fare() const {
        return baseFare + (distance * ratePerMile);
    }
    
    void rideDetails() const {
        cout << ".....Standard Ride....." << endl;
        Ride::rideDetails();
        cout << "Rate per mile: $" << ratePerMile << endl;
    }
};

// PremiumRide class
class PremiumRide : public Ride {
private:
    double ratePerMile;
    double luxuryFee;
    bool priorityPickup;

public:
    PremiumRide(int id, const string& pickup, const string& dropoff, double dist, bool priority = false)
        : Ride(id, pickup, dropoff, dist), ratePerMile(2.5), luxuryFee(10.0), priorityPickup(priority) {
    }
    
    double fare() const {
        double totalFare = baseFare + (distance * ratePerMile) + luxuryFee;
        if (priorityPickup) {
            totalFare += 5.0;
        }
        return totalFare;
    }
    
    void rideDetails() const {
        cout << ".....Premium Ride....." << endl;
        Ride::rideDetails();
        cout << "Rate per mile: $" << ratePerMile << endl;
        cout << "Luxury fee: $" << luxuryFee << endl;
        cout << "Priority pickup: " << (priorityPickup ? "Yes" : "No") << endl;
    }
};

// Driver class
class Driver {
private:
    int driverID;
    string name;
    double rating;
    vector<Ride*> assignedRides;

public:
    Driver(int id, const string& driverName, double initialRating = 5.0)
        : driverID(id), name(driverName), rating(initialRating) {
    }
    
    ~Driver() {
    }
    
    void addRide(Ride* ride) {
        assignedRides.push_back(ride);
    }
    
    void getDriverInfo() const {
        cout << ".....Driver Info....." << endl;
        cout << "Driver ID: " << driverID << endl;
        cout << "Name: " << name << endl;
        cout << "Rating: " << rating << " stars" << endl;
        cout << "Total Rides: " << assignedRides.size() << endl;
        cout << "Total Earnings: $" << getTotalEarnings() << endl;
        
        if (!assignedRides.empty()) {
            cout << "\n.....Ride History....." << endl;
            for (int i = 0; i < (int)assignedRides.size(); ++i) {
                cout << "\nRide " << (i + 1) << ":" << endl;
                assignedRides[i]->rideDetails();
            }
        }
    }
    
    double getTotalEarnings() const {
        double total = 0.0;
        for (int i = 0; i < (int)assignedRides.size(); ++i) {
            total += assignedRides[i]->fare();
        }
        return total;
    }
    
    int getDriverID() const { return driverID; }
    string getName() const { return name; }
    double getRating() const { return rating; }
    
    void setRating(double newRating) {
        if (newRating >= 0.0 && newRating <= 5.0) {
            rating = newRating;
        } else {
            cout << "Invalid. Rating must be between 0 and 5." << endl;
        }
    }
};

class Rider {
private:
    int riderID;
    string name;
    vector<Ride*> requestedRides;

public:
    Rider(int id, const string& riderName)
        : riderID(id), name(riderName) {
    }
    
    ~Rider() {
    }
    
    void requestRide(Ride* ride) {
        requestedRides.push_back(ride);
    }
    
    void viewRides() const {
        cout << ".....Rider Information....." << endl;
        cout << "Rider ID: " << riderID << endl;
        cout << "Name: " << name << endl;
        cout << "Total Rides: " << requestedRides.size() << endl;
        cout << "Total Spent: $" << getTotalSpent() << endl;
        
        if (!requestedRides.empty()) {
            cout << "\nRide History:" << endl;
            for (int i = 0; i < (int)requestedRides.size(); ++i) {
                cout << "\nRide " << (i + 1) << ":" << endl;
                requestedRides[i]->rideDetails();
            }
        }
    }
    
    double getTotalSpent() const {
        double total = 0.0;
        for (int i = 0; i < (int)requestedRides.size(); ++i) {
            total += requestedRides[i]->fare();
        }
        return total;
    }
    
    int getRiderID() const { return riderID; }
    string getName() const { return name; }
};

void demoPolymorphism(const vector<Ride*>& rides) {
    cout << "Demonstrating Polymorphism" << endl;
    cout << "Calling rideDetails() and fare() on different ride types:" << endl;
    
    for (int i = 0; i < (int)rides.size(); ++i) {
        cout << "\nPolymorphic calls " << (i + 1) << ":" << endl;
        rides[i]->rideDetails();
        
        cout << "Direct fare calculation: $" << rides[i]->fare() << endl;
    }
}

int main() {
    StandardRide* ride1 = new StandardRide(101, "123 East Street", "456 Oak Avenue", 5.2);
    PremiumRide* ride2 = new PremiumRide(102, "194 Pine Street", "321 Elm Street", 3.8, true);
    StandardRide* ride3 = new StandardRide(103, "111 Cedar Lane", "897 Maple Drive", 8.1);
    PremiumRide* ride4 = new PremiumRide(104, "124 Birch Road", "121 Spruce Court", 6.5, false);
    
    Driver driver1(201, "John Tiger");
    
    driver1.addRide(ride1);
    driver1.addRide(ride2);
    
    Rider rider1(301, "Emily Heartworth");
    
    rider1.requestRide(ride1);
    rider1.requestRide(ride2);
    rider1.requestRide(ride3);
    
    cout << "\n";
    driver1.getDriverInfo();
    
    cout << "\n";
    rider1.viewRides();
    
    Driver driver2(202, "Michael Jackson");
    driver2.addRide(ride3);
    driver2.addRide(ride4);
    
    Rider rider2(302, "Sarah Lee");
    rider2.requestRide(ride4);
    
    cout << "\n";
    driver2.getDriverInfo();
    
    cout << "\n";
    rider2.viewRides();
    
    vector<Ride*> allRides;
    allRides.push_back(ride1);
    allRides.push_back(ride2);
    allRides.push_back(ride3);
    allRides.push_back(ride4);
    
    cout << "\n";
    demoPolymorphism(allRides);
    
    delete ride1;
    delete ride2;
    delete ride3;
    delete ride4;
    return 0;
}