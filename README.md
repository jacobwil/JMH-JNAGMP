## What is it?

This repository is my effort to benchmark my [pirkAdditions](https://github.com/jacobwil/jna-gmp/tree/pirkAdditions) 
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
    
For the `ModularMultiplyBenchmarkParamSpace` benchmark, I got these results (hand processed and formatted)
```
-------------  --------------  ---------------  ------------------------  ---------------  -------
(FACTOR_SIZE)  (MODULUS_SIZE)  Score GMPMulMod  Score GMPMulModMulModMod  Faster Version   Speedup
1024           1024            135219.089       127481.098                MulMod           0.06
1024           2048            119314.118       112797.858                MulMod           0.06
1024           3074            106926.6         101447.4                  MulMod           0.05
1024           4096            94872.184        89335.507                 MulMod           0.06
1024           8192            66856.722        65039.01                  MulMod           0.03
2048           1024            92378.083        89010.837                 MulMod           0.04
2048           2048            83649.035        82110.773                 MulMod           0.02
2048           3074            77371.731        75673.047                 MulMod           0.02
2048           4096            70661.721        69994.128                 MulMod           0.01
2048           8192            55613.747        51950.81                  MulMod           0.07
3074           1024            68620.831        69878.844                 MulModMulModMod  0.02
3074           2048            59967.654        58274.661                 MulMod           0.03
3074           3074            60307.771        58134.988                 MulMod           0.04
3074           4096            57112.215        55486.284                 MulMod           0.03
3074           8192            45467.956        44934.575                 MulMod           0.01
4096           1024            53837.839        59091.77                  MulModMulModMod  0.10
4096           2048            47555.603        45500.227                 MulMod           0.05
4096           3074            44543.357        43886.02                  MulMod           0.01
4096           4096            45974.204        45731.662                 MulMod           0.01
4096           8192            38885.574        38738.002                 MulMod           0.00
8192           1024            29376.831        36167.688                 MulModMulModMod  0.23
8192           2048            24613.781        27622.644                 MulModMulModMod  0.12
8192           3074            21892.998        23043.419                 MulModMulModMod  0.05
8192           4096            21286.526        20824.538                 MulMod           0.02
8192           8192            24008.546        23446.881                 MulMod           0.02
-------------  --------------  ---------------  ------------------------  ---------------  -------
```