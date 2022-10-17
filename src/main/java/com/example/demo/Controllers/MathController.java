package com.example.demo.Controllers;

import com.example.demo.Exceptions.DivideByZeroException;
import com.example.demo.Exceptions.UnsupportedMathOperationException;
import com.example.demo.MathOperations;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {

    private final AtomicLong counter = new AtomicLong();
    private MathOperations mathOperations = new MathOperations();
    @RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable(name = "numberOne") String numberOne,
                      @PathVariable(name = "numberTwo") String numberTwo) throws Exception {

        return mathOperations.sum(numberOne, numberTwo);
    }

    @RequestMapping(value = "/subtraction/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double subtraction(@PathVariable(name = "numberOne") String numberOne,
                      @PathVariable(name = "numberTwo") String numberTwo) throws Exception {

        return mathOperations.subtraction(numberOne, numberTwo);
    }

    @RequestMapping(value = "/multiplication/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double multiplication(@PathVariable(name = "numberOne") String numberOne,
                              @PathVariable(name = "numberTwo") String numberTwo) throws Exception {

        return mathOperations.multiplication(numberOne, numberTwo);
    }

    @RequestMapping(value = "/division/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double division(@PathVariable(name = "numberOne") String numberOne,
                              @PathVariable(name = "numberTwo") String numberTwo) throws Exception {

        return mathOperations.division(numberOne, numberTwo);
    }

    @RequestMapping(value = "/average/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double average(@PathVariable(name = "numberOne") String numberOne,
                           @PathVariable(name = "numberTwo") String numberTwo) throws Exception {

        return mathOperations.average(numberOne, numberTwo);
    }

    @RequestMapping(value = "/sqrroot/{number}", method = RequestMethod.GET)
    public Double SqrRoot(@PathVariable(name = "number") String number)
                           throws Exception {

        return mathOperations.SqrRoot(number);
    }


}

