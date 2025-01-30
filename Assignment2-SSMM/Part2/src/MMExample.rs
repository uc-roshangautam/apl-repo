fn main() {
    let demo_box = Box::new(42); // Heap allocation using Box<T>
    println!("Value: {}", demo_box);

    // Ownership transfer
    let owner = demo_box; // Ownership moves to owner
    // println!("{}", box); // This would cause a compile-time error (use after move)

    println!("New owner value: {}", owner); // Access is safe

    // Memory is automatically freed when `owner` goes out of scope
} // Rust deallocates memory automatically here