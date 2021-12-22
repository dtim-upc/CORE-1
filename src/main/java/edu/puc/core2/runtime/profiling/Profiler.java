package edu.puc.core2.runtime.profiling;

public class Profiler {
    static long compileTime = 0; // ns
    static long enumerationTime = 0; // ns
    static long executionTime = 0; // ns
    static long numberOfMatches = 0; // N
    static long cleanUps = 0; // N

    public static void addCompileTime(long time) {
        compileTime += time;
    }

    public static void addEnumerationTime(long time) {
        enumerationTime += time;
    }

    public static void addExecutionTime(long time) {
        executionTime += time;
    }

    public static void incrementMatches() {
        numberOfMatches++;
    }

    public static void incrementCleanUps() {
        cleanUps++;
    }

    public static long getCompileTime() {
        return Profiler.compileTime;
    }

    public static long getEnumerationTime() {
        return Profiler.enumerationTime;
    }

    public static long getExecutionTime() {
        return Profiler.executionTime;
    }

    public static long getNumberOfMatches() {
        return Profiler.numberOfMatches;
    }

    public static long getCleanUps() {
        return Profiler.cleanUps;
    }

    public static void print(){
//        System.out.print((double)compileTime/1000000000 + ",");
//        System.out.print((double)executionTime/1000000000 + ",");
        System.out.print((double)enumerationTime/1000000000 + ",");
        System.out.print(numberOfMatches);
//        System.err.println("Number of cleanups: " + cleanUps);
    }
}
