package com.rahul.parkinglot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.LinkedHashMap;

import com.rahul.parkinglot.model.Car;
import com.rahul.parkinglot.model.Slot;
import com.rahul.parkinglot.model.EntryPoint;
import com.rahul.parkinglot.IParkingLot;
import com.rahul.parkinglot.exception.ParkingNotAvailableException;

public class ParkingLotMain implements IParkingLot{
	
	
	private Map<Slot, Car> slotCarMap;
    private Map<String, List<Car>> colorCarListMap;
    private Map<String, Slot> registrationNumberSlotMap;

    //This will be used to get the free slot with O(1) time complexity
    PriorityQueue<Integer> freeSlots = new PriorityQueue<Integer>();
    
    LinkedHashMap<EntryPoint, LinkedHashMap<Integer, String>> entryPointSlots = new LinkedHashMap<EntryPoint, LinkedHashMap<Integer, String>>();
    
    ParkingLotMain()
    {
        slotCarMap = new HashMap<Slot, Car>();

        colorCarListMap = new HashMap<String, List<Car>>();

        registrationNumberSlotMap = new HashMap<String, Slot>();
    }

    public void createParkingLot(int numOfSlots) {

        for(int c = 1; c <= numOfSlots; c++)
        {
            slotCarMap.put(new Slot(c), null);

            freeSlots.add(c);
        }
    }
    
    
    public boolean createParkingGate(int nrOfSlots,String[] slotDistanceFromEntryPoints,int entryPointNumber) {
    		LinkedHashMap<Integer, String> gate = new LinkedHashMap<Integer, String>();
	    	for(int i=0;i<slotDistanceFromEntryPoints.length; i++) {
	    		int temp = Integer.parseInt(slotDistanceFromEntryPoints[i].trim());
	    		if(temp > nrOfSlots || temp <=0 || gate.containsKey(temp)) {
	    			return false;
	    		}
	    		gate.put(temp, "f");  
	    	}
	    	
	    	entryPointSlots.put(new EntryPoint(entryPointNumber),gate);
    	
	    	return true;
    //	entryPointSlots[entryPointNumber] = slotDistance;
    
    }
    
    
    public int park(Car c,String entryPoint) throws ParkingNotAvailableException
    {
        //Get the free slot nearest to entrance
    	int entringFrom = Integer.parseInt(entryPoint.trim());
        int slotId = getFreeSlot(entringFrom);
        if(slotId > 0)
        {
        	markSlotBooked(slotId);
        	for(Slot slot : slotCarMap.keySet()){
        		if(slot.getId() == slotId) {
        			slotCarMap.put(slot, c);
        			addToRegistrationNumberSlotMap(c, slot);
        			addToColorCarListMap(c);
        		}
        	}
            
            
            
        }
        else
        {
            throw new ParkingNotAvailableException("Parking full");
        }
        
        //id is -1 when the parkinglot is full
        return slotId;
    }
    
    public void markSlotBooked(int slotId) {
    	
    	for (Map.Entry<EntryPoint,LinkedHashMap<Integer, String>> entry : entryPointSlots.entrySet())  {
    			LinkedHashMap<Integer, String> entringGateData =  entry.getValue();
        		entringGateData.put(slotId, "b");
        		entryPointSlots.put(entry.getKey(), entringGateData);
    		}
    	
    }

    public int leave(int slotId)
    {
    	for(Slot slot : slotCarMap.keySet()){
    		
    		if(slot.getId() == slotId) {
    			Car c = slotCarMap.get(slot);
    			slotCarMap.put(slot, null);
    			removeFromColorCarListMap(c);
    	        removeFromRegistrationNumberSlotMap(c);
    	        markSlotAvailable(slotId);
    		}
    	}
    	
//    	for (Map.Entry<Slot,Car> entry : slotCarMap.entrySet())  {
//    		if(entry.getKey().getId() == slotId) {
//    			Car c = entry.getValue();
//    			slotCarMap.put(entry.getKey(), null);
//    			removeFromColorCarListMap(c);
//    	        removeFromRegistrationNumberSlotMap(c);
//    	        markSlotAvailable(slotId);
//    	        break;
//    		}
//    	} 
    	
        return slotId;
    }

    public void markSlotAvailable(int slotId) {
    	
    	for (Map.Entry<EntryPoint,LinkedHashMap<Integer, String>> entry : entryPointSlots.entrySet())  {
    			LinkedHashMap<Integer, String> entringGateData =  entry.getValue();
        		entringGateData.put(slotId, "f");
        		entryPointSlots.put(entry.getKey(), entringGateData);
    	}
    	
    }
    
    private int getFreeSlot(int entringFrom)
    {
    	for (Map.Entry<EntryPoint,LinkedHashMap<Integer, String>> entry : entryPointSlots.entrySet())  {
    		if(entry.getKey().getId() == entringFrom) {
    			for(Map.Entry<Integer, String> entry1 : entry.getValue().entrySet()) {
    				if(entry1.getValue() == "f") {
    					return entry1.getKey();
    				}
        		}
    		}
    	} 
        return -1;
    }

    public Map<Slot, Car> getParkinglotStatus()
    {
        return this.slotCarMap;
    }

    public List<Car> getRegistrationNumbers(String color) {

        return colorCarListMap.get(color);
    }

    public Slot getSlotForCar(String registrationNumber) {
        if(registrationNumberSlotMap.containsKey(registrationNumber))
            return registrationNumberSlotMap.get(registrationNumber);
        else
            return null;
    }

    public List<Slot> getSlots(String color) {
        List<Car> cars = colorCarListMap.get(color);
        List<Slot> slots = new ArrayList<Slot>();
        for(Car c : cars)
        {
            Slot slot = registrationNumberSlotMap.get(c.getRegistrationNumber());
            slots.add(slot);
        }
        return slots;
    }

    private void addToColorCarListMap(Car car)
    {
        String color = car.getColor();
        List<Car> cars = colorCarListMap.get(color);
        
        if(cars == null)
        {
            cars = new ArrayList<Car>();
            cars.add(car);
            colorCarListMap.put(color, cars);
        }
        else
        {
            cars.add(car);
            colorCarListMap.put(color, cars);
        }
        
    }
    private void removeFromColorCarListMap(Car car)
    {
        String color = car.getColor();
        List<Car> cars = colorCarListMap.get(color);
        if(cars == null)
        {

        }
        else
        {
            cars.remove(car);
            colorCarListMap.put(color, cars);
        }
    }

    private void addToRegistrationNumberSlotMap(Car car, Slot s)
    {
        registrationNumberSlotMap.put(car.getRegistrationNumber(), s);
    }

    private void removeFromRegistrationNumberSlotMap(Car car)
    {
        registrationNumberSlotMap.remove(car.getRegistrationNumber());
    }
}
