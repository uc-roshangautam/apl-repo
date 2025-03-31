Object subclass: #Ride
    instanceVariableNames: 'rideID pickupLocation dropoffLocation distance fare'
    classVariableNames: ''
    poolDictionaries: ''
    category: 'RideSharing'.

initializeWithID: anID pickup: aPickup dropoff: aDropoff distance: aDistance
    rideID := anID.
    pickupLocation := aPickup.
    dropoffLocation := aDropoff.
    distance := aDistance.
    fare := 0.
    ^self.

calculateFare
    fare := distance * 1.5.
    ^fare.


rideDetails
    self calculateFare.  "Ensure fare is updated"
    ^String streamContents: [:stream |
         stream nextPutAll: 'Ride ID: '; nextPutAll: rideID asString; cr.
         stream nextPutAll: 'Pickup: '; nextPutAll: pickupLocation; cr.
         stream nextPutAll: 'Dropoff: '; nextPutAll: dropoffLocation; cr.
         stream nextPutAll: 'Distance: '; nextPutAll: distance asString; cr.
         stream nextPutAll: 'Fare: '; nextPutAll: fare asString].

Ride subclass: #StandardRide
    instanceVariableNames: ''
    classVariableNames: ''
    poolDictionaries: ''
    category: 'RideSharing'.

calculateFare
    fare := distance * 1.5.
    ^fare.

Ride subclass: #PremiumRide
    instanceVariableNames: ''
    classVariableNames: ''
    poolDictionaries: ''
    category: 'RideSharing'.

calculateFare
    fare := distance * 3.0.
    ^fare.

Object subclass: #Driver
    instanceVariableNames: 'driverID name rating assignedRides'
    classVariableNames: ''
    poolDictionaries: ''
    category: 'RideSharing'.

initializeWithID: anID name: aName rating: aRating
    driverID := anID.
    name := aName.
    rating := aRating.
    assignedRides := OrderedCollection new.
    ^self.

addRide: aRide
    assignedRides add: aRide.

getDriverInfo
    ^String streamContents: [:stream |
         stream nextPutAll: 'Driver ID: '; nextPutAll: driverID asString; cr.
         stream nextPutAll: 'Name: '; nextPutAll: name; cr.
         stream nextPutAll: 'Rating: '; nextPutAll: rating asString].

Object subclass: #Rider
    instanceVariableNames: 'riderID name requestedRides'
    classVariableNames: ''
    poolDictionaries: ''
    category: 'RideSharing'.

initializeWithID: anID name: aName
    riderID := anID.
    name := aName.
    requestedRides := OrderedCollection new.
    ^self.

requestRide: aRide
    requestedRides add: aRide.

viewRides
    ^requestedRides collect: [:ride | ride rideDetails].

| standardRide premiumRide rides driver rider |

"Create ride instances using the subclasses"
standardRide := StandardRide new 
    initializeWithID: 101 
    pickup: 'Location A' 
    dropoff: 'Location B' 
    distance: 10.
premiumRide := PremiumRide new 
    initializeWithID: 102 
    pickup: 'Location C' 
    dropoff: 'Location D' 
    distance: 15.

"Store rides in a collection to demonstrate polymorphism"
rides := {standardRide. premiumRide}.

"Clear the Transcript and display ride details for each ride"
Transcript clear.
Transcript show: 'Ride Details:'; cr.
rides do: [:ride | Transcript show: ride rideDetails; cr].

"Create a driver, assign rides, and display driver info"
driver := Driver new initializeWithID: 201 name: 'John Doe' rating: 4.8.
driver addRide: standardRide.
driver addRide: premiumRide.
Transcript show: 'Driver Info:'; cr.
Transcript show: driver getDriverInfo; cr.

"Create a rider, request rides, and view ride history"
rider := Rider new initializeWithID: 301 name: 'Jane Smith'.
rider requestRide: standardRide.
rider requestRide: premiumRide.
Transcript show: 'Rider''s Rides:'; cr.
rider viewRides do: [:details | Transcript show: details; cr].