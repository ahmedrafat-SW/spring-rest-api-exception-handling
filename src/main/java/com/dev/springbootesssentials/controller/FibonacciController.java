package com.dev.springbootesssentials.controller;

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

    @GetMapping(path = "findnumber")
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

    @GetMapping("filename")
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

    @PostMapping(path = "createsequence")
    public ResponseEntity<String> createFibonacciSequence(@RequestParam int n) throws IOException{
        List<Integer> sequence = createSequence(n);
        return ResponseEntity.ok(storeSequence(sequence));
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

    private List<Integer> createSequence(int n) {
        List<Integer> sequence = new ArrayList<>();
        sequence.add(0);
        int prev= 0;
        int curr= 1;
        int index= 1;
        int next = 0;
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
            throw new FibonacciOutOfRangeException("The enter number "+position+" is too large");
        }
        return fibonacci(position -1) + fibonacci(position -2);
    }

}
