package com.pjava.src.components.input;

import org.json.JSONObject;

import com.pjava.src.components.Gate;

/**
 * An input gate.
 * Inputs only have outputs. Functions in {@link Gate} used to edit inputs
 * always do nothing.
 * Input cannot change power state and is always true.
 */
public abstract class Input extends Gate {

    private int schemaInputPort = -1;

    /**
     * Create a new Input with the given amount of outputs.
     *
     * @param busOutput The output buses array.
     */
    public Input(int busOutput) {
        super(1, busOutput);
        super.setPowered(true);
    }

    /**
     * Does nothing in Input and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setInputBus(int[] busSizes) {
        return false;
    }

    /**
     * Does nothing in Input and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setInputBus(int busSize, int index) {
        return false;
    }

    /**
     * Does nothing in Input and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void addInputBus(int size) {
    }

    /**
     * Does nothing in Input and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void removeInputBus(int index) {
    }

    /**
     * Does nothing in Input and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void setPowered(boolean powered) {
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = super.toJson();

        json.put("schemaInputPort", schemaInputPort);

        return json;
    }

    public void setSchemaInputPort(int port){
        schemaInputPort = port;
    }

    public int getSchemaInputPort(){
        return schemaInputPort;
    }

}
