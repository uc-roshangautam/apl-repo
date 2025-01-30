//Simple example of memory management in C++ using new and delete operators
#include <iostream>

int main() {
    // Allocate memory on the heap
    std::cout << "Allocating memory on the heap" << std::endl;
    int* my_ptr = new int(799);
    std::cout << "Value: " << *my_ptr << std::endl;
    // Deallocate memory manually
    delete my_ptr;
    my_ptr = nullptr;
    std::cout << "Memory deallocated" << std::endl;
    return 0;
}