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

public class ExactDivideBenchmark
{
  private static final Logger logger = LoggerFactory.getLogger(ExactDivideBenchmark.class);

  private static final SecureRandom rand = new SecureRandom();

  private static final int FACTOR_SIZE = 3074;

  @State(Scope.Benchmark) public static class ExactDivideBenchmarkState
  {
    BigInteger value1 = null;
    BigInteger value2 = null;
    BigInteger product = null;

    /**
     * This sets up the state for the two separate benchmarks
     */
    @Setup(org.openjdk.jmh.annotations.Level.Trial) public void setUp()
    {
      // Create two very large numbers, multiply them together to get a product that can be divided by either value without a getting a remainder
      value1 = getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
      value2 = getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
      product = value1.multiply(value2);
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

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithGMP(ExactDivideBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPexactDivide = true; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.exactDivide(allState.product, allState.value1);
    } catch (Exception e)
    {
      logger.error("Exception in testWithGMP!\n" + e);
      System.exit(1);
    }
  }

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithoutGMP(ExactDivideBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPexactDivide = false; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.exactDivide(allState.product, allState.value1);
    } catch (Exception e)
    {
      logger.error("Exception in testWithoutGMP!\n" + e);
      System.exit(1);
    }
  }

}
