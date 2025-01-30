//Simple javaScript program to demonstrate the scope of variables

let gblVar = "Hello from global";

function demo() {
    let lclVar = "Hello from local";
    console.log("In demo - " + lclVar);
    console.log("In demo - " + gblVar);
}

demo();
console.log("Outside - " + gblVar);

function outer() {
    let outVar = "Hello from outer";
    function inner() {
        console.log("In inner - " + outVar);
    }
    return inner;
}

let closure = outer();
closure();