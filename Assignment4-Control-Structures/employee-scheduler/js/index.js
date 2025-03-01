import { collectData } from "./employeeDataStore.js";
import { createSchedule } from "./schedule.js"
import { print } from "./print.js"

function main() {
  const employeeData = collectData();
  const schedule = createSchedule(employeeData);
  print(schedule)
}

main();