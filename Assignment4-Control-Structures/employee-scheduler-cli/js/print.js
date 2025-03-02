export function print(schedule){
    for (const d of Object.keys(schedule)) {
        console.log(`===================================================`)
        console.log(`                    ${d}`)
        console.log(`===================================================`)
        const shifts = schedule[d];
        for (const shift of Object.keys(shifts)) {
            const emps = shifts[shift];
            const empStr = emps.length > 0 ? emps.join(", ") : "No one";
            console.log(`  ${shift.charAt(0).toUpperCase() + shift.slice(1)} - ${empStr}`);
        }
        console.log("");
    }
}