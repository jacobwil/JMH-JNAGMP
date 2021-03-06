package org.jacobwilder.JMHJNAGMP;

import java.math.BigInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class ModularMultiplyBenchmarkParamSpace
{
  private static final Logger logger = LoggerFactory.getLogger(ModularMultiplyBenchmarkParamSpace.class);

  private static final SecureRandom rand = new SecureRandom();

  

  @State(Scope.Benchmark) public static class ModularMultiplyBenchmarkParamSpaceState
  {
    @Param({"1024", "2048", "3074", "4096", "8192", "16384", "32768"})
    int MODULUS_SIZE;

    @Param({"1024", "2048", "3074", "4096", "8192", "16384"})
    int FACTOR_SIZE;

    BigInteger factor1 = null;
    BigInteger factor2 = null;
    BigInteger modulus = null;
    BigInteger smallerModulus = null;

    /**
     * This sets up the state for the two separate benchmarks
     */
    @Setup(org.openjdk.jmh.annotations.Level.Trial) public void setUp()
    {
      try
      {
        // Create a new large numbers with the second highest bit definitely set
        factor1 = getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
        factor2 = getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
        modulus = getRandomBigIntegerWithBitSet(MODULUS_SIZE, MODULUS_SIZE - 2);
      } catch (Exception e)
      {
        System.out.printf("Error!\n");
      }
    }
  }

  public static BigInteger getRandomBigIntegerWithBitSet(int bitlength, int bitset)
  {
    BigInteger toReturn = null;
    do
    {
      toReturn = new BigInteger(bitlength, rand);
    } while (!toReturn.testBit(bitset));
    return toReturn;
  }

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithGMPMulMod(ModularMultiplyBenchmarkParamSpaceState allState)
  {
    IntegerMathAbstraction.useGMPmodularMultiply = true; // Converted from SystemConfiguration.setP…
    IntegerMathAbstraction.mulModSegmentation = false;
    

    try
    {
      IntegerMathAbstraction.modularMultiply(allState.factor1, allState.factor2, allState.modulus);
    } catch (Exception e)
    {
      logger.error("Exception in testWithGMP!\n" + e);
      System.exit(1);
    }
  }


  // @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithoutGMP(ModularMultiplyBenchmarkParamSpaceState allState)
  // {
  //   IntegerMathAbstraction.useGMPmodularMultiply = false; // Converted from SystemConfiguration.setP…
  //   IntegerMathAbstraction.mulModSegmentation = false;
  //
  //
  //   try
  //   {
  //     IntegerMathAbstraction.modularMultiply(allState.factor1, allState.factor2, allState.modulus);
  //   } catch (Exception e)
  //   {
  //     logger.error("Exception in testWithoutGMP!\n" + e);
  //     System.exit(1);
  //   }
  // }

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithGMPMulModMulModMod(ModularMultiplyBenchmarkParamSpaceState allState)
  {
    IntegerMathAbstraction.useGMPmodularMultiply = true; // Converted from SystemConfiguration.setP…
    IntegerMathAbstraction.mulModSegmentation = true;

    try
    {
      IntegerMathAbstraction.modularMultiply(allState.factor1, allState.factor2, allState.modulus);
    } catch (Exception e)
    {
      logger.error("Exception in testWithGMPMulModMulModMod!\n" + e);
      System.exit(1);
    }
  }


  // @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithoutGMPMulModMulModMod(ModularMultiplyBenchmarkParamSpaceState allState)
  // {
  //   IntegerMathAbstraction.useGMPmodularMultiply = false; // Converted from SystemConfiguration.setP…
  //   IntegerMathAbstraction.mulModSegmentation = true;
  //
  //
  //   try
  //   {
  //     IntegerMathAbstraction.modularMultiply(allState.factor1, allState.factor2, allState.modulus);
  //   } catch (Exception e)
  //   {
  //     logger.error("Exception in testWithoutGMPMulModMulModMod!\n" + e);
  //     System.exit(1);
  //   }
  // }

}



