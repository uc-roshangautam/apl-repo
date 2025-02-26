import { collectData } from "./employeeDataStore.js";

function main() {
  const employeeData = collectData();
  console.log(employeeData);
}

main();