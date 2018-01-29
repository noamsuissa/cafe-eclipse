/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 13 "iteration1.ump"
public class Restaurant
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Restaurant Attributes
  private int numTables;
  private int numSeats;
  private int floorLayout;

  //Restaurant Associations
  private List<Reservation> reservations;
  private Menu menu;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Restaurant(int aNumTables, int aNumSeats, int aFloorLayout, Menu aMenu)
  {
    numTables = aNumTables;
    numSeats = aNumSeats;
    floorLayout = aFloorLayout;
    reservations = new ArrayList<Reservation>();
    if (aMenu == null || aMenu.getRestaurant() != null)
    {
      throw new RuntimeException("Unable to create Restaurant due to aMenu");
    }
    menu = aMenu;
  }

  public Restaurant(int aNumTables, int aNumSeats, int aFloorLayout, String aCoursesForMenu)
  {
    numTables = aNumTables;
    numSeats = aNumSeats;
    floorLayout = aFloorLayout;
    reservations = new ArrayList<Reservation>();
    menu = new Menu(aCoursesForMenu, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumTables(int aNumTables)
  {
    boolean wasSet = false;
    numTables = aNumTables;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumSeats(int aNumSeats)
  {
    boolean wasSet = false;
    numSeats = aNumSeats;
    wasSet = true;
    return wasSet;
  }

  public boolean setFloorLayout(int aFloorLayout)
  {
    boolean wasSet = false;
    floorLayout = aFloorLayout;
    wasSet = true;
    return wasSet;
  }

  public int getNumTables()
  {
    return numTables;
  }

  public int getNumSeats()
  {
    return numSeats;
  }

  public int getFloorLayout()
  {
    return floorLayout;
  }

  public Reservation getReservation(int index)
  {
    Reservation aReservation = reservations.get(index);
    return aReservation;
  }

  public List<Reservation> getReservations()
  {
    List<Reservation> newReservations = Collections.unmodifiableList(reservations);
    return newReservations;
  }

  public int numberOfReservations()
  {
    int number = reservations.size();
    return number;
  }

  public boolean hasReservations()
  {
    boolean has = reservations.size() > 0;
    return has;
  }

  public int indexOfReservation(Reservation aReservation)
  {
    int index = reservations.indexOf(aReservation);
    return index;
  }

  public Menu getMenu()
  {
    return menu;
  }

  public static int minimumNumberOfReservations()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Reservation addReservation(String aPhoneNumber, String aEmail, String aResNumber, String aDate, String aTime, int aNumPeople)
  {
    return new Reservation(aPhoneNumber, aEmail, aResNumber, aDate, aTime, aNumPeople, this);
  }

  public boolean addReservation(Reservation aReservation)
  {
    boolean wasAdded = false;
    if (reservations.contains(aReservation)) { return false; }
    Restaurant existingRestaurant = aReservation.getRestaurant();
    boolean isNewRestaurant = existingRestaurant != null && !this.equals(existingRestaurant);
    if (isNewRestaurant)
    {
      aReservation.setRestaurant(this);
    }
    else
    {
      reservations.add(aReservation);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeReservation(Reservation aReservation)
  {
    boolean wasRemoved = false;
    //Unable to remove aReservation, as it must always have a restaurant
    if (!this.equals(aReservation.getRestaurant()))
    {
      reservations.remove(aReservation);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addReservationAt(Reservation aReservation, int index)
  {  
    boolean wasAdded = false;
    if(addReservation(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveReservationAt(Reservation aReservation, int index)
  {
    boolean wasAdded = false;
    if(reservations.contains(aReservation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfReservations()) { index = numberOfReservations() - 1; }
      reservations.remove(aReservation);
      reservations.add(index, aReservation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addReservationAt(aReservation, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (reservations.size() > 0)
    {
      Reservation aReservation = reservations.get(reservations.size() - 1);
      aReservation.delete();
      reservations.remove(aReservation);
    }
    
    Menu existingMenu = menu;
    menu = null;
    if (existingMenu != null)
    {
      existingMenu.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "numTables" + ":" + getNumTables()+ "," +
            "numSeats" + ":" + getNumSeats()+ "," +
            "floorLayout" + ":" + getFloorLayout()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "menu = "+(getMenu()!=null?Integer.toHexString(System.identityHashCode(getMenu())):"null");
  }
}