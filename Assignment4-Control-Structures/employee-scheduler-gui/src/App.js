import React from "react";
import { Navbar, Container } from "react-bootstrap";
import Schedule from "./Schedule";

function App() {
  return (
    <>
      <Navbar bg="dark" variant="dark" expand="md">
        <Container>
          <Navbar.Brand href="#">Employee Shift Scheduler</Navbar.Brand>
        </Container>
      </Navbar>
      <div className="container my-4">
        <Schedule />
      </div>
    </>
  );
}

export default App;