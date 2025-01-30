//Simple c++ program to demonstrate the scope of variables

#include <iostream>
using namespace std;

string gblVar = "Hello from global";

void demo() {
    string lclVar = "Hello from local";
    cout << "In demo - " + lclVar << endl;
    cout << "In demo - " + gblVar << endl;
}

int main() {
    demo();
    cout << "In main - " + gblVar << endl;
    return 0;
}