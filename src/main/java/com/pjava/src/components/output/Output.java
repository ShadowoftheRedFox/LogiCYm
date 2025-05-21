package com.pjava.src.components.output;

import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.components.Gate;

/**
 * An output gate.
 * Outputs only have inputs. Functions in {@link Gate} used to edit outputs
 * always do nothing.
 */
public abstract class Output extends Gate {

    /**
     * FIXME javadoc
     */
    private int schemaOutputPort = -1;

    /**
     * Create a new Output with the given amount of outputs.
     *
     * @param busOutput The output buses array.
     */
    public Output(int[] busOutput) {
        super(busOutput, new int[] {});
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setOutputBus(int[] busSizes) {
        return false;
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     *
     * @return Always false.
     */
    @Override
    protected final boolean setOutputBus(int busSize, int index) {
        return false;
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void addOutputBus(int size) {
    }

    /**
     * Does nothing in Output and sub-class.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    protected final void removeOutputBus(int index) {
    }

    /**
     * Since output have no outputs, getState should return null, since they process
     * nothing.
     *
     * <p>
     * In other classes:
     * </p>
     * {@inheritDoc}
     */
    @Override
    public BitSet getState() {
        // no output
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put("schemaOutputPort", schemaOutputPort);

        return json;
    }

    /**
     * Setter for {@link #schemaOutputPort}.
     * BUG no value check
     *
     * @param port The new port.
     */
    public void setSchemaOutputPort(int port) {
        schemaOutputPort = port;
    }

    /**
     * Getter for {@link #schemaOutputPort}.
     *
     * @return The current output port for the schema.
     */
    public int getSchemaOutputPort() {
        return schemaOutputPort;
    }

}
