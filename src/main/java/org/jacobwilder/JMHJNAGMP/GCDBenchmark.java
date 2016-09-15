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

public class GCDBenchmark
{
  private static final Logger logger = LoggerFactory.getLogger(GCDBenchmark.class);

  private static final SecureRandom rand = new SecureRandom();

  private static final int FACTOR_SIZE = 3074;

  @State(Scope.Benchmark) public static class GCDBenchmarkState
  {
    BigInteger value1 = null;
    BigInteger value2 = null;

    /**
     * This sets up the state for the two separate benchmarks
     */
    @Setup(org.openjdk.jmh.annotations.Level.Trial) public void setUp()
    {
      // Create two very large numbers with a common factor.
      BigInteger common = getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2);
      value1 = common.multiply(getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2));
      value2 = common.multiply(getRandomBigIntegerWithBitSet(FACTOR_SIZE, FACTOR_SIZE - 2));
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

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithGMP(GCDBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPgcd = true; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.gcd(allState.value1, allState.value2);
    } catch (Exception e)
    {
      logger.error("Exception in testWithGMP!\n" + e);
      System.exit(1);
    }
  }

  @Benchmark @BenchmarkMode(Mode.Throughput) public void testWithoutGMP(GCDBenchmarkState allState)
  {
    IntegerMathAbstraction.useGMPgcd = false; // Converted from SystemConfiguration.setP…
    

    try
    {
      IntegerMathAbstraction.gcd(allState.value1, allState.value2);
    } catch (Exception e)
    {
      logger.error("Exception in testWithoutGMP!\n" + e);
      System.exit(1);
    }
  }

}
