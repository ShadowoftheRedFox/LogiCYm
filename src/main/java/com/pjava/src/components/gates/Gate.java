package com.pjava.src.components.gates;

import com.pjava.src.config.States;

/**
 * The base class of any logic gate.
 */
public abstract class Gate {
    /**
     * Whether of nor the input should be only 1, and not the fixed 2.
     * Set only when instanciating the class.
     */
    private boolean inputAlone = false;

    /**
     * The state of any flow.
     * The input state is fixed to 2 inputs, 1 if inputAlone is true.
     * It can have 3 modes:
     * - On (1)
     * - Off (0)
     * - Unpowered (-1)
     *
     * Default is both at -1.
     */
    private States[] inputState = { States.Unpowered, States.Unpowered };

    /**
     * The state of any flow.
     * It can have 3 modes:
     * - On (1)
     * - Off (0)
     * - Unpowered (-1)
     *
     * Default is -1.
     */
    private States outputState;

    // This constructor enable implicit constructor in extending class.
    public Gate() {
    };

    // This constructor is the one if we want to precise the inputAlone variable.
    public Gate(boolean isAlone) {
        inputAlone = isAlone;
    }

    /**
     * This function is called when inputState changes.
     * It should updates the outputState accordingly.
     */
    public abstract void updateState();

    // #region Getters
    /**
     * The state of any flow.
     * It can have 3 modes:
     * - Unpowered
     * - Low
     * - High
     *
     * Default is Unpowered.
     */
    public States[] getInputState() {
        return inputState;
    }

    /**
     * The state of any flow.
     * The input state is fixed to 2 inputs, 1 if inputAlone is true.
     * It can have 3 modes:
     * - Unpowered
     * - Low
     * - High
     *
     * Default is both at Unpowered.
     */
    public States getOutputState() {
        return outputState;
    }

    /**
     * Whether of nor the input should be only 1, and not the fixed 2.
     * Set only when instanciating the class.
     */
    public boolean getInputAlone() {
        return inputAlone;
    }
    // #endregion

    // #region Setters
    /**
     * Change the input of the gate. By default, propagate changes.
     *
     * @param inputState_1 The state of the first input. It is required.
     * @param inputState_2 The state of the second input. It is required depending
     *                     of the state of {@link #getInputAlone}
     */
    public void setInputState(States inputState_1, States inputState_2) {
        // change then update
        inputState = new States[] { inputState_1, inputState_2 };
        updateState();
    }

    public void setOutputState(States outputState) {
        this.outputState = outputState;
        // TODO tell output listeners that this has changed, or change input/output as
        // cable class to we can chain functions calls
    }
    // #endregion
}
