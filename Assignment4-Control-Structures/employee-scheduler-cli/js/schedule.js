import { shuffle } from "./suffle.js"
//Function to initialize an emtpy schedule.
export function initSchedule() {
    const days =  ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    const shifts = ["morning", "afternoon", "evening"];
    const schedule = {};
    for (const d of days) {
        schedule[d] = {};
        for (const s of shifts) {
            schedule[d][s]=[];
        }
    }
    return schedule;
}

export function createSchedule(data) {
    const schedule = initSchedule();
    const daysTracker = {};
    //Initialize with 0
    for (const e of Object.keys(data)) {
        daysTracker[e] = 0;
    }
    const days = Object.keys(schedule);
    const shifts = ["morning", "afternoon", "evening"];

    for (const d of days) {
        const employees = Object.keys(data);
        //Shuffle the employees so that the next schedule changes for fairness.
        shuffle(employees);
        for (const e of employees) {
            //Checking if employee has this day available.
            if (!data[e].hasOwnProperty(d)) {
                continue;
            }
            //Cannot work more than 5 days.
            if (daysTracker[e] >= 5) {
                continue;
            }

            const desiredShifts = data[e][d];
            let assigned = false;


            //Assigning desired shift
            for (const dShift of desiredShifts) {
                if (schedule[d][dShift].length < 2) {
                    schedule[d][dShift].push(e);
                    daysTracker[e] += 1;
                    assigned = true;
                    break;
                }
            }

            //If employee is not assigned, trying other shifts same day.
            if (!assigned) {
                for (const s of shifts) {
                    if (schedule[d][s].length < 2 && daysTracker[e] < 5) {
                        schedule[d][s].push(e);
                        daysTracker[e] += 1;
                        assigned = true;
                        break;
                    }
                }
            }
            //assingn in next day if still not assigned
            if (!assigned) {
                const currentDay = days.indexOf(d);
                const nextDayIdx = currentDay + 1;
                if (nextDayIdx < days.length) {
                    const nextDay = days[nextDayIdx];
                    for (const s of shifts) {
                        if (schedule[nextDay][s].length < 2 && daysTracker[e] < 5) {
                            schedule[nextDay][s].push(e);
                            daysTracker[e] += 1;
                            break;
                        }
                    }
                }
            }
        }
    }

    //Ensuring each shift has two employees.
    for (const d of days) {
        for (const s of shifts) {
            while (schedule[d][s].length < 2) {
                const otherEmployees = Object.keys(data).filter((emp) => {
                    return daysTracker[emp] < 5 && !schedule[d][s].includes(emp);
                });
                if (otherEmployees.lenth === 0) {
                    break;
                }
                const choose = otherEmployees[Math.floor(Math.random() * otherEmployees.length)];
                schedule[d][s].push(choose);
                daysTracker[choose] += 1;
            }
        }
    }
    return schedule
}