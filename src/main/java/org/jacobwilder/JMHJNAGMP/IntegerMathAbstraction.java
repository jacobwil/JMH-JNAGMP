/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jacobwilder.JMHJNAGMP;

import java.math.BigInteger;

import com.squareup.jnagmp.Gmp;

/**
 * The comments in this class are mostly WAAAAAAAY out of whack. Configuration properties do not exist, etc.
 */
public final class IntegerMathAbstraction
{

  public static boolean useGMPmodPow = true,
	                    useGMPConstantTimeMethods = true,
	                    useGMPmodularMultiply = true,
                        useGMPmodularInverse = true,
		                useGMPgcd = true,
		                useGMPexactDivide = true;
  
  public static boolean mulModSegmentation = false;

  /**
   * Performs modPow: {@code (factor1 ^ factor2) mod modulus}
   * <p>
   * This method uses the values of {@code paillier.useGMPmodPow} and {@code paillier.GMPConstantTimeMode}
   * as they were when the class was loaded to decide which implementation of modPow to invoke.
   * <p>
   * These values can be reloaded by invoking static method {@code IntegerMathAbstraction.reloadConfiguration()}
   *
   * @param base
   * @param exponent
   * @param modulus
   * @return {@code (factor1 ^ factor2) mod modulus}
   */
  public static BigInteger modPow(BigInteger base, BigInteger exponent, BigInteger modulus)
  {
    BigInteger result;

    if (useGMPmodPow)
    {
      if (useGMPConstantTimeMethods)
      {
        // Use GMP and use the "timing attack resistant" method
        // The timing attack resistance slows down performance and is not necessarily proven to block timing attacks.
        // Before getting concerned, please carefully consider your threat model
        // and if you really believe that you may need it
        result = Gmp.modPowSecure(base, exponent, modulus);
      }
      else
      {
        // The word "insecure" here does not imply any actual, direct insecurity.
        // It denotes that this function runs as fast as possible without trying to
        // counteract timing attacks. This is probably what you want unless you have a
        // compelling reason why you believe that this environment is safe enough to house
        // your keys but doesn't protect you from another entity on the machine watching
        // how long the program runs.
        result = Gmp.modPowInsecure(base, exponent, modulus);
      }
    }
    else
    {
      // If GMP isn't used, BigInteger's built-in modPow is used.
      // This is significantly slower but has the virtue of working everywhere.
      result = base.modPow(exponent, modulus);
    }

    return result;
  }

  /**
   * Performs modPow: {@code (factor1 ^ factor2) mod modulus}
   * <p>
   * This method uses the values of {@code paillier.useGMPmodPow} and {@code paillier.GMPConstantTimeMode}
   * as they were when the class was loaded to decide which implementation of modPow to invoke.
   * <p>
   * These values can be reloaded by invoking static method {@code IntegerMathAbstraction.reloadConfiguration()}
   *
   * @param base
   * @param exponent
   * @param modulus
   * @return {@code (factor1 ^ factor2) mod modulus}
   */
  public static BigInteger modPow(long base, BigInteger exponent, BigInteger modulus)
  {
    return modPow(BigInteger.valueOf(base), exponent, modulus);
  }

  /**
   * Performs modular multiply: {@code (factor1 * factor2) mod modulus}
   * <p>
   * This method uses the value of {@code paillier.useGMPForModularMultiply} as it was set
   * when the class was loaded to decide which implementation of modular multiplication to invoke.
   * <p>
   * These values can be reloaded by invoking static method {@code IntegerMathAbstraction.reloadConfiguration()}
   *
   * @param factor1 the first factor to the multiplication
   * @param factor2 the second factor to the multiplication
   * @param modulus the modulus to be applied to ({@code factor1 * factor2})
   * @return {@code (factor1 * factor2) mod modulus}
   */
  public static BigInteger modularMultiply(BigInteger factor1, BigInteger factor2, BigInteger modulus)
  {
    BigInteger result;

	if (mulModSegmentation == true)
	{
	    if (useGMPmodularMultiply)
	    {
	      result = Gmp.modularMultiplyAlt(factor1, factor2, modulus);
	    }
	    else
	    {
	      result = factor1.mod(modulus).multiply(factor2.mod(modulus)).mod(modulus);
	    }
	}
	else
	{
	    if (useGMPmodularMultiply)
	    {
	      result = Gmp.modularMultiply(factor1, factor2, modulus);
	    }
	    else
	    {
	      result = factor1.multiply(factor2).mod(modulus);
	    }
	}

    return result;
  }

  /**
   * Performs ({@code dividend} ^ -1) mod {@code modulus}
   * <p>
   * This method uses the value of {@code paillier.useGMPForModularInverse} as it was set
   * when the class was loaded to decide which implementation of modular inversion to invoke.
   *
   * @param dividend
   * @param modulus
   * @return ({@code dividend} ^ -1) mod {@code modulus}
   */
  public static BigInteger modInverse(BigInteger dividend, BigInteger modulus)
  {
    BigInteger result;

    if (useGMPmodularInverse)
    {
      result = Gmp.modInverse(dividend, modulus);
    }
    else
    {
      result = dividend.modInverse(modulus);
    }

    return result;
  }

  /**
   * Performs {@code dividend / divisor}
   * <p>
   * <b>NOTE:</b> This method  is only guaranteed to returns correct answers when the result of
   * {@code dividend / divisor} will have <b>no remainder</b>. Put another way, do not use this method if
   * {@code dividend mod divisor != 0}. Using this method inappropriately may result in wildly incorrect
   * answers. This behavior descends from the optimizations in GMP's {@code mpz_divexact}.
   * <p>
   * This method uses the value of {@code paillier.useGMPForExactDivide} as it was set
   * when the class was loaded to decide which implementation of exact divide to invoke.
   *
   * @param dividend
   * @param divisor
   * @return {@code dividend} / {@code divisor}
   */
  public static BigInteger exactDivide(BigInteger dividend, BigInteger divisor)
  {
    BigInteger result;

    if (useGMPexactDivide)
    {
      result = Gmp.exactDivide(dividend, divisor);
    }
    else
    {
      result = dividend.divide(divisor);
    }

    return result;
  }

  /**
   * Calculates the Greatest Common Divisor of {@code value1} and {@code value2}
   * <p>
   * This method uses the value of {@code paillier.useGMPForGCD} as it was set
   * when the class was loaded to decide which implementation of modular inversion to invoke.
   *
   * @param value1
   * @param value2
   * @return gcd(value1, value2)
   */
  public static BigInteger gcd(BigInteger value1, BigInteger value2)
  {
    BigInteger result;

    if (useGMPgcd)
    {
      result = Gmp.gcd(value1, value2);
    }
    else
    {
      result = value1.gcd(value2);
    }

    return result;
  }


  // For a short period of time these methods exposed GMP implementations. They no longer do.
  // They are kept here to make it easy to try optimizations in the future.

  /**
   * Returns a BigInteger whose value is {@code dividend mod modulus}.
   * <p>
   * This method always uses BigInteger's implementation. 
   *
   * @param dividend
   * @param modulus
   * @return {@code dividend mod modulus}
   */
  public static BigInteger mod(BigInteger dividend, BigInteger modulus)
  {
    return dividend.mod(modulus);
  }

  /**
   * Returns a BigInteger whose value is {@code value1 * value2}
   * <p>
   * This method always uses BigInteger's implementation. 
   *
   * @param value1
   * @param value2
   * @return {@code value1 * value2}
   */
  public static BigInteger multiply(BigInteger value1, BigInteger value2)
  {
    return value1.multiply(value2);
  }

}
