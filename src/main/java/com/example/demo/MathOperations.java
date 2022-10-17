package com.example.demo;

import com.example.demo.Exceptions.DivideByZeroException;
import com.example.demo.Exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class MathOperations {

    public boolean isNumeric(String strNumber) {
        if(strNumber == null) return false;

        String number = strNumber.replaceAll(",", ".");

        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public Double convertToDouble(String strNumber){
        if(strNumber == null) return 0D;

        String number = strNumber.replaceAll(",", ".");

        return Double.parseDouble(number);
    }

    public Double sum(String numberOne, String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    public Double subtraction(String numberOne, String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }

    public Double multiplication(String numberOne, String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }

    public Double division(String numberOne, String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }
        if(Double.parseDouble(numberOne) == 0.0 || Double.parseDouble(numberTwo) == 0.0){
            throw new DivideByZeroException("Cannot divide by zero");
        }

        return convertToDouble(numberOne) / convertToDouble(numberTwo);
    }

    public Double average(String numberOne, String numberTwo) throws Exception {

        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        return (convertToDouble(numberOne) + convertToDouble(numberTwo)) / 2;
    }

    public Double SqrRoot(String numberOne) throws Exception {

        if(!isNumeric(numberOne)){
            throw new UnsupportedMathOperationException("Please set a numeric value");
        }

        return Math.sqrt(convertToDouble(numberOne));
    }
}
