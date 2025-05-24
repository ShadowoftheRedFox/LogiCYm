package com.pjava.src.UI.components;

import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.Cable;
import com.pjava.src.components.Gate;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public abstract class UIGate extends UIElement {
    /**
     * width of the gate
     */
    private int width = 0;
    /** height of the gate */
    private int height = 0;
    /**
     * List of pins that are the inputs of this gate
     */
    protected List<Pin> inputPins = new ArrayList<>();
    /**
     * List of pins that are the outputs of this gate
     */
    protected List<Pin> outputPins = new ArrayList<>();
    /**
     * List of cables connect to this gate
     */
    private List<UICable> connectedCables = new ArrayList<>();

    @Override
    public void updateVisuals() {
        for (UICable connectedCables : getConnectedCables()) {
            if (connectedCables != null) {
                connectedCables.updateVisuals();
            }
        }
    }

    /**
     * add the given cable to the connected list. it does not connect the cable to
     * this gate, only this gate has a reference of this cable.
     *
     * @param cable the cable to add
     */
    public void addConnectedCable(UICable cable) {
        if (!connectedCables.contains(cable)) {
            // try to fill the first null
            if (connectedCables.indexOf(null) >= 0) {
                connectedCables.set(connectedCables.indexOf(null), cable);
            } else {
                connectedCables.add(cable);
            }
        }
    }

    /**
     * Trouve le câble connecté à un pin donné
     */
    public UICable getCableFromPin(Pin pin) {
        if (pin == null) {
            return null;
        }
        for (UICable cable : connectedCables) {
            // CORRECTION: Comparer avec inputPin ET outputPin
            if (cable != null && (pin.equals(cable.getInputPin()) || pin.equals(cable.getOutputPin()))) {
                return cable;
            }
        }
        return null;
    }

    /**
     * Déconnecte un câble d'input (input pins deviennent bleus)
     */
    public void disconnectInput(UICable cable) {
        if (cable == null) {
            return;
        }

        // Disconnect the cable from this gate
        cable.disconnect(this);

        // Remove the cable from the connected cables list
        if (connectedCables.contains(cable)) {
            // Recolor pins: input pins deviennent bleus, output pins deviennent rouges
            if (cable.getInputPin() != null) {
                cable.getInputPin().setColor(Color.BLUE); // Input pin = bleu
            }
            if (cable.getOutputPin() != null) {
                cable.getOutputPin().setColor(Color.RED); // Output pin = rouge
            }

            // Remove the cable visually from the container
            Node cableNode = cable.getNode();
            if (cableNode != null && cableNode.getParent() != null) {
                ((Pane) cableNode.getParent()).getChildren().remove(cableNode);
            }

            // Remove the cable completely from the list
            connectedCables.remove(cable);
        }
    }

    /**
     * Déconnecte un câble d'output (output pins deviennent rouges)
     */
    public void disconnectOutput(UICable cable) {
        if (cable == null) {
            return;
        }

        // Disconnect the cable from this gate
        cable.disconnect(this);

        // Remove the cable from the connected cables list
        if (connectedCables.contains(cable)) {
            // CORRECTION: Garder la logique cohérente
            // Input pins = bleu, Output pins = rouge
            if (cable.getInputPin() != null) {
                cable.getInputPin().setColor(Color.BLUE); // Input pin = bleu
            }
            if (cable.getOutputPin() != null) {
                cable.getOutputPin().setColor(Color.RED); // Output pin = rouge
            }

            // Remove the cable visually from the container
            Node cableNode = cable.getNode();
            if (cableNode != null && cableNode.getParent() != null) {
                ((Pane) cableNode.getParent()).getChildren().remove(cableNode);
            }

            // Remove the cable completely from the list
            connectedCables.remove(cable);
        }
    }

    /**
     * Disconnect all cables from this gate, and the gate from every cable
     */
    public void disconnect() {
        // Créer une copie pour éviter ConcurrentModificationException
        List<UICable> cablesToDisconnect = new ArrayList<>(connectedCables);

        for (UICable cable : cablesToDisconnect) {
            if (cable != null) {
                // Déterminer si c'est un cable d'input ou d'output pour ce gate
                boolean isInputCable = false;
                for (Pin inputPin : inputPins) {
                    if (inputPin.equals(cable.getOutputPin())) { // Le pin output du câble se connecte à notre input
                        isInputCable = true;
                        break;
                    }
                }

                if (isInputCable) {
                    disconnectInput(cable);
                } else {
                    disconnectOutput(cable);
                }
            }
        }

        // Clear the list to remove any remaining null references
        connectedCables.clear();
    }

    @Override
    protected void dragged(MouseEvent event) {
        // disconnect the gate from everything if it moves
        // TODO move cable with the gate instead of disconnecting
        disconnect();
        super.dragged(event);
    }

    // #region Getters
    /**
     * @return return the width of the gate
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return return the height of the gate
     */
    public int getHeight() {
        return height;
    }

    /**
     * returns the list of pins input of the gate
     *
     * @return (list of input pins)
     */
    public List<Pin> getInputPins() {
        return inputPins;
    }

    /**
     * used to get all the cable connecte to the gate sended
     *
     * @return List of Cable connected to a Gate
     */
    public List<Cable> getConnectedLogicCables() {
        List<Cable> cablesLogic = new ArrayList<>();
        for (UICable cable : connectedCables) {
            cablesLogic.add((Cable) cable.getLogic());
        }
        return cablesLogic;
    }

    @Override
    public Gate getLogic() {
        return (Gate) super.getLogic();
    }

    /**
     * to get a single pinInput
     *
     * @param index which one do you want (0 to N)
     * @return the pin selected
     */
    public Pin getPinInput(int index) {
        return inputPins.get(index);
    }

    /**
     * to get a single pinOutput
     *
     * @param index which one do you want (from 0 to N)
     * @return the Output pin selected
     */
    public Pin getPinOutput(int index) {
        return outputPins.get(index);
    }
    // #endregion

    // #region Setters
    /**
     * to modifie the width
     *
     * @param width (width of the gate you want to put)
     */
    protected void setWidth(int width) {
        this.width = width;
    }

    /**
     * to modifie the height
     *
     * @param height (height of the gate you want to put)
     */
    protected void setHeight(int height) {
        this.height = height;
    }
    // #endregion

    /**
     * returns the list of pins output
     *
     * @return ( list of output pins)
     */
    public List<Pin> getOutputPins() {
        return outputPins;
    }

    /**
     * in the name
     *
     * @param cable an UICable
     */
    public void removeConnectedCable(UICable cable) {
        connectedCables.remove(cable);
    }

    /**
     * to get the UICable connected
     *
     * @return a UICable
     */
    public List<UICable> getConnectedCables() {
        return connectedCables;
    }
}
