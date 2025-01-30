echo "_______________Memory management demo using Java, C++ and Rust_______________"
echo
echo "--------------Running java program--------------"
javac MMExample.java
java MMExample
echo "--------------Running c++ program--------------"
g++ MMExample.cpp -o MMExampleCPP
./MMExampleCPP
echo "--------------Running rust program--------------"
rustc MMExample.rs
./MMExample
echo "--------------Cleaning output files--------------"
rm MMExample.class MMExampleCPP MMExample
echo
echo "___________________________[ Execution complete ]___________________________"