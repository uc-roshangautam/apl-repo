let mean lst =
  let sum = List.fold_left (+) 0 lst in
  float_of_int sum /. float_of_int (List.length lst)

let median lst =
  let sorted = List.sort compare lst in
  let len = List.length sorted in
  if len mod 2 = 0 then
    let mid1 = List.nth sorted (len/2 - 1) in
    let mid2 = List.nth sorted (len/2) in
    (float_of_int (mid1 + mid2)) /. 2.0
  else
    float_of_int (List.nth sorted (len/2))

let mode lst =
  let freq_map =
    List.fold_left
      (fun acc x ->
         let count = try List.assoc x acc with Not_found -> 0 in
         (x, count + 1) :: List.remove_assoc x acc)
      [] lst
  in
  let max_freq = List.fold_left (fun acc (_, freq) -> max acc freq) 0 freq_map in
  List.fold_left (fun acc (k, v) -> if v = max_freq then k :: acc else acc) [] freq_map

let print_list lst =
  Printf.printf "Data: [";
  let rec print_elements = function
    | [] -> ()
    | [x] -> Printf.printf "%d" x
    | x :: xs -> Printf.printf "%d, " x; print_elements xs
  in
  print_elements lst;
  Printf.printf "]\n"

let () =
  let data = [23; 45; 12; 67; 89; 23; 45; 78; 34; 56] in
  print_list data;
  Printf.printf "Mean: %.2f\n" (mean data);
  Printf.printf "Median: %.2f\n" (median data);
  let modes = mode data in
  Printf.printf "Mode: ";
  List.iter (Printf.printf "%d ") modes;
  Printf.printf "\n"