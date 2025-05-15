package com.pjava.src.components;

import java.util.ArrayList;
import java.util.BitSet;

import org.json.JSONObject;

import com.pjava.src.errors.BusSizeException;
import com.pjava.src.utils.Cyclic;

import com.pjava.src.components.input.Input;
import com.pjava.src.components.output.Output;

class Circuit{

    /**
    * List of all the gates of a circuit
    */
    private ArrayList<Gate> circuit = new ArrayList<Gate>();

    /**
    * List of all the input gates of a circuit
    */
    private ArrayList<Input> circuitInput = new ArrayList<Input>();

    /**
    * List of all the output gates of a circuit
    */
    private ArrayList<Output> circuitOutput = new ArrayList<Output>();

    public Circuit(){

    }

    public void startSimulation(){

    }

    public void saveCircuit(){

    }

    public void loadCircuit(){

    }
}