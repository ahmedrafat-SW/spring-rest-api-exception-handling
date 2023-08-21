package com.dev.springbootesssentials.controller;

import com.dev.springbootesssentials.exception.DivisionByZeroException;
import com.dev.springbootesssentials.exception.FibonacciInputException;
import com.dev.springbootesssentials.exception.FibonacciOutOfRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "fibonacci")
public class FibonacciController {

    @GetMapping(path = "findNumber")
    public ResponseEntity<String> createFibonacciNumber(@RequestParam int position) {
        int fib;
        try {
            fib = fibonacci(position);
        }catch (FibonacciOutOfRangeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(String.valueOf(fib));
    }

    @GetMapping("fileName")
    public ResponseEntity<String> getFibonacciSequence(@RequestParam String filename){
        String sequence;
        try {
            sequence = getSequence(filename);
        }catch (FileNotFoundException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body("The Specified File Not found, Check your request " + e.getMessage());
        }
        return ResponseEntity.ok(sequence);
    }

    @PostMapping(path = "createSequence")
    public ResponseEntity<String> createFibonacciSequence(@RequestParam String n) throws IOException{
        List<Integer> sequence ;
        try{
            sequence= createSequence(n, null);
        }catch (FibonacciInputException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body("NullPointerException:(");
        }
        return ResponseEntity.ok(storeSequence(sequence));
    }

    @GetMapping(path = "findRatio")
    public ResponseEntity<String> getFibonacciRatio(@RequestParam int n){
        int curr;
        int prev;
        double ratio;
        try{
            curr= fibonacci(n);
            prev= fibonacci(n-1);
            ratio= (double) curr / prev;
        }catch (ArithmeticException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("0");
        }catch (FibonacciOutOfRangeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        if (prev <= 0){
            return ResponseEntity.ok("0");
        }
        return ResponseEntity.ok(String.valueOf(ratio));
    }

    private String storeSequence(List<Integer> sequence) throws IOException {
        String fileName = "fibonacci.txt";
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        writer.write(sequence.toString());
        writer.flush();
        writer.close();
        return fileName;
    }

    private List<Integer> createSequence(String str, List<Integer> sequence) throws FibonacciInputException{
        int n;
        try {
           n = Integer.parseInt(str);
        }catch (NumberFormatException e){
            throw new FibonacciInputException("Invalid Input. Please enter a valid number");
        }
        if (sequence == null)
            sequence = new ArrayList<>();
        sequence.add(0);
        int prev= 0;
        int curr= 1;
        int index= 1;
        int next;
        while (index <= n){
            sequence.add(curr);
            next = prev + curr;
            prev = curr;
            curr = next;
            index ++;
        }

        return sequence;
    }

    private String getSequence(String filename) throws FileNotFoundException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        return  bufferedReader.lines().collect(Collectors.joining());
    }

    private int fibonacci(int position) throws FibonacciOutOfRangeException {
        if (position <= 1 )
            return position;
        if(position > 9){
            throw new FibonacciOutOfRangeException("The enter number "
                    +position+" is too large. Please enter number in range of 1 to 9");
        }
        return fibonacci(position -1) + fibonacci(position -2);
    }

}
