/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/


import java.util.*;

// line 1 "iteration1.ump"
public class Reservation
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Reservation Attributes
  private String phoneNumber;
  private String email;
  private String resNumber;
  private String date;
  private String time;
  private int numPeople;

  //Reservation Associations
  private Restaurant restaurant;
  private List<Table> tables;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Reservation(String aPhoneNumber, String aEmail, String aResNumber, String aDate, String aTime, int aNumPeople, Restaurant aRestaurant)
  {
    phoneNumber = aPhoneNumber;
    email = aEmail;
    resNumber = aResNumber;
    date = aDate;
    time = aTime;
    numPeople = aNumPeople;
    boolean didAddRestaurant = setRestaurant(aRestaurant);
    if (!didAddRestaurant)
    {
      throw new RuntimeException("Unable to create reservation due to restaurant");
    }
    tables = new ArrayList<Table>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setResNumber(String aResNumber)
  {
    boolean wasSet = false;
    resNumber = aResNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(String aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setTime(String aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumPeople(int aNumPeople)
  {
    boolean wasSet = false;
    numPeople = aNumPeople;
    wasSet = true;
    return wasSet;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }

  public String getResNumber()
  {
    return resNumber;
  }

  public String getDate()
  {
    return date;
  }

  public String getTime()
  {
    return time;
  }

  public int getNumPeople()
  {
    return numPeople;
  }

  public Restaurant getRestaurant()
  {
    return restaurant;
  }

  public Table getTable(int index)
  {
    Table aTable = tables.get(index);
    return aTable;
  }

  public List<Table> getTables()
  {
    List<Table> newTables = Collections.unmodifiableList(tables);
    return newTables;
  }

  public int numberOfTables()
  {
    int number = tables.size();
    return number;
  }

  public boolean hasTables()
  {
    boolean has = tables.size() > 0;
    return has;
  }

  public int indexOfTable(Table aTable)
  {
    int index = tables.indexOf(aTable);
    return index;
  }

  public boolean setRestaurant(Restaurant aRestaurant)
  {
    boolean wasSet = false;
    if (aRestaurant == null)
    {
      return wasSet;
    }

    Restaurant existingRestaurant = restaurant;
    restaurant = aRestaurant;
    if (existingRestaurant != null && !existingRestaurant.equals(aRestaurant))
    {
      existingRestaurant.removeReservation(this);
    }
    restaurant.addReservation(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfTables()
  {
    return 0;
  }

  public boolean addTable(Table aTable)
  {
    boolean wasAdded = false;
    if (tables.contains(aTable)) { return false; }
    tables.add(aTable);
    if (aTable.indexOfReservation(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aTable.addReservation(this);
      if (!wasAdded)
      {
        tables.remove(aTable);
      }
    }
    return wasAdded;
  }

  public boolean removeTable(Table aTable)
  {
    boolean wasRemoved = false;
    if (!tables.contains(aTable))
    {
      return wasRemoved;
    }

    int oldIndex = tables.indexOf(aTable);
    tables.remove(oldIndex);
    if (aTable.indexOfReservation(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aTable.removeReservation(this);
      if (!wasRemoved)
      {
        tables.add(oldIndex,aTable);
      }
    }
    return wasRemoved;
  }

  public boolean addTableAt(Table aTable, int index)
  {  
    boolean wasAdded = false;
    if(addTable(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTableAt(Table aTable, int index)
  {
    boolean wasAdded = false;
    if(tables.contains(aTable))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTables()) { index = numberOfTables() - 1; }
      tables.remove(aTable);
      tables.add(index, aTable);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTableAt(aTable, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Restaurant placeholderRestaurant = restaurant;
    this.restaurant = null;
    if(placeholderRestaurant != null)
    {
      placeholderRestaurant.removeReservation(this);
    }
    ArrayList<Table> copyOfTables = new ArrayList<Table>(tables);
    tables.clear();
    for(Table aTable : copyOfTables)
    {
      aTable.removeReservation(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "email" + ":" + getEmail()+ "," +
            "resNumber" + ":" + getResNumber()+ "," +
            "date" + ":" + getDate()+ "," +
            "time" + ":" + getTime()+ "," +
            "numPeople" + ":" + getNumPeople()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "restaurant = "+(getRestaurant()!=null?Integer.toHexString(System.identityHashCode(getRestaurant())):"null");
  }
}