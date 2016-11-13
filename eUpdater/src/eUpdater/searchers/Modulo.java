package eUpdater.searchers;

import java.math.BigInteger;

/**
 * Created by Kyle on 11/21/2015.
 */
public class Modulo {
    public final BigInteger quotient;
    public final int bits;

    public Modulo(BigInteger quotient, int bits) {
        this.quotient = quotient;
        this.bits = bits;
    }

    public BigInteger compute() {
        try {
            BigInteger shift = BigInteger.ONE.shiftLeft(bits);
            return quotient.modInverse(shift);
        } catch (ArithmeticException e) {
            return null;
        }
    }

    public boolean isValid() {
        return compute() != null;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Modulo))
            return false;
        Modulo mod = (Modulo) object;
        return mod.quotient.intValue() == quotient.intValue();
    }
}
