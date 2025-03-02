export function shuffle(arr) {
    for (let i= arr.length - 1; i > 0; i--) {
        const k = Math.floor(Math.random() * (i + 1));
        [arr[i], arr[k]] = [arr[k], arr[i]];
    }
    return arr;
}