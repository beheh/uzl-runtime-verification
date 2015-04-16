package de.uni_luebeck.isp.quadratic;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Root computation of quadratic functions.
 *
 * @author Benedict Etzel <benedict.etzel@informatik.uni-luebeck.de>
 *
 */
public class Quadratic {

    static final char FIELD_SEPARATOR = 0x20; // space

    public static String roots(String input) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en_US"));
        DecimalFormat formatter = new DecimalFormat("#.####");
        formatter.setDecimalFormatSymbols(symbols);
        List<String> results = new LinkedList<>();

        try {
            // catch some special cases
            if (input == null) {
                throw new IllegalArgumentException("input cannot be null");
            }

            if (input.isEmpty()) {
                throw new IllegalArgumentException("input cannot be empty");
            }

            // setup parser
            BufferedReader reader = new BufferedReader(new StringReader(input));

            // parse line by line
            String line;
            while ((line = reader.readLine()) != null) {

                try {

                    // parse parameters
                    double[] parsed = new double[3];
                    int k = 0; // number or parsed parameters
                    int n = 0; // position at which the current active number starts
                    boolean numberActive = true;
                    for (int i = 0; i < line.length(); i++) {
                        if (!numberActive && line.charAt(i) != Quadratic.FIELD_SEPARATOR) {
                            // reached a new number
                            numberActive = true;
                            n = i;
                        }
                        if (numberActive) {
                            if (line.charAt(i) == Quadratic.FIELD_SEPARATOR || i + 1 >= line.length()) {
                                numberActive = false;
                                // until end of string (if we've reached the end of the string) or otherwise the FIELD_SEPARATOR at position i
                                int m = (i + 1 >= line.length()) ? i + 1 : i;
                                String toParse = line.substring(n, m);
                                if (!toParse.equals(toParse.trim())) {
                                    // this is to eliminate the QuadraticTest.testSeparator() behaviour due to parseFloat's trim
                                    throw new Exception("parameter has dangling whitespace characters");
                                }
                                Double current = Double.parseDouble(toParse);
                                if (!Double.isFinite(current)) {
									// this is due to parseDouble happily parsing non-real numbers (like the strings "Infinity" and "NaN")
                                    throw new Exception("parameter is not a real number");
                                }
                                parsed[k++] = current; // will throw an IndexOutOfBoundsException once we've reached a 4th parameter
                            }
                        }
                    }

                    if (k != 3) {
                        throw new RuntimeException("invalid parameter count");
                    }

                    double a = parsed[0];
                    double b = parsed[1];
                    double c = parsed[2];

                    // calculate square roots
                    Double[] doubles = new Double[0];
                    if (a == 0) {
                        // trivial non-quadratic solution
                        if(b != 0) {
                            doubles = new Double[1];
                            doubles[0] = (-c / b);
                        }
                        else if(c == 0) {
                            throw new RuntimeException("infinite solutions exist");
                        }
                        // we don't handle 0 0 0 since we can't print all solutions (infinite many) anyway => undefined behaviour
                    } else {
                        double discriminant = Math.pow(b, 2) - (4 * a * c);
                        if (discriminant == 0) {
                            doubles = new Double[1];
                            doubles[0] = (-b / 2 * a);
                        } else if (discriminant > 0) {
                            doubles = new Double[2];
                            doubles[0] = (-b + Math.sqrt(discriminant)) / (2d * a);
                            doubles[1] = (-b - Math.sqrt(discriminant)) / (2d * a);
                        }
                    }

                    // sort in ascending order
                    Arrays.sort(doubles, Double::compare);

                    // format and ensure uniqueness
                    Set formatted = new LinkedHashSet<>();
                    for (Double d : doubles) {
                        // ignore any weird result
                        if (d.isNaN() || d.isInfinite()) {
                            continue;
                        }
                        // LinkedHashSet preserves order and prevents duplicates
                        formatted.add(formatter.format(d));
                    }

                    results.add(String.join(" ", formatted));
                } catch (Exception ex) {
                    results.add("Invalid");
                }
            }
        } catch (Exception ex) {
            results.add("Invalid");
        }
        return String.join("\n", results);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(roots(args[0]));
        }
    }
}
