package com.rahul.parkinglot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rahul.parkinglot.model.Car;
import com.rahul.parkinglot.model.Slot;
import com.rahul.parkinglot.exception.ParkingNotAvailableException;

public interface IParkingLot {
	
	/**
     * Creates a parking lot with the given number of slots
     * @param numOfSlots
     */
    public void createParkingLot(int numOfSlots);

    /**
     * Creates a parking lot gate according to distance from slots
     * @param numOfSlots
     * @param slotDistanceFromEntryPoints
     * @param entryPointNumber
     * @return 
     */
    public boolean createParkingGate(int nrOfSlots,String[] slotDistanceFromEntryPoints,int entryPointNumber);
    
    /**
     * Calculates the nearest free slot from entrance and returns the slot number
     * @param c
     * @param entryPoint
     * @return  -1 when the parking slot is full
     */
    public int park(Car c, String entryPoint) throws ParkingNotAvailableException;

    /**
     * Mark the slot booked when the slot is assigned
     * @param slotId
     */
    public void markSlotBooked(int slotId);
    
    /**
     * Frees up a slot and returns the slot number
     * @param slotId
     * @return
     */
    public int leave(int slotId);

    /**
     * Mark slot available when a vehicle leaves
     * @param slotId
     * @return
     */
    public void markSlotAvailable(int slotId);
    
    /**
     *
     * @return  a Map containing the Slot and corresponding Cars. For the entries where the value (car) is null is considered
     * as free
     */
    public Map<Slot, Car> getParkinglotStatus();

    /**
     * returns list of occupied cars for the given color
     * @param color
     * @return
     */
    public List<Car> getRegistrationNumbers(String color);

    /**
     * Returns the Slot where the car is parked
     * @param registrationNumber
     * @return
     */
    public Slot getSlotForCar(String registrationNumber);

    /**
     * Returns the list of Slots where the cars of the given color is parked
     * @param color
     * @return
     */
    public List<Slot>  getSlots(String color);
    
}
