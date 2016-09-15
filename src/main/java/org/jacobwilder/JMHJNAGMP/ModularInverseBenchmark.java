package org.jacobwilder.JMHJNAGMP;

import java.math.BigInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

public class ModularInverseBenchmark
{
  private static final Logger logger = LoggerFactory.getLogger(ModularInverseBenchmark.class);
  // Since the results of this are only used for benchmarking and not for any actual
  // encryption we do not care if the results are any good.
  private static final SecureRandom rand = new SecureRandom();

  private static final int MODULUS_SIZE = 3074;
  private static final int FACTOR_SIZE = MODULUS_SIZE * 2;

  @State(Scope.Benchmark) public static class ModularInverseBenchmarkState
  {
    // Instantiate a Paillier object so we can grab the N-squared from it: 

    BigInteger factor1 = null;
    BigInteger modulus = null;

    /**
     * This sets up the state for the two separate benchmarks
     */
    @Setup(org.openjdk.jmh.annotations.Level.Trial) public void setUp()
    {

      factor1 = ModularMultiplyBenchmark.getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
      modulus = getRandomBigIntegerWithBitSet(MODULUS_SIZE, MODULUS_SIZE - 2).pow(2);
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

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithGMP(ModularInverseBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPmodularInverse = true; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.modInverse(allState.factor1, allState.modulus);
    } catch (Exception e)
    {
      logger.error("Exception in testWithGMP!\n" + e);
      System.exit(1);
    }
  }

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithoutGMP(ModularInverseBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPmodularInverse = false; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.modInverse(allState.factor1, allState.modulus);
    } catch (Exception e)
    {
      logger.error("Exception in testWithoutGMP!\n" + e);
      System.exit(1);
    }
  }

}
