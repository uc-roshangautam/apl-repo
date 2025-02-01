//Simple example to demonstrate memory management in Rust
fn main() {
    let demo_box = Box::new(42);
    println!("Value: {}", demo_box);
    let owner = demo_box;
    println!("New owner value: {}", owner);
}