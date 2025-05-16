package com.pjava.src.components.output;

import java.util.ArrayList;

import com.pjava.src.components.Cable;
import com.pjava.src.utils.Utils;

/**
 * An specific output gate that display the value of the input cable in the
 * wanted base (2, 8, 10, 16).
 * It doesn't have any outputs and has one input of the desired size. The value
 * displayed by this gate
 * will be capped out by the bus size of the input cable. For example, if the
 * cable bus size chosen is 2, the maximum output
 * value in base 10 will be capped out at 3. The base can be set when
 * instantiating the class or later.
 *
 * @see #outputBase
 */
public class Display extends Output {
    /**
     * The number base that is displayed: 2, 8, 10, 16.
     */
    private int outputBase = 2;

    /**
     * Create a new display with default base of 2 and maximum cable bus size of 1.
     *
     * @throws Exception Should not throw.
     * @see #Display(int, int)
     */
    public Display() throws Exception {
        this(1, 2);
    }

    /**
     * Create a new display with an user defined base and maximum cable bus size.
     * Throws an exception if the base value is smaller than 2 or greater than 16.
     *
     * @param inputSize The size of the input of the display.
     * @param base      The radix used to convert base number.
     * @throws Exception Throws if base is not between 2 and 16 included.
     */
    public Display(int inputSize, int base) throws Exception {
        super(new int[] { inputSize });
        setBaseOutput(base);
    }

    /**
     * Return the value of the cable value matching the selected base format.
     *
     * @return The string equivalent of the cable value in the selected base format
     *         (Base 2: 3 returns "11" and 1 returns "01").
     */
    public String getOutput() {
        final int maxOutput = Utils.pow2(this.getInputBus()[0]) - 1;
        Integer outputNumber = 0;
        ArrayList<Cable> inputs = getInputCable();
        Cable cable = inputs.get(0);

        for (int i = 0; i < cable.getBusSize(); i++) {
            outputNumber += (cable.getState(i) ? 1 << i : 0);
        }

        if (outputNumber > maxOutput) {
            System.out.println("NUMBER OVERFLOW " + outputNumber + ">" + maxOutput);
            outputNumber = maxOutput;
        }

        String output = Integer.toString(outputNumber, outputBase);
        final int bufferLength = output.length();
        final int maxLength = Integer.toString(maxOutput, outputBase).length();

        if (output.length() < Integer.toString(maxOutput, outputBase).length()) {
            for (int i = 0; i < maxLength - bufferLength; i++) {
                output = "0" + output;
            }
        }

        return output;
    }

    // #region Setters
    /**
     * Set the base of this display to the desired base.
     *
     * @param base The radix used to convert base number.
     * @throws Exception Throws if base is not between 2 and 16 included.
     */
    public void setBaseOutput(int base) throws Exception {
        if (base < 2 || base > 16) {
            throw new Exception("Base is out of bound");
        }
        this.outputBase = base;
    }
    // #endregion

    // #region Getters
    /**
     * Returns the base of this display.
     *
     * @return The output base.
     */
    public int getBaseOutput() {
        return outputBase;
    }
    // #endregion
}
