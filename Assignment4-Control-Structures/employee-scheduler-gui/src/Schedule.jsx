import React, { useState } from "react";
import { sampleData } from "./sampleData";   
import { shuffle } from "./suffle";

import { Button, Form, Card } from "react-bootstrap";

function initSchedule() {
  const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  const shifts = ["morning", "afternoon", "evening"];
  const schedule = {};

  for (const d of days) {
    schedule[d] = {};
    for (const s of shifts) {
      schedule[d][s] = [];
    }
  }
  return schedule;
}

function createSchedule(data) {
  const schedule = initSchedule();
  const days = Object.keys(schedule);
  const shifts = ["morning", "afternoon", "evening"];
  
  const daysAssigned = {};
  for (const e of Object.keys(data)) {
    daysAssigned[e] = new Set();  
  }
  
  for (const d of days) {
    const employees = Object.keys(data);
    shuffle(employees);

    for (const e of employees) {
      if (daysAssigned[e].has(d)) continue;
      if (daysAssigned[e].size >= 5) continue;
      if (!data[e].hasOwnProperty(d)) continue;

      let desiredShifts = data[e][d];
      if (desiredShifts.length > 0 && typeof desiredShifts[0] === "object") {
        desiredShifts = [...desiredShifts].sort((a, b) => a.priority - b.priority);
        desiredShifts = desiredShifts.map(obj => obj.shift);
      }

      let assigned = false;

      for (const prefShift of desiredShifts) {
        if (schedule[d][prefShift].length < 2) {
          schedule[d][prefShift].push(e);
          daysAssigned[e].add(d);
          assigned = true;
          break;
        }
      }

      if (!assigned) {
        for (const s of shifts) {
          if (schedule[d][s].length < 2 && !daysAssigned[e].has(d) && daysAssigned[e].size < 5) {
            schedule[d][s].push(e);
            daysAssigned[e].add(d);
            assigned = true;
            break;
          }
        }
      }

      if (!assigned) {
        const currentIndex = days.indexOf(d);
        const nextIndex = currentIndex + 1;
        if (nextIndex < days.length) {
          const nextDay = days[nextIndex];
          if (!daysAssigned[e].has(nextDay) && daysAssigned[e].size < 5) {
            for (const s of shifts) {
              if (schedule[nextDay][s].length < 2) {
                schedule[nextDay][s].push(e);
                daysAssigned[e].add(nextDay);
                break;
              }
            }
          }
        }
      }
    }
  }

  for (const d of days) {
    for (const s of shifts) {
      while (schedule[d][s].length < 2) {
        const candidates = Object.keys(data).filter(e => {
          return daysAssigned[e].size < 5 && !daysAssigned[e].has(d);
        });
        if (candidates.length === 0) {
          break; 
        }
        const chosen = candidates[Math.floor(Math.random() * candidates.length)];
        schedule[d][s].push(chosen);
        daysAssigned[chosen].add(d);
      }
    }
  }
  return schedule;
}

function ScheduleDisplay({ schedule }) {
  return (
    <div className="mt-4">
      <h4>Schedule</h4>
      {Object.keys(schedule).map(day => (
        <Card key={day} className="mb-3">
          <Card.Header style={{ fontWeight: "bold" }}>{day}</Card.Header>
          <Card.Body>
            {Object.keys(schedule[day]).map(shift => (
              <p key={shift} style={{ margin: 0 }}>
                <strong>{shift}:</strong>{" "}
                {schedule[day][shift].length
                  ? schedule[day][shift].join(", ")
                  : "No one assigned"}
              </p>
            ))}
          </Card.Body>
        </Card>
      ))}
    </div>
  );
}

export default function Schedule() {
  const [approach, setApproach] = useState("");
  const [finalSchedule, setFinalSchedule] = useState(null);

  const [employeeData] = useState(sampleData);
  const [customPreferences, setCustomPreferences] = useState([
    { name: "", day: "Monday", shift: "morning", priority: 1 }
  ]);

  function handleSelectSample() {
    setApproach("sample");
    setFinalSchedule(null);
  }

  function handleSelectCustom() {
    setApproach("custom");
    setFinalSchedule(null);
  }

  function handleGenerateSample() {
    const schedule = createSchedule(employeeData);
    setFinalSchedule(schedule);
  }

  function handleGenerateForm() {
    const data = buildDataStructure(customPreferences);
    const schedule = createSchedule(data);
    setFinalSchedule(schedule);
  }

  function buildDataStructure(preferences) {
    const result = {};
    for (const pref of preferences) {
      const { name, day, shift, priority } = pref;
      if (!name) continue;
      if (!result[name]) {
        result[name] = {};
      }
      if (!result[name][day]) {
        result[name][day] = [];
      }
      result[name][day].push({ shift, priority: Number(priority) });
    }
    return result;
  }

  function handleAddRow() {
    setCustomPreferences(prev => [
      ...prev,
      { name: "", day: "Monday", shift: "morning", priority: 1 }
    ]);
  }

  function handleRemoveRow(index) {
    setCustomPreferences(prev => {
      const updated = [...prev];
      updated.splice(index, 1);
      return updated;
    });
  }

  function handleChangeRow(index, field, value) {
    setCustomPreferences(prev => {
      const updated = [...prev];
      updated[index] = { ...updated[index], [field]: value };
      return updated;
    });
  }

  return (
    <div className="container my-4">
      {approach === "" && (
        <p>Please select one of the options.</p>
      )}
      <div className="mb-4">
        <Button 
          variant="primary" 
          onClick={handleSelectSample} 
          className="me-2"
        >
          Random employee schedule
        </Button>
        <Button 
          variant="success" 
          onClick={handleSelectCustom}
        >
          Create custom employee schedule
        </Button>
      </div>

      {/* SAMPLE Approach */}
      {approach === "sample" && (
        <Card className="p-3">
          <h3>Random Employee Scheduler</h3>
          <Button 
            variant="warning" 
            className="mt-3"
            onClick={handleGenerateSample}
          >
            Generate Random Schedule (9 employees)
          </Button>

          {finalSchedule && <ScheduleDisplay schedule={finalSchedule} />}
        </Card>
      )}

      {/* CUSTOM Approach */}
      {approach === "custom" && (
        <Card className="p-3">
          <h3>Custom Employee Schedule</h3>
          <p className="text-muted">
            Add rows for each employee preferences (name - day - shift - priority)
          </p>

          {customPreferences.map((row, idx) => (
            <div key={idx} className="d-flex mb-2 align-items-center">
              <Form.Control
                type="text"
                placeholder="Employee Name"
                value={row.name}
                onChange={e => handleChangeRow(idx, "name", e.target.value)}
                className="me-2"
                style={{ width: "150px" }}
              />
              <Form.Select
                value={row.day}
                onChange={e => handleChangeRow(idx, "day", e.target.value)}
                className="me-2"
                style={{ width: "120px" }}
              >
                <option>Sunday</option>
                <option>Monday</option>
                <option>Tuesday</option>
                <option>Wednesday</option>
                <option>Thursday</option>
                <option>Friday</option>
                <option>Saturday</option>
              </Form.Select>
              <Form.Select
                value={row.shift}
                onChange={e => handleChangeRow(idx, "shift", e.target.value)}
                className="me-2"
                style={{ width: "120px" }}
              >
                <option>morning</option>
                <option>afternoon</option>
                <option>evening</option>
              </Form.Select>
              <Form.Control
                type="number"
                min={1}
                max={5}
                value={row.priority}
                onChange={e => handleChangeRow(idx, "priority", e.target.value)}
                className="me-2"
                style={{ width: "70px" }}
              />
              <Button
                variant="outline-danger"
                onClick={() => handleRemoveRow(idx)}
              >
                Remove
              </Button>
            </div>
          ))}

          <div className="mb-3">
            <Button variant="outline-secondary" onClick={handleAddRow}>
              Add Row
            </Button>
          </div>

          <Button 
            variant="primary"
            onClick={handleGenerateForm}
          >
            Generate Schedule
          </Button>

          {finalSchedule && <ScheduleDisplay schedule={finalSchedule} />}
        </Card>
      )}
    </div>
  );
}