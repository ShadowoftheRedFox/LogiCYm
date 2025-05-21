package com.pjava.src.components.input;

import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.components.output.Display;
import com.pjava.src.utils.Utils;

/**
 * An specific input gate that display the his numerical value in the
 * wanted base (2, 8, 10, 16).
 * It has one output of the desired size.
 * The value displayed by this gate will be capped out by the bus size of the
 * cable.
 * For example, if the cable bus size chosen is 2, the maximum output
 * value in base 10 will be capped out at 3.
 * The base can be set when instantiating the class or later.
 *
 * @see #inputBase
 */
public class Numeric extends Input {
    /**
     * Value of the gate in the selected base
     */
    private String value = "";

    /**
     * The number base that is displayed: 2, 8, 10, 16.
     */
    private int inputBase = 2;

    /**
     * Create a new display with default base of 2 and maximum cable bus size of 1.
     *
     * @throws Exception Should not throw.
     * @see Display
     */
    public Numeric() throws Exception {
        this(1, 2);
    }

    /**
     * Create a new display with default value 0
     *
     * @param outputSize The size of the input of the display.
     * @param base       The radix used to convert base number.
     * @throws Exception Throws if base is not between 2 and 16 included.
     */
    public Numeric(int outputSize, int base) throws Exception {
        this(outputSize, base, "0");
    }

    /**
     * Create a new display with an user defined base and maximum cable bus size.
     * Throws an exception if the base value is smaller than 2 or greater than 16.
     *
     * @param outputSize   The size of the input of the display.
     * @param base         The radix used to convert base number.
     * @param initialValue String of a value formated to fit the base.
     * @throws Exception Throws if base is not between 2 and 16 included.
     */
    public Numeric(int outputSize, int base, String initialValue) throws Exception {
        super(new int[] { outputSize });
        setInputBase(base);
        setInputValue(initialValue);
    }

    // #region Setters

    /**
     * Set the base of this numeric gate to the desired base.
     *
     * @param base The radix used to convert base number.
     * @throws Exception Throws if base is not between 2 and 16 included.
     */
    public final void setInputBase(int base) throws Exception {
        if (base < 2 || base > 16) {
            throw new Exception("Base must be between 2 and 16");
        }
        this.inputBase = base;
    }

    /**
     * Set the Numeric state and value attribute to the newValue.
     * If the newValue overflow, it is set to the max value
     * allowed by the size of the bus.
     *
     * @param newValue Must be formated in the same base as Numeric.inputBase.
     */
    public final void setInputValue(String newValue) {
        final int maxInput = Utils.pow2(this.getOutputBus()[0]) - 1;
        Integer inputNumber = null;
        try {
            inputNumber = Integer.parseInt(newValue, this.inputBase);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }

        if (inputNumber > maxInput) {
            System.out.println("NUMBER OVERFLOW " + inputNumber + ">" + maxInput);
            inputNumber = maxInput;
        }

        String newValueFormated = Integer.toBinaryString(inputNumber);
        final int bufferLength = newValueFormated.length();
        final int maxLength = Integer.toBinaryString(maxInput).length();

        if (newValueFormated.length() < Integer.toString(maxInput, inputBase).length()) {
            for (int i = 0; i < maxLength - bufferLength; i++) {
                newValueFormated = "0" + newValueFormated;
            }
        }

        this.state = BitSet.valueOf(newValueFormated.getBytes());
        this.value = newValueFormated;
    }

    // #endregion

    // #region Getters

    /**
     * Returns the base of this numeric gate.
     *
     * @return The input base.
     */
    public int getInputBase() {
        return this.inputBase;
    }

    /**
     * return a copy of the input value.
     *
     * @return
     */
    public String getValue() {
        return String.format(this.value);
    }

    @Override
    public BitSet getState() {
        return (BitSet) state.clone();
    }

    // #endregion

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("base", this.inputBase);

        return json;
    }
}
