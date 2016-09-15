## What is it?

This repository is my effort to benchmark my [pirkAddtiions](https://github.com/jacobwil/jna-gmp/tree/pirkAdditions) 
branch of [Square's JNA-GMP](https://github.com/square/jna-gmp) as part of my [pull request]
(https://github.com/square/jna-gmp/pull/17). 

## How do I run it?
After pulling down and `cd`ing into the base folder of the repository, to compile: 
    mvn clean install 

To run all of the benchmarks:
	java -jar target/benchmarks.jar

To run only a single benchmark: 
    java -jar target/benchmarks.jar org.jacobwilder.JMHJNAGMP.ModularMultiplyBenchmark

To reduce the number of forks (how many sets of 20 reps) to 4 (for example): 
	java -jar target/benchmarks.jar -f 4

## What results do you get?
On September 15, 2016 I got these results: 

    Benchmark                                                Mode  Cnt      Score     Error  Units
    ModularMultiplyBenchmark.testWithGMPMulMod              thrpt  200  34053.772 ± 828.238  ops/s
    ModularMultiplyBenchmark.testWithGMPMulModMulModMod     thrpt  200  33004.568 ± 621.534  ops/s
    ModularMultiplyBenchmark.testWithoutGMP                 thrpt  200  13345.274 ± 372.364  ops/s
    ModularMultiplyBenchmark.testWithoutGMPMulModMulModMod  thrpt  200  13382.723 ± 180.021  ops/s