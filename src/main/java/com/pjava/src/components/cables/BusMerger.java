package com.pjava.src.components.cables;

import java.util.BitSet;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;
import com.pjava.src.errors.BusSizeException;

/**
 * Merge mutiple cables of different bus size into one cable of the sum of all
 * buses. The bus output size is calculated when instanciating, or when changing
 * inputs.
 */
public class Merger extends Gate {

    /**
     * Create a default merger, 2 input bus of size 1.
     *
     * @throws Exception Throws if inputs is null, empty or if the sum of each bus
     *                   is greater than 32.
     */
    public Merger() throws Exception {
        this(new int[] { 1, 1 });
    }

    /**
     * Create a new merger, with the given input buses.
     *
     * @param busInput The list of bus sizes.
     * @throws Exception Throws if inputs is null, empty or if the sum of each bus
     *                   is greater than 32.
     */
    public Merger(int[] busInput) throws Exception {
        // placholder buses
        super(new int[] { 1 }, new int[] { 1 });
        // setup true buses
        setInputBus(busInput);
    }

    /**
     * Get the sum of the provided array of buses, throwing exceptions if the
     * resulting bus size is invalid.
     *
     * @param array The array of bus sizes.
     * @return The value of the sum of all bus sizes.
     * @throws BusSizeException     Throws if the result is not a valid bus size.
     * @throws NullPointerException Throws if the given array is null.
     */
    private int sumOfIntArray(int[] array) throws BusSizeException, NullPointerException {
        if (array == null) {
            throw new NullPointerException("Expected array to be a int[], received null");
        }

        int sum = 0;
        for (int i : array) {
            sum += i;
        }

        if (BusSizeException.isBusSizeException(sum)) {
            throw BusSizeException.fromName("sum", sum);
        }

        return sum;
    }

    /**
     * Return a concatenation of all input cable state. The input cable at the index
     * 0 is the weak bit (the right most bit), and the last input cable is the
     * stongest bit (the left most bit).
     *
     * <p>
     * Exemple:
     * </p>
     * Inputs: 0010, 10
     * Output: 100010
     */
    @Override
    public BitSet getState() {
        if (!getPowered()) {
            return null;
        }

        BitSet result;
        try {
            result = new BitSet(sumOfIntArray(getInputBus()));
        } catch (NullPointerException | BusSizeException e) {
            throw new Error(e);
        }

        int shift = 0;
        for (Cable cable : getInputCable()) {
            BitSet cableState = cable.getState();
            for (int i = 0; i < cable.getBusSize(); i++) {
                result.set(shift, cableState.get(i));
                shift++;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc} Disconnect the output cable since the output bus size changes.
     * Change the output bus to the sum of the input bus size.
     */
    @Override
    public void addInputBus(int size) throws BusSizeException {
        super.addInputBus(size);
        // check new size is valid
        sumOfIntArray(getInputBus());
        // since there are changes, disconnect the output cable
        disconnect(getOutputCable().get(0));
        // we update the output size
        setOutputBus(new int[] { sumOfIntArray(this.inputBus) });
    }

    /**
     * {@inheritDoc} Disconnect the output cable since the output bus size changes.
     * Change the output bus to the sum of the input bus size.
     */
    @Override
    public void removeInputBus(int index) throws IllegalArgumentException {
        super.removeInputBus(index);
        // since there are changes, disconnect the output cable
        disconnect(getOutputCable().get(0));
        // we update the output size
        try {
            setOutputBus(new int[] { sumOfIntArray(this.inputBus) });
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * {@inheritDoc} Disconnect the output cable if the output bus size changes.
     * Change the output bus to the sum of the input bus size.
     */
    @Override
    public boolean setInputBus(int[] inputBus) throws BusSizeException, NullPointerException {

        // if no input (yet), because of initialization, skip it
        if (getOutputBus().length == 0) {
            return false;
        }

        // check new size is valid, and check if output change at all
        if (sumOfIntArray(inputBus) != getOutputBus()[0]) {
            // since there are changes, disconnect the output cable
            disconnect(getOutputCable().get(0));
            System.out.println("output isn't the same size as input sum! Output: " + getOutputBus()[0] + " sum: "
                    + sumOfIntArray(inputBus));
            System.out.println("Changing the output size to the input sum..");
            setOutputBus(new int[] { sumOfIntArray(inputBus) });

            // return false;
        }
        return super.setInputBus(inputBus);
    }

    /**
     * {@inheritDoc} Given array should be of length equal to 1.
     */
    @Override
    protected boolean setOutputBus(int[] outputBus) throws BusSizeException, NullPointerException {
        if (outputBus != null && outputBus.length != 1) {
            throw new BusSizeException("Merger can only have one output, received: " + outputBus.length);
        }
        return super.setOutputBus(outputBus);
    }

    /**
     * Use {@link #setOutputBus(int[])} instead.
     *
     * @deprecated
     */
    @Override
    protected void removeOutputBus(int index) throws IllegalArgumentException {
        throw new IllegalArgumentException("Merger can only have one output");

    }

    /**
     * Use {@link #setOutputBus(int[])} instead.
     *
     * @deprecated
     */
    @Override
    protected void addOutputBus(int size) throws BusSizeException {
        throw new BusSizeException("Merger can only have one output");
    }
}
