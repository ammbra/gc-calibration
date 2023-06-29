package org.acme.prime;

import java.math.BigInteger;

public class PrimeFinder extends Thread {

	long primeMax; //maximum prime number found
	long primeCount; //number of primes found
	long iteration;

	public PrimeFinder(long primeMax, long primeCount, long iteration) {
		this.primeMax = primeMax;
		this.primeCount = primeCount;
		this.iteration = iteration;
	}

	@Override
	public void run() {
		iteration = 0;
		while (true) {
			long count = 0;
			long max = 0;
			for (long i=3; i<=primeMax; i++) {
				if (isPrime(new BigInteger(String.valueOf(i)))) {
					count++;
					max = i;
				}
			}
			primeCount = count;
			primeMax = max;
			iteration++;
		}
	}

	private boolean isPrime(BigInteger number) {
		//check via BigInteger.isProbablePrime(certainty)
		if (!number.isProbablePrime(5))
			return false;

		//check if even
		BigInteger two = new BigInteger("2");
		if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two)))
			return false;

		//find divisor if any from 3 to 'number'
		for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) { //start from 3, 5, etc. the odd number, and look for a divisor if any
			if (BigInteger.ZERO.equals(number.mod(i))) //check if 'i' is divisor of 'number'
				return false;
		}
		return true;
	}
}
