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

    /**
     * assigned input port in a schema
     * -1 = unset
     */
    private int schemaInputPort = -1;

    /**
     * Create a new Input with the given amount of outputs.
     *
     * @param busOutput The output buses array.
     */
    public Input(int[] busOutput) {
        super(new int[] {}, busOutput);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("schemaInputPort", schemaInputPort);

        return json;
    }

    /**
     * FIXME javadoc
    * Sets the schema input port index for this connection.
    *
    * @param port the index of the schema's input port (must be >= 0)
    */
    public void setSchemaInputPort(int port) {
        schemaInputPort = port;
    }

    /**
     * FIXME javadoc
     * Gets the schema input port index for this connection.
     *
     * @return the index of the schema's input port, or the default value if not set
     */
    public int getSchemaInputPort() {
        return schemaInputPort;
    }

}
